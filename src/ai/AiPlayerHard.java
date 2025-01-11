// ░░░░░░░░░░░░░░░░░
// ░░░░▄▄████▄▄░░░░░
// ░░░██████████░░░░
// ░░░██▄▄██▄▄██░░░░
// ░░░░▄▀▄▀▀▄▀▄░░░░░
// ░░░▀░░░░░░░░▀░░░░
// ░░░░░░░░░░░░░░░░░
//
// NORMAL AI PLAYER
//

package ai;

import java.util.*;
import models.*;

public class AiPlayerHard extends Player {

    public AiPlayerHard(String name, Team team) {
        super(name, team);
    }

    // IMPLEMENTATION NEEDED
    // Choose a card to play
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
        if (playableIndexes.isEmpty()) {
            throw new IllegalArgumentException("No playable cards available!");
        }
        Random random = new Random();
        return playableIndexes.get(random.nextInt(playableIndexes.size()));
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

    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        return new ArrayList<>(); // Basic Zvanje logic for Normal AI
    }

    // Choose trump suit
    @Override
    public Card.Suit chooseTrump() {
        return Card.Suit.HEARTS; // Implement actual trump suit selection logic
    }
    
}
