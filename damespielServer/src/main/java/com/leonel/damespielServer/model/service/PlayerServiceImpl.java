package com.leonel.damespielServer.model.service;



import com.leonel.damespielServer.model.Player;
import com.leonel.damespielServer.model.dto.PlayerDto;
import com.leonel.damespielServer.model.dto.PlayerRequest;

import com.leonel.damespielServer.model.mapper.PlayerMapper;
import com.leonel.damespielServer.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public boolean authenticatePlayer(PlayerRequest playerRequest) throws Exception {

        Optional<Player> optionalPlayer = playerRepository.findByName(playerRequest.name());

        if(optionalPlayer.isPresent()) {
            throw new Exception(String.format("Player with username %s already exist", playerRequest.name()));
        }
        Player player = Player.builder().
                name(playerRequest.name()).
                password(playerRequest.password()).build();

        playerRepository.save(player);
        return true;
    }


    @Override
    public PlayerDto loginPlayer(PlayerRequest playerRequest) throws Exception {
        // Fetch the player by username
        // If player not found, throw exception

        Player player = getPlayerByName(playerRequest.name());
        if(player == null){
            throw new Exception(String.format("Player with username %s not found", playerRequest.name()));
        }
        // Validate password
        if (!player.getPassword().equals(playerRequest.password())) {
            throw new Exception("Invalid username or password");
        }


        return PlayerMapper.toDTO(player);
    }

    public boolean playerInGame(Player player){
        return player.isInGame();
    }

    public Player getPlayerById(Long playerId) throws Exception{
        return playerRepository.findById(playerId).orElseThrow(() ->
               new Exception("This PLayer does not exist")
       );
    }

    public Player getPlayerByName(String playerName){
        return playerRepository.findByName(playerName).get();
    }



}
