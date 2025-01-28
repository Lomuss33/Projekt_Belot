// ░░░░░░░▄█▄▄▄█▄
// ▄▀░░░░▄▌─▄─▄─▐▄░░░░▀▄
// █▄▄█░░▀▌─▀─▀─▐▀░░█▄▄█
// ░▐▌░░░░▀▀███▀▀░░░░▐▌
// ████░▄█████████▄░████


package controllers;

import ai.HumanPlayer;
import controllers.ZvanjeService.ZvanjeResult;
import controllers.ZvanjeService.ZvanjeType;
import java.util.*;
import models.*;
import models.Player.TrumpChoice;

public class Game implements Cloneable {

    private Match.Difficulty difficulty;
    private Team team1, team2;
    private List<Player> players;
    private List<ZvanjeResult> zvanjeResults;
    private ZvanjeResult zvanjeWin;
    private int winTreshold;
    private Deck deck;
    private Card.Suit trumpSuit;
    private int dealerIndex;
    private int roundStarterIndex;
    private int roundCount;
    private boolean teamPassed;
    private Team trumpTeam;
    private boolean midRound;
    private Round currentRound;

    public Game(List<Player> players, Team team1, Team team2, int dealerIndex, Match.Difficulty difficulty) {
        this.players = players;
        this.team1 = team1;
        this.team2 = team2;
        this.deck = new Deck();
        this.dealerIndex = dealerIndex;  
        this.difficulty = difficulty;  
        this.roundStarterIndex = (dealerIndex + 1) % 4; // Player next to the dealer starts    
        this.roundCount = 0;
        this.midRound = false;
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
    }

    protected boolean trumpSelection() { 
        trumpSuit = chooseTrumpSuit(dealerIndex);
        if (trumpSuit != null) {
            System.out.println();
            System.out.println("Trump suit chosen: " + trumpSuit + " " + trumpSuit.getSymbol());
            System.out.println("Trump team: " + trumpTeam.getName());
            System.out.println("---------------------");
            System.out.println();
            // Reset decisionMade for all players
            for (Player player : players) {
                player.setWaiting(false);
            }
            return true;
        } else {
            return false;
        }
    }
    
    protected void findZvanje() {
        // Sort all hands, deal last 2 cards and update card values
        updateCardValues(trumpSuit);
        deck.dealAllHands(players, 2);
        sortAllPlayersHands(players);        
        zvanjeWin = reportZvanje(trumpSuit, dealerIndex);
        winTreshold = calculateWinThreshold(zvanjeWin);
    }

    // Play 8 rounds and check for a winner after each round
    public boolean playRounds() {
        for (int i = roundCount; i < 8; i++) { // Resume from saved round count
            System.out.println("Round " + (i + 1));
            System.out.println("---------------------");
            displayScores();
            // Start a new round and get the winner's index
            System.out.println();
            System.out.println("Round starts: " + players.get(roundStarterIndex).getName());
            if (!midRound) currentRound = new Round(players, roundStarterIndex, trumpSuit, difficulty); // Start a new round
            int winnerIndex = currentRound.playTurns(i); // Start the round and find its winner 
            if(winnerIndex == -1) { 
                midRound = true; 
                return false; // If the round is not over, HumanPlayer is playing
            }
            roundStarterIndex = winnerIndex; // Winner starts the next round
            resetPlayersWaitingStatus();
            roundCount++; // Increment the round count
        }
        awardGameVictory(); // Award the game victory to the winning team
        roundCount = 0; // Reset round count for the next game
        return true; // End of all rounds 
    }

    public void displayScores() {
        if (difficulty == Match.Difficulty.LEARN) { 
            System.out.println(team1 + " - Bigs: " + team1.getBigs() + ", Smalls: " + team1.getSmalls());
            System.out.println(team2 + " - Bigs: " + team2.getBigs() + ", Smalls: " + team2.getSmalls());
        } else if (difficulty == Match.Difficulty.NORMAL) {
            System.out.println(team1 + " - Bigs: " + team1.getBigs());
            System.out.println(team2 + " - Bigs: " + team2.getBigs());
        }
    }

    private void resetPlayersWaitingStatus() {
        for(Player player : players) { // Reset all players' waiting status
            player.setWaiting(false);
        }
    }

