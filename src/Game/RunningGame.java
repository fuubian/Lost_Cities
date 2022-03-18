package Game;

import AI.ArtificialIntelligence;
import AI.InformationSet.InformationSetAI;
import AI.InformationSet2.InformationSetAI2;
import AI.RandomAI;
import Client.Client;

public class RunningGame {

    static ArtificialIntelligence AI1, AI2;
    static Game game;

    public static void main(String[] args) {
        game = new Game();
        AI1 = new InformationSetAI2(1);
        AI2 = new InformationSetAI(2);

        while (game.getState() != 4) {
            System.out.println("Player 1's state:");
            Client.printField(game.getGameState(1));
            PlayMove p1Move = AI1.playCard(game.getGameState(1));
            game.executeMove(p1Move);
            AI2.receiveOpponentPlayMove(p1Move);
            TakeMove t1Move = AI1.takeCard(game.getGameState(1));
            if (t1Move.getTarget() != 5) {
                Card takenCard = game.getGameState(1).getDiscardedCards().get(t1Move.getTarget()).get(
                        game.getGameState(1).getDiscardedCards().get(t1Move.getTarget()).size() - 1);
                AI2.receiveOpponentTakeMove(takenCard);
            }
            game.executeMove(t1Move);
            System.out.println(p1Move);
            System.out.println(t1Move);
            System.out.println();

            if (game.getState() == 4) {
                break;
            }

            //System.out.println("Player 2's state:");
            //Client.printField(game.getGameState(2));
            PlayMove p2Move = AI2.playCard(game.getGameState(2));
            game.executeMove(p2Move);
            AI1.receiveOpponentPlayMove(p2Move);
            TakeMove t2Move = AI2.takeCard(game.getGameState(2));
            if (t2Move.getTarget() != 5) {
                Card takenCard = game.getGameState(2).getDiscardedCards().get(t2Move.getTarget()).get(
                        game.getGameState(2).getDiscardedCards().get(t2Move.getTarget()).size() - 1);
                AI1.receiveOpponentTakeMove(takenCard);
            }
            game.executeMove(t2Move);
            System.out.println(p2Move);
            System.out.println(t2Move);
            System.out.println();
        }

        System.out.println("\nGame is over!");
        int[] points = game.getGameState(1).calculatePoints();
        System.out.println("Player 1: " + points[0]);
        System.out.println("Player 2: " + points[1]);
    }
}
