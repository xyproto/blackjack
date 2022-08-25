package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Optimizer {

    public static HashMap<Game.Result, Integer> stats = new HashMap<Game.Result, Integer>();

    /**
     * getWinRatio returns the win ratio of a benchmarked strategy
     *
     * @param strat is the Strategy to benchmark
     * @param nTimes is how many times a round should be played
     * @return the win ratio of SAM_WON vs DEALER_WON after having simulated nTimes games
     * @throws OutOfCardsException if there are less than 4 cards in the deck
     */
    public static final double getWinRatio(final Strategy strat, final int nTimes)
            throws OutOfCardsException {

        // Create a new game and set the strategy. The deck will be re-initialized as needed.
        Game game = new Game(strat);

        Game.Result winner;
        stats.clear();
        stats.put(Game.Result.DEALER_WON, 1); // start with 1 to avoid divide by zero
        stats.put(Game.Result.SAM_WON, 1);
        for (int i = 0; i < nTimes; i++) {
            winner = game.oneRound(strat);
            if (winner == Game.Result.PUSH) {
                // try again
                i--;
            } else {
                // record statistics
                stats.put(winner, stats.get(winner) + 1);
            }
            game.prepareNewRound();
        }
        // return the win ratio
        return stats.get(Game.Result.SAM_WON).doubleValue()
                / stats.get(Game.Result.DEALER_WON).doubleValue();
    }

    /**
     * run will run through all possible parameters for the given Strategy, and find the parameters
     * that gives the best win rate.
     *
     * <p>There are not that many possiblities, so brute force worked out for this case.
     *
     * <p>Each Strategy defines how many parameters they have and which value ranges they have.
     *
     * <p>The reason for having n, nSecondary and nTertiary is to be able to discover early if the
     * win ratio for a given set of parameters does not appear to be giving good result, and
     * continue to the next ones.
     *
     * @param strat the strategy to optimize
     * @param n is the number of rounds to play when initially looking for a better win ratio
     * @param nSecondary is the number of rounds to play after having simulated n rounds, if the win
     *     ratio is better.
     * @param nTertiary is the number of rounds to play after having simulated nSecondary rounds, if
     *     the win ratio is better.
     * @param randomValues is for trying random values for maxIterations instead of trying them out
     *     progressively
     * @param maxIterations is for limiting the iterations to a maximum number, set to 0 or -1 to
     *     ignore
     * @throws OutOfCardsException if there are less than 4 cards in the deck
     */
    public static void run(
            Strategy strat,
            final int n,
            final int nSecondary,
            final int nTertiary,
            final boolean randomValues,
            final int maxIterations)
            throws OutOfCardsException {

        int possibilities = maxIterations;
        if (maxIterations <= 0) {
            possibilities = strat.possibilities();
        }

        Strategy bestStrategySoFar = strat.copy();

        HashMap<Double, String> winratioToParams = new HashMap<Double, String>();
        HashMap<String, Integer> stats;

        strat.resetParameters();

        double bestRatioSoFar = getWinRatio(strat, n);

        final double anImprovement = 1.001; // 0.1% better is enough of an improvement to count

        double threshold = bestRatioSoFar * anImprovement;

        int counter = 0;
        double winRatio = 0;

        // iterate through all possible basic strategy parameters, as defined in the various classes
        // that implements the Strategy interface
        while (strat.nextParameter(randomValues)) {
            // Check if the win rate is the best so far after n simulated games
            winRatio = getWinRatio(strat, n);
            if (winRatio > threshold) { // there must be a 1% improvement to count
                // Check if the win rate is also better after a larger number of simulated games
                winRatio = getWinRatio(strat, nSecondary);
                if (winRatio > threshold) { // there must be a 1% improvement to count
                    // Check if the win rate is also better after an even larger number of simulated
                    // games
                    winRatio = getWinRatio(strat, nTertiary);
                    if (winRatio > threshold) { // there must be a 1% improvement to count
                        bestRatioSoFar = winRatio;
                        bestStrategySoFar = strat.copy();
                        threshold = bestRatioSoFar * anImprovement;
                    }
                }
            }
            System.out.printf(
                    "[%d/%d] best win ratio %f: %s, now at %f: %s, threshold %f\n",
                    counter,
                    possibilities,
                    bestRatioSoFar,
                    bestStrategySoFar.toString(),
                    winRatio,
                    strat.toString(),
                    threshold);

            winratioToParams.put(winRatio, strat.toString());
            counter++;

            if (randomValues && counter > maxIterations && maxIterations > 0) {
                break;
            }
        }

        ArrayList<Double> sortedKeys = new ArrayList<Double>(winratioToParams.keySet());
        Collections.sort(sortedKeys);

        for (Double k : sortedKeys) {
            String v = winratioToParams.get(k);
            System.out.printf("%f: %s\n", k, v);
        }
    }
}
