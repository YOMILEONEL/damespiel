package org.example.client;



import org.example.client.config.AppConfig;

import javax.swing.*;

public class client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppConfig::initialize);
    }
}
