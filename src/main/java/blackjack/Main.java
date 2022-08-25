package blackjack;

import java.io.FileNotFoundException;
import java.util.Optional;

/** Main has a main method that handles command line arguments and may start a game of Blackjack. */
public final class Main {

    private static final String VERSION_STRING = "blackjack 1.0.0";

    private static final String USAGE =
            "Usage: blackjack [ flags ] FILE\n"
                    + "\n"
                    + "The BasicOptimized strategy is used by default for Sam.\n"
                    + "\n"
                    + "Flags:\n"
                    + "-b | --basicopt       Use the BasicOptimized strategy for Sam. (default)\n"
                    + "-2 | --second         Use the SecondOptimized strategy for Sam.\n"
                    + "-3 | --third          Use the Third strategy for Sam.\n"
                    + "-a | --always-hit     Use a strategy where Sam always hits.\n"
                    + "-s | --always-stay    Use a strategy where Sam always stays.\n"
                    + "-t | --test           Quickly test the current strategy.\n"
                    + "-n | --noshuffle      Don't shuffle the cards.\n"
                    + "-o | --optimize       Optimize the parameters of the chosen strategy.\n"
                    + "-r | --random         Randomize parameters when optimizing them.\n"
                    + "-h | --help           Output this help.\n"
                    + "-v | --verbose        Output detailed information about the games.\n"
                    + "--version             Output the current version number.\n";

    public static void main(String[] args) {
        try {

            ParsedFlagsAndArguments pa = new ParsedFlagsAndArguments(args, USAGE);

            if (pa.hasFlags("-h", "--help")) {
                System.out.println(USAGE);
                return;
            }
            if (pa.hasFlag("--version")) {
                System.out.println(VERSION_STRING);
                return;
            }

            final boolean useTheBasicStrategy = pa.hasFlags("-b", "--basicopt");
            final boolean useTheSecondStrategy = pa.hasFlags("-2", "--second");
            final boolean useTheThirdStrategy = pa.hasFlags("-3", "--third");
            final boolean useTheHitStrategy = pa.hasFlags("-a", "--always-hit");
            final boolean useTheStayStrategy = pa.hasFlags("-s", "--always-stay");

            final boolean quickTest = pa.hasFlags("-t", "--test");
            final boolean noShuffle = pa.hasFlags("-n", "--noshuffle");
            final boolean optimize = pa.hasFlags("-o", "--optimize");
            final boolean randomize = pa.hasFlags("-r", "--random");
            final boolean verbose = pa.hasFlags("-v", "--verbose");

            Strategy strat = new BasicStrategyOptimized();
            if (useTheBasicStrategy) {
                // already using the BasicStrategyOptimized strategy
            } else if (useTheSecondStrategy) {
                strat = new SecondStrategyOptimized();
            } else if (useTheThirdStrategy) {
                strat = new ThirdStrategy();
            } else if (useTheHitStrategy) {
                strat = new AlwaysHitStrategy();
            } else if (useTheStayStrategy) {
                strat = new AlwaysStayStrategy();
            }

            // Try to optimize the parameters of the current strategy?

            if (optimize) {
                // maxIterations is only used if parameters are randomized, and not iterated over
                final int maxIterations = 70000;

                Optimizer.run(strat, 64, 256, 1024, randomize, maxIterations);
                return;
            }

            // Run a quick test to confirm the win ratio for the current strategy?

            if (quickTest) {
                final int iterations = 420000;
                final double winPercentage = Optimizer.getWinRatio(strat, iterations) * 100.0;
                System.out.printf("Current strategy: %s\n", strat);
                System.out.printf(
                        "After %d iterations, Sam wins %f%% of the rounds.\n",
                        iterations, winPercentage);
                return;
            }

            // Play a round of Blackjack

            Optional<String> maybeFilename = pa.firstArg();
            Game game = new Game(maybeFilename, strat, verbose, !noShuffle);
            System.out.println(game.summary(game.oneRound(strat)));

        } catch (CardParseException
                | FileNotFoundException
                | OutOfCardsException
                | UnrecognizedFlagException ex) {

            System.err.println(ex);
            System.exit(1);
        }
    }
}
