//  ________________
// |   |,"    `.|   |
// |   /        \   |
// |O _\   />   /_  |   ___ __
// |_(_)'.____.'(_)_|  (")__(")
// [___|[=]__[=]|___]  //    \\
//
// GAME
//

package controllers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import models.*;
import services.*;

public class Game {

    private Difficulty difficulty; // Selected difficulty level
    private final Team team1, team2; // Teams
    private int team1Score = 0, team2Score = 0; // Scores
    private ZvanjeService zvanjeService;
    public Deck deck; // Deck of cards
    public List<Player> players; // List of players
    public Card.Suit trumpSuit; // Trump suit
    public Stack<GameState> gameStates; // Stack to store game states for undo
    public int dealerIndex; // Index of the dealer
    public List<Card> onFloorCards; // Cards on the floor

    // Constructor initializes the game
    public Game() {
        deck = new Deck(); // Create a deck
        zvanjeService = new ZvanjeService(); // Initialize Zvanje service
        players = new ArrayList<>(); // Initialize list of players
        gameStates = new Stack<>(); // Initialize the stack for game states
        difficulty = Difficulty.TEST; // Default difficulty
        team1 = new Team("HOME"); // Create teams
        team2 = new Team("AWAY"); 
        dealerIndex = 0; // Default dealer index, YOU
        initializePlayers(); // Initialize players
    }

    public void startGame() {
        deck.shuffle();
        // Deal 6 cards in 3 rounds
        deck.dealHands(players, 2);
        deck.dealHands(players, 2);
        deck.dealHands(players, 2);

        // Select Trump Suit and Update Card Values
        trumpSuit = chooseTrumpSuit();
        System.out.println("Trump Suit chosen: " + trumpSuit);
        updateCardValues(trumpSuit);
        // Determine and Announce Zvanje
        Team zvanjeWonTeam = determineAndAnnounceZvanje();
        if (zvanjeWonTeam != null) {
            System.out.println(zvanjeWonTeam.getName() + " wins Zvanje!");
        }else {
            System.out.println("No team wins Zvanje.");
        }
        // Display hands (optional)
        players.forEach(Player::displayHand);
        // COMBINE THE ABOVE INTO A METHOD as startOfGame()

        // Start the rounds
        for (int i = 0; i < 8; i++) {
            startRound();
        }        
        players.forEach(Player::displayHand);
    }

    // NEEDS TO BE IMPLEMENTED
        // Get playable cards
        // Check what Cards are playable - isCardPlayable()
        // Choose a card to play, check if Dama/King is played, callDama() if adut
        // Play the card
        // Next players repeat
        // After 4 cards are played, determine the winner
        // Update the scores, check if the game is over (automatically or by player choice)
        // Move to the next dealer
    // Start a new round
    public void startRound() {
        System.out.println("New round started.");
        System.out.println(players.get(dealerIndex).getName() + " starts this round.");
        
        onFloorCards = new ArrayList<>();
    
        for(int turn = 0; turn < 4; turn++) {
            Player currentPlayer = players.get((getStartingPlayerIndex() + turn) % 4); // Get the current player

            List<Integer> playableIndexes = getPlayableCardIndexes(currentPlayer.getHand().getCards(), onFloorCards, trumpSuit); // Get playable cards
            int chosenCardIndex = currentPlayer.chooseCardToPlay(playableIndexes);

            Card playedCard = currentPlayer.playCard(chosenCardIndex);
            onFloorCards.add(playedCard);

            // checkDama(); // Check if Dama is played
            // If King or Queen of trump is played, handle "Bela"
        }
        // Determine the lead suit
        Card.Suit leadSuit = onFloorCards.get(0).getSuit();
        // Determine the winner of the round
        determineTurnWinner(onFloorCards, trumpSuit, leadSuit);

        // Display team scores
        System.out.println(team1.getName() + " - Score: " + team1.getScore() + ", Wins: " + team1.getWins());
        System.out.println(team2.getName() + " - Score: " + team2.getScore() + ", Wins: " + team2.getWins());
        nextDealer(); // Move to the next dealer
    }

    // Get the indexes of playable cards 
    public List<Integer> getPlayableCardIndexes(List<Card> handCards, List<Card> onFloorCards, Card.Suit trumpSuit) {
        List<Integer> playableIndexes = new ArrayList<>();
        // If no cards are played yet, all cards are playable
        if (onFloorCards.isEmpty()) {
            return getAllCardIndices(handCards.size());
        }

        // Determine the lead suit
        Card.Suit leadSuit = onFloorCards.get(0).getSuit();
        // Find the the strongest card on the floor 
        Card strongestCard = getStrongestCard(onFloorCards, trumpSuit, leadSuit);
        // Determine the strength of the strongest card
        int strongestStrength = strongestCard.getStrength(trumpSuit, leadSuit);

        // Find all stronger cards in hand
        for(int i=0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            if(card.getStrength(trumpSuit, leadSuit) > strongestStrength) {
                playableIndexes.add(i);
            }
        }
        // If no stronger card exists, all cards are playable
        if (playableIndexes.isEmpty()) {
            return getAllCardIndices(handCards.size());
        }
        return playableIndexes;
    }
    

