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

Implementierte Version
===============	
## Die Brišnik Bela
---
>Eine Belot-Variante, die seit der napoleonischen Zeit in Dalmatien an der Adriaküste
von Soldaten auf dem Dinara Gebirge Gebiet populär wurde und sich unter Männern etablierte. 
Nach über 200 Jahren haben sich die Regeln vereinfacht, oft abhängig von den Mitspielern. 
Meine Beschreibung basiert auf der Spielweise innerhalb meines Freundeskreises.

## Spielregeln
---
### Grundlegende Informationen

- **Spieleranzahl:** Klassisches Bela wird **mit vier Spielern** gespielt. Es existieren Varianten mit drei oder zwei Spielern, die jedoch meist weniger unterhaltsam sind.
- **Ziel des Spiels:** Das Spiel endet, sobald ein Team **1.001 Punkte** erreicht hat, was in der Regel **in weniger als 10 Runden** geschieht.
- **Kartenanzahl:** Bela wird mit einem **32-Karten-Deck** gespielt.
  - Die Kartenfarben sind: **Kreuz (♣), Karo (♦), Pik (♠), Herz (♥)**.
  - Pro Farbe gibt es folgende Karten: **7, 8, 9, 10, Bube (Jack), Dame (Queen), König (King) und Ass (Ace)**.

### Spielstart

1. **Kartenverteilung:**
   - Jede*r Spieler*in erhält **sechs Karten** ausgeteilt.
   - Anschließend wird entschieden, welche Farbe Trumpf wird.

2. **Ansage der Trumpffarbe:**
   - Basierend auf den sechs erhaltenen Karten entscheiden die Spieler*innen reihum, ob sie eine bestimmte Farbe als **Trumpffarbe (Adut)** ansagen.  
   - Der Spieler rechts von der Person, die die Karten gibt, beginnt mit der Entscheidung.
   - Alternativ kann ein Spieler „Weiter“ sagen, wodurch der nächste Spieler entscheidet.
   - Sobald eine Farbe angesagt wurde, ist sie als Trumpf festgelegt. Danach dürfen alle Spieler*innen die **verbleibenden zwei Karten** ihrer Hand ansehen.

3. **Start der Runde:**
   - Die Person rechts vom Dealer spielt die **erste Karte der Partie**.

---
### Zvanje (Ansagen) im Spiel und ihre Punkte

Nach dem Aufnehmen aller Karten prüfen die Spieler*innen, ob sie bestimmte Kartenkombinationen haben, um diese beim Team anzusagen. Diese Ansagephase kann dem Team zusätzliche Punkte einbringen. Die möglichen Ansagen und deren Punktewert sind wie folgt:

#### Kombinationen:

- **Belot:** Alle Karten derselben Farbe (automatischer Sieg; das Spiel endet sofort).
- **4 Karten des gleichen Rangs:**
  - 4 Buben: **200 Punkte**
  - 4 Neunen: **150 Punkte**
  - 4 Asse, 4 Zehnen, 4 Könige oder 4 Damen: **100 Punkte**
- **Folgen von Karten derselben Farbe:**
  - 7 aufeinanderfolgende Karten: **100 Punkte**
  - 6 aufeinanderfolgende Karten: **100 Punkte**
  - 5 aufeinanderfolgende Karten: **100 Punkte**
  - 4 aufeinanderfolgende Karten: **50 Punkte**
  - 3 aufeinanderfolgende Karten: **20 Punkte**
- **Besonderheit:** 4 Siebenen oder 4 Achtern bringen **keine Punkte**.

#### Besondere Regel zur Ansage „Bela“:
- „Bela“ bringt **zusätzliche 20 Punkte**.  
- Bedingungen für Bela:
  - Der Spieler muss **Dame** und **König in der Trumpffarbe** besitzen.
  - Bela wird während des Spiels angesagt.

### Regelungen für Ansagen und Punktevergabe

- Punkte für Ansagen werden **nur einem Team zugeordnet**:
  - Das Team mit der **stärkeren Ansage** erhält Punkte.
  - Das andere Team geht leer aus. Ausnahme: Wenn beide Teams **Punkte durch identische Ansagen** haben, erhält das Team näher am Geber die Punkte.
  
- Stärkere Ansagen eines Teams:
  - **Stärkere Karten** haben Vorrang:
    - Beispiel:
      - Team A hat eine Folge bis zur Zehn (8, 9, 10).
      - Team B hat eine Folge bis zur Dame (9, Bube, Dame).
      - **Team B** erhält die Punkte.
  - Kommt es innerhalb eines Teams zu mehreren Ansagen, werden deren Werte **addiert**.

### Zusätzliche Regeln für Zvanje

1. Wenn ein Spieler Ansagen bis zum Ass (höchste Karte) und ein anderer bis zur Neun hat, werden die Ansagen dem Spieler mit der stärkeren Kombination **zugeschrieben**.
2. Wenn Spieler eines Teams mehrere Ansagen von geringem Wert haben (z. B. zwei 20-Punkte-Ansagen) und das gegnerische Team eine einzelne höhere Ansage hat (z. B. 20 Punkte bis zur Dame), werden die Punkte automatisch dem Team mit der **stärkeren Einzelansage** zugeordnet.  
   - Beispiel:
     - Team A hat zwei Ansagen bis zur Neun (20+20 = 40 Punkte).
     - Team B hat eine Ansage bis zur Dame (20 Punkte).
     - Punkte werden **Team B** zugeschrieben.

