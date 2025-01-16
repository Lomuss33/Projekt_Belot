//                      ___
//  o__       o__     |   |\
// /|         /\      |   |X\
// / > o       <\     |   |XX\

//package models;

// import java.util.ArrayList;
// import java.util.List;

public class Team {
    private final String name;
    private final List<Player> players;
    private List<Card> wonCards;
    private int bigs; // big score / current score in game
    public int smalls; // small score / points won in the current round

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.wonCards = new ArrayList<>();
        this.bigs = 0; // Initialize big score
        this.smalls = 0; // Initialize small score
    }

    // Add the cards won in the current round to the team's won cards
    public void addWonCardsAsPoints(List<Card> cards) {
        wonCards.addAll(cards);
        for (Card card : cards) {
            smalls += card.getValue(); // Add card values to smalls
        }
    }

    public List<Card> getWonCards() {
        return wonCards;
    }

    public void resetWonCards() {
        wonCards = new ArrayList<>();
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

    // Update the bigs score
    public void addBigs(int points) {
        System.out.println("Adding " + points + " points to " + name + " big score");
        this.bigs += points;
    }

    public void setBigs(int points) {
        this.bigs = points;
    }

    public int getBigs() {
        return bigs;
    }

    public void addSmalls(int points) {
        System.out.println("Adding " + points + " points to " + name + " small score");
        this.smalls += points;
    }

    public void setSmalls(int points) {
        this.smalls = points;
    }

    // Reset the smalls score
    public void resetSmalls() {
        this.smalls = 0;
    }

    public int getSmalls() {
        return smalls;
    }
}
