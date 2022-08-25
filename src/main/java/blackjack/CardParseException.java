package blackjack;

/** CardParseException is thrown if a string like "H7" can not be parsed into a Card class */
public final class CardParseException extends Exception {

    CardParseException(String msg) {
        super(msg);
    }
}
