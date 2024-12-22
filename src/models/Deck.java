package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;

    // Constructor for deck with all cards
    public Deck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        shuffle();
    }

    // Shuffle to randomize card order
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Deal specified count of cards and remove them from the deck
    public List<Card> dealCards(int count) {
        if (count > cards.size()) {
            throw new IllegalArgumentException("Not enough cards left to deal.");
        }
        List<Card> dealtCards = new ArrayList<>(cards.subList(0, count));
        cards = new ArrayList<>(cards.subList(count, cards.size()));
        return dealtCards;
    }

    // Reset the deck to its full initial state
    public void reset() {
        cards.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        shuffle();
    }

    // For debugging - to display the deck contents
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + "Deck:\n");
        for (Card card : cards) {
            sb.append(card).append("\n");
        }
        return sb.toString();
    }
}