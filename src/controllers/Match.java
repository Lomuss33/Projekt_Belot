// ▐▓█▀▀▀▀▀▀▀▀▀█▓▌░▄▄▄▄▄░
// ▐▓█░░▀░░▀▄░░█▓▌░█▄▄▄█░
// ▐▓█░░▄░░▄▀░░█▓▌░█▄▄▄█░
// ▐▓█▄▄▄▄▄▄▄▄▄█▓▌░█████░
// ░░░░▄▄███▄▄░░░░░█████░


package controllers;

import ai.AiPlayerLEARN;
import ai.AiPlayerNORMAL;
import ai.AiPlayerPRO;
import ai.HumanPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import models.*;

// Match
public class Match implements Cloneable { // Implement Cloneable{

    // Enum for the difficulty levels of the AI players
    public enum Difficulty {
        LEARN, NORMAL, PRO,
    }

    public enum MatchPhase {
        START,
        CHOOSING_TRUMP,
        SHOW_ZVANJE,
        PLAYING_ROUNDS,
        END_OF_GAME,
        END_OF_MATCH
    }

    // Variables for the Match class
    public static final int WINNING_SCORE = 101; // Score required to win the match
    private MatchPhase currentPhase; // Tracks the current phase of the match
    public final Stack<Match> snapshots = new Stack<>(); // Stack for storing match states (e.g., for undo)
    public Game game; // Reference to the current game
    public int gameCounter; // Tracks the number of games played so far
    public Difficulty difficulty; // Difficulty level of the game
    public Team team1, team2; // The two competing teams
    public List<Player> players; // List of all players in the match
    public HumanPlayer me; // Player instance for the human player
    public Team winner; // Tracks the winner of the match
    public int dealerIndex; // Tracks the index of the player dealing cards

    // Boolean variables for match lifecycle
    public boolean startGame; // Indicates if the game has started
    public boolean endGame; // Indicates if the game has ended
    public boolean startRound; // Indicates if a round has started
    public boolean endMatch; // Indicates if the match has ended

    // Customization options (e.g., names and attributes)
    public Difficulty customDifficulty = Difficulty.LEARN; // Default difficulty level
    public String customTeam1Name = "Team 1";
    public String customPlayerName = "You";
    public String customTeamMate = "Teammate";
    public String customEnemy1 = "Rival_1";
    public String customEnemy2 = "Rival_2";
    public String customTeam2Name = "Team 2";

    public Match() {
        this.currentPhase = MatchPhase.START; // Initial phase is START
        this.gameCounter = 0; // Game counter starts from 0
        this.dealerIndex = 3; // The human player starts as the dealer by default
        this.startGame = false; // Game has not started
        this.endGame = false; // Game has not ended
        this.startRound = false; // Round has not started
        this.endMatch = false; // Match has not ended
    }
// Match

    // Play method to advance the game through different phases of the match
    // Play
    public void play() {
        switch (currentPhase) {
            case START -> {
                if (!runStartPhase()) {
                    return; // Exit the play method
                }
                this.play(); // Proceed to the next phase
            }
            case CHOOSING_TRUMP -> {
                if (!runChoosingTrumpPhase()) {
                    return; // Exit the play method
                }
                this.play(); // Proceed to the next phase
            }
            case SHOW_ZVANJE -> {
                if (!runShowZvanjePhase()) {
                    return; // Exit the play method
                }
                this.play(); // Proceed to the next phase
            }
            case PLAYING_ROUNDS -> {
                if (!runPlayingRoundsPhase()) {
                    return; // Exit the play method
                }
                this.play(); // Proceed to the next phase
            }
            case END_OF_GAME -> {
                if (!runEndOfGamePhase()) {
                    return; // Exit the play method
                }
                this.play(); // Proceed to the next phase
            }
            case END_OF_MATCH -> {
                if (!runEndOfMatchPhase()) {
                    return; // Exit the play method
                }
                this.play(); // Restart the match
                return; // Exit the play method
            }
            default -> throw new IllegalStateException("Unexpected value: " + currentPhase);
        }
    }
    // Play

