package controllers;

import models.Card;
import models.Deck;

import java.util.List;

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
        List<Card> dealtCards = deck.dealCards(5);
        System.out.println("Dealt Cards:");
        for (Card card : dealtCards) {
            System.out.println(card);
        }
        System.out.println("Remaining cards in deck: " + deck.remainingCards());

        // Test adding cards back
        deck.addCards(dealtCards);
        System.out.println("Deck after adding dealt cards back:");
        System.out.println(deck);

        // Test reset
        deck.reset();
        System.out.println("Deck after reset:");
        System.out.println(deck);
    }
}
