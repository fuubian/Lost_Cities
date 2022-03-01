package AI.InformationSet;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private Node father = null;
    private List<Node> children;

    private int winCount = 0;
    private int visitCount = 0;
    private int availabilityCount = 0;

    // Which move was executed to reach this node.
    private MoveSet move = null;

    // root constructor
    public Node() {
        this.children = new ArrayList<>();
    }

    // child constructor
    public Node(Node father, MoveSet move) {
        this.father = father;
        this.move = move;
        this.children = new ArrayList<>();
    }

    public void expand(MoveSet newMove) {
        Node child = new Node(this, newMove);
        this.children.add(child);
    }

    // Returns the child for a move if available
    public Node containsMove(MoveSet moveSet) {
        for (Node child : this.children) {
            if (child.getMove() == moveSet) {
                return child;
            }
        }

        return null;
    }

    public MoveSet getMove() {
        return this.move;
    }
}
