package AI;

import Game.Card;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

import java.util.ArrayList;
import java.util.List;

public class RandomAI extends ArtificialIntelligence {

    private int player;
    private int blockedColor;

    public RandomAI(int player) {
        this.player = player;
        this.blockedColor = -1;
    }

    /**
     * Plays a card onto the discard pile or expedition pile.
     */
    public PlayMove playCard(GameState state) {
        List<PlayMove> possibleMoves = new ArrayList<>();
        List<Card> playerCards = state.getPlayerCardsObject();

        // discard moves
        for(int i = 0; i < 8; i++) {
            possibleMoves.add(new PlayMove(this.player, i+1, state.getPlayerCardsObject().get(i), 2));
        }

        // play on the field
        for (int i = 0; i < 8; i++) {
            int color = playerCards.get(i).getColorCode();

            if (state.getField().get(color).size() == 0 ||
                    state.getField().get(color).get(state.getField().get(color).size()-1).getValue() <= playerCards.get(i).getValue()) {
                possibleMoves.add(new PlayMove(this.player, i+1, state.getPlayerCardsObject().get(i), 1));
            }
        }

        int random = (int) (Math.random() * possibleMoves.size());

        /**
         * block color to prevent to draw the played card from the discard pile
         */
        if (possibleMoves.get(random).getTarget() == 2) {
            int card = possibleMoves.get(random).getCard() - 1;
            this.blockedColor = playerCards.get(card).getColorCode();
        }

        return possibleMoves.get(random);
    }

    /**
     * Draws a card from the discard pile or normal pile.
     */
    public TakeMove takeCard(GameState state) {
        int random;
        while (true) {
            random = (int) (Math.random() * 6);

            if (random == 5 ||
                    (random != this.blockedColor && state.getDiscardedCards().get(random).size() > 0)) {
                break;
            }
        }

        this.blockedColor = -1;
        return new TakeMove(this.player, random);
    }
}
