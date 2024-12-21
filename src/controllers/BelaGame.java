// Core game state and enums
enum CardState {
    NEMA,    // Doesn't have
    IMA,     // Has
    MOZDA,   // Maybe has
    VJEROJATNO  // Probably has
}

enum Suit {
    TREF(0, 'T'),   // Clubs
    KARO(1, 'K'),   // Diamonds
    HERC(2, 'H'),   // Hearts
    PIK(3, 'P');    // Spades
    
    private final int value;
    private final char symbol;
    
    Suit(int value, char symbol) { // Constructor
        this.value = value;
        this.symbol = symbol;
    }
    
    public int getValue() { return value; }
    public char getSymbol() { return symbol; }
}

enum Rank {
    SEVEN(0, '7', 0, 0),
    EIGHT(1, '8', 0, 0),
    NINE(2, '9', 0, 14), // 14 points in trump
    TEN(3, 'X', 10, 10),
    JACK(4, 'J', 2, 20), // 20 points in trump
    QUEEN(5, 'Q', 3, 3),
    KING(6, 'K', 4, 4),
    ACE(7, 'A', 11, 11);
    
    private final int value;
    private final char symbol;
    private final int normalPoints;
    private final int trumpPoints;
    
    Rank(int value, char symbol, int normalPoints, int trumpPoints) {
        this.value = value;
        this.symbol = symbol;
        this.normalPoints = normalPoints;
        this.trumpPoints = trumpPoints;
    }
    
    public int getValue() { return value; }
    public char getSymbol() { return symbol; }
    public int getNormalPoints() { return normalPoints; }
    public int getTrumpPoints() { return trumpPoints; }
}

class Card {
    private final Suit suit;
    private final Rank rank;
    
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
    
    public int getIndex() {
        return suit.getValue() * 8 + rank.getValue();
    }
    
    public int getPoints(boolean isTrump) {
        return isTrump ? rank.getTrumpPoints() : rank.getNormalPoints();
    }
    
    @Override
    public String toString() {
        return String.format("%c%c", suit.getSymbol(), rank.getSymbol());
    }
}

class GameState {
    private final Card[] myCards = new Card[8];
    private final CardState[][] cards = new CardState[4][32];
    private final Card[] trick = new Card[4];
    private final boolean[] canPlay = new boolean[8];
    private final int[] scores = new int[2];
    private final boolean[] hasPlayed = new boolean[4];
    
    private int cardsPlayed = 0;
    private int round = 0;
    private int currentPlayer = 0;
    private int firstPlayer = 0;
    private int winningPlayer = -1;
    private Card strongestCard = null;
    private Suit trumpSuit = null;
    private boolean trumpPlayed = false;
    
    public GameState() {
        // Initialize arrays with default values
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 32; j++) {
                cards[i][j] = CardState.MOZDA;
            }
        }
    }
    
    // Getters and setters
    public Card[] getMyCards() { return myCards; }
    public CardState[][] getCards() { return cards; }
    public Card[] getTrick() { return trick; }
    public boolean[] getCanPlay() { return canPlay; }
    public int[] getScores() { return scores; }
    public int getCurrentPlayer() { return currentPlayer; }
    public Suit getTrumpSuit() { return trumpSuit; }
    
    // Core game logic methods will be added here
}

// Main class to handle game initialization and flow
public class BelaGame {
    private final GameState state;
    private final AIPlayer ai;
    
    public BelaGame() {
        this.state = new GameState();
        this.ai = new AIPlayer();
    }
    
    public void start() {
        // Initialize game
        initializeGame();
        
        // Main game loop
        for (int round = 0; round < 8; round++) {
            playRound();
        }
        
        // Show final scores
        System.out.printf("Final scores: %d vs %d%n", 
            state.getScores()[0], state.getScores()[1]);
    }
    
    private void initializeGame() {
        // Implementation will follow
    }
    
    private void playRound() {
        // Implementation will follow
    }
}