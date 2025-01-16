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

//package ai;

// import java.util.*;
// import models.*;

public class HumanPlayer extends Player {

    private TrumpChoice trumpChoice = null; // Variable to store the trump suit choice
    private int cardIndexChoice = -1; // Variable to store the card choice
    private final Object lock = new Object(); // Lock object for synchronizing threads

    public HumanPlayer(String name, Team team) {
        super(name, team);
    }

    // IMPLEMENTATION NEEDED
    // Choose a card index to play
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
        synchronized (lock) { // Enter synchronized block to wait for input
            while (cardIndexChoice == -1) { // Keep waiting until a valid choice is made
                try {
                    System.out.println(this.getName() + "'s hand:");
                    for (int i = 0; i < hand.getCards().size(); i++) {
                        Card card = hand.getCard(i);
                        String playableMarker = playableIndexes.contains(i) ? " (Playable)" : "";
                        System.out.println(i + " - " + card + playableMarker);
                    }
                    System.out.println("Choose a card to play by entering the index:");
                    lock.wait(); // Pause this thread until notified
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                    return -1; // Return -1 in case of an interruption
                }
            }
            int chosenCardIndex = trumpChoice.ordinal(); // Use the ordinal value as the chosen card index
            cardIndexChoice = -1; // Reset for the next round
            return chosenCardIndex; // Return the chosen index to the game
        }
    }
    
    // Method called to select a card by the player
    public void selectCard(int index) {
        synchronized (lock) { // Enter synchronized block to notify after updating the choice
            if (index < 0 || index >= hand.getCards().size()) {
                throw new IllegalArgumentException("Invalid card index.");
            }
            cardIndexChoice = index; // Temporarily reuse trumpChoice for card selection
            lock.notify(); // Wake up the thread waiting for this input
        }
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
        synchronized (lock) { // Enter synchronized block to wait for input
            while (trumpChoice == null) { // Keep waiting until a valid choice is made
                try {
                    System.out.println(this.getName() + " is choosing trump...");
                    lock.wait(); // Pause this thread until notified
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                    return null; // Return null in case of an interruption
                }
            }
            TrumpChoice result = trumpChoice; // Save the chosen suit to return
            trumpChoice = null; // Reset for the next round
            return result; // Return the choice to the game
        }


    }

    // CALLED BY PLAYER
    // Method called when human input arrives
    public void trumpChoice(int choice) {
        synchronized (lock) { // Enter synchronized block to notify after updating the choice
            trumpChoice = switch (choice) {
                case 1 -> Player.TrumpChoice.SPADES;
                case 2 -> Player.TrumpChoice.HEARTS;
                case 3 -> Player.TrumpChoice.DIAMONDS;
                case 4 -> Player.TrumpChoice.CLUBS;
                case 5 -> Player.TrumpChoice.SKIP;
                default -> throw new IllegalArgumentException("Invalid trump choice.");
            }; // Update the trump choice
            lock.notify(); // Wake up the thread waiting for this input
        }
    }

}