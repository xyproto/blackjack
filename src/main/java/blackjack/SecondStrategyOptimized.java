package blackjack;

/** SecondStrategyOptimized implements a new Blackjack strategy with optimized parameters. */
public final class SecondStrategyOptimized implements Strategy {

    // These values were found by running the program with the -s -o flags
    private double a = 3.728042;
    private double b = 4.422105;
    private double c = 12.990700;

    @Override
    public final boolean shouldHit(Hand hand, Card dealerUpcard) {
        return hand.score() * a + dealerUpcard.score() * b > c;
    }

    @Override
    public void resetParameters() {
        a = 0; // 0 .. 13
        b = 0; // 0 .. 9
        c = 0; // 0 .. 19
    }

    private final double rDouble(final double min, final double max) {
        return (Math.random() * (max - min)) + min;
    }

    private void setRandomParameters() {
        a = rDouble(0, 13);
        b = rDouble(0, 9);
        c = rDouble(0, 19);
    }

    /*
     * nextSetting iterates over all the parameters
     *
     * @return true when the largest numbers has been reached.
     */
    @Override
    public final boolean nextParameter(final boolean randomValues) {
        if (randomValues) {
            setRandomParameters();
            return true;
        }
        c += 0.15;
        if (c >= 19.0) {
            c = 0;
            b += 0.15;
            if (b >= 9.0) {
                b = 0;
                a += 0.15;
                if (a >= 13.0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public final int possibilities() {
        // it's really 6.6 and not 7, but it's close enough for this purpose
        return 7 * 13 * 7 * 9 * 7 * 19;
    }

    @Override
    public final SecondStrategyOptimized copy() {
        SecondStrategyOptimized strat = new SecondStrategyOptimized();
        strat.a = a;
        strat.b = b;
        strat.c = c;
        return strat;
    }

    /**
     * Create a string representation.
     *
     * @return a strategy name + the current parameters
     */
    @Override
    public final String toString() { // return a name
        return String.format("S (%f, %f, %f)", a, b, c);
    }
}
