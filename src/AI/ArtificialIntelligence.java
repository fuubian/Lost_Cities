package AI;

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
}
