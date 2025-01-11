package controllers;

import controllers.Game.Difficulty;
import models.*;

public class Match {

    private final Difficulty difficulty;
    private final Team team1, team2;
    private int dealerIndex;
    private static final int WINNING_SCORE = 1001; // The score required to win the match

    public Match(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.team1 = new Team("Team 1");
        this.team2 = new Team("Team 2");
        this.dealerIndex = 3; // Start with the last player as the dealer so YOU can play first
    }

    public void startMatch() throws InterruptedException {
        boolean finished = false;

        while (!finished) {
            System.out.println("! Starting a new game...");
            
            Game game = new Game(difficulty, team1, team2, dealerIndex);
            Team winner = game.startGame();

            if (winner != null) {
                System.out.println("Winner of this game: " + winner.getName());
            }

            // Rotate the dealer
            rotateDealer();

            // Check if the match is over
            finished = isOver();

            // Pause between games (optional)
            Thread.sleep(3000);
        }

        // Announce the final result
        announceMatchResults();
    }

    public boolean isOver() {
        // The match is over if either team reaches or exceeds the winning score
        return team1.getScore() >= WINNING_SCORE || team2.getScore() >= WINNING_SCORE;
    }

    private void rotateDealer() {
        dealerIndex = (dealerIndex + 1) % 4; // Rotate dealer index among 4 players
    }

    private void announceMatchResults() {
        System.out.println("! Match Over!");
        System.out.println("! Match Over!");
        if (team1.getScore() >= WINNING_SCORE) {
            System.out.println("Winner: " + team1.getName() + " with " + team1.getScore() + " points!");
        } else if (team2.getScore() >= WINNING_SCORE) {
            System.out.println("Winner: " + team2.getName() + " with " + team2.getScore() + " points!");
        } else {
            System.out.println("! No team reached the winning score. Match is a draw.");
        }

        System.out.println("! Final Scores:");
        System.out.println(team1.getName() + ": " + team1.getScore());
        System.out.println(team2.getName() + ": " + team2.getScore());
    }
}
