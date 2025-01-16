//package services;

// import java.util.Comparator;
// import java.util.List;
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;
// import models.Card;

public class CardUtils {

    // Get a list of all card indices in a hand / Used in RoundUtils.findPlayableCardIndexes
    public static List<Integer> getAllCardIndices(int handSize) {
        return IntStream.range(0, handSize)
                        .boxed()
                        .collect(Collectors.toList());
    }

    // Sort a list of cards by suit and rank
    public static void sortBySuitAndRank(List<Card> cards) {
        cards.sort(Comparator
                .comparing(Card::getSuit) // Sort by suit (primary)
                .thenComparing(card -> card.getRank().ordinal())); // Sort by rank (secondary)
    }

}
