package blackjack;

import java.io.FileNotFoundException;

/**
 * Deck is a collection of cards that is a full deck of 52 cards by default, and can be drawn from.
 */
public final class Deck extends CardCollection {

    Deck() {
        super(true); // start with 52 random cards
    }

    Deck(String filename) throws FileNotFoundException, CardParseException {
        super(filename);
    }
}
