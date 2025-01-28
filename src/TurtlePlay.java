import java.util.List;

import controllers.Match;
import models.Player;

public class TurtlePlay {

    public Turtle turtle;
    public controllers.Match match;
    private boolean matchStarted = false;

    public TurtlePlay(Turtle turtle) {
        this.turtle = turtle; 
        turtle.penUp();
        turtle.left(90);
        this.match = new controllers.Match(); 
        System.out.println("TurtlePlay initialized! Please feel free to customize the match.");
        drawMatchStart(turtle); // Draws the initial start screen
    }

    // Main method to play the game
    public void play() {
        // Reset the turtle
        turtle.reset();
        turtle.left(90);
 
        // Handle game phases
        switch (match.getCurrentPhase()) {
            case START -> {
                // Match anhalten, bis der Benutzer das Spiel startet
                if(!matchStarted) {
                    matchStarted = true;
                    match.play();
                }
                turtle.moveTo(0,400);
                turtle.text("📙", Font.COURIER, 2500, Font.Align.CENTER);
                drawGameStart(turtle, match);
            }
            case CHOOSING_TRUMP -> {
                turtle.moveTo(0,400);
                turtle.text("📗", Font.COURIER, 2000, Font.Align.CENTER);
                turtlePlayers(turtle, match.getPlayers());
                turtleHand(turtle, match.getPlayers().get(0));  
                turtleTrumpChoices(turtle, match.getCurrentGame());
            }
            case SHOW_ZVANJE -> {
                turtle.moveTo(0,400);
                turtle.text("📙", Font.COURIER, 2000, Font.Align.CENTER);
                turtleZvanjeResult(turtle, match.getCurrentGame());
                turtleSnap(turtle, match);
            }
            case PLAYING_ROUNDS -> {
                turtle.moveTo(0,400);
                drawPlayingRounds(turtle, match);
            }
            case END_OF_GAME -> {
                turtle.moveTo(0,400);
                turtle.text("📙", Font.COURIER, 2000, Font.Align.CENTER);
                drawEndGame(turtle, match);
                turtleSnap(turtle, match);
            }
            case END_OF_MATCH -> {
                drawEndMatch(turtle, match);
            }
            default -> throw new IllegalStateException("Unexpected phase: " + match.getCurrentPhase());
        }
    }

    // If user wants to go back one step, just call:
    public void goBack() {
        if(match.getCurrentPhase() == controllers.Match.MatchPhase.START || match.getCurrentPhase() == controllers.Match.MatchPhase.END_OF_MATCH) {
            System.out.println("Cannot go back further than the start of the match.");
            return; 
        }
        match.goBack();
        play();
    }

