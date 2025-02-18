Clerk.clear();
Clerk.markdown(
    Text.fillOut(
"""
### Name: Lovro Music | Matrikelnummer: 5517961
# belaDocu.java : kurze Spielbeschreibung
### `/open codeDocu.java` - Funktionsversprechen

# Belote


Ein Spiel, das nie langweilig wird. Ob in der Kneipe, im Garten, 
mit Freunden oder Familie – die strategische Tiefe, 
anpassbaren Regeln und der soziale Aspekt sorgen für stundenlangen Spielspaß. 

Mit nur 32 Karten und einer ebenen Fläche vergeht die Zeit schnell. 
Geheimnisse, Tricks, schnelles Denken und präzises Rechnen machen das Spiel aus. 
Besonders spannend: stundenlange Diskussionen über Regeln und Strategien, die immer wieder neue Perspektiven eröffnen. 

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

# Implementierte Version


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

""", Map.of("Match", Text.cutOut("./controllers/Match.java", "// Match constructor"),
"CheckSettings", Text.cutOut("./controllers/Match.java", "// Settings"), 
"Play", Text.cutOut("./controllers/Match.java", "// Play"))));
