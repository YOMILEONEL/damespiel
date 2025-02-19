package com.leonel.damespielServer.service;


import com.leonel.damespielServer.model.Player;
import com.leonel.damespielServer.model.dto.PlayerDto;
import com.leonel.damespielServer.model.dto.PlayerRequest;

public interface PlayerService {
    boolean authenticatePlayer(PlayerRequest playerRequest) throws Exception;

    PlayerDto loginPlayer(PlayerRequest playerLoginRequest) throws Exception;

    Player getPlayerByName(String playerName);

    Player getPlayerById(Long playerId) throws Exception;

    boolean playerInGame(Player player);
}
