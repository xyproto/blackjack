package blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;

/**
 * CardCollection is a generic collection of cards.
 *
 * <p>The Deck and Hand classes inherits from this class.
 */
public class CardCollection {

    // The cards that this CardCollection contains and represents
    private ArrayList<Card> cards;

    // If a new Deck was created, save a copy of the initial cards in case
    // the we runs out of cards and need a fresh set of cards.
    private ArrayList<Card> initialCards;

    // lastScore caches the score, to avoid re-calculating it needlessly
    private int lastScore;

    // has the cards been changed since last call to .score()?
    private boolean changed = true;

    // has the cards been shuffled?
    private boolean shuffled = false;

    /**
     * CardCollection constructs either an empty or a full deck of cards.
     *
     * @param fullDeck can be set to true to start with 52 unique cards
     */
    CardCollection(boolean fullDeck) {
        cards = new ArrayList<Card>();
        if (fullDeck) {
            // Generate and add 52 unique cards
            add52Cards();
        }
        // make a copy of the initial cards
        initialCards = new ArrayList<Card>(cards);
    }

    /** Convenience constructor for creating an empty collection of cards. */
    CardCollection() {
        this(false);
    }

    /**
     * CardCollection constructs a collection of cards based on the cards in the given filename.
     *
     * @param filename is the path to a file that contains a comma separated list of card strings
     */
    CardCollection(String filename) throws CardParseException, FileNotFoundException {
        final File deckFile = new File(filename);
        final Scanner scanner = new Scanner(deckFile);
        cards = new ArrayList<Card>();
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            addCards(line);
        }
        scanner.close();
    }

    /* add52 will generate and add a full deck of 52 unique cards */
    private void add52Cards() {
        for (Card.Suite suite : Card.Suite.values()) {
            for (Card.Value value : Card.Value.values()) {
                cards.add(new Card(suite, value));
            }
        }
    }

    /**
     * Add cards to the collection from a comma-separated list of cards.
     *
     * @param line is a comma-separated list of cards
     * @throws CardParseException if one of the card strings can not be parsed
     */
    public void addCards(String line) throws CardParseException {
        for (String code : line.split(",")) {
            cards.add(new Card(code.trim()));
            changed = true;
        }
        // make a copy of the cards
        initialCards = new ArrayList<Card>(cards);
    }

    /**
     * Modify the collection to only contain the given comma separated list of cards.
     *
     * @param line is a comma-separated list of cards
     * @throws CardParseException if one of the card strings can not be parsed
     */
    public void setCards(String line) throws CardParseException {
        clear();
        for (String code : line.split(",")) {
            cards.add(new Card(code.trim()));
            changed = true;
        }
        // make a copy of the cards
        initialCards = new ArrayList<Card>(cards);
    }

    /**
     * Pop a card from the top of the pile / start of the list. Does not re-initialize the deck.
     *
     * @return either a Card wrapped in an Optional, or an empty Optional if the collection is empty
     */
    public final Optional<Card> draw() {
        changed = true;
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        Optional<Card> maybeCard = Optional.of(cards.remove(0));
        return maybeCard;
    }

    /**
     * Pop a card from the given deck/hand/card collection and add it to this card collection.
     *
     * <p>If the deck is out of cards, an empty Optional will be returned.
     *
     * @param deck is a collection of cards runs out of cards
     * @return either a Card wrapped in an Optional, or an empty Optional if the collection is empty
     */
    public final Optional<Card> drawNoRenewal(CardCollection deck) {
        Optional<Card> maybeCard = deck.draw();
        if (maybeCard.isEmpty()) {
            return maybeCard;
        }
        cards.add(maybeCard.get());
        changed = true;
        return maybeCard;
    }

    /**
     * Pop a card from the given deck/hand/card collection and add it to this card collection.
     *
     * <p>If the deck is out of cards, a new deck will be created from the cards that were used in
     * the initial CardCollection constructor.
     *
     * @param deck is a collection of cards runs out of cards
     * @return either a Card wrapped in an Optional, or an empty Optional if the collection is empty
     * @throws OutOfCardsException if the deck is empty even after re-initializing it.
     */
    public final Optional<Card> draw(CardCollection deck) throws OutOfCardsException {
        Optional<Card> maybeCard = deck.draw();
        if (maybeCard.isEmpty()) {
            deck.renew();
            maybeCard = deck.draw();
            if (maybeCard.isEmpty()) {
                throw new OutOfCardsException("deck is empty after re-initializing it");
            }
        }
        cards.add(maybeCard.get());
        changed = true;
        return maybeCard;
    }

    /** Shuffle the cards */
    public void shuffle() {
        Collections.shuffle(cards);
        shuffled = true;
        changed = true;
    }

    /** Clear the current collection of cards. */
    public void clear() {
        cards.clear();
        changed = true;
    }

    /**
     * Calculate the total score for this collection of cards.
     *
     * <p>The score is cached in lastScore and re-used if the card collection has not changed.
     *
     * @return the total score
     */
    public final int score() {
        if (!changed) {
            return lastScore;
        }
        int sum = 0;
        for (Card card : cards) {
            sum += card.score();
        }
        changed = false;
        lastScore = sum;
        return sum;
    }

    /**
     * Check if the current card collection is empty.
     *
     * @return true if it is empty
     */
    public final boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Count the cards.
     *
     * @return the number of cards in this collection
     */
    public final int count() {
        return cards.size();
    }

    /**
     * Check if the card collection has a total score of 21.
     *
     * @return true if this is blackjack
     */
    public final boolean blackjack() {
        return score() == 21;
    }

    /** Re-initialize the cards. */
    public final void renew() {
        if (!initialCards.isEmpty()) {
            // Make a copy of the initial cards
            cards = new ArrayList<Card>(initialCards);
        } else {
            // Generate and add 52 unique cards
            add52Cards();
        }
        if (shuffled) {
            shuffle();
        }
        changed = true;
    }

    /** Return the current collection of cards as a comma separated string of cards. */
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(cards.get(i).toString());
        }
        return sb.toString();
    }
}