    // Method to be called from the GUI when the human player accepts the zvanje
    public void startGame() {  
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.START || !matchStarted) {
            System.out.println("Error: You cannot start a game at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        match.startGame();
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    // Method to be called from the GUI when the human player chooses a trump suit
    public void pickTrump(int choice) {
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.CHOOSING_TRUMP) {
            System.out.println("Error: You cannot pick a trump at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        // Proceed with trump selection if phase is valid
        match.pickTrump(choice);

        // Move the game forward by calling play() to progress to the next phase
        this.play();
    }    

    // Method to be called from the GUI when the human player accepts the zvanje
    public void startRound() {  
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.SHOW_ZVANJE) {
            System.out.println("Error: You cannot accept Zvanje at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        match.startRound();
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

    // Method to be called from the GUI when the human player chooses to end the game
    public void endGame() { 
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.END_OF_GAME) {
            System.out.println("Error: You cannot end the game at this phase! Current phase: " + match.getCurrentPhase());
            return; // Exit the method without advancing the game
        }
        match.endGame();
        // Move the game forward by calling play() to progress to the next round
        this.play();
    }

    // Method to be called from the GUI when the human player chooses to end the match
    public void endMatch() {
        if (match.getCurrentPhase() != controllers.Match.MatchPhase.END_OF_MATCH) {
            System.out.println("Error: You cannot end the match at this phase! Current phase: " + match.getCurrentPhase());
            return;
        }
        this.match = new controllers.Match();
        this.matchStarted = false;
        match.endMatch();
        drawMatchStart(turtle);
    }

    // Custom methods to set the match settings
    public void setDifficulty(String difficultyName) {
        try {
            match.setDifficulty(difficultyName);
        } catch (Exception e) {
            System.out.println("Error setting difficulty in turtle: " + e.getMessage());
        }
        drawMatchStart(turtle);
    }

    // Custom method to set the player name
    public void setMyName(String playerName) {
        try {
            match.setMyName(playerName);
        } catch (Exception e) {
            System.out.println("Error setting player name in turtle: " + e.getMessage());
        }
        drawMatchStart(turtle);
    }

    // Custom method to set the other player names
    public void setOtherNames(String teamMate, String enemyMate1, String enemyMate2) {
        try {
            match.setOtherNames(teamMate, enemyMate1, enemyMate2);
        } catch (Exception e) {
            System.out.println("Error setting other names in turtle: " + e.getMessage());
        }
        drawMatchStart(turtle);
    }
    
    // Custom method to set the team names
    public void setTeamNames(String team1Name, String team2Name) {
        try {
            match.setTeamNames(team1Name, team2Name);
        } catch (Exception e) {
            System.out.println("Error setting team names: " + e.getMessage());
        }
        drawMatchStart(turtle);
    }

    private void drawMatchStart(Turtle turtle) { 
        turtle.moveTo(0,400);
        turtle.text("📗", Font.COURIER, 2000, Font.Align.CENTER);
        turtle.moveTo(300, 60);
        turtle.text("Welcome to Bela!", Font.COURIER, 30, Font.Align.CENTER);
        turtle.backward(50);
        turtle.text("Game Settings", Font.COURIER, 20, Font.Align.CENTER);
        turtle.moveTo(300, 370);
        turtle.text("Start playing: play()", Font.COURIER, 25, Font.Align.CENTER);
        turtle.moveTo(40, 150);
        turtle.text("setDifficulty(String: LEARN | NORMAL | PRO)", Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(20);
        turtle.text("setMyName(String: Player name)", Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(20);
        turtle.text("setOtherNames(String: Teammate, Rival_1, Rival_2)", Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(20);
        turtle.text("setTeamNames(String: myTeam, rivalTeam)", Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(40);
        turtle.text("🔹Difficulty: " + match.difficulty.toString(), Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(20);
        turtle.text("🔹My Team name: " + match.customTeam1Name, Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(20);
        turtle.text("🔹My Team players: " + match.customPlayerName + " (you), " + match.customTeamMate, Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(20);
        turtle.text("🔹Rival Team name: " + match.customTeam2Name, Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(20);
        turtle.text("🔹Rival Team players: " + match.customEnemy1 + ", " + match.customEnemy2, Font.COURIER, 15, Font.Align.LEFT);
        turtle.backward(20);

    }
}

private void drawGameStart(Turtle turtle, controllers.Match match) { 
    turtle.moveTo(300, 100);
    turtle.text("Game #" + (match.getGameCounter()) + " starting!", Font.COURIER, 25, Font.Align.CENTER);
    turtle.penUp().backward(80);
    turtle.text("🃏", Font.COURIER, 60, Font.Align.CENTER);
    turtle.backward(120);
    turtle.text("Difficulty: " + match.difficulty.toString(), Font.COURIER, 15, Font.Align.CENTER);
    turtle.backward(60);
    turtle.text("Start this game with startGame()", Font.COURIER, 20, Font.Align.CENTER);
    turtlePlayerInfo(turtle, match);
}

private void drawPlayingRounds(Turtle turtle, controllers.Match match) {
    turtle.text("📗", Font.COURIER, 2000, Font.Align.CENTER);
    turtlePlayers(turtle, match.getPlayers());
    turtleHand(turtle, match.getPlayers().get(0));
    if (match.difficulty == Match.Difficulty.NORMAL) {
        turtleBigScore(turtle, match.getPlayers());
    } else if (match.difficulty == Match.Difficulty.LEARN) {
        turtleBigScore(turtle, match.getPlayers());
        turtleSmallScore(turtle, match.getPlayers());
        turtleGameZvanje(turtle, match.getCurrentGame());
    }
    turtleGameInfo(turtle, match.getCurrentGame());
    turtleOnFloor(turtle, match.getCurrentRound());
    turtleStartingPlayer(turtle, match.getCurrentRound());
    turtleSnap(turtle, match);
    turtleWonDecks(turtle, match);
}

private void drawEndMatch(Turtle turtle, controllers.Match match) { 
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
    turtle.text(match.getTeam1().getName() + ": " + match.getTeam1().getBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text(match.getTeam2().getName() + ": " + match.getTeam2().getBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.moveTo(300, 390);
    turtle.text("End this match with endMatch()", Font.COURIER, 20, Font.Align.CENTER);
}


private void drawEndGame(Turtle turtle, controllers.Match match) {
    turtle.moveTo(300, 100);
    turtle.text("Game #" + (match.getGameCounter()) + " ended!", Font.COURIER, 20, Font.Align.CENTER);
    turtle.penUp().backward(40);
    turtle.text("Trump team" + match.getCurrentGame().getTrumpTeam() + " passed? " + match.getCurrentGame().teamPassed(), Font.COURIER, 18, Font.Align.CENTER);
    turtle.backward(50);
    turtle.text("Points earned:", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(30);
    turtle.text(match.getTeam1().getName() + ": " + match.getTeam1().getAwardedBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text(match.getTeam2().getName() + ": " + match.getTeam2().getAwardedBigs(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(40);
    turtle.text("Game Zvanje: " + match.getCurrentGame().getZvanjePoints(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.moveTo(300, 390);
    turtle.text("End this game with endGame()", Font.COURIER, 20, Font.Align.CENTER); 
}

private void turtleZvanjeResult(Turtle turtle, controllers.Game game) {
    // Display the result of the zvanje
    turtle.moveTo(turtle.width / 2, turtle.height / 6);
    turtle.penUp();
    turtle.text("Trump suit: " + game.getTrumpSuit().getSymbol(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(40);
    turtle.text("Trump team: " + game.getTrumpTeam(), Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(60);
    if (game.getZvanjePoints() == 0) {
        turtle.text("No zvanje winner", Font.COURIER, 20, Font.Align.CENTER);
    } else {
        turtle.text("Zvanje winner: " + game.getZvanjeWinner(), Font.COURIER, 20, Font.Align.CENTER);
        turtle.backward(20);
        turtle.text("Zvanje team: " + game.getZvanjeWinner().getTeam(), Font.COURIER, 20, Font.Align.CENTER); 
        turtle.backward(40);
        turtle.text("Zvanje points: " + game.getZvanjePoints(), Font.COURIER, 20, Font.Align.CENTER);
        turtle.backward(60);
        for (models.Player player : game.getPlayers()) {
            int inxPlayer = game.getPlayers().indexOf(player);
            if (player.getTeam() == game.getZvanjeWin().getZvanjeTeam()) {
                turtle.text(player.getName() + " cards revealed: " + game.getZvanjeResults().get(inxPlayer).getCardsOfZvanje(), Font.COURIER, 15, Font.Align.CENTER);
                turtle.backward(40);
            }
        }
    }
    turtle.moveTo(300, 390);
    turtle.text("Start round: startRound()", Font.COURIER, 20, Font.Align.CENTER);
}

private void turtlePlayerInfo(Turtle turtle, controllers.Match match) {
    // Display the name of the player
    turtle.penUp();
    turtle.moveTo(turtle.width / 4, turtle.height / 2);
    turtle.color(0, 0, 0);
    turtle.text(match.customTeam1Name, Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text(match.customPlayerName + " & " + match.customTeamMate, Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    if(match.getTeam1() != null) turtle.text("Big points: " + match.getTeam1().getBigs(), Font.COURIER, 20, Font.Align.CENTER); 
    turtle.moveTo(turtle.width * 3/4, turtle.height / 2);
    turtle.color(0, 0, 0);
    turtle.text(match.customTeam2Name, Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    turtle.text(match.customEnemy1 + " & " + match.customEnemy2, Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(20);
    if(match.getTeam2() != null) turtle.text("Big points: " + match.getTeam2().getBigs(), Font.COURIER, 20, Font.Align.CENTER);
}

private void turtleStartScreen(Turtle turtle) {
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

private void turtlePlayers(Turtle turtle, List<Player> players) {
    // Display the name of the player
    for (int i = 0; i < players.size(); i++) {
        Player player = players.get(i);
        switch (i) {
            case 0 -> {
                turtle.moveTo(turtle.width / 2, turtle.height - 130); // Bottom
                turtle.color(0, 0, 255);
                turtle.text(player.getName(), Font.COURIER, 20, Font.Align.CENTER);
            }
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
}

private void turtleStartingPlayer(Turtle turtle, controllers.Round round) {
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
}

private void turtleGameInfo(Turtle turtle, controllers.Game game) {
    // Display the trump suit for the round
    turtle.moveTo(590, 10);
    turtle.text("Difficulty: " + game.getDifficulty().toString(), Font.COURIER, 10, Font.Align.RIGHT);
    turtle.backward(10);
    turtle.text("Dealer: " + game.getDealer(), Font.COURIER, 10, Font.Align.RIGHT);
    turtle.backward(10);
    turtle.text("Trump Team: " + game.getTrumpTeam(), Font.COURIER, 10, Font.Align.RIGHT); 
    turtle.backward(10);
    turtle.text("Trump: " + game.getTrumpSuit().getSymbol(), Font.COURIER, 10, Font.Align.RIGHT);
}

private void turtleTrumpChoices(Turtle turtle, controllers.Game game) {
    // Display the trump suit choices
    turtle.moveTo(turtle.width / 2, turtle.height / 3);
    turtle.text("Choose a trump suit: ", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(40);
    turtle.text("pickTrump(int index)", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(45);
    turtle.text("1. \u2660 | 2. \u2665 | 3. \u2666 | 4. \u2663", Font.COURIER, 20, Font.Align.CENTER);
    turtle.backward(25);
    if(game.getDealerIndex() != 0) { // If dealer is in front of you, you cant skip
        turtle.text("0. SKIP", Font.COURIER, 20, Font.Align.CENTER);
    }
}

private void turtleOnFloor(Turtle turtle, controllers.Round round) {
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
}

private void turtleHand(Turtle turtle, models.Player player) {
    int handSize = player.getHand().getSize(); 
    // Calculate the total width of the entire hand
    int totalHandWidth = (handSize - 1) * (10 * 5 + 10);  // Total gaps & card widths AFTER the first card.
    // Adjust the starting position to ensure the hand is centered 
    int startingWidth = (turtle.width / 2) - (totalHandWidth / 2); // Center of hand starts here
    // Fixed vertical offset from the bottom
    int startingHeight;
    // Start drawing cards 
    for (int i = 0; i < handSize; i++) {
        // Move to position of each card
        if (player.getPlayableIndices() != null && player.getPlayableIndices().contains(i)) {
            startingHeight = turtle.height - 20; // Adjust height for playable cards
            turtle.color(0, 0, 0); // Black for playable cards
        } else {
            startingHeight = turtle.height - 5; // Normal height for non-playable cards
            turtle.color(2, 2, 2); // Grey for non-playable cards
        }
        turtle.moveTo(startingWidth + i * (10 * 5 + 10), startingHeight);
        // Draw the card at the current position
        turtleCard(turtle, player.getHand().getCard(i));
    }
}

private void turtleCard(Turtle turtle, models.Card card) {
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
private void turtleBigScore(Turtle turtle, List<models.Player> players) {
    int teamCount = 2;
    turtle.moveTo(10, 20);
    for (int i = 0; i < teamCount; i++) {
        models.Team team = players.get(i).getTeam();
        turtle.text(team.getName() + " Bigs: " + team.getBigs(), Font.COURIER, 10, Font.Align.LEFT);
        turtle.backward(10);
    }
}

// Print the small scores for each team
private void turtleSmallScore(Turtle turtle, List<models.Player> players) {
    int teamCount = 2;
    turtle.moveTo(10, 40);
    for (int i = 0; i < teamCount; i++) {
        models.Team team = players.get(i).getTeam();
        turtle.text(team.getName() + " Smalls: " + team.getSmalls(), Font.COURIER, 10, Font.Align.LEFT);
        turtle.backward(10);
    }
}

private void turtleGameZvanje(Turtle turtle, controllers.Game game) {
    turtle.moveTo(10, 60);
    if(game.getZvanjePoints() != 0) {
        turtle.text("Zvanje: " + game.getZvanjePoints() + "pts " + game.getZvanjeWinner(), Font.COURIER, 10, Font.Align.LEFT);
    } else {
        turtle.text("Zvanje: None", Font.COURIER, 10, Font.Align.LEFT);
    }
}

private void turtleSnap(Turtle turtle, controllers.Match match) {
    if(match.isSnapEmpty()) {
        return;
    }
    turtle.moveTo(35, 370);
    turtle.text("↩️", Font.COURIER, 25, Font.Align.CENTER);
    turtle.backward(15);
    turtle.text("goBack()", Font.COURIER, 10, Font.Align.CENTER);
}

private void turtleWonDecks(Turtle turtle, controllers.Match match) {
    // Draw the deck of cards won by both teams
    for (models.Team team : List.of(match.getTeam1(), match.getTeam2())) {
        if (team.getName().equals(match.getTeam1().getName())) {
            turtle.moveTo(500, 275); // my team
        } else if (team.getName().equals(match.getTeam2().getName())) {
            turtle.moveTo(50, 120); // their team
        }
        if (team.getSmalls() != 0) {
            turtle.left(90);
            turtle.text("🃏", Font.COURIER, 50, Font.Align.CENTER);
            turtle.right(90);
        }
    }
}