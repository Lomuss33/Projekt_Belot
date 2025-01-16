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

void turtleScores(Turtle turtle, int size, List<Player> players) {
    int teamCount = 2;
    turtle.moveTo(turtle.height - 20, turtle.width / 2);
    for (int i = 0; i < teamCount; i++) {
        Player team = players.get(i).getTeam();
        turtle.text(team + ": " + team.getBigs(), Font.COURIER, size, Font.Align.CENTER);
        turtle.backward(3 * size);
    }
}

void turtleTrump(Turtle turtle, int size) {
    turtle.moveTo(turtle.height - 100, turtle.width / 2);
    String trumpTask = """
                    Choose Your Trump Suit Option:
                    0. Skip Trump Selection
                    1. Spades
                    2. Hearts
                    3. Diamonds
                    4. Clubs
                    -> trumpChoice(int choice)
                    Please make your choice (0-4): 
                    """;
    turtle.text(trumpTask, Font.COURIER, size, Font.Align.CENTER);
}

void turtleEnd(Turtle turtle, int size, Team winner) {
    turtle.moveTo(turtle.height / 2, turtle.width / 2);
    turtle.text("We have a WINNER: " + winner.getName(), Font.COURIER, size, Font.Align.CENTER);
    turtle.backward(3 * size);
    turtle.text(winner.getName());
    turtle.backward(3 * size);
    turtle.text("Game over! Scores:", Font.COURIER, size, Font.Align.CENTER);
    turtle.backward(3 * size);
    turtleScores(turtle, size, winner.getPlayers());
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