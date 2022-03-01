package Game;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Serializable {

    private String color;
    private int value;

    public Card (String color, int value) {
        this.color = color;
        this.value = value;
    }

    public String getColor() {return this.color;}
    public int getValue() {return this.value;}

    public int getColorCode() {
        switch (this.color) {
            case "Red":
                return 0;
            case "Green":
                return 1;
            case "Blue":
                return 2;
            case "White":
                return 3;
            case "Yellow":
                return 4;
        }

        return -1;
    }

    @Override
    public String toString() {
        return color+value;
    }

}
