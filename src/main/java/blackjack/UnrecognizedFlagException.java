package blackjack;

/**
 * If an unrecognized flag is passed in on the command line, then this exception might be thrown.
 */
public final class UnrecognizedFlagException extends Exception {

    UnrecognizedFlagException(String msg) {
        super(msg);
    }
}
