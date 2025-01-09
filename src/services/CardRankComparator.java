package services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import models.Card;

// NEEDS TO BE IMPLEMENTED IN GAME LOGIC
public class CardRankComparator implements Comparator<Card> {
    @Override
    public int compare(Card card1, Card card2) {
        return Integer.compare(card1.getRank().ordinal(), card2.getRank().ordinal());
    }

    
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
}
