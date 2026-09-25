package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a player's hand in Blackjack.
 *
 * @invariant cards != null
 * @invariant score() >= 0
 */
public class Hand {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Creates an empty hand.
     *
     * @pre true
     * @post size() == 0
     * @post score() == 0
     */
    public Hand() {
        checkInvariant();
        assert size() == 0 : "New hand must be empty";
        assert score() == 0 : "New hand score must be 0";
    }

    /**
     * Adds a card to this hand.
     *
     * @pre card != null
     * @post size() == oldSize + 1
     */
    public void addCard(Card card) {
        Objects.requireNonNull(card, "card must not be null");
        int oldSize = size();
        cards.add(card);
        checkInvariant();
        assert size() == oldSize + 1 : "Hand size must increase by one after addCard()";
    }

    /**
     * Immutable view of cards in this hand.
     *
     * @pre true
     * @post RESULT != null
     */
    public List<Card> getCards() {
        List<Card> view = Collections.unmodifiableList(cards);
        assert view != null : "getCards must not return null";
        return view;
    }

    /**
     * Current number of cards in the hand.
     *
     * @pre true
     * @post RESULT >= 0
     * @post RESULT == getCards().size()
     */
    public int size() {
        int s = cards.size();
        assert s >= 0 : "Hand size must be non-negative";
        assert s == getCards().size() : "size() must equal getCards().size()";
        return s;
    }

    /**
     * Calculates Blackjack score where Ace counts as 1 or 11.
     * Face cards (J, Q, K) count as 10.
     *
     * @pre true
     * @post RESULT >= 0
     */
    public int score() {
        int total = 0;
        int aceCount = 0;
        for (Card card : cards) {
            Rank r = card.rank();
            switch (r) {
                case Two -> total += 2;
                case Three -> total += 3;
                case Four -> total += 4;
                case Five -> total += 5;
                case Six -> total += 6;
                case Seven -> total += 7;
                case Eight -> total += 8;
                case Nine -> total += 9;
                case Ten, Jack, Queen, King -> total += 10;
                case Ace -> {
                    total += 1;
                    aceCount++;
                }
                default -> throw new IllegalStateException("Unknown rank: " + r);
            }
        }
        // Upgrade some Aces from 1 to 11 (i.e., +10 extra) while staying <= 21
        int score = total;
        while (aceCount > 0 && score + 10 <= 21) {
            score += 10;
            aceCount--;
        }
        assert score >= 0 : "Hand score must be non-negative";
        return score;
    }

    /**
     * Indicates whether the hand is bust (>21).
     *
     * @pre true
     * @post RESULT == (score() > 21)
     */
    public boolean isBust() {
        boolean bust = score() > 21;
        assert bust == (score() > 21) : "isBust must be consistent with score()";
        return bust;
    }

    /**
     * Clears all cards from the hand.
     *
     * @pre true
     * @post size() == 0
     * @post score() == 0
     */
    public void clearHand() {
        cards.clear();
        checkInvariant();
        assert size() == 0 : "clearHand must leave hand empty";
        assert score() == 0 : "clearHand must leave score 0";
    }

    /**
     * Checks class invariants.
     */
    private void checkInvariant() {
        assert cards != null : "Hand.cards must not be null";
        for (Card c : cards) {
            Objects.requireNonNull(c, "Hand must not contain null cards");
        }
        int s = score();
        assert s >= 0 : "Hand score invariant violated";
    }
}