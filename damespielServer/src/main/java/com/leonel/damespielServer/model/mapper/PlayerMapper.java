package com.leonel.damespielServer.model.mapper;


import com.leonel.damespielServer.model.Player;
import com.leonel.damespielServer.model.dto.PlayerDto;
import com.leonel.damespielServer.model.dto.PlayerRequest;

public class PlayerMapper {

    public static PlayerDto toDTO(Player player){
        return PlayerDto.builder()
                .id(player.getId())
                .name(player.getName())
                .inGame(player.isInGame())
                .build();
    }

    public static Player RequestToDTO(PlayerRequest playerRequest){
        return Player.builder()
                .name(playerRequest.name())
                .password(playerRequest.password())

                .build();
    }




}
