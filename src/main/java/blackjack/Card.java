package blackjack;

import java.util.HashMap;
import java.util.Optional;

/** Card is a playing card with a suite and a value. */
public final class Card {

    /** Suite is an enum that represents a card suite, like "hearts". */
    public enum Suite {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES
    }

    /** Value is an enum that represents a card value, like "7" or "Ace". */
    public enum Value {
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE
    }

    private final Suite suite;
    private final Value value;

    private static final HashMap<Suite, String> suiteTable = new HashMap<Suite, String>();
    private static final HashMap<Value, String> valueTable = new HashMap<Value, String>();

    static {
        // Create a lookup table from the card Suite enum to the card Suite string (like "H")
        suiteTable.put(Suite.CLUBS, "C");
        suiteTable.put(Suite.DIAMONDS, "D");
        suiteTable.put(Suite.HEARTS, "H");
        suiteTable.put(Suite.SPADES, "S");

        // Create a lookup table from the card Value enum to the card Value string (like "J")
        valueTable.put(Value.TWO, "2");
        valueTable.put(Value.THREE, "3");
        valueTable.put(Value.FOUR, "4");
        valueTable.put(Value.FIVE, "5");
        valueTable.put(Value.SIX, "6");
        valueTable.put(Value.SEVEN, "7");
        valueTable.put(Value.EIGHT, "8");
        valueTable.put(Value.NINE, "9");
        valueTable.put(Value.TEN, "10");
        valueTable.put(Value.JACK, "J");
        valueTable.put(Value.QUEEN, "Q");
        valueTable.put(Value.KING, "K");
        valueTable.put(Value.ACE, "A");
    }

    // Search for a given string value and return the corresponding enum key
    private <T> Optional<T> lookupByValue(HashMap<T, String> lookup, String lookingFor) {
        for (T enumKey : lookup.keySet()) {
            if (lookup.get(enumKey).equals(lookingFor)) {
                return Optional.of(enumKey);
            }
        }
        return Optional.empty();
    }

    /**
     * Take a 2 or 3 letter string like "H7" or "S10" and construct a Card instance.
     *
     * <p>Valid suites: C, D, H, S Valid values: 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A
     *
     * @param code is the card string, with a suite followed by a value
     * @throws CardParseException if the given string is not a valid card string
     */
    Card(String code) throws CardParseException {
        final int len = code.length();
        if (len < 2 || len > 3) {
            throw new CardParseException(String.format("invalid length: %d (%s)", len, code));
        }

        // check if the given card suite can be found in the enum lookup table
        final String givenSuiteCode = code.substring(0, 1).toUpperCase();
        Optional<Suite> maybeSuiteEnum = lookupByValue(suiteTable, givenSuiteCode);
        if (maybeSuiteEnum.isEmpty()) {
            throw new CardParseException("invalid card suite: " + givenSuiteCode);
        }

        // check if the given card value can be found in the enum lookup table
        final String givenValueCode = code.substring(1).toUpperCase();
        Optional<Value> maybeValueEnum = lookupByValue(valueTable, givenValueCode);
        if (maybeValueEnum.isEmpty()) {
            throw new CardParseException("invalid card value: " + givenValueCode);
        }

        // set the card suite and card value enums, since the card string is valid
        suite = maybeSuiteEnum.get();
        value = maybeValueEnum.get();
    }

    /**
     * Take a card suite and a card value and construct a Card instance.
     *
     * @param suite is the card suite, from the Suite enum
     * @param value is the card value, from the Value enum
     */
    Card(Suite suite, Value value) {
        this.suite = suite;
        this.value = value;
    }

    /**
     * Score the current card by looking at the card value (and not the suite).
     *
     * @return the score, from 2 to 11
     */
    public final int score() {
        switch (value) {
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            case TEN:
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            case ACE:
                return 11;
            default:
                // The code does not reach here, this is just to make the compiler happy
                return 0;
        }
    }

    /**
     * Generate a card string like "H7".
     *
     * @return a string on the form "suite + value"
     */
    public final String toString() {
        return suiteTable.get(suite) + valueTable.get(value);
    }
}
