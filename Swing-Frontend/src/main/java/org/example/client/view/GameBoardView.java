package org.example.client.view;

import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;
import org.example.client.IGameViewObserver;
import org.example.client.config.AudioPlayer;
import org.example.client.utils.Utils;
import org.example.client.controller.dto.PlayerDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class GameBoardView extends JPanel implements IGameViewObserver {

    private final int gridSize = 8;
    private final JButton[][] boardButtons = new JButton[gridSize][gridSize];
    private final JLabel jLabelPlayer1 = new JLabel();
    private final JLabel jLabelPlayer2 = new JLabel();
    private final IViewFactoryView viewFactoryView;
    private JPanel jPanelBoard;
    private String oldPosition = "";
    private JLabel jLabelCurrentTurn;
    private long playerId = 0;
    public GameBoardView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView = viewFactoryView;
        initComponents();
    }

    private Icon createColoredIcon(Color color,boolean isKng) {

        int iconSize = 30;
        BufferedImage image = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillOval(0, 0, iconSize, iconSize);
        if(isKng){
            g.setColor(color.equals(Color.BLACK) ? Color.WHITE : Color.BLACK); // Toujours contraste fort
            g.setFont(new Font("Arial", Font.BOLD, 14)); // Police plus grande
            g.drawString("D", iconSize / 3, iconSize / 2);
            // g.drawString("W",0,0);
        }
        g.dispose();
        return new ImageIcon(image);
    }


    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 700));
        setBackground(new Color(30, 30, 30));

        JLabel jLabelTitle = new JLabel();
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 30));
        jLabelTitle.setForeground(Color.RED);
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitle.setText("DAMESPIEL");
        jLabelTitle.setPreferredSize(new Dimension(1000, 60));
        add(jLabelTitle, BorderLayout.NORTH);

        JPanel jPanelLeft = new JPanel();
        jPanelLeft.setLayout(new BoxLayout(jPanelLeft, BoxLayout.Y_AXIS));
        jPanelLeft.setPreferredSize(new Dimension(200, 400));
        jPanelLeft.setBackground(new Color(50, 50, 50));

        add(jPanelLeft, BorderLayout.WEST);

        JPanel jPanelCenter = new JPanel();
        jPanelCenter.setPreferredSize(new Dimension(500, 500));
        jPanelCenter.setBackground(new Color(70, 70, 70));
        jPanelCenter.setLayout(new BorderLayout());

        JPanel jPanelTop = new JPanel();
        jPanelTop.setPreferredSize(new Dimension(500, 40));
        jPanelTop.setBackground(new Color(70, 70, 70));
        jPanelTop.setLayout(new FlowLayout(FlowLayout.CENTER));

        jLabelPlayer1.setFont(new Font("Arial", Font.BOLD, 14));
        jLabelPlayer1.setForeground(Color.WHITE);

        jPanelTop.add(jLabelPlayer1);
        jPanelCenter.add(jPanelTop, BorderLayout.NORTH);

        jPanelBoard = new JPanel();
        jPanelBoard.setPreferredSize(new Dimension(320, 320));
        jPanelBoard.setBackground(new Color(70, 70, 70));
        jPanelCenter.add(jPanelBoard, BorderLayout.CENTER);

        JPanel jPanelBottom = new JPanel();
        jPanelBottom.setPreferredSize(new Dimension(500, 40));
        jPanelBottom.setBackground(new Color(70, 70, 70));
        jPanelBottom.setLayout(new FlowLayout(FlowLayout.CENTER));

        jLabelPlayer2.setFont(new Font("Arial", Font.BOLD, 14));
        jLabelPlayer2.setForeground(Color.WHITE);

        jPanelBottom.add(jLabelPlayer2);
        jPanelCenter.add(jPanelBottom, BorderLayout.SOUTH);

        add(jPanelCenter, BorderLayout.CENTER);

        JPanel jPanelRight = new JPanel();
        jPanelRight.setLayout(new BoxLayout(jPanelRight, BoxLayout.Y_AXIS));
        jPanelRight.setPreferredSize(new Dimension(200, 400));
        jPanelRight.setBackground(new Color(50, 50, 50));

        JButton jButtonMenu = new JButton("Spiel verlassen");
        jButtonMenu.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButtonMenu.setMaximumSize(new Dimension(150, 35));
        jButtonMenu.setBackground(new Color(200, 200, 200));
        jButtonMenu.addActionListener(e -> viewFactoryView.getControllerFacade().leaveGame());

        JButton switchButton = new JButton("Zug beenden");
        switchButton.setFont(new Font("Arial", Font.BOLD, 14));
        switchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        switchButton.setMaximumSize(new Dimension(150, 35));
        switchButton.setBackground(new Color(200, 200, 200));
        switchButton.addActionListener(e -> viewFactoryView.getControllerFacade().endTurn());

        jLabelCurrentTurn = new JLabel("Aktueller Spieler");
        jLabelCurrentTurn.setFont(new Font("Arial", Font.BOLD, 12));
        jLabelCurrentTurn.setForeground(Color.YELLOW);
        jLabelCurrentTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanelRight.add(Box.createVerticalStrut(10));
        jPanelRight.add(jLabelCurrentTurn);

        jPanelRight.add(Box.createVerticalStrut(20));
        jPanelRight.add(Box.createVerticalStrut(10));
        jPanelRight.add(jButtonMenu);
        jPanelRight.add(switchButton);
        add(jPanelRight, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    @Override
    public void updateGameModel(GameModel gameModel) {

        String board = gameModel.getBoard();

        jPanelBoard.removeAll();
        jPanelBoard.updateUI();

        boolean isBlackPlayer = gameModel.getPlayerColors().get(playerId) == org.example.client.model.enumaration.Color.BLACK;
        initBoard(board, isBlackPlayer);

        if(gameModel.getCurrentPlayerId() == playerId) {
            jLabelCurrentTurn.setText("Aktueller Spieler: " + jLabelPlayer2.getText());
        } else {
            for (PlayerDto playerDto : gameModel.getPlayers()) {
                if (playerDto.getId() != playerId) {
                    jLabelCurrentTurn.setText("Aktueller Spieler: " + playerDto.getName());
                }
            }
        }

        jPanelBoard.revalidate();
        jPanelBoard.repaint();


        for (PlayerDto playerDto : gameModel.getPlayers()) {
            if (playerDto.getId() != playerId) {
                jLabelPlayer1.setText(playerDto.getName());
            }
        }

    }

    public void initBoard(String board, boolean isBlackPlayer) {
        jPanelBoard.removeAll();
        jPanelBoard.setLayout(new GridLayout(gridSize, gridSize));

        Icon blackTokenIcon = createColoredIcon(Color.BLACK, false);
        Icon whiteTokenIcon = createColoredIcon(Color.WHITE, false);
        Icon kingBlackTokenIcon = createColoredIcon(Color.BLACK, true);
        Icon kingWhiteTokenIcon = createColoredIcon(Color.WHITE, true);

        String[] boardArr = board.split(" ");

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int currentRow = isBlackPlayer ? (7 - i) : i;
                int currentCol = isBlackPlayer ? j : (7 - j);
                int index = currentRow * 8 + currentCol;
                String cell = boardArr[index];

                JButton button = new JButton();
                button.setPreferredSize(new Dimension(40, 40));

                if (Objects.equals(cell, "WC")) {
                    button.setBackground(Color.WHITE);
                } else {
                    button.setBackground(Color.DARK_GRAY);
                    if (Objects.equals(cell, "TB")) {
                        button.setIcon(blackTokenIcon);
                        button.addActionListener(e -> oldPosition = Utils.columnToPosition(currentRow, currentCol));
                    } else if (Objects.equals(cell, "TW")) {
                        button.setIcon(whiteTokenIcon);
                        button.addActionListener(e -> oldPosition = Utils.columnToPosition(currentRow, currentCol));
                    } else if (Objects.equals(cell, "KW")) {
                        button.setIcon(kingWhiteTokenIcon);
                        button.addActionListener(e -> oldPosition = Utils.columnToPosition(currentRow, currentCol));
                    } else if (Objects.equals(cell, "KB")) {
                        button.setIcon(kingBlackTokenIcon);
                        button.addActionListener(e -> oldPosition = Utils.columnToPosition(currentRow, currentCol));
                    } else {
                        button.addActionListener(new TokenActionListener(currentRow, currentCol, "White Token"));
                    }
                }

                boardButtons[currentRow][currentCol] = button;
                jPanelBoard.add(button);
            }
        }
        jPanelBoard.revalidate();
        jPanelBoard.repaint();
    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {
        jLabelPlayer2.setText(playerModel.getName());
        playerId = playerModel.getId();
    }

    private class TokenActionListener extends AbstractAction {
        private String position;
        private final String tokenType;

        public TokenActionListener(int row, int col, String tokenType) {
            this.position = Utils.columnToPosition(row,col);
            this.tokenType = tokenType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("move from" + oldPosition + " to " + position);
            System.out.println();
            if(!Objects.equals(oldPosition, "")){

                System.out.println(oldPosition);
                AudioPlayer.getInstance().playSoundEffect("audio/move.wav");
                viewFactoryView.getControllerFacade().makeMove(oldPosition, position);
            }
        }
    }


}