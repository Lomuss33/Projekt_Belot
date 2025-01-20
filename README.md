# Card Game Project: Belot

A Java-based implementation of the classic Belot card game. This project features AI players with varying difficulty levels, undo functionality, Zvanje scoring, and more, all designed for a fun and strategic gameplay experience.

---

## Table of Contents
- [Features](#features)
- [How to Play](#how-to-play)
- [Installation](#installation)
- [Game Overview](#game-overview)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## Features
- **AI Players**: Choose from Easy, Normal, or Hard AI opponents.
- **Zvanje Scoring**: Detect and score advanced combinations automatically.
- **Undo Feature**: Revert your moves to fix mistakes or replay strategies.
- **Dynamic Trump Selection**: Players dynamically choose the trump suit at the beginning of the game.
- **Team-Based Gameplay**: Play in teams and compete to score 1001 points to win the match.
- **Customizable Difficulty**: Set game difficulty levels for personalized challenges.
- **Real-Time Score Tracking**: Monitor team scores throughout the game.
- **Comprehensive Game Logs**: Review past rounds and moves for strategy improvement.

---

## How to Play
1. Start the game by running the `Main` class.
2. The game will initialize with:
   - 1 Human Player.
   - 3 AI Players (based on the selected difficulty).
   - A shuffled deck of cards.
3. **Game Phases**:
   - **Card Dealing**: Players are dealt 6 cards in 3 rounds.
   - **Trump Selection**: Players choose a trump suit or skip.
   - **Gameplay**: Play cards in rounds, attempting to win tricks.
   - **Scoring**: Earn points through tricks and Zvanje combinations.
4. Play continues for 8 rounds or until a team scores 1001 points.
5. The team with the highest score wins the match!

### Additional Commands
- **Drop Mid-Game**: Use `/open run.js` to terminate the current game.
- **Trump Selection**: Use `pickTrump(0-5)` to select the trump suit (0 to skip unless last, 1-4 for suit options).
- **Card Selection**: Use `pickCard(0-8)` to play a card from your hand, depending on the number of cards.

### Example to Start a New Game
```java
import models.*;
import controllers.*;
import services.*;
import ai.*;

Match y = new Match(Game.Difficulty.EASY);
y.play();
```

---

## Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   ```
2. Navigate to the project directory:
   ```bash
   cd your-repo-name
   ```
3. Open the project in your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse, VSCode).
4. Compile the project:
   ```bash
   javac -d out models/*.java controllers/*.java services/*.java ai/*.java
   ```
5. Run the `Main` class:
   ```bash
   jshell --class-path out
   ```

---

## Game Overview
### Core Classes
- **Game**: Manages the overall game flow.
- **RoundManager**: Handles logic for individual rounds.
- **GameInitializer**: Sets up players, teams, and initial game state.
- **ZvanjeService**: Detects and scores Zvanje combinations.
- **CardService**: Manages card-related utilities like playable cards and trump values.

### AI Levels
- **Easy AI**: Basic random card selection.
- **Normal AI**: Moderate logic for card and trump choices.
- **Hard AI**: Advanced strategy for competitive gameplay.

---

## Project Structure
```plaintext
├── src
│   ├── controllers
│   │   ├── Game.java
│   │   ├── Main.java
│   │   ├── Match.java
│   │   ├── RoundManager.java
│   ├── models
│   │   ├── Card.java
│   │   ├── Deck.java
│   │   ├── Hand.java
│   │   ├── Player.java
│   │   ├── Team.java
│   ├── services
│   │   ├── CardService.java
│   │   ├── GameInitializer.java
│   │   ├── GameStateManager.java
│   │   ├── ScoringService.java
│   │   ├── ZvanjeService.java
│   ├── ai
│   │   ├── EasyAI.java
│   │   ├── NormalAI.java
│   │   ├── HardAI.java

├── README.md
├── LICENSE
```

---

## Contributing
Contributions are welcome! To contribute:

1. Fork this repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a Pull Request on GitHub.

---

## License
This project is licensed under the [MIT License](LICENSE).

---

## Acknowledgments
Special thanks to everyone who contributed to the development of this project and the open-source community for inspiring ideas.

---

Enjoy playing Belot! 😊

