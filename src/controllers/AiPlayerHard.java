// ░░░░░░░░░░░░░░░░░
// ░░░░▄▄████▄▄░░░░░
// ░░░██████████░░░░
// ░░░██▄▄██▄▄██░░░░
// ░░░░▄▀▄▀▀▄▀▄░░░░░
// ░░░▀░░░░░░░░▀░░░░
// ░░░░░░░░░░░░░░░░░
//
// NORMAL AI PLAYER
//

package controllers;

import java.util.*;
import models.Card;
import models.Player;

public class AiPlayerHard extends Player {

    public AiPlayerHard(String name) {
        super(name);
    }

    @Override
    public boolean isCardPlayable(Card card) {
        return true; // Implement actual rule-checking logic
    }

    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        return new ArrayList<>(); // Basic Zvanje logic for Normal AI
    }
    
}
