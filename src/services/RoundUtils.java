package services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import models.Card;

public class RoundUtils {
    
    public static List<Integer> findPlayableCardIndexes(List<Card> handCards, List<Card> onFloorCards, Card.Suit trumpSuit) {
        if (onFloorCards.isEmpty()) {
            return CardUtils.getAllCardIndices(handCards.size());
        }

        Card.Suit leadSuit = onFloorCards.get(0).getSuit();
        Card strongestCard = findStrongestCard(onFloorCards, trumpSuit, leadSuit);
        int strongestStrength = strongestCard.getStrength(trumpSuit, leadSuit);

        List<Integer> playableIndexes = new ArrayList<>();
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            if (card.getStrength(trumpSuit, leadSuit) > strongestStrength) {
                playableIndexes.add(i);
            }
        }
        return playableIndexes.isEmpty() ? CardUtils.getAllCardIndices(handCards.size()) : playableIndexes;
    }

    public static Card findStrongestCard(List<Card> cards, Card.Suit trumpSuit, Card.Suit leadSuit) {
        return cards.stream()
                    .max(Comparator.comparingInt(c -> c.getStrength(trumpSuit, leadSuit)))
                    .orElseThrow(() -> new IllegalArgumentException("Card list is empty"));
    }
}
