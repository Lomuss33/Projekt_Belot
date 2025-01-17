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

    public Game game;
    public final Team team1, team2;
    private final List<Player> players;
    private int dealerIndex;
    public static final int WINNING_SCORE = 501; // The score required to win the match
    private final HumanPlayer me;
    private Team winner; // Reference to the winning team

    public Match(Difficulty difficulty) {
        this.team1 = new Team("Team 1");
        this.team2 = new Team("Team 2");
        players = GameUtils.initializePlayers(difficulty, team1, team2);
        this.dealerIndex = 3; // Start with the last player as the dealer so YOU can play first
        this.me = (HumanPlayer) players.get(0); // Initialize humanPlayer
    }

    public void startMatch() throws InterruptedException {
        boolean finished = false;

        while (!finished) {

            System.out.println("! Starting a new game...");
            game = new Game(players, team1, team2, dealerIndex);
            // Assertions to verify initial conditions of teams
            assertInitialTeamState(team1);
            assertInitialTeamState(team2);
            // Start the game and check for a winner
            game.startGame();
            
            System.out.println();
            System.out.println("Game over! Scores:");
            System.out.println(team1.getName() + ": " + team1.getBigs());
            System.out.println(team2.getName() + ": " + team2.getBigs());
            System.out.println();

            // Check if the match is over
            winner = matchWinner();

            if (winner != null) {
                System.out.println("Match winner found: " + winner.getName());
                finished = true; // Stop the loop when a winner is found
            } else {
                System.out.println("No winner yet, starting a new GAME...");
                rotateDealer(); // Rotate the dealer and prepare for the next game
                Thread.sleep(3000); // Optional delay
            }

            // reset valus for the next game
            resetForNextGame();

        }

        // Additional actions after the match ends (e.g., cleanup, next steps)
        handleMatchEnd(winner);
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
    public void trumpDecision(int choice) {
        me.trumpChoice(choice); // humanPlayer is your HumanPlayer instance
    }
}