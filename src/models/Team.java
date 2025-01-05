package models;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private final String name; // Team name
    private final List<Player> players; // List of players in the team
    private int score; // Total score for the team
    private int wins; // Total wins for the team

    // Constructor
    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.score = 0;
        this.wins = 0;
    }

    // Add a player to the team
    public void addPlayer(Player player) {
        players.add(player);
    }

    // Get total score
    public int getScore() {
        return score;
    }

    // Update the team's score
    public void addScore(int points) {
        this.score += points;
        // Check if the team has won a big point
        if (score >= 1001) {
            addWin();
        }
    }

    // Get total wins
    public int getWins() {
        return wins;
    }

    // Update the team's wins
    public void addWin() {
        this.wins++;
    }

    // Get team players
    public List<Player> getPlayers() {
        return players;
    }

    // Get team name
    public String getName() {
        return name;
    }
}
