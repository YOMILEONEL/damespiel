package com.leonel.damespielServer.model.mapper;



import com.leonel.damespielServer.model.Game;
import com.leonel.damespielServer.model.dto.GameDTO;
import lombok.Builder;

import java.util.stream.Collectors;

@Builder
public class GameMapper {

    public static GameDTO toDTO(Game game) {
        return GameDTO.builder()
                .gameId(game.getGameId())
                .gameStatus(game.getGameStatus())
                .winner(game.getWinner())
                .players(game.getPlayers().stream().map(PlayerMapper::toDTO).collect(Collectors.toList()))
                .currentPlayerId(game.getCurrentPlayerId())
                .playerColors(game.getPlayerColors())
                .board(game.getBoard())
                .build();
    }

}
