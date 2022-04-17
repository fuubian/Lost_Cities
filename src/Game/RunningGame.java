package Game;

import AI.ArtificialIntelligence;
import AI.ImprovedRandomAI;
import AI.MCTS.InformationSetHard.InformationSetAIHard;
import AI.MCTS.InformationSetHard2.InformationSetAIHard2;
import AI.MCTS.InformationSetLight.InformationSetAILight;
import AI.RandomAI;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class RunningGame {

    static ArtificialIntelligence AI1, AI2;
    static Game game;
    static final boolean textActive = false;
    static final boolean analysisActive = true;
    static final int AMOUNT_GAMES = 31;

    /**
     * Static values that are used by the Information Set AIs.
     * This enables to test AI's with different c constants and
     * different max_n values.
     */
    public static final double C_VALUE1 = 0.7;
    public static final double C_VALUE2 = 0.7;
    public static final int MAX_N1 = 8;
    public static final int MAX_N2 = 8;

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        int[][] allPoints = new int[AMOUNT_GAMES][2];
        Main:
        for (int i = 0; i < AMOUNT_GAMES; i++) {
            game = new Game();
            AI1 = new InformationSetAIHard2(1);
            AI2 = new InformationSetAIHard(2);

            int counter = 0;
            while (game.getState() != 4) {
                counter++;
                if (counter == 150) {
                    System.out.println("Game cancelled because too many moves.");
                    i--;
                    continue Main;
                }

                if (textActive) {
                    System.out.println("Player 1's state:");
                    printField(game.getGameState(1));
                }
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
                if (textActive) {
                    System.out.println(p1Move);
                    System.out.println(t1Move);
                    System.out.println();
                }

                if (game.getState() == 4) {
                    break;
                }

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
                if (textActive) {
                    System.out.println(p2Move);
                    System.out.println(t2Move);
                    System.out.println();
                }
            }

            System.out.println("\n" + (i+1) + "th Game is over!");
            int[] points = game.getGameState(1).calculatePoints();
            allPoints[i] = points;
            System.out.println("Player 1: " + points[0]);
            System.out.println("Player 2: " + points[1]);
        }

        if (analysisActive) {
            int won = 0;
            int draws = 0;
            int highest = allPoints[0][0];
            int lowest = allPoints[0][0];
            int oppHighest = allPoints[0][1];
            int oppLowest = allPoints[0][1];
            long ownPoints = 0;
            long opponentPoints = 0;

            try {
                PrintWriter writer = new PrintWriter("results.csv");
                for (int i = 0; i < AMOUNT_GAMES; i++) {
                    writer.write(allPoints[i][0] + ";" + allPoints[i][1] + "\n");
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < AMOUNT_GAMES; i++) {
                if (allPoints[i][0] > highest) {
                    highest = allPoints[i][0];
                }
                if (allPoints[i][0] < lowest) {
                    lowest = allPoints[i][0];
                }
                if (allPoints[i][1] > oppHighest) {
                    oppHighest = allPoints[i][1];
                }
                if (allPoints[i][1] < oppLowest) {
                    oppLowest = allPoints[i][1];
                }

                ownPoints += allPoints[i][0];
                opponentPoints += allPoints[i][1];
                if (allPoints[i][0] > allPoints[i][1]) {
                    won++;
                } else if (allPoints[i][0] == allPoints[i][1]) {
                    draws++;
                }
            }

            System.out.println("\nGames Won: " + won + "/" + AMOUNT_GAMES);
            System.out.println("Games Drawn: " + draws + "/" + AMOUNT_GAMES);
            System.out.println("Games Lost: " + (AMOUNT_GAMES-won-draws) + "/" + AMOUNT_GAMES);
            System.out.println("Average Points: " + (ownPoints / (double) AMOUNT_GAMES));
            System.out.println("Average Opponent Points: " + (opponentPoints / (double) AMOUNT_GAMES));
            System.out.println("Highest Points: " + highest);
            System.out.println("Lowest Points: " + lowest);
            System.out.println("Opponent Highest Points: " + oppHighest);
            System.out.println("Opponent Lowest Points: " + oppLowest);
        }

        System.out.println("Time needed: " + (System.currentTimeMillis()-time));
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
