package services;

import java.util.*;
import java.util.stream.Collectors;
import models.Card;
import models.Player;
import models.Team;

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
        private final ZvanjeType zvanjeType;
        private final List<Card> cards;

        public ZvanjeResult(ZvanjeType zvanjeType, List<Card> cards) {
            this.zvanjeType = zvanjeType;
            this.cards = cards;
        }

        public ZvanjeType getZvanjeType() {
            return zvanjeType;
        }

        public List<Card> getCards() {
            return cards;
        }
    }

    // Detect all Zvanje types for a player
    public List<ZvanjeResult> detectZvanje(Player player, Card.Suit trumpSuit) {
        if (player == null || player.getHand() == null || player.getHand().getCards().isEmpty()) {
            return Collections.emptyList();
        }

        List<Card> cards = player.getHand().getCards();
        List<ZvanjeResult> zvanjeResults = new ArrayList<>();

        // Detect Zvanje options
        detectFourOfAKind(groupByRank(cards), zvanjeResults);
        groupBySuit(cards).values().forEach(suitCards -> detectSequences(suitCards, zvanjeResults));

        return zvanjeResults;
    }

    // Group cards by rank
    private Map<Card.Rank, List<Card>> groupByRank(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getRank));
    }

    // Group cards by suit
    private Map<Card.Suit, List<Card>> groupBySuit(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getSuit));
    }

    // Detect four-of-a-kind Zvanje
    private void detectFourOfAKind(Map<Card.Rank, List<Card>> rankGroups, List<ZvanjeResult> zvanjeResults) {
        rankGroups.forEach((rank, groupedCards) -> {
            if (groupedCards.size() == 4) {
                ZvanjeType zvanje = switch (rank) {
                    case JACK -> ZvanjeType.FOUR_JACKS;
                    case NINE -> ZvanjeType.FOUR_NINES;
                    default -> ZvanjeType.FOUR_OTHERS;
                };
                zvanjeResults.add(new ZvanjeResult(zvanje, groupedCards));
            }
        });
    }

    // Detect sequences of cards
    private void detectSequences(List<Card> suitCards, List<ZvanjeResult> zvanjeResults) {
        suitCards.sort(Comparator.comparingInt(card -> card.getRank().ordinal()));

        List<Card> currentSequence = new ArrayList<>();
        for (Card card : suitCards) {
            if (!currentSequence.isEmpty() &&
                    card.getRank().ordinal() != currentSequence.get(currentSequence.size() - 1).getRank().ordinal() + 1) {
                evaluateSequence(currentSequence, zvanjeResults);
                currentSequence.clear();
            }
            currentSequence.add(card);
        }
        evaluateSequence(currentSequence, zvanjeResults);
    }

    private void evaluateSequence(List<Card> sequence, List<ZvanjeResult> zvanjeResults) {
        int size = sequence.size();
        if (size >= 3) {
            ZvanjeType zvanje = switch (size) {
                case 8 -> ZvanjeType.BELOT;
                case 5, 6, 7 -> ZvanjeType.SEQUENCE_OF_5_OR_MORE;
                case 4 -> ZvanjeType.SEQUENCE_OF_4;
                case 3 -> ZvanjeType.SEQUENCE_OF_3;
                default -> null;
            };
            if (zvanje != null) zvanjeResults.add(new ZvanjeResult(zvanje, new ArrayList<>(sequence)));
        }
    }

    public Team determineWinningTeam(
            Card.Suit trumpSuit,
            List<Player> players,
            Team team1,
            Team team2
    ) {
        Map<Player, List<ZvanjeResult>> playerZvannjes = players.stream()
                .collect(Collectors.toMap(player -> player, player -> detectZvanje(player, trumpSuit)));

        ZvanjeResult bestTeam1Zvanje = getBestZvanjeForTeam(team1, playerZvannjes);
        ZvanjeResult bestTeam2Zvanje = getBestZvanjeForTeam(team2, playerZvannjes);

        return (bestTeam1Zvanje != null &&
                (bestTeam2Zvanje == null ||
                        bestTeam1Zvanje.getZvanjeType().getPoints() > bestTeam2Zvanje.getZvanjeType().getPoints()))
                ? team1
                : team2;
    }

    private ZvanjeResult getBestZvanjeForTeam(Team team, Map<Player, List<ZvanjeResult>> playerZvannjes) {
        return team.getPlayers().stream()
                .flatMap(player -> playerZvannjes.getOrDefault(player, Collections.emptyList()).stream())
                .max(Comparator.comparing(zvanje -> zvanje.getZvanjeType().getPoints()))
                .orElse(null);
    }
}