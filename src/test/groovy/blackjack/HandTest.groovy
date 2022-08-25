import spock.lang.Specification
import blackjack.*

class HandTest extends Specification {

    def "create an empty hand"() {
        setup:
        def hand = new Hand()

        when:
        def result = hand.isEmpty()

        then:
        result == true
    }

    def "create another empty hand"() {
        setup:
        def hand = new Hand()

        when:
        def result = hand.count()

        then:
        result == 0
    }

    def "draw 52 cards and score them"() {
        setup:
        def hand = new Hand()
        def deck = new Deck()

        when:
        for (def i=0;i<52;i++) {
            hand.draw(deck)
        }
        def totalScore = hand.score()

        then:
        totalScore == 380
    }




}
