
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

    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
        this.hand = new Hand();
    }

    // Abstract method to choose trump
    public abstract Card.Suit chooseTrump();

    // Abstract method to call zvanje
    public abstract List<Card> callZvanje(List<Integer> selectedIndices);
    
    // Abstract method to call dama
    public abstract void callDama();

    // Abstract method to choose a card to be played
    public abstract int chooseCardToPlay(List<Integer> playableIndices);

    public final Card playCard(int index) {
        if (index < 0 || index >= hand.getCards().size()) {
            throw new IllegalArgumentException("Invalid card index.");
        }
        Card card = hand.getCard(index);
        hand.removeCard(index);
        return card;
    }

    // Get the name of the player
    public String getName() {
        return name;
    }

    // Get the hand of the player
    public Hand getHand() {
        return hand;
    }

    // Set the team of the player
    public Team getTeam() {
        return team;
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
