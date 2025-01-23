// ▐▓█▀▀▀▀▀▀▀▀▀█▓▌░▄▄▄▄▄░
// ▐▓█░░▀░░▀▄░░█▓▌░█▄▄▄█░
// ▐▓█░░▄░░▄▀░░█▓▌░█▄▄▄█░
// ▐▓█▄▄▄▄▄▄▄▄▄█▓▌░█████░
// ░░░░▄▄███▄▄░░░░░█████░


package controllers;

import ai.HumanPlayer;
import controllers.Game.Difficulty;
import java.util.List;
import models.*;
import services.GameUtils;

public class Match {

    public enum MatchPhase {
        START,
        CHOOSING_TRUMP,
        SHOW_ZVANJE,
        PLAYING_ROUNDS,
        END_OF_GAME,
        END_OF_MATCH
    }
    public MatchPhase currentPhase;
    public Game game;
    public int gameCounter;
    public Team team1, team2;
    public List<Player> players;
    public int dealerIndex;
    public static final int WINNING_SCORE = 101; // The score required to win the match
    public HumanPlayer me;
    public Team winner; // Reference to the winning team 
    public boolean startGame;
    public boolean endGame;
    public boolean startRound;

    public Match() {
        this.team1 = null;  // Initialize as placeholders
        this.team2 = null;
        this.players = null;
        this.game = null;
        this.me = null;
        this.dealerIndex = 3;  // Default to player 0 as dealer initially
        this.currentPhase = MatchPhase.START; // Initial phase
        this.gameCounter = 0; // No games played yet
        this.startGame = false; // Phase progression is off initially
        this.endGame = false;
        this.startRound = false;
    }
    

    public void play() {
        switch (currentPhase) {
            case START:
                System.out.println();
                System.out.println("Match phase: START");
                if (team1 == null || team2 == null || players == null) {
                    System.out.println("Error: Ensure teams, players, and difficulty are set");
                    break;
                }
                if(!startGame) { // Ensure startGame boolean must be true before advancing
                    System.out.println("Waiting for game to start. Use startGame(true) to proceed.");
                    break;
                }
                // Initialize the game when all prerequisites are met
                game = new Game(players, team1, team2, dealerIndex);
                game.initializeGame();
                assertInitialTeamState(team1);
                assertInitialTeamState(team2);

                // Reset boolean immediately after it's used
                startGame = false;

                currentPhase = MatchPhase.CHOOSING_TRUMP;
                this.play(); // Proceed to the next phase
                break;
    
            case CHOOSING_TRUMP:
                System.out.println();
                System.out.println("Match phase: CHOOSING_TRUMP");
                if (game.trumpSelection()) { // true = trump selection is over
                    currentPhase = MatchPhase.SHOW_ZVANJE;
                    game.findZvanje();
                } else { // Waiting for user input if human needs to choose trump
                    System.out.println("""
                        Choose Your Trump Suit Option:
                        0. Skip Trump Selection
                        1. Spades
                        2. Hearts
                        3. Diamonds
                        4. Clubs
                        -> Match.pickTrump(int choice)
                        Please make your choice (0-4): 
                        """);
                    break;
                }
                this.play(); // Proceed to the next phase
                break;
    
            case SHOW_ZVANJE:
                System.out.println();
                System.out.println("Match phase: SHOW_ZVANJE");
                if (!startRound) { // Ensure startRound boolean must be true before advancing
                    System.out.println("Waiting for Zvanje acceptance. Use startRound(true) to proceed.");
                    break;
                }
                // Reset boolean immediately after it's used
                startRound = false;
    
                currentPhase = MatchPhase.PLAYING_ROUNDS;
                this.play(); // Proceed to the next phase
                break;
    
            case PLAYING_ROUNDS:
                System.out.println();
                System.out.println("Match phase: PLAYING_ROUNDS");
                if (!game.playRounds()) { // false = still playing rounds
                    System.out.println("Currently playing rounds...");
                    break;
                }
                System.out.println("End of game");
    
                currentPhase = MatchPhase.END_OF_GAME;
                this.play(); // Proceed to the next phase
                break;
    
            case END_OF_GAME:
                game.awardGameVictory();
                System.out.println();
                System.out.println("Game " + gameCounter + " over!");
                if (!endGame) { // Ensure endGame boolean must be true before advancing
                    System.out.println("Waiting for game to end. Use endGame(true) to proceed.");
                    break;
                }
                // Reset boolean immediately after it's used
                endGame = false;
                
                gameCounter++;
                winner = matchWinner();
                if (winner == null) {
                    rotateDealer();
                    resetForNextGame();
                    currentPhase = MatchPhase.START; // Return to the START phase for the next game
                    System.err.println("Start game " + gameCounter + " with Match.play()");
                } else {
                    currentPhase = MatchPhase.END_OF_MATCH;
                }
                this.play(); // Proceed to the next phase
                break;
    
            case END_OF_MATCH:
                handleMatchEnd(winner);
                return; // Exit the play method
    
            default:
                throw new IllegalStateException("Unexpected value: " + currentPhase);
        }
    }
        
