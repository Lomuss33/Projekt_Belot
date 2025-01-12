package controllers;

import controllers.Game.Difficulty;
import java.util.List;
import models.*;
import services.GameUtils;

public class Match {

    public final Team team1, team2;
    private final List<Player> players;
    private int dealerIndex;
    public static final int WINNING_SCORE = 501; // The score required to win the match
    private Team winner; // Reference to the winning team

    public Match(Difficulty difficulty) {
        this.team1 = new Team("Team 1");
        this.team2 = new Team("Team 2");
        players = GameUtils.initializePlayers(difficulty, team1, team2);
        this.dealerIndex = 3; // Start with the last player as the dealer so YOU can play first
    }

    public void startMatch() throws InterruptedException {
        boolean finished = false;

        while (!finished) {
            System.out.println("! Starting a new game...");
            
            Game game = new Game(players, team1, team2, dealerIndex);

            // Asserts to check the initial state of the game
            assert team1.getSmalls() == 0 : "Team small score should be 0 at the start of the match";    
            assert team1.getBigs() == 0 : "Team big score should be 0 at the start of the match";
            for (Player player : team1.getPlayers()) {
                assert player.getHand().isEmpty() : "Player hand should be empty at the start of the match";
            }
            for (Player player : team2.getPlayers()) {
                assert player.getHand().isEmpty() : "Player hand should be empty at the start of the match";
            }

            // Start the game
            // Start the game and check for a winner
            winner = game.startGame(); // Assumes `startGame` returns the winning team or null if no winner yet

            System.out.println("Game over! Scores:");
            System.out.println(team1.getName() + ": " + team1.getBigs());
            System.out.println(team2.getName() + ": " + team2.getBigs());

            if (winner != null) {
                finished = true; // Stop the loop when a winner is found
            } else {
                System.out.println("No winner yet, starting a new GAME...");
                rotateDealer(); // Rotate the dealer and prepare for the next game
                Thread.sleep(3000); // Optional delay
            }
        }

        // Post-match logic after a winner is determined

        // Additional actions after the match ends (e.g., cleanup, next steps)
        handleMatchEnd(winner);
    }

    public boolean endMatch() {
        // The match is over if either team reaches or exceeds the winning score
        return team1.getBigs() >= WINNING_SCORE || team2.getBigs() >= WINNING_SCORE;
    }

    private void rotateDealer() {
        dealerIndex = (dealerIndex + 1) % 4; // Rotate dealer index among 4 players
    }
    
    private void handleMatchEnd(Team winner) {
        // Additional actions after the match ends (e.g., cleanup, next steps)
        // For example, update player statistics, save match results, etc.
        
        System.out.println("Match is over! Final scores:");
        System.out.println(team1.getName() + ": " + team1.getBigs());
        System.out.println(team2.getName() + ": " + team2.getBigs());
        System.out.println("Match ended. Winner: " + winner.getName());
    }
}