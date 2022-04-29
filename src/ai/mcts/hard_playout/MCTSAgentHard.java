package ai.mcts.hard_playout;

import ai.mcts.MCTSAgent;
import game.Card;
import game.GameState;

import java.util.List;

/**
 * Class of the HP-Agent.
 */
public class MCTSAgentHard extends MCTSAgent {

  /**
   * Constructor.
   */
  public MCTSAgentHard(GameState state, int player, List<Card> knownOpponentCards) {
    super(state, player, knownOpponentCards);
  }

  /**
   * Initializes the correct StateCopy.
   */
  @Override
  protected void initializeCopy() {
    this.copy = new StateCopyHard(this.state, this.player, this.knownOpponentCards);
  }
}
