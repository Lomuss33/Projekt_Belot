package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import controllers.Game;
import models.*;

public class GameStateManager {

    private Stack<GameState> gameStates = new Stack<>(); // Stack of all game states

    //     // Save the current state of the game
    // private void saveGameState() {
    //     List<Card> deckState = new ArrayList<>(deck.getCards());
    //     List<List<Card>> handsState = new ArrayList<>();
    //     for (Player player : players) {
    //         handsState.add(new ArrayList<>(player.getHand().getCards()));
    //     }
    //     gameStates.push(new GameState(deckState, handsState));
    // }
    public void saveGameState(Game game) {
        List<Card> deckState = new ArrayList<>(deck.getCards());
        List<List<Card>> handsState = players.stream()
                .map(player -> new ArrayList<>(player.getHand().getCards()))
                .collect(Collectors.toList());
        gameStates.push(new GameState(deckState, handsState));
    }


    // Undo the last move and restore the last game state
    public void undo(Deck deck, List<Player> players) {
        if (gameStates.isEmpty()) {
            System.out.println("No moves to undo!");
            return;
        }
        GameState lastState = gameStates.pop();
        deck.setCards(lastState.getDeckState());
        for (int i = 0; i < players.size(); i++) {
            players.get(i).getHand().setCards(lastState.getHandsState().get(i));
        }
    }

    // // Undo the last move
    // public void undo() {
    //     if (!gameStates.isEmpty()) {
    //         GameState lastState = gameStates.pop();
    //         deck.setCards(lastState.getDeckState());
    //         for (int i = 0; i < players.size(); i++) {
    //             players.get(i).getHand().setCards(lastState.getHandsState().get(i));
    //         }
    //         System.out.println("Undo successful. Restored to the previous state.");
    //     } else {
    //         System.out.println("No moves to undo!");
    //     }
    // }
}
