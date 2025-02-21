package com.leonel.damespielServer.controller;
import com.leonel.damespielServer.model.dto.GameDTO;
import com.leonel.damespielServer.model.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/game")
public class GameServerController {

    @Autowired
    private GameService gameService;


    @PostMapping("/create/{playerId}")
    public ResponseEntity<GameDTO> createGame(@PathVariable Long playerId) throws Exception {
        System.out.println("jj");
        return ResponseEntity.ok(gameService.CreateGame(playerId));
    }

    @PostMapping("/join/{gameId}/{playerId}")
    public ResponseEntity<GameDTO> joinGame(@PathVariable Long playerId, @PathVariable String gameId) throws Exception{
        return ResponseEntity.ok(gameService.joinGame(playerId, gameId));
    }


    @PostMapping("/leave/{gameId}/{playerId}")
    public ResponseEntity<Boolean> leaveGame(@PathVariable Long playerId, @PathVariable String gameId) throws Exception {
        return ResponseEntity.ok(gameService.leaveGame(playerId, gameId));
    }




}

