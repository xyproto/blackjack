import spock.lang.Specification
import blackjack.*

class GameTest extends Specification {

    def "create a game from a file that does not exist"() {
        when:
        def game = new Game("/asdf/qwerty/card.txt", false, false)

        then:
        thrown FileNotFoundException
    }

    def "create and run a random game"() {
        setup:
        def game = new Game()

        when:
        def winner = game.oneRound()

        then:
        winner == Game.Result.SAM_WON || winner == Game.Result.DEALER_WON || winner == Game.Result.PUSH
    }

    def "create and run a game with a specific deck of cards"() {
        setup:
        def deck = new Deck()
        deck.setCards("CA, D5, H9, HQ, S8")
        def game = new Game(deck, false, false) // new game, not verbose, no shuffle

        when:
        def winner = game.oneRound()

        then:
        winner == Game.Result.SAM_WON
    }

    def "create and run a game where sam wins"() {
        setup:
        def deck = new Deck()
        // sam draws card 1 and 3 for blackjack, dealer draws 2 and 4 for 4 points
        deck.setCards("HK, C2, SA, D2")
        def game = new Game(deck, true, false) // new game, verbose, no shuffle

        when:
        def winner = game.oneRound()

        then:
        winner == Game.Result.SAM_WON
    }


}
