package blackjack;

import java.util.Objects;

/**
 * Controller connecting the view (Swing) and the model.
 * No UI code, no Swing imports here; strictly commands + queries.
 *
 * @invariant model != null
 */
public class BlackjackController {

    private final BlackjackModel model;

    /**
     * Constructs a controller with a given model.
     *
     * @pre model != null
     * @post getModel() == model
     */
    public BlackjackController(BlackjackModel model) {
        Objects.requireNonNull(model, "model must not be null");
        this.model = model;
        checkInvariant();
        assert getModel() == model : "Controller model must be set correctly";
    }

    /**
     * Starts a new round.
     *
     * @pre !model.isRoundFinished() || true   (we allow multiple controllers and models)
     * @post !model.isRoundFinished()
     */
    public void startRound() throws DeckEmptyException {
        model.startRound();
        checkInvariant();
        assert !model.isRoundFinished() : "Round must not be finished immediately after startRound via controller";
    }

    /**
     * Player requests a hit.
     *
     * @pre !model.isRoundFinished()
     * @post model.getCurrentPlayer().getHand().size() == oldSize + 1
     */
    public void hit() throws DeckEmptyException {
        int oldSize = model.getCurrentPlayer().getHand().size();
        model.playerHit();
        checkInvariant();
        assert model.getCurrentPlayer().getHand().size() == oldSize + 1 :
                "hit must add one card to player's hand";
    }

    /**
     * Player stands; dealer plays.
     *
     * @pre !model.isRoundFinished()
     * @post model.isRoundFinished() == true
     */
    public void stand() throws DeckEmptyException {
        model.playerStand();
        checkInvariant();
        assert model.isRoundFinished() : "stand must finish the round";
    }

    /**
     * Access model for querying state (for view).
     *
     * @pre true
     * @post RESULT != null
     */
    public BlackjackModel getModel() {
        BlackjackModel m = model;
        assert m != null : "model invariant violated";
        return m;
    }

    /**
     * Checks invariants.
     */
    private void checkInvariant() {
        assert model != null : "model must not be null";
    }
}