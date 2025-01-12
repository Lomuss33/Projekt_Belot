package controllers;

import java.util.*;
import models.*;
import services.*;
import services.ZvanjeService.ZvanjeResult;
import services.ZvanjeService.ZvanjeType;

public class Game {

    private final Team team1, team2;
    private final List<Player> players;
    private ZvanjeResult zvanjeWin;
    private int winTreshold;
    private final Deck deck;
    private Card.Suit trumpSuit;
    private final int dealerIndex;

    public enum Difficulty {
        EASY, NORMAL, HARD, TEST
    }

    public Game(List<Player> players, Team team1, Team team2, int dealerIndex) {
        this.players = players;
        this.team1 = team1;
        this.team2 = team2;
        this.deck = new Deck();
        this.zvanjeWin = null;
        this.dealerIndex = dealerIndex;        
    }
 
    /* ------------------------------- start game ------------------------------- */
    public void startGame() {
        System.out.println("Game started!");

        // Initialize the game
        initializeGame();
    
        // Play 1 deck of rounds and check for a winner
        playRounds();

        // Award points to the winning team
        awardGameVictory();

        // Find game winner team
        Team winner = lookForWinner();

        if (winner != null) {
            System.out.println("Final scores: " + team1.getName() + " - " + team1.getBigs() + ", " + team2.getName() + " - " + team2.getBigs());
            finishGame(winner);
        }
    
    }

    public Team lookForWinner() {
        // Check if a team has crossed the win threshold
        Team winner = GameUtils.findGameWinner(team1, team2, zvanjeWin, winTreshold);
        if (winner != null) {
            System.out.println("Game over! Winner: " + winner.getName()); 
        } else {
            System.out.println("Game continues. Current scores: " + team1.getName() + " - " + team1.getBigs() + ", " + team2.getName() + " - " + team2.getBigs());
        }
        return winner;
    }


    public void initializeGame() {
        // Shuffle the deck in front of players
        deck.initializeDeck();

        // Secure reset small points for both teams
        team1.resetSmalls();
        team2.resetSmalls();

        // Assert that the deck size is 32
        if (deck.getCards().size() != 32) {
            throw new IllegalStateException("Deck size must be 32 cards.");
        }
        // Deal 6 cards to players to choose trump suit
        deck.dealAllHands(players, 6);

        System.err.println(players.get(dealerIndex).getName() + " is the dealer.");
    
        // Choose trump suit
        trumpSuit = chooseTrumpSuit(dealerIndex);

        // Assert that deck has 8 cards left after dealing all cards and that trump suit is chosen
        if (deck.getCards().size() != 8 || trumpSuit == null) {
            throw new IllegalStateException("Choosing Trump Suit failed.");
        }

        // Sort all hands, deal last 2 cards and update card values
        updateCardValues(trumpSuit);
        deck.dealAllHands(players, 2);
        sortAllPlayersHands(players);

        // Determine, announce, and award Zvanje
        System.out.println("ZVANJE:");
        zvanjeWin = reportZvanje(trumpSuit, dealerIndex);

        // Calculate the winning threshold
        winTreshold = GameUtils.calculateWinThreshold(zvanjeWin);

    }
    
    /* ------------------------------- start game ------------------------------- */

    // Play 8 rounds and check for a winner after each round
    private void playRounds() {

        // Assert that last player has 8 cards after dealing all cards
        if (players.get((dealerIndex + 3) % 4).getHand().getCards().size() != 8) {
            throw new IllegalStateException("Players should have 8 cards.");
        }
        
        int startingPlayerIndex = (dealerIndex + 1) % 4; // Player next to the dealer
        for (int i = 0; i < 8; i++) {
            System.out.println("Round " + (i + 1));
            // Start a new round and get the winner's index
            Round round = new Round(players, startingPlayerIndex, trumpSuit);
            int winnerIndex = round.start(i);
            // Winner starts the next round
            startingPlayerIndex = winnerIndex;
        }
    }

    public void awardGameVictory() {
        // Check if the dealer's team has crossed the win threshold
        Team dealerTeam = players.get(dealerIndex).getTeam();
        Team otherTeam = (dealerTeam == team1) ? team2 : team1;

        int dealerTeamPoints = GameUtils.calculateGamePoints(dealerTeam, zvanjeWin);
        int otherTeamPoints = GameUtils.calculateGamePoints(otherTeam, zvanjeWin);

        // Check if the dealer's team has crossed the win threshold
        if (dealerTeamPoints >= winTreshold) {
            dealerTeam.addBigs(dealerTeamPoints);
            otherTeam.addBigs(otherTeamPoints);
            System.out.println();
            System.out.println("Dealer's team PASSED!: " + dealerTeamPoints);
            System.out.println("Other team: " + otherTeamPoints);
        } else {
            // Award points to the other team
            otherTeam.addBigs(otherTeamPoints + dealerTeamPoints);
            System.out.println();
            System.out.println("Dealer's team didnt pass! Other team: " + otherTeamPoints);
        }
    }
    

    private void finishGame(Team winner) {
        System.out.println("Game found winner! Winner: " + winner.getName());
    }    

    private ZvanjeResult reportZvanje(Card.Suit trumpSuit, int dealerIndex) {
        ZvanjeService zvanjeService = new ZvanjeService();
        List<ZvanjeResult> zvanjeResults = new ArrayList<>();
    
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            // Calculate the current player's index, starting from dealerIndex and wrapping around
            int currentIndex = (dealerIndex + i + 1) % numPlayers;
            Player player = players.get(currentIndex);

            ZvanjeResult result = zvanjeService.detectPlayerZvanje(player, trumpSuit);
            zvanjeResults.add(result);

            // Print player's cards
            System.out.println();
            player.displayHand();
        }
    
        for (ZvanjeResult result : zvanjeResults) {
            System.out.println(result.getPlayer().getName() + "'s ZvanjeTypes: " + result.getZvanjeTypes());
        }

        // Find the player with the highest Zvanje
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
    
        // Calculate total points for the winning player's team
        int totalPoints = calculateTotalZvanjePoints(winningTeam, zvanjeResults);

        winningZvanjeResult.setPoints(totalPoints);

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
