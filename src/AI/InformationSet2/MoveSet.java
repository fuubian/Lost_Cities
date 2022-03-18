package AI.InformationSet2;

import Game.PlayMove;
import Game.TakeMove;

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
        && this.playMove.getCardObject().equals(other.getPlayMove().getCardObject())
        && this.takeMove.getTarget() == this.takeMove.getTarget()) {
            return true;
        }

        return false;
    }
}
