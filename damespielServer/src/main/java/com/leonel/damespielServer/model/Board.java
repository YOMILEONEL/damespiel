package com.leonel.damespielServer.model;

import com.leonel.damespielServer.model.enumeration.Color;
import com.leonel.damespielServer.model.enumeration.TokenType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;



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

    /**
     * Converts the board into a string representation.
     * The string will contain space-separated cell values, with each row of the board represented on a new line.
     *
     * @return A string representing the board, with each cell value space-separated.
     */
    public String toString() {
        StringBuilder board = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            if (i != 0) {
                board.append(" ");
            }
            for (int j = 0; j < 8; j++) {
                board.append(cells[i][j].toString());
                if (j < 7) {
                    board.append(" ");
                }
            }
        }
        return board.toString();
    }

    /**
     * Converts an algebraic position (e.g., "A1") to 2D array coordinates [row, column].
     *
     * @param position The position in algebraic notation.
     * @return An array containing the row and column as integers.
     * @throws IllegalArgumentException if the position is invalid (e.g., out of bounds or wrong format).
     */
    public int[] getCoordinatesFromPosition(String position) {
        if (position == null || position.length() != 2) {
            throw new IllegalArgumentException("Invalid position format: " + position);
        }

        char columnChar = position.toUpperCase().charAt(0);
        char rowChar = position.charAt(1);

        int column = columnChar - 'A';
        int row = rowChar - '1';

        if (column < 0 || column >= 8 || row < 0 || row >= 8) {
            throw new IllegalArgumentException("Position out of bounds: " + position);
        }

        return new int[]{row, column};
    }

    String getPositionFromCoordinates(int row, int col) {
        // Überprüfen, ob Zeile und Spalte im Bereich 0–7 liegen
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            throw new IllegalArgumentException("Coordinates out of bounds: row=" + row + ", col=" + col);
        }
        // Position in algebraischer Notation erstellen
        char column = (char) ('A' + col);
        char rowChar = (char) ('1' + row);
        return "" + column + rowChar;
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }



    /**
     * Promotes a token at the given position to a King.
     *
     * @param position The position of the token on the board (e.g., "A1", "B2").
     * @throws IllegalArgumentException If the position is invalid or there is no promotable token.
     */
    public void promoteToKing(String position) {
        // Umwandlung der Position (z. B. "A1") in Koordinaten
        int[] coordinates = getCoordinatesFromPosition(position);
        int row = coordinates[0];
        int col = coordinates[1];

        // Validierung: Zelle muss ein schwarzes Feld sein
        if (!(cells[row][col] instanceof BlackCell blackCell)) {
            throw new IllegalArgumentException("The token at position " + position + " is not on a valid black cell.");
        }

        // Validierung: Schwarzes Feld muss ein Token enthalten
        if (blackCell.token == null) {
            throw new IllegalArgumentException("No token found at position " + position + " to promote.");
        }

        // Abfrage des Tokens und seiner Farbe (weiße oder schwarze Tokens)
        Token token = blackCell.token;
        TokenType tokenType = token.getTokenType();

        // Bedingungen zum Befördern prüfen (letzte Reihe für Weiß / Schwarz)
        if (tokenType == TokenType.TW && row == 0) { // Weiß erreicht obere Reihe
            token.setTokenType(TokenType.KW); // Zu König befördern
        } else if (tokenType == TokenType.TB && row == 7) { // Schwarz erreicht untere Reihe
            token.setTokenType(TokenType.KB); // Zu König befördern
        } else {
            throw new IllegalArgumentException("The token at position " + position + " cannot be promoted.");
        }

        // Status aktualisieren
        token.setKing(true); // Markiere den Token als König
    }

    public void makeMove(String fromPosition, String toPosition, Color color) {
        if (!isValidMove(fromPosition, toPosition, color)) {
            throw new IllegalArgumentException("Ungültiger Zug von " + fromPosition + " nach " + toPosition);
        }

        int[] fromCoordinates = getCoordinatesFromPosition(fromPosition);
        int[] toCoordinates = getCoordinatesFromPosition(toPosition);

        int fromRow = fromCoordinates[0];
        int fromCol = fromCoordinates[1];
        int toRow = toCoordinates[0];
        int toCol = toCoordinates[1];

        BlackCell fromCell = (BlackCell) cells[fromRow][fromCol];
        BlackCell toCell = (BlackCell) cells[toRow][toCol];

        Token movingToken = fromCell.token;
        fromCell.token = null;
        toCell.token = movingToken;

        // Überprüfen, ob der Zug ein Sprung ist
        if (Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol)) {
            int stepRow = (toRow - fromRow) / Math.abs(toRow - fromRow);
            int stepCol = (toCol - fromCol) / Math.abs(toCol - fromCol);
            int middleRow = fromRow + stepRow;
            int middleCol = fromCol + stepCol;

            boolean foundOpponentToken = false;

            while (middleRow != toRow && middleCol != toCol) {
                if (cells[middleRow][middleCol] instanceof BlackCell middleCell && middleCell.token != null) {
                    if (foundOpponentToken) {
                        throw new IllegalArgumentException("Mehr als ein gegnerischer Token auf dem Weg");
                    }
                    foundOpponentToken = true;
                    middleCell.token = null; // Entfernen des geschlagenen Tokens
                }
                middleRow += stepRow;
                middleCol += stepCol;
            }
        }

        // Token zu König befördern, wenn es die letzte Reihe erreicht
        if (toRow == 0 && color == Color.WHITE) {
            toCell.token.setTokenType(TokenType.KW);
        } else if (toRow == 7 && color == Color.BLACK) {
            toCell.token.setTokenType(TokenType.KB);
        }
    }

    public boolean isValidMove(String fromPosition, String toPosition, Color color) {
        int[] fromCoordinates = getCoordinatesFromPosition(fromPosition);
        int fromRow = fromCoordinates[0];
        int fromCol = fromCoordinates[1];

        if (!(cells[fromRow][fromCol] instanceof BlackCell fromCell) || fromCell.token == null) {
            return false;
        }

        Token token = fromCell.token;
        TokenType tokenType = token.getTokenType();

        if (tokenType == TokenType.KW || tokenType == TokenType.KB) {
            return isValidMoveForKing(fromPosition, toPosition, color);
        } else {
            return isValidMoveForNormalToken(fromPosition, toPosition, color);
        }
    }

    private boolean isValidMoveForKing(String fromPosition, String toPosition, Color color) {
        int[] fromCoordinates = getCoordinatesFromPosition(fromPosition);
        int[] toCoordinates = getCoordinatesFromPosition(toPosition);

        int fromRow = fromCoordinates[0];
        int fromCol = fromCoordinates[1];
        int toRow = toCoordinates[0];
        int toCol = toCoordinates[1];

        if (!isWithinBounds(fromRow, fromCol) || !isWithinBounds(toRow, toCol)) {
            return false;
        }

        if (!(cells[fromRow][fromCol] instanceof BlackCell fromCell) || fromCell.token == null) {
            return false;
        }

        if (!(cells[toRow][toCol] instanceof BlackCell toCell) || toCell.token != null) {
            return false;
        }

        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        if (Math.abs(rowDiff) == Math.abs(colDiff)) {
            int stepRow = rowDiff / Math.abs(rowDiff);
            int stepCol = colDiff / Math.abs(colDiff);
            int middleRow = fromRow + stepRow;
            int middleCol = fromCol + stepCol;

            boolean foundOpponentToken = false;

            while (isWithinBounds(middleRow, middleCol) && (middleRow != toRow || middleCol != toCol)) {
                if (cells[middleRow][middleCol] instanceof BlackCell middleCell && middleCell.token != null) {
                    Color middleColor = middleCell.token.getColor();

                    if (middleColor != color) {
                        if (foundOpponentToken) {
                            return false; // Mehr als ein gegnerischer Token auf dem Weg
                        }
                        foundOpponentToken = true;
                    } else {
                        return false; // Ein eigener Token blockiert den Weg
                    }
                }
                middleRow += stepRow;
                middleCol += stepCol;
            }

            return true;
        }

        return false;
    }

    private boolean isValidMoveForNormalToken(String fromPosition, String toPosition, Color color) {
        int[] fromCoordinates = getCoordinatesFromPosition(fromPosition);
        int[] toCoordinates = getCoordinatesFromPosition(toPosition);

        int fromRow = fromCoordinates[0];
        int fromCol = fromCoordinates[1];
        int toRow = toCoordinates[0];
        int toCol = toCoordinates[1];

        if (!isWithinBounds(fromRow, fromCol) || !isWithinBounds(toRow, toCol)) {
            return false;
        }

        if (!(cells[fromRow][fromCol] instanceof BlackCell fromCell) || fromCell.token == null) {
            return false;
        }

        if (!(cells[toRow][toCol] instanceof BlackCell toCell) || toCell.token != null) {
            return false;
        }

        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        // Normale Token dürfen nur vorwärts ziehen
        if (Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 1) {
            if ((color == Color.WHITE && rowDiff == -1) || (color == Color.BLACK && rowDiff == 1)) {
                return true;
            }
        }

        // Normale Token dürfen nur vorwärts schlagen
        if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2) {
            int middleRow = fromRow + rowDiff / 2;
            int middleCol = fromCol + colDiff / 2;

            if (cells[middleRow][middleCol] instanceof BlackCell middleCell && middleCell.token != null) {
                TokenType middleTokenType = middleCell.token.getTokenType();
                Color middleColor = (middleTokenType == TokenType.KW || middleTokenType == TokenType.TW) ? Color.WHITE : Color.BLACK;

                // Überprüfen, ob der Schlag vorwärts geht
                if ((color == Color.WHITE && rowDiff == -2) || (color == Color.BLACK && rowDiff == 2)) {
                    return middleColor != color;
                }
            }
        }

        return false;
    }

    public int getEnemyToken(Color color){
        int anzahl=0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(getCells()[i][j] instanceof BlackCell blackCell && blackCell.token!=null){
                    if(color == blackCell.token.getColor()){
                        anzahl+=1;
                    }
                }
            }
        }
        return anzahl;
    }

}
