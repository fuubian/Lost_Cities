package AI.InformationSet2;

import AI.ArtificialIntelligence;
import Game.Card;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InformationSetAI2 extends ArtificialIntelligence {

    private final int player;
    private MoveSet move;

    /**
     * A list of all opponent's cards that are known.
     */
    private List<Card> knownOpponentCards;

    private final long MAX_TIME = 10000;

    public InformationSetAI2(int player) {
        this.player = player;
        this.knownOpponentCards = new ArrayList<>();
    }

    /**
     * Plays a card onto the discard pile or expedition pile.
     */
    public PlayMove playCard(GameState state) {
        if (this.player == 1) {
            System.out.print("Opponent Cards: ");
            for (Card card : knownOpponentCards) {
                System.out.print(card + " ");
            }
            System.out.println();
        }
        this.move = this.createTree(state);

        return this.move.getPlayMove();
    }

    /**
     * Draws a card from the discard pile or normal pile.
     */
    public TakeMove takeCard(GameState state) {
        return this.move.getTakeMove();
    }

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

    @Override
    public void receiveOpponentTakeMove(Card card) {
        this.knownOpponentCards.add(card);
    }

    /**
     * Creates a SO-ISMCTS and determines the best MoveSet.
     */
    private MoveSet createTree(GameState state) {
        Node root = new Node();

        long time = System.currentTimeMillis();

        while (System.currentTimeMillis() - time <= this.MAX_TIME) {
            //for (int i = 0; i < 1000; i++) {
            // Determinization
            StateCopy copy = new StateCopy(state, this.player, this.knownOpponentCards);
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
                    //Collections.shuffle(possibleMoves);
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
                    } else if (copy.getRoundState() != 4) {
                        // no node exists
                        selectedMove = copy.executeRandomMove();
                        currentNode = currentNode.expand(selectedMove);
                    }
                } else {
                    copy.executeMove(currentNode.getMove());
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
