
//   ___...
// / ,-(_`;
// \ )_  _;
// (\[_][_])
//  |  L  |  
//  | \-_/ 
//
// PLAYER
//

package models;

import java.util.List;

// IDEA: maybe create a unique hashcode for backtracing the hand of the player
public abstract class Player {
    
    protected String name;
    protected Hand hand;
    protected Team team;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    // TODO: check before playing for playable cards and store them
    public abstract boolean isCardPlayable(Card card);

    public final Card playCard(int index) {
        if (index < 0 || index >= hand.getCards().size()) {
            throw new IllegalArgumentException("Invalid card index.");
        }
        Card card = hand.getCard(index);
        if (isCardPlayable(card)) {
            hand.removeCard(index);
            return card;
        }
        return null;
    }

    // Abstract method to choose trump
    public abstract Card.Suit chooseTrump();

    // Abstract method to call zvanje
    public abstract List<Card> callZvanje(List<Integer> selectedIndices);
    
    // public abstract void callDama(); ?

    // Get the name of the player
    public String getName() {
        return name;
    }

    // Get the hand of the player
    public Hand getHand() {
        return hand;
    }

    // Display the hand of the player
    public void displayHand() {
        System.out.println(name + "'s Hand:");
        List<Card> cards = hand.getCards();
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ": " + cards.get(i));
        }
    }
}
