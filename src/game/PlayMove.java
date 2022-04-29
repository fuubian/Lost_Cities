package game;

/**
 * The first part of a move.
 */
public class PlayMove {

  /**
   * Index of the active player.
   * 1 or 2.
   */
  private final int player;

  /**
   * Index of the played card.
   * 1-8.
   */
  private final int card;

  /**
   * Object of the played card.
   */
  private final Card cardObject;

  /**
   * Target of the Move.
   * 1 - Field
   * 2 - Discarded Piles
   */
  private final int target;

  /**
   * Constructor.
   */
  public PlayMove(int player, int card, Card cardObject, int target) {
    this.player = player;
    this.card = card;
    this.cardObject = cardObject;
    this.target = target;
  }

  /**
   * Returns the index of the player.
   */
  public int getPlayer() {
    return this.player;
  }

  /**
   * Returns the index of the card.
   */
  public int getCard() {
    return this.card;
  }

  /**
   * Returns the object of the card.
   */
  public Card getCardObject() {
    return this.cardObject;
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
    String text = "Player " + player + " played " + this.cardObject.toString() + " to ";

    if (target == 1) text += "their expedition cards.";
    else text += "the discard pile";

    return text;
  }
}
