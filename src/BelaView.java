///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// TURTLE PLAY
/// 
Clerk.clear();

Clerk.markdown(
    Text.fillOut(
"""

# BELA TURTLE PLAY
> Falls das Turtle-Objekt nicht sichtbar ist, bitte die file erneut starten.
---
"""));

Turtle turtle = new Turtle(600, 400);
TurtlePlay x = new TurtlePlay(turtle);

Clerk.markdown(
    Text.fillOut(
"""
### Nutzen sie dieses Fenster und die ` x ` Instanz. um ein eigenes Spiel im Jshell zu spielen.

---
---
# Funktionsversprechen: Belot (Bela) Kartenspiel
---

## Szenario 1 – Spielerstellung und Ablauf 
---

### **Einen neuen Match starten**

Um einen neuen Match zu starten, erstellen Sie einfach eine Instanz der `Match`-Klasse:

```java
Match m = new Match();
```

### **Einen neuen Match mit TurtleView starten**

Falls Sie eine TurtleView für die grafische Darstellung des Matches nutzen möchten, 
initialisieren Sie zuerst eine `Turtle`-Instanz mit den gewünschten Abmessungen (Breite und Höhe). 
Anschließend übergeben Sie diese an ein `TurtlePlay`-Objekt der die Match-Instanz erstellt und Logik steuert:

```java
Turtle turtle = new Turtle(600, 400); // Erstellt ein Turtle-Fenster mit 600x400 Pixeln
TurtlePlay x = new TurtlePlay(turtle); // Verknüpft die Turtle mit TurtlePlay
```


### **Match Klasse Übersicht**
Der `Match`-Konstruktor und die zugehörigen Variablen steuern den gesamten Zustand und Ablauf eines Matches.

#### **Wichtige Variablen**
- **Spielzustand und Phasenmanagement**:
  - `MatchPhase currentPhase`: Verfolgt die aktuelle Phase des Matches (z. B. START, PLAYING_ROUNDS).
  - `Stack<Match> snapshots`: Ermöglicht das Speichern von Spielzuständen (z. B. für Undo-Funktionalität).
- **Teams und Spieler**:
  - `Team team1, team2`: Repräsentiert die beiden Teams.
  - `List<Player> players`: Enthält alle Spieler des Matches.
  - `HumanPlayer me`: Referenz auf den Menschen als Spieler.
- **Spielstatus** (Lifecycle Flags):
  - `startGame`, `endGame`, `startRound`, `endMatch`: Flags, die den Fortschritt des Spiels markieren.
- **Anpassungen**:
  - Benutzerspezifische Namen (für Teams und Spieler), sowie Schwierigkeitsstufe (`customDifficulty`).

#### **Konstruktor**
Der Konstruktor initialisiert die Hauptparameter:
- `currentPhase`: Startet das Spiel in der `START`-Phase.
- `gameCounter`: Zählt die Anzahl gespielter Spiele (startet bei 0).
- `dealerIndex`: Setzt den Dealer so das der Spieler anfängt (Index 3).
- Alle Flags (`startGame`, `endGame`, etc.) werden auf `false` gesetzt.


### **Kommandozeilen der Anpassungen**:
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
### Die Match Klasse mit Konstruktor und Einstellungen:

```java
${Match}
``` 

---

### Die Methode `play()`
`play()` steuert den Ablauf des Spiels durch die verschiedenen Phasen. Sie nutzt eine rekursive Zustandsmaschine für einen flüssigen Spielfluss und berücksichtigt Unterbrechungen.

#### Funktionsweise:
1. **Phasen-Handling**: Je nach aktueller Phase (`MatchPhase`) wird die spezifische Logik ausgeführt (`runPhase()`).
2. **Zwischenprüfung (`runPhase`)**: Jeder Phase wird innerhalb der eigenen `runPhase` Methode abgeschlossen, 
die `true` zurückgibt, wenn die Phase beendet ist. Ist dies nicht der Fall, wird der Spielfluss unterbrochen. 
Nach Abschluss einer Phase wird zur nächsten Phase gewechselt und `play()` erneut aufgerufen
   ```java
       public void play() {
        switch (currentPhase) {
            case START -> {
                if (!runStartPhase()) { // Run Phase and check if it's finished
                    return; // Exit the play method
                }
                this.play(); // Proceed to the next phase
            }
    ...
   ```
3. **Rekursion für Phase-Wechsel**: :
   ```java
   currentPhase = MatchPhase.END_OF_MATCH;
   this.play();
   ```
4. **Flags zur Steuerung**: Spieleraktionen (z. B. `pickCard()`) setzen bestimmte Flags. Beim nächsten Durchlauf merkt dies `play()` und erkennt, dass die Voraussetzung für den Phasenabschluss erfüllt wurde.

#### Unterbrechung & Fortsetzung:
Während `play()` pausiert (z. B. bis ein Spieler eine Karte spielt), werden Statusvariablen wie der aktuelle Spieler oder die Trumpffarbe **persistiert**. Mit gesetzten Flags kann `play()` an derselben Stelle wieder fortgesetzt werden.

#### Thread-Mechanismus:
- **Unterbrechbarkeit**: `play()` kann pausieren, ohne den Thread zu stoppen.
- **Fortsetzung**: Aktionen von Spielern triggern die Weiterführung der Methode.
- **Thread-Sicherheit**: Synchronisation sorgt für konsistente Zustandsverwaltung.

---

##  Szenario 2 – [ Zug machen / letzten Zug zürucknehmen ] 
---

Ablauf | Phase | Bedeutung | Aktion in der Phase
------|------|-----------|---------
1 | **START** | Übernimmt die Einstellungen und bereitet das Spiel vor. | `match.startGame()`
2 | **CHOOSING_TRUMP** | Die Spieler wählen die Trumpffarbe (Adut). | `match.pickTrump(int x)`
3 |**SHOW_ZVANJE** | Der Zvanje wird angezeigt und dessen Punkte ausgewertet. | `match.startRound()` & `match.goBack()`
4 |**PLAYING_ROUNDS** | Die Runden werden gespielt, wobei jeder Spieler Karten ausspielen kann. | `match.pickCard(int x)` & `match.goBack()`
5 |**END_OF_GAME** | Am Ende jeder Runde werden die Punkte vergeben und geprüft, ob ein Team das Spiel gewonnen hat. | `match.endGame()` & `match.goBack()`
6 |**END_OF_MATCH** | Das gesamte Match wird beendet, und die endgültigen Ergebnisse werden angezeigt. | `match.endMatch()`


### Zug machen / Spielablauf

1. **Match starten `x.play()` zum START** 

![nach ausfuhrung von play()](./DocuImgs/img1START.png)

2. **Game 1 starten `x.startGame()` zum CHOOSING_TRUMP** 

![nach ausfuhrung von startGame()](./DocuImgs/img2CHOOSING_TRUMP.png)

3. **Trumpf wählen `x.pickTrump(0)` zum SHOW_ZVANJE**  <br>
0 = Skip, 1 = Herz (♥), 2 = Karo (♦), 3 = Kreuz(♣), 4 = Pik (♠)

![nach ausfuhrung von pickTrump(0)](./DocuImgs/img2CHOOSING_TRUMP.png)

4. **Runde starten `x.startRound()` zum PLAYING_ROUNDS**

![nach ausfuhrung von startRound()](./DocuImgs/img3SHOW_ZVANJE.png)

5. **Karte ausspielen `x.pickCard(3)` zum END_OF_GAME** (0 = erste Karte, 1 = zweite Karte, ...)

![nach ausfuhrung von pickCard(0)](./DocuImgs/img4PLAYING_ROUNDS.png)

6. **Spiel beenden `x.endGame()` zum END_OF_GAME**

![nach ausfuhrung von endGame()](./DocuImgs/img5END_OF_GAME.png)

7. **Match beenden `x.endMatch()` zum END_OF_MATCH**

![nach ausfuhrung von endMatch()](./DocuImgs/img6END_OF_MATCH.png)

---

### Letzten Zug zürucknehmen / Reset-Funktionen

- **`saveSnapshot()`**

Diese Methode speichert den aktuellen Spielzustand, indem sie eine tiefe Kopie des `Match`-Objekts erstellt und diese auf einen Stack (`snapshots`) legt.  Die tiefe Kopie ist entscheidend, um sicherzustellen, dass Änderungen an der kopierten Spielsituation die Originalversion nicht beeinflussen. Diese tiefe Kopie wird nur durchgeführt, wenn der Schwierigkeitsgrad des Spiels nicht auf "PRO" eingestellt ist.
```java
${saveSnapshot}
```

- **`goBack()`**

Diese Methode ermöglicht es einem Spieler, seinen letzten Zug rückgängig zu machen. Sie prüft zuerst, ob das Rückgängigmachen erlaubt ist (Schwierigkeit ist nicht "PRO" und das Spiel befindet sich nicht am Anfang oder Ende). Wenn erlaubt und ein vorheriger Zustand existiert (`snapshots` ist nicht leer), ruft sie den letzten Spielzustand vom `snapshots`-Stack ab. Der abgerufene Zustand wird dann wiederhergestellt, wodurch das Spiel effektiv auf den Punkt vor dem letzten Zug zurückgesetzt wird.  Anschließend wird `play()` aufgerufen, um fortzufahren.
```java
${goBack}
``` 

- **`pickCard()`**

Diese Methode verarbeitet den Zug eines Spielers (die Auswahl einer Karte). Bevor der Zug verarbeitet wird, prüft sie die Schwierigkeitseinstellung. Wenn sie nicht "PRO" ist, ruft sie `saveSnapshot()` auf, um den Spielzustand *vor* dem Ausspielen der Karte zu speichern. Dann wird die gewählte Karte verarbeitet und die `play()`-Methode bringt das Spiel in die nächste Phase.
```java
${pickCard}
```

---
##  Szenario 3 – [ Punkteverfolgung ]

### **Szenario 3 – Punkteverfolgung**

#### **1. Akkumulation von "Smalls":**

"Smalls"-Punkte werden nur während der Phase PLAYING_ROUNDS gesammelt. 
Jedes Mal, wenn ein Team eine Runde gewinnt, 
erhöht sich dessen "Smalls"-Punktestand um den Wert der Karten die sie gewonnen haben.
Die Verwaltung der "Smalls"-Punkte erfolgt über die `Team`-Klasse mithilfe von Methoden wie `addWonCardsAsPoints()`. 
Diese Methode erhöht die Punktzahl des Teams basierend auf den Kartenwerten. 

```java
    public void addWonCardsAsPoints(List<Card> addedCards) {
        wonCards.addAll(addedCards);
        for (Card card : addedCards) {
            smalls += card.getValue(); // Add card values to smalls
        }
    }
```
---

#### **2. Berechnung der Zvanje-Punkte:**
 
Zvanje-Punkte werden *einmal* berechnet, nachdem die Trumpffarbe ausgewählt wurde 
und die Spielere alle ihre Karten haben, 
aber bevor die Runden beginnen. Diese Berechnung erfolgt, 
um zusätzliche Punkte basierend auf speziellen Kartenkombinationen zu vergeben.

Die `ZvanjeService`-Klasse spielt hier eine zentrale Rolle. 
Mit `ZvanjeService.detectPlayerZvanje()` werden die Karten jedes Spielers überprüft, 
um gültige Zvanje-Kombinationen (z. B. Sequenzen, spezielle Kartenpaare) zu identifizieren. 
Die Punkte werden dann entsprechend den Regeln zugewiesen.

```java
${detectPlayerZvanje}
``` 

---

#### **3. Berechnung der Gesamt-Punkte:**

Die Gesamt-Punkte eines Teams werden am Ende jeder Runde und zusätzlich am Ende des Spiels berechnet. 
Diese ermöglichen es, den aktuellen Siegesstand oder den endgültigen Gewinner zu ermitteln.

Mithilfe von `Game.alculateGamePoints()` werden die Gesamtergebnisse eines Teams berechnet. 
Dabei werden die "Smalls"-Punkte aus der `Team`-Klasse und die zuvor bestimmten Zvanje-Punkte kombiniert. 
Somit ergibt sich die Gesamtpunktzahl eines Teams. Jeder Team hat auch die Variable "int awardedBigs" 
für eine temporäre Speicherung von im Game gewonnen Punkten, die zu "Biggs" addiert werden.

Die Gewinnbedingung einer Game wird ermittelt, indem geprüft wird, 
ob ein Team die Gesamtpunktzahl erreicht hat, die den `winThreshold` überschreitet. 
Dieser Schwellenwert wird mit der Methode `calculateWinThreshold()` berechnet und entspricht mindestens 50 % plus 1 Punkt.

```java
private void awardGameVictory() {
    Team dealerTeam = getTrumpTeam();
    Team otherTeam = (dealerTeam == team1) ? team2 : team1;
    int dealerTeamPoints = calculateGamePoints(dealerTeam, zvanjeWin);
    int otherTeamPoints = calculateGamePoints(otherTeam, zvanjeWin);

    if (dealerTeamPoints >= winTreshold) {
        handleDealerTeamVictory(dealerTeam, otherTeam, dealerTeamPoints, otherTeamPoints);
    } else {
        handleOtherTeamVictory(dealerTeam, otherTeam, dealerTeamPoints, otherTeamPoints);
    }
}


// Method to calculate game points for a given team
private static int calculateGamePoints(Team team, ZvanjeResult zvanjeWin) {
    int zvanjePoints = (zvanjeWin != null && zvanjeWin.getWinningTeam() == team) ? zvanjeWin.getTotalPoints() : 0;
    return team.getSmalls() + zvanjePoints;
}
``` 


#### **4. Match Gewinner suche:**

Die Methode matchWinner prüft, ob eines der Teams die WINNING_SCORE erreicht hat und ermittelt den Sieger. 
Wenn beide Teams die Gewinnbedingung erfüllen, gewinnt das Team mit der höheren Punktzahl. 
Hat nur eines der Teams die WINNING_SCORE überschritten, wird dieses Team als Sieger zurückgegeben. 
Falls kein Team die erforderliche Punktzahl erreicht hat, gibt die Methode null zurück, was signalisiert, 
dass das Match noch nicht entschieden ist. Die Methode sorgt so für eine klare Entscheidungslogik, auch bei knappen Spielständen.

```java
private Team matchWinner() {
    // The match is over if either team reaches or exceeds the winning score
    if (team1.getBigs() >= WINNING_SCORE && team2.getBigs() >= WINNING_SCORE) {
        return team1.getBigs() > team2.getBigs() ? team1 : team2;
    } else if (team1.getBigs() >= WINNING_SCORE) {
        return team1;
    } else if (team2.getBigs() >= WINNING_SCORE) {
        return team2;
    }
    return null;
}
```

#### **Zusammenfassung:**
1. "Smalls"-Punkte werden schrittweise während der Runden gesammelt.  
2. Zvanje-Punkte werden einmalig nach Auswahl der Trumpffarbe berechnet.  
3. Die Gesamt-Punkte "Bigs" kombinieren die "Smalls" und Zvanje-Punkte, um den Game- und Matchstand zu ermitteln.  
4. Ein Team gewinnt den Game, wenn seine Gesamt-Punkte den errechneten `winThreshold` überschreiten (entspricht meist 50 % + 1 Punkt).
5. Nach erreichen der `WINNING_SCORE` wird der Match-Gewinner ermittelt.

---
## Scenario 4 – [ AI Mitspieler ]

Der KI-Spieler werden in einem Kartenspiel basierend auf einem gewählten Schwierigkeitsgrad erstellt werden. Die Methode `GameUtils.initializePlayers` nimmt einen `Difficulty`-Enum (LEARN, NORMAL, PRO) als Eingabe und instanziiert die entsprechende KI-Spieler-Klasse für jeden Schwierigkeitsgrad.

* **Erstellung:** Die `initializePlayers`-Methode erstellt eine Liste von `Player`-Objekten. Ein Spieler ist immer ein `HumanPlayer`. Die übrigen sind KI-Spieler, die basierend auf der angegebenen `Difficulty` instanziiert werden.

* **KI-Spieler-Typen:** Es existieren drei KI-Spieler-Typen: `AiPlayerLEARN`, `AiPlayerNORMAL` und `AiPlayerPRO`. Jeder ist eine Unterklasse der abstrakten `Player`-Klasse. Der Code zeigt nur ihre Instanziierung; die interne Logik, die ihre Spielstrategien bestimmt, ist in den bereitgestellten Codeausschnitten nicht enthalten.

* **Unterschiede:** Der Hauptunterschied zwischen den KI-Spielern liegt in ihren Entscheidungsprozessen innerhalb der Methoden `chooseCardToPlay`, `chooseTrumpOrSkip` und `callZvanje`. Diese Methoden sind in der `Player`-Klasse abstrakt, und ihre Implementierungen in jeder KI-Spieler-Klasse definieren ihre Spielstrategien. Von höheren Schwierigkeitsgraden (`NORMAL` und `PRO`) wird erwartet, dass sie ausgeklügeltere Algorithmen verwenden, die zu besserem Spielverhalten führen.

```java
${findPlayableCardIndexes}
``` 


## Scenario 5 – [ Schwierigkeitsgrad ]

### Schwierigkeitsmodi: LEARN, NORMAL, PRO

Die Schwierigkeitsmodi "LEARN", "NORMAL" und "PRO" unterscheiden sich in mehreren Aspekten:

### Punkteanzeige

* **LEARN:**  Biggs-, Smalls- und Zvanje-Punkte werden kontinuierlich angezeigt.
* **NORMAL:** Nur Biggs-Punkte werden angezeigt.
* **PRO:**  Keine Punkteanzeige während des Spiels; die Punkte werden erst am Ende gezeigt.

### Rückgängigmachen von Zügen

* **LEARN:** Spielübergreifendes Rückgängigmachen von Zügen ist möglich.
* **NORMAL:**  Rückgängigmachen von Zügen ist nur innerhalb eines einzelnen Spiels erlaubt.
* **PRO:**  Kein Rückgängigmachen von Zügen möglich.

### Zulässige Züge

* **LEARN & PRO:**  Es können beliebige Züge gespielt werden, auch unzulässige.
* **NORMAL:**  Nur zulässige Züge können gespielt werden.

### KI-Verhalten

* **LEARN:** Der KI-Bot spielt rein zufällig (random).
* **NORMAL:** Der KI-Bot berücksichtigt seine Handkarten und die bereits ausgespielten Karten.
* **PRO:** Der KI-Bot verwendet eine ausgefeilte Spielstrategie.

```java
${chooseTrumpOrSkip}
``` 

""", Map.of(
    "Match", Text.cutOut("./controllers/Match.java", "// Match"), 
    "pickCard", Text.cutOut("./controllers/Match.java", "// pickCard"),
    "goBack", Text.cutOut("./controllers/Match.java", "// goBack"), 
    "saveSnapshot", Text.cutOut("./controllers/Match.java", "// saveSnapshot"),
    "chooseTrumpOrSkip", Text.cutOut("./ai/AiPlayerPRO.java", "// chooseTrumpOrSkip"), 
    "findPlayableCardIndexes", Text.cutOut("./controllers/Round.java", "// findPlayableCardIndexes"), 
    "detectPlayerZvanje", Text.cutOut("./controllers/ZvanjeService.java", "// detectPlayerZvanje")
    )));
