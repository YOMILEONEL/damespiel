package org.example.client;


import org.example.client.controller.dto.PlayerDto;

public interface IPlayerModel {
    Long getId();
    String getName();
    void update(PlayerDto playerDto);
    void reinitialize();
}
