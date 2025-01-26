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

package ai;

import java.util.*;
import models.*;

public class HumanPlayer extends Player implements Cloneable {

    public  TrumpChoice trumpChoice; // Variable to store the trump suit choice
    public int cardIndexChoice; // Variable to store the card choice
    public boolean choiceMade;

    public HumanPlayer(String name, Team team) {
        super(name, team);
        this.trumpChoice = null; // No choice made yet
        this.cardIndexChoice = -1; // No choice made yet
        this.choiceMade = false;
    }

    @Override
    public HumanPlayer clone() throws CloneNotSupportedException {
        // Step 1: Perform shallow copy using `super.clone()`
        HumanPlayer cloned = (HumanPlayer) super.clone(); // Clone basic Player fields

        // Step 2: Clone `trumpChoice` and `cardIndexChoice` fields
        cloned.trumpChoice = this.trumpChoice; // Enum: safe to copy directly
        cloned.cardIndexChoice = this.cardIndexChoice; // Primitive field: safe to copy

        // Step 3: Return the cloned object
        return cloned;
    }

    public TrumpChoice getTrumpChoice() {
        return trumpChoice;
    }

    public int getCardIndexChoice() {
        return cardIndexChoice;
    }

    // Choose a card index to play
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
        if (playableIndexes == null || playableIndexes.isEmpty()) {
            System.out.println("No playable cards available.");
            return -1; // No playable cards
        }
        if (cardIndexChoice == -1) { // Keep waiting until a valid choice is made
            return -1; // No choice made yet
        }
        if (!playableIndexes.contains(cardIndexChoice)) {
            System.out.println("Chosen card is not playable.");
            return -1; // Chosen card is not in the list of playable cards
        }
        int chosenCardIndex = cardIndexChoice; // Use the chosen card index
        cardIndexChoice = -1; // Reset for the next round
        return chosenCardIndex; // Return the chosen index to the game
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
    
    // Choose trump suit
    @Override
    public TrumpChoice chooseTrumpOrSkip(int turnForChoosingTrump) {
            if (trumpChoice == null) { // Keep waiting until a valid choice is made
                    if (turnForChoosingTrump == 3) {
                        System.out.println("CANT SKIP");
                    }
                    return null; // No choice made yet
            }
            TrumpChoice result = trumpChoice; // Save the chosen suit to return

            trumpChoice = null; // Reset for the next game
            return result; // Return the choice to the game
    }

    // Method called when trump input arrives 
    public void trumpChoice(int choice) {
        trumpChoice = switch (choice) {
            case 1 -> Player.TrumpChoice.SPADES;
            case 2 -> Player.TrumpChoice.HEARTS;
            case 3 -> Player.TrumpChoice.DIAMONDS;
            case 4 -> Player.TrumpChoice.CLUBS;
            case 0 -> Player.TrumpChoice.SKIP;
            default -> throw new IllegalArgumentException("Invalid trump choice.");
        }; // Update the trump choice
    }

    // Method called when card input arrives
    public void cardChoice(int choice) {
        // Check if the index is valid
        if (choice < 0 || choice >= hand.getSize()) {
            throw new IllegalArgumentException("Invalid card choice.");
        }
        cardIndexChoice = choice; // Update the card choice
    }

}