package org.example.client.controller;

public interface IGameClientController {
    void createGame();
    void joinGame(String gameId);
    void getGameStatus(String gameId,boolean hasJoin);
    void makeMove(String oldPosition, String newPosition);
    void endTurn();
    void leaveGame();
    void startGame();
}
