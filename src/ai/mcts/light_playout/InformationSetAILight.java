package ai.mcts.light_playout;

import ai.mcts.InformationSetAI;
import ai.mcts.MCTSAgent;
import ai.mcts.MCTSThread;
import game.GameState;

import java.util.List;

/**
 * Class of the "SO-ISMCTS-Agent mit Light-Playout".
 */
public class InformationSetAILight extends InformationSetAI {

  /**
   * Constructor.
   */
  public InformationSetAILight(int player, long time) {
    super(player, time);
  }

  /**
   * Initializes LP-Agents.
   */
  @Override
  protected void initializeAgents(List<MCTSAgent> agents, List<MCTSThread> threads, GameState state) {
    for (int i = 0; i < AMOUNT_THREADS; i++) {
      agents.add(new MCTSAgentLight(state, this.player, this.knownOpponentCards));
      threads.add(new MCTSThread(agents.get(i)));
    }
    agents.add(new MCTSAgentLight(state, this.player, this.knownOpponentCards));
  }
}
