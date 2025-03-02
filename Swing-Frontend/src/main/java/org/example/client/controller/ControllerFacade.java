package org.example.client.controller;

import org.example.client.ICheckerGameModel;
import org.example.client.IGameViewHandler;
import org.example.client.view.ViewFactory;

import javax.swing.*;

public class ControllerFacade implements IGameViewHandler {
    private final IGameClientController  gameClientController;
    private final IPlayerClientController playerClientController;
    private final IRoutingController routingController;

    public ControllerFacade(
            IGameClientController gameClientController,
            IPlayerClientController playerClientController,
            IRoutingController routingController,
            ICheckerGameModel gameModel,
            ICheckerGameModel playerModel,
            ViewFactory viewFactory,
            JFrame frame
    ) {
        this.routingController = routingController;
        this.playerClientController = playerClientController;
        this.gameClientController = gameClientController;

        gameModel.addObserver(viewFactory.getView("gameBoard"));
        playerModel.addObserver(viewFactory.getView("gameBoard"));
        playerModel.addObserver(viewFactory.getView("home"));
        gameModel.addObserver(viewFactory.getView("waiting"));
        playerModel.addObserver(viewFactory.getView("waiting"));

        viewFactory.setControllerFacade(this);

        frame.setVisible(true);
        this.navigate("welcome");
    }

    @Override
    public void createGame() {
        this.gameClientController.createGame();
    }

    @Override
    public void joinGame(String gameId) {
        this.gameClientController.joinGame(gameId);
    }

    @Override
    public void makeMove(String oldPosition, String newPosition) {
        this.gameClientController.makeMove(oldPosition, newPosition);
    }

    @Override
    public void login(String name, String password) {
        this.playerClientController.login(name, password);
    }

    @Override
    public void register(String name, String password) {
        this.playerClientController.register(name, password);
    }

    @Override
    public void endTurn() {
        this.gameClientController.endTurn();
    }
    @Override
    public void leaveGame() {
        this.gameClientController.leaveGame();
    }

    @Override
    public void startGame() {
        gameClientController.startGame();
    }

    @Override
    public void updatePlayerName(String name) {
        this.playerClientController.updatePlayerName(name);
    }

    @Override
    public void logout() {

    }

    @Override
    public void navigate(String viewName) {
        routingController.navigate(viewName);
    }
}
