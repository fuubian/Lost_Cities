package ai.mcts.hard_playout;

import ai.mcts.StateCopy;
import game.*;

import java.util.List;

/**
 * StateCopy for HP-Agent.
 */
public class StateCopyHard extends StateCopy {

  /**
   * Constructor.
   */
  public StateCopyHard(GameState state, int player, List<Card> knownOpponentCards) {
    super(state, player, knownOpponentCards);
  }
}
