package ai.mcts.heuristic_hard_playout;

import ai.mcts.StateCopy;
import game.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StateCopy for HHP-Agent.
 */
public class StateCopyHard2 extends StateCopy {

  /**
   * N-Value for the technique Best-Of-N.
   */
  private final int MAX_N = 12;

  /**
   * Constructor.
   */
  public StateCopyHard2(GameState state, int player, List<Card> knownOpponentCards) {
    super(state, player, knownOpponentCards);
  }

  /**
   * Executes the move with the best heuristic value to the current state and returns it.
   */
  public MoveSet executeRandomMove() {
    List<MoveSet> possibleMoves = this.getPossibleMoves();
    Collections.shuffle(possibleMoves);

    for (int i = MAX_N; i < possibleMoves.size(); ) {
      possibleMoves.remove(i);
    }

    MoveSet move = possibleMoves.get(0);
    int bestHeuristic = this.calculateHeuristic(move);

    if (move.getPlayMove().getPlayer() == this.player) {
      for (int i = 1; i < possibleMoves.size(); i++) {
        int tmpHeuristic = this.calculateHeuristic(possibleMoves.get(i));

        if (tmpHeuristic > bestHeuristic) {
          move = possibleMoves.get(i);
          bestHeuristic = tmpHeuristic;
        }
      }
    } else {
      for (int i = 1; i < possibleMoves.size(); i++) {
        int tmpHeuristic = this.calculateHeuristic(possibleMoves.get(i));

        if (tmpHeuristic < bestHeuristic) {
          move = possibleMoves.get(i);
          bestHeuristic = tmpHeuristic;
        }
      }
    }

    this.executeMove(move);
    return move;
  }

  /**
   * Applies the move to an internal copy of the saved state.
   */
  private void applyMoveCopy(MoveSet move, List<Card> currentPlayerCards, int offset, List<List<Card>> copyField,
                             List<List<Card>> copyDiscarded, List<Card> copyPile) {
    // PlayMove Sequence
    PlayMove playMove = move.getPlayMove();

    if (playMove.getTarget() == 1) {
      copyField.get(playMove.getCardObject().getColor() + offset).add(playMove.getCardObject());
    } else {
      copyDiscarded.get(playMove.getCardObject().getColor()).add(playMove.getCardObject());
    }
    currentPlayerCards.remove(playMove.getCard() - 1);

    // TakeMove Sequence
    TakeMove takeMove = move.getTakeMove();

    if (takeMove.getTarget() == 5) {
      currentPlayerCards.add(copyPile.get(0));
      copyPile.remove(0);
    } else {
      currentPlayerCards.add(copyDiscarded.get((takeMove.getTarget())).get(
              copyDiscarded.get((takeMove.getTarget())).size() - 1));
      copyDiscarded.get(takeMove.getTarget()).remove(copyDiscarded.get((takeMove.getTarget())).size() - 1);
    }
  }

  /**
   * Calculates the heuristic value of the state after a specific move.
   */
  private int calculateHeuristic(MoveSet move) {
    // Copy list of cards
    List<Card> copyPile = new ArrayList<>(this.pile.size());
    copyPile.addAll(this.pile);

    List<Card> copyPlayerCards = new ArrayList<>(8);
    copyPlayerCards.addAll(this.playerCards);

    List<Card> copyOpponentCards = new ArrayList<>(8);
    copyOpponentCards.addAll(this.opponentsCards);

    List<List<Card>> copyField = new ArrayList<>(10);
    for (int i = 0; i < 10; i++) {
      copyField.add(new ArrayList<>());
      copyField.get(i).addAll(this.field.get(i));
    }

    List<List<Card>> copyDiscard = new ArrayList<>(5);
    for (int i = 0; i < 5; i++) {
      copyDiscard.add(new ArrayList<>());
      copyDiscard.get(i).addAll(this.discardedCards.get(i));
    }

    // Execute move
    if (move.getPlayMove().getPlayer() == 1) {
      this.applyMoveCopy(move, copyPlayerCards, 0, copyField, copyDiscard, copyPile);
    } else {
      this.applyMoveCopy(move, copyOpponentCards, 5, copyField, copyDiscard, copyPile);
    }

    // Evaluate the new game state
    int heuristic = 0;

    int amountExpeditions = 0;
    // own expeditions
    for (int i = 0; i < 5; i++) {
      if (copyField.get(i).size() > 0) {
        amountExpeditions++;

        int points = 0;
        for (int j = 0; j < copyField.get(i).size(); j++) {
          int value = copyField.get(i).get(j).getValue();
          points += value;

          if (value == 0) {
            heuristic += 7;
          } else if (value >= 7 && copyPile.size() > 30) {
            heuristic -= value;
          } else if (value >= 9 && copyPile.size() > 25) {
            heuristic -= value;
          }
        }

        double factor;
        if (copyPile.size() < 15) {
          factor = 1.0;
        } else if (copyPile.size() < 28) {
          factor = 0.67;
        } else {
          factor = 0.33;
        }
        heuristic += (20 - points) * factor;
        heuristic += copyField.get(i).size() * 3;

        if (copyField.get(i).size() >= 8) {
          // bonus points
          heuristic += 20;
        }
      }
    }

    int enemyExpeditions = 0;
    // opponent's expedition
    for (int i = 5; i < 10; i++) {
      if (copyField.get(i).size() > 0) {
        enemyExpeditions++;

        int points = 0;
        for (int j = 0; j < copyField.get(i).size(); j++) {
          int value = copyField.get(i).get(j).getValue();
          points += value;

          if (value == 0) {
            heuristic -= 7;
          } else if (value >= 7 && copyPile.size() > 30) {
            heuristic += value;
          } else if (value >= 9 && copyPile.size() > 25) {
            heuristic += value;
          }
        }

        double factor;
        if (copyPile.size() < 15) {
          factor = 1.0;
        } else if (copyPile.size() < 28) {
          factor = 0.67;
        } else {
          factor = 0.33;
        }
        heuristic -= (20 - points) * factor;
        heuristic -= copyField.get(i).size() * 3;

        if (copyField.get(i).size() >= 8) {
          // bonus points
          heuristic -= 20;
        }
      }
    }

    // decrease heuristic for having no optimal number of open expeditions
    if (amountExpeditions == 0) {
      if (copyField.size() <= 35) {
        heuristic -= 10;
      }
      if (copyField.size() <= 25) {
        heuristic -= 20;
      }
    } else if (amountExpeditions == 1 && copyField.size() <= 25) {
      heuristic -= 10;
    } else if (amountExpeditions == 4) {
      heuristic -= 10;
    } else if (amountExpeditions == 5) {
      heuristic -= 20;
    }

    if (enemyExpeditions == 0) {
      if (copyField.size() <= 35) {
        heuristic += 10;
      }
      if (copyField.size() <= 25) {
        heuristic += 20;
      }
    } else if (enemyExpeditions == 1 && copyField.size() <= 25) {
      heuristic += 10;
    } else if (enemyExpeditions == 4) {
      heuristic += 10;
    } else if (enemyExpeditions == 5) {
      heuristic += 20;
    }

    return heuristic;
  }
}
