package ai.mcts;

/**
 * Thread that executes an MCTSAgent.
 */
public class MCTSThread extends Thread {

  /**
   * The executed MCTSAgent.
   */
  private final MCTSAgent agent;

  /**
   * Determines if the agent is currently working.
   */
  private boolean running;

  /**
   * Determines if the agent has finished.
   */
  private boolean finished = false;

  /**
   * Constructor.
   */
  public MCTSThread(MCTSAgent agent) {
    this.agent = agent;
  }

  /**
   * Execution of the agent.
   */
  public void run() {
    this.running = true;
    while (this.running) {
      this.agent.runThrough();
    }
    this.finished = true;
  }

  /**
   * Stops the execution of the agent after the current iteration.
   */
  public void stopRun() {
    this.running = false;
  }

  /**
   * Checks if the agent has finished the last iteration.
   */
  public boolean isFinished() {
    return this.finished;
  }
}
