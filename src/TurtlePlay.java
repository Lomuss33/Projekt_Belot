Clerk.clear();

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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted, Failed to complete operation");
        }
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
        // fix 2x play() call
        match.play();
        turtle.reset();
        turtle.left(90);

        switch(match.getCurrentPhase()) {
            case START:
                // turtleOtherPlayers(turtle, match.players);
                // SHOW OPTIONS TO CHANGE NAMES of players and teams
                break;
            case CHOOSING_TRUMP:
                turtleOtherPlayers(turtle, match.players);
                turtleHand(turtle, match.me.getHand());
                turtleTrumpChoices(turtle, match.getCurrentGame());
                break;

            case SHOW_ZVANJE:
                // turtleOtherPlayers(turtle, match.players);
                turtleHand(turtle, match.me.getHand());
                break;
            
            case PLAYING_ROUNDS:
                turtleOtherPlayers(turtle, match.players);
                turtleHand(turtle, match.me.getHand());
                turtleBigScore(turtle, match.players);
                turtleSmallScore(turtle, match.players);
                turtleGameInfo(turtle, match.getCurrentGame());
                turtleOnFloor(turtle, match.getCurrentRound());
                turtleStartingPlayer(turtle, match.getCurrentRound());
                break;

            case END_OF_GAME:
                
                break;
            case END_OF_MATCH:
                
                break;
        }
        return turtle;
    }

    public void pickCard(int cardIndex) {
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.PLAYING_ROUNDS) {
            throw new IllegalStateException("Cannot play a card at this phase!");
        }
        // Play a card
        match.pickCard(cardIndex);
        play();
    }

    public void pickTrump(int trumpChoice) {
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.CHOOSING_TRUMP) {
            throw new IllegalStateException("Cannot choose trump at this phase!");
        }
        // Proceed with trump selection if phase is valid
        match.pickTrump(trumpChoice);
        play();
    }
}

public Turtle turtleOtherPlayers(Turtle turtle, List<Player> players) {
    // Display the name of the player
    for (int i = 1; i < players.size(); i++) {
        Player player = players.get(i);
        switch (i) {
            case 1 -> {
                turtle.moveTo(turtle.width - 20, turtle.height / 2); // Right
                turtle.right(90);
                turtle.color(255, 0, 0);
                turtle.text(player.getName(), Font.COURIER, 20, Font.Align.CENTER);
                turtle.left(90);
            }
            case 2 -> { 
                turtle.moveTo(turtle.width / 2, 20); // Top
                turtle.color(0, 0, 255);
                turtle.text(player.getName(), Font.COURIER, 20, Font.Align.CENTER);
            }
            case 3 -> {
                turtle.moveTo(20, turtle.height / 2); // Left
                turtle.left(90);
                turtle.color(255, 0, 0);
                turtle.text(player.getName(), Font.COURIER, 20, Font.Align.CENTER);
                turtle.right(90);
            }
            default -> throw new IllegalArgumentException("Invalid player index: " + i);
        }
    }
    turtle.color(0, 0, 0);
    return turtle;
}

public Turtle turtleStartingPlayer(Turtle turtle, controllers.Round round) {
    int xMiddle = turtle.width / 2;
    int yMiddle = turtle.height / 2;
    // Display the name of the starting player 
    int startingPlayer = round.getStartingPlayerIndex();
    switch (startingPlayer) { 
        case 0 -> turtle.moveTo(xMiddle, turtle.height - 100); // Bottom
        case 1 -> turtle.moveTo(turtle.width - 60, yMiddle); // Left
        case 2 -> turtle.moveTo(xMiddle, 60); // Top
        case 3 -> turtle.moveTo(60, yMiddle); // Right
        default -> throw new IllegalArgumentException("Invalid player index: " + startingPlayer);
    }
    turtle.color(0, 100, 0);
    turtle.text("\u2735", Font.COURIER, 30, Font.Align.CENTER);
    turtle.color(0, 0, 0);
    return turtle;
}

public Turtle turtleGameInfo(Turtle turtle, controllers.Game game) {
    // Display the trump suit for the round
    turtle.moveTo(600, 20);
    turtle.text("Trump: " + game.getTrumpSuit().getSymbol(), Font.COURIER, 20, Font.Align.RIGHT);
    turtle.backward(20);
    if(game.getZvanjePoints() != 0) {
        turtle.text("Zvanje: " + game.getZvanjePoints() + "pts " + game.getZvanjeWinner(), Font.COURIER, 20, Font.Align.RIGHT);
    } else {
        turtle.text("Zvanje: None", Font.COURIER, 20, Font.Align.RIGHT);
    }
    return turtle;
}

