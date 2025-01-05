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

    // Check if the card is playable
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
