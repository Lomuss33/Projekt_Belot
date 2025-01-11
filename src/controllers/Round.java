package controllers;

import java.util.*;
import models.*;
import models.Card.Suit;
import services.RoundUtils;

public class Round {
    private final List<Player> players;
    private List<Card> onFloorCards;
    private Suit trumpSuit;
    private final int startingPlayerIndex;

    public Round(List<Player> players, int startingPlayerIndex) {
        this.players = players;
        this.startingPlayerIndex = startingPlayerIndex;
        this.onFloorCards = new ArrayList<>();
    }

    // Play 4 turns, determine the round winner, and return the index of the next starting player
        // Determine and award the round winner    
    public int start(int i) {
        // Reset floor cards for the new round
        onFloorCards = new ArrayList<>();
        // Play turns for all players
        playTurns(startingPlayerIndex);
        // Determine and award the round winner
        Player winner = awardAndReturnWinner(startingPlayerIndex);
        System.out.println(onFloorCards);
        // Award 10 points if this is the 7th round
        if (i == 7) {
            winner.getTeam().addSmalls(10);
            System.out.println("Bonus 10 points awarded to " + winner.getTeam().getName() + " for winning the 7th round!");
        }
    
        // Return the index of the next starting player
        return players.indexOf(winner);
    }
    
    // Play each turn in the round 
    private void playTurns(int startingPlayerIndex) {
        for (int turn = 0; turn < players.size(); turn++) {
            Player currentPlayer = players.get((startingPlayerIndex + turn) % 4);

            // Get playable card indexes and let the player choose one
            List<Integer> playableIndexes = RoundUtils.findPlayableCardIndexes(
                currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);
            
            int chosenCardIndex = currentPlayer.chooseCardToPlay(playableIndexes);

            // Play the chosen card
            Card playedCard = currentPlayer.playCard(chosenCardIndex);
            onFloorCards.add(playedCard);
        }
    }

    // Determine and award points to the round winner and get starter for the next round
    private Player awardAndReturnWinner(int startingPlayerIndex) {
        // Find the strongest card on the floor
        Suit leadSuit = onFloorCards.get(0).getSuit();
        Card strongestCard = RoundUtils.findStrongestCard(onFloorCards, trumpSuit, leadSuit);

        // Find the player and his team who played the strongest card
        int winningPlayerIndex = (startingPlayerIndex + onFloorCards.indexOf(strongestCard)) % 4;
        Player winningPlayer = players.get(winningPlayerIndex);
        Team winningTeam = winningPlayer.getTeam();

        // Award the winning players team with the won cards and their points
        winningTeam.addWonCardsAndPoints(onFloorCards);

        return winningPlayer;
    }
}