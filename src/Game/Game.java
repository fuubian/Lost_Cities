package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private List<Card> deck;
    private List<Card> player1Cards;
    private List<Card> player2Cards;

    /*
    0-4: Player 1's Played Expedition Cards
    0: Red
    1: Green
    2: Blue
    3: White
    4: Yellow
    5-9: Player 2's Played Expedition Cards
     */
    private List<List<Card>> field;

    /*
    0: Red
    1: Green
    2: Blue
    3: White
    4: Yellow
     */
    private List<List<Card>> discardedCards;

    private int blockedCard;


    /*
    Gamestates:
    0 - Player 1's turn to play card
    1 - Player 1's turn to take card
    2 - Player 2's turn to play card
    3 - Player 2's turn to take card
    4 - Game is finished
     */
    private int state;

    public Game() {
        this.createCards();
        this.state = 0;
        this.blockedCard = -1;
    }

    public boolean executeMove(PlayMove move) {
        Card card;
        int added;

        if (move.getPlayer() == 1 && this.state == 0) {
            card = this.player1Cards.get(move.getCard() - 1);
            added = 0;
        }
        else if (move.getPlayer() == 2 && this.state == 2) {
            card = this.player2Cards.get(move.getCard() - 1);
            added = 5;
        }
        else {
            return false;
        }

        if (move.getTarget() == 1) {    // field
            if (field.get(card.getColor() + added).size() > 0) {
                if (field.get(card.getColor() + added).get(field.get(card.getColor() + added).size()-1).getValue() <= card.getValue()) {
                    this.field.get(card.getColor()+added).add(card);
                } else {
                    return false;
                }
            } else {
                this.field.get(card.getColor() + added).add(card);
            }
        } else if (move.getTarget() == 2) { // discarded
            this.discardedCards.get(card.getColor()).add(card);
            this.blockedCard = card.getColor(); // the card isn't allowed to be taken in the same move
        }

        if (move.getPlayer() == 1) {
            this.player1Cards.remove(move.getCard() - 1);
            this.state = 1;
        }
        else {
            this.player2Cards.remove(move.getCard() - 1);
            this.state = 3;
        }
        return true;
    }

    public boolean executeMove (TakeMove move) {
        int target = move.getTarget();

        if (!(move.getPlayer() == 1 && this.state == 1) && !(move.getPlayer() == 2 && this.state == 3)) {
            return false;
        }

        if (target == 5) {  // stack
            if (move.getPlayer() == 1) {
                this.player1Cards.add(this.deck.get(this.deck.size()-1));
            } else {
                this.player2Cards.add(this.deck.get(this.deck.size()-1));
            }
            this.deck.remove(this.deck.size()-1);
        } else {
            if (this.discardedCards.get(target).size() == 0 || this.blockedCard == target) { // if there is no discarded card
                return false;
            } else if (move.getPlayer() == 1) {
                this.player1Cards.add(this.discardedCards.get(target).get(this.discardedCards.get(target).size()-1));
            } else {
                this.player2Cards.add(this.discardedCards.get(target).get(this.discardedCards.get(target).size()-1));
            }
            this.discardedCards.get(target).remove(this.discardedCards.get(target).size()-1);
        }

        if (this.deck.size() == 0)
            this.state = 4;
        else if (move.getPlayer() == 1)
            this.state = 2;
        else
            this.state = 0;

        this.blockedCard = -1;
        return true;
    }

    /**
     * Input: 1 or 2
     */
    public GameState getGameState(int player) {
        if (player == 1) {
            return new GameState(this.player1Cards, this.field, this.discardedCards, this.deck.size(), this.state);
        } else if (player == 2) {
            List<List<Card>> reversedField = new ArrayList<>();
            for (int i = 5; i < 10; i++)
                reversedField.add(this.field.get(i));
            for (int i = 0; i < 5; i++)
                reversedField.add(this.field.get(i));

            return new GameState(this.player2Cards, reversedField, this.discardedCards, this.deck.size(), this.state);
        }

        return null;
    }

    public int getState() {return this.state;}

    private void createCards() {
        this.deck = new ArrayList<>(60);

        for (int i = 0; i < 5; i++) {
            for (int x = 2; x <= 10; x++) {
                this.deck.add(new Card(i, x));
            }
            this.deck.add(new Card(i, 0));
            this.deck.add(new Card(i, 0));
            this.deck.add(new Card(i, 0));
        }

        Collections.shuffle(this.deck);

        this.player1Cards = new ArrayList<Card>();
        this.player2Cards = new ArrayList<Card>();

        for (int i = 0; i < 8; i++) {
            this.player1Cards.add(this.deck.get(0));
            this.deck.remove(0);
            this.player2Cards.add(this.deck.get(0));
            this.deck.remove(0);
        }

        this.field = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            this.field.add(new ArrayList<Card>());
        }

        this.discardedCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            this.discardedCards.add(new ArrayList<Card>());
        }
    }
}
