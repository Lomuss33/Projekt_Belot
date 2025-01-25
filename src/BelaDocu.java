Clerk.clear();
Clerk.markdown(
    Text.fillOut(
"""
# Belot Kartenspiel im Match-Format

Im Spiel Bela Belot treten zwei Teams in einem 2-gegen-2-Format gegeneinander an, wobei jeder Spieler strategische Entscheidungen trifft, um Punkte zu sammeln. 
Die Spielmechanik wird über die Klasse Match.java gesteuert, die verschiedene Phasen wie Spielstart, Trumpfwahl und Runden spielt. 
Zunächst wählen die Spieler eine Trumpfkarte, gefolgt von der Anzeige der Zvanje, bevor die Runden beginnen.
Punkte werden in Echtzeit verfolgt, und ein Team gewinnt, wenn es als erstes 1001 Punkte erreicht. 
Das Spiel bietet außerdem die Möglichkeit, durch eine Rückgängig-Funktion Züge zu revidieren und damit strategische Optionen zu berücksichtigen.

# Steuerung - JShell

Die Interaktion mit dem Spiel erfolgt über die JShell, wo Benutzer einfache Befehle eingeben, um die Match Instanz zu steuern. 
Die wesentlichen Kommandos sind:

Befehl | Bedeutung
-------|----------
`play()` | Fortsetzung des Spiels durch Wechsel zwischen den Phasen.
`startGame()` | Beginnt das Spiel und initiert die erste Runde.
`pickTrump(int x)` | Wählt die Trumpfkarte, wobei `x` die 선택te Option darstellt.
`startRound()` | Beginn der Runde, nachdem die Trumpfkarte gewählt wurde.
`pickCard(int x)` | Spielt eine Karte aus der Hand, wobei `x` die Position der Karte angibt.
`endGame()` | Beendet die aktuelle Runde und bereitet die Punktevergabe vor.
`endMatch()` | Beendet das gesamte Match und zeigt die Endergebnisse an.

# Kartenspiel - die Logik

Um das Spiel und die Java-Logik zu verstehen, spiele ein Spiel mit mir:

### Beispiel Spielablauf

1. **Match-Erstellung**: 

```java
Match match = new Match();
```

Der Konstruktor der Klasse Match (`models.Match.java`) initialisiert eine neue Instanz mit Standardwerten. 
Er setzt den `gameCounter` auf 0, legt den `dealerIndex` auf 3 fest 
(was bedeutet, dass der HumanPlayer das Spiel beginnt), 
und weist den anfänglichen MatchPhase den Wert START zu. 
Außerdem werden alle boolesche Phase-Flags 
(`startGame`, `endGame`, `startRound` und `endMatch`) auf false gesetzt. 

2. **Einstellungen**:
- Eigenen Namen einstellen:
```java
match.setMyName(String playerName);
```	

- Namen der Spieler einstellen:
```java
match.setPlayerNames(String teamMate, String enemyMate1, String enemyMate2);
```

- Namen der Teams einstellen:
```java
match.setTeamNames(String myTeamName, String enemyTeamName);
```

- Spielregeln (Difficulty) einstellen:
```java
match.setDifficulty(String learn | normal | pro);
```

Difficulty | Bedeutung
-------|----------
`LEARN` | Ermöglicht es, das Spiel zu lernen, wo es keine Begrenzung der Ruckgängig-Funktion gibt und die (Learn-)Bots ohne Betrachtung des Spieles die Wahl treffen, und zwar auf Züfallig.
`NORMAL` | Wählt Karten zufällig aus spielbaren Optionen und hat eine einfache Logik für die Trumpfwahl, die lediglich die Gesamtstärke der Farben berücksichtigt.
`PRO` | Nutzt eine strategische Analyse, um die beste Karte basierend auf Gewinnchancen auszuwählen, und wählt die Trumpffarbe gezielt, basierend auf komplexeren Kriterien wie der Kombination von Karten und weiteren strategischen Überlegungen.

3. **Spielbeginn**: 

```java
match.play();
```

Der Spielbeginn wird durch den Aufruf der `play()`-Funktion initiiert, 
nachdem die Spielkonfiguration abgeschlossen ist. 
Diese Funktion bringt das Spiel in die START-Phase. 
Sollte die Konfiguration unvollständig sein, 
werden standardmäßige (Default-)Werte für die nicht konfigurierten Parameter verwendet, 
um sicherzustellen, dass das Spiel starten kann und für einen schnellen Start.

```java
public Difficulty customDifficulty = Difficulty.LEARN; 
public String customTeam1Name = "Team 1";
public String customPlayerName = "You";
public String customTeamMate = "Teammate"; 
public String customEnemy1 = "Rival_1"; 
public String customEnemy2 = "Rival_2"; 
public String customTeam2Name = "Team 2";
```

4. **Phasenwechsel**: 
   - Wenn `play()` aufgerufen wird, was nach jeder Aktion des Spielers erfolgt,
    wechselt das Spiel durch die verschiedenen Phasen:

    Ablauf | Phase | Bedeutung | Aktion in der Phase
    ------|------|-----------|---------
    1 | **START** | Übernimmt die Einstellungen und bereitet das Spiel vor. | `match.startGame()`
    2 | **CHOOSING_TRUMP** | Die Spieler wählen die Trumpfkarte. | `match.pickTrump(int x)`
    3 |**SHOW_ZVANJE** | Der Zvanje wird angezeigt und dessen Punkte ausgewertet. | `match.startRound()`
    4 |**PLAYING_ROUNDS** | Die Runden werden gespielt, wobei jeder Spieler Karten ausspielen kann. | `match.pickCard(int x`
    5 |**END_OF_GAME** | Am Ende jeder Runde werden die Punkte vergeben und geprüft, ob ein Team das Spiel gewonnen hat. | `match.endGame()`
    6 |**END_OF_MATCH** | Das gesamte Match wird beendet, und die endgültigen Ergebnisse werden angezeigt. | `match.endMatch()`







```java
int x = 1;
```

""", Map.of("match", Text.cutOut("./logo.java", "// Match constructor"),
"tree", Text.cutOut("./logo.java", "// tree"))));

// myFirstTurtle