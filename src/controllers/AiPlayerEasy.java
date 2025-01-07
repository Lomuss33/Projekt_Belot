// ░░░░░░░░░░░░░░░░░
// ░░░░░░▄██▄░░░░░░░
// ░░░░▄██████▄░░░░░
// ░░░███▄██▄███░░░░
// ░░░░░▄▀▄▄▀▄░░░░░░
// ░░░░▀░▀░░▀░▀░░░░░
// ░░░░░░░░░░░░░░░░░
//
// EASY AI PLAYER
//

package controllers;

import java.util.*;
import models.Card;
import models.Player;

public class AiPlayerEasy extends Player {

    // Constructor
    public AiPlayerEasy(String name) {
        super(name);
    }

    // IMPLEMENTATION NEEDED
    // Choose a card to play
    @Override
    public Card chooseCard() {
        return hand.getCard(0); // Implement actual card selection logic
    }

    // IMPLEMENTATION NEEDED
    // Find Dama in hand
    @Override
    public void callDama() {
        for(Card card : hand.getCards()) {
            if(card.getRank() == Card.Rank.QUEEN && card.getSuit() == Card.Suit.CLUBS) {
                // Call Dama
            }
        }
    }

    // IMPLEMENTATION NEEDED
    // Check if card is playable
    @Override
    public boolean isCardPlayable(Card card) {
        return true; // Implement actual rule-checking logic
    }
    
    // Get selected cards from index to go check Zvanje
    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        return new ArrayList<>(); // Basic Zvanje logic for Normal AI
    }

    // Choose trump suit
    @Override
    public Card.Suit chooseTrump() {
        return null; // Implement actual trump suit selection logic
    }
    
}
