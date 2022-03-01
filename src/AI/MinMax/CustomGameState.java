package AI.MinMax;

import Game.Card;
import Game.GameState;
import Game.PlayMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
This class is used for the min-max-tree.
It is a copy of an original with a random distribution of unknown cards.
PlayMoves and TakeMoves can be simulated.
 */

public class CustomGameState {

    private final List<Card> playerCards;
    private final List<Card> opponentsCards;    // random cards

    private List<Card> pile;  // random cards

    /*
    0-4: Own Played Expedition Cards
    0: Red
    1: Green
    2: Blue
    3: White
    4: Yellow
    5-9: Opponents's Played Expedition Cards
     */
    private final List<List<Card>> field;

    /*
    0: Red
    1: Green
    2: Blue
    3: White
    4: Yellow
     */
    private final List<List<Card>> discardedCards;

    private int cardsRemaining;
    private int state;

    public CustomGameState(GameState state) {
        this.playerCards = state.getPlayerCardsObject();
        this.field = state.getField();
        this.discardedCards = state.getDiscardedCards();
        this.cardsRemaining = state.getCardsRemaining();
        this.state = state.getState();

        // randomize unknown cards
        this.opponentsCards = new ArrayList<>();
        this.createRandomCards();
    }

    public CustomGameState(CustomGameState state, PlayMove move) {
        this.playerCards = new ArrayList<>();
        this.playerCards.addAll(state.getPlayerCardsObject());
        this.opponentsCards = new ArrayList<>();
        this.opponentsCards.addAll(state.getOpponentsCards());
        this.pile = new ArrayList<>();
        this.pile.addAll(state.getPile());
        this.field = new ArrayList<>();
        this.field.addAll(state.getField());
        this.discardedCards = new ArrayList<>();
        this.discardedCards.addAll(state.getDiscardedCards());
        this.cardsRemaining = state.getCardsRemaining();
        this.state = state.getState();

        // execute move (must be valid)
        int target = move.getTarget();
        if (target == 1) {
            if (this.state == 0) {
                Card c = this.playerCards.get(move.getCard()-1);
                this.playerCards.remove(c);
                this.field.get(c.getColorCode()).add(c);
                this.state = 1;
            } else if (this.state == 2) {
                Card c = this.opponentsCards.get(move.getCard()-1);
                this.opponentsCards.remove(c);
                this.field.get(c.getColorCode()+5).add(c);
                this.state = 3;
            }
        } else {
            if (this.state == 0) {
                Card c = this.playerCards.get(move.getCard()-1);
                this.playerCards.remove(c);
                this.discardedCards.get(c.getColorCode()).add(c);
                this.state = 1;
            } else if (this.state == 2) {
                Card c = this.opponentsCards.get(move.getCard()-1);
                this.opponentsCards.remove(c);
                this.discardedCards.get(c.getColorCode()).add(c);
                this.state = 3;
            }
        }
    }

    public int[] calculatePoints() {
        int[] points = new int[2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                if (this.field.get(j+(i*5)).size() > 0) {
                    int pointsField = -20;  // expedition cost
                    int factor = 1;

                    for (int x = 0; x < this.field.get(j+(i*5)).size(); x++) {
                        Card c = this.field.get(j+(i*5)).get(x);

                        if (c.getValue() == 0)
                            factor++;
                        else
                            pointsField += c.getValue();
                    }

                    points[i] += factor * pointsField;
                    if (this.field.get(j+(i*5)).size() >= 8) {
                        points[i] += 20;    // bonus for at least 8 cards in one expedition
                    }
                }
            }
        }

        return points;
    }

    public List<Card> getPlayerCardsObject() {
        return this.playerCards;
    }

    public List<Card> getOpponentsCards() { return this.opponentsCards; }

    public List<List<Card>> getField() {
        return this.field;
    }

    public List<List<Card>> getDiscardedCards() {
        return this.discardedCards;
    }

    public List<Card> getPile() { return this.pile; }

    public int getCardsRemaining() {
        return this.cardsRemaining;
    }

    public int getState() {
        return this.state;
    }

    private void createRandomCards() {
        // create all Cards
        List<Card> unknownCards = new ArrayList<>();
        String[] colors = {"Red", "Green", "Blue", "White", "Yellow"};

        for (String color : colors) {
            LookForCard:
            for (int i = 2; i <= 10; i++) {
                Card newCard = new Card(color, i);

                // player cards
                for (Card c : this.playerCards) {
                    if (c.toString().equals(newCard.toString())) {
                        break LookForCard;
                    }
                }

                // discard piles
                for (List<Card> discard : this.discardedCards) {
                    for (Card c : discard) {
                        if (c.toString().equals(newCard.toString())) {
                            break LookForCard;
                        }
                    }
                }

                // expedition piles
                for (List<Card> expedition : this.field) {
                    for (Card c : expedition) {
                        if (c.toString().equals(newCard.toString())) {
                            break LookForCard;
                        }
                    }
                }

                unknownCards.add(newCard);
            }

            // look for wager cards
            int wagersLeft = 3;

            // player cards
            for (Card c : this.playerCards) {
                if (c.toString().equals(color + "0")) {
                    wagersLeft--;
                }
            }

            // discard piles
            for (List<Card> discard : this.discardedCards) {
                for (Card c : discard) {
                    if (c.toString().equals(color + "0")) {
                        wagersLeft--;
                    }
                }
            }

            // expedition piles
            for (List<Card> expedition : this.field) {
                for (Card c : expedition) {
                    if (c.toString().equals(color + "0")) {
                        wagersLeft--;
                    }
                }
            }

            for (int i = 0; i < wagersLeft; i++) {
                unknownCards.add(new Card(color, 0));
            }
        }

        // randomize order
        Collections.shuffle(unknownCards);

        for (int i = 0; i < 8; i++) {
            this.opponentsCards.add(unknownCards.get(0));
            unknownCards.remove(0);
        }
        this.pile = unknownCards;
    }
}
