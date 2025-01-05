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

public class Game {

    private Difficulty difficulty; // Selected difficulty level
    private Team team1, team2; // Teams
    private int team1Score = 0, team2Score = 0; // Scores
    private ZvanjeService zvanjeService;
    public Deck deck; // Deck of cards
    public List<Player> players; // List of players
    public Card.Suit trumpSuit; // Trump suit
    public Stack<GameState> gameStates; // Stack to store game states for undo
    public int dealerIndex; // Index of the dealer

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

    public void startGame() {
        // Ensure deck is shuffled before dealing
        deck.shuffle();
        // Deal 5 cards to each player
        deck.dealHands(players, 2);
        deck.dealHands(players, 2);
        deck.dealHands(players, 2);
        // Difficulty level
        System.out.println("Game started with difficulty: " + difficulty);
        // Display each player's hand (optional, for testing)
        for (Player player : players) {
            player.displayHand();
        }

        // Call Zvanje
        for (Player player : players) {
            int zvanjePoints = calculateZvanjePoints(player, trumpSuit);
            Team playerTeam = getPlayerTeam(player); // Get the player's team
            if (playerTeam != null) {
                playerTeam.addScore(zvanjePoints); // Add points to the team's score
            }
        }
    }

    // Get the index of the starting player
    public int getStartingPlayerIndex() {
        return (dealerIndex + 1) % 4;
    }

    // Move to the next dealer
    public void nextDealer() {
        dealerIndex = (dealerIndex + 1) % 4;
    }

    // NEEDS TO BE IMPLEMENTED
    // Start a new round
    public void startRound() {
        nextDealer(); // Move to the next dealer
        System.out.println("New round started.");
        System.out.println(players.get(dealerIndex).getName() + " starts this round.");
    
        // Display team scores
        System.out.println(team1.getName() + " - Score: " + team1.getScore() + ", Wins: " + team1.getWins());
        System.out.println(team2.getName() + " - Score: " + team2.getScore() + ", Wins: " + team2.getWins());
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
    
}