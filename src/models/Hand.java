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

    // Get the current all cards in the hand
    public List<Card> showCards() {
        return cards;
    }

    // Get a single card by index
    public Card getCard(int index) {
        return cards.get(index);
    }

    // Remove a single card from the hand by index
    public void removeCard(int index) {
        cards.remove(index);
    }

    // Save the current hand as a snapshot of the round
    public List<Card> saveHandRound() {
        return new ArrayList<>(cards); // Return a copy of current hand
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