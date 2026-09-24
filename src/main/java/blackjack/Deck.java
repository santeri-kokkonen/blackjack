package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {
    final List<Card> cards = new ArrayList<>();

    public Deck() {

        for(var rank : Rank.values()) {
            for (var suit : Suit.values()) {
                cards.add(new Card(rank, suit));
            }
            Collections.shuffle(cards);
        }
    }

    /**
     * jaa kortti. otetaan pakan ylin kortti
     *
     * @pre true
     * @post RESULT == jaettu kortti TAI ei kortteja -> poikkeus
     */
    Card deal() throws DeckEmptyException {
        try {
            return cards.removeFirst();
        }
        catch(NoSuchElementException e) {
            throw new DeckEmptyException();
        }


    }

}
class DeckEmptyException extends Exception {}
