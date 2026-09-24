package blackjack;

public class Turn {
    PlayerType activePlayer;
    Player[] turns = new Player[] {};
    int active=0;
    /**
     * @.ClassInvariant playerType != null
     */

    void flip() {
        active++;
        active %= turns.length;
        return turns[active];
    }

}

enum PlayerType {
    Dealer,
    Player
}
