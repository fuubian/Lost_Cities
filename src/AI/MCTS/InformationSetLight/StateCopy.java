package AI.MCTS.InformationSetLight;

import Game.MoveSet;
import Game.Card;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StateCopy {

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
    private int roundState;

    private final int player;
    private final int opponent;

    /**
     * Works like a copy constructor.
     */
    public StateCopy(GameState state, int player, List<Card> knownOpponentCards) {
        this.player = player;
        this.opponent = player == 1 ? 2 : 1;

        this.playerCards = new ArrayList<>(8);
        this.playerCards.addAll(state.getPlayerCardsObject());
        this.field = new ArrayList<>(10);
        for (int i = 0; i < state.getField().size(); i++) {
            ArrayList<Card> tmp = new ArrayList<>();
            tmp.addAll(state.getField().get(i));
            this.field.add(tmp);
        }
        this.discardedCards = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            ArrayList<Card> tmp = new ArrayList<>(state.getDiscardedCards().size());
            tmp.addAll(state.getDiscardedCards().get(i));
            this.discardedCards.add(tmp);
        }
        this.cardsRemaining = state.getCardsRemaining();
        this.roundState = state.getRoundState();

        // randomize unknown cards
        this.opponentsCards = new ArrayList<>(8);
        this.opponentsCards.addAll(knownOpponentCards);
        this.createRandomCards(state);
    }

    /**
     * Executes a random move to the current state and returns it.
     */
    public MoveSet executeRandomMove() {
        // randomize a move
        List<MoveSet> possibleMoves = this.getPossibleMoves();
        int random = (int) (Math.random() * possibleMoves.size());
        MoveSet move = possibleMoves.get(random);

        this.executeMove(move);

        return move;
    }

    public void executeSimulationMove(List<Card> handCards, int player, int offset) {
        // randomize a move
        int card = (int) (Math.random() * 8);
        int color = handCards.get(card).getColorCode();
        int target;

        if (this.field.get(color+offset).size() > 0 &&
                this.field.get(color+offset).get(this.field.get(color+offset).size()-1).getValue() <= handCards.get(card).getValue()) {
            target = (int) (Math.random() * 2) + 1;
        } else {
            target = 2;
        }

        PlayMove playMove = new PlayMove(player, card+1, handCards.get(card), target);

        int target2;

        while (true) {
            target2 = (int) (Math.random() * 6);;
            if (target2 == 5) {
                break;
            } else if (this.discardedCards.get(target2).size() > 0 && target2 != color) {
                break;
            }
        }

        TakeMove takeMove = new TakeMove(player, target2);
        MoveSet move = new MoveSet(playMove, takeMove);

        this.executeMove(move);
    }

    /**
     * Applies a move to the current state.
     */
    public void executeMove(MoveSet move) {
        if ((this.player == 1 && this.roundState == 0) ||
                (this.player == 2 && this.roundState == 2)) {
            this.applyMove(move, this.playerCards, 0);
        } else {
            this.applyMove(move, this.opponentsCards, 5);
        }
        this.roundState = this.roundState == 0 ? 2 : 0;

        // check if game is over
        if (this.cardsRemaining == 0) {
            this.roundState = 4;
        }
    }

    /**
     * Calculates and returns all possible froms within the current state.
     */
    public List<MoveSet> getPossibleMoves() {
        List<MoveSet> possibleMoves = new ArrayList<>();

        if (this.player == 1 && this.roundState == 0 || this.player == 2 && this.roundState == 2) {
            // Own Move
            this.addMoveSets(possibleMoves, this.player, this.playerCards, 0);
        } else if (this.player == 1 && this.roundState == 2 || this.player == 2 && this.roundState == 0) {
            // Opponent's move
            this.addMoveSets(possibleMoves, this.opponent, this.opponentsCards, 5);
        }

        return possibleMoves;
    }

    public double simulateGame() {
        while (this.roundState != 4) {
            if (this.player == 1 && this.roundState == 0 || this.player == 2 && this.roundState == 2) {
                // Own Move
                this.executeSimulationMove(this.playerCards, this.player, 0);
            } else {
                // Opponent's move
                this.executeSimulationMove(this.opponentsCards, this.opponent, 5);
            }
        }

        int[] finalPoints = this.calculatePoints();
        if (finalPoints[0] > finalPoints[1]) {
            return 1.0;
        } else if (finalPoints[0] < finalPoints[1]) {
            return 0;
        }

        return 0.5;
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

    public int getRoundState() {
        return this.roundState;
    }

    private void createRandomCards(GameState state) {
        // create all Cards
        this.pile = new ArrayList<>(60);
        String[] colors = {"Red", "Green", "Blue", "White", "Yellow"};

        for (int i = 0; i < colors.length; i++) {
            for (int x = 2; x <= 10; x++) {
                this.pile.add(new Card(colors[i], x));
            }
            this.pile.add(new Card(colors[i], 0));
            this.pile.add(new Card(colors[i], 0));
            this.pile.add(new Card(colors[i], 0));
        }

        // removing known cards from pile
        this.removeCardsFromPile(state.getPlayerCardsObject());
        for (int i = 0; i < state.getField().size(); i++) {
            this.removeCardsFromPile(state.getField().get(i));
        }
        for (int i = 0; i < state.getDiscardedCards().size(); i++) {
            this.removeCardsFromPile(state.getDiscardedCards().get(i));
        }

        // remove already known cards
        this.removeCardsFromPile(this.opponentsCards);

        // randomize order
        Collections.shuffle(this.pile);

        // distribute first x cards to opponent
        for (int i = this.opponentsCards.size(); i < 8; i++) {
            this.opponentsCards.add(this.pile.get(0));
            this.pile.remove(0);
        }
    }

    private void removeCardsFromPile(List<Card> list) {
        for (Card c : list) {
            for (int i = 0; i < this.pile.size(); i++) {
                if (c.getColorCode() == this.pile.get(i).getColorCode()) {
                    if (c.getValue() == this.pile.get(i).getValue()) {
                        this.pile.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void addMoveSets(List<MoveSet> moveList, int currentPlayer, List<Card> currentPlayerCards, int offset) {
        for (int i = 0; i < 8; i++) {
            // all combinations of discarding moves + drawing moves
            PlayMove discardMove = new PlayMove(currentPlayer, i+1, currentPlayerCards.get(i), 2);
            int cardColor = currentPlayerCards.get(i).getColorCode();
            this.addTakeMoves(moveList, discardMove, currentPlayer, cardColor);

            // all combination of expedition moves + drawing moves
            if (this.field.get(cardColor+offset).size() == 0 ||
                    this.field.get(cardColor+offset).get(this.field.get(cardColor+offset).size()-1).getValue() <= currentPlayerCards.get(i).getValue()) {
                PlayMove expeditionMove = new PlayMove(currentPlayer, i+1, currentPlayerCards.get(i), 1);
                this.addTakeMoves(moveList, expeditionMove, currentPlayer, -1);
            }
        }
    }

    private void addTakeMoves(List<MoveSet> moveList, PlayMove playMove, int currentPlayer, int cardColor) {
        moveList.add(new MoveSet(playMove, new TakeMove(currentPlayer, 5)));
        for (int j = 0; j < 5; j++) {
            if (j != cardColor && this.discardedCards.get(j).size() > 0) {
                MoveSet moveSet = new MoveSet(playMove, new TakeMove(currentPlayer, j));
                moveList.add(moveSet);
            }
        }
    }

    private void applyMove(MoveSet move, List<Card> currentPlayerCards, int offset) {
        // PlayMove Sequence
        PlayMove playMove = move.getPlayMove();

        if (playMove.getTarget() == 1) {
            this.field.get(playMove.getCardObject().getColorCode()+offset).add(playMove.getCardObject());
        } else {
            this.discardedCards.get(playMove.getCardObject().getColorCode()).add(playMove.getCardObject());
        }
        currentPlayerCards.remove(playMove.getCard()-1);

        // TakeMove Sequence
        TakeMove takeMove = move.getTakeMove();

        if (takeMove.getTarget() == 5) {
            currentPlayerCards.add(this.pile.get(0));
            this.pile.remove(0);
            this.cardsRemaining--;
        } else {
            currentPlayerCards.add(this.discardedCards.get((takeMove.getTarget())).get(
                    this.discardedCards.get((takeMove.getTarget())).size()-1));
            this.discardedCards.get(takeMove.getTarget()).remove(this.discardedCards.get((takeMove.getTarget())).size()-1);
        }
    }
}
