package services;

import java.util.Comparator;
import models.Card;

// NEEDS TO BE IMPLEMENTED IN GAME LOGIC
public class CardRankComparator implements Comparator<Card> {
    @Override
    public int compare(Card card1, Card card2) {
        return Integer.compare(card1.getRank().ordinal(), card2.getRank().ordinal());
    }
}
