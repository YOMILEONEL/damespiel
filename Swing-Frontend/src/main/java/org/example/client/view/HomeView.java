package org.example.client.view;



import org.example.client.IGameViewObserver;
import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel implements IGameViewObserver {

    private final IViewFactoryView viewFactoryView;
    public HomeView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView = viewFactoryView;
        this.setPreferredSize(new Dimension(1000, 700));
        this.setMinimumSize(new Dimension(1000, 700));
        this.setMaximumSize(new Dimension(1000, 700));
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        joinGameButton = new JButton();
        createGameButton = new JButton();
        parameterButton = new JButton();
        logoutButton = new JButton();
        ruleButton = new JButton();

        jPanel1.setBackground(new Color(255, 255, 0));

        jLabel1.setFont(new Font("Arial", Font.BOLD, 28));
        jLabel1.setForeground(new Color(255, 51, 0));
        jLabel1.setText("WILLKOMMEN");

        jLabel2.setFont(new Font("Serif", Font.ITALIC, 16));
        jLabel2.setText("DAMESPIEL");

        joinGameButton.setBackground(new Color(204, 204, 0));
        joinGameButton.setFont(new Font("Snap ITC", Font.PLAIN, 16));
        joinGameButton.setText("RAUM BEITRETEN");
        joinGameButton.addActionListener(e -> joinGameButtonActionPerformed());

        createGameButton.setBackground(new Color(204, 0, 204));
        createGameButton.setFont(new Font("Snap ITC", Font.PLAIN, 16));
        createGameButton.setText("RAUM ERSTELLEN");
        createGameButton.addActionListener(e -> createGameButtonActionPerformed());

        parameterButton.setBackground(new Color(204, 204, 204));
        parameterButton.setFont(new Font("Snap ITC", Font.PLAIN, 14));
        parameterButton.setText("EINSTELLUNGEN");
        parameterButton.addActionListener(e -> viewFactoryView.getControllerFacade().navigate("parameter"));

        ruleButton.setBackground(new Color(204, 204, 204));
        ruleButton.setFont(new Font("Snap ITC", Font.PLAIN, 14));
        ruleButton.setText("REGELN");
        ruleButton.addActionListener(e -> ruleButtonActionPerformed());

        logoutButton.addActionListener(e->this.viewFactoryView.getControllerFacade().logout());

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(17, 17, 17))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel1)
                                .addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(73, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(400, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(joinGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(createGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(parameterButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ruleButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(111, 111, 111))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(joinGameButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(createGameButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(parameterButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ruleButton)
                                .addContainerGap(50, Short.MAX_VALUE))
        );
    }

    private void ruleButtonActionPerformed() {
        viewFactoryView.getControllerFacade().navigate("rule");
    }

    private void createGameButtonActionPerformed() {
        viewFactoryView.getControllerFacade().createGame();
    }

    private void joinGameButtonActionPerformed() {
        viewFactoryView.getControllerFacade().navigate("joinGame");
    }

    // Variables declaration - do not modify
    private JButton logoutButton;
    private JButton joinGameButton;
    private JButton createGameButton;
    private JButton parameterButton;
    private JButton ruleButton;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;

    @Override
    public void updateGameModel(GameModel gameModel) {

    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {
        jLabel1.removeAll();
        jLabel1.updateUI();

        jLabel1.setText("WILLKOMMEN "+ playerModel.getName());
        jLabel1.revalidate();
        jLabel1.repaint();

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Damespiel - Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setResizable(false);
        frame.add(new HomeView(null));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}
