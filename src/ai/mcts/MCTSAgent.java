package ai.mcts;

import game.Card;
import game.GameState;
import game.MoveSet;

import java.util.List;

/**
 * Superclass for all MCTSAgents.
 * This class contains the MCTS-algorithm.
 */
public abstract class MCTSAgent {

  /**
   * Root of the tree.
   */
  private final Node root;

  /**
   * The current GameState.
   */
  protected GameState state;

  /**
   * Player index.
   */
  protected int player;

  /**
   * A list of all opponent's cards that are known.
   */
  protected List<Card> knownOpponentCards;

  /**
   * A copy of the current GameState including a determinzation of opponent's cards and draw pile.
   */
  protected StateCopy copy;

  /**
   * Constructor.
   */
  public MCTSAgent(GameState state, int player, List<Card> knownOpponentCards) {
    this.state = state;
    this.player = player;
    this.knownOpponentCards = knownOpponentCards;

    this.root = new Node();
  }

  /**
   * MCTS-Algorithm.
   */
  public void runThrough() {
    // Determinization
    this.initializeCopy();
    Node currentNode = this.root;
    boolean selectionCompleted = false;
    MoveSet selectedMove = null;

    // Descending and Selection
    while (true) {
      List<MoveSet> possibleMoves = this.copy.getPossibleMoves();
      if (possibleMoves.size() == 0) {
        // no node exists to that move OR it is a terminal node
        selectionCompleted = true;
      } else {
        currentNode.setCurrentLegalMoves(possibleMoves);
        MoveSet move = currentNode.searchUnexploredNode(possibleMoves);
        if (move == null) {
          // Look for move with best UCT and descend
          currentNode = currentNode.determineBestUCTChild(possibleMoves);
        } else {
          // Not listed move was found
          selectionCompleted = true;
          selectedMove = move;
        }
      }

      // Expansion
      if (selectionCompleted) {
        if (selectedMove != null) {
          currentNode = currentNode.expand(selectedMove);
        } else if (this.copy.getRoundState() != 4) {
          // no node exists
          selectedMove = this.copy.executeRandomMove();
          currentNode = currentNode.expand(selectedMove);
        }
      } else {
        this.copy.executeMove(currentNode.getMove());
        continue;
      }

      // Simulation
      double reward = this.copy.simulateGame();

      // Backpropagation
      while (currentNode.getFather() != null) {
        currentNode.update(reward);
        currentNode = currentNode.getFather();
        currentNode.updateAvailabilityChildren();
      }

      break;
    }
  }

  /**
   * Returns the root of the tree.
   */
  public Node getRoot() {
    return this.root;
  }

  /**
   * Initializes the needed variant of a StateCopy.
   */
  protected abstract void initializeCopy();
}
