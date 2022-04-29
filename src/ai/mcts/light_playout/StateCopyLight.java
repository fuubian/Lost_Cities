package ai.mcts.light_playout;

import ai.mcts.StateCopy;
import game.MoveSet;
import game.Card;
import game.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * StateCopy of the LP-Agent.
 */
public class StateCopyLight extends StateCopy {

  /**
   * Constructor.
   */
  public StateCopyLight(GameState state, int player, List<Card> knownOpponentCards) {
    super(state, player, knownOpponentCards);
  }

  /**
   * Calculates and returns all possible froms within the current state.
   */
  @Override
  public List<MoveSet> getPossibleMoves() {
    List<MoveSet> possibleMoves = new ArrayList<>();

    if (this.player == 1 && this.roundState == 0 || this.player == 2 && this.roundState == 2) {
      // Own Move
      this.addMoveSets(possibleMoves, this.player, this.playerCards, 0);
    } else if (this.player == 1 && this.roundState == 2 || this.player == 2 && this.roundState == 0) {
      // Opponent's move
      this.addMoveSets(possibleMoves, this.opponent, this.opponentsCards, 5);
    }

    return possibleMoves;
  }

}
