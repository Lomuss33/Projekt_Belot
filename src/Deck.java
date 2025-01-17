import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/* -------------------------------------------------------------------------- */
/*                                    Card                                    */
/* -------------------------------------------------------------------------- */
public class Card {
    // Enum for the Suit of the card
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    // Enum for the Rank of the card
    public enum Rank { 
        SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE 
    }

    private final Suit suit; // Suit of the card
    private final Rank rank; // Rank of the card
    private int value; // Value of the card

    // Constructor // e.g. 9♥ (Value: 14) => Card card = new Card(Suit.HEARTS, Rank.NINE)
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = 0; // Value based on trump or non-trump
    }

    public int getStrength(Card.Suit trumpSuit, Card.Suit leadSuit) {
        if (this.getSuit() == trumpSuit) {
            // Strength for Trump Cards
            switch (this.getRank()) {
                case JACK: return 16; // Strongest
                case NINE: return 15;
                case ACE: return 14;
                case KING: return 13;
                case QUEEN: return 12;
                case TEN: return 11;
                case EIGHT: return 10;
                case SEVEN: return 9; // Weakest
            }
        } else if (this.getSuit() == leadSuit) {
            // Strength for Non-Trump Cards
            switch (this.getRank()) {
                case ACE: return 8; // Strongest
                case KING: return 7;
                case QUEEN: return 6;
                case JACK: return 5;
                case TEN: return 4;
                case NINE: return 3;
                case EIGHT: return 2;
                case SEVEN: return 1; // Weakest
            }
        }
        return 0; // If the card is not of trump or lead suit
    }

    // Set the value of the card based on whether it's a trump card
    public void calculateValue(Suit trumpSuit) {
        boolean isTrump = this.suit == trumpSuit;
        switch (this.rank) {
            case SEVEN: value = 0; break;
            case EIGHT: value = 0; break;
            case NINE: value = isTrump ? 14 : 0; break;
            case TEN: value = 10; break;
            case JACK: value = isTrump ? 20 : 2; break;
            case QUEEN: value = 3; break;
            case KING: value = 4; break;
            case ACE: value = 11; break;
        }
    }

    // Getter Suit
    public Suit getSuit() {
        return suit;
    }

    // Getter Rank
    public Rank getRank() {
        return rank;
    }

    // Getter Value
    public int getValue() {
        return value;
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return rank + " | " + suit;
    }
}

/* -------------------------------------------------------------------------- */
/*                                    Deck                                    */
/* -------------------------------------------------------------------------- */
public final class Deck {

    private List<Card> cards; // List of cards in the deck

    // Constructor for deck with all cards
    public Deck() {
        initializeDeck(); // Use helper method to initialize cards
        shuffle(); // Randomize the deck
    }

    // Private helper to initialize all cards in the deck
    public void initializeDeck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        shuffle(); // Shuffle to randomize card order
    }

    // Shuffle to randomize card order
    public final void shuffle() {
        Collections.shuffle(cards);
    }

    // Deal specified count of cards directly to a player
    public void dealHand(Player player, int count) {
        for (int i = 0; i < count; i++) {
            if (cards.isEmpty()) {
                throw new IllegalArgumentException("Not enough cards left to deal.");
            }
            player.getHand().addCard(cards.remove(0));
        }
    }

    // Deal cards to all players
    public void dealAllHands(List<Player> players, int cardsPerPlayer) {
        for (Player player : players) {
            dealHand(player, cardsPerPlayer); // Directly deal cards to the player
        }
    }

    // Reset the deck to its full initial state
    public void reset() {
        initializeDeck(); // Use helper method to initialize cards
        shuffle(); // Randomize the deck
    }

    // Set the state of the deck (undo functionality)
    public void setCards(List<Card> newCards) {
        this.cards = new ArrayList<>(newCards); // Set the deck to the new state
    }

    // Get the current state of the deck
    public List<Card> getCards() {
        return new ArrayList<>(cards);  // Return a copy for immutability
    }

    // For debugging - to display the deck contents
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Deck: \n");
        for (Card card : cards) {
            sb.append(card).append("\n");
        }
        return sb.toString();
    }
}

/* -------------------------------------------------------------------------- */
/*                                    Hand                                    */
/* -------------------------------------------------------------------------- */
public class Hand {

    private List<Card> cards;

    // Constructor initializes an empty hand
    public Hand() {
        cards = new ArrayList<>();
    }

    // Add cards to the hand
    public void addCards(List<Card> newCards) {
        cards.addAll(newCards);
    }

    public void addCard(Card newCard) {
        cards.add(newCard);
    }

    // Get the current all cards in the hand
    public List<Card> getCards() {
        return new ArrayList<>(cards); // Return a copy of current hand
    }

    // Get a single card by index
    public final Card getCard(int index) {
        return cards.get(index);
    }

    // Remove a single card from the hand by index
    public void removeCard(int index) {
        cards.remove(index);
    }

    public void setCards(List<Card> newCards) {
        cards = new ArrayList<>(newCards);
    }

        // New method to sort the internal cards
    public void sortCards() {
        CardUtils.sortBySuitAndRank(cards);
    }

    // Check if the hand is empty / used for testing
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Display the hand as a string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hand: \n");
        for (Card card : cards) {
            sb.append(card).append("\n");
        }
        return sb.toString();
    }
}

/* -------------------------------------------------------------------------- */
/*                                   Player                                   */
/* -------------------------------------------------------------------------- */
public abstract class Player {
    
    protected String name;
    protected Hand hand;
    protected Team team;

    public static enum TrumpChoice {
        SKIP(null), // No corresponding suit for SKIP
        SPADES(Card.Suit.SPADES),
        HEARTS(Card.Suit.HEARTS),
        DIAMONDS(Card.Suit.DIAMONDS),
        CLUBS(Card.Suit.CLUBS);

        private final Card.Suit suit;

        private TrumpChoice(Card.Suit suit) {
            this.suit = suit;
        }

        public Card.Suit getSuit() {
            return suit;
        }
    }

    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
        this.hand = new Hand();
    }

    // Abstract method to choose trump
    public abstract TrumpChoice chooseTrumpOrSkip(int turnForChoosingTrump);

    // Abstract method to call zvanje
    public abstract List<Card> callZvanje(List<Integer> selectedIndices);
    
    // Abstract method to choose a card to be played
    public abstract int chooseCardToPlay(List<Integer> playableIndices);

    public final Card playCard(int index) {
        if (index < 0 || index >= hand.getCards().size()) {
            throw new IllegalArgumentException("Invalid card index.");
        }
        Card card = hand.getCard(index);
        hand.removeCard(index);
        return card;
    }

    // Get the name of the player
    public String getName() {
        return name;
    }

    // Get the hand of the player
    public Hand getHand() {
        return hand;
    }

    // Set the team of the player
    public Team getTeam() {
        return team;
    }

    // Display the hand of the player
    public void displayHand() {
        System.out.println(name + "'s Hand:");
        List<Card> cards = hand.getCards();
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ": " + cards.get(i));
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                                    Team                                    */
/* -------------------------------------------------------------------------- */
public class Team {
    private final String name;
    private List<Card> wonCards;
    private int bigs; // big score / current score in game
    public int smalls; // small score / points won in the current round

