package blackjack;

/**
 *
 * @pre rank ei saa olla null && suit ei saa olla null
 * @post olio luotu
 */
public record Card(
    Rank rank,
    Suit suit
){}

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