package org.example.client;

import org.example.client.model.GameModel;
import org.example.client.model.PlayerModel;

public interface IGameViewObserver {
    void updateGameModel(GameModel gameModel);
    void updatePlayerModel(PlayerModel playerModel);
}
