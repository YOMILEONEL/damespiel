package org.example.client.controller;

import javax.swing.*;
import java.awt.*;


public class RoutingController implements IRoutingController {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public RoutingController(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    @Override
    public void navigate(String pageName) {
        cardLayout.show(mainPanel, pageName);
    }
}