    public Team(String name) {
        this.name = name;
        this.wonCards = new ArrayList<>();
        this.bigs = 0; // Initialize big score
        this.smalls = 0; // Initialize small score
    }

    // Add the cards won in the current round to the team's won cards
    public void addWonCardsAsPoints(List<Card> cards) {
        wonCards.addAll(cards);
        for (Card card : cards) {
            smalls += card.getValue(); // Add card values to smalls
        }
    }

    public List<Card> getWonCards() {
        return wonCards;
    }

    public void resetWonCards() {
        wonCards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // Update the bigs score
    public void addBigs(int points) {
        System.out.println("Adding " + points + " points to " + name + " big score");
        this.bigs += points;
    }

    public void setBigs(int points) {
        this.bigs = points;
    }

    public int getBigs() {
        return bigs;
    }

    public void addSmalls(int points) {
        System.out.println("Adding " + points + " points to " + name + " small score");
        this.smalls += points;
    }

    public void setSmalls(int points) {
        this.smalls = points;
    }

    // Reset the smalls score
    public void resetSmalls() {
        this.smalls = 0;
    }

    public int getSmalls() {
        return smalls;
    }
}

/* -------------------------------------------------------------------------- */
/*                                 HumanPlayer                                */
/* -------------------------------------------------------------------------- */
public class HumanPlayer extends Player {

    private TrumpChoice trumpChoice = null; // Variable to store the trump suit choice
    private int cardIndexChoice = -1; // Variable to store the card choice
    private final Object lock = new Object(); // Lock object for synchronizing threads

    public HumanPlayer(String name, Team team) {
        super(name, team);
    }

    // IMPLEMENTATION NEEDED
    // Choose a card index to play
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
        synchronized (lock) { // Enter synchronized block to wait for input
            while (cardIndexChoice == -1) { // Keep waiting until a valid choice is made
                try {
                    System.out.println(this.getName() + "'s hand:");
                    for (int i = 0; i < hand.getCards().size(); i++) {
                        Card card = hand.getCard(i);
                        String playableMarker = playableIndexes.contains(i) ? " (Playable)" : "";
                        System.out.println(i + " - " + card + playableMarker);
                    }
                    System.out.println("Choose a card to play by entering the index:");
                    lock.wait(); // Pause this thread until notified
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                    return -1; // Return -1 in case of an interruption
                }
            }
            int chosenCardIndex = trumpChoice.ordinal(); // Use the ordinal value as the chosen card index
            cardIndexChoice = -1; // Reset for the next round
            return chosenCardIndex; // Return the chosen index to the game
        }
    }
    
    // Method called to select a card by the player
    public void selectCard(int index) {
        synchronized (lock) { // Enter synchronized block to notify after updating the choice
            if (index < 0 || index >= hand.getCards().size()) {
                throw new IllegalArgumentException("Invalid card index.");
            }
            cardIndexChoice = index; // Temporarily reuse trumpChoice for card selection
            lock.notify(); // Wake up the thread waiting for this input
        }
    }

    // Get selected cards from index to go check Zvanje
    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        List<Card> selectedCards = new ArrayList<>();
        for(int index : selectedIndices) {
            selectedCards.add(hand.getCard(index));
        }
        return selectedCards;
    }

    // NEEDS TO BE IMPLEMENTED CORRECTLY
    
    // Choose trump suit
    @Override
    public TrumpChoice chooseTrumpOrSkip(int turnForChoosingTrump) {
        synchronized (lock) { // Enter synchronized block to wait for input
            while (trumpChoice == null) { // Keep waiting until a valid choice is made
                try {
                    System.out.println(this.getName() + " is choosing trump...");
                    lock.wait(); // Pause this thread until notified
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                    return null; // Return null in case of an interruption
                }
            }
            TrumpChoice result = trumpChoice; // Save the chosen suit to return
            trumpChoice = null; // Reset for the next round
            return result; // Return the choice to the game
        }


    }

    // CALLED BY PLAYER
    // Method called when human input arrives
    public void trumpChoice(int choice) {
        synchronized (lock) { // Enter synchronized block to notify after updating the choice
            trumpChoice = switch (choice) {
                case 1 -> Player.TrumpChoice.SPADES;
                case 2 -> Player.TrumpChoice.HEARTS;
                case 3 -> Player.TrumpChoice.DIAMONDS;
                case 4 -> Player.TrumpChoice.CLUBS;
                case 5 -> Player.TrumpChoice.SKIP;
                default -> throw new IllegalArgumentException("Invalid trump choice.");
            }; // Update the trump choice
            lock.notify(); // Wake up the thread waiting for this input
        }
    }

}

/* -------------------------------------------------------------------------- */
/*                               AiPlayerNormal                               */
/* -------------------------------------------------------------------------- */
public class AiPlayerNormal extends Player {

    public AiPlayerNormal(String name, Team team) {
        super(name, team);
    }

    // Randomly choose a card to play from playable cards
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
        if (playableIndexes.isEmpty()) {
            throw new IllegalArgumentException("No playable cards available!");
        }
        Random random = new Random();
        return playableIndexes.get(random.nextInt(playableIndexes.size()));
    }

    // Choose Zvanje cards
    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        return new ArrayList<>(); // Keep this basic for easy AI
    }

    // Choose the trump suit based on the most valuable cards in hand
    @Override
    public TrumpChoice chooseTrumpOrSkip(int turnForChoosingTrump) {
        Map<Card.Suit, Integer> suitStrengths = new HashMap<>();
    
        // Calculate the total strength of each suit as if it were the trump suit
        for (Card card : hand.getCards()) {
            Card.Suit suit = card.getSuit();
            suitStrengths.put(
                suit,
                suitStrengths.getOrDefault(suit, 0) + card.getStrength(suit, null) // Treat `suit` as the trump suit
            );
        }
    
        // Find the suit with the highest total strength
        Card.Suit strongestSuit = suitStrengths.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    
        // If turnForChoosingTrump is 3, MUST choose a suit
        if (turnForChoosingTrump == 3) {
            if (strongestSuit == null) {
                // Fallback to the first available suit if no valid strong suit is found
                strongestSuit = suitStrengths.keySet().stream().findFirst().orElse(null);
            }
            if (strongestSuit != null) {
                System.out.println(name + " is forced to choose " + strongestSuit);
                return Player.TrumpChoice.valueOf(strongestSuit.name());
            }
        }
    
        // If no strong suit is found or its strength is <= 30, skip trump selection
        if (strongestSuit == null || suitStrengths.get(strongestSuit) <= 30) {
            System.out.println(name + " chooses to skip.");
            return Player.TrumpChoice.SKIP;
        }
    
        // Return the trump choice corresponding to the strongest suit
        System.out.println(name + " chooses " + strongestSuit);
        return Player.TrumpChoice.valueOf(strongestSuit.name());
    }
    
    


    // Group cards by suit
    private Map<Card.Suit, List<Card>> groupBySuit(List<Card> cards) {
        Map<Card.Suit, List<Card>> groupedBySuit = new HashMap<>();
        for (Card card : cards) {
            groupedBySuit.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }
        return groupedBySuit;
    }
}

