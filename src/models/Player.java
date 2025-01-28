
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

import java.util.ArrayList;
import java.util.List;

public abstract class Player implements Cloneable {
    
    protected String name;
    protected Hand hand;
    protected List<Integer> playableIndices; 
    protected Team team;
    protected boolean waiting;

    public static enum TrumpChoice {
        SKIP(null), // No corresponding suit for SKIP
        SPADES(Card.Suit.SPADES),
        HEARTS(Card.Suit.HEARTS),
        DIAMONDS(Card.Suit.DIAMONDS),
        CLUBS(Card.Suit.CLUBS);

        private final Card.Suit suit;

        private TrumpChoice(Card.Suit suit) {
            this.suit = suit;
        }

        public Card.Suit getSuit() {
            return suit;
        }
    }

    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
        this.hand = new Hand();
        this.waiting = false;
    }

    // Abstract method to choose trump
    public abstract TrumpChoice chooseTrumpOrSkip(int turnForChoosingTrump);
    
    // Abstract method to choose a card to be played
    public abstract int chooseCardToPlay(List<Integer> playableIndexes, List<Card> onFloor, Card.Suit trump);

    public final Card playCard(int index) {
        if (index < 0 || index >= hand.getCards().size()) {
            throw new IllegalArgumentException("Out of bounds card index.");
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

    public boolean isWaiting() {
        return waiting;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWaiting(boolean decisionMade) {
        this.waiting = decisionMade;
    }

    public void setPlayableIndices(List<Integer> playableIndices) {
        this.playableIndices = playableIndices;
    }

    public List<Integer> getPlayableIndices() {
        return playableIndices;
    }

    // Display the hand of the player
    public void displayHand() {
        System.out.println(name + "'s Hand:");
        List<Card> cards = hand.getCards();
        for (int i = 0; i < cards.size(); i++) {
            System.out.println(i + " : " + cards.get(i));
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Player clone() throws CloneNotSupportedException{ 
            Player cloned = (Player) super.clone(); // Shallow copy first
            cloned.hand = (this.hand != null) ? this.hand.clone() : null; // Deep copy of the hand if not null
            cloned.team = this.team; // Deep copy of the team if not null
            cloned.playableIndices = (playableIndices != null) ? new ArrayList<>(this.playableIndices) : null; // Deep copy of the playable indices	
            return cloned;
    }
}
