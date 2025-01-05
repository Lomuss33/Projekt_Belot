package services;

import models.Card;
import models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZvanjeService {

    public enum ZvanjeType { 
        FOUR_JACKS(200), // Jacks
        FOUR_NINES(150), // Nines
        FOUR_OTHERS(100), // Aces, Tens, Kings, Queens
        SEQUENCE_OF_3(20), // 3 cards
        SEQUENCE_OF_4(50), // 4 cards
        SEQUENCE_OF_5_OR_MORE(100), // 5 or more cards
        BELOT(1001), // Full Belot
        BELA(20); // King and Queen in trump suit
    
        private final int points;
    
        ZvanjeType(int points) {
            this.points = points;
        }
    
        public int getPoints() {
            return points;
        }
    }

    public List<ZvanjeType> detectZvanje(Player player, Card.Suit trumpSuit) {
        List<ZvanjeType> zvanjeList = new ArrayList<>();
        List<Card> cards = player.getHand().getCards();

        // Detect Four-of-a-Kind
        Map<Card.Rank, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank));
        rankGroups.forEach((rank, groupedCards) -> {
            if (groupedCards.size() == 4) {
                if (rank == Card.Rank.JACK) zvanjeList.add(ZvanjeType.FOUR_JACKS);
                else if (rank == Card.Rank.NINE) zvanjeList.add(ZvanjeType.FOUR_NINES);
                else zvanjeList.add(ZvanjeType.FOUR_OTHERS);
            }
        });

        // Detect Sequences
        Map<Card.Suit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));
        suitGroups.forEach((suit, groupedCards) -> {
            groupedCards.sort((c1, c2) -> c1.getRank().ordinal() - c2.getRank().ordinal());
            zvanjeList.addAll(detectSequences(groupedCards));
        });

        // Detect Belot
        if (cards.stream().filter(c -> c.getSuit() == trumpSuit)
                .anyMatch(c -> c.getRank() == Card.Rank.KING || c.getRank() == Card.Rank.QUEEN)) {
            zvanjeList.add(ZvanjeType.BELOT);
        }

        return zvanjeList;
    }

    private List<ZvanjeType> detectSequences(List<Card> cards) {
        List<ZvanjeType> sequences = new ArrayList<>();
        List<Card> currentSequence = new ArrayList<>();

        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank().ordinal() + 1 == cards.get(i + 1).getRank().ordinal()) {
                if (currentSequence.isEmpty()) {
                    currentSequence.add(cards.get(i));
                }
                currentSequence.add(cards.get(i + 1));
            } else {
                if (currentSequence.size() >= 3) {
                    sequences.add(getSequenceType(currentSequence.size()));
                }
                currentSequence.clear();
            }
        }

        if (currentSequence.size() >= 3) {
            sequences.add(getSequenceType(currentSequence.size()));
        }

        return sequences;
    }

    private ZvanjeType getSequenceType(int size) {
        switch (size) {
            case 3:
                return ZvanjeType.SEQUENCE_OF_3;
            case 4:
                return ZvanjeType.SEQUENCE_OF_4;
            case 8:
                return ZvanjeType.BELOT;
            default:
                return ZvanjeType.SEQUENCE_OF_5_OR_MORE;
        }
    }

    
}
