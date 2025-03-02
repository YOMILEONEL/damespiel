package org.example.client;

public interface IGameViewHandler {
    void createGame();
    void joinGame(String gameId);
    void makeMove(String oldPosition, String newPosition);
    void login(String name, String password);
    void register(String name, String password);
    void endTurn();
    void navigate(String viewName);
    void leaveGame();
    void startGame();
    void updatePlayerName(String name);
    void logout();
}
