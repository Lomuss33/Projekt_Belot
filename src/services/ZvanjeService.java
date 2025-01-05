package services;

import java.util.*;
import java.util.stream.Collectors;
import models.Card;
import models.Player;
import models.Team;

public class ZvanjeService {

    public enum ZvanjeType {
        FOUR_JACKS(200),
        FOUR_NINES(150),
        FOUR_OTHERS(100),
        SEQUENCE_OF_3(20),
        SEQUENCE_OF_4(50),
        SEQUENCE_OF_5_OR_MORE(100),
        BELOT(1001),
        BELA(20);

        private final int points;

        ZvanjeType(int points) {
            this.points = points;
        }

        public int getPoints() {
            return points;
        }
    }

    public List<ZvanjeType> detectZvanje(Player player, Card.Suit trumpSuit) {
        if (player == null || player.getHand() == null || player.getHand().getCards().isEmpty()) {
            return Collections.emptyList(); // Return an empty list if player has no valid cards
        }
    
        List<ZvanjeType> zvanjeList = new ArrayList<>();
        List<Card> cards = player.getHand().getCards();
    
        // Detect Four-of-a-Kind
        detectFourOfAKind(cards, zvanjeList);
    
        // Detect Sequences
        Map<Card.Suit, List<Card>> groupedBySuit = CardRankComparator.groupAndSortBySuit(cards);
        for (List<Card> suitCards : groupedBySuit.values()) {
            detectSequences(suitCards, zvanjeList);
        }
    
        return zvanjeList; // Return the list, even if it is empty
    }
    

    private void detectFourOfAKind(List<Card> cards, List<ZvanjeType> zvanjeList) {
        Map<Card.Rank, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank));
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

    private void detectSequences(List<Card> suitCards, List<ZvanjeType> zvanjeList) {
        List<Card> currentSequence = new ArrayList<>();
        for (Card card : suitCards) {
            if (!currentSequence.isEmpty() &&
                card.getRank().ordinal() != currentSequence.get(currentSequence.size() - 1).getRank().ordinal() + 1) {
                evaluateSequence(currentSequence, zvanjeList);
                currentSequence.clear();
            }
            currentSequence.add(card);
        }
        evaluateSequence(currentSequence, zvanjeList); // Check last sequence
    }

    private void evaluateSequence(List<Card> sequence, List<ZvanjeType> zvanjeList) {
        if (sequence.size() >= 3) {
            ZvanjeType zvanje = switch (sequence.size()) {
                case 8 -> {
                    zvanjeList.clear(); // Prioritize BELOT
                    yield ZvanjeType.BELOT;
                }
                case 5, 6, 7 -> ZvanjeType.SEQUENCE_OF_5_OR_MORE;
                case 4 -> ZvanjeType.SEQUENCE_OF_4;
                case 3 -> ZvanjeType.SEQUENCE_OF_3;
                default -> null;
            };
            if (zvanje != null) zvanjeList.add(zvanje);
        }
    }

    public Map<String, Object> determineZvanje(List<Player> players, Team team1, Team team2, Card.Suit trumpSuit) {
        Map<Player, List<ZvanjeType>> playerZvannjes = new HashMap<>();
    
        // Detect Zvanje for each player
        for (Player player : players) {
            List<ZvanjeType> zvanja = detectZvanje(player, trumpSuit);
            playerZvannjes.put(player, zvanja != null ? zvanja : Collections.emptyList());
        }
    
        // Find the strongest Zvanje players in each team
        Player bestTeam1Player = getStrongestZvanjePlayer(team1, playerZvannjes);
        Player bestTeam2Player = getStrongestZvanjePlayer(team2, playerZvannjes);
    
        // Compare strengths
        int team1Strength = getZvanjeStrength(playerZvannjes.getOrDefault(bestTeam1Player, Collections.emptyList()));
        int team2Strength = getZvanjeStrength(playerZvannjes.getOrDefault(bestTeam2Player, Collections.emptyList()));
    
        // Determine the winning team
        Team winningTeam = (team1Strength > team2Strength) ? team1 : team2;
    
        // Calculate total points
        int totalPoints = playerZvannjes.values().stream()
                .flatMap(List::stream)
                .mapToInt(ZvanjeType::getPoints)
                .sum();
    
        // Return results in a map
        Map<String, Object> result = new HashMap<>();
        result.put("winningTeam", winningTeam);
        result.put("points", totalPoints);
    
        return result;
    }    

    private Player getStrongestZvanjePlayer(Team team, Map<Player, List<ZvanjeType>> playerZvannjes) {
        return team.getPlayers().stream()
                .max(Comparator.comparing(player -> getZvanjeStrength(playerZvannjes.get(player))))
                .orElse(null);
    }
    
    private int getZvanjeStrength(List<ZvanjeType> zvanjeList) {
        if (zvanjeList == null || zvanjeList.isEmpty()) {
            return 0; // Return 0 strength for null or empty Zvanje list
        }
        return zvanjeList.stream().mapToInt(ZvanjeType::getPoints).sum();
    }
    
    

}
