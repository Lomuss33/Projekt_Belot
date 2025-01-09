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

    // Detect all Zvanje for a player
    public List<ZvanjeType> detectZvanje(Player player, Card.Suit trumpSuit) {
        // If player is null or has no cards, return an empty list
        if (player == null || player.getHand() == null || player.getHand().getCards().isEmpty()) {
            return Collections.emptyList();
        }

        List<Card> cards = player.getHand().getCards();
        List<ZvanjeType> zvanjeList = new ArrayList<>();

        // Detect Zvanje options
        detectFourOfAKind(groupByRank(cards), zvanjeList);
        groupBySuit(cards).values().forEach(suitCards -> detectSequences(suitCards, zvanjeList));

        return zvanjeList;
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
    private void detectFourOfAKind(Map<Card.Rank, List<Card>> rankGroups, List<ZvanjeType> zvanjeList) {
        rankGroups.forEach((rank, groupedCards) -> {
            if (groupedCards.size() == 4) {
                ZvanjeType zvanje = switch (rank) {
                    case JACK -> ZvanjeType.FOUR_JACKS;
                    case NINE -> ZvanjeType.FOUR_NINES;
                    default -> ZvanjeType.FOUR_OTHERS;
                };
                zvanjeList.add(zvanje);
            }
        });
    }

    // Detect sequences of cards
    private void detectSequences(List<Card> suitCards, List<ZvanjeType> zvanjeList) {
        suitCards.sort(Comparator.comparingInt(card -> card.getRank().ordinal()));

        List<Card> currentSequence = new ArrayList<>();
        for (Card card : suitCards) {
            if (!currentSequence.isEmpty() && 
                card.getRank().ordinal() != currentSequence.get(currentSequence.size() - 1).getRank().ordinal() + 1) {
                evaluateSequence(currentSequence, zvanjeList);
                currentSequence.clear();
            }
            currentSequence.add(card);
        }
        evaluateSequence(currentSequence, zvanjeList);
    }

    // Evaluate sequence for Zvanje
    private void evaluateSequence(List<Card> sequence, List<ZvanjeType> zvanjeList) {
        int size = sequence.size();
        if (size >= 3) {
            ZvanjeType zvanje = switch (size) {
                case 8 -> { zvanjeList.clear(); yield ZvanjeType.BELOT; }
                case 5, 6, 7 -> ZvanjeType.SEQUENCE_OF_5_OR_MORE;
                case 4 -> ZvanjeType.SEQUENCE_OF_4;
                case 3 -> ZvanjeType.SEQUENCE_OF_3;
                default -> null;
            };
            if (zvanje != null) zvanjeList.add(zvanje);
        }
    }

    // Determine Zvanje results for players and teams
    public Map<String, Object> determineZvanje(List<Player> players, Team team1, Team team2, Card.Suit trumpSuit) {
        Map<Player, List<ZvanjeType>> playerZvannjes = new HashMap<>();
        players.forEach(player -> playerZvannjes.put(player, detectZvanje(player, trumpSuit)));

        Player bestTeam1Player = getStrongestZvanjePlayer(team1, playerZvannjes);
        Player bestTeam2Player = getStrongestZvanjePlayer(team2, playerZvannjes);

        int team1Strength = getZvanjeStrength(playerZvannjes.getOrDefault(bestTeam1Player, Collections.emptyList()));
        int team2Strength = getZvanjeStrength(playerZvannjes.getOrDefault(bestTeam2Player, Collections.emptyList()));

        Team winningTeam = (team1Strength > team2Strength) ? team1 : team2;

        int totalPoints = playerZvannjes.values().stream()
                .flatMap(List::stream)
                .mapToInt(ZvanjeType::getPoints)
                .sum();

        return Map.of("winningTeam", winningTeam, "points", totalPoints);
    }

    // Get the strongest Zvanje player in a team
    private Player getStrongestZvanjePlayer(Team team, Map<Player, List<ZvanjeType>> playerZvannjes) {
        return team.getPlayers().stream()
                .max(Comparator.comparing(player -> getZvanjeStrength(playerZvannjes.get(player))))
                .orElse(null);
    }

    // Get the total Zvanje strength
    private int getZvanjeStrength(List<ZvanjeType> zvanjeList) {
        return zvanjeList == null || zvanjeList.isEmpty()
                ? 0
                : zvanjeList.stream().mapToInt(ZvanjeType::getPoints).sum();
    }
}
