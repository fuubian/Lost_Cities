package AI.InformationSet;

import Game.PlayMove;
import Game.TakeMove;

import java.util.Objects;

public class MoveSet {

    private PlayMove playMove;
    private TakeMove takeMove;

    public MoveSet(PlayMove playMove, TakeMove takeMove) {
        this.playMove = playMove;
        this.takeMove = takeMove;
    }

    public PlayMove getPlayMove() {
        return this.playMove;
    }

    public TakeMove getTakeMove() {
        return this.takeMove;
    }

    public boolean equals(MoveSet other) {
        if (this.playMove.getTarget() == other.getPlayMove().getTarget()
        && this.playMove.getCard() == other.getPlayMove().getCard()
        && this.takeMove.getTarget() == this.takeMove.getTarget()) {
            return true;
        }

        return false;
    }
}
