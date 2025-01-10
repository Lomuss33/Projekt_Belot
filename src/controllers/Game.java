package controllers;

import java.util.*;
import models.*;
import services.*;
import services.ZvanjeService.ZvanjeResult;
import services.ZvanjeService.ZvanjeType;

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
        System.err.println(players.get(dealerIndex).getName() + " is the dealer.");
        System.out.println("Choosing trump suit!");

        // Choose trump suit, give last 2 cards and update all card values
        trumpSuit = chooseTrumpSuit(dealerIndex);
        sortAllPlayersHands(players);
        updateCardValues(trumpSuit);

        // Determine, announce and award Zvanje
        System.out.println("ZVANJE:");
        reportZvanje(trumpSuit, dealerIndex);

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

    private ZvanjeResult reportZvanje(Card.Suit trumpSuit, int dealerIndex) {
        ZvanjeService zvanjeService = new ZvanjeService();
        List<ZvanjeResult> zvanjeResults = new ArrayList<>();
    
        // Detect Zvanje for all players and print their cards
        for (Player player : players) {
            ZvanjeResult result = zvanjeService.detectPlayerZvanje(player, trumpSuit);
            zvanjeResults.add(result);
    
            // Print player's cards
            System.out.println();
            System.out.println(player.getName() + "'s cards: " + player.getHand().toString());
            System.out.println(player.getName() + "'s ZvanjeTypes: " + result.getZvanjeTypes());
        }
    
        // Filter out players with no Zvanje
        ZvanjeResult winningZvanjeResult = zvanjeResults.stream()
                .filter(result -> result.getBiggestZvanje() != null) // Skip players without Zvanje
                .max(Comparator.comparing(result -> result.getBiggestZvanje().getPoints()))
                .orElse(null);
    
        if (winningZvanjeResult == null) {
            System.out.println("No Zvanje detected for any player.");
            return null;
        }
    
        Player winningPlayer = winningZvanjeResult.getPlayer();
        Team winningTeam = winningPlayer.getTeam();
    
        // Calculate total points for the winning team
        int totalPoints = calculateTotalZvanjePoints(winningTeam, zvanjeResults);
        winningTeam.addScore(totalPoints);
    
        // Print results
        System.out.println("Player with the highest Zvanje: " + winningPlayer.getName());
        System.out.println("Winning Team: " + winningTeam.getName());
        System.out.println("Total Zvanje Points: " + totalPoints);
    
        // Print ZvanjeTypes for the winning team
        System.out.println("ZvanjeTypes for the winning team:");
                zvanjeResults.stream()
                        .filter(result -> result.getPlayer().getTeam() == winningTeam)
                        .flatMap(result -> result.getZvanjeTypes().stream())
                        .forEach(System.out::println);
    
        return winningZvanjeResult;
    }
    
    
    // Method to calculate total Zvanje points for a given team
    private int calculateTotalZvanjePoints(Team team, List<ZvanjeResult> zvanjeResults) {
        // Filter ZvanjeResults for the given team and sum up the points
        return zvanjeResults.stream()
                .filter(result -> result.getPlayer().getTeam() == team)
                .flatMap(result -> result.getZvanjeTypes().stream())
                .mapToInt(ZvanjeType::getPoints)
                .sum();
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
            System.out.println(currentPlayer.getName() + "'s turn to choose trump suit.");

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
            deck.dealCards(currentPlayer, 2);  // Deal last 2 cards to the current player
        }
        return chosenSuit;
    }

    // Sort all players' hands by suit and rank
    public static void sortAllPlayersHands(List<Player> players) {
        for (Player player : players) {
            player.getHand().sortCards(); // Sort each player's hand in place
        }
    }


















}
