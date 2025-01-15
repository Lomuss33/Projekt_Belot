// ░▀▄░░▄▀
// ▄▄▄██▄▄▄▄▄░▀█▀▐░▌
// █▒░▒░▒░█▀█░░█░▐░▌
// █░▒░▒░▒█▀█░░█░░█
// █▄▄▄▄▄▄███══════


package controllers;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("------------------------------------ x -----------------------------------");
        
        // Start the match
        // Create a new match with EASY difficulty
        Match match = new Match(Game.Difficulty.EASY);
        // Start the match
        match.startMatch();
        // 
        System.out.println("------------------------------------ x -----------------------------------");

    }  

}
