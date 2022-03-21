package AI;

import Game.*;

import java.util.ArrayList;
import java.util.List;

public class ImprovedRandomAI extends ArtificialIntelligence {

    private int player;
    private MoveSet move;

    public ImprovedRandomAI(int player) {
        this.player = player;
    }

    /**
     * Plays a card onto the discard pile or expedition pile.
     */
    public PlayMove playCard(GameState state) {
        List<MoveSet> possibleMoves = this.getPossibleMoves(state);
        this.removeBadMoves(possibleMoves, state);

        if (possibleMoves.size() == 0) {
            // only bad moves are available
            possibleMoves = this.getPossibleMoves(state);
        }

        this.move = possibleMoves.get((int) (Math.random() * possibleMoves.size()));

        return this.move.getPlayMove();
    }

    /**
     * Draws a card from the discard pile or normal pile.
     */
    public TakeMove takeCard(GameState state) {
        return this.move.getTakeMove();
    }

    /**
     * Calculates and returns all possible froms within the current state.
     */
    public List<MoveSet> getPossibleMoves(GameState state) {
        List<MoveSet> possibleMoves = new ArrayList<>();
        List<Card> playerCards = state.getPlayerCardsObject();
        for (int i = 0; i < 8; i++) {
            // all combinations of discarding moves + drawing moves
            PlayMove discardMove = new PlayMove(this.player, i + 1, playerCards.get(i), 2);
            int cardColor = playerCards.get(i).getColorCode();
            this.addTakeMoves(possibleMoves, discardMove, cardColor, state);

            // all combination of expedition moves + drawing moves
            if (state.getField().get(cardColor).size() == 0 ||
                    state.getField().get(cardColor).get(state.getField().get(cardColor).size() - 1).getValue() <= playerCards.get(i).getValue()) {
                PlayMove expeditionMove = new PlayMove(this.player, i + 1, playerCards.get(i), 1);
                this.addTakeMoves(possibleMoves, expeditionMove, -1, state);
            }
        }

        return possibleMoves;
    }

    private void addTakeMoves(List<MoveSet> moveList, PlayMove playMove, int cardColor, GameState state) {
        for (int j = 0; j < 5; j++) {
            if (j != cardColor && state.getDiscardedCards().get(j).size() > 0) {
                MoveSet moveSet = new MoveSet(playMove, new TakeMove(this.player, j));
                moveList.add(moveSet);
            }
        }
        moveList.add(new MoveSet(playMove, new TakeMove(this.player, 5)));
    }

    /**
     * Removes obvious bad moves from a list of moves.
     */
    private void removeBadMoves(List<MoveSet> possibleMoves, GameState state) {
        List<Card> playerCards = state.getPlayerCardsObject();
        Main:
        for (int i = 0; i < possibleMoves.size(); i++) {
            // inspect PlayMove for mistakes
            PlayMove playMove = possibleMoves.get(i).getPlayMove();
            int colorPlayMove = playMove.getCardObject().getColorCode();
            if (playMove.getTarget() == 2) {
                // don't discard useful cards
                if (state.getField().get(colorPlayMove).size() > 0 &&
                        state.getField().get(colorPlayMove).get(state.getField().get(colorPlayMove).size() - 1).getValue() <
                                playMove.getCardObject().getValue()) {
                    possibleMoves.remove(i);
                    i--;
                    continue;
                }

                // don't give good cards to the opponent
                int opponentField = (colorPlayMove + 5) % 10;
                if (state.getField().get(opponentField).size() > 0 && state.getField().get(opponentField).get(state.getField().get(opponentField).size() - 1).getValue() <
                        playMove.getCardObject().getValue()) {
                    possibleMoves.remove(i);
                    i--;
                    continue;
                }
            } else {
                int sameColor = 0;
                int colorValue = 0;
                for (Card card : playerCards) {
                    if (card.getColorCode() == colorPlayMove) {
                        if (card.getValue() < playMove.getCardObject().getValue() &&
                                card.getValue() != 0) {
                            possibleMoves.remove(i);
                            i--;
                            continue Main;
                        }

                        sameColor++;
                        colorValue += card.getValue();
                    }
                }

                if (state.getField().get(colorPlayMove).size() == 0 && (sameColor <= 2 || colorValue <= 11)) {
                    // don't open expedition if there is no hope for it to be successful
                    possibleMoves.remove(i);
                    i--;
                    continue;
                }
            }

            // inspect TakeMove for mistakes
            TakeMove takeMove = possibleMoves.get(i).getTakeMove();
            int colorTakeMove = takeMove.getTarget();
            if (colorTakeMove != 5) {
                int sameColor = 0;
                for (Card card : playerCards) {
                    if (card.getColorCode() == colorPlayMove) {
                        sameColor++;
                    }
                }
                if ((state.getField().get(colorTakeMove).size() == 0 && sameColor <= 1) || (state.getField().get(colorTakeMove).size() > 0 &&
                        state.getField().get(colorTakeMove).get(state.getField().get(colorTakeMove).size() - 1).getValue() >
                                state.getDiscardedCards().get(colorTakeMove).get(state.getDiscardedCards().get(colorTakeMove).size() - 1).getValue())) {
                    // don't draw unnecessary cards from discard piles (when you are not losing)
                    if (state.getCardsRemaining() <= 10) {
                        int points[] = state.calculatePoints();
                        if (points[0] > points[1]) {
                            possibleMoves.remove(i);
                            i--;
                            continue;
                        }
                    } else {
                        possibleMoves.remove(i);
                        i--;
                        continue;
                    }
                }
            }
        }
    }

    @Override
    public void receiveOpponentPlayMove (PlayMove move){

    }

    @Override
    public void receiveOpponentTakeMove (Card card){

    }
}