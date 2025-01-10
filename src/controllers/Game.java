package controllers;

import java.util.*;
import models.*;
import services.*;

public class Game {

    private final Difficulty difficulty;
    private final Team team1, team2;
    private final List<Player> players;
    private final Deck deck;
    private Card.Suit trumpSuit;
    private int dealerIndex = 3;

    public enum Difficulty {
        EASY, NORMAL, HARD, TEST
    }

    public Game(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.deck = new Deck();
        this.team1 = new Team("HOME");
        this.team2 = new Team("AWAY");
        this.players = GameInitializer.initializePlayers(difficulty, team1, team2);
    }
 
    /* ------------------------------- start game ------------------------------- */
    public void startGame() {
        // Shuffle the deck in front of players
        deck.shuffle();
        // Deal cards to players
        deck.dealHands(players, 6);
        System.out.println("Game started!");
        System.out.println("Choosing trump suit, " + players.get(dealerIndex).getName() + " is the dealer.");

        // Choose trump suit, give last 2 cards and update all card values
        trumpSuit = chooseTrumpSuit(dealerIndex);
        updateCardValues(trumpSuit);
        System.out.println("Trump suit: " + trumpSuit);

        // Determine, announce and award Zvanje
        System.out.println("ZVANJE:");
        Team zvanjeWinningTeam = determineAndAnnounceZvanje();
        System.out.println("Zvanje winning team: " + zvanjeWinningTeam.getName());

        for (int i = 0; i < 4; i++) {
            System.out.println("Round " + (i + 1));
            playRound();
        }

        endGame();
    }
    /* ------------------------------- start game ------------------------------- */

    // Play a round of the game
    private void playRound() {
        Round round = new Round(players);
        round.start();
    }

    // End the game and announce the winner
    private void endGame() {
        Team winner = team1.getScore() > team2.getScore() ? team1 : team2;
        System.out.println("Game over! Winner: " + winner.getName());
        System.out.println("Final scores: " + team1.getName() + " - " + team1.getScore() + ", " + team2.getName() + " - " + team2.getScore());
    }

    // Determine and announce Zvanje results
    private Team determineAndAnnounceZvanje() {
        ZvanjeService zvanjeService = new ZvanjeService();
        Map<Player, List<ZvanjeService.ZvanjeResult>> playerZvannjes = new HashMap<>();

        // Detect Zvanje for all players
        for (Player player : players) {
            List<ZvanjeService.ZvanjeResult> results = zvanjeService.detectZvanje(player, trumpSuit);
            playerZvannjes.put(player, results);
            // Log detected Zvanje for each player
            System.out.println(player.getName() + " Zvanje: " + results.stream()
                .map(res -> res.getZvanjeType() + " (" + res.getCards() + ")")
                .toList());
        }

        // Determine the winning team based on Zvanje
        Team team1 = players.get(0).getTeam(); // Assumes the first player is part of team1
        Team team2 = players.get(2).getTeam(); // Assumes the third player is part of team2

        Team winningTeam = zvanjeService.determineWinningTeam(trumpSuit, players, team1, team2);

        // Calculate and add Zvanje points to the winning team
        int totalPoints = playerZvannjes.entrySet().stream()
            .filter(entry -> winningTeam.getPlayers().contains(entry.getKey()))
            .flatMap(entry -> entry.getValue().stream())
            .mapToInt(res -> res.getZvanjeType().getPoints())
            .sum();
        winningTeam.addScore(totalPoints);

        // Announce Zvanje winner
        System.out.println("Zvanje Winner: " + winningTeam.getName());
        System.out.println("Total Zvanje Points: " + totalPoints);

        // Reveal cards contributing to the Zvanje
        System.out.println("Revealed Cards:");
        playerZvannjes.entrySet().stream()
            .filter(entry -> winningTeam.getPlayers().contains(entry.getKey()))
            .forEach(entry -> {
                System.out.println(entry.getKey().getName() + "'s Zvanje cards: " +
                        entry.getValue().stream()
                        .flatMap(res -> res.getCards().stream())
                        .distinct()
                        .toList());
            });

        return winningTeam;
    }

    // Method to update values for all cards in the deck and players' hands
    public void updateCardValues(Card.Suit trumpSuit) {
        // Update values for all cards in the deck
        for (Card card : deck.getCards()) {
            card.calculateValue(trumpSuit);
        }
        // Update values for all cards in players' hands
        for (Player player : players) {
            for (Card card : player.getHand().getCards()) {
                card.calculateValue(trumpSuit);
            }
        }
    }

    private Card.Suit chooseTrumpSuit(int dealerIndex) {
        int currentIndex = (dealerIndex + 1) % 4; // Player next to the dealer
        int skips = 0;
        Card.Suit chosenSuit = null;

        for (int i = 0; i < 4; i++) {
            Player currentPlayer = players.get(currentIndex);

            // Ask the current player to choose trump suit
            chosenSuit = currentPlayer.chooseTrump();

            if (chosenSuit != null) {
                System.out.println(currentPlayer.getName() + " chose " + chosenSuit);
                deck.dealCards(currentPlayer, 2);
                break;
            } else {
                skips++;
                System.out.println(currentPlayer.getName() + " skipped.");
            }

            // If all players skip, the last player is forced to choose
            if (i == 3 && skips == 3) {
                    System.out.println(currentPlayer + " MUST choose a trump suit:");
                    while (chosenSuit == null) {
                        chosenSuit = currentPlayer.chooseTrump();
                    }
            }
            currentIndex = (currentIndex + 1) % 4; // Move to the next player
            deck.dealCards(currentPlayer, 2);
        }
        return chosenSuit;
    }






















}
