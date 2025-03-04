package org.example.client.view;

import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;
import org.example.client.IGameViewObserver;

import javax.swing.*;

/**
 * @author Yoga
 */
public class JoinGameView extends JPanel implements IGameViewObserver {

    private  final IViewFactoryView viewFactoryView;
    private JTextField jTextField1;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    public JoinGameView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView = viewFactoryView;
        initComponents();
    }

    private void initComponents() {
        jButton1 = new JButton();
        jLabel1 = new JLabel();
        jTextField1 = new JTextField();
        jButton2 = new JButton();

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 12)); // NOI18N
        jButton1.setText("Zurück Zum Menü");
        jButton1.addActionListener(e -> viewFactoryView.getControllerFacade().navigate("home"));

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Game BEITRETEN");

        jTextField1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jTextField1.setText("Code hier eingeben ...");
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 255, 0));
        jButton2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jButton2.setText("Bestätigen");
        jButton2.addActionListener(e-> jButton2ActionPerformed());

        // Update layout for your JPanel
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton1))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(58, 58, 58)
                                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(71, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButton2)
                                                .addGap(42, 42, 42))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
                                                .addGap(128, 128, 128))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel1)
                                .addGap(26, 26, 26)
                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addContainerGap(76, Short.MAX_VALUE))
        );
    }

    private void jButton2ActionPerformed() {
        viewFactoryView.getControllerFacade().joinGame(jTextField1.getText());
        jTextField1.setText("");
        repaint();
        revalidate();
    }

    private void jButton1ActionPerformed() {
        viewFactoryView.getControllerFacade().joinGame(jTextField1.getText());
    }

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {
        if (jTextField1.getText().equals("Code hier eingeben ...")) {
            jTextField1.setText("");
        }
    }

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {
        if (jTextField1.getText().isEmpty()) {
            jTextField1.setText("Code hier eingeben ...");
        }
    }

    @Override
    public void updateGameModel(GameModel gameModel) {
    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {
    }
}