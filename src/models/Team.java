package models;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private final String name;
    private final List<Player> players;
    private List<Card> wonCards;
    private int score; // current score in game

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.wonCards = new ArrayList<>();
        this.score = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getWonCards() {
        return wonCards;
    }

    public void addWonCard(List<Card> cards) {
        wonCards.addAll(cards);
    }

    public void resetWonCards() {
        wonCards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void resetScore() {
        this.score = 0;
    }

    // Update the team's score
    public void addScore(int points) {
        this.score += points;
    }

    public void setScore(int points) {
        this.score = points;
    }

    public int getScore() {
        return score;
    }

}
