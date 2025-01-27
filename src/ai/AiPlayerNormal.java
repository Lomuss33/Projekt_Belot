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

        // Step 1: Prepare playable cards
        List<Card> playableCards = new ArrayList<>();
        for (int index : playableIndexes) {
            playableCards.add(hand.getCard(index));
        }

        // Step 2: Decide logic to select a card
        // (1) Prioritize cards that can potentially win the round
        Optional<Card> possibleWinner = playableCards.stream()
            .filter(card -> cardCanWin(card, onFloor, trump)) // Add logic for "cardCanWin()"
            .min(Comparator.comparingInt(Card::getValue));    // Choose the weakest winning strategy (if available)

        if (possibleWinner.isPresent()) {
            Card winningCard = possibleWinner.get();
            return playableIndexes.stream() // Map back to playableIndexes
                    .filter(index -> hand.getCard(index).equals(winningCard))
                    .findFirst()
                    .orElse(playableIndexes.get(0)); // Fallback to the first playable index
        }

        // (2) No winning card, fall back to a different strategy
        // Option 1: Pick the weakest card
        int weakestCardIndex = playableIndexes.stream()
            .min(Comparator.comparingInt(index -> hand.getCard(index).getValue()))
            .orElse(playableIndexes.get(0));

        // **Option 2**: (Make it fun) Randomly select any card if all are weak
        if (playableIndexes.size() > 1) {
            Random random = new Random();
            int randomChoice = playableIndexes.get(random.nextInt(playableIndexes.size()));
            return randomChoice; // Return a random playable card index
        }

        // Return fallback weakest card in case no randomness applies
        return weakestCardIndex;
    }

    /**
     * Helper method to determine if a card can win.
     * Adjust this logic based on your game's rules.
     */
    private boolean cardCanWin(Card card, List<Card> onFloor, Card.Suit trumpSuit) {
        if (onFloor.isEmpty()) {
            // No cards on the floor, the card can win by default
            return true;
        }

        // Find the strongest card currently on the floor
        Card strongestCardOnFloor = onFloor.stream()
            .max(Comparator.comparingInt(c -> c.getStrength(trumpSuit, onFloor.get(0).getSuit())))
            .orElse(null);

        // Check if the provided card is stronger
        return card.getStrength(trumpSuit, onFloor.get(0).getSuit()) > 
            strongestCardOnFloor.getStrength(trumpSuit, onFloor.get(0).getSuit());
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

