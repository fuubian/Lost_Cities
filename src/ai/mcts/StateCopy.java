package ai.mcts;

import game.MoveSet;
import game.Card;
import game.GameState;
import game.PlayMove;
import game.TakeMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copy of the GameState including a determinization of opponent's cards and the pile.
 */
public abstract class StateCopy {

  /**
   * Hand cards of the agent.
   */
  protected final List<Card> playerCards;

  /**
   * Determinization of the opponent's hand cards.
   */
  protected final List<Card> opponentsCards;

  /**
   * Determinization of the pile.
   */
  protected List<Card> pile;

  /**
   * Includes all played expeditions.
   * 0: Own Red
   * 1: Own Green
   * 2: Own Blue
   * 3: Own White
   * 4: Own Yellow
   * 5-9: Opponent's Played Expedition Cards
   */
  protected final List<List<Card>> field;

  /**
   * Piles of discarded cards.
   * 0: Red
   * 1: Green
   * 2: Blue
   * 3: White
   * 4: Yellow
   */
  protected final List<List<Card>> discardedCards;

  /**
   * How many cards remain in the pile.
   */
  protected int cardsRemaining;

  /**
   * Which player's tun.
   * 0 - Player 1
   * 2 - Player 2
   * 4 - Game Over
   */
  protected int roundState;

  /**
   * Index of the agent.
   */
  protected final int player;

  /**
   * Index of the opponent.
   */
  protected final int opponent;

  /**
   * Constructor.
   * Copys all elements of the GameState.
   */
  public StateCopy(GameState state, int player, List<Card> knownOpponentCards) {
    this.player = player;
    this.opponent = player == 1 ? 2 : 1;

    this.playerCards = new ArrayList<>(8);
    this.playerCards.addAll(state.getPlayerCardsObject());
    this.field = new ArrayList<>(10);
    for (int i = 0; i < state.getField().size(); i++) {
      ArrayList<Card> tmp = new ArrayList<>(state.getField().get(i));
      this.field.add(tmp);
    }
    this.discardedCards = new ArrayList<>(5);
    for (int i = 0; i < 5; i++) {
      ArrayList<Card> tmp = new ArrayList<>(state.getDiscardedCards().size());
      tmp.addAll(state.getDiscardedCards().get(i));
      this.discardedCards.add(tmp);
    }
    this.cardsRemaining = state.getCardsRemaining();
    this.roundState = state.getRoundState();

    // randomize unknown cards
    this.opponentsCards = new ArrayList<>(8);
    this.opponentsCards.addAll(knownOpponentCards);
    this.createRandomCards(state);
  }

  /**
   * Choose a random move and apply it to the state.
   */
  public MoveSet executeRandomMove() {
    // randomize a move
    List<MoveSet> possibleMoves = this.getPossibleMoves();
    int random = (int) (Math.random() * possibleMoves.size());
    MoveSet move = possibleMoves.get(random);

    this.executeMove(move);

    return move;
  }

  /**
   * Checks if the move is valid and applies it.
   */
  public void executeMove(MoveSet move) {
    if ((this.player == 1 && this.roundState == 0) ||
            (this.player == 2 && this.roundState == 2)) {
      this.applyMove(move, this.playerCards, 0);
    } else {
      this.applyMove(move, this.opponentsCards, 5);
    }
    this.roundState = this.roundState == 0 ? 2 : 0;

    // check if game is over
    if (this.cardsRemaining == 0) {
      this.roundState = 4;
    }
  }

  /**
   * Calculates and returns all possible froms within the current state.
   */
  public List<MoveSet> getPossibleMoves() {
    List<MoveSet> possibleMoves = new ArrayList<>();

    if (this.player == 1 && this.roundState == 0 || this.player == 2 && this.roundState == 2) {
      // Own Move
      this.addMoveSets(possibleMoves, this.player, this.playerCards, 0);
      this.removeBadMoves(possibleMoves, this.playerCards, 0);
      if (possibleMoves.size() == 0) {
        // only bad moves are available
        possibleMoves = new ArrayList<>();
        this.addMoveSets(possibleMoves, this.player, this.playerCards, 0);
      }
    } else if (this.player == 1 && this.roundState == 2 || this.player == 2 && this.roundState == 0) {
      // Opponent's move
      this.addMoveSets(possibleMoves, this.opponent, this.opponentsCards, 5);
      this.removeBadMoves(possibleMoves, this.opponentsCards, 5);
      if (possibleMoves.size() == 0) {
        // only bad moves are available
        possibleMoves = new ArrayList<>();
        this.addMoveSets(possibleMoves, this.opponent, this.opponentsCards, 5);
      }
    }

    return possibleMoves;
  }

