import spock.lang.Specification
import blackjack.*

class CardCollectionTest extends Specification {

    def "create an empty deck"() {
        setup:
        def cc = new CardCollection()

        when:
        def result = cc.isEmpty()

        then:
        result == true
    }

    def "create 52 cards"() {
        setup:
        def cc = new CardCollection(true)

        when:
        def result = cc.count();

        then:
        result == 52
    }

    def "list a deck of cards"() {
        setup:
        def cc = new CardCollection(true)

        when:
        def result = cc.toString().split(",").size();

        then:
        result == 52
    }

    def "open a file that does not exist"() {
        when:
        def cc = new CardCollection("/asdf/qwerty/card.txt")

        then:
        thrown FileNotFoundException
    }

    def "re-create a card collection via a string"() {
        setup:
        def cc = new CardCollection(true)
        cc.shuffle()

        when:
        def s1 = cc.toString()
        cc.setCards(s1)
        def s2 = cc.toString()

        then:
        s1 == s2
    }

    def "create a blackjack situation"() {
        setup:
        def cc = new CardCollection()
        cc.addCards("HK, SA")

        when:
        def blackjack = cc.blackjack()

        then:
        blackjack == true
    }

    def "score an empty deck of cards"() {
        setup:
        def cc = new CardCollection()

        when:
        def score = cc.score()

        then:
        score == 0
    }

    def "score a deck of a single card"() {
        setup:
        def cc = new CardCollection()
        cc.addCards("SA")

        when:
        def score = cc.score()

        then:
        score == 11 // ace of spades
    }

    def "score a full deck of 52 cards"() {
        setup:
        def cc = new CardCollection(true)

        when:
        def score = cc.score()

        then:
        score == 380
    }

}
