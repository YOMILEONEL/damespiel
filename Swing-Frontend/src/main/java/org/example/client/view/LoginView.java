package org.example.client.view;



import org.example.client.IGameViewObserver;
import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class LoginView extends JPanel implements IGameViewObserver {

    private final IViewFactoryView viewFactoryView;
    private JTextField jTextField1;
    private JPasswordField jPasswordField1;
    private JButton loginButton;

    public LoginView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView = viewFactoryView;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout()); // Centrer le contenu
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("EINLOGGEN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(nameLabel, gbc);

        jTextField1 = new JTextField(15);
        gbc.gridx = 1;
        add(jTextField1, gbc);

        JLabel passwordLabel = new JLabel("Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        jPasswordField1 = new JPasswordField(15);
        gbc.gridx = 1;
        add(jPasswordField1, gbc);

        loginButton = new JButton("Einloggen");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);


        loginButton.addActionListener(evt -> loginAction());
    }


    private void loginAction() {
        String playerName = jTextField1.getText();
        char[] passwordChars = jPasswordField1.getPassword();
        String playerPassword = new String(passwordChars);
        Arrays.fill(passwordChars, ' ');

        if (playerName.isEmpty() || playerPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Password are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        viewFactoryView.getControllerFacade().login(playerName, playerPassword);
    }

    @Override
    public void updateGameModel(GameModel gameModel) {

    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {

    }
}
