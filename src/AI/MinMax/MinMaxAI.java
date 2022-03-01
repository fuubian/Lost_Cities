package AI.MinMax;

import Game.Card;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

import java.util.ArrayList;
import java.util.List;

public class MinMaxAI {

    private int player;
    private int blockedColor;

    public MinMaxAI(int player) {
        this.player = player;
        this.blockedColor = -1;
    }

    /**
     * Plays a card onto the discard pile or expedition pile.
     */
    public PlayMove playCard(GameState state) {
        return null;
    }

    /**
     * Draws a card from the discard pile or normal pile.
     */
    public TakeMove takeCard(GameState state) {
        return null;
    }

    private int calculateHeuristic(GameState state) {
        List<List<Card>> field = state.getField();

        int value = 0;

        // own expedition cards
        for (int i = 0; i < 5; i++) {
            if (field.get(i).size() > 0) {
                value -= 14;  // heuristical costs for starting an expedition
                for (Card c : field.get(i)) {
                    value += c.getValue();
                }

                // rewarding expeditions with lots of cards
                value += 3 * field.get(i).size();

                if (field.get(i).size() >= 8)
                    value += 20;    // 20 point bonus for eight cards
            }
        }

        // opponent's expedition cards
        for (int i = 5; i < 10; i++) {
            if (field.get(i).size() > 0) {
                value += 14;  // heuristic costs for starting an expedition
                for (Card c : field.get(i)) {
                    value -= c.getValue();
                }

                // rewarding expeditions with lots of cards
                value -= 3 * field.get(i).size();

                if (field.get(i).size() >= 8)
                    value -= 20;    // 20 point bonus for eight cards
            }
        }

        return value;
    }
}
