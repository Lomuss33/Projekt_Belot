package services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import models.Card;

public class CardService implements Comparator<Card> {

    /* ---------------- Compare two cards based on their rank ---------------- */
    @Override
    public int compare(Card card1, Card card2) {
        return Integer.compare(card1.getRank().ordinal(), card2.getRank().ordinal());
    }

    // Group and sort cards by suit
    public static Map<Card.Suit, List<Card>> groupAndSortBySuit(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(
                        Card::getSuit,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(c -> c.getRank().ordinal()))
                                        .collect(Collectors.toList()))));
    }


    /* ------------------  Get the indexes of playable cards ------------------ */
    public static List<Integer> getPlayableCardIndexes(List<Card> handCards, List<Card> onFloorCards, Card.Suit trumpSuit) {
        List<Integer> playableIndexes = new ArrayList<>();
        // If no cards are played yet, all cards are playable
        if (onFloorCards.isEmpty()) {
            return getAllCardIndices(handCards.size());
        }

        // Determine the lead suit
        Card.Suit leadSuit = onFloorCards.get(0).getSuit();
        // Find the the strongest card on the floor 
        Card strongestCard = CardService.getStrongestCard(onFloorCards, trumpSuit, leadSuit);
        // Determine the strength of the strongest card
        int strongestStrength = strongestCard.getStrength(trumpSuit, leadSuit);

        // Find all stronger cards in hand
        for(int i=0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            if(card.getStrength(trumpSuit, leadSuit) > strongestStrength) {
                playableIndexes.add(i);
            }
        }
        // If no stronger card exists, all cards are playable
        if (playableIndexes.isEmpty()) {
            return getAllCardIndices(handCards.size());
        }
        return playableIndexes;
    }

    /* ------------- // Get the strongest card from a list of cards ------------- */
    public static Card getStrongestCard(List<Card> cards, Card.Suit trumpSuit, Card.Suit leadSuit) {
        return cards.stream()
                    .max(Comparator.comparingInt(c -> c.getStrength(trumpSuit, leadSuit)))
                    .orElseThrow(() -> new IllegalArgumentException("Card list is empty"));
    }    
    
    /* ------------------------- // Get all card indices ------------------------ */
    public static List<Integer> getAllCardIndices(int handSize) {
    return IntStream.range(0, handSize)
                    .boxed()
                    .collect(Collectors.toList());
    }    
}


