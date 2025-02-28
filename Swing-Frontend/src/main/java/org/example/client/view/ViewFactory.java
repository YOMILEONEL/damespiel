package org.example.client.view;

import lombok.Setter;
import org.example.client.IGameViewHandler;
import org.example.client.IGameViewObserver;
import org.example.client.IVewFactory;


import javax.swing.*;
import java.util.HashMap;
import java.util.Map;


public class ViewFactory implements IVewFactory, IViewFactoryView {
    private final Map<String, IGameViewObserver> viewCache = new HashMap<>();



    @Setter
    IGameViewHandler controllerFacade;

    public ViewFactory(IGameViewHandler controllerFacade){
        this.controllerFacade= controllerFacade;
    }

    public IGameViewObserver getView(String viewName) {
        return viewCache.computeIfAbsent(viewName, this::createView);
    }

    private IGameViewObserver createView(String viewName) {
        switch (viewName) {
            case "welcome": return new WelcomeView(this);
            case "login": return new LoginView(this);
            case "register": return new RegistrationView(this);
            case "home": return new HomeView(this);
            case "joinGame": return new JoinGameView(this);
            case "gameBoard": return new GameBoardView(this);
            case "parameter":return new ParameterView(this);
            case "rule":return new RuleView(this);
            case "waiting":return new WaitingView(this);
            default: throw new IllegalArgumentException("unknown view: " + viewName);
        }
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    @Override
    public IGameViewHandler getControllerFacade() {
        return controllerFacade;
    }
}
