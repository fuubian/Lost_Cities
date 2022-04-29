package ai.mcts;

import ai.ArtificialIntelligence;
import game.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for all AIs that are based on MCTS.
 * This manages the execution the agent.
 */
public abstract class InformationSetAI extends ArtificialIntelligence {

  /**
   * Player index.
   */
  protected final int player;

  /**
   * The resulting move of the MCTS.
   */
  private MoveSet move;

  /**
   * A list of all opponent's cards that are known.
   */
  protected List<Card> knownOpponentCards;

  /**
   * How much time is available for a move.
   */
  private final long MAX_TIME;

  /**
   * How many additional threads are executed during MCTS.
   */
  protected final int AMOUNT_THREADS = 5;

  /**
   * Constructor.
   */
  public InformationSetAI(int player, long time) {
    this.player = player;
    this.knownOpponentCards = new ArrayList<>();
    this.MAX_TIME = time;
  }

  /**
   * Plays a card onto the discard pile or expedition pile.
   */
  public PlayMove playCard(GameState state) {
    this.move = this.createTree(state);

    return this.move.getPlayMove();
  }

  /**
   * Draws a card from the discard pile or normal pile.
   */
  public TakeMove takeCard(GameState state) {
    return this.move.getTakeMove();
  }

  /**
   * Receive opponent's move to remove card of knownOpponentCards.
   */
  @Override
  public void receiveOpponentPlayMove(PlayMove move) {
    for (int i = 0; i < this.knownOpponentCards.size(); i++) {
      if (move.getCardObject().equals(this.knownOpponentCards.get(i))) {
        // remove known card from opponent's hand
        this.knownOpponentCards.remove(i);
        break;
      }
    }
  }

  /**
   * Receive opponent's move to add card to knownOpponentCards.
   */
  @Override
  public void receiveOpponentTakeMove(Card card) {
    this.knownOpponentCards.add(card);
  }

  /**
   * Creates a SO-ISMCTS and determines the best MoveSet.
   */
  private MoveSet createTree(GameState state) {
    List<MCTSAgent> agents = new ArrayList<>(AMOUNT_THREADS + 1);
    List<MCTSThread> threads = new ArrayList<>(AMOUNT_THREADS);

    // specification
    this.initializeAgents(agents, threads, state);

    long time = System.currentTimeMillis();
    for (int i = 0; i < AMOUNT_THREADS; i++) {
      threads.get(i).start();
    }
    while (System.currentTimeMillis() - time <= this.MAX_TIME) {
      agents.get(AMOUNT_THREADS).runThrough();
    }
    for (int i = 0; i < AMOUNT_THREADS; i++) {
      threads.get(i).stopRun();
    }

    Node root = agents.get(AMOUNT_THREADS).getRoot();
    for (int i = 0; i < AMOUNT_THREADS; i++) {
      while (!threads.get(i).isFinished()) {
        // wait until last iteration was finished
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      root.fusionWithOther(agents.get(i).getRoot());
    }

    return root.determineMoveSet();
  }

  /**
   * Initializes the needed variant of a MCTS agent.
   */
  protected abstract void initializeAgents(List<MCTSAgent> agents, List<MCTSThread> threads, GameState state);
}
