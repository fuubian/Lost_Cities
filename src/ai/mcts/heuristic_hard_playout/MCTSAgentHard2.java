package ai.mcts.heuristic_hard_playout;

import ai.mcts.MCTSAgent;
import game.Card;
import game.GameState;

import java.util.List;

/**
 * Class of the HHP-Agent.
 */
public class MCTSAgentHard2 extends MCTSAgent {

  /**
   * Constructor.
   */
  public MCTSAgentHard2(GameState state, int player, List<Card> knownOpponentCards) {
    super(state, player, knownOpponentCards);
  }

  /**
   * Initializes the correct StateCopy.
   */
  @Override
  protected void initializeCopy() {
    this.copy = new StateCopyHard2(this.state, this.player, this.knownOpponentCards);
  }
}
