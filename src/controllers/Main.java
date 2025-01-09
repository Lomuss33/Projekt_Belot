//   ,d                          ,d     
//   88                          88     
// MM88MMM ,adPPYba, ,adPPYba, MM88MMM  
//   88   a8P_____88 I8[    ""   88     
//   88   8PP"""""""  `"Y8ba,    88     
//   88,  "8b,   ,aa aa    ]8I   88,    
//   "Y888 `"Ybbd8"' `"YbbdP"'   "Y888  
//
                                     
package controllers;

import controllers.Game.Difficulty;

/* ------------------------------------ x ----------------------------------- */
public class Main {
    public static void main(String[] args) {

        Game game = new Game();
        game.chooseDifficulty(Difficulty.EASY);
        game.startGame();
        
    }  
}
/* ------------------------------------ x ----------------------------------- */