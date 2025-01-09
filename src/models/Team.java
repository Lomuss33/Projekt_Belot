package models;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private int score;
    private int wins; // Total wins for the team
    private List<Player> players;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.score = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }

    // Update the team's score
    public void addScore(int points) {
        score += points;
        // Check if the team has won a big point
        if (score >= 1001) {
            addWin();
        }
    }

    public int getScore() {
        return score;
    }

    // Get total wins
    public int getWins() {
        return wins;
    }

    // Update the team's wins
    public void addWin() {
        this.wins++;
    }
}
