package Client;

import AI.RandomAI;
import Game.Card;
import Game.GameState;
import Game.PlayMove;
import Game.TakeMove;

import javax.crypto.spec.PSource;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Client {

    public static final int port = 8421;
    public static int player;

    public static GameState state;
    public static RandomAI AI;

    public static ObjectOutputStream out;
    public static ObjectInputStream in;

    /**
     * Play a card!
     * @throws IOException
     */
    public static void playCard() throws IOException {
        PlayMove move = AI.playCard(state);
        System.out.println(move);

        out.writeObject(move);
        out.flush();
    }

    /**
     * Take a card!
     * @throws IOException
     */
    public static void takeCard() throws IOException {
        TakeMove move = AI.takeCard(state);
        System.out.println(move);

        out.writeObject(move);
        out.flush();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Socket socket = new Socket("localhost", port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        player = in.readInt();  // are we player 1 or player 2
        AI = new RandomAI(player);

        while (true) {  // Game Loop
            //Thread.sleep(1000);
            state = (GameState) in.readObject();
            printField(state);

            if (state.getState() == 4)
                break;

            if ((state.getState() == 0 && player == 1) || (state.getState() == 2 && player == 2)) {
                while (true) {  // Play Card Loop (until valid)
                    playCard();
                    int success = in.readInt();

                    if (success == 1) {
                        break;
                    }
                    System.out.println("Invalid move. Try again.");
                }
            }

            state = (GameState) in.readObject();
            printField(state);

            if ((state.getState() == 1 && player == 1) || (state.getState() == 3 && player == 2)) {
                while (true) {  // Take Card Loop (until valid)
                    takeCard();
                    int success = in.readInt();

                    if (success == 1) {
                        break;
                    }
                    System.out.println("Invalid move. Try again.");
                }
            }
        }

        System.out.println("Game is over.");

        int[] points = state.calculatePoints();
        System.out.println("Player 1: " + points[0] + " Points.");
        System.out.println("Player 2: " + points[1] + " Points.");
    }

    public static void printField(GameState state) {
        List<List<Card>> field = state.getField();
        List<List<Card>> discarded = state.getDiscardedCards();

        String[] cards = state.getPlayerCards();
        System.out.print("Your cards: ");
        for (String c : cards) {
            System.out.print(c + ", ");
        }
        System.out.println();

        System.out.println("--------------------------------------------------");
        System.out.println("Opponent's Played Expedition Cards: ");

        System.out.print("| ");
        for (int i = 5; i < 10; i++) {
            if (field.get(i).size() > 0) {
                System.out.print(field.get(i).get(field.get(i).size()-1) + " | ");
            } else {
                System.out.print("--- | ");
            }
        }
        System.out.println();

        System.out.println("--------------------------------------------------");
        System.out.println("Discarded Cards: ");
        System.out.print("| ");
        for (int i = 0; i < 5; i++) {
            if (discarded.get(i).size() > 0) {
                System.out.print(discarded.get(i).get(discarded.get(i).size()-1) + " | ");
            } else {
                System.out.print("--- | ");
            }
        }
        System.out.println();

        System.out.println("--------------------------------------------------");
        System.out.println("Your Played Expedition Cards: ");

        System.out.print("| ");
        for (int i = 0; i < 5; i++) {
            if (field.get(i).size() > 0) {
                System.out.print(field.get(i).get(field.get(i).size()-1) + " | ");
            } else {
                System.out.print("--- | ");
            }
        }
        System.out.println();

        System.out.println("--------------------------------------------------");
        System.out.println("Cards left in the deck: " + state.getCardsRemaining());
        System.out.println();
    }

}
