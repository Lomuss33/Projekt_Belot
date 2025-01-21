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
            return switch (this.getRank()) {
                case JACK -> 16; // Strongest
                case NINE -> 15;
                case ACE -> 14;
                case KING -> 13;
                case QUEEN -> 12;
                case TEN -> 11;
                case EIGHT -> 10;
                case SEVEN -> 9; // Weakest
            };
        } else if (this.getSuit() == leadSuit) {
            // Strength for Non-Trump Cards
            return switch (this.getRank()) {
                case ACE -> 8; // Strongest
                case KING -> 7;
                case QUEEN -> 6;
                case JACK -> 5;
                case TEN -> 4;
                case NINE -> 3;
                case EIGHT -> 2;
                case SEVEN -> 1; // Weakest
            };
        }
        return 0; // If the card is not of trump or lead suit
    }

    // Set the value of the card based on whether it's a trump card
    public void calculateValue(Suit trumpSuit) {
        boolean isTrump = this.suit == trumpSuit;
        switch (this.rank) {
            case SEVEN -> value = 0;
            case EIGHT -> value = 0;
            case NINE -> value = isTrump ? 14 : 0;
            case TEN -> value = 10;
            case JACK -> value = isTrump ? 20 : 2;
            case QUEEN -> value = 3;
            case KING -> value = 4;
            case ACE -> value = 11;
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

    public String printSuit() {
        return switch (this.suit) {
            case HEARTS -> "\u2665"; // ♥
            case DIAMONDS -> "\u2666"; // ♦
            case CLUBS -> "\u2663 "; // ♣
            case SPADES -> "\u2660 "; // ♠
            default -> "?";
        };
    }
    

    public String printRank() {
        return switch (this.rank) {
            case ACE -> "A";
            case KING -> "K";
            case QUEEN -> "Q";
            case JACK -> "J";
            case TEN -> "10";
            case NINE -> "9";
            case EIGHT -> "8";
            case SEVEN -> "7";
            default -> "";
        };
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return printRank() + printSuit();
    }
}