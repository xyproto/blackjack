package blackjack;

/** AlwaysStayStrategy implements a Blackjack strategy that never tries to draw a card. */
public final class AlwaysStayStrategy implements Strategy {

    @Override
    public final boolean shouldHit(Hand hand, Card dealerUpcard) {
        return false;
    }

    @Override
    public final String toString() {
        return "Always Stay";
    }

    @Override
    public boolean nextParameter(boolean randomValues) {
        return false;
    }

    @Override
    public final void resetParameters() {}

    @Override
    public final int possibilities() {
        return 1;
    }

    @Override
    public final AlwaysStayStrategy copy() {
        return new AlwaysStayStrategy();
    }
}
