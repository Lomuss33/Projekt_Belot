public class TurtlePlay {

    public Turtle turtle;
    public Match match;


    public TurtlePlay(Turtle turtle) {
        // Create a new match
        this.turtle = turtle;
        this.match = new controllers.Match(controllers.Game.Difficulty.EASY);
    }

    public Turtle drawStart() { 
        turtle.penUp();
        turtle.left(90);
        turtle.text("Welcome to Bela!", Font.COURIER, 20, Font.Align.CENTER);
        turtle.backward(40);
        turtle.text("Start game with play()", Font.COURIER, 20, Font.Align.CENTER);
        return turtle;
    }

    public Turtle play() {
        // Start the game
        match.play();
        // Draw the hand of cards
        turtleHand(turtle, 20, match.players.get(0).getHand());
        // Draw scores for players
        turtleScores(turtle, 20, match.players);
        // Display trump selection prompt
        turtleTrump(turtle, 20);
        return turtle;
    }

    public Turtle 



}

Clerk.clear();
TurtlePlay x = new TurtlePlay(new Turtle(600, 400));








// Draw the hand of cards
void turtleHand(Turtle turtle, int sizeCard, models.Card[] cards) {
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
void turtleCard(Turtle turtle, int size, models.Card card) {
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
    turtle.text(suit, Font.COURIER, 3 * size, Font.Align.CENTER);
    turtle.backward(2 * size);

    String rank = card.printRank();
    turtle.text(rank, Font.COURIER, 3 * size, Font.Align.CENTER);
}

// Draw scores for players
void turtleScores(Turtle turtle, int size, List<models.Player> players) {
    int teamCount = 2;
    turtle.moveTo(turtle.height - 20, turtle.width / 2);
    for (int i = 0; i < teamCount; i++) {
        models.Team team = players.get(i).getTeam();
        turtle.text(team.getName() + ": " + team.getBigs(), Font.COURIER, size, Font.Align.CENTER);
        turtle.backward(3 * size);
    }
}

// Display trump selection prompt
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

// Display the end-of-game winner and scores
void turtleEnd(Turtle turtle, int size, controllers.Match match) {
    turtle.moveTo(turtle.height / 2, turtle.width / 2);
    turtle.text("We have a WINNER: " + match.winner.getName(), Font.COURIER, size, Font.Align.CENTER);
    turtle.backward(3 * size);

    turtle.text("Game over! Scores:", Font.COURIER, size, Font.Align.CENTER);
    turtle.backward(3 * size);

    List<models.Player> players = match.winner.getPlayers(match.players);
    turtleScores(turtle, size, players);
}
