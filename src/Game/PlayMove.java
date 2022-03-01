package Game;

import java.io.Serializable;

public class PlayMove implements Serializable {

    private int player; // 1 or 2
    private int card;   // 1-8
    private int target; // field = 1; discard = 2

    public PlayMove(int player, int card, int target) {
        this.player = player;
        this.card = card;
        this.target = target;
    }

    public int getPlayer() {
        return this.player;
    }

    public int getCard() {
        return this.card;
    }

    public int getTarget() {
        return this.target;
    }

    public String toString() {
        String text = "Player " + player + " played a card to ";

        if (target == 1) text += "their expedition cards.";
        else text += "the discard pile";

        return text;
    }
}
