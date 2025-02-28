package org.example.client.model;


import lombok.Data;
import org.example.client.ICheckerGameModel;
import org.example.client.IGameViewObserver;
import org.example.client.IPlayerModel;
import org.example.client.controller.dto.PlayerDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlayerModel implements ICheckerGameModel, IPlayerModel {
    List<IGameViewObserver> observers  = new ArrayList<>();

    Long id;
    String name;
    boolean inGame;

    public void notifyObserver(){
        for (IGameViewObserver observer : observers) {
            observer.updatePlayerModel(this);
        }
    }

    @Override
    public void addObserver(IGameViewObserver iGameViewObserver){
        observers.add(iGameViewObserver);
    }

    @Override
    public void update(PlayerDto playerDto) {
        this.setId(playerDto.getId());
        this.setName(playerDto.getName());
        this.setInGame(playerDto.isInGame());
        notifyObserver();
    }

    @Override
    public void reinitialize() {
        this.setId(null);
        this.setName("");
        this.setInGame(false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getId() {
        return id;
    }


}