    private void awardGameVictory() {
        Team dealerTeam = getTrumpTeam();
        Team otherTeam = (dealerTeam == team1) ? team2 : team1;
        int dealerTeamPoints = calculateGamePoints(dealerTeam, zvanjeWin);
        int otherTeamPoints = calculateGamePoints(otherTeam, zvanjeWin);
        System.err.println("---------------------");
        System.err.println("Game over, all rounds played!");
        if (dealerTeamPoints >= winTreshold) {
            handleDealerTeamVictory(dealerTeam, otherTeam, dealerTeamPoints, otherTeamPoints);
        } else {
            handleOtherTeamVictory(dealerTeam, otherTeam, dealerTeamPoints, otherTeamPoints);
        }
    }

    private void handleDealerTeamVictory(Team dealerTeam, Team otherTeam, int dealerTeamPoints, int otherTeamPoints) {
        teamPassed = true;
        dealerTeam.setAwardedBigs(dealerTeamPoints);
        dealerTeam.addBigs(dealerTeamPoints);
        otherTeam.setAwardedBigs(otherTeamPoints);
        otherTeam.addBigs(otherTeamPoints);
        System.out.println();
        System.out.println("Dealer's team " + dealerTeam.getName() + " PASSED!: " + dealerTeamPoints);
        System.out.println("Other team " + dealerTeam.getName() + " points: " + otherTeamPoints);
    }

    private void handleOtherTeamVictory(Team dealerTeam, Team otherTeam, int dealerTeamPoints, int otherTeamPoints) {
        teamPassed = false;
        otherTeam.setAwardedBigs(otherTeamPoints + dealerTeamPoints);
        otherTeam.addBigs(otherTeamPoints + dealerTeamPoints);
        System.out.println();
        System.out.println("Dealer's team " + dealerTeam.getName() + " did NOT PASS! Points: " + dealerTeam.getBigs());
        System.out.println("Other team GETS ALL: " + otherTeam.getBigs());
    }

    private ZvanjeResult reportZvanje(Card.Suit trumpSuit, int dealerIndex) {
        System.out.println("Match phase: SHOW_ZVANJE");
        zvanjeResults = detectAllZvanje(trumpSuit, dealerIndex);
        players.get(0).displayHand(); // print the hand of the human player

        ZvanjeResult winningZvanjeResult = ZvanjeService.biggestZvanje(zvanjeResults);
        if (winningZvanjeResult == null) {
            System.out.println("No Zvanje detected for any player.");
            return null;
        } else {
            updateWinningZvanjeResult(winningZvanjeResult);
            printZvanjeResults();
        }
        return winningZvanjeResult;
    }

