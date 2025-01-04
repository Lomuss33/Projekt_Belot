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

    private static final int MAX_POINTS = 1001; // Win treshold

    public Deck deck; // Deck of cards
    public List<Player> players; // List of players
    public Scoring scoring; // Scoring service
    public Card.Suit trumpSuit; // Trump suit
    public Stack<GameState> gameStates; // Stack to store game states for undo

    // Constructor initializes the game
    public Game() {
        deck = new Deck();
        players = new ArrayList<>();
        scoring = new Scoring();
        gameStates = new Stack<>();
        initializePlayers();
    }

    // NEEDS AUTOMATION
    // Initialize 3 AI players and 1 human player
    private void initializePlayers() {
        players.add(new HumanPlayer("You")); // Add human player
        players.add(new AiPlayerEasy("AI 1")); // Add AI players...
        players.add(new AiPlayerNormal("AI 2"));
        players.add(new AiPlayerHard("AI 3"));
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