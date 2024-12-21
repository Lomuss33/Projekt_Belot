package controllers;

import models.Card;
import models.Card.Suit;
import models.Card.Rank;

public class Main {
    public static void main(String[] args) {
        Card card = new Card(Suit.HEARTS, Rank.NINE);
        card.calculateValue(Suit.HEARTS);
        System.out.println(card);
    }
}
