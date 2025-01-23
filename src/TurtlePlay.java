
Clerk.clear();

public class TurtlePlay {

    public Turtle turtle;
    public controllers.Match match;
    private controllers.Game.Difficulty customDifficulty = controllers.Game.Difficulty.EASY;
    private boolean matchStarted = false;
    private String customTeam1Name = "Team 1";
    private String customTeam2Name = "Team 2";
    private String customPlayerName = "You";
    private String customTeamMate = "Teammate"; 
    private String customEnemy1 = "Rival_1"; 
    private String customEnemy2 = "Rival_1"; 


    public TurtlePlay(Turtle turtle) {
        this.turtle = turtle; 
        turtle.penUp();
        turtle.left(90);
        this.match = new controllers.Match(); 
        System.out.println("TurtlePlay initialized. Please call settings() to customize the match.");
        drawMatchStart(turtle); // Draws the initial start screen
    }
    
    // If user wants to go back one step, just call:
    public void oups() {
        if(match.getCurrentPhase() == controllers.Match.MatchPhase.START || match.getCurrentPhase() == controllers.Match.MatchPhase.END_OF_MATCH) {
            System.out.println("Cannot go back further than the start of the match.");
            return;
        }
        match.revertToPreviousSnapshot();
        play();
    }

    public Turtle play() {
        // Reset the turtle
        turtle.reset();
        turtle.left(90);
 
        // Handle game phases
        switch (match.getCurrentPhase()) {
            case START -> {
                // Initialize with default basic settings
                if(!matchStarted) {
                    System.out.println("Settings were not initialized. Using default settings...");
                    this.settings(customDifficulty, customTeam1Name, customTeam2Name, customPlayerName, customTeamMate, customEnemy1, customEnemy2);
                    matchStarted = true;
                }
                turtle.moveTo(0,400);
                turtle.text("📓", Font.COURIER, 2500, Font.Align.CENTER);
                drawGameStart(turtle, match);
            }
            case CHOOSING_TRUMP -> {
                System.err.println("CHOOSING_TRUMP");
                turtle.moveTo(0,400);
                turtle.text("📗", Font.COURIER, 2000, Font.Align.CENTER);
                turtleOtherPlayers(turtle, match.players);
                turtleHand(turtle, match.me.getHand());
                turtleTrumpChoices(turtle, match.getCurrentGame());
            }
            case SHOW_ZVANJE -> {
                System.err.println("SHOW_ZVANJE");
                turtle.moveTo(0,400);
                turtle.text("📙", Font.COURIER, 2000, Font.Align.CENTER);
                turtleZvanjeResult(turtle, match.getCurrentGame());
            }
            case PLAYING_ROUNDS -> {
                turtle.moveTo(0,400);
                turtle.text("📗", Font.COURIER, 2000, Font.Align.CENTER);
                turtleOtherPlayers(turtle, match.players);
                turtleHand(turtle, match.me.getHand());
                turtleBigScore(turtle, match.players);
                turtleSmallScore(turtle, match.players);
                turtleGameInfo(turtle, match.getCurrentGame());
                turtleOnFloor(turtle, match.getCurrentRound());
                turtleStartingPlayer(turtle, match.getCurrentRound());
            }
            case END_OF_GAME -> {
                turtle.moveTo(0,400);
                turtle.text("📙", Font.COURIER, 2000, Font.Align.CENTER);
                System.out.println("Game over!");
                drawEndGame(turtle, match);
            }
            case END_OF_MATCH -> {
                System.out.println("Match ended!");
                drawEndMatch(turtle, match);
            }
            default -> throw new IllegalStateException("Unexpected phase: " + match.getCurrentPhase());
        }
        return turtle;
    }

    public void settings(controllers.Game.Difficulty difficulty, String team1Name, String team2Name, 
                     String playerName, String teamMate, String enemyMate1, String enemyMate2) {
        // Set up the teams
        match.initializeGameSettings(
            difficulty != null ? difficulty : controllers.Game.Difficulty.EASY,
            team1Name != null ? team1Name : "Team 1",
            team2Name != null ? team2Name : "Team 2",
            playerName != null ? playerName : "You",
            teamMate != null ? teamMate : "Teammate",
            enemyMate1 != null ? enemyMate1 : "Enemy 1",
            enemyMate2 != null ? enemyMate2 : "Enemy 2"
        );
    }

