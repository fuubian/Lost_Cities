package game;

/**
 * Representation of a card.
 */
public class Card {

  /**
   * Color of the card.
   * 0 - Red
   * 1 - Green
   * 2 - Blue
   * 3 - White
   * 4 - Yellow
   */
  private int color;

  /**
   * Value of the card.
   * 0 -   Wager Card
   * 2-10: Expedition Card
   */
  private int value;

  /**
   * Constructor.
   */
  public Card(int color, int value) {
    this.color = color;
    this.value = value;
  }

  /**
   * Compares the current card object with another card object.
   */
  public boolean equals(Card other) {
    return this.color == other.getColor() && this.value == other.getValue();
  }

  /**
   * Returns the color.
   */
  public int getColor() {
    return this.color;
  }

  /**
   * Returns the value.
   */
  public int getValue() {
    return this.value;
  }

  /**
   * String representation of the card object.
   */
  @Override
  public String toString() {
    String text = "";
    switch (this.color) {
      case 0:
        text += "Red";
        break;
      case 1:
        text += "Green";
        break;
      case 2:
        text += "Blue";
        break;
      case 3:
        text += "White";
        break;
      case 4:
        text += "Yellow";
        break;
    }

    return text + value;
  }

}
