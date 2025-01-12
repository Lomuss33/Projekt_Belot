package services;

import java.util.*;
import models.*;

public class GameStateManager {

    private Stack<GameState> gameStates = new Stack<>(); // Stack to store game states

    /**
     * Saves the current game state to the stack.
     * @param onFloorCards Cards currently on the floor.
     * @param players List of players with their current hands.
     * @param team1 First team with its state.
     * @param team2 Second team with its state.
     * @param zvanjeResult Current Zvanje results (if any).
     */
    
    public void saveGameState(
        List<Card> onFloorCards,
        List<Player> players,
        Team team1,
        Team team2,
        ZvanjeService.ZvanjeResult zvanjeResult
    ) {
        // Save the state of each player's hand
        List<List<Card>> handsState = new ArrayList<>();
        for (Player player : players) {
            handsState.add(new ArrayList<>(player.getHand().getCards())); // Deep copy of each hand
        }

        // Save the state of the teams
        TeamState team1State = new TeamState(team1.getBigs(), team1.getSmalls(), team1.getWonCards());
        TeamState team2State = new TeamState(team2.getBigs(), team2.getSmalls(), team2.getWonCards());

        // Create and push the new game state
        gameStates.push(new GameState(
            new ArrayList<>(onFloorCards), // Deep copy of on-floor cards
            handsState,
            team1State,
            team2State,
            zvanjeResult
        ));
    }

    /**
     * Restores the last saved game state.
     * @param onFloorCards Reference to the list of cards on the floor to update.
     * @param players List of players to update their hands.
     * @param team1 First team to update its state.
     * @param team2 Second team to update its state.
     * @return True if the state was successfully restored; false if no state exists.
     */
    public boolean goBackLastState(
        List<Card> onFloorCards,
        List<Player> players,
        Team team1,
        Team team2
    ) {
        if (gameStates.isEmpty()) {
            return false; // No state to restore
        }

        // Pop the last saved state
        GameState lastState = gameStates.pop();

        // Restore on-floor cards
        onFloorCards.clear();
        onFloorCards.addAll(lastState.getOnFloorCards());

        // Restore players' hands
        List<List<Card>> handsState = lastState.getHandsState();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).getHand().setCards(handsState.get(i));
        }

        // Restore teams' state
        TeamState team1State = lastState.getTeam1State();
        team1.setBigs(team1State.getBigs());
        team1.setSmalls(team1State.getSmalls());
        team1.resetWonCards();
        team1.getWonCards().addAll(team1State.getWonCards());

        TeamState team2State = lastState.getTeam2State();
        team2.setBigs(team2State.getBigs());
        team2.setSmalls(team2State.getSmalls());
        team2.resetWonCards();
        team2.getWonCards().addAll(team2State.getWonCards());

        return true;
    }
}

/**
 * Encapsulates the state of a game at a given point in time.
 */
class GameState {
    private final List<Card> onFloorCards;
    private final List<List<Card>> handsState;
    private final TeamState team1State;
    private final TeamState team2State;
    private final ZvanjeService.ZvanjeResult zvanjeResult;

    public GameState(
        List<Card> onFloorCards,
        List<List<Card>> handsState,
        TeamState team1State,
        TeamState team2State,
        ZvanjeService.ZvanjeResult zvanjeResult
    ) {
        this.onFloorCards = onFloorCards;
        this.handsState = handsState;
        this.team1State = team1State;
        this.team2State = team2State;
        this.zvanjeResult = zvanjeResult;
    }

    public List<Card> getOnFloorCards() {
        return onFloorCards;
    }

    public List<List<Card>> getHandsState() {
        return handsState;
    }

    public TeamState getTeam1State() {
        return team1State;
    }

    public TeamState getTeam2State() {
        return team2State;
    }

    public ZvanjeService.ZvanjeResult getZvanjeResult() {
        return zvanjeResult;
    }
}

/**
 * Encapsulates the state of a team.
 */
class TeamState {
    private final int bigs;
    private final int smalls;
    private final List<Card> wonCards;

    public TeamState(int bigs, int smalls, List<Card> wonCards) {
        this.bigs = bigs;
        this.smalls = smalls;
        this.wonCards = new ArrayList<>(wonCards); // Deep copy
    }

    public int getBigs() {
        return bigs;
    }

    public int getSmalls() {
        return smalls;
    }

    public List<Card> getWonCards() {
        return wonCards;
    }
}
