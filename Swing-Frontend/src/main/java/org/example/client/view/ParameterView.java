package org.example.client.view;


import lombok.Setter;
import org.example.client.IGameViewObserver;
import org.example.client.config.AudioPlayer;
import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;

import javax.swing.*;
import java.awt.*;

public class ParameterView extends JPanel implements IGameViewObserver {


    @Setter
    private AudioPlayer audioplayer;
    private final IViewFactoryView viewFactoryView;
    private JButton backButton;
    private JLabel jLabel1;
    private JLabel audioLabel;
    private JButton audioOnButton;
    private JButton audioOffButton;
    private JButton changeNameButton;
    private JTextField nameField;


    public ParameterView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView = viewFactoryView;
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new JLabel();
        backButton = new JButton();
        audioLabel = new JLabel();
        audioOnButton = new JButton();
        audioOffButton = new JButton();
        changeNameButton = new JButton();
        nameField = new JTextField(); // Champ de saisie pour le nom de l'utilisateur

        jLabel1.setFont(new Font("Arial", Font.BOLD, 18)); // NOI18N
        jLabel1.setText("Einstellungen");

        backButton.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18)); // NOI18N
        backButton.setText("Zurück Zum Menu");
        backButton.addActionListener(e -> viewFactoryView.getControllerFacade().navigate("home"));

        audioLabel.setText("Audio:");

        audioOnButton.setText("✔");
        audioOnButton.addActionListener(e -> {
            // Démarre la musique de fond
            audioplayer.playBackgroundMusic("audio/backgroundmusic.wav");
            JOptionPane.showMessageDialog(this, "Musik eingeschaltet");
        });

        audioOffButton.setText("✖");
        audioOffButton.addActionListener(e -> {
            // Arrête la musique de fond
            audioplayer.stopBackgroundMusic();
            JOptionPane.showMessageDialog(this, "Musik ausgeschaltet");
        });

        changeNameButton.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18)); // NOI18N
        changeNameButton.setText("Profilnamen ändern");
        changeNameButton.addActionListener(e -> {
            // Récupère le texte entré par l'utilisateur et affiche un message de confirmation
            String newName = nameField.getText();
            if (!newName.isEmpty()) {
                viewFactoryView.getControllerFacade().updatePlayerName(newName);
                JOptionPane.showMessageDialog(this, "Profilname geändert zu: " + newName);
                nameField.setText("");
                repaint();
                revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie einen gültigen Namen ein.");
            }
        });

        nameField.setFont(new Font("Arial", Font.PLAIN, 16)); // Style du champ de saisie

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(audioLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(audioOnButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(audioOffButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(backButton)
                                                .addGap(100, 100, 100)
                                                .addComponent(jLabel1))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(changeNameButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameField, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(442, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(backButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jLabel1)))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(audioLabel)
                                        .addComponent(audioOnButton)
                                        .addComponent(audioOffButton))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(changeNameButton)
                                        .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(300, Short.MAX_VALUE))
        );
    }

    @Override
    public void updateGameModel(GameModel gameModel) {

    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {

    }
}
