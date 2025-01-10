package controllers;

import java.util.*;
import models.*;
import models.Card.Suit;
import services.RoundUtils;

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

            // Get playable card indexes and let the player choose one
            List<Integer> playableIndexes = RoundUtils.findPlayableCardIndexes(
                currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);
            
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
}