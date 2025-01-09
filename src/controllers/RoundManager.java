package controllers;

import java.util.ArrayList;
import java.util.List;

import models.*;
import models.Card.Suit;
import services.CardUtils;
import services.RoundUtils;

public class RoundManager {
    private List<Card> onFloorCards;

    // Starts a round
    public void startRound(List<Player> players, Card.Suit trumpSuit, Deck deck, int dealerIndex) {
        onFloorCards = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            List<Integer> playableIndexes = RoundUtils.findPlayableCardIndexes(
                currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);
            int chosenCardIndex = currentPlayer.chooseCardToPlay(playableIndexes);
            Card playedCard = currentPlayer.playCard(chosenCardIndex);
            onFloorCards.add(playedCard);
        }
        Suit leadSuit = onFloorCards.get(0).getSuit();
        determineTurnWinner(onFloorCards, trumpSuit, leadSuit, dealerIndex, players);
    }

    // Determines the turn winner
    public static void determineTurnWinner(
            List<Card> onFloorCards, Card.Suit trumpSuit, Card.Suit leadSuit, int dealerIndex, List<Player> players) {
        Card strongestCard = RoundUtils.findStrongestCard(onFloorCards, trumpSuit, leadSuit);
        int winningPlayerIndex = findWinningPlayerIndex(onFloorCards, strongestCard, dealerIndex);
        if (winningPlayerIndex == -1) {
            System.out.println("Error: Could not determine the winner.");
            return;
        }
        Player winningPlayer = players.get(winningPlayerIndex);
        Team winningTeam = winningPlayer.getTeam();
        int totalPoints = onFloorCards.stream().mapToInt(Card::getValue).sum();

        // Award points and announce
        winningTeam.addScore(totalPoints);
        System.out.println(winningPlayer.getName() + " wins the round with " + strongestCard);
        System.out.println(winningTeam.getName() + " awarded " + totalPoints + " points.");
    }

    // Finds the index of the winning player
    private static int findWinningPlayerIndex(List<Card> onFloorCards, Card strongestCard, int dealerIndex) {
        for (int i = 0; i < onFloorCards.size(); i++) {
            if (onFloorCards.get(i) == strongestCard) {
                return (getStartingPlayerIndex(dealerIndex) + i) % onFloorCards.size();
            }
        }
        return -1;
    }

    // Gets the starting player's index
    public static int getStartingPlayerIndex(int dealerIndex) {
        return (dealerIndex + 1) % 4;
    }
}
