package org.example.client.view;



import org.example.client.IGameViewObserver;
import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;

import javax.swing.*;

public class RuleView extends JPanel implements IGameViewObserver {

    private final IViewFactoryView viewFactoryView;

    public RuleView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView=viewFactoryView;

        initComponents();
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        jButton1 = new JButton();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Regeln");

        jButton1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jButton1.setText("Zurück Zum Menu");
        jButton1.addActionListener(e -> viewFactoryView.getControllerFacade().navigate("home"));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Grundregeln des Damespiels\n\n       1. Ziel des Spiels\n         - Das Ziel des Spiels ist es, alle Steine des Gegners zu schlagen oder ihn so zu blockieren, dass er keinen Zug mehr ausführen kann.\n\n        2. Spielbrett\n            - Das Damespiel wird auf einem Brett mit 64 Feldern (8x8) gespielt. Die Spielfelder wechseln sich in schwarzen und weißen Quadraten ab.\n\n        3. Anfangsaufstellung\n            - Jeder Spieler beginnt mit 12 Steinen, die auf den dunklen Feldern der ersten drei Reihen auf seiner Seite des Bretts platziert werden.\n\n         4. Zugeglen\n            - Die Steine dürfen nur diagonal vorwärts auf die nächsten dunklen Felder gezogen werden.\n            - Wenn ein Stein des Gegners auf einem angrenzenden Feld steht und dahinter ein leeres Feld ist, kann der gegnerische Stein\n               übersprungen und geschlagen werden.\n\n          5. Dame\n            - Wenn ein Stein die letzte Reihe der gegenüberliegenden Seite erreicht, wird er zur „Dame“ befördert.\n\n          6. Schlagen\n            - Ein Spieler hat die Möglichkeit zu schlagen, ist aber verpflichtet dies zu tun, wenn es möglich ist.\n\n           7. Ende des Spiels\n            - Das Spiel endet, wenn ein Spieler keine Steine mehr hat oder keine legalen Züge mehr ausführen kann.\n        ");
        jScrollPane1.setViewportView(jTextArea1);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 770, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addGap(118, 118, 118)
                                                .addComponent(jLabel1)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton1))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jLabel1)))
                                .addGap(24, 24, 24)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
        );
    }

    private JButton jButton1;
    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;

    @Override
    public void updateGameModel(GameModel gameModel) {

    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {

    }
}