package org.example.client.view;





import org.example.client.IGameViewObserver;
import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class RegistrationView extends JPanel implements IGameViewObserver {

    private final IViewFactoryView viewFactoryView;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton registerButton;



    public RegistrationView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView = viewFactoryView;
        initComponents();
    }


    private void initComponents() {
        setLayout(new GridBagLayout()); // Centrer le contenu
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("REGISTRATION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(nameLabel, gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        add(nameField, gbc);

        JLabel passwordLabel = new JLabel("Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        registerButton = new JButton("Registrieren");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(registerButton, gbc);


        registerButton.addActionListener(evt -> registerUser());
    }


    private void registerUser() {
        String playerName = nameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String playerPassword = new String(passwordChars);
        Arrays.fill(passwordChars, ' '); // Securely clear password from memory

        if (playerName.isEmpty() || playerPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name und Passwort sind erforderlich!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        viewFactoryView.getControllerFacade().register(playerName, playerPassword);
    }

    @Override
    public void updateGameModel(GameModel gameModel) {

    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {

    }

}
