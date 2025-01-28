### Name: Lovro Music | Matrikelnummer: 5517961

# Belot: A Pure Java Implementation with JShell Control

---

## Setup

The interaction with the Belot game is done through **JShell** in the Command Prompt (ensure to use Command Prompt, NOT PowerShell). The following steps provide detailed instructions for setting up and running the game.

## How to Run

### Step-by-Step Instructions:

1. **Open Command Prompt in the Project Directory**  
   Navigate to the source code directory:  
   ```shell
   cd src
   ```

2. **Set Console Encoding to UTF-8**  
   Ensure the console is using UTF-8 encoding for proper character rendering:  
   ```shell
   chcp 65001
   ```

3. **Compile the Project Code**  
   Compile the Java files into the `out` directory:  
   ```shell
   javac -d out models/*.java controllers/*.java ai/*.java
   ```

4. **Launch JShell with Required Options**  
   Start **JShell** in preview mode with the `out` directory as the class path:  
   ```shell
   jshell --class-path out --enable-preview
   ```

5. **Load the `run.jsh` File**  
   Open the `run.jsh` file in JShell to load the game and activate **lvp**:  
   ```shell
   /open run.jsh
   ```

6. **Start the Game**  
   Follow one of the options below:

   - Open the code documentation:
     ```shell
     /o codeDocu.java
     x.play();
     ```
   
   - Create a new `Match` instance:  
     ```shell
     Match match = new Match();
     match.play();
     ```
   
   - Use the `TurtlePlay` instance for graphical simulation (requires a `Turtle`):  
     ```shell
     Turtle turtle = new Turtle(600, 400);
     TurtlePlay x = new TurtlePlay(turtle);
     x.play();
     ```

---

## Game Commands

The following table lists the commands available for interacting with the Belot game via JShell:

| **Command**        | **Description**                                                                 |
|---------------------|---------------------------------------------------------------------------------|
| `play()`           | Starts the match by transitioning to the first/next phase.                         |
| `startGame()`      | Begins the game and initiates the first game.                                 |
| `pickTrump(int x)` | Selects the trump card, where `x` is the chosen option.                        |
| `startRound()`     | Starts a round after the trump card has been selected.                        |
| `pickCard(int x)`  | Plays a card from the hand, where `x` is the position of the card.             |
| `endGame()`        | Ends the current round and proceeds to score calculation.                     |
| `endMatch()`       | Ends the entire match and displays the final results.                         |
| `goBack()`         | Rolls back to a previous game state (Undo functionality).                     |

---

## Notes

1. Ensure you are using **Command Prompt**, not PowerShell, for proper UTF-8 rendering.
2. If you experience improper character rendering, verify that UTF-8 is properly configured using  
   ```shell
   chcp 65001
   ```
3. The `lvp` mode is activated under `http://localhost:50001` once `run.jsh` is opened. This step is essential for enabling proper console output and display.

### In case of server isuse: Free Port 50001 in Windows and reload

1. **Find the process using port 50001**:  
   ```bash
   netstat -ano | findstr :50001
   ```
   - Note the **PID** from the output.

2. **Kill the process**:  
   ```bash
   taskkill /PID [PID_NUMBER] /F
   ```
   - Replace `[PID_NUMBER]` with the PID from step 1. Done! 🎉 

---
## Summary

The `Match` class in Java forms the foundation for a Belot (Bela) game simulation. It manages the gameplay using a **state machine** that transitions through several game phases:

1. **Start**
2. **Trump Selection**
3. **Zvanje**
4. **Playing Rounds**
5. **Game End**
6. **Match End**

Each phase is handled by methods of the `Match` class, which in turn rely on functionality from the `Game` class (for game logic) and the `Round` class (for individual rounds). 

Players interact via **JShell** by calling methods like `pickTrump()` or `startRound()`. The game pauses and waits for user input whenever provided commands are incomplete. After a round or game is completed, the game state is updated, and the next phase is initiated. This cycle continues until one team reaches the required points to win the match.

The implementation also includes a **snapshot mechanism** to allow saving the state of the game and rolling back to previous states if necessary.
---
