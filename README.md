# Blackjack – Java Swing Blackjack Game

A simple Blackjack game implemented in Java using Swing, following MVC and Design by Contract principles.  
Player is at the bottom, dealer at the top, with real playing card graphics.

## Requirements
* Java JDK (recent version, e.g., 17+)
* Maven

## Project Structure
* `src/main/java/blackjack/Main` – entry point
* `BlackjackModel` – game logic (rules, scoring, deck)
* `BlackjackController` – connects UI to model
* `BlackjackFrame` – top-level window
* `BlackjackTablePanel` – Swing UI (cards, buttons, labels)
* `Deck`, `Card`, `Hand`, `Player`, `Turn`, `Rank`, `Suit` – domain classes
* `src/main/resources/cards/` – 52 card images (`2C.png`...`AS.png`), named by rank + suit letter

## Setup
Go to the project directory:
```bash
cd ~/projects/blackjack
```
Ensure card images are in `src/main/resources/cards/` and named exactly:
* **Clubs:** `2C.png` … `10C.png`, `JC.png`, `QC.png`, `KC.png`, `AC.png`
* **Diamonds:** `2D.png` … `10D.png`, `JD.png`, `QD.png`, `KD.png`, `AD.png`
* **Hearts:** `2H.png` … `10H.png`, `JH.png`, `QH.png`, `KH.png`, `AH.png`
* **Spades:** `2S.png` … `10S.png`, `JS.png`, `QS.png`, `KS.png`, `AS.png`

## Build
From the project root (where `pom.xml` is located):
```bash
mvn clean package
```
This compiles the code and builds: `target/blackjack-1.0-SNAPSHOT.jar`

## Run

**Option 1 – Runnable JAR**
```bash
java -jar target/blackjack-1.0-SNAPSHOT.jar
```

**Option 2 – Maven exec plugin**
```bash
mvn compile exec:java
```

## How to Play
1. Start the program. A "Blackjack" window appears.
2. Click **Start Round** to begin a hand.
3. **Player (bottom):**
    * Click **Hit** to take another card.
    * Click **Stand** to stop; the dealer then plays automatically.
4. **Dealer (top):**
    * Hits while the score is below 17.
    * Stands on 17 or more (rule text shown under "Dealer").
5. **Result:** Status text shows who wins (Player, Dealer, or Push). Buttons are disabled after the hand finishes.
6. Click **Start Round** again to begin a new hand.
7. The deck automatically reshuffles when low, so play can continue indefinitely.