/* -------------------------------------------------------------------------- */
/*                                AiPlayerHard                                */
/* -------------------------------------------------------------------------- */
public class AiPlayerHard extends Player {


    public AiPlayerHard(String name, Team team) {
        super(name, team);
    }

    // Choose the best card to play based on strategy
    @Override
    public int chooseCardToPlay(List<Integer> playableIndexes) {
        if (playableIndexes.isEmpty()) {
            throw new IllegalArgumentException("No playable cards available!");
        }

        // Group playable cards
        List<Card> playableCards = new ArrayList<>();
        for (int index : playableIndexes) {
            playableCards.add(hand.getCard(index));
        }

        // Find the weakest card that can win
        Card bestCard = playableCards.stream()
            .filter(card -> canWin(card)) // Filter cards that can win
            .min(Comparator.comparingInt(Card::getValue)) // Choose the weakest winning card
            .orElse(null);

        // If no winning card exists, play the lowest card
        if (bestCard == null) {
            return playableIndexes.stream()
                .min(Comparator.comparingInt(index -> hand.getCard(index).getValue()))
                .orElse(playableIndexes.get(0));
        }

        return playableIndexes.get(hand.getCards().indexOf(bestCard));
    }

    // Determine if the card can win the current round
    private boolean canWin(Card card) {
        // Implement logic to compare the card against the strongest card on the floor
        // Use trumpSuit, leadSuit, and other contextual variables
        return true; // Placeholder for actual logic
    }

    // Choose the trump suit based on the best scoring mechanism
    @Override
    public TrumpChoice chooseTrumpOrSkip(int turnForChoosingTrump) {
        Map<Card.Suit, Integer> suitScores = new HashMap<>();
        boolean hasJackAndNineCombo = false;
        Card.Suit bestSuit = null;
    
        // Evaluate hand to find potential trump suits
        for (Card card : hand.getCards()) {
            Card.Suit suit = card.getSuit();
    
            // Update suit scores, excluding EIGHT and SEVEN cards for final selection
            if (card.getRank() != Card.Rank.EIGHT && card.getRank() != Card.Rank.SEVEN) {
                int score = card.getStrength(suit, null); // Calculate strength assuming this suit as trump
                suitScores.put(suit, suitScores.getOrDefault(suit, 0) + score);
            }
    
            // Check if the suit contains both JACK and NINE
            boolean hasJack = hand.getCards().stream().anyMatch(c -> c.getSuit() == suit && c.getRank() == Card.Rank.JACK);
            boolean hasNine = hand.getCards().stream().anyMatch(c -> c.getSuit() == suit && c.getRank() == Card.Rank.NINE);
    
            if (hasJack && hasNine) {
                long supportingCards = hand.getCards().stream()
                    .filter(c -> (c.getSuit() == suit && c.getRank() != Card.Rank.JACK && c.getRank() != Card.Rank.NINE) || 
                                 (c.getRank() == Card.Rank.ACE && c.getSuit() != suit))
                    .count();
    
                if (supportingCards >= 2) {
                    hasJackAndNineCombo = true;
                    bestSuit = suit;
                }
            }
        }
    
        // If turnForChoosingTrump is 3, MUST choose a suit
        if (turnForChoosingTrump == 3) {
            if (bestSuit == null) {
                // Choose the suit with the highest score if no JACK and NINE combo
                bestSuit = suitScores.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            }
            if (bestSuit != null) {
                System.out.println(name + " is forced to choose " + bestSuit);
                return Player.TrumpChoice.valueOf(bestSuit.name());
            }
        }
    
        // Skip unless there is a valid JACK and NINE combo
        if (!hasJackAndNineCombo) {
            System.out.println(name + " chooses to skip.");
            return Player.TrumpChoice.SKIP;
        }
    
        // Return the trump choice corresponding to the best suit
        System.out.println(name + " chooses " + bestSuit);
        return Player.TrumpChoice.valueOf(bestSuit.name());
    }

    // Call Zvanje by detecting high-value combinations
    @Override
    public List<Card> callZvanje(List<Integer> selectedIndices) {
        Map<Card.Rank, List<Card>> groupedByRank = groupByRank(hand.getCards());
        Map<Card.Suit, List<Card>> groupedBySuit = groupBySuit(hand.getCards());

        List<Card> zvanjeCards = new ArrayList<>();
        detectFourOfAKind(groupedByRank, zvanjeCards);
        detectSequences(groupedBySuit, zvanjeCards);

        return zvanjeCards;
    }

    // Group cards by rank
    private Map<Card.Rank, List<Card>> groupByRank(List<Card> cards) {
        Map<Card.Rank, List<Card>> groupedByRank = new HashMap<>();
        for (Card card : cards) {
            groupedByRank.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
        }
        return groupedByRank;
    }

    // Group cards by suit
    private Map<Card.Suit, List<Card>> groupBySuit(List<Card> cards) {
        Map<Card.Suit, List<Card>> groupedBySuit = new HashMap<>();
        for (Card card : cards) {
            groupedBySuit.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }
        return groupedBySuit;
    }

    // Detect four-of-a-kind combinations
    private void detectFourOfAKind(Map<Card.Rank, List<Card>> groupedByRank, List<Card> zvanjeCards) {
        for (List<Card> cards : groupedByRank.values()) {
            if (cards.size() == 4) {
                zvanjeCards.addAll(cards);
            }
        }
    }

