package org.example.client;


import org.example.client.controller.dto.GameDTO;
import org.example.client.controller.dto.PlayerDto;
import org.example.client.model.enumaration.Color;
import org.example.client.model.enumaration.GameStatus;

import java.util.List;
import java.util.Map;

public interface IGameModel {
    String getGameId();
    GameStatus getGameStatus();
    String getBoard();
    Long getCurrentPlayerId();
    Map<Long, Color> getPlayerColors();
    List<PlayerDto> getPlayers();
    void update(GameDTO gameDTO);
    void reinitialize();
}
