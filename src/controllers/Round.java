// ───────────────▄▄───▐█
// ───▄▄▄───▄██▄──█▀───█─▄
// ─▄██▀█▌─██▄▄──▐█▀▄─▐█▀
// ▐█▀▀▌───▄▀▌─▌─█─▌──▌─▌
// ▌▀▄─▐──▀▄─▐▄─▐▄▐▄─▐▄─▐▄

package controllers;

import controllers.Match.Difficulty;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import models.*;
import models.Card.Suit;

public class Round implements Cloneable {
    private List<Player> players;
    private Suit trumpSuit;
    private int startingPlayerIndex;
    private List<Card> onFloorCards;
    private Match.Difficulty difficulty;

    public Round(List<Player> players, int startingPlayerIndex, Suit trumpSuit, Match.Difficulty difficulty) {
        this.players = players;
        this.startingPlayerIndex = startingPlayerIndex;
        this.trumpSuit = trumpSuit;
        this.difficulty = difficulty;
        this.onFloorCards = new ArrayList<>();
    }

    @Override
    protected Round clone() throws CloneNotSupportedException {
        Round cloned = (Round) super.clone();
        // Shallow clone the players and on-floor cards
        cloned.players = this.players != null ? new ArrayList<>(this.players) : new ArrayList<>();
        cloned.onFloorCards = this.onFloorCards != null ? new ArrayList<>(this.onFloorCards) : new ArrayList<>();
        if (this.onFloorCards != null) {
            for (Card card : this.onFloorCards) {
                cloned.onFloorCards.add(card.clone());
            }
        }
        cloned.trumpSuit = this.trumpSuit;
        cloned.startingPlayerIndex = this.startingPlayerIndex;
        return cloned;
    }

    // Return winner index of round, if HumanPlayer hast played returns -1
    protected int playTurns(int i) {
        for (int turn = 0; turn < players.size(); turn++) { // Resume from the current turn
            Player currentPlayer = players.get((startingPlayerIndex + turn) % 4);
            // Skip the player if he has already made a decision
            if(currentPlayer.isWaiting()) continue;
            // Calculate playable card indexes and let the player choose one
            int cardBeingPlay = throwCard(currentPlayer);
            if (cardBeingPlay == -1) { // HumanPlayer has not made a choice yet
                printOnFloorCards(startingPlayerIndex);
                currentPlayer.displayHand(); // Print the hand of the player
                return -1;
            }
        }
        printOnFloorCards(startingPlayerIndex);
        System.out.println();
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
        // Clear the cards on the floor
        onFloorCards.clear();
        // Return the index of the next starting player
        return players.indexOf(winner);
    }

