// ░░░░░░░▄█▄▄▄█▄
// ▄▀░░░░▄▌─▄─▄─▐▄░░░░▀▄
// █▄▄█░░▀▌─▀─▀─▐▀░░█▄▄█
// ░▐▌░░░░▀▀███▀▀░░░░▐▌
// ████░▄█████████▄░████


package controllers;

import ai.HumanPlayer;
import java.util.*;
import models.*;
import models.Player.TrumpChoice;
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
            System.out.println("Dealer's team didnt pass! Dealer's team: " + dealerTeam.getBigs());
            System.out.println("Other team GETS ALL: " + otherTeam.getBigs());
        }
    }
    

    private void finishGame(Team winner) {
        System.out.println("Game found winner! Winner: " + winner.getName());
    }    

    private ZvanjeResult reportZvanje(Card.Suit trumpSuit, int dealerIndex) {
        List<ZvanjeResult> zvanjeResults = new ArrayList<>();
    
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            // Calculate the current player's index, starting from dealerIndex and wrapping around
            int currentIndex = (dealerIndex + i + 1) % numPlayers;
            Player player = players.get(currentIndex);

            ZvanjeResult result = ZvanjeService.detectPlayerZvanje(player, trumpSuit);
            zvanjeResults.add(result);

            // Print player's cards
            System.out.println();
            player.displayHand();
        }
        ////////////////////////////////////////////////////
        System.out.println();
        for (ZvanjeResult result : zvanjeResults) {
            System.out.println(result.getPlayer().getName() + "'s ZvanjeTypes: " + result.getZvanjeTypes());
        }
        System.out.println();
        // Find the player with the highest Zvanje
        ZvanjeResult winningZvanjeResult = ZvanjeService.biggestZvanje(zvanjeResults, dealerIndex);
    
        // If no Zvanje is detected for any player, return null
        if (winningZvanjeResult == null) {

            System.out.println("No Zvanje detected for any player.");

            return null;
        }
    
        Player winningPlayer = winningZvanjeResult.getPlayer();
        Team winningTeam = winningPlayer.getTeam();
    
        // Calculate total points for the winning player's team
        int totalPoints = calculateTotalZvanjePoints(winningTeam, zvanjeResults);
        // Update the winning ZvanjeResult with the total points
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

    // Method to choose the trump suit by going around the table and forcing a choice if all players skip (Human com: trumpChoice(int choice))
    private Card.Suit chooseTrumpSuit(int dealerIndex) {
        int currentIndex = (dealerIndex + 1) % players.size(); // Player next to the dealer
        int skips = 0;
        TrumpChoice finalChoice = null;

        CurrentState currentState = new CurrentState(team1, team2, players, zvanjeWin, winTreshold, dealerIndex);

        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(currentIndex);
            System.out.println(currentPlayer.getName() + "'s turn to choose trump suit.");
            if(currentPlayer instanceof HumanPlayer) {
                System.out.println("""
                    Choose Your Trump Suit Option:
                    0. Skip Trump Selection
                    1. Spades
                    2. Hearts
                    3. Diamonds
                    4. Clubs
                    -> trumpChoice(int choice)
                    Please make your choice (0-4): 
                    """);
            }

            if (i == players.size() - 1 && skips == players.size()) {
                System.out.println(currentPlayer.getName() + " MUST choose a trump suit:");
            }
            TrumpChoice playerChoice = currentPlayer.chooseTrumpOrSkip(i);

            if (playerChoice != TrumpChoice.SKIP) {
                finalChoice = playerChoice;
                System.out.println(currentPlayer.getName() + " chose " + finalChoice);
                break;
            } else {
                skips++;
                System.out.println(currentPlayer.getName() + " skipped.");
            }
                System.out.println(currentPlayer.getName() + " finally chose " + finalChoice);

            currentIndex = (currentIndex + 1) % players.size(); // Move to the next player
        }

        return finalChoice != null ? finalChoice.getSuit() : null; // Use enum conversion
    }


    // Sort all players' hands by suit and rank
    public static void sortAllPlayersHands(List<Player> players) {
        for (Player player : players) {
            player.getHand().sortCards(); // Sort each player's hand in place
        }
    }



}
