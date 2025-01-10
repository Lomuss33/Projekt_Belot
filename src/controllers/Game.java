package controllers;

import java.util.*;
import models.*;
import services.*;

public class Game {

    private final Difficulty difficulty;
    private final Team team1, team2;
    private final List<Player> players;

    public enum Difficulty {
        EASY, NORMAL, HARD, TEST
    }

    public Game(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.team1 = new Team("HOME");
        this.team2 = new Team("AWAY");
        this.players = GameInitializer.initializePlayers(difficulty, team1, team2);
    }

    public void startGame() {
        for (int i = 0; i < 4; i++) {
            System.out.println("Round " + (i + 1));
            playRound();
        }
        endGame();
    }

    private void playRound() {
        Round round = new Round(players);
        round.start();
    }

    private void endGame() {
        Team winner = team1.getScore() > team2.getScore() ? team1 : team2;
        System.out.println("Game over! Winner: " + winner.getName());
        System.out.println("Final scores: " + team1.getName() + " - " + team1.getScore() + ", " + team2.getName() + " - " + team2.getScore());
    }
}
