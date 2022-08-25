package blackjack;

/** Hand is a player hand, that is empty by default but can hold cards as they are drawn. */
public class Hand extends CardCollection {

    Hand() {
        super(false); // don't start with any cards
    }
}
