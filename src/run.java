
Clerk.clear();
Turtle a = new Turtle(600, 600);

// !!!
// Draw the hand of cards
void turtleHand(Turtle turtle, int sizeCard, Card[] cards) {
    int handSize = cards.length;
    int startingWidth = (turtle.width / 2) - handSize * sizeCard;
    turtle.moveTo(20, startingWidth);
    for (int i = 0; i < handSize; i++) {
        turtleCard(turtle, sizeCard, cards[i]);
        turtle.right(90);
        turtle.forward(1.5 * sizeCard);
        turtle.left(90);
    }
}

// Draw a card
// Card.java needed to be imported
void turtleCard(Turtle turtle, int size, Card card) {
    turtle.penDown();
    turtle.forward(8 * size).right(90) // up
            .forward(5 * size).right(90) // right
            .forward(8 * size).right(90) // down
            .forward(5 * size).right(90); // left
    turtle.penUp();
    turtle.forward(5 * size);
    turtle.right(90);
    turtle.forward(2.5 * size);
    turtle.left(90);
    String suit = card.printSuit();
    turtle.text(suit, Font.COURIER, 3 * size , Font.Align.CENTER);
    turtle.backward(2 * size);
    String rank = card.printRank();
    turtle.text(rank, Font.COURIER, 3 * size , Font.Align.CENTER);
}

import java.util.*;

/open models/Card.java
/open models/Deck.java
/open models/Player.java
/open models/Team.java

/open controllers/Round.java
/open controllers/Game.java
/open controllers/Match.java

/open models/CurrentState.java

/open services/GameUtils.java
/open services/CardUtils.java
/open services/GameStateManager.java
/open services/RoundUtils.java
/open services/ZvanjeService.java


//////////////////////////////
/// 
/// After loading this file
/// 
/// run the following commands:
/// 
/// Match match = new Match(Difficulty.HARD);
/// math.startMatch();
/// 
/// First possible game choice is adut selection:
/// 
/// trumpChoice(1);
/// 
/// 
/// 
/// 