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
        Deck deck = new Deck();
        System.out.println("Initial Deck:");
        System.out.println(deck);

        // Test shuffle
        deck.shuffle();
        System.out.println("Shuffled Deck:");
        System.out.println(deck);

        // Test dealing cards
        List<Card> dealtCards = deck.dealCards(15);
        System.out.println("Dealt Cards:");
        for (Card card : dealtCards) {
            System.out.println(card);
        }
        System.out.println("Remaining cards in deck: " + deck);

        // Test reset
        deck.reset();
        System.out.println("Deck after reset:");
        System.out.println(deck);
    }
}
