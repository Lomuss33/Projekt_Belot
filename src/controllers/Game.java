package controllers;

import java.util.*;
import models.*;
import services.*;

public class Game {

    private final Difficulty difficulty;
    private final Team team1, team2;
    private final List<Player> players;
    
    private final ZvanjeService zvanjeService;
    private final GameStateManager gameStateManager;

    /* ----------------------------- Initilize game ----------------------------- */
    public Game(Difficulty difficulty) {
        
        this.zvanjeService = new ZvanjeService();
        this.gameStateManager = new GameStateManager();

        /////

        this.difficulty = difficulty;
        this.players = new ArrayList<>();
        this.team1 = new Team("HOME");
        this.team2 = new Team("AWAY");

        GameInitializer.initializePlayers(difficulty, team1, team2);
    }

/////////////////////////  SLEEP IN PROGRESS  /////////////////////////


    public void startGame() {
        gameInitializer.dealInitialCards(deck, players);
        trumpSuit = TrumpSelector.selectTrump(players, deck, dealerIndex);
        cardService.updateCardValues(deck, players, trumpSuit);

        Team zvanjeWinner = zvanjeService.determineZvanje(players, team1, team2, trumpSuit);
        if (zvanjeWinner != null) {
            System.out.println(zvanjeWinner.getName() + " wins Zvanje!");
        }

        RoundManager roundManager = new RoundManager();
        for (int i = 0; i < 8; i++) {
            roundManager.startRound(players, trumpSuit, dealerIndex, team1, team2);
        }
    }

    public void chooseDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        gameInitializer.initializePlayers(difficulty, players, team1, team2);
    }

    public void undo() {
        gameStateManager.undo(deck, players);
    }

    public void nextDealer() {
        dealerIndex = (dealerIndex + 1) % 4;
    }

    public enum Difficulty {
        EASY,
        NORMAL,
        HARD,
        TEST
    }
}
