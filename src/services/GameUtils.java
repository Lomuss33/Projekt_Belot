package services;

import controllers.AiPlayerEasy;
import controllers.AiPlayerHard;
import controllers.AiPlayerNormal;
import controllers.Game.Difficulty;
import controllers.HumanPlayer;
import java.util.ArrayList;
import java.util.List;
import models.*;
import services.ZvanjeService.ZvanjeResult;

public class GameUtils {

    // Initialize players and assign them to the given teams
    public static List<Player> initializePlayers(Difficulty difficulty, Team team1, Team team2) {
        List<Player> players = new ArrayList<>();

        // Assign human player to Team 1
        players.add(new HumanPlayer("YOU", team1));
        team1.addPlayer(players.get(0)); // Add to Team 1

        // Create AI players based on difficulty
        switch (difficulty) {
            case EASY:
                players.add(new AiPlayerEasy("Bot 1", team2)); // Bot 1 in Team 2
                players.add(new AiPlayerEasy("Bot 2", team1)); // Bot 2 in Team 1
                players.add(new AiPlayerEasy("Bot 3", team2)); // Bot 3 in Team 2
                break;
            case NORMAL:
                players.add(new AiPlayerNormal("Bot 1", team2));
                players.add(new AiPlayerNormal("Bot 2", team1));
                players.add(new AiPlayerNormal("Bot 3", team2));
                break;
            case HARD:
                players.add(new AiPlayerHard("Bot 1", team2));
                players.add(new AiPlayerHard("Bot 2", team1));
                players.add(new AiPlayerHard("Bot 3", team2));
                break;
            case TEST:
                players.add(new AiPlayerEasy("Bot 1", team2));
                players.add(new AiPlayerNormal("Bot 2", team1));
                players.add(new AiPlayerHard("Bot 3", team2));
                break;
        }
        // Add players to their respective teams
        players.forEach(player -> player.getTeam().addPlayer(player));

        return players;
    }

    public static Team winnerAchieved(Team team1, Team team2, ZvanjeResult zvanjeWin) {
        if (calculateTotalScore(team1, zvanjeWin) >= 1001) {
            return team1;
        } else if (calculateTotalScore(team2, zvanjeWin) >= 1001) {
            return team2;
        }
        return null;
    }
    
    public static int calculateTotalScore(Team team, ZvanjeResult zvanjeWin) {
        int cardPoints = team.getWonCards().stream().mapToInt(Card::getValue).sum();
        int zvanjePoints = (zvanjeWin != null && zvanjeWin.getWinningTeam() == team) ? zvanjeWin.getTotalPoints() : 0;
        return cardPoints + team.getScore() + zvanjePoints;
    }    

    public static int calculateWinThreshold(ZvanjeResult zvanjeWin) {
        int basePoints = 162; // Base points for a "čista igra"
        int zvanjePoints = (zvanjeWin != null) ? zvanjeWin.getTotalPoints() : 0;
        int totalPoints = basePoints + zvanjePoints;
        // Threshold to pass is half the total points plus 1
        return (totalPoints / 2) + 1;
    }    

}
