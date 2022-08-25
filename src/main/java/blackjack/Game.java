package blackjack;

import java.io.FileNotFoundException;
import java.util.Optional;

public final class Game {

    /** verbose can be set to true to print additional output when the game is running */
    private final boolean verbose;

    // shuffle can be set to false keep the deck untouched when a new round starts
    private final boolean shuffle;

    // the main elements of the game: the deck of cards, hand of the dealer and hand of the player
    private final Deck deck;
    private final Dealer dealer;
    private final Player sam;

    /**
     * Construct a game of blackjack. Sam is the player and the dealer deals the cards.
     *
     * @param deck is a deck of cards to start with.
     * @param strat is the strategy that Sam will be using, to "hit" or "stay".
     * @param verbose is if extra output should be written to stdout as the game is progressing.
     * @param shuffle is if the cards should be shuffled when the game starts, or not.
     */
    Game(Deck deck, Strategy strat, boolean verbose, boolean shuffle) {
        this.verbose = verbose;
        this.shuffle = shuffle;

        dealer = new Dealer();
        sam = new Player(strat);

        if (shuffle) {
            deck.shuffle();
        }
        this.deck = deck;
    }

    /**
     * Construct a game of blackjack. Sam is the player and the dealer deals the cards.
     *
     * @param maybeDeckFilename is an optional filename for a comma separated list of cards to start
     *     with.
     * @param strat is the strategy that Sam will be using, to "hit" or "stay".
     * @param verbose is if extra output should be written to stdout as the game is progressing.
     * @param shuffle is if the cards should be shuffled when the game starts, or not.
     */
    Game(Optional<String> maybeDeckFilename, Strategy strat, boolean verbose, boolean shuffle)
            throws CardParseException, FileNotFoundException {
        this.verbose = verbose;
        this.shuffle = shuffle;

        dealer = new Dealer();
        sam = new Player(strat);

        if (maybeDeckFilename.isEmpty()) {
            // No optional filename was given, create a new deck of 52 cards
            Deck deck = new Deck();
            if (shuffle) {
                deck.shuffle();
            }
            this.deck = deck;
            return;
        }

        // A filename was given
        final String filename = maybeDeckFilename.get();
        Deck deck = new Deck(filename);
        if (verbose) {
            System.out.printf("Loaded deck from %s\n", filename);
        }

        if (shuffle) {
            deck.shuffle();
        }
        this.deck = deck;
    }

    Game() {
        this(new Deck(), Player.defaultStrategy, false, true);
    }

    Game(Strategy strat) {
        this(new Deck(), strat, false, true);
    }

    Game(final String deckFilename, boolean verbose, boolean shuffle)
            throws CardParseException, FileNotFoundException {
        this(Optional.of(deckFilename), Player.defaultStrategy, verbose, shuffle);
    }

    Game(Deck deck, boolean verbose, boolean shuffle)
            throws CardParseException, FileNotFoundException {
        this(deck, Player.defaultStrategy, verbose, shuffle);
    }

    public final Strategy getStrategy() {
        return sam.getStrategy();
    }

    // return a summary of the game containing the winner and the cards that everyone are holding
    public final String summary(final Result winner) {
        StringBuilder sb = new StringBuilder();
        switch (winner) {
            case SAM_WON:
                sb.append("sam\n");
                break;
            case DEALER_WON:
                sb.append("dealer\n");
                break;
            case PUSH:
                sb.append("push\n");
                break;
        }
        sb.append("sam: " + sam.toString() + "\n");
        sb.append("dealer: " + dealer.toString());
        return sb.toString();
    }

    // vmsg will write a message to stdout, if verbose is true
    private void vmsg(String msg) {
        if (!verbose) {
            return;
        }
        System.out.println(msg);
    }

    // vscore will write the score of both players and a message to stdout, if verbose is true
    private void vscore(String msg) {
        if (!verbose) {
            return;
        }
        System.out.printf(
                "Sam: %d points, Dealer: %d points. %s\n", sam.score(), dealer.score(), msg);
    }

    public enum Result {
        SAM_WON,
        DEALER_WON,
        PUSH // it's a tie
    }

    /**
     * oneRound will simulate one round of blackjack.
     *
     * @param strat is the strategy that Sam should use
     * @return the result of the round, either SAM_WON, DEALER_WON or (in the case of a tie) PUSH
     * @throws OutOfCardsException if there are fewer than 4 cards in the deck
     */
    public final Result oneRound(final Strategy strat) throws OutOfCardsException {

        if (deck.count() < 4) {
            deck.renew();
        }

        vmsg("Starting a round of Blackjack");
        vmsg("Deck: " + deck + " (shuffled: " + shuffle + ")");

        if (deck.count() < 4) {
            throw new OutOfCardsException(
                    "too few cards in the deck to start a round of Blackjack");
        }

        // Let Sam draw a card, and try to re-initialize the deck if needed
        sam.draw(deck);

        // Let the dealer draw a card, and try to re-initialize the deck if needed
        dealer.draw(deck);

        vmsg("Sam cards: " + sam);
        vmsg("Dealer cards: " + dealer);

        // Let Sam draw a card, and try to re-initialize the deck if needed
        sam.draw(deck);

        // Let the dealer draw a card, and try to re-initialize the deck if needed
        Optional<Card> maybeDealerUpcard = dealer.draw(deck);

        // Now we know that it is not empty
        Card dealerUpcard = maybeDealerUpcard.get();

        vmsg("Sam cards: " + sam);
        vmsg("Dealer cards: " + dealer);
        vmsg("Initial draw is complete.");

        if (sam.blackjack()) {
            vmsg("Sam has blackjack. Regardless of what the dealer may have, Sam won.");
            return Result.SAM_WON;
        }

        if (sam.score() == 22 && dealer.score() == 22) {
            vmsg("Both have two aces. The dealer won.");
            return Result.DEALER_WON;
        }

        // Initialize the strategies

        if (verbose) {
            System.out.printf("Sam uses the %s strategy.\n", sam.getStrategy());
        }

        // Sam's turn to draw cards

        while (sam.score() < 17) {

            // should Sam stay or hit?
            if (!sam.shouldHit(dealerUpcard)) {
                // Sam stays
                break;
            }

            // Let Sam draw a card, and try to re-initialize the deck if needed
            sam.draw(deck);

            vmsg("Sam cards: " + sam);
            if (sam.score() > 21) {
                vscore("The dealer won because Sam scored higher than 21.");
                return Result.DEALER_WON;
            }
        }

        // Dealer's turn to draw cards

        while (dealer.score() < sam.score()) {
            // the dealer draws cards whenever possible

            // Let the Dealer draw a card, and try to re-initialize the deck if needed
            dealer.draw(deck);

            vmsg("Dealer cards: " + dealer);
            if (dealer.score() > 21) {
                vscore("Sam won because the dealer scored higher than 21.");
                return Result.SAM_WON;
            }
        }

        // Check the final score now that the card drawing is complete

        if (sam.score() > dealer.score()) {
            vscore("Sam won on points.");
            return Result.SAM_WON;
        }

        if (sam.score() < dealer.score()) {
            vscore("The dealer won on points.");
            return Result.DEALER_WON;
        }

        // sam.score() == dealer.score()
        vscore("It's a push.");
        return Result.PUSH; // nobody lost or won
    }

    /** prepareNewRound keeps the same deck, but clears the hand of both the dealer and of Sam */
    public void prepareNewRound() {
        dealer.clear();
        sam.clear();
    }
}