    // Method to be called from the GUI when the human player accepts the zvanje
    public void startGame() {  
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.START || !matchStarted) {
            System.out.println("Error: You cannot start a game at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        match.startGame(true);
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    // Method to be called from the GUI when the human player chooses a trump suit
    public void pickTrump(int choice) {
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.CHOOSING_TRUMP) {
            System.out.println("Error: You cannot pick a trump at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        System.err.println("///////////////////////////////GameTrump picked: " + match.getCurrentGame().getTrumpSuit());
        System.err.println("///////////////////////////////Player Trump picked: " + match.me.trumpChoice);

        // Proceed with trump selection if phase is valid
        match.pickTrump(choice);
        // Move the game forward by calling play() to progress to the next phase
        System.err.println("/////////////////////////////// Game Trump picked: " + match.getCurrentGame().getTrumpSuit());
        System.err.println("///////////////////////////////Player Trump picked: " + match.me.trumpChoice);

        this.play();
    }    

    // Method to be called from the GUI when the human player accepts the zvanje
    public void startRound() {  
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.SHOW_ZVANJE) {
            System.out.println("Error: You cannot accept Zvanje at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        match.startRound(true);
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    // Method to be called from the GUI when the human player chooses a card to play
    public void pickCard(int choice) {
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.PLAYING_ROUNDS) {
            System.out.println("Error: You cannot play a card at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        // Proceed with card play if phase is valid
        match.pickCard(choice); 
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }    

    public void endGame() { 
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.END_OF_GAME) {
            System.out.println("Error: You cannot end the game at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        match.endGame(true);
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    public void endMatch() {
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.END_OF_MATCH) {
            System.out.println("Error: You cannot end the match at this phase! Current phase: " + match.getCurrentPhase());
            return;
        }
        this.match = new controllers.Match();
        this.matchStarted = false;
        drawMatchStart(turtle);
    }

    public void setDifficulty(String difficulty) {
        if (difficulty != null) {
            switch (difficulty.toUpperCase()) {
            case "EASY" -> this.customDifficulty = controllers.Game.Difficulty.EASY;
            case "NORMAL" -> this.customDifficulty = controllers.Game.Difficulty.NORMAL;
            case "HARD" -> this.customDifficulty = controllers.Game.Difficulty.HARD;
            default -> {
                System.out.println("Invalid difficulty. Please choose EASY, NORMAL, or HARD.");
                return;
            }
            }
            System.out.println("Difficulty set to: " + difficulty);
        }
    }

    public void setMyName(String playerName) {
        if (playerName != null) {
            this.customPlayerName = playerName;
        }
    }

    public void setPlayerNames(String playerName, String teamMate, String enemyMate1, String enemyMate2) {
        if (playerName != null) {
            this.customPlayerName = playerName;
        }
        if (teamMate != null) {
            this.customTeamMate = teamMate;
        }
        if (enemyMate1 != null) {
            this.customEnemy1 = enemyMate1;
        }
        if (enemyMate2 != null) {
            this.customEnemy2 = enemyMate2;
        }
    }
    
    public void setTeamNames(String team1Name, String team2Name) {
        if (team1Name != null) {
            this.customTeam1Name = team1Name;
        }
        if (team2Name != null) {
            this.customTeam2Name = team2Name;
        }
    }
}

public Turtle drawEndMatch(Turtle turtle, controllers.Match match) { 
    if(match.getWinningTeam() != match.getPlayerTeam()) {
        turtle.moveTo(0,400);
        turtle.text("📕", Font.COURIER, 2000, Font.Align.CENTER);
    }else {
        turtle.moveTo(0,400);
        turtle.text("📘", Font.COURIER, 2000, Font.Align.CENTER);
    }
    turtle.moveTo(300, 100);
    turtle.text("Match Ended!", Font.COURIER, 20, Font.Align.CENTER);
    turtle.penUp().backward(40);
    turtle.text("Winner: " + match.getWinnerPrint(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(50);
    turtle.text("Final Scores:", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(30);
    turtle.text(match.team1.getName() + ": " + match.team1.getBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text(match.team2.getName() + ": " + match.team2.getBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.moveTo(300, 390);
    turtle.text("End this match with endMatch()", Font.COURIER, 20, Font.Align.CENTER);
    return turtle;
}


public Turtle drawEndGame(Turtle turtle, controllers.Match match) {
    turtle.moveTo(300, 100);
    turtle.text("Game #" + (match.getGameCounter() + 1) + " ended!", Font.COURIER, 20, Font.Align.CENTER);
    turtle.penUp().backward(40);
    turtle.text("Trump team passed? " + match.getCurrentGame().teamPassed(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(50);
    turtle.text("Points earned:", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(30);
    turtle.text(match.team1.getName() + ": " + match.team1.getAwardedBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text(match.team2.getName() + ": " + match.team2.getAwardedBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(40);
    turtle.text("Game Zvanje: " + match.getCurrentGame().getZvanjePoints(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.moveTo(300, 390);
    turtle.text("End this game with endGame()", Font.COURIER, 20, Font.Align.CENTER); 
    return turtle;
}

public Turtle drawGameStart(Turtle turtle, controllers.Match match) { 
    turtle.moveTo(300, 100);
    turtle.text("Game #" + (match.getGameCounter() + 1) + " starting!", Font.COURIER, 20, Font.Align.CENTER);
    turtle.penUp().backward(80);
    turtle.text("🃏", Font.COURIER, 60, Font.Align.CENTER);
    turtle.backward(120);
    turtle.text("Start this game with startGame()", Font.COURIER, 20, Font.Align.CENTER);
    turtlePlayerInfo(turtle, match);
    return turtle;
}

public Turtle drawMatchStart(Turtle turtle) { 
    turtle.moveTo(0,400);
    turtle.text("📗", Font.COURIER, 2000, Font.Align.CENTER);
    turtle.moveTo(300, 60);
    turtle.text("Welcome to Bela!", Font.COURIER, 25, Font.Align.CENTER);
    turtle.backward(60);
    turtle.text("Game starts with: play()", Font.COURIER, 25, Font.Align.CENTER);
    turtle.backward(60);
    turtle.text("Edit Game Settings or leave defaults:", Font.COURIER, 20, Font.Align.CENTER);
    turtle.moveTo(40, 250);
    turtle.text("🔹setDifficulty(String: EASY | NORMAL | HARD)", Font.COURIER, 15, Font.Align.LEFT);
    turtle.backward(25);
    turtle.text("🔹setMyName(String: Player name)", Font.COURIER, 15, Font.Align.LEFT);
    turtle.backward(25);
    turtle.text("🔹setPlayerNames(String: Player, Teammate, Rival_1, Rival_2)", Font.COURIER, 15, Font.Align.LEFT);
    turtle.backward(25);
    turtle.text("🔹setTeamNames(String: myTeam, rivalTeam)", Font.COURIER, 15, Font.Align.LEFT);
    return turtle;
}

public void turtleZvanjeResult(Turtle turtle, controllers.Game game) {
    // Display the result of the zvanje
    turtle.moveTo(turtle.width / 2, turtle.height / 4);
    turtle.penUp();
    turtle.text("Trump suit:", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(40);
    turtle.text(game.getTrumpSuit().getSymbol(), Font.COURIER, 40, Font.Align.CENTER);
    turtle.backward(40);
    if (game.getZvanjePoints() == 0) {
        turtle.text("No zvanje winner", Font.COURIER, 20, Font.Align.CENTER);
    } else {
        turtle.text("Zvanje winner: " + game.getZvanjeWinner(), Font.COURIER, 20, Font.Align.CENTER);
        turtle.backward(40);
        turtle.text("Zvanje points: " + game.getZvanjePoints(), Font.COURIER, 20, Font.Align.CENTER);
        turtle.backward(40);
        turtle.text("Zvanje types: ", Font.COURIER, 20, Font.Align.CENTER);
        turtle.backward(20);
        turtle.text(game.getZvanjeTypes(), Font.COURIER, 20, Font.Align.CENTER);
        turtle.backward(40);
        turtle.text("Cards used: " + game.getZvanjeCards(), Font.COURIER, 20, Font.Align.CENTER);
    }
    turtle.moveTo(300, 390);
    turtle.text("Start round: startRound()", Font.COURIER, 20, Font.Align.CENTER);
}

public void turtlePlayerInfo(Turtle turtle, controllers.Match match) {
    // Display the name of the player
    turtle.penUp();
    turtle.moveTo(turtle.width / 4, turtle.height / 2);
    turtle.color(0, 0, 0);
    turtle.text(match.team1.getName(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text(match.players.get(0).getName() + " & " + match.players.get(2).getName(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text("Big points: " + match.team1.getBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.moveTo(turtle.width * 3/4, turtle.height / 2);
    turtle.color(0, 0, 0);
    turtle.text(match.team2.getName(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text(match.players.get(1).getName() + " & " + match.players.get(3).getName(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text("Big points: " + match.team2.getBigs(), Font.COURIER, 20, Font.Align.CENTER);
}

public void turtleStartScreen(Turtle turtle) {
    // Display the start screen
    turtle.moveTo(100, 300);
    turtle.text("Match created", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text("Start game with startGame(true)", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(40);
    turtle.text("GAME SETTINGS:", Font.COURIER, 20, Font.Align.CENTER);
    turtle.moveTo(150, 200);
    turtle.text("Difficulty: EASY", Font.COURIER, 20, Font.Align.LEFT);

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
    turtle.moveTo(turtle.width / 2, turtle.height / 3);
    turtle.text("Choose a trump suit: ", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(40);
    turtle.text("pickTrump(int index)", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(55);
    turtle.text("1. \u2665 | 2. \u2666 | 3. \u2663 | 4. \u2660", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(35);
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
        turtle.text("YOU START", Font.COURIER, 12, Font.Align.CENTER);
        turtle.backward(60);
        turtle.text("Play a card: playCard(int: cardIndex)", Font.COURIER, 12, Font.Align.CENTER);
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