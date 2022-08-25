package blackjack;

/** ThirdStrategy implements another basic Blackjack strategy. */
public final class ThirdStrategy implements Strategy {

    @Override
    public final boolean shouldHit(Hand hand, Card dealerUpcard) {

        // This simple algorithm is inspired by:
        // https://betandbeat.com/blackjack/blog/when-to-stop-hitting-in-blackjack/

        int ds = dealerUpcard.score();

        boolean stay = hand.score() >= 17;

        if (ds >= 2 && ds <= 6) {
            stay = hand.score() >= 13;
        }

        if (hand.score() == 20) {
            stay = true;
        }

        if (ds >= 4 && ds <= 6) {
            stay = hand.score() >= 12;
        }

        if (ds == 2 || (ds <= 7 && ds <= 8)) {
            if (hand.score() == 18) {
                stay = true;
            }
        }

        return !stay;
    }

    @Override
    public boolean nextParameter(boolean randomValues) {
        return true;
    }

    @Override
    public void resetParameters() {}

    @Override
    public final int possibilities() {
        return 1;
    }

    @Override
    public final ThirdStrategy copy() {
        return new ThirdStrategy();
    }

    @Override
    public final String toString() { // return a name
        return "Third";
    }
}