    private void printStart() {
        System.out.println("Match phase: START");
        System.out.println("Match started!");
        System.out.println("Team 1: " + team1.getName());
        System.out.println("Team 2: " + team2.getName());
        System.out.println("Players: " + players);
        System.out.println("Dealer index: " + dealerIndex);
    }

    private boolean runStartPhase() {
        initializeGameSettings(difficulty, customTeam1Name, customTeam2Name, 
                       customPlayerName, customTeamMate, customEnemy1, customEnemy2);
        printStart();
        if(!startGame) { // Ensure startGame boolean must be true before advancing
            System.out.println("Waiting for game to start. Use startGame(true) to proceed.");
            return false; // Exit the method without advancing the game
        }
        if(game == null) {
            game = new Game(players, team1, team2, dealerIndex, difficulty);
        }
        // Initialize the game when all prerequisites are met
        game.initializeGame();
        assertInitialTeamState(team1);
        assertInitialTeamState(team2);

        currentPhase = MatchPhase.CHOOSING_TRUMP;
        startGame = false;
        return true; // Proceed to the next phase
    }

    private boolean runChoosingTrumpPhase() {
        System.out.println("Match phase: CHOOSING_TRUMP");
        if (!game.trumpSelection()) {
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
            return false; // Exit the method without advancing the game
        }
        currentPhase = MatchPhase.SHOW_ZVANJE;
        game.findZvanje();
        return true;
    }

    private boolean runShowZvanjePhase() {
        System.out.println("Match phase: SHOW_ZVANJE");
        if (!startRound) { // Ensure startRound boolean must be true before advancing
            System.out.println("Waiting for Zvanje acceptance. Use startRound(true) to proceed.");
            return false;
        }
        // Reset boolean immediately after it's used
        startRound = false;
        currentPhase = MatchPhase.PLAYING_ROUNDS;
        return true;
    }

    private boolean runPlayingRoundsPhase() {
        System.out.println("Match phase: PLAYING_ROUNDS");
        if (!game.playRounds()) { // false = still playing rounds
            System.out.println("Use pickCard(int choice) to play a card.");
            return false;
        }
        System.out.println("End of game");

        currentPhase = MatchPhase.END_OF_GAME;
        return true;
    }

    private boolean runEndOfGamePhase() {
        System.out.println("Match phase: END_OF_GAME");
        if (!endGame) { // Ensure endGame boolean must be true before advancing
            System.out.println("Waiting for game end. Use endGame() to proceed.");
            return false;
        }
        // Reset boolean immediately after it's used
        endGame = false;
        // Check if the match is over
        winner = matchWinner();
        printEndGame(winner);
        if (winner != null) {
            currentPhase = MatchPhase.END_OF_MATCH;
        } else {
            // Reset the game for the next round
            resetForNextGame();
            gameCounter++;
            currentPhase = MatchPhase.START;
        }
        return true;
    }

    private boolean runEndOfMatchPhase() {
        printMatchEnd(winner); 
        if (!endMatch) { // Ensure endMatch boolean must be true before advancing
            System.out.println("Waiting for match to end. Use endMatch(true) to proceed.");
            return false;
        }
        // Reset boolean immediately after it's used
        endMatch = false;
        resetMatch();
        currentPhase = MatchPhase.START;
        return true;
    }

    public void resetMatch() {
        team1 = null;
        team2 = null;
        players = null;
        game = null;
        me = null;
        dealerIndex = 3;
        currentPhase = MatchPhase.START;
        gameCounter = 0;
        startGame = false;
        endGame = false;
        startRound = false;
        winner = null;
        snapshots.clear();
        System.out.println("Match has been reset.");
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
        rotateDealer(); // Rotate dealer index among 4 players
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
        System.out.println("dealer rotated from " + dealerIndex); 
        System.out.println("dealer rotated to " + (dealerIndex + 1) % 4);
        this.dealerIndex = (dealerIndex + 1) % 4; // Rotate dealer index among 4 players
    }
    
