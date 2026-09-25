package blackjack;

import java.util.Objects;

/**
 * Simple turn manager for player index.
 *
 * @invariant playerCount >= 1
 * @invariant 0 <= activeIndex && activeIndex < playerCount
 */
public class Turn {
    private final int playerCount;
    private int activeIndex;

    /**
     * Constructs a turn manager.
     *
     * @pre playerCount >= 1
     * @post getActiveIndex() == 0
     */
    public Turn(int playerCount) {
        if (playerCount < 1) {
            throw new IllegalArgumentException("playerCount must be >= 1");
        }
        this.playerCount = playerCount;
        this.activeIndex = 0;
        checkInvariant();
        assert getActiveIndex() == 0 : "Initial active index must be 0";
    }

    /**
     * Advances to the next player.
     *
     * @pre true
     * @post getActiveIndex() == (oldIndex + 1) % getPlayerCount()
     */
    public void next() {
        int oldIndex = activeIndex;
        activeIndex = (activeIndex + 1) % playerCount;
        checkInvariant();
        assert activeIndex == (oldIndex + 1) % playerCount :
                "next() must advance active index by one modulo playerCount";
    }

    /**
     * Current active player index.
     *
     * @pre true
     * @post RESULT >= 0 && RESULT < getPlayerCount()
     */
    public int getActiveIndex() {
        int idx = activeIndex;
        assert idx >= 0 && idx < playerCount : "Active index must be within [0, playerCount)";
        return idx;
    }

    /**
     * Total number of players.
     *
     * @pre true
     * @post RESULT >= 1
     */
    public int getPlayerCount() {
        int pc = playerCount;
        assert pc >= 1 : "playerCount invariant violated";
        return pc;
    }

    /**
     * Checks invariants.
     */
    private void checkInvariant() {
        assert playerCount >= 1 : "There must be at least one player";
        assert activeIndex >= 0 && activeIndex < playerCount :
                "Active index out of range";
    }
}