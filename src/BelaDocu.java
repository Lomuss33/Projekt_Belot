Clerk.clear();
Clerk.markdown(
    Text.fillOut(
"""

Belote
===============	

Ein Spiel, das nie langweilig wird. Egal ob in der Kneipe oder im Garten, 
mit Freunden oder Familie – die strategische Tiefe, die Anpassbarkeit der Regeln 
und der soziale Aspekt sorgen für stundenlangen Spielspaß. 

Täglich neue Herausforderungen, 
immer wieder andere Strategien, das perfekte Spiel bleibt unerreichbar, 
und doch ist der Spaß mit denselben Menschen garantiert. Geheimnisse, Tricks, schnelles Denken, präzises Rechnen – all das steckt
in diesem kleinen Kartenspiel mit 32 Karten. Man braucht nur eine ebene Fläche, 
ein Getränk und schon vergeht die Zeit wie im Flug.

Genauer gesagt "la Belote“, ist ein französisches Kartenspiel, 
das in meinem kroatischen Dorf "Brišnik" in Bosnien und Herzegowina
und in vielen anderen Ländern seit Generationen gespielt wird. 
Obwohl die Regeln in verschiedenen Regionen variieren, haben sie denselben Ursprung. 
Die Spielweise und die Regeln unterscheiden sich jedoch erheblich.

Region/Gruppe | Name
-------|----------
Frankreich | Belote
ex-Yugo | Bela & Belot
Arabien| Balūt & Belote
Griechenland | Vida & Bourlot
Ukraine | Deberts & Klabor
Italien | Belotta
Jewish | Clobyosh
Mazedonien | Beljot
Armenia | Bazaar belote
Madagascar | Tsiroanomandidy & Beloty
Niederland | Klaberjass
Turkei | Belot
Zypern | Pilotta

Quelle: [Wikipedia](https://en.wikipedia.org/wiki/Belote)

---

Implementierte Version
===============	

## Die Brišnik Bela
>Eine Belot-Variante, die seit der napoleonischen Zeit in Dalmatien an der Adriaküste
von Soldaten auf dem Dinara Gebirge Gebiet populär wurde und sich unter Männern etablierte. 
Nach über 200 Jahren haben sich die Regeln vereinfacht, oft abhängig von den Mitspielern. 
Meine Beschreibung basiert auf der Spielweise innerhalb meines Freundeskreises.

---
## Spielablauf

#### 1. **Vorbereitung**
- Das Spiel wird mit vier Spielern und einem 32-Karten-Deck gespielt.
- Jedes Team besteht aus zwei Spielern, die sich gegenüber sitzen.
- Es werden so viele Spiele gespielt, bis ein Team 1.001 Punkte erreicht.
- Es werden 8 Runden pro Spiel gespielt.
- Ruden verdienen Punkte, die am Ende des Spiels gezählt werden und möglich verloren gehen. - Kleine (Smalls) Punkte
- Wir unterscheiden die unfeste im Runden gesammelte Punkte kleine Punkte (Smalls) 
- und feste nach einen Spiel bestätigte große Punkte (Bigs)
- Ziel: Ein Team muss **1.001 Punkte** erreichen, um zu gewinnen. 
Falls beide Teams 1.001 Punkte in derselben Spiel erreichen, gewinnt das Team mit den meisten Punkten. 

#### 2. **Kartenverteilung & Trump bestimmung**
- Der Geber teilt jedem Spieler **sechs Karten** aus.
- Basierend auf diesen Karten wird die **Trumpffarbe** (Adut) bestimmt. Spieler entscheiden reihum, beginnend mit dem rechten Nachbarn des Gebers. 
  - Ein Spieler kann eine bestimmte Trumpffarbe ansagen oder „Weiter“ sagen.
  - Der Geber entscheidet zuletzt und muss eine Farbe wählen, wenn alle anderen Spieler „Weiter“ gesagt haben.
- Nachdem die Trumpffarbe festgelegt wurde, nimmt jeder Spieler die **verbleibenden zwei Karten** auf.

#### 3. **Ansagephase (Zvanje)**
- Spieler prüfen ihre Karten auf Kombinationen, um Punkte für ihr Team anzusagen.
- **Mögliche Ansagen und Punkte:**
  - **Belot** (alle Karten einer Farbe): 1001 Punkte.
  - **Bela** (Dame + König der Trumpffarbe): 20 Punkte.
  - **Vier Karten desselben Rangs**: 
    - 4 Buben: 200 Punkte, 4 Neunen: 150 Punkte, 
    - 4 Asse, 4 Zehnen, 4 Könige oder 4 Damen: 100 Punkte.
  - **Folgen derselben Farbe**:
    - 7 Karten: 100 Punkte, 6 Karten: 100 Punkte,
    - 5 Karten: 100 Punkte, 4 Karten: 50 Punkte, 
    - 3 Karten: 20 Punkte.
- **Regeln zur Punktevergabe:**
  - Nur das Team mit der stärksten Ansage erhält die Punkte. Bei Gleichstand gewinnt das Team näher am Geber.
  - Spieler, die eine Ansage machen, zeigen die Karten, ohne ihre ganze Hand zu offenbaren.

#### 4. **Start des Runden**
- Der Spieler rechts vom Geber legt die erste Karte ab.
- Gespielt wird gegen Uhrzeigersinn. Alle Spieler legen pro Runde eine Karte.
- Es werden 8 Runden für 8 Karten gespielt.

#### 5. **Regeln beim Kartenlegen**
- **Farbregel:** Die erste gespielte Karte bestimmt die führende Farbe der Runde. Diese Farbe behaltet die standard stärke, restliche 2 Farben sind kraftloss.
- **Stärkeregel:** Spieler müssen, wenn möglich, eine Karte legen, die stärker ist als die aktuelle höchste Karte am Tisch.

#### 6. **Ende der Runde**
- Sobald alle vier Spieler je eine Karte abgelegt haben, endet die Runde.
- **Rundengewinner:** Der Spieler mit der stärksten Karte gewinnt die Runde.
- **Belohnung:** Die Werte aller gespielten Karten in der Runde werden seinen Team in Smalls addiert, falls es die letzte Runde ist +10 Punkte.
- Rundengewinner startet die neuer Runde mit ersten kartenwurf.

#### 7. **Spielende**
- Das Team, das den Trumpf gewählt hat, muss mindestens **die Hälfte der Gesamtpunkte + 1** erreichen (z. B. mindestens 82 Punkte in einer Standardspiel).
- Wenn das Trumpf-Team diese Punktzahl nicht erreicht, erhält das gegnerische Team **alle** Punkte der Runde.
- Hat das Trumpf-Team die nötigen Punkte erreicht, werden die erzielten Punkte normal zwischen den Teams aufgeteilt.
- Smalls werden reset und Bigs werden mit dem Gewinn addiert.

#### 9. **Spielende**
- Solange kein Team **1.001 Punkte** erreicht, werden neue Spiele angefagen.
- Als nächster Geber wird der Spieler rechts vom vorherigen Geber bestimmt.
- Der Geber mischt und teilt die Karten erneut aus, und das Spiel beginnt von vorne.

---
---



Belot: Eine reine Java-Implementierung mit JShell-Steuerung
===============	

### Übersicht
>Die `Match`-Klasse in Java bildet das Gerüst für die Bela-Spielsimulation.  
Sie steuert den Spielablauf durch einen Zustandsautomaten, der verschiedene Phasen 
(Start, Trumpfauswahl, Zvanje, Rundenspiel, Spielende, Matchende) definiert.  
Jede Phase wird durch Methoden der `Match`-Klasse abgearbeitet, die wiederum auf die Funktionalität der `Game`-Klasse 
(für Spiellogik) und der `Round`-Klasse (für einzelnen Runden) zugreifen.  
Der Spieler interagiert über JShell, indem er Methoden wie `pickTrump()` oder `pickCard()` aufruft;  
das Spiel wartet auf Benutzereingaben und hält an, wenn diese fehlen.  
Nach dem Abschluss einer Runde oder eines Spiels wird der Spielzustand entsprechend aktualisiert 
und die nächste Phase initiiert,  wobei sich der Zyklus aus Phasen und Runden wiederholt, 
bis ein Team die benötigten Punkte erreicht und das Match gewinnt.  Der Mechanismus der Snapshots ermöglicht es, 
den Spielverlauf zu speichern und bei Bedarf auf vorherige Zustände zurückzusetzen.
 


## Steuerung

>Die Interaktion mit dem Bela-Spiel erfolgt über die JShell in der Command Prompt (kein PowerShell). 
Der Benutzer steuert die Match-Instanz mittels einfacher Befehle. 
Wichtig ist dabei die korrekte Konsolenkodierung: chcp 65001 stellt UTF-8 sicher.
Die Ausführung der Datei run.jsh (mit /open) importiert notwendige Bibliotheken und konfiguriert die Konsolenausgabe für UTF-8 
(System.setOut(...)), um korrekte Sonderzeichen anzuzeigen für die Konsole-Ansicht.


### Tutorial

1. Neues Command Prompt-Fenster öffnen in Projektverzeichnis.
 > `cd src` navigiert zum Quellcode-Verzeichnis.
2. Konsolenkodierung auf UTF-8 Codepage einstellen.
 > chcp 65001
3. Das Projekt kompilieren und die .class-Dateien in das out-Verzeichnis speichern.
  > javac -d out models/*.java controllers/*.java services/*.java ai/*.java
4. JShell mit aktiviertem Preview-Modus und dem out-Verzeichnis als Klassenpfad starten
  > jshell --class-path out --enable-preview
5. Die Datei run.jsh öffnen, um das Spiel laden und lvp zu aktivieren unter http://localhost:50001.
  > /open run.jsh

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
`goBack()` | Geht zu einem vorherigen Spielzustand zurück (Undo-Funktion).

Spielstart
===============	

## 1. **Match-Erstellung**: 

```java
Match match = new Match();
```

Der Konstruktor der Klasse Match (`models.Match.java`) initialisiert eine neue Instanz mit Standardwerten. 
Er setzt den `gameCounter` auf 0, legt den `dealerIndex` auf 3 fest 
(was bedeutet, dass der HumanPlayer das Spiel beginnt), 
und weist den anfänglichen MatchPhase den Wert START zu. 
Außerdem werden alle boolesche Phase-Flags 
(`startGame`, `endGame`, `startRound` und `endMatch`) auf false gesetzt. 

## 2. **Einstellungen**:
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

```java
${CheckSettings}
```

## 3. **Spielbeginn**: 

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
${Match}
```

## 4. **Phasenwechsel und Interaktionen**: 
- Wenn `play()` aufgerufen wird, was nach am Start jeder Aktion des Spielers erfolgt,
wechselt das Spiel durch die verschiedenen Phasen:

Ablauf | Phase | Bedeutung | Aktion in der Phase
------|------|-----------|---------
1 | **START** | Übernimmt die Einstellungen und bereitet das Spiel vor. | `match.startGame()`
2 | **CHOOSING_TRUMP** | Die Spieler wählen die Trumpfkarte. | `match.pickTrump(int x)`
3 |**SHOW_ZVANJE** | Der Zvanje wird angezeigt und dessen Punkte ausgewertet. | `match.startRound()`
4 |**PLAYING_ROUNDS** | Die Runden werden gespielt, wobei jeder Spieler Karten ausspielen kann. | `match.pickCard(int x)`
5 |**END_OF_GAME** | Am Ende jeder Runde werden die Punkte vergeben und geprüft, ob ein Team das Spiel gewonnen hat. | `match.endGame()`
6 |**END_OF_MATCH** | Das gesamte Match wird beendet, und die endgültigen Ergebnisse werden angezeigt. | `match.endMatch()`

```java
${Play}
```

### Phasendurchlauf


**1. START:**  -> **match.startGame()**

The `runStartPhase` method:

1. Checks settings and prints the start message.
2. Exits if `startGame` is false, prompting the user to start.
3. Initializes a new game if it doesn't already exist.
4. Sets the initial state for both teams.
5. Advances to the `CHOOSING_TRUMP` phase and resets `startGame`.

**2. CHOOSING_TRUMP:** -> **match.pickTrump(int x)**

The `runChoosingTrumpPhase` method:

1. Prints the current phase.
2. If `game.trumpSelection()` fails (meaning trump suit wasn't chosen), it prompts the user to choose a suit (or skip).
3. If trump selection was successful, it proceeds to `SHOW_ZVANJE` phase and finds the `zvanje`.
4. Returns `true` to signal successful phase completion.

**3. SHOW_ZVANJE:** -> **match.startRound()**

The `runShowZvanjePhase` method:

1. Prints the Anruf (Zvanje) Result.
2. Checks if `startRound` is true; if not, it waits for the zvanje to be accepted and returns `false`.
3. If `startRound` is true, it resets `startRound` to `false`.
4. Transitions to the `PLAYING_ROUNDS` phase and returns `true`.

**4. PLAYING_ROUNDS:** -> **match.pickCard(int x)**

The `runPlayingRoundsPhase` method:

1. Prints that the game is in the `PLAYING_ROUNDS` phase.
2. Calls `game.playRounds()`. If this returns `false`, it means rounds are still being played, and the user is prompted to play a card.  The method then returns `false`.
3. If `game.playRounds()` returns `true` (meaning all rounds are finished), it prints "End of game", transitions to the `END_OF_GAME` phase, and returns `true`.

**5.  END_OF_GAME:** -> **match.endGame()**

The `runEndOfGamePhase` method handles the end of a game within a match:

1. It prints the current phase ("END_OF_GAME").
2. It checks if `endGame` is true; if not, it waits for the game to end and returns `false`.
3. If `endGame` is true, it resets `endGame` to `false`.
4. It determines the winner using `matchWinner()`.
5. It prints the end-game results using `printEndGame(winner)`.
6. If there's a winner, the phase changes to `END_OF_MATCH`.
7. Otherwise (no overall match winner yet), the game resets for the next round via `resetForNextGame()`, the game counter increments, and the phase returns to `START`.

**6.  END_OF_MATCH:** -> **match.endMatch()**

The `runEndOfMatchPhase` method handles the conclusion of an entire match:

1. It prints the match end results using `printMatchEnd(winner)`.
2. It checks if `endMatch` is true; if false, it waits for confirmation to end the match and returns `false`.
3. If `endMatch` is true, it resets `endMatch` to false.
4. It resets the match state using `resetMatch()`.
5. It sets the current phase back to `START`, preparing for a new match.
6. Finally, it returns `true`.

## Snapshot-Verwaltung und Spielzustand im Java-Code

Der Java-Code implementiert ein System zur Speicherung und Wiederherstellung von Spielzuständen (Snapshots), 
um dem Spieler das Zurücksetzen von Spielzügen zu ermöglichen.  Dies wird hauptsächlich über die Methoden `saveSnapshot()`, `revertToPreviousSnapshot()`, und die Verwendung der `clone()`-Methode realisiert.

**1. `saveSnapshot()`:**

Diese Methode erstellt eine Kopie des aktuellen Spielzustands.  Sie verwendet die `clone()`-Methode, um ein 
tiefes Klonen des `Match`-Objekts zu gewährleisten.  Dies bedeutet, dass nicht nur Referenzen kopiert werden, 
sondern auch alle enthaltenen Objekte (wie Teams, Spieler, und Spielrunden) dupliziert werden.  
Der so erstellte Snapshot wird auf einem Stapel (`snapshots`) gespeichert.  
Snapshots werden nur für die Schwierigkeitsgrade `LEARN` (Spiel übergreifend) und `NORMAL`(Spiel begrenzt) erstellt; im `PRO`-Modus ist diese Funktion deaktiviert.

**2. `clone()`-Methode:**

Die `clone()`-Methode ist essentiell für die Snapshot-Funktionalität. Sie implementiert ein tiefes Klonen 
des `Match`-Objekts und aller seiner Komponenten.  Dies verhindert, dass Änderungen an einem Snapshot den 
ursprünglichen Spielzustand beeinflussen, oder umgekehrt. Die Implementierung umfasst das rekursive Klonen 
von `Team`-, `Player`- und `Game`-Objekten um Konsistenz zu gewährleisten.  `CloneNotSupportedException` wird 
abgefangen, um das Programm stabil zu halten.

**3. `match.goBack()`:**

Diese Methode ermöglicht es, zu einem zuvor gespeicherten Snapshot zurückzukehren.  
Sie entfernt den letzten Snapshot vom Stapel (`snapshots`) und setzt den aktuellen Spielzustand auf 
den Inhalt des Snapshots `restoreSnapshot(Match previousState)`.  Ähnlich wie bei `saveSnapshot()` wird ein tiefes Klonen verwendet. 
Falls kein Snapshot vorhanden ist, oder die ist unverfügbar da 
die Schwierigkeit `PRO` ist or da im Normalmodus bis zum Anfang von letzten Spiel ist, 
wird eine entsprechende Meldung ausgegeben. 

**4.  `resetMatch()`:**

Diese Methode dient zum vollständigen Zurücksetzen des Spiels.  Im Gegensatz zu `goBack()`, 
welches nur den Spielzustand auf einen vorherigen Punkt zurücksetzt, löscht `resetMatch()` den gesamten Spielzustand,
einschließlich der gespeicherten Snapshots, und bereitet das Spiel für einen komplett neuen Match vor.


>Dieses System ermöglicht es dem Spieler, Fehler zu korrigieren oder alternative Spielzüge auszuprobieren, 
ohne das Spiel neu starten zu müssen, jedoch mit Einschränkungen bezüglich der Schwierigkeit 
und der Phase des Spiels.


# Die TurtleView und Funktions

## Bitte die File BelaView.java öffnen und die TurtleView.java

""", Map.of("Match", Text.cutOut("./controllers/Match.java", "// Match constructor"),
"CheckSettings", Text.cutOut("./controllers/Match.java", "// Settings"), 
"Play", Text.cutOut("./controllers/Match.java", "// Play"))));
