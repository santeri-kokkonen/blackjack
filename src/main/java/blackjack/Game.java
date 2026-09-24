package blackjack;

import java.util.List;

public class Game {
    Player dealer = new Player(List.of(), "Jarmo Jakaja");
    List<Player> players =List.of(new Player(List.of(),"Jarmo Jakaja"));
    Deck deck = new Deck();
    Turn turn = new Turn();
    final UI ui;

    public Game(UI ui) {
        this.ui = ui;
    }

    void playGame() {
        ui.askWhetherWeDeal();
    }

    /**
     * Pelaa kierros
     *  - jos dealCard -> nosta kortti pakasta ja jaa pelaajalle
     *  - muuten ei tapahdu mitään
     *  - tarkista pisteet
     */
    void PlayRound(boolean dealCard) {
        var active = turn.activePlayer;
        turn.flip();
        try {
            var dealedCard = deck.deal();

        } catch (DeckEmptyException e) {

        }
        return wins().equals();
    }

    /**
     * kertoo onko pelissä voittaja tällä hetkellä
     * RESULT.equals(dealer) -> jakaja on voittaja
     * RESULT.equals(Player) -> pelaaja on voittja
     * RESULT.equals(null) -> ei vielä voittajaa
     */
    Player wins() {
        return null;
    }
}
