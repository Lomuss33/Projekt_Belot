// ░▀▄░░▄▀
// ▄▄▄██▄▄▄▄▄░▀█▀▐░▌
// █▒░▒░▒░█▀█░░█░▐░▌
// █░▒░▒░▒█▀█░░█░░█
// █▄▄▄▄▄▄███══════


package controllers;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("------------------------------------ x -----------------------------------");
        Match match = new Match(Game.Difficulty.EASY);
        match.startMatch();
        System.out.println("------------------------------------ x -----------------------------------");

    }  

}
