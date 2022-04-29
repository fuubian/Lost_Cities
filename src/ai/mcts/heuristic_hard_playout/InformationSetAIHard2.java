package ai.mcts.heuristic_hard_playout;

import ai.mcts.InformationSetAI;
import ai.mcts.MCTSAgent;
import ai.mcts.MCTSThread;
import game.*;

import java.util.List;

/**
 * Class of the "SO-ISMCTS-Agent mit heuristischem Hard-Playout".
 */
public class InformationSetAIHard2 extends InformationSetAI {

  /**
   * Constructor.
   */
  public InformationSetAIHard2(int player, long time) {
    super(player, time);
  }

  /**
   * Initializes HHP-Agents.
   */
  @Override
  protected void initializeAgents(List<MCTSAgent> agents, List<MCTSThread> threads, GameState state) {
    for (int i = 0; i < AMOUNT_THREADS; i++) {
      agents.add(new MCTSAgentHard2(state, this.player, this.knownOpponentCards));
      threads.add(new MCTSThread(agents.get(i)));
    }
    agents.add(new MCTSAgentHard2(state, this.player, this.knownOpponentCards));
  }
}