    private void printMatchEnd(Team winner) {
        // Additional actions after the match ends (e.g., cleanup, next steps)
        // For example, update player statistics, save match results, etc.
        System.out.println();
        System.out.println("Match is over! Final scores:");
        System.out.println(team1.getName() + ": " + team1.getBigs());
        System.out.println(team2.getName() + ": " + team2.getBigs());
        System.out.println("Match ended. Winner: " + winner.getName());
    }

    public void setDifficulty(String difficultyName) {
        if (difficultyName != null) {
            switch (difficultyName.toUpperCase()) {
            case "LEARN" -> this.difficulty = Difficulty.LEARN;
            case "NORMAL" -> this.difficulty = Difficulty.NORMAL;
            case "PRO" -> this.difficulty = Difficulty.PRO;
            default -> {
                System.out.println("Invalid difficulty. Please choose String LEARN, NORMAL, or PRO.");
                return;
            }
            }
            System.out.println("Difficulty set to: " + this.difficulty);
        }
        System.out.println("Difficulty set: " + difficulty);
    }

    public void setMyName(String playerName) {  
        this.customPlayerName = playerName; 
        System.out.println("My name set to: " + customPlayerName);
    }

    public void setOtherNames(String teamMate, String enemyMate1, String enemyMate2) {
        if (teamMate != null) {
            this.customTeamMate = teamMate;
        }
        if (enemyMate1 != null) {
            this.customEnemy1 = enemyMate1;
        }
        if (enemyMate2 != null) {
            this.customEnemy2 = enemyMate2;
        }
        System.out.println("""
            Other player names set: 
            Team mate: %s
            Rival 1: %s
            Rival 2: %s
            """.formatted(customTeamMate, customEnemy1, customEnemy2));
    }

    public void setTeamNames(String team1Name, String team2Name) {
        if (team1Name != null) {
            this.customTeam1Name = team1Name;
        }
        if (team2Name != null) {
            this.customTeam2Name = team2Name;
        }
        System.out.println("Team names set: " + this.customTeam1Name + " vs " + this.customTeam2Name);
    }

    // Method to be called from the GUI when the human player accepts the zvanje
    public void startGame() {
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
        this.startGame = true;
        this.play(); // Move the game forward once all conditions are met
    }
    

