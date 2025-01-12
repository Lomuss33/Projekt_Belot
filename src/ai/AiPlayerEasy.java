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
package ai;

import java.util.*;
import models.*;

public class AiPlayerEasy extends Player {

    public AiPlayerEasy(String name, Team team) {
        super(name, team);
    }

    // Randomly choose a card to play
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
        if (playableIndexes.isEmpty()) {
            throw new IllegalArgumentException("No playable cards available!");
        }
        Random random = new Random();
        return playableIndexes.get(random.nextInt(playableIndexes.size())); // Pick a random card
    }

    // Randomly choose a trump suit
    @Override
    public Card.Suit chooseTrump() {
        Card.Suit[] suits = Card.Suit.values();
        Random random = new Random();
        Card.Suit chosenSuit = suits[random.nextInt(suits.length)];
        System.out.println(name + " randomly chooses " + chosenSuit + " as trump suit.");
        return chosenSuit;
    }

    // Randomly decide to call Dama (even if it doesn't make sense)
    @Override
    public void callDama() {
        Random random = new Random();
        if (random.nextBoolean()) {
            System.out.println(name + " calls Dama randomly, even if it's not valid!");
        } else {
            System.out.println(name + " decides not to call Dama.");
        }
    }

    // Return an empty list for Zvanje (no detection logic)
    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        System.out.println(name + " does not know how to detect Zvanje.");
        return new ArrayList<>(); // Always returns an empty list
    }
}
