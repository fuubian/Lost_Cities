package game;

/**
 * The second part of a move.
 */
public class TakeMove {

  /**
   * Index of the active player.
   * 1 or 2.
   */
  private final int player;

  /**
   * Target of the Move.
   * 0 - Discarded Red Cards
   * 1 - Discarded Green Cards
   * 2 - Discarded Blue Cards
   * 3 - Discarded White Cards
   * 4 - Discarded Yellow Cards
   * 5 - New Cards Pile
   */
  private final int target;

  /**
   * Constructor.
   */
  public TakeMove(int player, int target) {
    this.player = player;
    this.target = target;
  }

  /**
   * Returns the index of the player.
   */
  public int getPlayer() {
    return this.player;
  }

  /**
   * Returns the target of the move.
   */
  public int getTarget() {
    return this.target;
  }

  /**
   * Returns a string representation of the move.
   */
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
