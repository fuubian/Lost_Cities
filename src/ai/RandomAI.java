package ai;

import game.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the "Zufallsbasierte KI".
 */
public class RandomAI extends ArtificialIntelligence {

  /**
   * Player index.
   */
  private final int player;

  /**
   * The chosen move of the AI.
   */
  private MoveSet move;

  /**
   * Constructor.
   */
  public RandomAI(int player) {
    this.player = player;
  }

  /**
   * Plays a card onto the discard pile or expedition pile.
   */
  public PlayMove playCard(GameState state) {
    List<MoveSet> possibleMoves = this.getPossibleMoves(state);
    this.move = possibleMoves.get((int) (Math.random() * possibleMoves.size()));

    return this.move.getPlayMove();
  }

  /**
   * Draws a card from the discard pile or normal pile.
   */
  public TakeMove takeCard(GameState state) {
    return this.move.getTakeMove();
  }

  /**
   * Calculates and returns all possible froms within the current state.
   */
  public List<MoveSet> getPossibleMoves(GameState state) {
    List<MoveSet> possibleMoves = new ArrayList<>();
    List<Card> playerCards = state.getPlayerCardsObject();
    for (int i = 0; i < 8; i++) {
      // all combinations of discarding moves + drawing moves
      PlayMove discardMove = new PlayMove(this.player, i + 1, playerCards.get(i), 2);
      int cardColor = playerCards.get(i).getColor();
      this.addTakeMoves(possibleMoves, discardMove, cardColor, state);

      // all combination of expedition moves + drawing moves
      if (state.getField().get(cardColor).size() == 0 ||
              state.getField().get(cardColor).get(state.getField().get(cardColor).size() - 1).getValue() <= playerCards.get(i).getValue()) {
        PlayMove expeditionMove = new PlayMove(this.player, i + 1, playerCards.get(i), 1);
        this.addTakeMoves(possibleMoves, expeditionMove, -1, state);
      }
    }

    return possibleMoves;
  }

  private void addTakeMoves(List<MoveSet> moveList, PlayMove playMove, int cardColor, GameState state) {
    for (int j = 0; j < 5; j++) {
      if (j != cardColor && state.getDiscardedCards().get(j).size() > 0) {
        MoveSet moveSet = new MoveSet(playMove, new TakeMove(this.player, j));
        moveList.add(moveSet);
      }
    }
    moveList.add(new MoveSet(playMove, new TakeMove(this.player, 5)));
  }

  @Override
  public void receiveOpponentPlayMove(PlayMove move) {

  }

  @Override
  public void receiveOpponentTakeMove(Card card) {

  }
}
