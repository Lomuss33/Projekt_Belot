// _____
// |A .  | _____
// | /.\ ||A ^  | _____
// |(_._)|| / \ ||A _  | _____
// |  |  || \ / || ( ) ||A_ _ |
// |____V||  .  ||(_'_)||( v )|
//        |____V||  |  || \ / |
//               |____V||  .  |
//                      |____V|
//
// DECK
//

package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards; // List of cards in the deck

    // Constructor for deck with all cards
    public Deck() {
        initializeDeck(); // Use helper method to initialize cards
        shuffle(); // Randomize the deck
    }

    // Private helper to initialize all cards in the deck
    private void initializeDeck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    // Shuffle to randomize card order
    public final void shuffle() {
        Collections.shuffle(cards);
    }

    // Return specified count of cards and remove them from the deck
    private List<Card> dealCards(int count) {
        // Check if there are enough cards to deal
        if (count > cards.size()) {
            throw new IllegalArgumentException("Not enough cards left to deal.");
        }
        List<Card> dealtCards = new ArrayList<>(cards.subList(0, count)); // Define dealt cards
        cards = new ArrayList<>(cards.subList(count, cards.size())); // Remove dealt cards
        return dealtCards; // Return dealt cards
    }

    // Deal the cards to the players hands
    public void dealHands(List<Player> players, int cardsPerPlayer) {
        for (Player player : players) {
            player.getHand().addCards(dealCards(cardsPerPlayer)); // Add dealt cards to player's hand
        }
    }

    // Reset the deck to its full initial state
    public void reset() {
        initializeDeck(); // Use helper method to initialize cards
        shuffle(); // Randomize the deck
    }

    // Set the state of the deck (undo functionality)
    public void setCards(List<Card> newCards) {
        this.cards = new ArrayList<>(newCards); // Set the deck to the new state
    }

    // Get the current state of the deck
    public List<Card> getCards() {
        return new ArrayList<>(cards);  // Return a copy for immutability
    }

    // For debugging - to display the deck contents
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Deck: \n");
        for (Card card : cards) {
            sb.append(card).append("\n");
        }
        return sb.toString();
    }
}