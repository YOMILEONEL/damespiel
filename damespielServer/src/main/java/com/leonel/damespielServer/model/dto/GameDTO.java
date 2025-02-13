package com.leonel.damespielServer.model.dto;


import com.leonel.damespielServer.model.enumeration.Color;
import com.leonel.damespielServer.model.enumeration.GameStatus;
import com.leonel.damespielServer.model.enumeration.Winner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private String gameId;
    private GameStatus gameStatus;
    private Winner winner;
    private List<PlayerDto> players;
    private Long currentPlayerId ;
    private String board;
    private Map<Long, Color> playerColors;
}