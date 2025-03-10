package com.leonel.damespielServer.model;

import com.leonel.damespielServer.model.enumeration.Color;
import com.leonel.damespielServer.model.enumeration.TokenType;
import com.leonel.damespielServer.model.enumeration.Winner;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.leonel.damespielServer.model.enumeration.TokenType.*;


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
                        blackCell.token = new Token(TW, false, false);
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
                blackCell.token = new Token(TW, false, false);
                yield blackCell;
            }
            case "TB" -> { // Schwarzer Token
                blackCell.token = new Token(TokenType.TB, false, false);
                yield blackCell;
            }
            case "KW" -> { // Weißer König
                blackCell.token = new Token(KW, true, false);
                yield blackCell;
            }
            case "KB" -> { // Schwarzer König
                blackCell.token = new Token(KB, true, false);
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
        if (tokenType == TW && row == 0) { // Weiß erreicht obere Reihe
            token.setTokenType(KW); // Zu König befördern
        } else if (tokenType == TokenType.TB && row == 7) { // Schwarz erreicht untere Reihe
            token.setTokenType(KB); // Zu König befördern
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
            toCell.token.setTokenType(KW);
        } else if (toRow == 7 && color == Color.BLACK) {
            toCell.token.setTokenType(KB);
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

        if (tokenType == KW || tokenType == KB) {
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
                Color middleColor = (middleTokenType == KW || middleTokenType == TW) ? Color.WHITE : Color.BLACK;

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

    public Winner determineWinner() {
        boolean whiteHasMoves = false;
        boolean blackHasMoves = false;
        boolean whiteHasTokens = false;
        boolean blackHasTokens = false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (getCells()[row][col] instanceof BlackCell blackCell && blackCell.token != null) {
                    Token token = blackCell.token;
                    Color tokenColor = token.getColor();

                    if (tokenColor == Color.WHITE) {
                        whiteHasTokens = true;
                        if (!whiteHasMoves) {
                            whiteHasMoves = !getPossibleMoves(getPositionFromCoordinates(row, col), Color.WHITE).isEmpty();
                        }
                    } else if (tokenColor == Color.BLACK) {
                        blackHasTokens = true;
                        if (!blackHasMoves) {
                            blackHasMoves = !getPossibleMoves(getPositionFromCoordinates(row, col), Color.BLACK).isEmpty();
                        }
                    }
                }
            }
        }

        if (!whiteHasTokens) {
            return Winner.BLACK;
        } else if (!blackHasTokens) {
            return Winner.WHITE;
        } else if (!whiteHasMoves) {
            return Winner.BLACK;
        } else if (!blackHasMoves) {
            return Winner.WHITE;
        }

        return Winner.TIE;
    }

    public List<String> getPossibleMoves(String position, Color color) {
        List<String> possibleMoves = new ArrayList<>();
        int[] coordinates = getCoordinatesFromPosition(position);
        int row = coordinates[0];
        int col = coordinates[1];



        System.out.println("Debugging getPossibleMoves:");
        System.out.println("Input position: " + position + ", color: " + color);
        System.out.println("Coordinates: row = " + row + ", col = " + col);

        if (cells[row][col] instanceof BlackCell blackCell) {
            System.out.println("Cell at position is a BlackCell.");
            if (blackCell.token != null) {
                Token token = blackCell.token;
                TokenType tokenType = token.getTokenType();
                System.out.println("Token found: " + tokenType);

                int[][] directions;
                if (tokenType == KW || tokenType == KB) { // King piece
                    directions = new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
                    System.out.println("Token is a King. Directions: " + directions.length);
                } else if (color == Color.WHITE) { // Normal white piece
                    directions = new int[][]{{-1, -1}, {-1, 1}};
                    System.out.println("Token is a normal white piece. Directions: " + directions.length);
                } else { // Normal black piece
                    directions = new int[][]{{1, -1}, {1, 1}};
                    System.out.println("Token is a normal black piece. Directions: " + directions.length);
                }

                for (int[] direction : directions) {
                    int newRow = row + direction[0];
                    int newCol = col + direction[1];
                    System.out.println("Checking direction: (" + direction[0] + ", " + direction[1] + ")");
                    System.out.println("New coordinates: row = " + newRow + ", col = " + newCol);

                    if (isWithinBounds(newRow, newCol) && cells[newRow][newCol] instanceof BlackCell targetCell) {
                        System.out.println("Target cell is within bounds and is a BlackCell.");
                        if (targetCell.token == null) {
                            // 1. Neues hinzugefügt: Normales Ziehen erlauben, wenn Ziel leer
                            System.out.println("Target cell is empty. Adding move: " +
                                    getPositionFromCoordinates(newRow, newCol));
                            possibleMoves.add(getPositionFromCoordinates(newRow, newCol));
                        } else {
                            System.out.println("Target cell has a token.");
                            Color color1 = targetCell.token.getColor();

                            System.out.println("Target cell token color: " + color1);

                            if (color1 != color) { // Jump over opponent
                                int jumpRow = newRow + direction[0];
                                int jumpCol = newCol + direction[1];
                                System.out.println("Checking jump move. Jump coordinates: row = " + jumpRow + ", col = " + jumpCol);

                                if (isWithinBounds(jumpRow, jumpCol) && cells[jumpRow][jumpCol] instanceof BlackCell jumpCell) {
                                    if (jumpCell.token == null) {
                                        System.out.println("Jump move valid. Adding position: " +
                                                getPositionFromCoordinates(jumpRow, jumpCol));
                                        possibleMoves.add(getPositionFromCoordinates(jumpRow, jumpCol));
                                    } else {
                                        System.err.println("Jump cell is not empty. Cannot complete jump move.");
                                    }
                                } else {
                                    System.err.println("Jump cell is out of bounds or not a BlackCell.");
                                }
                            } else {
                                System.err.println("Target token color matches player's color. Cannot jump.");
                            }
                        }
                    } else {
                        System.err.println("Target cell is out of bounds or not a BlackCell.");
                    }
                }
            } else {
                System.err.println("No token found at the starting position.");
            }
        } else {
            System.err.println("Starting cell is not a BlackCell.");
        }

        System.out.println("Possible moves: " + possibleMoves);
        return possibleMoves;
    }

    public boolean isGameOver() {
        boolean hasWhitePieces = false, hasBlackPieces = false;
        boolean hasWhiteMoves = false, hasBlackMoves = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[i][j] instanceof BlackCell blackCell && blackCell.token != null) {
                    // Token-Typ und Farbe ermitteln
                    TokenType tokenType = blackCell.token.getTokenType();
                    boolean isWhite = (tokenType == KW || tokenType == TW);

                    if (isWhite) {
                        hasWhitePieces = true;
                    } else {
                        hasBlackPieces = true;
                    }

                    // Nur nach möglichen Zügen suchen, wenn noch keine gefunden wurden
                    if ((isWhite && !hasWhiteMoves) || (!isWhite && !hasBlackMoves)) {
                        List<String> possibleMoves = getPossibleMoves(getPositionFromCoordinates(i, j), isWhite ? Color.WHITE : Color.BLACK);
                        if (!possibleMoves.isEmpty()) {
                            if (isWhite) {
                                hasWhiteMoves = true;
                            } else {
                                hasBlackMoves = true;
                            }
                        }
                    }
                }
            }
        }

        // Spiel ist vorbei, wenn entweder keine Figuren oder keine Züge mehr für eine Seite verfügbar sind
        return !hasWhitePieces || !hasBlackPieces || !hasWhiteMoves || !hasBlackMoves;
    }








}
