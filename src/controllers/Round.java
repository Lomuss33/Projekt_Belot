package controllers;

import java.util.*;
import models.*;
import models.Card.Suit;
import services.RoundUtils;

public class Round {
    private final List<Player> players;
    private final Suit trumpSuit;
    private final int startingPlayerIndex;
    private List<Card> onFloorCards;

    public Round(List<Player> players, int startingPlayerIndex, Suit trumpSuit) {
        this.players = players;
        this.startingPlayerIndex = startingPlayerIndex;
        this.trumpSuit = trumpSuit;
        this.onFloorCards = new ArrayList<>();
    }

    // Play 4 turns, determine the round winner, and return the index of the next starting player
    public int start(int i) {

        // Reset floor cards for the new round
        onFloorCards = new ArrayList<>();

        // Play turns for all players
        playTurns(startingPlayerIndex);

        // Print the cards on the floor for each player                 !!!!!!!!!!!!!!!!!!!!!!!!!!
        for (int turn = 0; turn < players.size(); turn++) {
            Player currentPlayer = players.get((startingPlayerIndex + turn) % 4);
            Card playedCard = onFloorCards.get(turn);
            System.out.println(currentPlayer.getName() + " : " + playedCard);
        }

        // Determine the round winner
        Player winner = returnWinner(startingPlayerIndex);
        // Award 10 points if this is the 7th round
        if (i == 7) {
            winner.getTeam().addSmalls(10);
            System.out.println("Bonus 10 points awarded to " + winner.getTeam().getName() + " for winning the 7th round!");
        }
        
        System.out.println();
        // Add the won cards and their points to the winning player's team
        winner.getTeam().addWonCardsAsPoints(onFloorCards);
    
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

            if (playableIndexes.isEmpty()) {
                throw new IllegalStateException("No playable cards available.");
            }

            int chosenCardIndex = currentPlayer.chooseCardToPlay(playableIndexes);

            // Play the chosen card
            Card playedCard = currentPlayer.playCard(chosenCardIndex);
            onFloorCards.add(playedCard);
        }
    }

    // Determine and award points to the round winner and get starter for the next round
    private Player returnWinner(int startingPlayerIndex) {
        // Assert that there are cards on the floor
        if (onFloorCards.isEmpty()) {
            throw new IllegalStateException("No cards played this round.");
        }        
        // Find the strongest card on the floor
        Suit leadSuit = onFloorCards.get(0).getSuit();
        Card strongestCard = RoundUtils.findStrongestCard(onFloorCards, trumpSuit, leadSuit);

        // Find the player and his team who played the strongest card
        int winningPlayerIndex = (startingPlayerIndex + onFloorCards.indexOf(strongestCard)) % 4;
        Player winningPlayer = players.get(winningPlayerIndex);


        System.out.println();
        System.out.println("WIN CARD: " + strongestCard.toString());
        System.out.println("Round winner: " + winningPlayer.getName());
        System.out.println("Points won: " + onFloorCards.stream().mapToInt(Card::getValue).sum());
        
        return winningPlayer;
    }
}