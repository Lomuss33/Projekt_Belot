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
    public final Team team1, team2;
    public final List<Player> players;
    public int dealerIndex;
    public static final int WINNING_SCORE = 501; // The score required to win the match
    public final HumanPlayer me;
    public Team winner; // Reference to the winning team 
    public boolean zvanjeCheck;

    public Match(Difficulty difficulty) {
        this.team1 = new Team("Your Team");
        this.team2 = new Team("Enemy Team");
        players = GameUtils.initializePlayers(difficulty, team1, team2);
        this.dealerIndex = 3; // Start with the last player as the dealer so YOU can play first
        this.me = (HumanPlayer) players.get(0); // Initialize humanPlayer
        this.currentPhase = MatchPhase.START;
        this.gameCounter = 0;
        this.zvanjeCheck = false;
    }

    public void play() {
        switch (currentPhase) {
            case START:
                System.out.println();
                System.out.println("Match phase: START");
                game = new Game(players, team1, team2, dealerIndex);
                game.initializeGame();
                assertInitialTeamState(team1);
                assertInitialTeamState(team2);
                currentPhase = MatchPhase.CHOOSING_TRUMP;
                // Fall through after initializing the game
            case CHOOSING_TRUMP:
                System.out.println();
                System.out.println("Match phase: CHOOSING_TRUMP");
                if (game.trumpSelection()) { // true = trump selection is over
                    currentPhase = MatchPhase.SHOW_ZVANJE;
                    game.findZvanje();
                }else { // false = trump selection is on HumanPlayer
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
                // Fall through if trump selection is successful
            case SHOW_ZVANJE:
                System.out.println();
                System.out.println("Match phase: SHOW_ZVANJE");
                if(zvanjeCheck){
                    currentPhase = MatchPhase.PLAYING_ROUNDS;
                    break;
                }else{
                    System.out.println("Accept the Zvanje with Match.zvanjeCheck(boolean setBoolean)");
                    break;
                }
                // Fall through after showing zvanje
            case PLAYING_ROUNDS:
                System.out.println();
                System.out.println("Match phase: PLAYING_ROUNDS");
                if(!(game.playRounds())){ // false = still playing rounds stop the rounds
                    break;
                }
                System.out.println("End of game");
                currentPhase = MatchPhase.END_OF_GAME;
                // Fall through after playing all rounds
            case END_OF_GAME:
                System.out.println();
                System.out.println("Game " + gameCounter + "over!");
                zvanjeCheck = false;
                gameCounter++;
                winner = matchWinner();
                if (winner == null) { 
                    rotateDealer();
                    resetForNextGame();
                    currentPhase = MatchPhase.START;
                    System.err.println("Start game " + gameCounter + " with Match.play()");
                    break;
                } else {
                    currentPhase = MatchPhase.END_OF_MATCH;
                }
                // Fall through after finding the match winner
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

    public void zvanjeCheck(boolean setBoolean) {  
        if (currentPhase != MatchPhase.SHOW_ZVANJE) {
            System.out.println("Error: You cannot accapt Zvanje at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        this.zvanjeCheck = setBoolean;
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

}