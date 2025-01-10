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

    public int getStrength(Card.Suit trumpSuit, Card.Suit leadSuit) {
        if (this.getSuit() == trumpSuit) {
            // Strength for Trump Cards
            switch (this.getRank()) {
                case JACK: return 16; // Strongest
                case NINE: return 15;
                case ACE: return 14;
                case KING: return 13;
                case QUEEN: return 12;
                case TEN: return 11;
                case EIGHT: return 10;
                case SEVEN: return 9; // Weakest
            }
        } else if (this.getSuit() == leadSuit) {
            // Strength for Non-Trump Cards
            switch (this.getRank()) {
                case ACE: return 8; // Strongest
                case KING: return 7;
                case QUEEN: return 6;
                case JACK: return 5;
                case TEN: return 4;
                case NINE: return 3;
                case EIGHT: return 2;
                case SEVEN: return 1; // Weakest
            }
        }
        return 0; // If the card is not of trump or lead suit
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

    // ToString method for debugging
    @Override
    public String toString() {
        return rank + " of " + suit + " (" + value + "pts)";
    }
}