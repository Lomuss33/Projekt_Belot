// ⠀⠀⠀⠀⣀⣤⣴⣶⣶⣶⣦⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
// ⠀⠀⣠⣾⣿⣿⣿⣿⣿⣿⢿⣿⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀
// ⢀⣾⣿⣿⣿⣿⣿⣿⣿⣅⢀⣽⣿⣿⡿⠃⠀⠀⠀⠀⠀⠀⠀⠀
// ⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
// ⣿⣿⣿⣿⣿⣿⣿⣿⣿⠛⠁⠀⠀⣴⣶⡄⠀⣶⣶⡄⠀⣴⣶⡄
// ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣀⠀⠙⠋⠁⠀⠉⠋⠁⠀⠙⠋⠀
// ⠸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀
// ⠀⠙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠀⠀⠀⠀⠀⠀⠀⠀
// ⠀⠀⠈⠙⠿⣿⣿⣿⣿⣿⣿⣿⠿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
// ⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
// 
// HUMAN PLAYER 
//

package controllers;

import java.util.*;
import models.*;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    public boolean isCardPlayable(Card card) {
        return true; // Implement actual rule-checking logic
    }

    // Play card from hand by index and delete it from hand
    @Override
    public Card playCard(int index) {
        if (index < 0 || index >= hand.showCards().size()) {
            throw new IllegalArgumentException("Invalid card index.");
        }
        Card cardPlayed = hand.getCard(index);
        hand.removeCard(index);
        return cardPlayed;
    }

    // Get selected cards from index to go check Zvanje
    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        List<Card> selectedCards = new ArrayList<>();
        for(int index : selectedIndices) {
            selectedCards.add(hand.getCard(index));
        }
        return selectedCards;
    }

}