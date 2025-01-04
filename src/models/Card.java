// _____
// |A .  |
// | /.\ |
// |(_._)|
// |  |  |
// |____V|
//
// CARD
//

package models;
//import models.Card;

public class Card {
    // Enum for the Suit of the card
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    // Enum for the Rank of the card
    public enum Rank {
        SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE 
    }

    private final Suit suit; // Suit of the card
    private final Rank rank; // Rank of the card
    private int value; // Value of the card

    // Constructor // e.g. 9♥ (Value: 14) => Card card = new Card(Suit.HEARTS, Rank.NINE)
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = 0; // Value based on trump or non-trump
    }

    // Getter Suit
    public Suit getSuit() {
        return suit;
    }

    // Getter Rank
    public Rank getRank() {
        return rank;
    }

    // Getter Value
    public int getValue() {
        return value;
    }

    // Set the value of the card based on whether it's a trump card
    public void calculateValue(Suit trumpSuit) {
        boolean isTrump = this.suit == trumpSuit;
        switch (this.rank) {
            case SEVEN: value = 0; break;
            case EIGHT: value = 0; break;
            case NINE: value = isTrump ? 14 : 0; break;
            case TEN: value = 10; break;
            case JACK: value = isTrump ? 20 : 2; break;
            case QUEEN: value = 3; break;
            case KING: value = 4; break;
            case ACE: value = 11; break;
        }
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return rank + " of " + suit + " (Value: " + value + ")";
    }
}