package com.leonel.damespielServer.model.utils;

import java.security.SecureRandom;

public class GameIdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 7;
    private static final SecureRandom random = new SecureRandom();

    // Method to generate unique id of 7 characters
    public static String generateGameId() {
        StringBuilder gameId = new StringBuilder(ID_LENGTH);

        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            gameId.append(CHARACTERS.charAt(randomIndex));
        }

        return gameId.toString();
    }
}