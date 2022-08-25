package blackjack;

/** AlwaysHitStrategy implements a Blackjack strategy that tries to always draws a card. */
public final class AlwaysHitStrategy implements Strategy {

    @Override
    public final boolean shouldHit(Hand hand, Card dealerUpcard) {
        return true;
    }

    @Override
    public final String toString() {
        return "Always Hit";
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
    public final AlwaysHitStrategy copy() {
        return new AlwaysHitStrategy();
    }
}
