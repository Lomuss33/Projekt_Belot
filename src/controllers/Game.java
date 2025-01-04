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
import models.*;
import services.*;

import java.util.*;

public class Game {

    // In a new file or within the Game class
    public enum Difficulty {
        EASY,
        NORMAL,
        HARD,
        TEST
    }

    private static final int MAX_POINTS = 1001; // Win treshold

    private Difficulty difficulty; // Selected difficulty level
    public Deck deck; // Deck of cards
    public List<Player> players; // List of players
    public Card.Suit trumpSuit; // Trump suit
    public Stack<GameState> gameStates; // Stack to store game states for undo

    // Constructor initializes the game
    public Game() {
        deck = new Deck();
        players = new ArrayList<>();
        gameStates = new Stack<>();
        difficulty = Difficulty.NORMAL; // Default difficulty
        initializePlayers();
    }

    public void startGame() {
        // Ensure deck is shuffled before dealing
        deck.shuffle();
        // Deal 5 cards to each player
        deck.dealHands(players, 2);
        // Difficulty level
        System.out.println("Game started with difficulty: " + difficulty);
        // Display each player's hand (optional, for testing)
        for (Player player : players) {
            player.displayHand();
        }
    }

    // Method to choose difficulty
    public void chooseDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        players.clear(); // Reset players based on difficulty
        initializePlayers();
    }

    // NEEDS AUTOMATION
    // Initialize 3 AI players and 1 human player
    private void initializePlayers() {
        players.add(new HumanPlayer("YOU")); // Add human player
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

    // IDEA: Example of making a move
    public void makeMove(int playerIndex, int cardIndex) {
        saveGameState(); // Save state before making the move
        Player currentPlayer = players.get(playerIndex);
        currentPlayer.playCard(cardIndex); // Remove card from player's hand
        System.out.println(currentPlayer.getName() + " played " + cardIndex + ". card.");
    }
}