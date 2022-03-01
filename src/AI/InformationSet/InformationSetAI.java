package AI.InformationSet;

import Game.Card;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

import java.util.ArrayList;
import java.util.List;

public class InformationSetAI {

    private final int player;

    private final int MAX_ITERATIONS = 10;

    public InformationSetAI(int player) {
        this.player = player;
    }

    /**
     * Plays a card onto the discard pile or expedition pile.
     */
    public PlayMove playCard(GameState state) {
        Node root = new Node();

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            StateCopy copy = new StateCopy(state, this.player);
            Node currentNode = root;
        }

        return null;
    }

    /**
     * Draws a card from the discard pile or normal pile.
     */
    public TakeMove takeCard(GameState state) {
        return null;
    }

    private void createTree(GameState state) {

    }
}
