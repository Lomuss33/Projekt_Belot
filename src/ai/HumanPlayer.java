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

public class HumanPlayer extends Player {

    private TrumpChoice trumpChoice = null; // Variable to store the trump suit choice
    private int cardIndexChoice = -1; // Variable to store the card choice

    public HumanPlayer(String name, Team team) {
        super(name, team);
    }

    public TrumpChoice getTrumpChoice() {
        return trumpChoice;
    }

    public int getCardIndexChoice() {
        return cardIndexChoice;
    }

    // IMPLEMENTATION NEEDED
    // Choose a card index to play
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
            while (cardIndexChoice == -1) { // Keep waiting until a valid choice is made
                    System.out.println(this.getName() + "'s hand:");
                    for (int i = 0; i < hand.getCards().size(); i++) {
                        Card card = hand.getCard(i);
                        String playableMarker = playableIndexes.contains(i) ? " (Playable)" : "";
                        System.out.println(i + " - " + card + playableMarker);
                    }
                    System.out.println("Choose a card to play by entering the index:");
                }
            int chosenCardIndex = trumpChoice.ordinal(); // Use the ordinal value as the chosen card index
            cardIndexChoice = -1; // Reset for the next round
            return chosenCardIndex; // Return the chosen index to the game
    }
    
    // Method called to select a card by the player
    public void selectCard(int index) {
            if (index < 0 || index >= hand.getCards().size()) {
                throw new IllegalArgumentException("Invalid card index.");
            }
            cardIndexChoice = index; // Temporarily reuse trumpChoice for card selection
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

    // NEEDS TO BE IMPLEMENTED CORRECTLY
    
    // Choose trump suit
    @Override
    public TrumpChoice chooseTrumpOrSkip(int turnForChoosingTrump) {
            while (trumpChoice == null) { // Keep waiting until a valid choice is made
                    System.out.println(this.getName() + " is choosing trump...");
                    return null; // Return null in case of an interruption
            }
            TrumpChoice result = trumpChoice; // Save the chosen suit to return
            trumpChoice = null; // Reset for the next round
            return result; // Return the choice to the game
    }

    // CALLED BY PLAYER
    // Method called when human input arrives
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

}