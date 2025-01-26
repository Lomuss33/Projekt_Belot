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
 


Steuerung
===============	

>Die Interaktion mit dem Bela-Spiel erfolgt über die JShell in der Command Prompt (kein PowerShell). 
Der Benutzer steuert die Match-Instanz mittels einfacher Befehle. 
Wichtig ist dabei die korrekte Konsolenkodierung: chcp 65001 stellt UTF-8 sicher.
Die Ausführung der Datei run.jsh (mit /open) importiert notwendige Bibliotheken und konfiguriert die Konsolenausgabe für UTF-8 
(System.setOut(...)), um korrekte Sonderzeichen anzuzeigen für die Konsole-Ansicht.


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

**1. START:**

*   `initializeGameSettings()` richtet Spieler, Teams und Schwierigkeit ein.
*   `startGame()` startet das Spiel und führt die notwendigen Prüfungen durch.


**2. CHOOSING_TRUMP:**

*   Die `Spiel`-Klasse (nicht vollständig gezeigt) würde das Verteilen von sechs Karten an jeden Spieler übernehmen (`dealCards()`-Methode, angenommen).
*   `runChoosingTrumpPhase()` verwaltet die Trumpfauswahl. Eine Methode innerhalb von `Spiel` (z. B. `trumpfAuswahl()`) würde die Spielerentscheidungen verarbeiten und die Trumpffarbe bestimmen.


**3. SHOW_ZVANJE:**

*   Eine neue Methode, z. B. `verarbeiteAnsagen()`, würde der `Spiel`-Klasse hinzugefügt.
*   Diese Methode würde die Hände der Spieler (`Spieler.getHand()`) analysieren, um Ansagen (Belot, Bela, Vierlinge usw.) zu identifizieren.
*   Die Punktevergabe für jede Ansage würde innerhalb der `verarbeiteAnsagen()`-Methode gehandhabt.


**4. PLAYING_ROUNDS:**

*   `runPlayingRoundsPhase()` verwaltet die Phase des Rundenspiels.
*   `Spiel.spieleRunden()` iteriert durch acht Runden.
*   Die `Runde`-Klasse (angenommen) handhabt das Kartenlegen und bestimmt den Rundensieger basierend auf Farbe und Rang (`Karte`-Klasse angenommen).
*   Die `Runde`-Klasse (angenommen) addiert die Punkte der Runde zu den Teamwerten (Smalls).


**5.  END_OF_GAME:**

*   In der `Runde`-Klasse (angenommen) addiert eine `berechneRundenpunkte()`- oder ähnliche Methode Punkte (Smalls) zum siegreichen Team. Ein Bonus von +10 Punkten könnte für die letzte Runde hinzugefügt werden.


**6.  END_OF_MATCH:**

*   `runEndOfGamePhase()` verarbeitet das Spielende.
*   `spiel.trumpfTeamErfolgreich()` würde prüfen, ob das Trumpfteam die Punktzahlanforderung erfüllt hat. Wenn nicht, werden die Punkte dem Gegner gutgeschrieben.
*   `spielGewinner()` prüft, ob eines der Teams 1001 Punkte erreicht hat und erklärt den Spielgewinner.
*   `setzeFürNächstesSpielZurück()` setzt Smalls zurück und rotiert den Geber (`rotateDealer()` bereits vorhanden).


**7.  Matchende:**

*   `runEndOfMatchPhase()` verwaltet das Matchende.
*   `printMatchEnd()` zeigt die Endergebnisse an.
*   `setzeMatchZurück()` setzt den gesamten Spielzustand zurück.

**Wichtige Überlegungen:**

*   Die Klassen `Spiel`, `Runde`, `Spieler`, `Karte` und `Team` sind entscheidend und werden im bereitgestellten Code-Snippet nicht vollständig gezeigt. Diese Klassen enthalten den größten Teil der Spiellogik (Kartenwerte, Punkteberechnung usw.).
*   Die Fehlerbehandlung (ungültige Spieleraktionen) muss in der `Match`-Klasse und ihren abhängigen Klassen gründlich implementiert werden.
*   Der `snapshots`-Mechanismus und die `clone()`-Methode verarbeiten das Speichern und Wiederherstellen des Spielzustands und unterstützen die Rückgängig-Funktionalität.

Diese detaillierte Aufschlüsselung verdeutlicht, wie die Bela-Spielregeln in der `Match`-Klasse implementiert werden können, vorausgesetzt, dass die Hilfsklassen existieren und die spezifischen Details der Kartenspielmechanik handhaben.
  




```java
int x = 1;
```

""", Map.of("Match", Text.cutOut("./controllers/Match.java", "// Match constructor"),
"CheckSettings", Text.cutOut("./controllers/Match.java", "// Check Settings"), 
"Play", Text.cutOut("./controllers/Match.java", "// Play"),
"x", Text.cutOut("./controllers/Match.java", "// Check Settings"))));