    // Detect sequences
    private void detectSequences(Map<Card.Suit, List<Card>> groupedBySuit, List<Card> zvanjeCards) {
        for (List<Card> cards : groupedBySuit.values()) {
            cards.sort(Comparator.comparing(card -> card.getRank().ordinal()));
            // Implement sequence detection logic here
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                                  CardUtils                                 */
/* -------------------------------------------------------------------------- */
public class CardUtils {

    // Get a list of all card indices in a hand / Used in RoundUtils.findPlayableCardIndexes
    public static List<Integer> getAllCardIndices(int handSize) {
        return IntStream.range(0, handSize)
                        .boxed()
                        .collect(Collectors.toList());
    }

    // Sort a list of cards by suit and rank
    public static void sortBySuitAndRank(List<Card> cards) {
        cards.sort(Comparator
                .comparing(Card::getSuit) // Sort by suit (primary)
                .thenComparing(card -> card.getRank().ordinal())); // Sort by rank (secondary)
    }

}

/* -------------------------------------------------------------------------- */
/*                              GameStateManager                              */
/* -------------------------------------------------------------------------- */
// public class GameStateManager {

//     private final Stack<CurrentState> gameStates = new Stack<>(); // Stack to store game states

//     /**
//      * Saves the current game state to the stack.
//      * @param currentState The current state of the game.
//      */
//     public void saveGameState(CurrentState currentState) {
//         gameStates.push(currentState);
//     }

//     /**
//      * Restores the last saved game state.
//      * @return The last saved CurrentState, or null if no state exists.
//      */
//     public CurrentState goBackLastState() {
//         if (gameStates.isEmpty()) {
//             System.err.println("No game states to restore!");
//             return null; // No state to restore
//         }

//         // Pop the last saved state
//         return gameStates.pop();
//     }

//     /**
//      * Creates a new CurrentState for the start of the game.
//      */
//     public static CurrentState createStartGameState(Team team1, Team team2, List<Player> players, ZvanjeService.ZvanjeResult zvanjeWin, int winTreshold, int dealerIndex) {
//         return new CurrentState(team1, team2, players, zvanjeWin, winTreshold, dealerIndex);
//     }

//     /**
//      * Creates a new CurrentState for mid-game.
//      */
//     public static CurrentState createMidGameState(Team team1, Team team2, List<Player> players, ZvanjeService.ZvanjeResult zvanjeWin, int winTreshold, int dealerIndex, 
//                                                   Card.Suit trumpSuit, Card.Suit leadSuit, int startingPlayerIndex, List<Card> onFloorCards) {
//         return new CurrentState(team1, team2, players, zvanjeWin, winTreshold, dealerIndex, trumpSuit, leadSuit, startingPlayerIndex, onFloorCards);
//     }

//     /**
//      * Creates a new CurrentState for a round.
//      */
//     public static CurrentState createRoundState(List<Card> onFloorCards, Card.Suit trumpSuit, Team team1, Team team2, int startingPlayerIndex) {
//         return new CurrentState(onFloorCards, trumpSuit, team1, team2, startingPlayerIndex);
//     }
// }

/* -------------------------------------------------------------------------- */
/*                                  GameUtils                                 */
/* -------------------------------------------------------------------------- */
public class GameUtils {
    
    // Initialize players and assign them to the given teams
    public static List<Player> initializePlayers(Difficulty difficulty, Team team1, Team team2) {
        List<Player> players = new ArrayList<>();

        // Assign human player to Team 1
        players.add(new HumanPlayer("Lovro", team1));

        // Create AI players based on difficulty
        switch (difficulty) {
            case EASY:
                players.add(new AiPlayerEasy("Bot 1", team2)); // Bot 1 in Team 2
                players.add(new AiPlayerEasy("Bot 2", team1)); // Bot 2 in Team 1
                players.add(new AiPlayerEasy("Bot 3", team2)); // Bot 3 in Team 2
                break;
            case NORMAL:
                players.add(new AiPlayerNormal("Bot 1", team2));
                players.add(new AiPlayerNormal("Bot 2", team1));
                players.add(new AiPlayerNormal("Bot 3", team2));
                break;
            case HARD:
                players.add(new AiPlayerHard("Bot 1", team2));
                players.add(new AiPlayerHard("Bot 2", team1));
                players.add(new AiPlayerHard("Bot 3", team2));
                break;
            case TEST:
                players.add(new AiPlayerEasy("Bot 1", team2));
                players.add(new AiPlayerNormal("Bot 2", team1));
                players.add(new AiPlayerHard("Bot 3", team2));
                break;
        }
        return players;
    }

    // Find the winner of the game
    public static Team findGameWinner(Team team1, Team team2, ZvanjeResult zvanjeWin, int winTreshold) {
        if (checkGamePass(team1, zvanjeWin)) {
            return team1;
        } else if (checkGamePass(team2, zvanjeWin)) {
            return team2;
        }
        return null;
    }

    // Check if the team has passed the game threshold
    public static boolean checkGamePass(Team team, ZvanjeResult zvanjeWin) {
        int threshold = calculateWinThreshold(zvanjeWin);
        // Check if the team has won Zvanje
        int zvanjePoints = (zvanjeWin != null && zvanjeWin.getWinningTeam() == team) ? zvanjeWin.getTotalPoints() : 0;
        // Check if the team has reached the threshold
        return (team.getSmalls() + zvanjePoints >= threshold);
    }

    // Calculate the threshold for winning the game
    public static int calculateWinThreshold(ZvanjeResult zvanjeWin) {
        int basePoints = 162; // Base points for a "čista igra"
        int zvanjePoints = (zvanjeWin != null) ? zvanjeWin.getTotalPoints() : 0;
        int totalPoints = basePoints + zvanjePoints;
        // Threshold to pass is half the total points plus 1
        return (totalPoints / 2) + 1;
    }    

    public static int calculateGamePoints(Team team, ZvanjeResult zvanjeWin) {
        int zvanjePoints = (zvanjeWin != null && zvanjeWin.getWinningTeam() == team) ? zvanjeWin.getTotalPoints() : 0;
        return team.getSmalls() + zvanjePoints;
    }

}

/* -------------------------------------------------------------------------- */
/*                                 RoundUtils                                 */
/* -------------------------------------------------------------------------- */
public class RoundUtils {
    
    public static List<Integer> findPlayableCardIndexes(List<Card> handCards, List<Card> onFloorCards, Card.Suit trumpSuit) {
        List<Integer> playableIndexes = new ArrayList<>();
    
        // If no cards are played yet, the player is starting and can play any card
        if (onFloorCards.isEmpty()) {
            // System.out.println("No cards on the floor. RoundUtils()");
            return CardUtils.getAllCardIndices(handCards.size());
        }
    
        // Determine the lead suit: trump suit if present on the floor, otherwise the suit of the first card
        Card.Suit leadSuit = onFloorCards.stream()
                                         .anyMatch(card -> card.getSuit() == trumpSuit) 
                                         ? trumpSuit 
                                         : onFloorCards.get(0).getSuit();

        Card strongestCard = findStrongestCard(onFloorCards, trumpSuit, leadSuit);
        int strongestStrength = strongestCard.getStrength(trumpSuit, leadSuit);
    
        // Step 1: Find all cards in hand that follow the lead suit or trump suit
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            boolean isLeadSuit = card.getSuit() == leadSuit;
            boolean isTrumpSuit = card.getSuit() == trumpSuit;
    
            // Rule: Only stronger cards can be played unless there are no stronger cards
            if ((isLeadSuit || isTrumpSuit) && card.getStrength(trumpSuit, leadSuit) > strongestStrength) {
                playableIndexes.add(i);
            }
        }
    
        // Step 2: If no stronger cards exist, allow any card to be played
        if (playableIndexes.isEmpty()) {
            // System.out.println("No stronger cards found. RoundUtils()");
            return CardUtils.getAllCardIndices(handCards.size());
        }

        //System.out.println("RoundUtils() Playable card indexes: " + playableIndexes);
        return playableIndexes;
    }
    

    public static Card findStrongestCard(List<Card> cards, Card.Suit trumpSuit, Card.Suit leadSuit) {
        return cards.stream()
                    .max(Comparator.comparingInt(c -> c.getStrength(trumpSuit, leadSuit)))
                    .orElseThrow(() -> new IllegalArgumentException("Card list is empty"));
    }

}

/* -------------------------------------------------------------------------- */
/*                                ZvanjeService                               */
/* -------------------------------------------------------------------------- */
public static class ZvanjeService {

    public enum ZvanjeType {
        FOUR_JACKS(200), FOUR_NINES(150), FOUR_OTHERS(100), SEQUENCE_OF_3(20),
        SEQUENCE_OF_4(50), SEQUENCE_OF_5_OR_MORE(100), BELOT(1001), BELA(20);

        private final int points;

        ZvanjeType(int points) {
            this.points = points;
        }

        public int getPoints() {
            return points;
        }
    }

    public static class ZvanjeResult {

        private final List<ZvanjeType> zvanjeTypes;
        private final ZvanjeType biggestZvanje;
        private final Player player;
        private int totalPoints;
    
        public ZvanjeResult(Player player, List<ZvanjeType> zvanjeTypes) {
            this(player, zvanjeTypes, 0, null);
        }

        public ZvanjeResult(Player player, List<ZvanjeType> zvanjeTypes, int totalPoints, ZvanjeType biggestZvanje) {
            this.player = player;
            this.zvanjeTypes = new ArrayList<>(zvanjeTypes);
            this.totalPoints = totalPoints;
            this.biggestZvanje = biggestZvanje;
        }
        
        public List<ZvanjeType> getZvanjeTypes() {
            return zvanjeTypes;
        }
    
        public ZvanjeType getBiggestZvanje() {
            return biggestZvanje;
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public void setPoints(int points) {
            this.totalPoints = points;
        }
    
        public Player getPlayer() {
            return player;
        }

        public Team getWinningTeam() {
            return player.getTeam();
        }
    }

    public static ZvanjeResult biggestZvanje(List<ZvanjeResult> results) {
        if (results == null || results.isEmpty()) {
            return null; // No results
        }
        // Find the maximum Zvanje points
        int maxPoints = results.stream()
            .filter(result -> result.getBiggestZvanje() != null) // Ignore players with no Zvanje
            .mapToInt(ZvanjeResult::getTotalPoints)
            .max()
            .orElse(0); // Default to 0 if no Zvanje is found
    
        // Filter results with the maximum points
        List<ZvanjeResult> topResults = results.stream()
            .filter(result -> result.getTotalPoints() == maxPoints)
            .collect(Collectors.toList());
    
        // Choose the result closest to dealer (who got their result first)
        return topResults.get(0);
    }
    

    // Detect all Zvanje types for a player
    public static ZvanjeResult detectPlayerZvanje(Player player, Card.Suit trumpSuit) {
        if (player == null || player.getHand() == null || player.getHand().getCards().isEmpty()) {
            return new ZvanjeResult(player, Collections.emptyList());
        }
    
        List<Card> cards = player.getHand().getCards();
        List<ZvanjeType> detectedZvanjeTypes = new ArrayList<>();
    
        // Detect four-of-a-kind Zvanje
        detectFourOfAKind(groupByRank(cards), detectedZvanjeTypes);
    
        // Detect sequences
        groupBySuit(cards).values().forEach(suitCards -> detectSequences(suitCards, detectedZvanjeTypes));
    
        // Detect special combinations like BELA
        detectBela(cards, trumpSuit, detectedZvanjeTypes);
    
        // Calculate total points and determine the biggest Zvanje
        int totalPoints = detectedZvanjeTypes.stream()
                .mapToInt(ZvanjeType::getPoints)
                .sum();
    
        ZvanjeType biggestZvanje = detectedZvanjeTypes.stream()
                .max(Comparator.comparingInt(ZvanjeType::getPoints))
                .orElse(null);
    
        return new ZvanjeResult(player, detectedZvanjeTypes, totalPoints, biggestZvanje);
    }

    // Group cards by rank
    private static Map<Card.Rank, List<Card>> groupByRank(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getRank));
    }

    // Group cards by suit
    private static Map<Card.Suit, List<Card>> groupBySuit(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getSuit));
    }



    private static void detectFourOfAKind(Map<Card.Rank, List<Card>> rankGroups, List<ZvanjeType> zvanjeTypes) {
        rankGroups.forEach((rank, groupedCards) -> {
            if (groupedCards.size() == 4) {
                ZvanjeType zvanje = switch (rank) {
                    case JACK -> ZvanjeType.FOUR_JACKS;
                    case NINE -> ZvanjeType.FOUR_NINES;
                    case EIGHT, SEVEN -> null; // Skip these cases
                    default -> ZvanjeType.FOUR_OTHERS;
                };
                if (zvanje != null) {
                    zvanjeTypes.add(zvanje);
                }
            }
        });
    }
    

    private static void detectSequences(List<Card> suitCards, List<ZvanjeType> zvanjeTypes) {
        suitCards.sort(Comparator.comparingInt(card -> card.getRank().ordinal()));

        List<Card> currentSequence = new ArrayList<>();
        for (Card card : suitCards) {
            if (!currentSequence.isEmpty() &&
                    card.getRank().ordinal() != currentSequence.get(currentSequence.size() - 1).getRank().ordinal() + 1) {
                evaluateSequence(currentSequence, zvanjeTypes);
                currentSequence.clear();
            }
            currentSequence.add(card);
        }
        evaluateSequence(currentSequence, zvanjeTypes);
    }

    private static void evaluateSequence(List<Card> sequence, List<ZvanjeType> zvanjeTypes) {
        int size = sequence.size();
        if (size >= 3) {
            ZvanjeType zvanje = switch (size) {
                case 8 -> ZvanjeType.BELOT;
                case 5, 6, 7 -> ZvanjeType.SEQUENCE_OF_5_OR_MORE;
                case 4 -> ZvanjeType.SEQUENCE_OF_4;
                case 3 -> ZvanjeType.SEQUENCE_OF_3;
                default -> null;
            };
            if (zvanje != null) {
                zvanjeTypes.add(zvanje);
            }
        }
    }
    private static void detectBela(List<Card> cards, Card.Suit trumpSuit, List<ZvanjeType> zvanjeTypes) {
        List<Card> belaCards = cards.stream()
                .filter(card -> card.getSuit() == trumpSuit &&
                        (card.getRank() == Card.Rank.KING || card.getRank() == Card.Rank.QUEEN))
                .toList();
        if (belaCards.size() == 2) {
            zvanjeTypes.add(ZvanjeType.BELA);
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                                CurrentState                                */
/* -------------------------------------------------------------------------- */
// public class CurrentState {
//     private final Team team1, team2;
//     private final ZvanjeResult zvanjeWin;
//     private final int winTreshold;
//     private final Card.Suit trumpSuit;
//     private final Card.Suit leadSuit;
//     private final int dealerIndex;
//     private final int startingPlayerIndex;
//     private final List<Card> onFloorCards;
//     private final List<Player> players;
//     // Constructor for the start of the GAME (after Zvanje)
//     public CurrentState(Team team1, Team team2, List<Player> players, ZvanjeResult zvanjeWin, int winTreshold, int dealerIndex) {
//         this.team1 = team1;
//         this.team2 = team2;
//         this.players = players;
//         this.zvanjeWin = zvanjeWin;
//         this.winTreshold = winTreshold;
//         this.dealerIndex = dealerIndex;
//         this.trumpSuit = null;
//         this.leadSuit = null;
//         this.startingPlayerIndex = -1;
//         this.onFloorCards = null;
//     }
//     // Constructor for mid-GAME state (used by advanced AI)
//     public CurrentState(Team team1, Team team2, List<Player> players, ZvanjeResult zvanjeWin, int winTreshold, int dealerIndex, 
//                         Card.Suit trumpSuit, Card.Suit leadSuit, int startingPlayerIndex, List<Card> onFloorCards) {
//         this.team1 = team1;
//         this.team2 = team2;
//         this.players = players;
//         this.zvanjeWin = zvanjeWin;
//         this.winTreshold = winTreshold;
//         this.dealerIndex = dealerIndex;
//         this.trumpSuit = trumpSuit;
//         this.leadSuit = leadSuit;
//         this.startingPlayerIndex = startingPlayerIndex;
//         this.onFloorCards = onFloorCards;
//     }
//     // Constructor for the ROUND (used by simple AI)
//     public CurrentState(List<Card> onFloorCards, Card.Suit trumpSuit, Team team1, Team team2, int startingPlayerIndex) {
//         this.team1 = team1;
//         this.team2 = team2;
//         this.onFloorCards = onFloorCards;
//         this.trumpSuit = trumpSuit;
//         this.startingPlayerIndex = startingPlayerIndex;
//         this.zvanjeWin = null;
//         this.winTreshold = 0;
//         this.dealerIndex = -1;
//         this.leadSuit = null;
//         this.players = null;
//     }
//     // Getters for all attributes
//     public Team getTeam1() {
//         return team1;
//     }
//     public Team getTeam2() {
//         return team2;
//     }
//     public ZvanjeResult getZvanjeWin() {
//         return zvanjeWin;
//     }
//     public int getWinTreshold() {
//         return winTreshold;
//     }
//     public Card.Suit getTrumpSuit() {
//         return trumpSuit;
//     }
//     public Card.Suit getLeadSuit() {
//         return leadSuit;
//     }
//     public int getDealerIndex() {
//         return dealerIndex;
//     }
//     public int getStartingPlayerIndex() {
//         return startingPlayerIndex;
//     }
//     public List<Card> getOnFloorCards() {
//         return onFloorCards;
//     }
//     public List<Player> getPlayers() {
//         return players;
//     }
// }

/* -------------------------------------------------------------------------- */
/*                                    ROUND                                   */
/* -------------------------------------------------------------------------- */
public class Round {
    private final List<Player> players;
    private final Suit trumpSuit;
    private final int startingPlayerIndex;
    private List<Card> onFloorCards;

    public Round(List<Player> players, int startingPlayerIndex, Suit trumpSuit) {
        this.players = players;
        this.startingPlayerIndex = startingPlayerIndex;
        this.trumpSuit = trumpSuit;
        this.onFloorCards = new ArrayList<>();
    }

    // Play 4 turns, determine the round winner, and return the index of the next starting player
    public int start(int i) {

        // Reset floor cards for the new round
        onFloorCards = new ArrayList<>();

        // Play turns for all players
        playTurns(startingPlayerIndex);

        // Print the cards on the floor for each player                 !!!!!!!!!!!!!!!!!!!!!!!!!!
        for (int turn = 0; turn < players.size(); turn++) {
            Player currentPlayer = players.get((startingPlayerIndex + turn) % 4);
            Card playedCard = onFloorCards.get(turn);
            System.out.println(currentPlayer.getName() + " : " + playedCard);
        }

        // Determine the round winner
        Player winner = returnWinner(startingPlayerIndex);
        // Award 10 points if this is the 7th round
        if (i == 7) {
            winner.getTeam().addSmalls(10);
            System.out.println("Bonus 10 points awarded to " + winner.getTeam().getName() + " for winning the 7th round!");
        }
        
        System.out.println();
        // Add the won cards and their points to the winning player's team
        winner.getTeam().addWonCardsAsPoints(onFloorCards);
    
        // Return the index of the next starting player
        return players.indexOf(winner);
    }
    
    // Play each turn in the round 
    private void playTurns(int startingPlayerIndex) {
        for (int turn = 0; turn < players.size(); turn++) {
            Player currentPlayer = players.get((startingPlayerIndex + turn) % 4);

            // Get playable card indexes and let the player choose one
            List<Integer> playableIndexes = RoundUtils.findPlayableCardIndexes(
                currentPlayer.getHand().getCards(), onFloorCards, trumpSuit);

            if (playableIndexes.isEmpty()) {
                throw new IllegalStateException("No playable cards available.");
            }

            int chosenCardIndex = currentPlayer.chooseCardToPlay(playableIndexes);

            // Play the chosen card
            Card playedCard = currentPlayer.playCard(chosenCardIndex);
            onFloorCards.add(playedCard);
        }
    }

    // Determine and award points to the round winner and get starter for the next round
    private Player returnWinner(int startingPlayerIndex) {
        // Assert that there are cards on the floor
        if (onFloorCards.isEmpty()) {
            throw new IllegalStateException("No cards played this round.");
        }        
        // Find the strongest card on the floor
        Suit leadSuit = onFloorCards.get(0).getSuit();
        Card strongestCard = RoundUtils.findStrongestCard(onFloorCards, trumpSuit, leadSuit);

        // Find the player and his team who played the strongest card
        int winningPlayerIndex = (startingPlayerIndex + onFloorCards.indexOf(strongestCard)) % 4;
        Player winningPlayer = players.get(winningPlayerIndex);


        System.out.println();
        System.out.println("WIN CARD: " + strongestCard.toString());
        System.out.println("Round winner: " + winningPlayer.getName());
        System.out.println("Points won: " + onFloorCards.stream().mapToInt(Card::getValue).sum());
        
        return winningPlayer;
    }
}

/* -------------------------------------------------------------------------- */
/*                                    GAME                                    */
/* -------------------------------------------------------------------------- */
public class Game {

    private final Team team1, team2;
    private final List<Player> players;
    private ZvanjeResult zvanjeWin;
    private int winTreshold;
    private final Deck deck;
    private Card.Suit trumpSuit;
    private final int dealerIndex;

    public enum Difficulty {
        EASY, NORMAL, HARD, TEST
    }

    public Game(List<Player> players, Team team1, Team team2, int dealerIndex) {
        this.players = players;
        this.team1 = team1;
        this.team2 = team2;
        this.deck = new Deck();
        this.zvanjeWin = null;
        this.dealerIndex = dealerIndex;        
    }
 
    /* ------------------------------- start game ------------------------------- */
    public void startGame() {
        System.out.println("Game started!");

        // Initialize the game
        initializeGame();
    
        // Play 1 deck of rounds and check for a winner
        playRounds();

        // Award points to the winning team
        awardGameVictory();

        // Find game winner team
        Team winner = lookForWinner();

        if (winner != null) {
            System.out.println("Final scores: " + team1.getName() + " - " + team1.getBigs() + ", " + team2.getName() + " - " + team2.getBigs());
            finishGame(winner);
        }
    
    }

    public Team lookForWinner() {
        // Check if a team has crossed the win threshold
        Team winner = GameUtils.findGameWinner(team1, team2, zvanjeWin, winTreshold);
        if (winner != null) {
            System.out.println("Game over! Winner: " + winner.getName()); 
        } else {
            System.out.println("Game continues. Current scores: " + team1.getName() + " - " + team1.getBigs() + ", " + team2.getName() + " - " + team2.getBigs());
        }
        return winner;
    }


    public void initializeGame() {
        // Shuffle the deck in front of players
        deck.initializeDeck();
        // Secure reset small points for both teams
        team1.resetSmalls();
        team2.resetSmalls();
        // Assert that the deck size is 32
        if (deck.getCards().size() != 32) {
            throw new IllegalStateException("Deck size must be 32 cards.");
        }
        // Deal 6 cards to players to choose trump suit
        deck.dealAllHands(players, 6);

        System.out.println(players.get(dealerIndex).getName() + " is the dealer.");
        System.out.println();
        // Choose trump suit
        trumpSuit = chooseTrumpSuit(dealerIndex);

        // Assert that deck has 8 cards left after dealing all cards and that trump suit is chosen
        if (deck.getCards().size() != 8 || trumpSuit == null) {
            throw new IllegalStateException("Choosing Trump Suit failed.");
        }

        // Sort all hands, deal last 2 cards and update card values
        updateCardValues(trumpSuit);
        deck.dealAllHands(players, 2);
        sortAllPlayersHands(players);

        // Determine, announce, and award Zvanje
        System.out.println("ZVANJE:");
        zvanjeWin = reportZvanje(trumpSuit, dealerIndex);

        // Calculate the winning threshold
        winTreshold = GameUtils.calculateWinThreshold(zvanjeWin);

    }
    
    /* ------------------------------- start game ------------------------------- */

    // Play 8 rounds and check for a winner after each round
    private void playRounds() {

        // Assert that last player has 8 cards after dealing all cards
        if (players.get((dealerIndex + 3) % 4).getHand().getCards().size() != 8) {
            throw new IllegalStateException("Players should have 8 cards.");
        }
        
        int startingPlayerIndex = (dealerIndex + 1) % 4; // Player next to the dealer
        for (int i = 0; i < 8; i++) {
            System.out.println("Round " + (i + 1));
            // Start a new round and get the winner's index
            Round round = new Round(players, startingPlayerIndex, trumpSuit);
            int winnerIndex = round.start(i);
            // Winner starts the next round
            startingPlayerIndex = winnerIndex;
        }
    }

    public void awardGameVictory() {
        // Check if the dealer's team has crossed the win threshold
        Team dealerTeam = players.get(dealerIndex).getTeam();
        Team otherTeam = (dealerTeam == team1) ? team2 : team1;

        int dealerTeamPoints = GameUtils.calculateGamePoints(dealerTeam, zvanjeWin);
        int otherTeamPoints = GameUtils.calculateGamePoints(otherTeam, zvanjeWin);

        // Check if the dealer's team has crossed the win threshold
        if (dealerTeamPoints >= winTreshold) {
            dealerTeam.addBigs(dealerTeamPoints);
            otherTeam.addBigs(otherTeamPoints);
            System.out.println();
            System.out.println("Dealer's team PASSED!: " + dealerTeamPoints);
            System.out.println("Other team: " + otherTeamPoints);
        } else {
            // Award points to the other team
            otherTeam.addBigs(otherTeamPoints + dealerTeamPoints);
            System.out.println();
            System.out.println("Dealer's team didnt pass! Dealer's team: " + dealerTeam.getBigs());
            System.out.println("Other team GETS ALL: " + otherTeam.getBigs());
        }
    }
    

    private void finishGame(Team winner) {
        System.out.println("Game found winner! Winner: " + winner.getName());
    }    

    private ZvanjeResult reportZvanje(Card.Suit trumpSuit, int dealerIndex) {
        List<ZvanjeResult> zvanjeResults = new ArrayList<>();
    
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            // Calculate the current player's index, starting from dealerIndex and wrapping around
            int currentIndex = (dealerIndex + i + 1) % numPlayers;
            Player player = players.get(currentIndex);

            ZvanjeResult result = ZvanjeService.detectPlayerZvanje(player, trumpSuit);
            zvanjeResults.add(result);

            // Print player's cards
            System.out.println();
            player.displayHand();
        }
        ////////////////////////////////////////////////////
        System.out.println();
        for (ZvanjeResult result : zvanjeResults) {
            System.out.println(result.getPlayer().getName() + "'s ZvanjeTypes: " + result.getZvanjeTypes());
        }
        System.out.println();
        // Find the player with the highest Zvanje
        ZvanjeResult winningZvanjeResult = ZvanjeService.biggestZvanje(zvanjeResults);
    
        // If no Zvanje is detected for any player, return null
        if (winningZvanjeResult == null) {

            System.out.println("No Zvanje detected for any player.");

            return null;
        }
    
        Player winningPlayer = winningZvanjeResult.getPlayer();
        Team winningTeam = winningPlayer.getTeam();
    
        // Calculate total points for the winning player's team
        int totalPoints = calculateTotalZvanjePoints(winningTeam, zvanjeResults);
        // Update the winning ZvanjeResult with the total points
        winningZvanjeResult.setPoints(totalPoints);


        // Print results
        System.out.println("Player with the highest Zvanje: " + winningPlayer.getName());
        System.out.println("Winning Team: " + winningTeam.getName());
        System.out.println("Total Zvanje Points: " + totalPoints);
        // Print ZvanjeTypes for the winning team
        System.out.println("ZvanjeTypes for the winning team:");
                zvanjeResults.stream()
                        .filter(result -> result.getPlayer().getTeam() == winningTeam)
                        .flatMap(result -> result.getZvanjeTypes().stream())
                        .forEach(System.out::println);
    
        return winningZvanjeResult;
    }
    
    // Method to calculate total Zvanje points for a given team
    private int calculateTotalZvanjePoints(Team team, List<ZvanjeResult> zvanjeResults) {
        // Filter ZvanjeResults for the given team and sum up the points
        return zvanjeResults.stream()
                .filter(result -> result.getPlayer().getTeam() == team)
                .flatMap(result -> result.getZvanjeTypes().stream())
                .mapToInt(ZvanjeType::getPoints)
                .sum();
    }

    // Method to update values for all cards in the deck and players' hands
    public void updateCardValues(Card.Suit trumpSuit) {
        // Update values for all cards in the deck
        for (Card card : deck.getCards()) {
            card.calculateValue(trumpSuit);
        }
        // Update values for all cards in players' hands
        for (Player player : players) {
            for (Card card : player.getHand().getCards()) {
                card.calculateValue(trumpSuit);
            }
        }
    }

    // Method to choose the trump suit by going around the table and forcing a choice if all players skip (Human com: trumpChoice(int choice))
    private Card.Suit chooseTrumpSuit(int dealerIndex) {
        int currentIndex = (dealerIndex + 1) % players.size(); // Player next to the dealer
        int skips = 0;
        TrumpChoice finalChoice = null;

        CurrentState currentState = new CurrentState(team1, team2, players, zvanjeWin, winTreshold, dealerIndex);

        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(currentIndex);
            System.out.println(currentPlayer.getName() + "'s turn to choose trump suit.");
            if(currentPlayer instanceof HumanPlayer) {
                System.out.println("""
                    Choose Your Trump Suit Option:
                    0. Skip Trump Selection
                    1. Spades
                    2. Hearts
                    3. Diamonds
                    4. Clubs
                    -> trumpChoice(int choice)
                    Please make your choice (0-4): 
                    """);
            }

            if (i == players.size() - 1 && skips == players.size()) {
                System.out.println(currentPlayer.getName() + " MUST choose a trump suit:");
            }
            TrumpChoice playerChoice = currentPlayer.chooseTrumpOrSkip(i);

            if (playerChoice != TrumpChoice.SKIP) {
                finalChoice = playerChoice;
                System.out.println(currentPlayer.getName() + " chose " + finalChoice);
                break;
            } else {
                skips++;
                System.out.println(currentPlayer.getName() + " skipped.");
            }
                System.out.println(currentPlayer.getName() + " finally chose " + finalChoice);

            currentIndex = (currentIndex + 1) % players.size(); // Move to the next player
        }

        return finalChoice != null ? finalChoice.getSuit() : null; // Use enum conversion
    }


    // Sort all players' hands by suit and rank
    public static void sortAllPlayersHands(List<Player> players) {
        for (Player player : players) {
            player.getHand().sortCards(); // Sort each player's hand in place
        }
    }



}

