import java.util.List;

import models.Player;

public class TurtlePlay {

    public Turtle turtle;
    public controllers.Match match;
    public Player me;


    public TurtlePlay(Turtle turtle) {
        // Create a new match 
        this.turtle = turtle;
        turtle.left(90);
        System.out.println("Creating a new match...");
        this.match = new controllers.Match(controllers.Game.Difficulty.EASY);
        System.out.println("Match created!");
        this.me = match.me;
        drawStart();
    }

    public Turtle drawStart() { 
        turtle.penUp();
        turtle.text("Welcome to Bela!", Font.COURIER, 20, Font.Align.CENTER);
        turtle.backward(40);
        turtle.text("Start game with play()", Font.COURIER, 20, Font.Align.CENTER);
        return turtle;
    }

    public Turtle play() {
        turtle.reset();
        turtle.left(90);
        // Start the game
        match.play();
        // Draw the hand of cards
        turtleHand(turtle, 10, match.me.getHand());
        // Draw scores for players
        turtleBigScore(turtle, 12, match.players);
        turtleSmallScore(turtle, 12, match.players);
        if(match.getCurrentGame().getTrumpSuit() == null) {
            printTrumpChoices(turtle, 20, match.getCurrentGame());
        }
        // Display trump selection prompt
        //turtleTrump(turtle, 20);
        return turtle;
    }

    public Turtle pickCard(int cardIndex) {
        // Play a card
        match.pickCard(cardIndex);
        // Draw the hand of cards
        play();
        // Draw scores for players
        //turtleScores(turtle, 20, match.players);
        // Display trump selection prompt
        //turtleTrump(turtle, 20);
        return turtle;
    }

    public Turtle pickTrump(int trumpChoice) {
        // Choose a trump suit
        match.pickTrump(trumpChoice);
        // Draw the hand of cards
        play();
        // Draw scores for players
        //turtleScores(turtle, 20, match.players);
        // Display trump selection prompt
        //turtleTrump(turtle, 20);
        return turtle;
    }
}

public Turtle printTrumpChoices(Turtle turtle, int size, controllers.Game game) {
    // Display the trump suit choices
    turtle.moveTo(turtle.width / 2, turtle.height / 2);
    turtle.text("Choose a trump suit:", Font.COURIER, size, Font.Align.CENTER);
    turtle.backward(2 * size);
    turtle.text("1. \u2665 | 2. \u2666 | 3. \u2663 | 4. \u2660", Font.COURIER, size, Font.Align.CENTER);
    turtle.backward(2 * size);
    if(game.getDealerIndex() != 0) { // If dealer is in front of you, you cant skip
        turtle.text("0. SKIP", Font.COURIER, size, Font.Align.CENTER);
    }
    return turtle;
}

public Turtle turtleOnFloor(Turtle turtle, int size, controllers.Round round) {
    // Get the list of cards on the floor
    List<models.Card> onFloorCards = round.getOnFloorCards();
    
    // Get the index of the player who played first
    int startingPlayerIndex = round.getStartingPlayerIndex();
    
    // If no cards are on the floor
    if (onFloorCards.isEmpty()) {
        // Move to the center of the canvas and display the message
        turtle.moveTo(turtle.width / 2, turtle.height / 2);
        turtle.text("No cards on floor", Font.COURIER, size * 2, Font.Align.CENTER);
        return turtle;
    }
    
    // Calculate the starting position for the cards (centered on the screen)
    int totalCards = onFloorCards.size();
    int totalWidth = totalCards * size * 5 + (totalCards - 1) * size; // Total width accounting for spacing
    int startX = (turtle.width / 2) - (totalWidth / 2); // Start position to center the cards
    int startY = turtle.height / 2; // Vertical position
    
    turtle.moveTo(startX, startY); // Move to the starting position
    
    // Iterate through the cards and draw them with the name of the player who threw it
    for (int i = 0; i < totalCards; i++) {
        // Get the current card
        Card card = onFloorCards.get(i);
        
        // Calculate the index of the player who threw the card
        int playerIndex = (startingPlayerIndex + i) % 4;
        Player player = round.getPlayerIndex(playerIndex);
        
        // Draw the card
        turtleCard(turtle, size, card); // Assuming `turtleCard` already centers the card
        
        // Move below the card to write the player's name
        turtle.right(90); // Face downward
        turtle.forward(size); // Move down a bit
        turtle.left(90); // Face forward
        
        // Print the name of the player who played the card
        turtle.text(player.getName(), Font.COURIER, size, Font.Align.CENTER);
        
        // Reset position for the next card
        turtle.left(90); // Face upward
        turtle.backward(size); // Move back up
        turtle.right(90); // Face forward
        
        // Move to the position of the next card
        turtle.forward(size * 5 + size); // Adjust distance to next card
    }

    return turtle;
}

