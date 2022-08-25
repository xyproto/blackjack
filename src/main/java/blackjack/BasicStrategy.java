package blackjack;

/** BasicStrategy implements a basic Blackjack strategy. */
public final class BasicStrategy implements Strategy {

    @Override
    public final boolean shouldHit(Hand hand, Card dealerUpcard) {

        int ds = dealerUpcard.score();

        // The basic strategy is based on information from "How to play Blackjack":
        // https://bicyclecards.com/how-to-play/blackjack/

        if (ds >= 7) {
            return hand.score() <= 17;
        }
        if (3 < ds && ds < 7) {
            return hand.score() < 12;
        }
        return hand.score() < 13;
    }

    @Override
    public void resetParameters() {}

    @Override
    public boolean nextParameter(boolean randomValues) {
        return false;
    }

    @Override
    public final int possibilities() {
        return 1;
    }

    @Override
    public final BasicStrategy copy() {
        return new BasicStrategy();
    }

    @Override
    public final String toString() { // return a name
        return "Basic";
    }
}
