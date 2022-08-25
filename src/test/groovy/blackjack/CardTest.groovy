import spock.lang.Specification
import blackjack.*

class CardTest extends Specification {

    def "card H5 scores 5"() {
        setup:
        def card = new Card("H5")

        when:
        def result = card.score()

        then:
        result == 5
    }

    def "ace of spades scores 11"() {
        setup:
        def card = new Card("SA")

        when:
        def result = card.score()

        then:
        result == 11
    }

    def "ASDF is not a valid card code"() {
        when:
        def card = new Card("ASDF")

        then:
        thrown CardParseException
    }

    def "X7 is not a valid card code"() {
        when:
        def card = new Card("X7")

        then:
        thrown CardParseException
    }

    def "king scores 10"() {
        setup:
        def card = new Card(Card.Suite.HEARTS, Card.Value.KING)

        when:
        def result = card.score()

        then:
        result == 10
    }

    def "king of hearts is HK"() {
        setup:
        def card = new Card(Card.Suite.HEARTS, Card.Value.KING)

        when:
        def result = card.toString()

        then:
        result == "HK"
    }

}
