package controllers;

import controllers.Game.Difficulty;
import models.*;

public class Match {

    private final Difficulty difficulty;
    private final Team team1, team2;
    private int dealerIndex;
    private static final int WINNING_SCORE = 101; // The score required to win the match
    private Team winner; // Reference to the winning team

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
            winner = game.startGame();
            System.out.println("startGame() SKIPPED");

            // Check if game ended and has a winner
            if (endMatch()) {
                System.out.println("Winner of this game: " + winner.getName());
                finished = true;
            }
            // Rotate the dealer
            System.out.println("rotateDealer() STARTING NEW GAME MAYBE");
            rotateDealer();
            // Pause between games (optional)
            Thread.sleep(3000);
        }
        // Announce the final result (optional)
        System.out.println("Match is over! Final scores:");
        System.out.println(team1.getName() + ": " + team1.getBigs());
        System.out.println(team2.getName() + ": " + team2.getBigs());
        System.out.println("Winner: " + winner.getName());
    }

    public boolean endMatch() {
        // The match is over if either team reaches or exceeds the winning score
        return team1.getBigs() >= WINNING_SCORE || team2.getBigs() >= WINNING_SCORE;
    }

    private void rotateDealer() {
        dealerIndex = (dealerIndex + 1) % 4; // Rotate dealer index among 4 players
    }
}
