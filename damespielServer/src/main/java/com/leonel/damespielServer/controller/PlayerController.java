package com.leonel.damespielServer.controller;



import com.leonel.damespielServer.model.dto.PlayerDto;
import com.leonel.damespielServer.model.dto.PlayerRequest;
import com.leonel.damespielServer.model.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;


    @PostMapping("/registration")
    public ResponseEntity<Boolean> authenticate( @RequestBody @Valid PlayerRequest playerRequest) throws Exception {
        return ResponseEntity.ok(playerService.authenticatePlayer( playerRequest));
    }


    @PostMapping("/login")
    public ResponseEntity<PlayerDto> login(@RequestBody  PlayerRequest playerRequest) throws Exception {
       System.out.println("00");
        return ResponseEntity.ok(playerService.loginPlayer(playerRequest));
    }

}
