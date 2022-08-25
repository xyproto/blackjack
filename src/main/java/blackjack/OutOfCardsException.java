package blackjack;

/**
 * OutOfCardsException should only be thrown if it's not possible to draw cards, even after renewing
 * the deck.
 */
public final class OutOfCardsException extends Exception {

    OutOfCardsException(String msg) {
        super(msg);
    }
}
