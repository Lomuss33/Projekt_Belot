package controllers;

import java.util.*;

import models.Card;
import models.Player;

public class AiPlayerEasy extends Player {

    public AiPlayerEasy(String name) {
        super(name);
    }

    @Override
    public boolean isCardPlayable(Card card) {
        return true; // Implement actual rule-checking logic
    }

    @Override
    public Card playCard(int index) {
        Card playedCard = hand.getCard(index);
        hand.removeCard(index);
        return playedCard;
    }


    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        return new ArrayList<>(); // Basic Zvanje logic for Normal AI
    }
    
}
