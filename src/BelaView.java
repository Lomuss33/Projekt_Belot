///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// TURTLE PLAY
/// 
Clerk.clear();

Clerk.markdown(
    Text.fillOut(
"""

# BELA TURTLE PLAY
> Falls das Turtle-Objekt nicht sichtbar ist, bitte die file erneut starten.
"""));

Turtle turtle = new Turtle(600, 400);
TurtlePlay x = new TurtlePlay(turtle);

Clerk.markdown(
    Text.fillOut(
"""
### Nutzen sie dieses Fenster und die ` x ` Instanz. um ein eigenes Spiel im Jshell zu spielen.

# Funktionsversprechen: Belot (Bela) Kartenspiel

---
## Szenario 1 – Spiel Start / Ansagen

Mit `x.play()` beginnt das Spiel im **START**-Modus, wo die ausgewählten Einstellungen initialisiert werden. Der Befehl `startGame()` startet das erste Spiel.  Der Ablauf durch die verschiedenen Spielphasen wird durch einen Zustandsautomaten gesteuert, der mit einem `enum MatchPhase` implementiert wurde.

- **START-Phase:** Jeder Spieler erhält 6 Karten und sieht nur seine eigene verdeckte Hand.
- **CHOOSING_TRUMP-Phase:** Die Spieler wählen ihre Trumpffarbe (Adut).
- Nach der Wahl erhalten alle Spieler 2 weitere Karten, womit das vollständige Blatt verteilt wird.
- **SHOW_ZVANJE-Phase:** Ansagen (Zvanje) werden angezeigt, und der Spieler bestätigt diese mit `startRound()`.
- **PLAYING_ROUNDS:** Die Runde beginnt, und Spieler können abwechselnd Karten ausspielen.

Der Übergang zwischen den Phasen erfolgt innerhalb der Zustandsmaschine, gesteuert durch die `MatchPhase`-Enumeration und entsprechende Methodenaufrufe.
 

1. **Spiel starten:** `x.play()`

![nach ausfuhrung von play()](./imgs/img1START.png)

2. **Game 1 starten :** `x.startGame()`

![nach ausfuhrung von startGame()](./imgs/img2CHOOSING_TRUMP.png)

3. **Trumpf wählen:** `x.pickTrump(0)` (0 = Herz, 1 = Karo, 2 = Pik, 3 = Kreuz)

![nach ausfuhrung von pickTrump(0)](./imgs/img2CHOOSING_TRUMP.png)

4. **Runde starten:** `x.startRound()`

![nach ausfuhrung von startRound()](./imgs/img3SHOW_ZVANJE.png)

5. **Karte ausspielen:** `x.pickCard(3)` (0 = erste Karte, 1 = zweite Karte, ...)

![nach ausfuhrung von pickCard(0)](./imgs/img4PLAYING_ROUNDS.png)

6. **Spiel beenden:** `x.endGame()`

![nach ausfuhrung von endGame()](./imgs/img5END_OF_GAME.png)

7. **Match beenden:** `x.endMatch()`

![nach ausfuhrung von endMatch()](./imgs/img6END_OF_MATCH.png)

---
##  Szenario 2 – [ Zug machen / letzten Zug zürucknehmen ] 

Okay, hier ist eine Beschreibung der Implementierung, aufgeteilt nach Methoden, sodass Sie den eigentlichen Code darunter einfügen können:


**1. `saveSnapshot()`**

Diese Methode speichert den aktuellen Spielzustand, indem sie eine tiefe Kopie des `Match`-Objekts erstellt und diese auf einen Stack (`snapshots`) legt.  Die tiefe Kopie ist entscheidend, um sicherzustellen, dass Änderungen an der kopierten Spielsituation die Originalversion nicht beeinflussen. Diese tiefe Kopie wird nur durchgeführt, wenn der Schwierigkeitsgrad des Spiels nicht auf "PRO" eingestellt ist.
```java
${saveSnapshot}
```

**2. `pickCard()`**

Diese Methode verarbeitet den Zug eines Spielers (die Auswahl einer Karte). Bevor der Zug verarbeitet wird, prüft sie die Schwierigkeitseinstellung. Wenn sie nicht "PRO" ist, ruft sie `saveSnapshot()` auf, um den Spielzustand *vor* dem Ausspielen der Karte zu speichern. Dann wird die gewählte Karte verarbeitet und die `play()`-Methode bringt das Spiel in die nächste Phase.
```java
${pickCard}
```

**3. `goBack()`**

Diese Methode ermöglicht es einem Spieler, seinen letzten Zug rückgängig zu machen. Sie prüft zuerst, ob das Rückgängigmachen erlaubt ist (Schwierigkeit ist nicht "PRO" und das Spiel befindet sich nicht am Anfang oder Ende). Wenn erlaubt und ein vorheriger Zustand existiert (`snapshots` ist nicht leer), ruft sie den letzten Spielzustand vom `snapshots`-Stack ab. Der abgerufene Zustand wird dann wiederhergestellt, wodurch das Spiel effektiv auf den Punkt vor dem letzten Zug zurückgesetzt wird.  Anschließend wird `play()` aufgerufen, um fortzufahren.
```java
${goBack}
``` 

---
##  Szenario 3 – [ Punkteverfolgung ]

**1. Akkumulation von "Smalls":**

* **Wann:** Die "Smalls"-Punkte werden während des gesamten Spiels gesammelt, wahrscheinlich innerhalb der `Round`-Klasse (die Sie nicht gezeigt haben). Jedes Mal, wenn ein Team eine Runde gewinnt, erhöht sich sein "Smalls"-Punktestand. Der genaue Mechanismus, wie viele Punkte pro gewonnenen Runde hinzugefügt werden, ist nicht im bereitgestellten Code enthalten und in der Implementierung der `Round`-Klasse verborgen.

* **Wie:** Die "Smalls"-Punkte werden von der `Team`-Klasse mithilfe von Methoden wie `addSmalls()` verwaltet (die Sie nicht gezeigt haben). Diese Methoden sind im gegebenen `GameUtils`-Code nicht sichtbar.


**2. Berechnung der Zvanje-Punkte:**

* **Wann:** Die Zvanje-Punkte werden *einmal* berechnet, nachdem die Trumpffarbe ausgewählt wurde, aber *bevor* die Runden beginnen. Diese Berechnung findet in der `Game`-Klasse (die Sie nicht gezeigt haben) statt, indem `ZvanjeService.detectPlayerZvanje()` für jeden Spieler aufgerufen wird.

* **Wie:** Die `ZvanjeService`-Klasse ermittelt die Zvanje-Punkte basierend auf den Karten in der Hand jedes Spielers und der Trumpffarbe. Die Methode `ZvanjeService.detectPlayerZvanje()` verwendet komplexe Regeln, um verschiedene Zvanje-Kombinationen zu identifizieren und ihnen entsprechend Punkte zuzuweisen.


**3. Berechnung der Gesamt-Punkte:**

* **Wann:** Die Gesamt-Punkte werden am Ende jeder Runde *und* am Ende des Spiels berechnet, um den Gewinner zu ermitteln.

* **Wie:** Die Gesamt-Punkte werden mit der Funktion `GameUtils.calculateGamePoints()` berechnet. Diese Funktion summiert die "Smalls" des Teams (erhalten von der `Team`-Klasse) und addiert die Zvanje-Punkte, falls das Team das Zvanje gewonnen hat.


**4. Gewinnbedingung:**

* **Wann:** Die Gewinnbedingung wird am Ende jeder Runde von `GameUtils.findGameWinner()` und nach dem Ende des Spiels überprüft.

* **Wie:** Die Gewinnbedingung wird ermittelt, indem geprüft wird, ob die Gesamt-Punkte eines Teams (Smalls + Zvanje-Punkte, falls zutreffend) den `winThreshold` (berechnet mit `GameUtils.calculateWinThreshold()`) überschreiten.


Kurz gesagt: "Smalls" werden inkrementell während jeder Runde gesammelt. Zvanje-Punkte werden einmal vor Beginn der Runden berechnet. Die Gesamt-Punkte für jedes Team werden am Ende jeder Runde (und des Spiels) berechnet, indem "Smalls" und Zvanje-Punkte kombiniert werden. Das Gewinnerteam ist dasjenige, das zuerst den berechneten `winThreshold` (50% + 1 Punkt) überschreitet. Ohne den Code für `Team` und `Round` bleibt diese Erklärung etwas unvollständig.
```java
${detectPlayerZvanje}
``` 

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
    "pickCard", Text.cutOut("./controllers/Match.java", "// pickCard"),
    "goBack", Text.cutOut("./controllers/Match.java", "// goBack"), 
    "saveSnapshot", Text.cutOut("./controllers/Match.java", "// saveSnapshot"),
    "chooseTrumpOrSkip", Text.cutOut("./ai/AiPlayerPRO.java", "// chooseTrumpOrSkip"), 
    "findPlayableCardIndexes", Text.cutOut("./services/RoundUtils.java", "// findPlayableCardIndexes"), 
    "detectPlayerZvanje", Text.cutOut("./services/ZvanjeService.java", "// detectPlayerZvanje")
    )));


