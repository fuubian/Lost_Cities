package Game;

import java.io.Serializable;

public class TakeMove implements Serializable {

    int player; // 1 or 2
    int target; // 0-red; 1-green; ... 5 - stack

    public TakeMove(int player, int target) {
        this.player = player;
        this.target = target;
    }

    public int getPlayer() {
        return this.player;
    }

    public int getTarget() {
        return this.target;
    }

    public String toString() {
        String text = "Player " + player + " drew a card from the ";

        if (target == 0) text += "red discard pile.";
        else if (target == 1) text += "green discard pile.";
        else if (target == 2) text += "blue discard pile.";
        else if (target == 3) text += "white discard pile.";
        else if (target == 4) text += "yellow discard pile.";
        else text += "new card pile.";

        return text;
    }
}
