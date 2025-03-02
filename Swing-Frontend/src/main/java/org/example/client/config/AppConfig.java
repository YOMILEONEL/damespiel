package org.example.client.config;



import org.example.client.controller.ControllerFacade;
import org.example.client.controller.GameClientController;
import org.example.client.controller.PlayerClientController;
import org.example.client.controller.RoutingController;
import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;
import org.example.client.view.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class AppConfig {
    public static void initialize(){
        RestTemplate restTemplate = new RestTemplate();

        JFrame frame = new JFrame("Damespiel");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);


        PlayerModel playerModel = new PlayerModel();
        GameModel gameModel = new GameModel();

        ViewFactory viewFactory = new ViewFactory(null);
        LoginView loginView = (LoginView) viewFactory.getView("login");
        RegistrationView registrationView = (RegistrationView) viewFactory.getView("register");
        HomeView homeView = (HomeView) viewFactory.getView("home");
        WelcomeView welcomeView = (WelcomeView) viewFactory.getView("welcome");
        JoinGameView joinGameView = (JoinGameView) viewFactory.getView("joinGame");
        GameBoardView gameBoardView = (GameBoardView) viewFactory.getView("gameBoard");
        ParameterView parameterView=(ParameterView) viewFactory.getView("parameter");
        RuleView ruleView=(RuleView) viewFactory.getView("rule");
        WaitingView waitingView=(WaitingView) viewFactory.getView("waiting");


        mainPanel.add(welcomeView, "welcome");
        mainPanel.add(loginView, "login");
        mainPanel.add(registrationView, "register");
        mainPanel.add(homeView, "home");
        mainPanel.add(joinGameView, "joinGame");
        mainPanel.add(gameBoardView, "gameBoard");
        mainPanel.add(parameterView,"parameter");
        mainPanel.add(ruleView,"rule");
        mainPanel.add(waitingView,"waiting");

        frame.add(mainPanel);

        RoutingController routingController = new RoutingController(cardLayout,mainPanel);
        GameClientController gameClientController = new GameClientController(restTemplate,viewFactory,playerModel,gameModel,routingController);
        PlayerClientController playerClientController = new PlayerClientController(restTemplate,viewFactory,playerModel,routingController);

        AudioPlayer audioPlayer = AudioPlayer.getInstance();
        audioPlayer.playBackgroundMusic("audio/backgroundmusic.wav");
        parameterView.setAudioplayer(audioPlayer);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Möchten Sie wirklich das Spiel verlassen?", "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            try {
                                gameClientController.leaveGame();
                            } catch (Exception ex) {
                                throw new RuntimeException("Deconnection error : " + ex.getMessage());
                            } finally {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ignored) {}
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            audioPlayer.stopBackgroundMusic();
                            System.exit(0);
                        }
                    }.execute();
                }
            }
        });

        new ControllerFacade(gameClientController, playerClientController, routingController, gameModel, playerModel, viewFactory, frame);
    }

}
