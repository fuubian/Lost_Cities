package AI.InformationSet;

import AI.ArtificialIntelligence;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

import java.util.List;

public class InformationSetAI extends ArtificialIntelligence {

    private final int player;
    private MoveSet move;

    private final long MAX_TIME = 3000;

    public InformationSetAI(int player) {
        this.player = player;
    }

    /**
     * Plays a card onto the discard pile or expedition pile.
     */
    public PlayMove playCard(GameState state) {
        long time = System.currentTimeMillis();
        this.move = this.createTree(state);
        time = time - System.currentTimeMillis();
        System.out.println("!!!ZEIT: " + time + " Milisekunden!!!");

        return this.move.getPlayMove();
    }

    /**
     * Draws a card from the discard pile or normal pile.
     */
    public TakeMove takeCard(GameState state) {
        return this.move.getTakeMove();
    }

    /**
     * Creates a SO-ISMCTS and determines the best MoveSet.
     */
    private MoveSet createTree(GameState state) {
        Node root = new Node();
        long time = System.currentTimeMillis();

        while (System.currentTimeMillis() - time <= this.MAX_TIME) {
            // Determinization
            StateCopy copy = new StateCopy(state, this.player);
            Node currentNode = root;
            boolean selectionCompleted = false;
            MoveSet selectedMove = null;

            // Descending and Selection
            while (true) {
                List<MoveSet> possibleMoves = copy.getPossibleMoves();
                if (possibleMoves.size() == 0) {
                    // no node exists to that move OR it is a terminal node
                    selectionCompleted = true;
                } else {
                    MoveSet move = currentNode.searchUnexploredNode(possibleMoves);
                    if (move == null) {
                        // Look for move with best UCT
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
                    } else if (copy.getRoundState() != 4) {
                        // no node exists
                        selectedMove = copy.executeRandomMove();
                        currentNode = currentNode.expand(selectedMove);
                    }
                } else {
                    continue;
                }

                // Simulation
                int reward = copy.simulateGame() ? 1 : 0;

                // Backpropagation
                while (currentNode.getFather() != null) {
                    currentNode.update(reward);
                    currentNode = currentNode.getFather();
                }

                break;
            }
        }

        return root.determineMoveSet();
    }
}
