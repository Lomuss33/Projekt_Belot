// ───────────────▄▄───▐█
// ───▄▄▄───▄██▄──█▀───█─▄
// ─▄██▀█▌─██▄▄──▐█▀▄─▐█▀
// ▐█▀▀▌───▄▀▌─▌─█─▌──▌─▌
// ▌▀▄─▐──▀▄─▐▄─▐▄▐▄─▐▄─▐▄

package controllers;

import java.util.*;
import models.*;
import models.Card.Suit;
import services.RoundUtils;

public class Round {
    private final List<Player> players;
    private final Suit trumpSuit;
    private int startingPlayerIndex;
    private final List<Card> onFloorCards;

    public Round(List<Player> players, int startingPlayerIndex, Suit trumpSuit) {
        this.players = players;
        this.startingPlayerIndex = startingPlayerIndex;
        this.trumpSuit = trumpSuit;
        this.onFloorCards = new ArrayList<>();
    }

    // WHY ALWAYS int 1 return
    // Return winner index of round, if HumanPlayer hast played returns -1
    public int playTurns(int i) {

        for (int turn = 0; turn < players.size(); turn++) { // Resume from the current turn
            Player currentPlayer = players.get((startingPlayerIndex + turn) % 4);
            System.out.println("ROUNDplayTurns() startingplayerindex: " + startingPlayerIndex);
            System.out.println("ROUNDplayTurns()currentPlayer: " + currentPlayer.getName());
            // Skip the player if he has already made a decision
            if(currentPlayer.isWaiting()) {
                System.out.println("ROUNDplayTurns() Skipping " + currentPlayer.getName() + "...");
                continue;
            } 
            System.out.println("ROUNDplayTurns() Waiting for " + currentPlayer.getName() + " to play a card...");
            // Calculate playable card indexes and let the player choose one
            System.out.println("ROUNDplayTurns()onFloorCards: " + onFloorCards);
            int cardBeingPlay = throwCard(currentPlayer);
            System.out.println("ROUNDplayTurns()cardBeingPlay: " + cardBeingPlay);

            if (cardBeingPlay == -1) { // HumanPlayer has not made a choice yet
                System.out.println("ROUNDplayTurns()cardBeingPlay: " + cardBeingPlay);
                System.err.println("ROUNDplayTurns()On floor cards: " + onFloorCards);
                currentPlayer.displayHand(); // Print the hand of the player
                return -1;
            }

        }
        // Determine the round winner
        Player winner = returnWinner(startingPlayerIndex);
        startingPlayerIndex = players.indexOf(winner);

        // Award 10 points if this is the 7th round
            if (i == 7) {
            winner.getTeam().addSmalls(10);
            System.out.println("Bonus 10 points awarded to " + winner.getTeam().getName() + " for winning the 7th round!");
        }
        
        System.out.println();
        // Add the won cards and their points to the winning player's team
        winner.getTeam().addWonCardsAsPoints(onFloorCards);
        System.out.println("onFloorCards: " + onFloorCards);
        // Clear the cards on the floor
        onFloorCards.clear();

        // Return the index of the next starting player
        return players.indexOf(winner);
        
    }

    // INFO: getPlayableIndices() resets
    private List<Integer> calcPlayableIndices(Player currentPlayer) {
        // Get playable card indexes
        List<Integer> playableIndexes = RoundUtils.findPlayableCardIndexes(
            currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);

        // Set the playable card indexes for the player
        currentPlayer.setPlayableIndices(playableIndexes);

        return playableIndexes;
    }
    
    // Winner player index
    // Play each turn in the round 
    private int throwCard(Player currentPlayer) {
        // Get playable card indexes and let the player choose one
        List<Integer> playableIndexes = calcPlayableIndices(currentPlayer);

        if (playableIndexes.isEmpty()) {
            throw new IllegalStateException("No playable cards available.");
        }
 
        // Choose a card to play
        int chosenIndex = currentPlayer.chooseCardToPlay(playableIndexes);

        if(chosenIndex == -1) { // Keep waiting until a valid choice is made
            System.err.println("playableIndexes: " + playableIndexes);
            return -1; // Human Player has not made a choice yet
        }else {
            Card playedCard = currentPlayer.playCard(chosenIndex);

            System.out.println(currentPlayer.getName() + " played: " + playedCard.toString());
            onFloorCards.add(playedCard);
            System.err.println("On floor cards: " + onFloorCards);
            currentPlayer.setPlayableIndices(playableIndexes); // Reset  playable indices
        } 
        currentPlayer.setWaiting(true);
        return chosenIndex;
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