package blackjack;

/** Player is a Hand with a Strategy. It inherits from Hand which inherits from CardCollection. */
public final class Player extends Hand {

    public static final Strategy defaultStrategy = new BasicStrategyOptimized();

    private Strategy strategy;

    Player(Strategy strategy) {
        super();
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public final Strategy getStrategy() {
        return strategy;
    }

    public final boolean shouldHit(Card dealerUpcard) {
        return this.strategy.shouldHit(this, dealerUpcard);
    }
}