public Turtle turtleTrumpChoices(Turtle turtle, controllers.Game game) {
    // Display the trump suit choices
    turtle.moveTo(turtle.width / 2, turtle.height / 2);
    turtle.text("Choose a trump suit:", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(35);
    turtle.text("1. \u2665 | 2. \u2666 | 3. \u2663 | 4. \u2660", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(25);
    if(game.getDealerIndex() != 0) { // If dealer is in front of you, you cant skip
        turtle.text("0. SKIP", Font.COURIER, 20, Font.Align.CENTER);
    }
    return turtle;
}

public Turtle turtleOnFloor(Turtle turtle, controllers.Round round) {
    // Draw the box of Floor
    turtle.moveTo(150, 80); // Top-left corner
    turtle.lineTo(450, 80); // Top-right corner
    turtle.lineTo(450, 250); // Bottom-right corner
    turtle.lineTo(150, 250); // Bottom-left corner
    turtle.lineTo(150, 80); // Close the box

    double xMiddle = turtle.width / 2;
    double yMiddle = turtle.height / 2;
    double xOffset = turtle.width / 3;
    double yOffset = turtle.height / 2.3;
    // Get the list of cards on the floor
    List<models.Card> onFloorCards = round.getOnFloorCards();
    
    // Get the index of the player who played first
    int startingPlayerIndex = round.getStartingPlayerIndex();
    
    // If no cards are on the floor
    if (onFloorCards.isEmpty()) {
        // Move to the center of the canvas and display the message
        turtle.moveTo(xMiddle, yMiddle - 30);
        turtle.text("YOU START", Font.COURIER, 10, Font.Align.CENTER);
        return turtle;
    }
    
    // Iterate through the cards and draw them with the name of the player who threw it
    for (int i = 0; i < onFloorCards.size(); i++) {
        // Get the current card
        models.Card card = onFloorCards.get(i);
        
        // Determine the position based on the player index
        switch ((startingPlayerIndex + i) % 4) {
            case 0 -> turtle.moveTo(xMiddle, yOffset); // Bottom
            case 1 -> turtle.moveTo(xOffset * 2, yMiddle); // Left
            case 2 -> turtle.moveTo(xMiddle, yOffset); // Top
            case 3 -> turtle.moveTo(xOffset, yMiddle); // Right
            default -> throw new IllegalArgumentException("Invalid player index: " + startingPlayerIndex);
        }
        
        // Draw the card
        turtleCard(turtle, card); // Assuming `turtleCard` already centers the card
        
        // // Move below the card to write the player's name
        // turtle.right(90); // Face downward
        // turtle.forward(size); // Move down a bit
        // turtle.left(90); // Face forward
        
        // // // Print the name of the player who played the card
        // // turtle.text(player.getName(), Font.COURIER, size, Font.Align.CENTER);
        
        // // Reset position for the next card
        // turtle.left(90); // Face upward
        // turtle.backward(size); // Move back up
        // turtle.right(90); // Face forward
    }

    return turtle;
}

void turtleHand(Turtle turtle, models.Hand yourHand) {
    int handSize = yourHand.getSize();
    // Calculate the total width of the entire hand
    // Starting from "middle of the first card",
    int totalHandWidth = (handSize - 1) * (10 * 5 + 10);  // Total gaps & card widths AFTER the first card.
    // Adjust the starting position to ensure the hand is centered 
    int startingWidth = (turtle.width / 2) - (totalHandWidth / 2); // Center of hand starts here
    // Fixed vertical offset from the bottom
    int startingHeight = turtle.height - 5;
    // Start drawing cards
    for (int i = 0; i < handSize; i++) {
        // Move to position of each card
        turtle.moveTo(startingWidth + i * (10 * 5 + 10), startingHeight);
        // Draw the card at the current position
        turtleCard(turtle, yourHand.getCard(i));
    }
}

void turtleCard(Turtle turtle, models.Card card) {
    int cardWidth = 50;  // Card width (proportional to size)
    int cardHeight = 80;  // Card height (proportional to size)

    // Draw the card outline
    turtle.penDown();
    turtle.left(90).forward(cardWidth / 2);
    turtle.right(90);
    turtle.forward(cardHeight).right(90)  // Top side
          .forward(cardWidth).right(90)  // Right side
          .forward(cardHeight).right(90) // Bottom side
          .forward(cardWidth / 2).right(90); // Left side
    turtle.penUp();

    // ** Centering Text Inside the Card **

    // Move turtle to the center of the card
    turtle.forward(cardHeight / 1.8);  // Move half-width to the center horizontally

    // Draw rank in the center
    String rank = card.printRank();
    turtle.text(rank, Font.COURIER, 10 * 3, Font.Align.CENTER);  // Adjust text size for scaling

    // Draw rank slightly above the suit
    turtle.backward(cardHeight * 0.3);  // Adjust position upward (small tweak)
    String suit = card.printSuit();
    turtle.text(suit, Font.COURIER, 10 * 3, Font.Align.CENTER);  // Adjust text size for scaling

    // Reset turtle's position to avoid affecting subsequent drawing (optional)
    turtle.forward(cardHeight * 0.3);  // Move back to the center
}

// Print the big scores for each team
public Turtle turtleBigScore(Turtle turtle, List<models.Player> players) {
    int teamCount = 2;
    turtle.moveTo(10, 20);
    for (int i = 0; i < teamCount; i++) {
        models.Team team = players.get(i).getTeam();
        turtle.text(team.getName() + " bigs: " + team.getBigs(), Font.COURIER, 10, Font.Align.LEFT);
        turtle.backward(1 * 10);
    }
    return turtle;
}

// Print the small scores for each team
public Turtle turtleSmallScore(Turtle turtle, List<models.Player> players) {
    int teamCount = 2;
    turtle.moveTo(10, 50);
    for (int i = 0; i < teamCount; i++) {
        models.Team team = players.get(i).getTeam();
        turtle.text(team.getName() + " smalls: " + team.getSmalls(), Font.COURIER, 10, Font.Align.LEFT);
        turtle.backward(1 * 10);
    }
    return turtle;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// TURTLE PLAY
/// 
Turtle t = new Turtle(600, 400);
TurtlePlay x = new TurtlePlay(t);