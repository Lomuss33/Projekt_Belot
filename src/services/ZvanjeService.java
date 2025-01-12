package services;

import java.util.*;
import java.util.stream.Collectors;
import models.*;;

public class ZvanjeService {

    public enum ZvanjeType {
        FOUR_JACKS(200), FOUR_NINES(150), FOUR_OTHERS(100), SEQUENCE_OF_3(20),
        SEQUENCE_OF_4(50), SEQUENCE_OF_5_OR_MORE(100), BELOT(1001), BELA(20);

        private final int points;

        ZvanjeType(int points) {
            this.points = points;
        }

        public int getPoints() {
            return points;
        }
    }

    public static class ZvanjeResult {

        private final List<ZvanjeType> zvanjeTypes;
        private final ZvanjeType biggestZvanje;
        private final Player player;
        private int totalPoints;
    
        public ZvanjeResult(Player player, List<ZvanjeType> zvanjeTypes) {
            this(player, zvanjeTypes, 0, null);
        }

        public ZvanjeResult(Player player, List<ZvanjeType> zvanjeTypes, int totalPoints, ZvanjeType biggestZvanje) {
            this.player = player;
            this.zvanjeTypes = new ArrayList<>(zvanjeTypes);
            this.totalPoints = totalPoints;
            this.biggestZvanje = biggestZvanje;
        }
        
        public List<ZvanjeType> getZvanjeTypes() {
            return zvanjeTypes;
        }
    
        public ZvanjeType getBiggestZvanje() {
            return biggestZvanje;
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public void setPoints(int points) {
            this.totalPoints = points;
        }
    
        public Player getPlayer() {
            return player;
        }

        public Team getWinningTeam() {
            return player.getTeam();
        }
    }

    public static ZvanjeResult biggestZvanje(List<ZvanjeResult> results, int dealerIndex) {
        if (results == null || results.isEmpty()) {
            return null; // No results
        }
        // Find the maximum Zvanje points
        int maxPoints = results.stream()
            .filter(result -> result.getBiggestZvanje() != null) // Ignore players with no Zvanje
            .mapToInt(ZvanjeResult::getTotalPoints)
            .max()
            .orElse(0); // Default to 0 if no Zvanje is found
    
        // Filter results with the maximum points
        List<ZvanjeResult> topResults = results.stream()
            .filter(result -> result.getTotalPoints() == maxPoints)
            .collect(Collectors.toList());
    
        if (topResults.size() == 1) {
            return topResults.get(0); // Return the only top result
        }
    
        // Resolve tie using the dealerIndex: Choose the result closest to dealerIndex (right-hand modulo 4)
        return topResults.stream()
            .min(Comparator.comparingInt(result -> (4 + result.getPlayer().getTeam().getPlayers().indexOf(result.getPlayer()) - dealerIndex) % 4))
            .orElse(null);
    }
    

    // Detect all Zvanje types for a player
    public static ZvanjeResult detectPlayerZvanje(Player player, Card.Suit trumpSuit) {
        if (player == null || player.getHand() == null || player.getHand().getCards().isEmpty()) {
            return new ZvanjeResult(player, Collections.emptyList());
        }
    
        List<Card> cards = player.getHand().getCards();
        List<ZvanjeType> detectedZvanjeTypes = new ArrayList<>();
    
        // Detect four-of-a-kind Zvanje
        detectFourOfAKind(groupByRank(cards), detectedZvanjeTypes);
    
        // Detect sequences
        groupBySuit(cards).values().forEach(suitCards -> detectSequences(suitCards, detectedZvanjeTypes));
    
        // Detect special combinations like BELA
        detectBela(cards, trumpSuit, detectedZvanjeTypes);
    
        // Calculate total points and determine the biggest Zvanje
        int totalPoints = detectedZvanjeTypes.stream()
                .mapToInt(ZvanjeType::getPoints)
                .sum();
    
        ZvanjeType biggestZvanje = detectedZvanjeTypes.stream()
                .max(Comparator.comparingInt(ZvanjeType::getPoints))
                .orElse(null);
    
        return new ZvanjeResult(player, detectedZvanjeTypes, totalPoints, biggestZvanje);
    }

    // Group cards by rank
    private static Map<Card.Rank, List<Card>> groupByRank(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getRank));
    }

    // Group cards by suit
    private static Map<Card.Suit, List<Card>> groupBySuit(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getSuit));
    }



    private static void detectFourOfAKind(Map<Card.Rank, List<Card>> rankGroups, List<ZvanjeType> zvanjeTypes) {
        rankGroups.forEach((rank, groupedCards) -> {
            if (groupedCards.size() == 4) {
                ZvanjeType zvanje = switch (rank) {
                    case JACK -> ZvanjeType.FOUR_JACKS;
                    case NINE -> ZvanjeType.FOUR_NINES;
                    case EIGHT, SEVEN -> null; // Skip these cases
                    default -> ZvanjeType.FOUR_OTHERS;
                };
                if (zvanje != null) {
                    zvanjeTypes.add(zvanje);
                }
            }
        });
    }
    

    private static void detectSequences(List<Card> suitCards, List<ZvanjeType> zvanjeTypes) {
        suitCards.sort(Comparator.comparingInt(card -> card.getRank().ordinal()));

        List<Card> currentSequence = new ArrayList<>();
        for (Card card : suitCards) {
            if (!currentSequence.isEmpty() &&
                    card.getRank().ordinal() != currentSequence.get(currentSequence.size() - 1).getRank().ordinal() + 1) {
                evaluateSequence(currentSequence, zvanjeTypes);
                currentSequence.clear();
            }
            currentSequence.add(card);
        }
        evaluateSequence(currentSequence, zvanjeTypes);
    }

    private static void evaluateSequence(List<Card> sequence, List<ZvanjeType> zvanjeTypes) {
        int size = sequence.size();
        if (size >= 3) {
            ZvanjeType zvanje = switch (size) {
                case 8 -> ZvanjeType.BELOT;
                case 5, 6, 7 -> ZvanjeType.SEQUENCE_OF_5_OR_MORE;
                case 4 -> ZvanjeType.SEQUENCE_OF_4;
                case 3 -> ZvanjeType.SEQUENCE_OF_3;
                default -> null;
            };
            if (zvanje != null) {
                zvanjeTypes.add(zvanje);
            }
        }
    }
    private static void detectBela(List<Card> cards, Card.Suit trumpSuit, List<ZvanjeType> zvanjeTypes) {
        List<Card> belaCards = cards.stream()
                .filter(card -> card.getSuit() == trumpSuit &&
                        (card.getRank() == Card.Rank.KING || card.getRank() == Card.Rank.QUEEN))
                .toList();
        if (belaCards.size() == 2) {
            zvanjeTypes.add(ZvanjeType.BELA);
        }
    }
}