  /**
   * Deletes from a list of moves all obvious bad moves.
   */
  private void removeBadMoves(List<MoveSet> possibleMoves, List<Card> currentPlayerCards, int offset) {
    Main:
    for (int i = 0; i < possibleMoves.size(); i++) {
      // inspect PlayMove for mistakes
      PlayMove playMove = possibleMoves.get(i).getPlayMove();
      int colorPlayMove = playMove.getCardObject().getColor();
      if (playMove.getTarget() == 2) {
        // don't discard useful cards
        if (this.field.get(colorPlayMove + offset).size() > 0 &&
                this.field.get(colorPlayMove + offset).get(this.field.get(colorPlayMove + offset).size() - 1).getValue() <
                        playMove.getCardObject().getValue()) {
          possibleMoves.remove(i);
          i--;
          continue;
        }

        // don't give good cards to the opponent
        int opponentField = (colorPlayMove + 5 + offset) % 10;
        if (this.field.get(opponentField).size() > 0 && this.field.get(opponentField).get(this.field.get(opponentField).size() - 1).getValue() <
                playMove.getCardObject().getValue()) {
          possibleMoves.remove(i);
          i--;
          continue;
        }
      } else {
        int sameColor = 0;
        int colorValue = 0;
        for (Card card : currentPlayerCards) {
          if (card.getColor() == colorPlayMove) {
            if (card.getValue() < playMove.getCardObject().getValue() &&
                    card.getValue() != 0) {
              possibleMoves.remove(i);
              i--;
              continue Main;
            }

            sameColor++;
            colorValue += card.getValue();
          }
        }

        if (this.field.get(colorPlayMove + offset).size() == 0 && (sameColor <= 2 || colorValue <= 10)) {
          // don't open expedition if there is no hope for it to be successful
          possibleMoves.remove(i);
          i--;
          continue;
        }

        if (playMove.getCardObject().getValue() == 0 &&
                this.field.get(colorPlayMove).size() == 0 && (sameColor <= 3 || colorValue <= 12)) {
          // don't play a wager card if your hand cards are not good enough
          possibleMoves.remove(i);
          i--;
          continue;
        }
      }

      // inspect TakeMove for mistakes
      TakeMove takeMove = possibleMoves.get(i).getTakeMove();
      int colorTakeMove = takeMove.getTarget();
      if (colorTakeMove != 5) {
        int sameColor = 0;
        for (Card card : currentPlayerCards) {
          if (card.getColor() == colorPlayMove) {
            sameColor++;
          }
        }
        if ((this.field.get(colorTakeMove + offset).size() == 0 && sameColor <= 1) || (this.field.get(colorTakeMove + offset).size() > 0 &&
                this.field.get(colorTakeMove + offset).get(this.field.get(colorTakeMove + offset).size() - 1).getValue() >
                        this.discardedCards.get(colorTakeMove).get(this.discardedCards.get(colorTakeMove).size() - 1).getValue())) {
          // don't draw unnecessary cards from discard piles (when you are not losing)
          if (this.cardsRemaining <= 10) {
            int[] points = this.calculatePoints();
            if ((offset == 0 && points[0] > points[1]) || (offset == 5 && points[0] < points[1])) {
              possibleMoves.remove(i);
              i--;
            }
          } else {
            possibleMoves.remove(i);
            i--;
          }
        }
      }
    }
  }

  /**
   * Simulate the rest of the game.
   */
  public double simulateGame() {
    while (this.roundState != 4) {
      this.executeRandomMove();
    }

    int[] finalPoints = this.calculatePoints();
    if (finalPoints[0] > finalPoints[1]) {
      return 1.0;
    } else if (finalPoints[0] < finalPoints[1]) {
      return 0;
    }

    return 0.5;
  }

