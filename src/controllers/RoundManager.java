package controllers;

import java.util.ArrayList;
import java.util.List;

import models.*;
import models.Card.Suit;
import services.CardService;

public class RoundManager {
    private List<Card> onFloorCards;

    public void startRound(List<Player> players, Card.Suit trumpSuit, Deck deck, int dealerIndex) {
        onFloorCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player currentPlayer = players.get(i);
            List<Integer> playableIndexes = CardService.getPlayableCardIndexes(
                currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);
            int chosenCardIndex = currentPlayer.chooseCardToPlay(playableIndexes);
            Card playedCard = currentPlayer.playCard(chosenCardIndex);
            onFloorCards.add(playedCard);
        }
        Suit leadSuit = onFloorCards.get(0).getSuit();
        determineTurnWinner(onFloorCards, trumpSuit, leadSuit, dealerIndex);
    }

    public static void determineTurnWinner(List<Card> onFloorCards, Card.Suit trumpSuit, Card.Suit leadSuit, int dealerIndex) {
        // Assuming CardRankComparator or similar logic determines the strongest card
        Card strongestCard = CardService.getStrongestCard(onFloorCards, trumpSuit, leadSuit);

        // Determine which player played the strongest card
        int winningPlayerIndex = -1; // Initialize with an invalid index
        for (int i = 0; i < onFloorCards.size(); i++) {
            if (onFloorCards.get(i) == strongestCard) {
                winningPlayerIndex = (getStartingPlayerIndex(dealerIndex) + i) % 4;
                break;
            }
        }
        if (winningPlayerIndex == -1) {
            System.out.println("Error: Could not find the player who played the strongest card.");
            return;
        }
        Player winningPlayer = players.get(winningPlayerIndex);
        Team winningTeam = getPlayerTeam(winningPlayer);

        int totalPoints = onFloorCards.stream().mapToInt(Card::getValue).sum();
    
        // Assign points or other actions
        System.out.println(winningPlayer.getName() + " wins the round with " + strongestCard);
    
        // Award points to the winning team
        winningTeam.addScore(totalPoints);

        // Announce the winner and points
        System.out.println(winningPlayer.getName() + " wins the round with " + strongestCard);
        System.out.println(winningTeam.getName() + " awarded " + totalPoints + " points.");
    }   

    /* ----------------- // Get the index of the starting player ---------------- */
    public static int getStartingPlayerIndex(int dealerIndex) {
        return (dealerIndex + 1) % 4;
    }
}

