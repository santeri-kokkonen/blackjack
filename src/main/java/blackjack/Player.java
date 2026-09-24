package blackjack;

import java.util.List;

public class Player {
    final List<Card> hand;
    final String name;

    public Player(List<Card> hand, String name) {
        this.hand = hand;
        this.name = name;
    }
}