  /**
   * Calculate the points of both players.
   */
  public int[] calculatePoints() {
    int[] points = new int[2];

    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 5; j++) {
        if (this.field.get(j + (i * 5)).size() > 0) {
          int pointsField = -20;  // expedition cost
          int factor = 1;

          for (int x = 0; x < this.field.get(j + (i * 5)).size(); x++) {
            Card c = this.field.get(j + (i * 5)).get(x);

            if (c.getValue() == 0)
              factor++;
            else
              pointsField += c.getValue();
          }

          points[i] += factor * pointsField;
          if (this.field.get(j + (i * 5)).size() >= 8) {
            points[i] += 20;    // bonus for at least 8 cards in one expedition
          }
        }
      }
    }

    return points;
  }

  /**
   * Creates a list of all cards and deletes already known cords from this list.
   */
  private void createRandomCards(GameState state) {
    // create all Cards
    this.pile = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      for (int x = 2; x <= 10; x++) {
        this.pile.add(new Card(i, x));
      }
      this.pile.add(new Card(i, 0));
      this.pile.add(new Card(i, 0));
      this.pile.add(new Card(i, 0));
    }

    // removing known cards from pile
    this.removeCardsFromPile(state.getPlayerCardsObject());
    for (int i = 0; i < state.getField().size(); i++) {
      this.removeCardsFromPile(state.getField().get(i));
    }
    for (int i = 0; i < state.getDiscardedCards().size(); i++) {
      this.removeCardsFromPile(state.getDiscardedCards().get(i));
    }

    // remove already known cards
    this.removeCardsFromPile(this.opponentsCards);

    // randomize order
    Collections.shuffle(this.pile);

    // distribute first x cards to opponent
    for (int i = this.opponentsCards.size(); i < 8; i++) {
      this.opponentsCards.add(this.pile.get(0));
      this.pile.remove(0);
    }
  }

  /**
   * Removes known cards from the list.
   */
  private void removeCardsFromPile(List<Card> list) {
    for (Card c : list) {
      for (int i = 0; i < this.pile.size(); i++) {
        if (c.getColor() == this.pile.get(i).getColor()) {
          if (c.getValue() == this.pile.get(i).getValue()) {
            this.pile.remove(i);
            break;
          }
        }
      }
    }
  }

  /**
   * Add MoveSets to create a list of all legal moves.
   */
  protected void addMoveSets(List<MoveSet> moveList, int currentPlayer, List<Card> currentPlayerCards, int offset) {
    for (int i = 0; i < 8; i++) {
      // all combinations of discarding moves + drawing moves
      PlayMove discardMove = new PlayMove(currentPlayer, i + 1, currentPlayerCards.get(i), 2);
      int cardColor = currentPlayerCards.get(i).getColor();
      this.addTakeMoves(moveList, discardMove, currentPlayer, cardColor);

      // all combination of expedition moves + drawing moves
      if (this.field.get(cardColor + offset).size() == 0 ||
              this.field.get(cardColor + offset).get(this.field.get(cardColor + offset).size() - 1).getValue() <= currentPlayerCards.get(i).getValue()) {
        PlayMove expeditionMove = new PlayMove(currentPlayer, i + 1, currentPlayerCards.get(i), 1);
        this.addTakeMoves(moveList, expeditionMove, currentPlayer, -1);
      }
    }
  }

  /**
   * Combine TakeMoves with PlayMoves.
   */
  private void addTakeMoves(List<MoveSet> moveList, PlayMove playMove, int currentPlayer, int cardColor) {
    moveList.add(new MoveSet(playMove, new TakeMove(currentPlayer, 5)));
    for (int j = 0; j < 5; j++) {
      if (j != cardColor && this.discardedCards.get(j).size() > 0) {
        MoveSet moveSet = new MoveSet(playMove, new TakeMove(currentPlayer, j));
        moveList.add(moveSet);
      }
    }
  }

  /**
   * Applies the move to the state.
   */
  private void applyMove(MoveSet move, List<Card> currentPlayerCards, int offset) {
    // PlayMove Sequence
    PlayMove playMove = move.getPlayMove();

    if (playMove.getTarget() == 1) {
      this.field.get(playMove.getCardObject().getColor() + offset).add(playMove.getCardObject());
    } else {
      this.discardedCards.get(playMove.getCardObject().getColor()).add(playMove.getCardObject());
    }
    currentPlayerCards.remove(playMove.getCard() - 1);

    // TakeMove Sequence
    TakeMove takeMove = move.getTakeMove();

    if (takeMove.getTarget() == 5) {
      currentPlayerCards.add(this.pile.get(0));
      this.pile.remove(0);
      this.cardsRemaining--;
    } else {
      currentPlayerCards.add(this.discardedCards.get((takeMove.getTarget())).get(
              this.discardedCards.get((takeMove.getTarget())).size() - 1));
      this.discardedCards.get(takeMove.getTarget()).remove(this.discardedCards.get((takeMove.getTarget())).size() - 1);
    }
  }

  /**
   * Returns the RoundState.
   */
  public int getRoundState() {
    return this.roundState;
  }
}
