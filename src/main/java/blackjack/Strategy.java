package blackjack;

/** Strategy is a Blackjack strategy. */
interface Strategy {

    /**
     * draw another card or stop ("hit or stand")
     *
     * @return true if another card should be drawn
     */
    public boolean shouldHit(Hand hand, Card dealerUpcard);

    /**
     * If the Strategy has parameters/settings, try the next one. Return false when the last
     * possible setting has been reached.
     */
    public boolean nextParameter(boolean randomValues);

    /** For returning the number of possible parameters, ref. the nextParameter method */
    public int possibilities();

    /** For resetting the parameters to their initial/lowest values */
    public void resetParameters();

    /** For making a copy / deep clone */
    public Strategy copy();

    /**
     * toString returns the name of this strategy
     *
     * @return the strategy name
     */
    public String toString(); // return a name
}
