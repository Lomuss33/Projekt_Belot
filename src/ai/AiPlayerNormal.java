package ai;

import java.util.*;
import models.*;

public class AiPlayerNormal extends Player {

    public AiPlayerNormal(String name, Team team) {
        super(name, team);
    }

    // Randomly choose a card to play from playable cards
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
        if (playableIndexes.isEmpty()) {
            throw new IllegalArgumentException("No playable cards available!");
        }
        Random random = new Random();
        return playableIndexes.get(random.nextInt(playableIndexes.size()));
    }

    // Call Dama if the player has Queen and King of the trump suit
    @Override
    public void callDama() {
        Map<Card.Suit, List<Card>> groupedBySuit = groupBySuit(hand.getCards());
        for (Map.Entry<Card.Suit, List<Card>> entry : groupedBySuit.entrySet()) {
            List<Card> cards = entry.getValue();
            boolean hasQueen = cards.stream().anyMatch(card -> card.getRank() == Card.Rank.QUEEN);
            boolean hasKing = cards.stream().anyMatch(card -> card.getRank() == Card.Rank.KING);

            if (hasQueen && hasKing) {
                System.out.println(name + " calls Dama in " + entry.getKey());
                return;
            }
        }
    }

    // Choose Zvanje cards
    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        return new ArrayList<>(); // Keep this basic for easy AI
    }

    // Choose the trump suit based on the most valuable cards in hand
    @Override
    public Card.Suit chooseTrump() {
        Map<Card.Suit, Integer> suitValues = new HashMap<>();
        for (Card card : hand.getCards()) {
            suitValues.put(card.getSuit(), suitValues.getOrDefault(card.getSuit(), 0) + card.getValue());
        }

        // Find the suit with the highest value
        return suitValues.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    // Group cards by suit
    private Map<Card.Suit, List<Card>> groupBySuit(List<Card> cards) {
        Map<Card.Suit, List<Card>> groupedBySuit = new HashMap<>();
        for (Card card : cards) {
            groupedBySuit.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }
        return groupedBySuit;
    }
}

