//package services;

// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;
// import models.Card;

public class RoundUtils {
    
    public static List<Integer> findPlayableCardIndexes(List<Card> handCards, List<Card> onFloorCards, Card.Suit trumpSuit) {
        List<Integer> playableIndexes = new ArrayList<>();
    
        // If no cards are played yet, the player is starting and can play any card
        if (onFloorCards.isEmpty()) {
            // System.out.println("No cards on the floor. RoundUtils()");
            return CardUtils.getAllCardIndices(handCards.size());
        }
    
        // Determine the lead suit: trump suit if present on the floor, otherwise the suit of the first card
        Card.Suit leadSuit = onFloorCards.stream()
                                         .anyMatch(card -> card.getSuit() == trumpSuit) 
                                         ? trumpSuit 
                                         : onFloorCards.get(0).getSuit();

        Card strongestCard = findStrongestCard(onFloorCards, trumpSuit, leadSuit);
        int strongestStrength = strongestCard.getStrength(trumpSuit, leadSuit);
    
        // Step 1: Find all cards in hand that follow the lead suit or trump suit
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            boolean isLeadSuit = card.getSuit() == leadSuit;
            boolean isTrumpSuit = card.getSuit() == trumpSuit;
    
            // Rule: Only stronger cards can be played unless there are no stronger cards
            if ((isLeadSuit || isTrumpSuit) && card.getStrength(trumpSuit, leadSuit) > strongestStrength) {
                playableIndexes.add(i);
            }
        }
    
        // Step 2: If no stronger cards exist, allow any card to be played
        if (playableIndexes.isEmpty()) {
            // System.out.println("No stronger cards found. RoundUtils()");
            return CardUtils.getAllCardIndices(handCards.size());
        }

        //System.out.println("RoundUtils() Playable card indexes: " + playableIndexes);
        return playableIndexes;
    }
    

    public static Card findStrongestCard(List<Card> cards, Card.Suit trumpSuit, Card.Suit leadSuit) {
        return cards.stream()
                    .max(Comparator.comparingInt(c -> c.getStrength(trumpSuit, leadSuit)))
                    .orElseThrow(() -> new IllegalArgumentException("Card list is empty"));
    }

}