// Draw the hand of cards
// Draw the hand of cards
void turtleHand(Turtle turtle, int sizeCard, models.Hand yourHand) {
    int handSize = yourHand.getSize();
    
    // Dynamically calculate starting positions
    int startingWidth = (turtle.width / 2) - ((handSize * sizeCard * 5) + (handSize - 1) * (sizeCard)) / 2; // Center horizontally
    int startingHeight = turtle.height - 20;  // Fixed offset from the canvas top

    turtle.moveTo(startingWidth, startingHeight);

    for (int i = 0; i < handSize; i++) {
        // Position and draw each card
        turtle.moveTo(startingWidth + i * (sizeCard * 5 + sizeCard), startingHeight); 
        turtleCard(turtle, sizeCard, yourHand.getCard(i));  // Draw the card

        // Adjust vertical movement for clear spacing
        turtle.right(90);
        turtle.forward(1.5 * sizeCard);  // Forward buffer
        turtle.left(90);
    }
}



void turtleCard(Turtle turtle, int size, models.Card card) {
    int cardWidth = 5 * size;  // Card width (proportional to size)
    int cardHeight = 8 * size;  // Card height (proportional to size)

    // Draw the card outline
    turtle.penDown();
    turtle.forward(cardHeight).right(90)  // Top side
          .forward(cardWidth).right(90)  // Right side
          .forward(cardHeight).right(90) // Bottom side
          .forward(cardWidth).right(90); // Left side
    turtle.penUp();

    // ** Centering Text Inside the Card **

    // Move turtle to the center of the card
    turtle.forward(cardHeight / 1.8);  // Move half-width to the center horizontally
    turtle.right(90);
    turtle.forward(cardWidth / 2);  // Move half-height to the center vertically
    turtle.left(90);

    // Draw rank in the center
    String rank = card.printRank();
    turtle.text(rank, Font.COURIER, size * 3, Font.Align.CENTER);  // Adjust text size for scaling

    // Draw rank slightly above the suit
    turtle.backward(cardHeight * 0.3);  // Adjust position upward (small tweak)
    String suit = card.printSuit();
    turtle.text(suit, Font.COURIER, size * 3, Font.Align.CENTER);  // Adjust text size for scaling

    // Reset turtle's position to avoid affecting subsequent drawing (optional)
    turtle.forward(cardHeight * 0.3);  // Move back to the center
}

    // Print the big scores for each team
    public Turtle turtleBigScore(Turtle turtle, int size, List<models.Player> players) {
        int teamCount = 2;
        turtle.moveTo(10, 20);
        for (int i = 0; i < teamCount; i++) {
            models.Team team = players.get(i).getTeam();
            turtle.text(team.getName() + " bigs: " + team.getBigs(), Font.COURIER, size, Font.Align.LEFT);
            turtle.backward(1 * size);
        }
       return turtle;
    }

    // Print the small scores for each team
    public Turtle turtleSmallScore(Turtle turtle, int size, List<models.Player> players) {
        int teamCount = 2;
        turtle.moveTo(10, 50);
        for (int i = 0; i < teamCount; i++) {
            models.Team team = players.get(i).getTeam();
            turtle.text(team.getName() + " smalls: " + team.getSmalls(), Font.COURIER, size, Font.Align.LEFT);
            turtle.backward(1 * size);
        }
       return turtle;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// TURTLE PLAY
/// 
Turtle t = new Turtle(600, 400);
TurtlePlay x = new TurtlePlay(t);