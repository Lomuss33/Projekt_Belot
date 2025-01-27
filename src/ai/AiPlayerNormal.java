package ai;

import java.util.*;
import models.*;

public class AiPlayerNORMAL extends Player {

    public AiPlayerNORMAL(String name, Team team) {
        super(name, team);
    }

    // Randomly choose a card to play from playable cards
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes, List<Card> onFloor, Card.Suit trump) {
        if (playableIndexes.isEmpty()) {
            throw new IllegalArgumentException("No playable cards available!");
        }

        // Group playable cards
        List<Card> playableCards = new ArrayList<>();
        for (int index : playableIndexes) {
            playableCards.add(hand.getCard(index));
        }

        // Find the weakest card that can win
        Card bestCard = playableCards.stream()
            .min(Comparator.comparingInt(Card::getValue)) // Choose the weakest winning card
            .orElse(null);

        // If no winning card exists, play the lowest card
        if (bestCard == null) {
            return playableIndexes.stream()
                .min(Comparator.comparingInt(index -> hand.getCard(index).getValue()))
                .orElse(playableIndexes.get(0));
        }

        return playableIndexes.get(hand.getCards().indexOf(bestCard));
    }

    // Choose the trump suit based on the most valuable cards in hand
    @Override
    public TrumpChoice chooseTrumpOrSkip(int turnForChoosingTrump) {
        Map<Card.Suit, Integer> suitStrengths = new HashMap<>();
    
        // Calculate the total strength of each suit as if it were the trump suit
        for (Card card : hand.getCards()) {
            Card.Suit suit = card.getSuit();
            suitStrengths.put(
                suit,
                suitStrengths.getOrDefault(suit, 0) + card.getStrength(suit, null) // Treat `suit` as the trump suit
            );
        }
    
        // Find the suit with the highest total strength
        Card.Suit strongestSuit = suitStrengths.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    
        // If turnForChoosingTrump is 3, MUST choose a suit
        if (turnForChoosingTrump == 3) {
            if (strongestSuit == null) {
                // Fallback to the first available suit if no valid strong suit is found
                strongestSuit = suitStrengths.keySet().stream().findFirst().orElse(null);
            }
            if (strongestSuit != null) {
                System.out.println(name + " is forced to choose " + strongestSuit);
                return Player.TrumpChoice.valueOf(strongestSuit.name());
            }
        }
    
        // If no strong suit is found or its strength is <= 30, skip trump selection
        if (strongestSuit == null || suitStrengths.get(strongestSuit) <= 30) {
            System.out.println(name + " chooses to skip.");
            return Player.TrumpChoice.SKIP;
        }
    
        // Return the trump choice corresponding to the strongest suit
        System.out.println(name + " chooses " + strongestSuit);
        return Player.TrumpChoice.valueOf(strongestSuit.name());
    }

    // Group cards by rank
    private Map<Card.Rank, List<Card>> groupByRank(List<Card> cards) {
        Map<Card.Rank, List<Card>> groupedByRank = new HashMap<>();
        for (Card card : cards) {
            groupedByRank.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
        }
        return groupedByRank;
    }
}

