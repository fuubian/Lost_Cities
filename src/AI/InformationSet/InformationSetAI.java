package AI.InformationSet;

import AI.ArtificialIntelligence;
import Game.MoveSet;
import Game.Card;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

import java.util.ArrayList;
import java.util.List;

public class InformationSetAI extends ArtificialIntelligence {

    private final int player;
    private MoveSet move;

    private final int AMOUNT_THREADS = 3;

    /**
     * A list of all opponent's cards that are known.
     */
    private List<Card> knownOpponentCards;

    private final long MAX_TIME = 10000;

    public InformationSetAI(int player) {
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
        List<MCTSAgent> agents = new ArrayList<>(AMOUNT_THREADS+1);
        List<MCTSThread> threads = new ArrayList<>(AMOUNT_THREADS);

        for (int i = 0; i < AMOUNT_THREADS; i++) {
            agents.add(new MCTSAgent(state, this.player, this.knownOpponentCards));
            threads.add(new MCTSThread(agents.get(i)));
        }
        agents.add(new MCTSAgent(state, this.player, this.knownOpponentCards));

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

        Node root = agents.get(0).getRoot();
        for (int i = 1; i < AMOUNT_THREADS+1; i++) {
            root.fusionWithOther(agents.get(i).getRoot());
        }

        return root.determineMoveSet();
    }
}
