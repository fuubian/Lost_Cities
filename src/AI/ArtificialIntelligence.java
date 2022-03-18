package AI;

import Game.Card;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

public abstract class ArtificialIntelligence {

    public ArtificialIntelligence() {

    }

    /**
     * Plays a card onto the discard pile or expedition pile.
     */
    public abstract PlayMove playCard(GameState state);

    /**
     * Draws a card from the discard pile or normal pile.
     */
    public abstract TakeMove takeCard(GameState state);

    /**
     * Receives the opponent's move for new information.
     */
    public abstract void receiveOpponentPlayMove(PlayMove move);

    /**
     * Receives the opponent's move for new information.
     */
    public abstract void receiveOpponentTakeMove(Card card);
}
