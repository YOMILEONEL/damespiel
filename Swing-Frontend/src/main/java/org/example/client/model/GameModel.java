package org.example.client.model;


import lombok.Data;
import org.example.client.ICheckerGameModel;
import org.example.client.IGameModel;
import org.example.client.IGameViewObserver;
import org.example.client.controller.dto.GameDTO;
import org.example.client.controller.dto.PlayerDto;
import org.example.client.model.enumaration.Color;
import org.example.client.model.enumaration.GameStatus;
import org.example.client.model.enumaration.Winner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
public class GameModel implements ICheckerGameModel, IGameModel {
    List<IGameViewObserver> observers = new ArrayList<>();

    String gameId;

    GameStatus gameStatus;

    Winner winner;

    List<PlayerDto> players;

    Long currentPlayerId ;

    Map<Long, Color> playerColors;

    String board;

    public void notifyObserver(){
        for (IGameViewObserver observer : observers){
            observer.updateGameModel(this);
        }
    }

    @Override
    public void addObserver(IGameViewObserver iGameViewObserver){
        observers.add(iGameViewObserver);
    }

    @Override
    public void update(GameDTO gameDTO) {
        this.setGameId(gameDTO.getGameId());
        this.setGameStatus(gameDTO.getGameStatus());
        this.setPlayers(gameDTO.getPlayers());
        this.setWinner(gameDTO.getWinner());
        this.setPlayerColors(gameDTO.getPlayerColors());
        this.setCurrentPlayerId(gameDTO.getCurrentPlayerId());
        this.setBoard(gameDTO.getBoard());
        notifyObserver();
    }

    @Override
    public void reinitialize() {
        this.setGameId("");
        this.setGameStatus(GameStatus.LOBBY);
        this.setPlayers(new ArrayList<>());
        this.setBoard("");
        this.setWinner(Winner.NONE);
        notifyObserver();
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public String getBoard() {
        return board;
    }

    @Override
    public Long getCurrentPlayerId() {
        return currentPlayerId;
    }

    @Override
    public Map<Long, Color> getPlayerColors() {
        return playerColors;
    }

    @Override
    public List<PlayerDto> getPlayers() {
        return players;
    }

}