    // INFO: getPlayableIndices() resets
    private List<Integer> calcPlayableIndices(Player currentPlayer) {
        List<Integer> playableIndexes;
        if (difficulty == Difficulty.LEARN || difficulty == Difficulty.PRO) {
            // If difficulty is LEARN, all indices from hand size are playable
            playableIndexes = new ArrayList<>();
            for (int i = 0; i < currentPlayer.getHand().getSize(); i++) {
                playableIndexes.add(i);
            }
        } else {
            // Get playable card indexes
            playableIndexes = findPlayableCardIndexes(
                currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);
        }

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
        
        int chosenIndex = currentPlayer.chooseCardToPlay(playableIndexes, onFloorCards, trumpSuit);

        if(chosenIndex == -1) { // Keep waiting until a valid choice is made
            return -1; // Human Player has not made a choice yet
        }else {
            Card playedCard = currentPlayer.playCard(chosenIndex);
            onFloorCards.add(playedCard);
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
        Card strongestCard = findStrongestCard(onFloorCards, trumpSuit, leadSuit);

        // Find the player and his team who played the strongest card 
        int winningPlayerIndex = (startingPlayerIndex + onFloorCards.indexOf(strongestCard)) % 4;
        Player winningPlayer = players.get(winningPlayerIndex);

        System.out.println();
        System.out.println("WIN CARD: " + strongestCard.toString());
        System.out.println("Round winner: " + winningPlayer.getName());
        System.out.println("Points won: " + onFloorCards.stream().mapToInt(Card::getValue).sum());

        return winningPlayer;
    }

    // findPlayableCardIndexes
    private static List<Integer> findPlayableCardIndexes(List<Card> handCards, List<Card> onFloorCards, Card.Suit trumpSuit) {
        List<Integer> playableIndexes = new ArrayList<>();
        // If no cards are played yet, the player is starting and can play any card
        if (onFloorCards.isEmpty()) {
            // System.out.println("No cards on the floor. RoundUtils()");
            return getAllCardIndices(handCards.size());
        }
        // Determine the lead suit: trump suit if present on the floor, otherwise the suit of the first card
        Card.Suit leadSuit = onFloorCards.stream()
                                         .anyMatch(card -> card.getSuit() == trumpSuit) 
                                         ? trumpSuit 
                                         : onFloorCards.get(0).getSuit();

        Card strongestCard = findStrongestCard(onFloorCards, trumpSuit, leadSuit);
        int strongestStrength = strongestCard.getStrength(trumpSuit, leadSuit);
        // Step 1: Find all cards in hand that follow the lead suit or trump suit
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            boolean isLeadSuit = card.getSuit() == leadSuit;
            boolean isTrumpSuit = card.getSuit() == trumpSuit;
            // Rule: Only stronger cards can be played unless there are no stronger cards
            if ((isLeadSuit || isTrumpSuit) && card.getStrength(trumpSuit, leadSuit) > strongestStrength) {
                playableIndexes.add(i);
            }
        }
        // Step 2: If no stronger cards exist, allow any card to be played
        if (playableIndexes.isEmpty()) {
            // System.out.println("No stronger cards found. RoundUtils()");
            return getAllCardIndices(handCards.size()); // All cards are playable
        }
        //System.out.println("RoundUtils() Playable card indexes: " + playableIndexes);
        return playableIndexes;
    }
    // findPlayableCardIndexes

    private static Card findStrongestCard(List<Card> cards, Card.Suit trumpSuit, Card.Suit leadSuit) {
        return cards.stream()
                    .max(Comparator.comparingInt(c -> c.getStrength(trumpSuit, leadSuit)))
                    .orElseThrow(() -> new IllegalArgumentException("Card list is empty"));
    }

    // Get a list of all card indices in a hand / Used in RoundUtils.findPlayableCardIndexes
    private static List<Integer> getAllCardIndices(int handSize) {
        return IntStream.range(0, handSize)
                        .boxed()
                        .collect(Collectors.toList());
    }

    private void printOnFloorCards(int startingPlayerIndex) {
        System.out.println("On floor cards:");
        // Loop through the on-floor cards and print each with the player who placed it
        for (int i = 0; i < onFloorCards.size(); i++) {
            Card card = onFloorCards.get(i);
            // Calculate the index of the player who placed the card
            int playerIndex = (startingPlayerIndex + i) % players.size();
            Player player = players.get(playerIndex);
    
            // Print the card and the corresponding player's name
            System.out.println(card + " : " + player.getName());
        }
        System.out.println();
    }

    public List<Card> getOnFloorCards() {
        return onFloorCards;
    }

    public int getStartingPlayerIndex() {
        return startingPlayerIndex;
    }

    public Player getPlayerIndex(int index) {
        return players.get(index);
    }

    public Suit getTrumpSuit() {
        return trumpSuit;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public void setOnFloorCards(List<Card> onFloorCards) {
        this.onFloorCards = onFloorCards;
    }

    public void setStartingPlayerIndex(int startingPlayerIndex) {
        this.startingPlayerIndex = startingPlayerIndex;
    }

    public void setTrumpSuit(Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    } 
    
}