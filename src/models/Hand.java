package models;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Card> cards;

    // Constructor initializes an empty hand
    public Hand() {
        cards = new ArrayList<>();
    }

    // Add cards to the hand
    public void addCards(List<Card> newCards) {
        cards.addAll(newCards);
    }

    // Save the current hand as a snapshot of the round
    public List<Card> saveHandRound() {
        return new ArrayList<>(cards); // Return a copy of current hand
    }

    // Get the current hand
    public List<Card> getCards() {
        return cards;
    }

    // Display the hand as a string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hand: ");
        for (Card card : cards) {
            sb.append(card).append(", ");
        }
        return sb.toString();
    }
}