3. Spieler, deren Ansagen anerkannt wurden, **zeigen die betreffenden Karten** vor, jedoch nicht ihre komplette Hand.

Das Spiel wird auf diese Weise Runde für Runde fortgeführt, bis ein Team die 1.001 Punkte erreicht, wodurch es den Sieg erringt. 

---

## Kartenlegen: Der Spielverlauf

Nachdem die **Trumpffarbe** gewählt wurde, alle Spieler die **zwei verbleibenden Karten aufgenommen** haben, ihre **acht Karten auf der Hand halten** und potenzielle Ansagen überprüft wurden, kann das eigentliche Spiel beginnen.

---

### Reihenfolge und Regeln des Kartenlegens

1. **Startspieler:**
   - Der erste Spieler, der rechts vom Geber sitzt, **legt die erste Karte ab.**
   - Jede*r Spieler*in legt dann **eine Karte** pro Runde ab, wobei sich die Reihenfolge im Uhrzeigersinn bewegt.

2. **Regeln beim Ablegen:**
   Beim Kartenlegen gelten zwei wichtige Regeln, die das Spielgeschehen bestimmen:
   
   - **Die Farbregel:**  
     Die Farbe der **ersten gespielten Karte** bestimmt die sogenannte **führende Farbe** der Runde.  
     - Jeder Spieler ist verpflichtet, Karten der führenden Farbe zu spielen, sofern er welche auf der Hand hat.  
     - Sind keine Karten der führenden Farbe vorhanden, können **Trumpfkarten** gespielt werden.
     - Falls der Spieler weder Karten der führenden Farbe noch Trümpfe hat, darf er **eine beliebige Karte** ablegen.
   
   - **Die Stärkeregel:**  
     Jeder Spieler muss, falls möglich, **eine höhere Karte** legen als die derzeit höchste Karte auf dem Tisch.  
     - Wenn der Spieler keine Karte hat, die stärker ist, darf er auch eine **niedrigere oder beliebige Karte** abspielen.

---

### Auswertung der Runde und Punktevergabe

1. **Rundenende:**
   Die Runde endet, sobald alle Spieler jeweils eine Karte abgelegt haben. Danach wird geprüft, wer die Runde gewonnen hat:
   - Der Spieler mit der **höchsten Karte der führenden Farbe** gewinnt die Runde,  
   - oder, falls Trumpfkarten gespielt wurden, der Spieler mit der **höchsten Trumpfkarte.**

2. **Rundenpunkte zählen:**
   - Die Werte der gespielten Karten werden addiert.  
   - Eine **Standardpartie ohne Ansagen** hat insgesamt **162 Punkte.**  
   - Für Spiele mit Ansagen wird der Wert der Punkte entsprechend angepasst (z. B. eine 20-Punkte-Ansage erhöht dies auf **182 Punkte** usw.).

3. **Siegbedingungen während der Runde:**
   - Das Team, das den **Trumpf gewählt** hat, versucht, die Runde zu gewinnen.  
   - Für einen Runden-Sieg muss es **mehr als die Hälfte der Gesamtpunkte + 1 Punkt** erzielen.  
     - Beispiel: In einer Standardpartie ohne Ansagen müssen sie mindestens **82 Punkte** sammeln (161/2 + 1).
   
   - **Wird die Punktgrenze nicht überschritten**, erhält das **gegnerische Team** sämtliche Punkte der Runde.  
   - Wenn das Team, das den Trumpf auswählte, die Punkte erreicht, erhält **jedes Team nur die Punkte, die es innerhalb der Runde erspielt hat.**

---

### Ablauf der nächsten Runde

- Der Spieler, der die vorangegangene Runde **erster begonnen hat (erste Karte ablegte)**, wird der Geber der darauffolgenden Runde.  
- Der Geber mischt und teilt die Karten an alle Spieler aus, bevor die nächste Runde beginnt.

---
Mit diesen Regeln wird die Partie Bela fortgeführt, bis ein Team die **Zielsiegpunktzahl von 1.001 Punkte** erreicht und das Spiel beendet wird. 

















---
Belot Kartenspiel im Match-Format
===============	

Im Spiel Bela Belot treten zwei Teams in einem 2-gegen-2-Format gegeneinander an, wobei jeder Spieler strategische Entscheidungen trifft, um Punkte zu sammeln. 
Die Spielmechanik wird über die Klasse Match.java gesteuert, die verschiedene Phasen wie Spielstart, Trumpfwahl und Runden spielt. 
Zunächst wählen die Spieler eine Trumpfkarte, gefolgt von der Anzeige der Zvanje, bevor die Runden beginnen.
Punkte werden in Echtzeit verfolgt, und ein Team gewinnt, wenn es als erstes 1001 Punkte erreicht. 
Das Spiel bietet außerdem die Möglichkeit, durch eine Rückgängig-Funktion Züge zu revidieren und damit strategische Optionen zu berücksichtigen.

---
Steuerung - JShell
===============	

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

Kartenspiel - die Logik
===============	

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