/* -------------------------------------------------------------------------- */
/*                                    MATCH                                   */
/* -------------------------------------------------------------------------- */
public class Match {

    public Game game;
    public final Team team1, team2;
    private final List<Player> players;
    private int dealerIndex;
    public static final int WINNING_SCORE = 501; // The score required to win the match
    private final HumanPlayer humanPlayer;
    private Team winner; // Reference to the winning team

    public Match(Difficulty difficulty) {
        this.team1 = new Team("Team 1");
        this.team2 = new Team("Team 2");
        players = GameUtils.initializePlayers(difficulty, team1, team2);
        this.dealerIndex = 3; // Start with the last player as the dealer so YOU can play first
        this.humanPlayer = (HumanPlayer) players.get(0); // Initialize humanPlayer
    }

    public void startMatch() throws InterruptedException {
        boolean finished = false;

        while (!finished) {

            System.out.println("! Starting a new game...");
            game = new Game(players, team1, team2, dealerIndex);
            // Assertions to verify initial conditions of teams
            assertInitialTeamState(team1);
            assertInitialTeamState(team2);
            // Start the game and check for a winner
            game.startGame();
            
            System.out.println();
            System.out.println("Game over! Scores:");
            System.out.println(team1.getName() + ": " + team1.getBigs());
            System.out.println(team2.getName() + ": " + team2.getBigs());
            System.out.println();

            // Check if the match is over
            winner = matchWinner();

            if (winner != null) {
                System.out.println("Match winner found: " + winner.getName());
                finished = true; // Stop the loop when a winner is found
            } else {
                System.out.println("No winner yet, starting a new GAME...");
                rotateDealer(); // Rotate the dealer and prepare for the next game
                Thread.sleep(3000); // Optional delay
            }

            // reset valus for the next game
            resetForNextGame();

        }

        // Additional actions after the match ends (e.g., cleanup, next steps)
        handleMatchEnd(winner);
    }

