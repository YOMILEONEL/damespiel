package com.leonel.damespielServer.model;

import com.leonel.damespielServer.model.enumeration.TokenType;
import lombok.Getter;
import lombok.Setter;


/**
 * The {@code Board} class represents a standard 8x8 checkers board.
 * It handles the initialization, serialization, and deserialization of the board to and from JSON format.
 * Each cell on the board can be a black or white cell, and black cells can hold tokens (either white or black).
 * The board can be serialized to a JSON format string and can also be deserialized from a JSON string.
 */

@Getter
@Setter
public class Board {

    /**
     * A 2D array of cells representing the checkers board (8x8).
     */
    Cell[][] cells = new Cell[8][8];

    /**
     * Default constructor that initializes a new 8x8 checkers board.
     * The board is populated with alternating black and white cells, and black cells are assigned tokens
     * in the first three rows and white cells in the last three rows.
     */
    public Board() {
        // Erstelle das Brett entsprechend dem gewünschten Muster
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) {
                    // Schwarzes Feld
                    BlackCell blackCell = new BlackCell();

                    // Setze die Tokens entsprechend ihrer Position
                    if (i < 3) { // Obere schwarze (schwarze Tokens - TB)
                        blackCell.token = new Token(TokenType.TB, false, false);
                    } else if (i > 4) { // Untere schwarze (weiße Tokens - TW)
                        blackCell.token = new Token(TokenType.TW, false, false);
                    }

                    cells[i][j] = blackCell;
                } else {
                    // Weißes Feld
                    cells[i][j] = new WhiteCell();
                }
            }
        }
    }


    /**
     * Deserializes a JSON string into a {@code Board} object.
     * The JSON string should represent the board with cell values like "TW" for white token,
     * "TB" for black token, "KW" for king white token, and so on.
     *
     * @param stringBoard The JSON-formatted string representing the board.
     * @return A {@code Board} object populated with the data from the JSON string.
     */
    public static Board mapStringToBoard(String stringBoard)  {
        // Validierung: Sicherstellen, dass die Eingabe nicht leer oder ungültig ist
        if (stringBoard == null || stringBoard.trim().isEmpty()) {
            throw new IllegalArgumentException("Das Board-String-Datenformat ist leer.");
        }

        // Aufteilen und Validierung der Länge
        String[] boardData = stringBoard.trim().split(" ");
        if (boardData.length != 64) {
            throw new IllegalArgumentException("Ungültiges Board-Format. Erwartet werden 64 Werte, gefunden: " + boardData.length);
        }

        // Neues Board erstellen
        Board board = new Board();

        // Iteration über alle Zellen (8x8)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String cellValue = boardData[i * 8 + j];
                board.cells[i][j] = parseCell(cellValue); // Zellinhalt auswerten und setzen
            }
        }

        return board;
    }


    /**
     * Hilfsmethode zur Interpretation eines Zellinhalts-Strings.
     *
     * @param value Der String-Wert der Zelle (z. B. "TW", "TB", "WC", etc.).
     * @return Die entsprechende Cell-Instanz (WhiteCell oder BlackCell).
     */
    private static Cell parseCell(String value) {
        // Basis-Fall: BlackCell wird standardmäßig erstellt
        BlackCell blackCell = new BlackCell();

        // Switch-Logik zur Erstellung der entsprechenden Zelltypen
        return switch (value) {
            case "TW" -> { // Weißer Token
                blackCell.token = new Token(TokenType.TW, false, false);
                yield blackCell;
            }
            case "TB" -> { // Schwarzer Token
                blackCell.token = new Token(TokenType.TB, false, false);
                yield blackCell;
            }
            case "KW" -> { // Weißer König
                blackCell.token = new Token(TokenType.KW, true, false);
                yield blackCell;
            }
            case "KB" -> { // Schwarzer König
                blackCell.token = new Token(TokenType.KB, true, false);
                yield blackCell;
            }
            case "BC" -> blackCell; // Leeres schwarzes Feld
            case "WC" -> new WhiteCell(); // Weißes Feld
            default -> throw new IllegalArgumentException("Ungültiger Zellwert: " + value);
        };
    }


}
