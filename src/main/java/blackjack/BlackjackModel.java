package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Core Blackjack game model.
 * Contains rules and state, independent of any UI.
 *
 * @invariant deck != null
 * @invariant dealer != null
 * @invariant players != null && !players.isEmpty()
 * @invariant turn != null
 */
public class BlackjackModel {

    private final Deck deck;
    private final Player dealer;
    private final List<Player> players;
    private final Turn turn;
    private boolean roundFinished;

    /**
     * Constructs a new game with one human player and one dealer.
     *
     * @pre playerName != null && !playerName.isBlank()
     * @post getPlayers().size() == 1
     * @post getDealer().isDealer() == true
     */
    public BlackjackModel(String playerName) {
        Objects.requireNonNull(playerName, "playerName must not be null");
        if (playerName.isBlank()) {
            throw new IllegalArgumentException("playerName must not be blank");
        }
        this.deck = new Deck();
        this.dealer = new Player("Dealer", true);
        this.players = new ArrayList<>();
        this.players.add(new Player(playerName, false));
        this.turn = new Turn(players.size());
        this.roundFinished = false;

        checkInvariant();
        assert players.size() == 1 : "Game must start with exactly one player";
        assert dealer.isDealer() : "Dealer must be marked as dealer";
    }

    /**
     * Starts a new round:
     *  - if deck is low, resets it
     *  - clears hands
     *  - deals two cards to player and dealer
     *
     * @pre true
     * @post getCurrentPlayer().getHand().size() == 2
     * @post dealer.getHand().size() == 2
     * @post !isRoundFinished()
     */
    public void startRound() throws DeckEmptyException {
        // If fewer than 15 cards remain, reset deck (casino style: reshuffle)
        if (deck.size() < 15) {
            deck.reset();
        }

        // reset hands
        getCurrentPlayer().resetHand();
        dealer.resetHand();
        roundFinished = false;

        // initial deal: player, dealer, player, dealer
        dealToPlayer(getCurrentPlayer());
        dealToDealer();
        dealToPlayer(getCurrentPlayer());
        dealToDealer();

        checkInvariant();
        assert getCurrentPlayer().getHand().size() == 2 : "Player must have two cards after initial deal";
        assert dealer.getHand().size() == 2 : "Dealer must have two cards after initial deal";
        assert !roundFinished : "Round must not be finished right after startRound";
    }

    /**
     * Player requests another card (hit).
     *
     * @pre !isRoundFinished()
     * @pre !getCurrentPlayer().getHand().isBust()
     * @post getCurrentPlayer().getHand().size() == oldSize + 1
     */
    public void playerHit() throws DeckEmptyException {
        if (roundFinished) {
            throw new IllegalStateException("Round already finished");
        }
        Player current = getCurrentPlayer();
        if (current.getHand().isBust()) {
            throw new IllegalStateException("Player is already bust");
        }
        int oldSize = current.getHand().size();
        dealToPlayer(current);
        checkInvariant();
        assert current.getHand().size() == oldSize + 1 : "playerHit must add exactly one card";

        if (current.getHand().isBust()) {
            // player bust: round ends immediately
            roundFinished = true;
        }
    }

    /**
     * Player stands; dealer plays their turn according to standard rules (hit until 17 or more).
     *
     * @pre !isRoundFinished()
     * @post isRoundFinished() == true
     */
    public void playerStand() throws DeckEmptyException {
        if (roundFinished) {
            throw new IllegalStateException("Round already finished");
        }
        // Dealer hits until score >= 17
        while (dealer.getHand().score() < 17 && !dealer.getHand().isBust()) {
            dealToDealer();
        }
        roundFinished = true;
        checkInvariant();
        assert roundFinished : "Round must be finished after playerStand()";
    }

    /**
     * Computes winner after round is finished.
     *
     * @pre isRoundFinished()
     * @post RESULT != null
     */
    public Winner getWinner() {
        if (!roundFinished) {
            throw new IllegalStateException("Round not finished yet");
        }
        Player player = getCurrentPlayer();
        int playerScore = player.getHand().score();
        int dealerScore = dealer.getHand().score();
        boolean playerBust = player.getHand().isBust();
        boolean dealerBust = dealer.getHand().isBust();

        Winner result;
        if (playerBust && dealerBust) {
            // Both bust: treat as dealer win (house edge)
            result = Winner.DEALER;
        } else if (playerBust) {
            result = Winner.DEALER;
        } else if (dealerBust) {
            result = Winner.PLAYER;
        } else if (playerScore > dealerScore) {
            result = Winner.PLAYER;
        } else if (dealerScore > playerScore) {
            result = Winner.DEALER;
        } else {
            result = Winner.PUSH;
        }
        assert result != null : "getWinner must not return null";
        return result;
    }

    /**
     * Indicates whether the round is finished.
     *
     * @pre true
     * @post RESULT == roundFinished
     */
    public boolean isRoundFinished() {
        boolean rf = roundFinished;
        assert rf == roundFinished : "isRoundFinished must reflect internal flag";
        return rf;
    }

    /**
     * Current (single) player.
     *
     * @pre true
     * @post RESULT != null
     */
    public Player getCurrentPlayer() {
        Player p = players.get(turn.getActiveIndex());
        Objects.requireNonNull(p);
        return p;
    }

    /**
     * Dealer.
     *
     * @pre true
     * @post RESULT != null && RESULT.isDealer()
     */
    public Player getDealer() {
        Player d = dealer;
        assert d != null && d.isDealer() : "Dealer invariant violated";
        return d;
    }

    /**
     * All players (excluding dealer).
     *
     * @pre true
     * @post RESULT != null && !RESULT.isEmpty()
     */
    public List<Player> getPlayers() {
        List<Player> ps = players;
        assert ps != null && !ps.isEmpty() : "Players invariant violated";
        return ps;
    }

    /**
     * Indicates if deck has enough cards for initial 4-card deal.
     *
     * @pre true
     * @post RESULT == (deck.size() < 4)
     */
    public boolean isDeckEmptyForInitialDeal() {
        boolean empty = deck.size() < 4;
        assert empty == (deck.size() < 4) : "isDeckEmptyForInitialDeal must be consistent";
        return empty;
    }

    private void dealToPlayer(Player player) throws DeckEmptyException {
        Objects.requireNonNull(player, "player must not be null");
        Card c = deck.deal();
        player.receiveCard(c);
    }

    private void dealToDealer() throws DeckEmptyException {
        Card c = deck.deal();
        dealer.receiveCard(c);
    }

    /**
     * Checks invariants.
     */
    private void checkInvariant() {
        assert deck != null : "deck must not be null";
        assert dealer != null : "dealer must not be null";
        assert players != null : "players list must not be null";
        assert !players.isEmpty() : "there must be at least one player";
        assert turn != null : "turn must not be null";
    }

    /**
     * Possible winners.
     */
    public enum Winner {
        PLAYER,
        DEALER,
        PUSH
    }
}