    private void assertInitialTeamState(Team team) {
        assert team.getSmalls() == 0 : team.getName() + " small score should be 0 at the start of the match";
        assert team.getBigs() == 0 : team.getName() + " big score should be 0 at the start of the match";
        for (Player player : players) {
            assert player.getHand().isEmpty() : player.getName() + "'s hand should be empty at the start of the match";
        }
    }

    private void resetForNextGame() {
        team1.setSmalls(0);
        team2.setSmalls(0);
        team1.resetWonCards();
        team2.resetWonCards();
        System.out.println("Teams have been reset for the next game.");
    }

    public Team matchWinner() {
        // The match is over if either team reaches or exceeds the winning score
        if (team1.getBigs() >= WINNING_SCORE) {
            return team1;
        } else if (team2.getBigs() >= WINNING_SCORE) {
            return team2;
        }
        return null;
    }

    private void rotateDealer() {
        dealerIndex = (dealerIndex + 1) % 4; // Rotate dealer index among 4 players
    }
    
    private void handleMatchEnd(Team winner) {
        // Additional actions after the match ends (e.g., cleanup, next steps)
        // For example, update player statistics, save match results, etc.
        System.out.println();
        System.out.println("Match is over! Final scores:");
        System.out.println(team1.getName() + ": " + team1.getBigs());
        System.out.println(team2.getName() + ": " + team2.getBigs());
        System.out.println("Match ended. Winner: " + winner.getName());
    }

    // Method to be called from the GUI when the human player chooses a trump suit
    public void trumpDecision(int choice) {
        humanPlayer.trumpChoice(choice); // humanPlayer is your HumanPlayer instance
    }
}