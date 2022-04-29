package game;

import java.util.List;

/**
 * Current GameState of the Game object.
 */
public class GameState {

  /**
   * All player's hand cards.
   */
  private final List<Card> playerCards;

  /**
   * Includes all played expeditions.
   * 0: Own Red
   * 1: Own Green
   * 2: Own Blue
   * 3: Own White
   * 4: Own Yellow
   * 5-9: Opponent's Played Expedition Cards
   */
  private final List<List<Card>> field;

  /**
   * Piles of discarded cards.
   * 0: Red
   * 1: Green
   * 2: Blue
   * 3: White
   * 4: Yellow
   */
  private final List<List<Card>> discardedCards;

  /**
   * How many cards remain in the pile.
   */
  private final int cardsRemaining;

  /**
   * Which player's tun.
   * 0 - Player 1
   * 2 - Player 2
   * 4 - Game Over
   */
  private final int roundState;

  /**
   * Constructor.
   */
  public GameState(List<Card> playerCards, List<List<Card>> field, List<List<Card>> discardedCards, int cardsRemaining, int roundState) {
    this.playerCards = playerCards;
    this.field = field;
    this.discardedCards = discardedCards;
    this.cardsRemaining = cardsRemaining;
    this.roundState = roundState;
  }

  /**
   * Calculates the points of both players.
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
   * Returns the player's hand cards as string representation.
   */
  public String[] getPlayerCards() {
    String[] cards = new String[this.playerCards.size()];
    for (int i = 0; i < cards.length; i++) {
      cards[i] = this.playerCards.get(i).toString();
    }
    return cards;
  }

  /**
   * Returns the player's hand cards as Card objects.
   */
  public List<Card> getPlayerCardsObject() {
    return this.playerCards;
  }

  /**
   * Returns the field.
   */
  public List<List<Card>> getField() {
    return this.field;
  }

  /**
   * Returns the discarded cards.
   */
  public List<List<Card>> getDiscardedCards() {
    return this.discardedCards;
  }

  /**
   * Returns the amount of remaining cards.
   */
  public int getCardsRemaining() {
    return this.cardsRemaining;
  }

  /**
   * Returns the RoundState.
   */
  public int getRoundState() {
    return this.roundState;
  }
}
