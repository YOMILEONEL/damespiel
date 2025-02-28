package org.example.client.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.client.model.enumaration.Color;
import org.example.client.model.enumaration.GameStatus;
import org.example.client.model.enumaration.Winner;

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