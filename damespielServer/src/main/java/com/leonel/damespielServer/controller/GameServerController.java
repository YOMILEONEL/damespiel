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
    @PostMapping("/start/{gameId}/{playerId}")
    public ResponseEntity<GameDTO> startGame(@PathVariable Long playerId, @PathVariable String gameId) throws Exception {
        return ResponseEntity.ok(gameService.startGame(playerId, gameId));
    }

    @PostMapping("/leave/{gameId}/{playerId}")
    public ResponseEntity<Boolean> leaveGame(@PathVariable Long playerId, @PathVariable String gameId) throws Exception {
        return ResponseEntity.ok(gameService.leaveGame(playerId, gameId));
    }

    @PostMapping("/move/{gameId}/{playerId}/{fromPosition}/{toPosition}")
    public ResponseEntity<GameDTO> makeMove(@PathVariable String gameId,
                                            @PathVariable Long playerId,
                                            @PathVariable String fromPosition,
                                            @PathVariable String toPosition) throws Exception {
        return ResponseEntity.ok(gameService.makeMove(gameId, playerId, fromPosition, toPosition));
    }

    @PostMapping("/switchPlayer/{gameId}/{currentPlayerId}")
    public ResponseEntity<GameDTO> switchPlayer(@PathVariable String gameId, @PathVariable Long currentPlayerId) throws Exception{
        return ResponseEntity.ok(gameService.switchPlayer(gameId, currentPlayerId));
    }




}

