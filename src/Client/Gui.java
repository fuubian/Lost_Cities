package Client;

import AI.ArtificialIntelligence;
import AI.InformationSet.InformationSetAI;
import Game.Game;
import Game.GameState;
import Game.Card;
import Game.PlayMove;
import Game.TakeMove;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.util.Objects;

public class Gui extends JFrame {

    private final Game game;
    private final ArtificialIntelligence AI;
    private String[] cards;

    private final int POS_Y = 537;
    private final int HEIGHT = 113;
    private final int WIDTH = 70;

    private final List<JButton> playerCards;
    private final List<JButton> discardedCards;
    private final List<JButton> playedField;

    private int selectedCard = -1;
    private final JLabel lbTurn;
    private final JLabel lbInstruction;
    private final JLabel lbCardsLeft;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Gui frame = new Gui();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public Gui() {
        this.game = new Game();
        this.AI = new InformationSetAI(2);
        this.cards = this.game.getGameState(1).getPlayerCards();

        setResizable(false);
        setTitle("Lost Cities");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 705, 700);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblCardsLeft = new JLabel("Cards Left:");
        lblCardsLeft.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblCardsLeft.setBounds(20, 320, 70, 14);
        contentPane.add(lblCardsLeft);

        lbCardsLeft = new JLabel("44");
        lbCardsLeft.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lbCardsLeft.setBounds(78, 320, 46, 14);
        contentPane.add(lbCardsLeft);