    private List<ZvanjeResult> detectAllZvanje(Card.Suit trumpSuit, int dealerIndex) {
        zvanjeResults = new ArrayList<>();
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            int currentIndex = (dealerIndex + i + 1) % numPlayers;
            Player player = players.get(currentIndex);
            ZvanjeResult result = ZvanjeService.detectPlayerZvanje(player, trumpSuit);
            zvanjeResults.add(result);
            
        }
        return zvanjeResults;
    }

    private void printZvanjeResults() {
        System.out.println("Zvanje Results:");
        ZvanjeResult biggestZvanje = ZvanjeService.biggestZvanje(zvanjeResults);
        if (biggestZvanje != null) {
            int totalPoints = biggestZvanje.getTotalPoints();
            Team winningTeam = biggestZvanje.getZvanjeTeam();
            System.out.println("Player with the highest Zvanje: " + biggestZvanje.getPlayer().getName() + " from " + winningTeam.getName()); 
            System.out.println("Players of " + winningTeam.getName() +  " won Zvanje: " + totalPoints + " points."); 
            System.out.println();
            for (Player player : players) {
                int inxPlayer = players.indexOf(player); 
                if (player.getTeam() == winningTeam) {
                    System.out.println(player.getName() + "'s ZvanjeTypes: " + zvanjeResults.get(inxPlayer).getZvanjeTypes());
                    System.out.println(player.getName() + " cards revealed: " + zvanjeResults.get(inxPlayer).getCardsOfZvanje()); 
                    System.out.println();
                }
            }

            if (zvanjeResults.get(0).getTotalPoints() == 0) {
                System.out.println("You have no Zvanje.");
            } else if (players.get(0).getTeam() == winningTeam) {
                System.out.println("Your Team won the Zvanje!");
            } else {
            System.out.println(players.get(0).getName() + " ZvanjeTypes:");
                System.out.println(zvanjeResults.get(0).getZvanjeTypes());
            }
        } else {
            System.out.println("No Zvanje detected for any player.");
        }
        System.out.println();
    }

    private void updateWinningZvanjeResult(ZvanjeResult winningZvanjeResult) {
        Player winningPlayer = winningZvanjeResult.getPlayer();
        Team winningTeam = winningPlayer.getTeam();
        int totalPoints = calculateTotalZvanjePoints(winningTeam);
        winningZvanjeResult.setPoints(totalPoints);
    }

    // Method to calculate total Zvanje points for a given team
    private int calculateTotalZvanjePoints(Team team) {
        // Filter ZvanjeResults for the given team and sum up the points
        return zvanjeResults.stream()
                .filter(result -> result.getPlayer().getTeam() == team)
                .flatMap(result -> result.getZvanjeTypes().stream())
                .mapToInt(ZvanjeType::getPoints)
                .sum();
    }

    // Method to calculate game points for a given team
    private static int calculateGamePoints(Team team, ZvanjeResult zvanjeWin) {
        int zvanjePoints = (zvanjeWin != null && zvanjeWin.getZvanjeTeam() == team) ? zvanjeWin.getTotalPoints() : 0;
        return team.getSmalls() + zvanjePoints;
    }

    // Calculate the threshold for winning the game 
    private static int calculateWinThreshold(ZvanjeResult zvanjeWin) {
        int basePoints = 162; // Base points for a "clean game"
        int zvanjePoints = (zvanjeWin != null) ? zvanjeWin.getTotalPoints() : 0;
        int totalPoints = basePoints + zvanjePoints;
        // Threshold to pass is half the total points plus 1
        return (totalPoints / 2) + 1;
    }    

    // Method to update values for all cards in the deck and players' hands
    private void updateCardValues(Card.Suit trumpSuit) {
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

    // Method to choose the trump suit
    private Card.Suit chooseTrumpSuit(int dealerIndex) { 
        int currentIndex = (dealerIndex + 1) % players.size();
        TrumpChoice finalChoice = null;
    
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(currentIndex);
    
            System.out.println(currentPlayer.getName() + "'s turn to choose trump suit.");
    
            if (currentPlayer instanceof HumanPlayer) {
                TrumpChoice humanChoice = handleHumanPlayerChoice((HumanPlayer) currentPlayer, i);
                if (humanChoice == null) return null;
                if (humanChoice  != TrumpChoice.SKIP) {
                    trumpTeam = players.get(i).getTeam();
                    finalChoice = humanChoice;
                    break;
                }
            } else {
                finalChoice = handleComputerPlayerChoice(currentPlayer, i);
                if (finalChoice != null) {
                    trumpTeam = players.get(i).getTeam();
                    break; // Break if a choice (other than skip) is made
                }
            }
    
            currentIndex = (currentIndex + 1) % players.size();
            // Skip is handled implicitly by the continue statement in handleHumanPlayerChoice.
        }
    
        return finalChoice != null ? finalChoice.getSuit() : null;
    }
    
    // Method to handle human player's choice of trump suit
    private TrumpChoice handleHumanPlayerChoice(HumanPlayer player, int round) {
        TrumpChoice humanChoice = player.getTrumpChoice(); 
        
        if (humanChoice == null) return null;
        
        player.setWaiting(true);
        
        if (humanChoice == TrumpChoice.SKIP) {
            if (round == 3) {
                System.out.println(player.getName() + " must pick!");
                return null;
            }
            System.out.println(player.getName() + " skipped.");
        } else {
            System.out.println(player.getName() + " chose " + humanChoice);
        }
        
        return humanChoice;
    }
    
    // Method to handle computer player's choice of trump suit
    private TrumpChoice handleComputerPlayerChoice(Player player, int round) {
        if (round == 3) {
            System.out.println(player.getName() + " MUST choose a trump suit:");
        }
    
        TrumpChoice playerChoice = player.chooseTrumpOrSkip(round);
        player.setWaiting(true);
    
        if (playerChoice != TrumpChoice.SKIP) {
            System.out.println(player.getName() + " chose " + playerChoice);
            return playerChoice;
        } else {
            System.out.println(player.getName() + " skipped.");
            return null;
        }
    }
    
    
    // Sort all players' hands by suit and rank
    public static void sortAllPlayersHands(List<Player> players) {
        for (Player player : players) {
            player.getHand().sortCards(); // Sort each player's hand in place
        }
    }

    public void setTrumpSuit(int choice) {
        this.trumpSuit = Card.Suit.values()[choice];
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setZvanjeWin(ZvanjeResult zvanjeWin) {
        this.zvanjeWin = zvanjeWin;
    }

    public void setWinTreshold(int winTreshold) {
        this.winTreshold = winTreshold;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setDealerIndex(int dealerIndex) {
        this.dealerIndex = dealerIndex;
    }

    public void setRoundStarterIndex(int roundStarterIndex) {
        this.roundStarterIndex = roundStarterIndex;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public void setTeamPassed(boolean teamPassed) {
        this.teamPassed = teamPassed;
    }

    public void setMidRound(boolean midRound) {
        this.midRound = midRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public void setDifficulty(Match.Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public List<Player> getPlayers() {
        return players;
    }
    
    public Player getPlayer(int index) {
        return players.get(index);
    }

    public ZvanjeResult getZvanjeWin() {
        return zvanjeWin;
    }

    public List<ZvanjeResult> getZvanjeResults() {
        return zvanjeResults;
    }

    public Player getZvanjeWinner() {
        return zvanjeWin.getPlayer();
    }

    public int getZvanjePoints() {
        return zvanjeWin.getTotalPoints();
    }

    public List<Card> getZvanjeCards() {
        return zvanjeWin.getCardsOfZvanje();
    }

    public String getZvanjeTypes() {
        return zvanjeWin.getZvanjeTypes().toString();
    }

    public int getWinTreshold() {
        return winTreshold;
    }

    public Deck getDeck() {
        return deck;
    }

    public Card.Suit getTrumpSuit() {
        return trumpSuit;
    }

    public int getDealerIndex() {
        return dealerIndex;
    }

    public Player getDealer() {
        return players.get(dealerIndex);
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public Team getTrumpTeam() {
        return trumpTeam;
    }

    public String teamPassed() {
        return teamPassed ? "YES" : "NO";
    }

    public Match.Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public Game clone() throws CloneNotSupportedException {
        // Create shallow copy of Game instance
        Game clonedGame = (Game) super.clone();

        clonedGame.difficulty = (difficulty != null) ? difficulty : null;

        // Deep clone teams
        clonedGame.team1 = (team1 != null) ? team1.clone() : null;
        clonedGame.team2 = (team2 != null) ? team2.clone() : null;

        // Deep clone players list
        clonedGame.players = this.players != null ? new ArrayList<>(this.players) : new ArrayList<>();

        // Deep clone deck (assuming Deck has a clone method)
        clonedGame.deck = (deck != null) ? deck.clone() : null;

        // Deep clone zvanjeWin (assuming ZvanjeResult has a clone method)
        clonedGame.zvanjeWin = (zvanjeWin != null) ? zvanjeWin.clone() : null;

        // Deep clone currentRound (assuming Round has a clone method)
        clonedGame.currentRound = (currentRound != null) ? currentRound.clone() : null;
        if (clonedGame.currentRound != null) {
            // Update the cloned round with the cloned players and trump suit
            clonedGame.currentRound.setStartingPlayerIndex(roundStarterIndex);
            clonedGame.currentRound.setTrumpSuit(trumpSuit);
            clonedGame.currentRound.setPlayers(clonedGame.players);
            clonedGame.currentRound.setDifficulty(difficulty); 
        }

        // Copy primitive fields and enumerations (here they are immutable, so direct assignment is fine)
        clonedGame.dealerIndex = this.dealerIndex;
        clonedGame.roundStarterIndex = this.roundStarterIndex; 
        clonedGame.roundCount = this.roundCount;
        clonedGame.winTreshold = this.winTreshold;
        clonedGame.trumpSuit = this.trumpSuit; // Enums are immutable, so this is directly referenced
        clonedGame.teamPassed = this.teamPassed;
        clonedGame.midRound = this.midRound;
        clonedGame.trumpTeam = this.trumpTeam;

        return clonedGame;
    }

}
