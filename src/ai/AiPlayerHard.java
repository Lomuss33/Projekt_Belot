// ░░░░░░░░░░░░░░░░░
// ░░░░▄▄████▄▄░░░░░
// ░░░██████████░░░░
// ░░░██▄▄██▄▄██░░░░
// ░░░░▄▀▄▀▀▄▀▄░░░░░
// ░░░▀░░░░░░░░▀░░░░
// ░░░░░░░░░░░░░░░░░
//
// NORMAL AI PLAYER
//

package ai;

import java.util.*;
import models.*;

public class AiPlayerHard extends Player {


    public AiPlayerHard(String name, Team team) {
        super(name, team);
    }

    // Choose the best card to play based on strategy
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
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
            .filter(card -> canWin(card)) // Filter cards that can win
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

    // Determine if the card can win the current round
    private boolean canWin(Card card) {
        // Implement logic to compare the card against the strongest card on the floor
        // Use trumpSuit, leadSuit, and other contextual variables
        return true; // Placeholder for actual logic
    }

    // Choose the trump suit based on the best scoring mechanism
    @Override
    public Card.Suit chooseTrump() {
        Map<Card.Suit, Integer> suitScores = new HashMap<>();
        for (Card card : hand.getCards()) {
            int score = getCardScore(card);
            suitScores.put(card.getSuit(), suitScores.getOrDefault(card.getSuit(), 0) + score);
        }

        // Choose the suit with the highest score
        return suitScores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    // Calculate score for a card based on rank
    private int getCardScore(Card card) {
        return switch (card.getRank()) {
            case JACK -> 20;
            case NINE -> 14;
            case ACE -> 11;
            case TEN -> 10;
            case KING -> 4;
            case QUEEN -> 3;
            default -> 0;
        };
    }

    // Call Dama if the AI has both Queen and King of the trump suit
    @Override
    public void callDama() {
        Card.Suit trumpSuit = chooseTrump();
        boolean hasQueen = hand.getCards().stream()
            .anyMatch(card -> card.getRank() == Card.Rank.QUEEN && card.getSuit() == trumpSuit);
        boolean hasKing = hand.getCards().stream()
            .anyMatch(card -> card.getRank() == Card.Rank.KING && card.getSuit() == trumpSuit);

        if (hasQueen && hasKing) {
            System.out.println(name + " calls Dama in " + trumpSuit);
        }
    }

    // Call Zvanje by detecting high-value combinations
    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        Map<Card.Rank, List<Card>> groupedByRank = groupByRank(hand.getCards());
        Map<Card.Suit, List<Card>> groupedBySuit = groupBySuit(hand.getCards());

        List<Card> zvanjeCards = new ArrayList<>();
        detectFourOfAKind(groupedByRank, zvanjeCards);
        detectSequences(groupedBySuit, zvanjeCards);

        return zvanjeCards;
    }

    // Group cards by rank
    private Map<Card.Rank, List<Card>> groupByRank(List<Card> cards) {
        Map<Card.Rank, List<Card>> groupedByRank = new HashMap<>();
        for (Card card : cards) {
            groupedByRank.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
        }
        return groupedByRank;
    }

    // Group cards by suit
    private Map<Card.Suit, List<Card>> groupBySuit(List<Card> cards) {
        Map<Card.Suit, List<Card>> groupedBySuit = new HashMap<>();
        for (Card card : cards) {
            groupedBySuit.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }
        return groupedBySuit;
    }

    // Detect four-of-a-kind combinations
    private void detectFourOfAKind(Map<Card.Rank, List<Card>> groupedByRank, List<Card> zvanjeCards) {
        for (List<Card> cards : groupedByRank.values()) {
            if (cards.size() == 4) {
                zvanjeCards.addAll(cards);
            }
        }
    }

    // Detect sequences
    private void detectSequences(Map<Card.Suit, List<Card>> groupedBySuit, List<Card> zvanjeCards) {
        for (List<Card> cards : groupedBySuit.values()) {
            cards.sort(Comparator.comparing(card -> card.getRank().ordinal()));
            // Implement sequence detection logic here
        }
    }
}