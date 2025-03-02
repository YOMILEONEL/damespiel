package org.example.client;

import javax.swing.*;
import org.example.client.config.AppConfig;


public class client2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppConfig::initialize);
    }

}
