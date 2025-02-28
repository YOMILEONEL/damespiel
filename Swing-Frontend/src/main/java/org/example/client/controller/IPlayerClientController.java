package org.example.client.controller;

public interface IPlayerClientController {
    void login(String name, String password);
    void updatePlayerName(String name);
    void register(String name, String password);
}
