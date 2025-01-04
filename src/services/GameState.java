package services;

import models.Card;
import java.util.List;

public class GameState {
    private List<Card> deckState;
    private List<List<Card>> handsState;

    public GameState(List<Card> deckState, List<List<Card>> handsState) {
        this.deckState = deckState;
        this.handsState = handsState;
    }

    public List<Card> getDeckState() {
        return deckState;
    }

    public List<List<Card>> getHandsState() {
        return handsState;
    }
}
