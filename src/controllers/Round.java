package controllers;

import java.util.*;
import models.*;
import models.Card.Suit;
import services.RoundUtils;
import services.ZvanjeService;

public class Round {
    private List<Player> players;
    private Deck deck;
    private List<Card> onFloorCards;
    private Suit trumpSuit;
    private int dealerIndex;

    public Round(List<Player> players) {
        this.players = players;
        this.dealerIndex = 3;
        this.deck = new Deck();
        this.onFloorCards = new ArrayList<>();
    }
    // Start a new round
    public void start() {
        System.out.println("New round started.");

        // Reset floor cards for the new round
        onFloorCards = new ArrayList<>();

        // Shuffle the deck
        deck.shuffle();

        // Deal cards to players
        deck.dealHands(players, 6);

        // Choose trump suit and update card values
        trumpSuit = chooseTrumpSuit();
        updateCardValues(trumpSuit);

        // Determine and announce Zvanje
        Team zvanjeWinningTeam = determineAndAnnounceZvanje();

        // Play turns for all players
        System.out.println(players.get(dealerIndex).getName() + " starts this round.");
        playTurns();

        // Determine and award the round winner
        awardRoundWinner();

        // Move to the next dealer
        nextDealer();
    }

    // Play each turn in the round 
    private void playTurns() {
        for (int turn = 0; turn < players.size(); turn++) {
            Player currentPlayer = players.get((getStartingPlayerIndex() + turn) % players.size());
            System.out.println(currentPlayer.getName() + "'s turn.");
            System.out.println("Player Hand Cards: " + currentPlayer.getHand().getCards());

            // Get playable card indexes and let the player choose one
            List<Integer> playableIndexes = RoundUtils.findPlayableCardIndexes(
                currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);
            
            System.out.println("Playable cards: " + playableIndexes);

            int chosenCardIndex = currentPlayer.chooseCardToPlay(playableIndexes);

            // Play the chosen card
            Card playedCard = currentPlayer.playCard(chosenCardIndex);
            onFloorCards.add(playedCard);

            // checkDama(); // Check if Dama is played
            // If King or Queen of trump is played, handle "Bela"
        }
    }

    // Determine and award points to the round winner
    private void awardRoundWinner() {
        if (onFloorCards.isEmpty()) {
            System.out.println("Error: No cards played this round.");
            return;
        }

        Suit leadSuit = onFloorCards.get(0).getSuit();
        Card strongestCard = RoundUtils.findStrongestCard(onFloorCards, trumpSuit, leadSuit);

        // Find the player who played the strongest card
        int winningPlayerIndex = (getStartingPlayerIndex() +
                onFloorCards.indexOf(strongestCard)) % players.size();
        Player winningPlayer = players.get(winningPlayerIndex);
        Team winningTeam = winningPlayer.getTeam();

        // Calculate and award points
        int totalPoints = onFloorCards.stream().mapToInt(Card::getValue).sum();
        winningTeam.addScore(totalPoints);

        // Announce the winner
        System.out.println(winningPlayer.getName() + " wins the round with " + strongestCard);
        System.out.println(winningTeam.getName() + " awarded " + totalPoints + " points.");
    }

    // Get the index of the starting player
    private int getStartingPlayerIndex() {
        return (dealerIndex + 1) % players.size();
    }

    // Move to the next dealer
    private void nextDealer() {
        dealerIndex = (dealerIndex + 1) % players.size();
    }

    private Card.Suit chooseTrumpSuit() {
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
        System.out.println("Card values updated for trump suit: " + trumpSuit);
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


}
