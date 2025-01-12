//   ,d                          ,d     
//   88                          88     
// MM88MMM ,adPPYba, ,adPPYba, MM88MMM  
//   88   a8P_____88 I8[    ""   88     
//   88   8PP"""""""  `"Y8ba,    88     
//   88,  "8b,   ,aa aa    ]8I   88,    
//   "Y888 `"Ybbd8"' `"YbbdP"'   "Y888  


package controllers;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("------------------------------------ x -----------------------------------");
        Match match = new Match(Game.Difficulty.EASY);
        match.startMatch();
        System.out.println("------------------------------------ x -----------------------------------");

    }  

}
