package ai.mcts.light_playout;

import ai.mcts.MCTSAgent;
import game.Card;
import game.GameState;

import java.util.List;

/**
 * Class of the LP-Agent.
 */
public class MCTSAgentLight extends MCTSAgent {

  /**
   * Constructor.
   */
  public MCTSAgentLight(GameState state, int player, List<Card> knownOpponentCards) {
    super(state, player, knownOpponentCards);
  }

  /**
   * Initializes the correct StateCopy.
   */
  @Override
  protected void initializeCopy() {
    this.copy = new StateCopyLight(this.state, this.player, this.knownOpponentCards);
  }
}
