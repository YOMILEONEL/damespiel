package com.leonel.damespielServer.model.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.leonel.damespielServer.model.dto.GameDTO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

import java.util.concurrent.CompletableFuture;

/**
 * The {@code GameService} interface defines the operations available for managing a game.
 * It includes methods for creating a game, joining a game, getting the game status, starting a game, and leaving a game.
 * Each method involves interaction with a specific game using the player's unique ID and the game's unique ID.
 */
public interface GameService {

    /**
     * Creates a new game for a player.
     *
     * @param playerId The unique ID of the player creating the game.
     * @return A {@code GameDTO} containing the details of the newly created game.
     * @throws Exception If an error occurs while creating the game (e.g., invalid player ID, internal error).
     */
    public GameDTO CreateGame(Long playerId) throws Exception;

    /**
     * Allows a player to join an existing game.
     *
     * @param playerId The unique ID of the player joining the game.
     * @param gameId The unique ID of the game the player wants to join.
     * @return {@code true} if the player successfully joins the game, {@code false} otherwise.
     */
    public GameDTO joinGame(Long playerId, String gameId) throws Exception;



    /**
     * Allows a player to leave a game.
     *
     * @param playerId The unique ID of the player leaving the game.
     * @param gameId The unique ID of the game from which the player is leaving.
     * @return {@code true} if the player successfully leaves the game, {@code false} otherwise.
     */
    public boolean leaveGame(Long playerId, String gameId) throws Exception;


    /**
     * Wechselt den aktuell aktiven Spieler im laufenden Spiel.
     *
     * @param gameId          Die aktuelle Game-Id.
     * @param currentPlayerId Die ID des aktuellen Spielers vor dem Wechsel.
     */
    public GameDTO switchPlayer(String gameId, Long currentPlayerId) throws Exception;



    /**
     * Starts the game after both players have joined.
     *
     * @param playerId The unique ID of the player initiating the start of the game.
     * @param gameId The unique ID of the game being started.
     * @return A {@code GameDTO} containing the details of the game after it has been started.
     */
    public GameDTO startGame(Long playerId, String gameId) throws Exception;

}

