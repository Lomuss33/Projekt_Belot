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
    private Player roundWinner;

    public Round(List<Player> players, int startingPlayerIndex) {
        this.players = players;
        this.startingPlayerIndex = startingPlayerIndex;
        this.onFloorCards = new ArrayList<>();
    }

    // Play 4 turns, determine the round winner, and return the index of the next starting player
    public int start() {
        // Reset floor cards for the new round
        onFloorCards = new ArrayList<>();
        // Play turns for all players
        System.out.println(players.get(startingPlayerIndex).getName() + " starts this round.");
        playTurns(startingPlayerIndex);
        // Determine and award the round winner
        return players.indexOf(awardAndReturnWinner(startingPlayerIndex));
    }

    // Play each turn in the round 
    private void playTurns(int startingPlayerIndex) {
        for (int turn = 0; turn < players.size(); turn++) {
            Player currentPlayer = players.get((startingPlayerIndex + turn) % 4);
            System.out.println(currentPlayer.getName() + "'s turn.");

            // Get playable card indexes and let the player choose one
            List<Integer> playableIndexes = RoundUtils.findPlayableCardIndexes(
                currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);
            
            int chosenCardIndex = currentPlayer.chooseCardToPlay(playableIndexes);

            // Play the chosen card
            Card playedCard = currentPlayer.playCard(chosenCardIndex);
            onFloorCards.add(playedCard);
            System.out.println("! Floor: " + onFloorCards.size() + " cards on the floor: " + onFloorCards);
        }
    }

    // Determine and award points to the round winner and get starter for the next round
    private Player awardAndReturnWinner(int startingPlayerIndex) {
        // Find the strongest card on the floor
        Suit leadSuit = onFloorCards.get(0).getSuit();
        Card strongestCard = RoundUtils.findStrongestCard(onFloorCards, trumpSuit, leadSuit);

        // Find the player who played the strongest card
        int winningPlayerIndex = (startingPlayerIndex +
                onFloorCards.indexOf(strongestCard)) % 4;
        Player winningPlayer = players.get(winningPlayerIndex);
        Team winningTeam = winningPlayer.getTeam();

        winningTeam.addWonCard(onFloorCards);

        return winningPlayer;
    }
}