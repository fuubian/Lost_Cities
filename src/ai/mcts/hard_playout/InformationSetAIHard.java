package ai.mcts.hard_playout;

import ai.mcts.InformationSetAI;
import ai.mcts.MCTSAgent;
import ai.mcts.MCTSThread;
import game.*;

import java.util.List;

/**
 * Class of the "SO-ISMCTS-Agent mit einfachem Hard-Playout".
 */
public class InformationSetAIHard extends InformationSetAI {

  /**
   * Constructor.
   */
  public InformationSetAIHard(int player, long time) {
    super(player, time);
  }

  /**
   * Initializes HP-Agents.
   */
  @Override
  protected void initializeAgents(List<MCTSAgent> agents, List<MCTSThread> threads, GameState state) {
    for (int i = 0; i < AMOUNT_THREADS; i++) {
      agents.add(new MCTSAgentHard(state, this.player, this.knownOpponentCards));
      threads.add(new MCTSThread(agents.get(i)));
    }
    agents.add(new MCTSAgentHard(state, this.player, this.knownOpponentCards));
  }
}
