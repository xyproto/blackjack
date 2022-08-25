package blackjack;

/** BasicStrategyOptimized implements a basic Blackjack strategy, but with optimized parameters. */
public final class BasicStrategyOptimized implements Strategy {

    // These values were found by running the program with the -o flag
    private int upperGoodScoreLimit = 4;
    private int lowerGoodScoreLimit = 3;
    private int stopDrawingLimit1 = 20;
    private int stopDrawingLimit2 = 5;
    private int stopDrawingLimit3 = 14;

    @Override
    public final boolean shouldHit(Hand hand, Card dealerUpcard) {
        final int ds = dealerUpcard.score();
        if (ds >= upperGoodScoreLimit) {
            return hand.score() < stopDrawingLimit1;
        }
        if (lowerGoodScoreLimit < ds && ds < upperGoodScoreLimit) {
            return hand.score() < stopDrawingLimit2;
        }
        return hand.score() < stopDrawingLimit3;
    }

    @Override
    public void resetParameters() {
        upperGoodScoreLimit = 3; // ..11
        lowerGoodScoreLimit = 3; // ..11
        stopDrawingLimit1 = 16; // ..21
        stopDrawingLimit2 = 0; // ..21
        stopDrawingLimit3 = 2; // ..17
    }

    private final int rInt(final int min, final int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void setRandomParameters() {
        upperGoodScoreLimit = rInt(3, 11);
        lowerGoodScoreLimit = rInt(3, 11);
        stopDrawingLimit1 = rInt(16, 21);
        stopDrawingLimit2 = rInt(0, 21);
        stopDrawingLimit3 = rInt(2, 17);
    }

    /*
     * nextSetting iterates over all the parameters, from 3,3,16,0,2 to 11,11,21,21,17
     *
     * resetParameters should be called to set the parameters to their lowest values before starting.
     *
     * Note that the parameters in the method are listed in the reverse order from resetParameters.
     *
     * @param randomValues can be set to true to use random parameters instead
     * @return true when the upper values have been reached.
     */
    @Override
    public final boolean nextParameter(final boolean randomValues) {
        if (randomValues) {
            setRandomParameters();
            return true;
        }
        stopDrawingLimit3++;
        if (stopDrawingLimit3 >= 17) {
            stopDrawingLimit3 = 2;
            stopDrawingLimit2++;
            if (stopDrawingLimit2 >= 21) {
                stopDrawingLimit2 = 0;
                stopDrawingLimit1++;
                if (stopDrawingLimit1 >= 21) {
                    stopDrawingLimit1 = 16;
                    lowerGoodScoreLimit++;
                    if (lowerGoodScoreLimit >= 11) {
                        lowerGoodScoreLimit = 3;
                        upperGoodScoreLimit++;
                        if (upperGoodScoreLimit >= 11) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public final int possibilities() {
        return 8 * 8 * 5 * 21 * 15;
    }

    @Override
    public final BasicStrategyOptimized copy() {
        BasicStrategyOptimized strat = new BasicStrategyOptimized();
        strat.upperGoodScoreLimit = upperGoodScoreLimit;
        strat.lowerGoodScoreLimit = lowerGoodScoreLimit;
        strat.stopDrawingLimit1 = stopDrawingLimit1;
        strat.stopDrawingLimit2 = stopDrawingLimit2;
        strat.stopDrawingLimit3 = stopDrawingLimit3;
        return strat;
    }

    /**
     * Create a string representation.
     *
     * @return a strategy name + the current parameters
     */
    @Override
    public final String toString() {
        return String.format(
                "B (%d,%d,%d,%d,%d)",
                upperGoodScoreLimit,
                lowerGoodScoreLimit,
                stopDrawingLimit1,
                stopDrawingLimit2,
                stopDrawingLimit3);
    }
}