    private void determineTurnWinner(List<Card> onFloorCards, Card.Suit trumpSuit, Card.Suit leadSuit) {
        // Assuming CardRankComparator or similar logic determines the strongest card
        Card strongestCard = getStrongestCard(onFloorCards, trumpSuit, leadSuit);

        // Determine which player played the strongest card
        int winningPlayerIndex = -1; // Initialize with an invalid index
        for (int i = 0; i < onFloorCards.size(); i++) {
            if (onFloorCards.get(i) == strongestCard) {
                winningPlayerIndex = (getStartingPlayerIndex() + i) % 4;
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

    // Method to choose difficulty
    public void chooseDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        players.clear(); // Reset players based on difficulty
        initializePlayers();
    }
    
    // Initialize 3 AI players and 1 human player
    private void initializePlayers() {
        players.add(new HumanPlayer("YOU", 1)); // Add human player
        switch (difficulty) {
            case EASY:
                players.add(new AiPlayerEasy("Bot 1"));  // Add AI players...
                players.add(new AiPlayerEasy("Bot 2"));
                players.add(new AiPlayerEasy("Bot 3"));
                break;
            case NORMAL:
                players.add(new AiPlayerNormal("Bot 1"));
                players.add(new AiPlayerNormal("Bot 2"));
                players.add(new AiPlayerNormal("Bot 3"));
                break;
            case HARD:
                players.add(new AiPlayerHard("Bot 1"));
                players.add(new AiPlayerHard("Bot 2"));
                players.add(new AiPlayerHard("Bot 3"));
                break;
            case TEST:
                players.add(new AiPlayerEasy("Bot 1"));
                players.add(new AiPlayerNormal("Bot 2"));
                players.add(new AiPlayerHard("Bot 3"));
                break;
        }
        team1.addPlayer(players.get(0));
        team2.addPlayer(players.get(1));
        team1.addPlayer(players.get(2));
        team2.addPlayer(players.get(3));
    }

    // Get the index of the starting player
    public int getStartingPlayerIndex() {
        return (dealerIndex + 1) % 4;
    }

    // Move to the next dealer
    public void nextDealer() {
        dealerIndex = (dealerIndex + 1) % 4;
    }

    // Get the team of a player
    public Team getPlayerTeam(Player player) {
        if (team1.getPlayers().contains(player)) {
            return team1;
        } else if (team2.getPlayers().contains(player)) {
            return team2;
        }
        return null; // Handle edge case if player is not in any team
    }
    
    // IDEA: Example of making a move
    public void makeMove(int playerIndex, int cardIndex) {
        saveGameState(); // Save state before making the move
        Player currentPlayer = players.get(playerIndex);
        currentPlayer.playCard(cardIndex); // Remove card from player's hand
        System.out.println(currentPlayer.getName() + " played " + cardIndex + ". card.");
    }

    // Undo the last move
    public void undo() {
        if (!gameStates.isEmpty()) {
            GameState lastState = gameStates.pop();
            deck.setCards(lastState.getDeckState());
            for (int i = 0; i < players.size(); i++) {
                players.get(i).getHand().setCards(lastState.getHandsState().get(i));
            }
            System.out.println("Undo successful. Restored to the previous state.");
        } else {
            System.out.println("No moves to undo!");
        }
    }

    public int calculateZvanjePoints(Player player, Card.Suit trumpSuit) {
        List<ZvanjeService.ZvanjeType> zvanjeList = zvanjeService.detectZvanje(player, trumpSuit);
        int totalPoints = zvanjeList.stream().mapToInt(ZvanjeService.ZvanjeType::getPoints).sum();
    
        System.out.println(player.getName() + " announced: " + zvanjeList);
        System.out.println(player.getName() + " scored " + totalPoints + " points for Zvanje.");
        return totalPoints;
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

    // Determine the winning team and points for Zvanje
    private Team determineAndAnnounceZvanje() {
        // Delegate the Zvanje logic to ZvanjeService
        Map<String, Object> zvanjeResult = zvanjeService.determineZvanje(players, team1, team2, trumpSuit);
    
        // Extract the winning team and points from the result
        Team winningTeam = (Team) zvanjeResult.get("winningTeam");
        int points = (int) zvanjeResult.get("points");
    
        // Update scores
        if (winningTeam == team1) {
            team1Score += points;
        } else {
            team2Score += points;
        }
    
        // Announce results
        System.out.println(winningTeam.getName() + " awarded " + points + " points for Zvanje.");
        return winningTeam;
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

    // Save the current state of the game
    private void saveGameState() {
        List<Card> deckState = new ArrayList<>(deck.getCards());
        List<List<Card>> handsState = new ArrayList<>();
        for (Player player : players) {
            handsState.add(new ArrayList<>(player.getHand().getCards()));
        }
        gameStates.push(new GameState(deckState, handsState));
    }

    // Difficulty levels for the game 
    public enum Difficulty {
        EASY,
        NORMAL,
        HARD,
        TEST
    }

    public static List<Integer> getAllCardIndices(int handSize) {
        return IntStream.range(0, handSize)
                        .boxed()
                        .collect(Collectors.toList());
    }    

    public static Card getStrongestCard(List<Card> cards, Card.Suit trumpSuit, Card.Suit leadSuit) {
        return cards.stream()
                    .max(Comparator.comparingInt(c -> c.getStrength(trumpSuit, leadSuit)))
                    .orElseThrow(() -> new IllegalArgumentException("Card list is empty"));
    }    
    
}