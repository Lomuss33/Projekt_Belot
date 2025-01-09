package services;

import java.util.List;

import models.*;
import models.Team;

public class ScoringService {

    // Calculate zvanje points for a player
    public int calculateZvanjePoints(Player player, Card.Suit trumpSuit, ZvanjeService zvanjeService) {
        List<ZvanjeService.ZvanjeType> zvanja = zvanjeService.detectZvanje(player, trumpSuit);
        return zvanja.stream().mapToInt(ZvanjeService.ZvanjeType::getPoints).sum();
    }

    // Update the team scores
    public void updateTeamScores(Team winningTeam, int points) {
        winningTeam.addScore(points);
    }
}
