package game;

/**
 * Class that combines PlayMove and TakeMove.
 */
public class MoveSet {

  /**
   * First part of a move.
   */
  private final PlayMove playMove;

  /**
   * Second part of a move.
   */
  private final TakeMove takeMove;

  /**
   * Constructor.
   */
  public MoveSet(PlayMove playMove, TakeMove takeMove) {
    this.playMove = playMove;
    this.takeMove = takeMove;
  }

  /**
   * Returns the PlayMove.
   */
  public PlayMove getPlayMove() {
    return this.playMove;
  }

  /**
   * Returns the TakeMove.
   */
  public TakeMove getTakeMove() {
    return this.takeMove;
  }

  /**
   * Compares this MoveSet object with another MoveSet object.
   */
  public boolean equals(MoveSet other) {
    return this.playMove.getTarget() == other.getPlayMove().getTarget()
            && this.playMove.getCardObject().equals(other.getPlayMove().getCardObject())
            && this.takeMove.getTarget() == other.getTakeMove().getTarget();
  }
}
