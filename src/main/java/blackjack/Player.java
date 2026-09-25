package blackjack;

import java.util.Objects;

/**
 * Represents a Blackjack player (dealer or regular).
 *
 * @invariant name != null && !name.isBlank()
 * @invariant hand != null
 */
public class Player {
    private final String name;
    private final Hand hand;
    private final boolean dealer;

    /**
     * Constructs a player with an empty hand.
     *
     * @pre name != null && !name.isBlank()
     * @post getName().equals(name)
     * @post getHand().size() == 0
     */
    public Player(String name, boolean dealer) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
        this.hand = new Hand();
        this.dealer = dealer;
        checkInvariant();
        assert getName().equals(name) : "Player name must be set correctly";
        assert getHand().size() == 0 : "New player hand must be empty";
    }

    /**
     * Adds card to player's hand.
     *
     * @pre card != null
     * @post getHand().size() == oldSize + 1
     */
    public void receiveCard(Card card) {
        Objects.requireNonNull(card, "card must not be null");
        int oldSize = hand.size();
        hand.addCard(card);
        checkInvariant();
        assert hand.size() == oldSize + 1 : "receiveCard must increase hand size by one";
    }

    /**
     * Player's display name.
     *
     * @pre true
     * @post RESULT != null && !RESULT.isBlank()
     */
    public String getName() {
        String n = name;
        assert n != null && !n.isBlank() : "Player name invariant violated";
        return n;
    }

    /**
     * Player's hand.
     *
     * @pre true
     * @post RESULT != null
     */
    public Hand getHand() {
        Hand h = hand;
        assert h != null : "Player hand invariant violated";
        return h;
    }

    /**
     * Indicates whether this player is the dealer.
     *
     * @pre true
     * @post RESULT == dealer
     */
    public boolean isDealer() {
        boolean d = dealer;
        assert d == dealer : "isDealer must reflect internal dealer flag";
        return d;
    }

    /**
     * Resets the player's hand to empty.
     *
     * @pre true
     * @post getHand().size() == 0
     * @post getHand().score() == 0
     */
    public void resetHand() {
        int oldSize = hand.size();
        hand.clearHand();
        checkInvariant();
        assert hand.size() == 0 : "resetHand must leave hand empty (oldSize=" + oldSize + ")";
        assert hand.score() == 0 : "resetHand must leave score 0";
    }

    /**
     * Checks class invariants.
     */
    private void checkInvariant() {
        assert name != null : "Player.name must not be null";
        assert !name.isBlank() : "Player.name must not be blank";
        assert hand != null : "Player.hand must not be null";
    }
}