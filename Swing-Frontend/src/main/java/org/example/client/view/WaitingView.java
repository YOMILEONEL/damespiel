package org.example.client.view;

import org.example.client.model.PlayerModel;
import org.example.client.model.GameModel;

import org.example.client.*;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class WaitingView extends JPanel implements IGameViewObserver {
    private final IViewFactoryView viewFactoryView;
    private String playerName;
    private final JLabel labelGameID;
    private final JLabel labelPlayer;
    private final JLabel labelPlayer2;
    private final JButton buttonPlay;
    private final JButton buttonBack;


    public WaitingView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView = viewFactoryView;
        setLayout(null);

        // Initialize components
        labelGameID = new JLabel();
        labelGameID.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        labelGameID.setBounds(140, 20, 200, 30);
        add(labelGameID);

        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(null);
        playerPanel.setBackground(Color.WHITE);
        playerPanel.setBounds(50, 60, 300, 120);
        add(playerPanel);

        labelPlayer = new JLabel();
        labelPlayer.setOpaque(true);
        labelPlayer.setBackground(new Color(204, 0, 0));
        labelPlayer.setFont(new Font("Arial", Font.BOLD, 18));
        labelPlayer.setBounds(20, 20, 260, 40);
        labelPlayer.setHorizontalAlignment(SwingConstants.CENTER);

        labelPlayer2 = new JLabel();
        labelPlayer2.setOpaque(true);
        labelPlayer2.setBackground(new Color(204, 0, 0));
        labelPlayer2.setFont(new Font("Arial", Font.BOLD, 18));
        labelPlayer2.setBounds(20, 20, 260, 40);
        labelPlayer2.setHorizontalAlignment(SwingConstants.CENTER);
        labelPlayer.setBounds(20, 20, 260, 40);
        labelPlayer2.setBounds(20, 70, 260, 40);
        playerPanel.add(labelPlayer);
        playerPanel.add(labelPlayer2);

        JLabel labelWaiting = new JLabel("Spieler wird erwartet...");
        labelWaiting.setFont(new Font("Serif", Font.ITALIC, 16));
        labelWaiting.setBounds(90, 120, 200, 30);
        playerPanel.add(labelWaiting);

        buttonBack = new JButton("Zurück Zum Menü");

        buttonBack.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
        buttonBack.setBounds(110, 200, 180, 30);
        buttonBack.addActionListener(e -> viewFactoryView.getControllerFacade().leaveGame());
        add(buttonBack);

        buttonPlay = new JButton("Spielen");
        buttonPlay.setVisible(false);
        buttonPlay.setEnabled(false);
        buttonPlay.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPlay.setBounds(280, 240, 100, 30);
        buttonPlay.addActionListener(e-> viewFactoryView.getControllerFacade().startGame());
        add(buttonPlay);
    }

    @Override
    public void updateGameModel(GameModel gameModel) {
        labelPlayer.setText("");
        labelPlayer2.setText("");
        labelPlayer.revalidate();
        labelPlayer2.repaint();
        labelGameID.setText("Game-ID: " + gameModel.getGameId());
        labelPlayer.setText(gameModel.getPlayers().get(0).getName());
        if(gameModel.getPlayers().size()>1)
        {
            buttonBack.setEnabled(false);
            labelPlayer2.setText(gameModel.getPlayers().get(1).getName());
            buttonPlay.setVisible(false);
            if(Objects.equals(playerName, gameModel.getPlayers().get(0).getName())){
                buttonPlay.setEnabled(true);
            }
        }

        if(Objects.equals(playerName, gameModel.getPlayers().get(0).getName())) {
            buttonPlay.setVisible(true);
        }

        revalidate();
        repaint();
    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {
        playerName = playerModel.getName();
        labelPlayer.setText( playerModel.getName());
    }


}