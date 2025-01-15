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
    private final Object lock = new Object(); // Lock object for synchronizing threads

    public HumanPlayer(String name, Team team) {
        super(name, team);
    }

    // IMPLEMENTATION NEEDED
    // Choose a card index to play
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