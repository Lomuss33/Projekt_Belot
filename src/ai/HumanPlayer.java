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
    public Card.Suit chooseTrump() {
        return Card.Suit.HEARTS; // Implement actual trump suit selection logic
    }
    // Choose suit based on input string
    public Card.Suit chooseSuit(String suit) {
        switch (suit.toUpperCase()) {
            case "HEARTS":
                return Card.Suit.HEARTS;
            case "DIAMONDS":
                return Card.Suit.DIAMONDS;
            case "CLUBS":
                return Card.Suit.CLUBS;
            case "SPADES":
                return Card.Suit.SPADES;
            case "SKIP":
                return null; // or handle skip case appropriately
            default:
                throw new IllegalArgumentException("Invalid suit: " + suit);
        }
    }

}