package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Represents a standard 52-card deck.
 *
 * @invariant cards != null
 * @invariant 0 <= cards.size() && cards.size() <= 52
 */
public class Deck {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Constructs a new shuffled standard deck.
     *
     * @pre true
     * @post size() == 52
     * @post isEmpty() == false
     */
    public Deck() {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
        checkInvariant();
        assert size() == 52 : "New deck must contain 52 cards";
        assert !isEmpty() : "New deck must not be empty";
    }

    /**
     * Deals one card from the top of the deck.
     *
     * @pre !isEmpty()
     * @post RESULT != null
     * @post size() == oldSize - 1
     */
    public Card deal() throws DeckEmptyException {
        if (isEmpty()) {
            throw new DeckEmptyException("Cannot deal from an empty deck");
        }
        int oldSize = size();
        try {
            Card dealt = cards.remove(0);
            checkInvariant();
            assert dealt != null : "Dealt card must not be null";
            assert size() == oldSize - 1 : "Deck size must decrease by one after deal()";
            return dealt;
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            throw new DeckEmptyException("Deck ran out of cards unexpectedly", e);
        }
    }

    /**
     * Number of cards remaining in the deck.
     *
     * @pre true
     * @post RESULT >= 0 && RESULT <= 52
     */
    public int size() {
        int s = cards.size();
        assert s >= 0 && s <= 52 : "size invariant violated";
        return s;
    }

    /**
     * Indicates whether the deck is empty.
     *
     * @pre true
     * @post RESULT == (size() == 0)
     */
    public boolean isEmpty() {
        boolean empty = cards.isEmpty();
        assert empty == (size() == 0) : "isEmpty must be consistent with size()";
        return empty;
    }

    /**
     * Shuffles remaining cards in the deck.
     *
     * @pre true
     * @post size() == oldSize
     */
    public void shuffle() {
        int oldSize = size();
        Collections.shuffle(cards);
        checkInvariant();
        assert size() == oldSize : "Shuffle must not change deck size";
    }

    /**
     * Resets this deck back to a full, shuffled 52-card deck.
     *
     * @pre true
     * @post size() == 52
     */
    public void reset() {
        cards.clear();
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
        checkInvariant();
        assert size() == 52 : "reset must create a full 52-card deck";
    }

    /**
     * Checks class invariants.
     */
    private void checkInvariant() {
        assert cards != null : "Deck.cards must not be null";
        assert cards.size() >= 0 && cards.size() <= 52 : "Deck size must be between 0 and 52";
        for (Card c : cards) {
            Objects.requireNonNull(c, "Deck must not contain null cards");
        }
    }
}

class DeckEmptyException extends Exception {
    public DeckEmptyException() {
        super();
    }

    public DeckEmptyException(String message) {
        super(message);
    }

    public DeckEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}