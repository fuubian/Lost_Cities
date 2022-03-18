package Game;

import AI.InformationSet.MoveSet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int port = 8421;

    public static Game game;

    public static Socket player1;
    public static ObjectOutputStream out1;
    public static ObjectInputStream in1;
    public static Socket player2;
    public static ObjectOutputStream out2;
    public static ObjectInputStream in2;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket host = new ServerSocket(port);

        player1 = host.accept();
        out1 = new ObjectOutputStream(player1.getOutputStream());
        in1 = new ObjectInputStream(player1.getInputStream());
        System.out.println("Player 1 connected.");
        out1.writeInt(1);
        out1.flush();

        player2 = host.accept();
        out2 = new ObjectOutputStream(player2.getOutputStream());
        in2 = new ObjectInputStream(player2.getInputStream());
        System.out.println("Player 2 connected.");
        out2.writeInt(2);
        out2.flush();

        game = new Game();

        System.out.println("Start game");
        while (game.getState() != 4) {
            //Thread.sleep(2000);
            sendStates();
            PlayMove pMove1;
            while (true) {  // until valid move was received (play card)
                pMove1 = (PlayMove) in1.readObject();
                if (game.executeMove(pMove1)) {
                    out1.writeInt(1);   // success
                    out1.flush();
                    break;
                } else {
                    out1.writeInt(-1);   // failure
                    out1.flush();
                }
            }

            sendStates();
            TakeMove tMove1;
            while (true) {  // until valid move was received (take card)
                tMove1 = (TakeMove) in1.readObject();
                if (game.executeMove(tMove1)) {
                    out1.writeInt(1);   // success
                    out1.flush();
                    break;
                } else {
                    out1.writeInt(-1);   // failure
                    out1.flush();
                }
            }

            if (game.getState() == 4)
                break;

            sendStates();
            while (true) {  // until valid move was received (play card)
                PlayMove move = (PlayMove) in2.readObject();
                if (game.executeMove(move)) {
                    out2.writeInt(1);   // success
                    out2.flush();
                    break;
                } else {
                    out2.writeInt(-1);   // failure
                    out2.flush();
                }
            }

            sendStates();
            while (true) {  // until valid move was received (take card)
                TakeMove move = (TakeMove) in2.readObject();
                if (game.executeMove(move)) {
                    out2.writeInt(1);   // success
                    out2.flush();
                    break;
                } else {
                    out2.writeInt(-1);   // failure
                    out2.flush();
                }
            }
        }

        sendStates();
        System.out.println("Game is over!");
    }

    /**
     * Sends the current gamestate to both players
     * @throws IOException
     */
    public static void sendStates() throws IOException {
        out1.writeObject(game.getGameState(1));
        out1.flush();
        out1.reset();
        out2.writeObject(game.getGameState(2));
        out2.flush();
        out2.reset();
    }

}