    private void assertInitialTeamState(Team team) {
        assert team.getSmalls() == 0 : team.getName() + " small score should be 0 at the start of the match";
        assert team.getBigs() == 0 : team.getName() + " big score should be 0 at the start of the match";
        for (Player player : players) {
            assert player.getHand().isEmpty() : player.getName() + "'s hand should be empty at the start of the match";
        }
    }

    private void resetForNextGame() {
        team1.setSmalls(0);
        team2.setSmalls(0);
        team1.resetWonCards();
        team2.resetWonCards();
        System.out.println("Teams have been reset for the next game.");
    }

    public Team matchWinner() {
        // The match is over if either team reaches or exceeds the winning score
        if (team1.getBigs() >= WINNING_SCORE) {
            return team1;
        } else if (team2.getBigs() >= WINNING_SCORE) {
            return team2;
        }
        return null;
    }

    private void rotateDealer() {
        dealerIndex = (dealerIndex + 1) % 4; // Rotate dealer index among 4 players
    }
    
    private void handleMatchEnd(Team winner) {
        // Additional actions after the match ends (e.g., cleanup, next steps)
        // For example, update player statistics, save match results, etc.
        System.out.println();
        System.out.println("Match is over! Final scores:");
        System.out.println(team1.getName() + ": " + team1.getBigs());
        System.out.println(team2.getName() + ": " + team2.getBigs());
        System.out.println("Match ended. Winner: " + winner.getName());
    }

    // Method to be called from the GUI when the human player accepts the zvanje
    public void startGame(boolean setBoolean) {
        if (currentPhase != MatchPhase.START) {
            System.out.println("Error: You cannot start a game at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        if (team1 == null || team2 == null) {
            System.out.println("Error: Teams are not set. Use setTeamNames(String team1Name, String team2Name) to proceed.");
            return;
        }
        if (players == null) {
            System.out.println("Error: Players and difficulty are not set. Use setDifficultyAndPlayers(Difficulty difficulty) to proceed.");
            return;
        }
        this.startGame = setBoolean;
        this.play(); // Move the game forward once all conditions are met
    }
    

    // Method to be called from the GUI when the human player chooses a trump suit
    public void pickTrump(int choice) {
        if (currentPhase != MatchPhase.CHOOSING_TRUMP) {
            System.out.println("Error: You cannot pick a trump at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        // Proceed with trump selection if phase is valid
        me.trumpChoice(choice); // humanPlayer is your HumanPlayer instance
        // Move the game forward by calling play() to progress to the next phase
        this.play();
    }    

    // Method to be called from the GUI when the human player accepts the zvanje
    public void startRound(boolean setBoolean) {  
        if (currentPhase != MatchPhase.SHOW_ZVANJE) {
            System.out.println("Error: You cannot start a round at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        this.startRound = setBoolean;
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    // Method to be called from the GUI when the human player chooses a card to play
    public void pickCard(int choice) {
        if (currentPhase != MatchPhase.PLAYING_ROUNDS) {
            System.out.println("Error: You cannot play a card at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        // Proceed with card play if phase is valid
        me.cardChoice(choice); // humanPlayer is your HumanPlayer instance
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }    

    public void endGame(boolean setBoolean) { 
        if (currentPhase != MatchPhase.END_OF_GAME) {
            System.out.println("Error: You cannot end the game at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        this.endGame = setBoolean;
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }


    public void initializeGameSettings(Difficulty difficulty, String team1Name, String team2Name,  
                        String playerName, String teamMate, String enemyMate1, String enemyMate2) {
        // Set team names
        this.team1 = new Team(team1Name);
        this.team2 = new Team(team2Name);
        System.out.println("Team names set: " + team1.getName() + " vs " + team2.getName());

        // Initialize players using GameUtils
        this.players = GameUtils.initializePlayers(difficulty, team1, team2, playerName, teamMate, enemyMate1, enemyMate2);

        // Assign the human player
        this.me = (HumanPlayer) this.players.get(0); // Assume Player 0 is the human
        System.out.println("Players initialized: " + players);
    }

    public String getWinnerPrint() {
        return winner == null ? "no winner yet" : winner.getName();
    }

    public Team getWinningTeam() {
        return winner;
    }

    public Team getPlayerTeam() {
        return me.getTeam();
    }

    public void currentPhase() {
        System.out.println("Current phase: " + currentPhase);
    }

    public Round getCurrentRound() {
        return game.currentRound;
    }

    public Game getCurrentGame() {
        return game;
    }

    public MatchPhase getCurrentPhase() {
        return currentPhase;
    }

    public int getGameCounter() {
        return gameCounter;
    }
}