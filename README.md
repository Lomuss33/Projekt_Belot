# Projekt_Belot

A Java implementation of the Croatian card game Belot (Bela) featuring a 2v2 scenario with 1 player and 3 AI opponents.

## Overview

Belot is a popular trick-taking card game played primarily in Croatia and surrounding regions. This implementation allows you to play against AI opponents in a traditional 2v2 team setting.

## Running the Project

1. Navigate to the project directory:
   ```bash
   cd Projekt_Belot/src/main/java/com/projektbelot
   ```

2. Start JShell with assertions and preview features enabled:
   ```bash
   jshell -R-ea --enable-preview
   ```

3. Load the main project file:
   ```java
   /o lvp.java
   ```

4. Load additional server files as needed:
   ```java
   /o [filename.java]
   ```


## How to Run the Belot Project in VS Code

1. Open the **Run and Debug Panel** (`Ctrl+Shift+D` or `Cmd+Shift+D` on Mac).
2. Select a configuration:
   - **Main**: Runs the `controllers.Main` class.
   - **Current File**: Runs the open Java file.
3. Click the green play button or press `F5` to run.
4. View the output in the **Debug Console**.

To test, add this to `Main.java`:
```java
Card card = new Card(Suit.HEARTS, Rank.ACE);
card.calculateValue(Suit.HEARTS);
System.out.println(card);
```
## Server Management

If you need to restart the server, follow these steps:

1. Find the server process:
   ```bash
   netstat -ano | findstr :50001
   ```

2. Locate the Process ID (PID) from the output:
   ```
   TCP    127.0.0.1:50001        0.0.0.0:0              LISTENING         22716
   ```

3. Kill the process using the identified PID:
   ```bash
   taskkill /PID [PID_NUMBER] /F
   ```

## Contributing

Feel free to submit issues and enhancement requests!

## License

[Add your license information here]