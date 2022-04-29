package ai;

import game.Card;
import game.GameState;
import game.PlayMove;
import game.TakeMove;

/**
 * Superclass for all AIs.
 */
public abstract class ArtificialIntelligence {

  /**
   * Constructor.
   */
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

  /**
   * Receives the opponent's move for new information.
   */
  public abstract void receiveOpponentPlayMove(PlayMove move);

  /**
   * Receives the opponent's move for new information.
   */
  public abstract void receiveOpponentTakeMove(Card card);
}