    // Method to be called from the GUI when the human player chooses a trump suit
    public void pickTrump(int choice) {
        if (currentPhase != MatchPhase.CHOOSING_TRUMP) {
            System.out.println("Error: You cannot pick a trump at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        if (choice < 0 || choice > 4) { // Ensure valid choice
            System.out.println("Invalid choice! Please select a number between 0 and 4.");
            return;
        }
        if (difficulty == Difficulty.NORMAL) {
            snapshots.clear();
            System.out.println("Snapshots cleared for new Game due to NORMAL difficulty.");
        }
        // Save the current state before proceeding
        if (difficulty != Difficulty.PRO) {
            saveSnapshot();
        }

        // Proceed with trump selection if phase is valid
        me.trumpChoice(choice); // humanPlayer is your HumanPlayer instance
        //game.setTrumpSuit(choice);
        // Move the game forward by calling play() to progress to the next phase
        this.play();
    }    

    // Method to be called from the GUI when the human player accepts the zvanje 
    public void startRound() {  
        if (currentPhase != MatchPhase.SHOW_ZVANJE) {
            System.out.println("Error: You cannot start a round at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        this.startRound = true;
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    // pickCard
    public void pickCard(int choice) {
        if (currentPhase != MatchPhase.PLAYING_ROUNDS) {
            System.out.println("Error: You cannot play a card at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        // Save the current state before proceeding
        if (difficulty != Difficulty.PRO) {
            saveSnapshot();
        }
        // Proceed with card play if phase is valid
        me.cardChoice(choice); // humanPlayer is your HumanPlayer instance
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }    
    // pickCard

    public void endGame() { 
        if (currentPhase != MatchPhase.END_OF_GAME) {
            System.out.println("Error: You cannot end the game at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        this.endGame = true;
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    public void endMatch() { 
        if (currentPhase != MatchPhase.END_OF_MATCH) {
            System.out.println("Error: You cannot end the Match at this phase! Current phase: " + currentPhase);
            return; // Exit the method without advancing the game
        }
        this.endMatch = true;
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    // saveSnapshot
    public void saveSnapshot() {
        try {
            Match snapshot = this.clone(); // Clone the match object
            System.err.println("snapshot: " + snapshot);
            System.out.println("Not Saved. Current snapshot count: " + snapshots.size());
            snapshots.push(snapshot);
            System.out.println("Snapshot saved. Current snapshot count: " + snapshots.size());
            System.err.println("snapshots: " + snapshots);
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Snapshot saving failed", e);
        }
    }
    // saveSnapshot

    // goBack
    public void goBack() {
        if (difficulty == Difficulty.PRO) {
            System.err.println("Reverting to a previous snapshot is not allowed on PRO difficulty!");
            return;
        }
        if (currentPhase == MatchPhase.START || currentPhase == MatchPhase.END_OF_MATCH || currentPhase == MatchPhase.CHOOSING_TRUMP) {
            System.err.println("Cannot revert to previous snapshot at the start or end of the match!");
            return;
        }
        if (snapshots.isEmpty()) {
            System.err.println("No previous snapshot to revert to!");
            return;
        }
        restoreSnapshot(snapshots.pop());
        play(); // Resume the game from the previous state
    }

    private void restoreSnapshot(Match previousState) {
        try {
            this.game = previousState.game.clone();
            this.currentPhase = previousState.currentPhase;
            this.team1 = game.getTeam1();
            this.team2 = game.getTeam2();
            this.players = new ArrayList<>(previousState.players);
            this.dealerIndex = game.getDealerIndex();
            this.me = (HumanPlayer) players.get(0);
            this.startGame = previousState.startGame;
            this.startRound = previousState.startRound;
            this.endGame = previousState.endGame;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Snapshot restoration failed", e);
        }
    }
    // goBack

    // Settings
    public void initializeGameSettings(Difficulty difficulty, String team1Name, String team2Name,  
                        String playerName, String teamMate, String enemyMate1, String enemyMate2) {

        if (difficulty == null) {
            this.difficulty = customDifficulty;
        }
        if (team1 == null || team2 == null) {
            this.team1 = new Team(customTeam1Name);
            this.team2 = new Team(customTeam2Name);
        }
        if (players == null) {
            this.players = initializePlayers(difficulty, team1, team2, 
                            customPlayerName, customTeamMate, customEnemy1, customEnemy2);
            this.me = (HumanPlayer) players.get(0); // Player 0 is the human
        }
    }
    // Settings

        // Initialize players and assign them to the given teams
    public static List<Player> initializePlayers(Difficulty difficulty, Team team1, Team team2, 
                String playerName, String teamMate, String enemyMate1, String enemyMate2) {
        List<Player> players = new ArrayList<>();

        // Assign human player to Team 1
        players.add(new HumanPlayer(playerName, team1));

        // Create AI players based on difficulty
        switch (difficulty) {
            case LEARN:
                players.add(new AiPlayerLEARN(enemyMate1, team2)); // Bot 1 in Team 2
                players.add(new AiPlayerLEARN(teamMate, team1)); // Bot 2 in Team 1
                players.add(new AiPlayerLEARN(enemyMate2, team2)); // Bot 3 in Team 2
                break;
            case NORMAL:
                players.add(new AiPlayerNORMAL(enemyMate1, team2));
                players.add(new AiPlayerNORMAL(teamMate, team1));
                players.add(new AiPlayerNORMAL(enemyMate2, team2));
                break;
            case PRO:
                players.add(new AiPlayerPRO(enemyMate1, team2));
                players.add(new AiPlayerPRO(teamMate, team1));
                players.add(new AiPlayerPRO(enemyMate2, team2));
                break;
        }
        return players;
    }

    private void printEndGame(Team winner) {
        System.out.println("End of game!");
        System.out.println("Trump team passed? " + game.teamPassed());
        System.out.println("Team 2: " + team2.getName() + " - " + team2.getBigs());
        System.out.println("Game counter: " + gameCounter);
        if(winner != null) {
            System.out.println("We have a winner" + winner.getName());
        }else {
            System.out.println("No winner yet... another game?");
        }
        System.out.println("Use endGame() to proceed.");
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    // Clone the Match object to save snapshots
    @Override
    public Match clone() throws CloneNotSupportedException {
        // Create shallow clone of Match instance 
        Match clonedMatch = (Match) super.clone();

        // Deep clone teams
        clonedMatch.team1 = (team1 != null) ? team1.clone() : null;
        clonedMatch.team2 = (team2 != null) ? team2.clone() : null;

        // Deep clone players list (same deep-cloned players list is consistent across all)
        clonedMatch.players = new ArrayList<>(this.players.size());
        for (Player player : this.players) {
            clonedMatch.players.add(player.clone());
        }
        // Change the cloned HumanPlayer reference to the cloned players list
        clonedMatch.me = (HumanPlayer) clonedMatch.players.get(0);

        // Deep clone the game
        makeGameClone(clonedMatch);
        // Deep clone the current round 
        makeRoundClone(clonedMatch);
        
        // Copy primitive fields and booleans
        clonedMatch.dealerIndex = this.dealerIndex;
        clonedMatch.gameCounter = this.gameCounter;
        clonedMatch.currentPhase = this.currentPhase;
        clonedMatch.difficulty = this.difficulty;

        System.out.println("Clone match snapshot count: " + snapshots.size());
        return clonedMatch;
    }

    private void makeRoundClone(Match clonedMatch) throws CloneNotSupportedException { 
        // Deep clone currentRound
        clonedMatch.game.currentRound = (game.currentRound != null) ? game.getCurrentRound().clone() : null; 
        if (clonedMatch.game.currentRound != null) {
            clonedMatch.game.currentRound.setPlayers(clonedMatch.players); // Update the players reference 
            clonedMatch.game.currentRound.setDifficulty(difficulty);
        }
    }

    // Deep clone the game and its internal hierarchy
    private void makeGameClone(Match clonedMatch) throws CloneNotSupportedException {
        if (game != null) {
            clonedMatch.game = game.clone(); // Game will propagate the players and Round correctly
            clonedMatch.game.setDifficulty(difficulty);
            clonedMatch.game.players = clonedMatch.players;
            clonedMatch.game.setTeam1(clonedMatch.team1);
            clonedMatch.game.setTeam2(clonedMatch.team2);
            // clonedMatch.game.setPlayers(clonedMatch.players); // Use the shared cloned players in Game
            if (clonedMatch.game.currentRound != null) {
                clonedMatch.game.currentRound.setPlayers(clonedMatch.players); // Use the shared cloned players in Round 
            }
                // Update the player reference in the cloned game zvanjeWin
            if (clonedMatch.game.zvanjeWin != null && clonedMatch.game.zvanjeWin.getTotalPoints() != 0) {
                clonedMatch.game.zvanjeWin.setPlayer(clonedMatch.game.players.get(players.indexOf(game.zvanjeWin.getPlayer())));
            }
        }
    }
}