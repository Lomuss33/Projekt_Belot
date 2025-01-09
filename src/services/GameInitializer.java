package services;

import java.util.ArrayList;
import java.util.List;

import controllers.AiPlayerEasy;
import controllers.AiPlayerHard;
import controllers.AiPlayerNormal;
import controllers.Game.Difficulty;
import controllers.HumanPlayer;
import models.*;

public class GameInitializer {

    // Initialize 3 AI players and 1 human player
    public void initializePlayers(Difficulty difficulty, List<Player> players, Team team1, Team team2) {

        players.add(new HumanPlayer("YOU", 1)); // Add human player
        
        switch (difficulty) {
            case EASY:
                players.add(new AiPlayerEasy("Bot 1"));  // Add AI players...
                players.add(new AiPlayerEasy("Bot 2"));
                players.add(new AiPlayerEasy("Bot 3"));
                break;
            case NORMAL:
                players.add(new AiPlayerNormal("Bot 1"));
                players.add(new AiPlayerNormal("Bot 2"));
                players.add(new AiPlayerNormal("Bot 3"));
                break;
            case HARD:
                players.add(new AiPlayerHard("Bot 1"));
                players.add(new AiPlayerHard("Bot 2"));
                players.add(new AiPlayerHard("Bot 3"));
                break;
            case TEST:
                players.add(new AiPlayerEasy("Bot 1"));
                players.add(new AiPlayerNormal("Bot 2"));
                players.add(new AiPlayerHard("Bot 3"));
                break;
        }
        team1.addPlayer(players.get(0));
        team2.addPlayer(players.get(1));
        team1.addPlayer(players.get(2));
        team2.addPlayer(players.get(3));
    }

    // public List<Player> getPlayers() {
    //     return players;
    // }

    // public Deck getDeck() {
    //     return deck;
    // }

    // public Team getTeam1() {
    //     return team1;
    // }

    // public Team getTeam2() {
    //     return team2;
    // }
}
