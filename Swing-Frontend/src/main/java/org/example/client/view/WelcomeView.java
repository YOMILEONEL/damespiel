package org.example.client.view;



import org.example.client.IGameViewObserver;
import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;

import javax.swing.*;
import java.awt.*;

public class WelcomeView extends JPanel implements IGameViewObserver {
    private final IViewFactoryView viewFactoryView;
    private JButton loginButton;
    private JButton registerButton;


    public WelcomeView(IViewFactoryView viewFactoryView) {
        this.viewFactoryView = viewFactoryView;
        setPreferredSize(new Dimension(1000, 700));
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 51));

        // Titre principal
        JLabel titleLabel = new JLabel("Willkommen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.RED);

        // Sous-titre
        JLabel subLabel = new JLabel("DAMESPIEL", SwingConstants.CENTER);
        subLabel.setFont(new Font("Serif", Font.ITALIC, 20));

        // Panel pour le titre
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(255, 255, 51));
        titlePanel.add(titleLabel);
        titlePanel.add(subLabel);

        // Bouton pour s'authentifier
        loginButton = new JButton("EINLOGGEN");
        loginButton.setBackground(new Color(255, 51, 51));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> viewFactoryView.getControllerFacade().navigate("login"));

        // Bouton pour s'inscrire
        registerButton = new JButton("REGISTRIEREN");
        registerButton.setBackground(new Color(0, 0, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> viewFactoryView.getControllerFacade().navigate("register"));

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Ajouter les panels à la vue principale
        add(titlePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void updateGameModel(GameModel gameModel) {
        // Implémentation vide pour l'instant
    }

    @Override
    public void updatePlayerModel(PlayerModel playerModel) {
        // Implémentation vide pour l'instant
    }

}
