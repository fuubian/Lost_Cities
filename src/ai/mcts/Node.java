package ai.mcts;

import game.MoveSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Node of an MCTS-Tree.
 */
public class Node {

  /**
   * Father-Node of this node.
   * Null if this node is the root.
   */
  private final Node father;

  /**
   * List of all Children-Nodes.
   */
  protected final List<Node> children;

  /**
   * Amount of wins.
   */
  private double winCount = 0;

  /**
   * Amount of visits.
   */
  private int visitCount = 0;

  /**
   * How often the node was available for each determinization.
   */
  private int availabilityCount = 0;

  /**
   * Coefficient c for exploration term.
   */
  protected final double CONSTANT_C = 0.7;

  /**
   * Which move was executed to reach this node.
   */
  private final MoveSet move;

  /**
   * Was the last move executed by the agent.
   */
  protected final boolean ownMove;

  /**
   * Which moves were with the current determinization legal.
   */
  private List<MoveSet> currentLegalMoves;

  /**
   * Root constructor.
   */
  public Node() {
    this.father = null;
    this.children = new ArrayList<>();
    this.move = null;
    this.ownMove = false;
  }

  /**
   * Child constructor.
   */
  public Node(Node father, MoveSet move, boolean ownMove) {
    this.father = father;
    this.move = move;
    this.children = new ArrayList<>();
    this.ownMove = ownMove;
  }

  /**
   * Expands a new node.
   */
  public Node expand(MoveSet newMove) {
    Node child = new Node(this, newMove, !this.ownMove);
    this.children.add(child);

    return child;
  }

  /**
   * Fusions the children of this node with the children of another node.
   * This is only used with roots.
   */
  public void fusionWithOther(Node other) {
    List<Node> otherChildren = other.getChildren();
    for (Node otherChild : otherChildren) {
      boolean found = false;
      for (Node child : this.children) {
        if (child.getMove().equals(otherChild.getMove())) {
          found = true;
          child.updateVisit(otherChild.getVisitCount());
          child.updateAvailability(otherChild.getAvailabilityCount());
          child.updateWin(otherChild.getWin());
          break;
        }
      }
      if (!found) {
        this.children.add(otherChild);
      }
    }
  }

  /**
   * Checks if one of the available moves lead to an unexplored node.
   */
  public MoveSet searchUnexploredNode(List<MoveSet> legalMoves) {
    for (MoveSet move : legalMoves) {
      boolean found = false;
      for (Node child : this.children) {
        if (child.getMove().equals(move)) {
          found = true;
          break;
        }
      }
      if (!found) {
        return move;
      }
    }

    return null;
  }

  /**
   * Determines the "legal child" with the best UCT.
   */
  public Node determineBestUCTChild(List<MoveSet> legalMoves) {
    int bestUCT = -1;
    double currentUCT = -100.0;
    for (MoveSet move : legalMoves) {
      for (int i = 0; i < this.children.size(); i++) {
        if (this.children.get(i).getMove().equals(move)) {
          if (this.children.get(i).calculateUCT() > currentUCT) {
            bestUCT = i;
            currentUCT = this.children.get(i).calculateUCT();
          }
          // It is easier to increase the availability counts here instead during the backpropagation.
          //this.children.get(i).increaseAvailability();
          break;
        }
      }
    }

    return this.children.get(bestUCT);
  }

  /**
   * Determines the node with the most visits and therefore the chosen move.
   */
  public MoveSet determineMoveSet() {
    int node = 0;
    int mostVisits = this.children.get(0).getVisitCount();
    for (int i = 1; i < this.children.size(); i++) {
      if (this.children.get(i).getVisitCount() > mostVisits) {
        node = i;
        mostVisits = this.children.get(i).getVisitCount();
      }
    }

    return this.children.get(node).getMove();
  }

  /**
   * Calculates the UCT of itself.
   */
  public double calculateUCT() {
    if (this.ownMove) {
      // UCT by agent's view
      return (this.winCount / (double) this.visitCount) + this.CONSTANT_C * Math.sqrt(Math.log(this.availabilityCount) / (double) this.visitCount);
    }

    // UCT by opponent's view
    return 1.0 - (this.winCount / (double) this.visitCount) + this.CONSTANT_C * Math.sqrt(Math.log(this.availabilityCount) / this.visitCount);
  }

  /**
   * Updates the visitCount and the winCount.
   */
  public void update(double reward) {
    this.visitCount++;
    this.winCount += reward;
  }

  /**
   * Updates the AvailabilityCount of children that were available.
   */
  public void updateAvailabilityChildren() {
    for (Node child : this.children) {
      for (MoveSet move : this.currentLegalMoves) {
        if (child.getMove().equals(move)) {
          child.updateAvailability(1);
          break;
        }
      }
    }
  }

  /**
   * Sets the list of legal moves of the current determinization.
   */
  public void setCurrentLegalMoves(List<MoveSet> legalMoves) {
    this.currentLegalMoves = legalMoves;
  }

  /**
   * Updates the VisitCount.
   */
  public void updateVisit(int visit) {
    this.visitCount += visit;
  }

  /**
   * Updates the WinCount.
   */
  public void updateWin(double win) {
    this.winCount += win;
  }

  /**
   * Updates the AvailabilityCount.
   */
  public void updateAvailability(int availability) {
    this.availabilityCount += availability;
  }

  /**
   * Returns the WinCount.
   */
  public double getWin() {
    return this.winCount;
  }

  /**
   * Returns the AvailabilityCount.
   */
  public int getAvailabilityCount() {
    return this.availabilityCount;
  }

  /**
   * Returns the VisitCount.
   */
  public int getVisitCount() {
    return this.visitCount;
  }

  /**
   * Returns the Move that was used to reach this node.
   */
  public MoveSet getMove() {
    return this.move;
  }

  /**
   * Returns the father of this node.
   */
  public Node getFather() {
    return this.father;
  }

  /**
   * Returns a list of all children of this node.
   */
  public List<Node> getChildren() {
    return this.children;
  }
}
