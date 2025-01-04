//   ,d                          ,d     
//   88                          88     
// MM88MMM ,adPPYba, ,adPPYba, MM88MMM  
//   88   a8P_____88 I8[    ""   88     
//   88   8PP"""""""  `"Y8ba,    88     
//   88,  "8b,   ,aa aa    ]8I   88,    
//   "Y888 `"Ybbd8"' `"YbbdP"'   "Y888  
//
                                     
package controllers;

import java.util.List;
import models.*;

public class Main {
    public static void main(String[] args) {
        // Create a new deck
        // Deck deck = new Deck();
        // deck.shuffle();
        // System.out.println("Shuffled Deck");
        // System.out.println(deck);

        // // Test dealing cards
        // List<Card> dealtCards = deck.dealCards(8);
        // System.out.println("Dealt Cards");
        // for (Card card : dealtCards) {
        //     System.out.println(card);
        // }

        // Hand hand = new Hand();
        // hand.addCards(dealtCards);
        // System.out.println(hand);

        Game game = new Game();


        game.players.get(0).getHand().addCard(new Card(Card.Suit.HEARTS, Card.Rank.ACE));
        game.players.get(0).getHand().addCard(new Card(Card.Suit.HEARTS, Card.Rank.KING));
        game.players.get(0).getHand().addCard(new Card(Card.Suit.HEARTS, Card.Rank.QUEEN));
        game.players.get(0).getHand().addCard(new Card(Card.Suit.HEARTS, Card.Rank.JACK));
        game.players.get(0).getHand().addCard(new Card(Card.Suit.HEARTS, Card.Rank.TEN));

        // Simulate moves
        System.out.println(game.players.get(0).getHand());
        game.makeMove(0, 2);
        System.out.println(game.players.get(0).getHand());
        game.makeMove(0, 1); 
        System.out.println(game.players.get(0).getHand());
        // Undo last move
        game.undo();
        System.out.println(game.players.get(0).getHand());
        game.undo();
        System.out.println(game.players.get(0).getHand());
    }  
}
