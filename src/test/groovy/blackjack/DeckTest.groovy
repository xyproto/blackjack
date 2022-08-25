import spock.lang.Specification
import blackjack.*

class DeckTest extends Specification {

    def "create a full deck of 52 cards"() {
        setup:
        def deck = new Deck()

        when:
        def result = deck.count()

        then:
        result == 52
    }

    def "draw a card from a new deck of cards"() {
        setup:
        def deck = new Deck()

        when:
        def maybeCard = deck.draw()

        then:
        maybeCard.isPresent()
    }


    def "pop a card and count the remaining ones"() {
        setup:
        def deck = new Deck()

        when:
        deck.draw()
        def count = deck.count()

        then:
        count == 51
    }

    def "pop a card and count the score + the total score"() {
        setup:
        def deck = new Deck()

        when:
        def cardScore = deck.draw().get().score()
        def deckScore = deck.score()

        then:
        cardScore + deckScore == 380
    }

}
