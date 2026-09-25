package blackjack;

import java.util.Objects;

/**
 * Immutable representation of a playing card.
 *
 * @invariant rank != null
 * @invariant suit != null
 */
public record Card(
        Rank rank,
        Suit suit
) {

    /**
     * Constructs a card with given rank and suit.
     *
     * @pre rank != null
     * @pre suit != null
     * @post this.rank() == rank
     * @post this.suit() == suit
     */
    public Card(Rank rank, Suit suit) {
        Objects.requireNonNull(rank, "rank must not be null");
        Objects.requireNonNull(suit, "suit must not be null");

        this.rank = rank;
        this.suit = suit;

        // Postconditions and invariants
        assert this.rank != null : "Card.rank invariant violated";
        assert this.suit != null : "Card.suit invariant violated";
    }
}

enum Rank {
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King,
    Ace
}

enum Suit {
    Club,
    Diamond,
    Heart,
    Spade
}