        JButton btnOwnCard1 = new JButton("");
        btnOwnCard1.addActionListener(e -> {
            if (game.getState() == 0)
                selectCard(0);
        });
        btnOwnCard1.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[0] + ".png"))));
        btnOwnCard1.setBounds(20, 537, 70, 113);
        contentPane.add(btnOwnCard1);

        lbTurn = new JLabel("Your Turn: ");
        lbTurn.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lbTurn.setBounds(10, 472, 91, 22);
        contentPane.add(lbTurn);

        lbInstruction = new JLabel("Select Card");
        lbInstruction.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lbInstruction.setBounds(100, 472, 118, 22);
        contentPane.add(lbInstruction);

        JButton btnOwnCard2 = new JButton("");
        btnOwnCard2.addActionListener(e -> {
            if (game.getState() == 0)
                selectCard(1);
        });
        btnOwnCard2.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnOwnCard2.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[1] + ".png"))));
        btnOwnCard2.setBounds(100, 538, 70, 112);
        contentPane.add(btnOwnCard2);

        JButton btnOwnCard3 = new JButton("");
        btnOwnCard3.addActionListener(e -> {
            if (game.getState() == 0)
                selectCard(2);
        });
        btnOwnCard3.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnOwnCard3.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[2] + ".png"))));
        btnOwnCard3.setBounds(180, 538, 70, 112);
        contentPane.add(btnOwnCard3);

        JButton btnOwnCard4 = new JButton("");
        btnOwnCard4.addActionListener(e -> {
            if (game.getState() == 0)
                selectCard(3);
        });
        btnOwnCard4.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnOwnCard4.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[3] + ".png"))));
        btnOwnCard4.setBounds(260, 538, 70, 112);
        contentPane.add(btnOwnCard4);

        JButton btnOwnCard5 = new JButton("");
        btnOwnCard5.addActionListener(e -> {
            if (game.getState() == 0)
                selectCard(4);
        });
        btnOwnCard5.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnOwnCard5.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[4] + ".png"))));
        btnOwnCard5.setBounds(340, 538, 70, 112);
        contentPane.add(btnOwnCard5);

        JButton btnOwnCard6 = new JButton("");
        btnOwnCard6.addActionListener(e -> {
            if (game.getState() == 0)
                selectCard(5);
        });
        btnOwnCard6.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnOwnCard6.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[5] + ".png"))));
        btnOwnCard6.setBounds(420, 538, 70, 112);
        contentPane.add(btnOwnCard6);

        JButton btnOwnCard7 = new JButton("");
        btnOwnCard7.addActionListener(e -> {
            if (game.getState() == 0)
                selectCard(6);
        });
        btnOwnCard7.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnOwnCard7.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[6] + ".png"))));
        btnOwnCard7.setBounds(500, 538, 70, 112);
        contentPane.add(btnOwnCard7);

        JButton btnOwnCard8 = new JButton("");
        btnOwnCard8.addActionListener(e -> {
            if (game.getState() == 0)
                selectCard(7);
        });
        btnOwnCard8.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnOwnCard8.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[7] + ".png"))));
        btnOwnCard8.setBounds(580, 538, 70, 112);
        contentPane.add(btnOwnCard8);

        JSeparator separator = new JSeparator();
        separator.setBounds(-11, 505, 974, 2);
        contentPane.add(separator);

        JButton redDiscard = new JButton("");
        redDiscard.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/Red.png"))));
        redDiscard.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(1);
            else if (game.getState() == 1)
                drawCard(1);
        });
        redDiscard.setBounds(127, 205, 70, 113);
        contentPane.add(redDiscard);

        JButton greenDiscard = new JButton("");
        greenDiscard.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/Green.png"))));
        greenDiscard.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(2);
            else if (game.getState() == 1)
                drawCard(2);
        });
        greenDiscard.setBounds(237, 205, 70, 113);
        contentPane.add(greenDiscard);

        JButton blueDiscard = new JButton("");
        blueDiscard.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/Blue.png"))));
        blueDiscard.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(3);
            else if (game.getState() == 1)
                drawCard(3);
        });
        blueDiscard.setBounds(348, 205, 70, 113);
        contentPane.add(blueDiscard);

        JButton whiteDiscard = new JButton("");
        whiteDiscard.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/White.png"))));
        whiteDiscard.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(4);
            else if (game.getState() == 1)
                drawCard(4);
        });
        whiteDiscard.setBounds(461, 205, 70, 113);
        contentPane.add(whiteDiscard);

        JButton yellowDiscard = new JButton("");
        yellowDiscard.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/Yellow.png"))));
        yellowDiscard.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(5);
            else if (game.getState() == 1)
                drawCard(5);
        });
        yellowDiscard.setBounds(571, 205, 70, 113);
        contentPane.add(yellowDiscard);

        this.playerCards = new ArrayList<>();
        this.playerCards.add(btnOwnCard1);
        this.playerCards.add(btnOwnCard2);
        this.playerCards.add(btnOwnCard3);
        this.playerCards.add(btnOwnCard4);
        this.playerCards.add(btnOwnCard5);
        this.playerCards.add(btnOwnCard6);
        this.playerCards.add(btnOwnCard7);
        this.playerCards.add(btnOwnCard8);

        this.discardedCards = new ArrayList<>();
        this.discardedCards.add(redDiscard);
        this.discardedCards.add(greenDiscard);
        this.discardedCards.add(blueDiscard);
        this.discardedCards.add(whiteDiscard);
        this.discardedCards.add(yellowDiscard);

        JButton normalPile = new JButton("");
        normalPile.setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/back.png"))));
        normalPile.addActionListener(e -> {
            if (game.getState() == 1)
                drawCard(0);
        });
        normalPile.setBounds(20, 205, 70, 113);
        contentPane.add(normalPile);

        JButton redPlay = new JButton("");
        redPlay.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(6);
        });
        redPlay.setBounds(127, 345, 70, 112);
        contentPane.add(redPlay);

        JButton greenPlay = new JButton("");
        greenPlay.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(7);
        });
        greenPlay.setBounds(237, 345, 70, 112);
        contentPane.add(greenPlay);

        JButton bluePlay = new JButton("");
        bluePlay.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(8);
        });
        bluePlay.setBounds(348, 345, 70, 112);
        contentPane.add(bluePlay);

        JButton whitePlay = new JButton("");
        whitePlay.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(9);
        });
        whitePlay.setBounds(461, 345, 70, 112);
        contentPane.add(whitePlay);

        JButton yellowPlay = new JButton("");
        yellowPlay.addActionListener(e -> {
            if (game.getState() == 0)
                playCard(10);
        });
        yellowPlay.setBounds(571, 345, 70, 112);
        contentPane.add(yellowPlay);

        JButton redOpponent = new JButton("");
        redOpponent.setBounds(127, 67, 70, 112);
        contentPane.add(redOpponent);

        JButton greenOpponent = new JButton("");
        greenOpponent.setBounds(237, 67, 70, 112);
        contentPane.add(greenOpponent);

        JButton blueOpponent = new JButton("");
        blueOpponent.setBounds(348, 67, 70, 112);
        contentPane.add(blueOpponent);

        JButton whiteOpponent = new JButton("");
        whiteOpponent.setBounds(461, 67, 70, 112);
        contentPane.add(whiteOpponent);

        JButton yellowOpponent = new JButton("");
        yellowOpponent.setBounds(571, 67, 70, 112);
        contentPane.add(yellowOpponent);

        this.playedField = new ArrayList<>();
        this.playedField.add(redPlay);
        this.playedField.add(greenPlay);
        this.playedField.add(bluePlay);
        this.playedField.add(whitePlay);
        this.playedField.add(yellowPlay);
        this.playedField.add(redOpponent);
        this.playedField.add(greenOpponent);
        this.playedField.add(blueOpponent);
        this.playedField.add(whiteOpponent);
        this.playedField.add(yellowOpponent);
    }

    /**
     * @param nr: determines which playerCard is played (0-7).
     */
    private void selectCard(int nr) {
        if (this.selectedCard != nr) {
            if (this.selectedCard != -1) {
                this.playerCards.get(this.selectedCard).setBounds(this.playerCards.get(this.selectedCard).getBounds().x, POS_Y, WIDTH, HEIGHT);
            } else {
                lbInstruction.setText("Select Pile");
            }

            this.selectedCard = nr;
            this.playerCards.get(nr).setBounds(this.playerCards.get(nr).getBounds().x, POS_Y-20, WIDTH, HEIGHT);
        }
    }

    /**
     * @param pile: determines on which pile the card is played
     */
    private void playCard(int pile) {
        if (this.selectedCard == -1) {
            // No card was selected
            JOptionPane.showMessageDialog(new JFrame(), "Please select first one of your cards.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (((pile == 1 || pile == 6) && this.cards[this.selectedCard].matches("Red[0-9]+"))
            || ((pile == 2 || pile == 7) && this.cards[this.selectedCard].matches("Green[0-9]+"))
            || ((pile == 3 || pile == 8) && this.cards[this.selectedCard].matches("Blue[0-9]+"))
            || ((pile == 4 || pile == 9) && this.cards[this.selectedCard].matches("White[0-9]+"))
            || ((pile == 5 || pile == 10) && this.cards[this.selectedCard].matches("Yellow[0-9]+"))) {
                int target = pile >= 6 ? 1 : 2;
                if (this.game.executeMove(new PlayMove(1, this.selectedCard+1,
                        this.game.getGameState(1).getPlayerCardsObject().get(this.selectedCard), target))) {
                    updateField(this.game.getGameState(1));
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Illegal move.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(new JFrame(), "Illegal move.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @param pile: determines from which pile a card is drawn
     */
    private void drawCard(int pile) {
        if (pile == 0) {
            this.game.executeMove(new TakeMove(1, 5));
        } else {
            if (!this.game.executeMove(new TakeMove(1, pile-1))) {
                JOptionPane.showMessageDialog(new JFrame(), "Illegal move.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        this.updateField(this.game.getGameState(1));
        this.executeAIMoves();
    }

    /**
     * Updates the GUI.
     */
    private void updateField(GameState state) {

        // update State
        if (state.getState() == 0) {
            lbTurn.setText("Your Turn:");
            lbInstruction.setText("Select card");
        }
        else if (state.getState() == 1) {
            this.playerCards.get(this.selectedCard).setBounds(this.playerCards.get(this.selectedCard).getBounds().x, POS_Y, WIDTH, HEIGHT);
            this.selectedCard = -1;
            lbInstruction.setText("Draw a card");
        }
        else if (state.getState() == 2) {
            lbTurn.setText("AI's Turn:");
            lbInstruction.setText("Please wait");
        }
        else if (state.getState() == 4) {
            lbTurn.setText("Game is over.");
            lbInstruction.setText("");
        }
        lbCardsLeft.setText("" + state.getCardsRemaining());

        // update player cards
        this.cards = state.getPlayerCards();
        for (int i = 0; i < this.cards.length; i++) {
            this.playerCards.get(i).setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cards[i] + ".png"))));
        }
        if (this.cards.length == 7) {
            this.playerCards.get(7).setIcon(null);
        }

        // update discard piles
        List<List<Card>> discarded = state.getDiscardedCards();
        for (int i = 0; i < 5; i++) {
            if (discarded.get(i).size() > 0) {
                String cardName = discarded.get(i).get(discarded.get(i).size()-1).toString();
                this.discardedCards.get(i).setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cardName + ".png"))));
            } else {
                String color;
                if (i == 0)
                    color = "Red";
                else if (i == 1)
                    color = "Green";
                else if (i == 2)
                    color = "Blue";
                else if (i == 3)
                    color = "White";
                else
                    color = "Yellow";

                this.discardedCards.get(i).setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + color + ".png"))));
            }
        }

        // update Field
        List<List<Card>> field = state.getField();
        for (int i = 0; i < 10; i++) {
            if (field.get(i).size() > 0) {
                String cardName = field.get(i).get(field.get(i).size()-1).toString();
                this.playedField.get(i).setIcon(new ImageIcon(Objects.requireNonNull(Gui.class.getResource("images/" + cardName + ".png"))));
            } else {
                this.playedField.get(i).setIcon(null);
            }
        }

        SwingUtilities.invokeLater(()->repaint());

        if (state.getState() == 4) {
            int points[] = state.calculatePoints();

            String message = "Results:\nYou: " + points[0] + "\nOpponent: " + points[1];
            JOptionPane.showMessageDialog(new JFrame(), message, "The game is over.", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // executes the Moves of the AI
    private void executeAIMoves(){
        if (this.game.getState() == 2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    PlayMove move1;
                    TakeMove move2;

                    while (true) {
                        move1 = AI.playCard(game.getGameState(2));
                        if (game.executeMove(move1)) {
                            System.out.println(move1);
                            break;
                        }
                    }

                    updateField(game.getGameState(1));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (true) {
                        move2 = AI.takeCard(game.getGameState(2));
                        if (game.executeMove(move2)) {
                            System.out.println(move2);
                            break;
                        }
                    }
                    updateField(game.getGameState(1));
                }
            }).start();
        }
    }
}