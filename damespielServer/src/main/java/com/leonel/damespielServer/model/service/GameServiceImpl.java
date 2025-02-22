package com.leonel.damespielServer.model.service;

import com.leonel.damespielServer.model.Board;
import com.leonel.damespielServer.model.Game;
import com.leonel.damespielServer.model.Player;
import com.leonel.damespielServer.model.dto.GameDTO;
import com.leonel.damespielServer.model.enumeration.Color;
import com.leonel.damespielServer.model.enumeration.GameStatus;
import com.leonel.damespielServer.model.enumeration.Winner;
import com.leonel.damespielServer.model.mapper.GameMapper;
import com.leonel.damespielServer.model.repository.GameRepository;
import com.leonel.damespielServer.model.repository.PlayerRepository;
import com.leonel.damespielServer.model.utils.GameIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;



/**
 * The {@code GameServiceImpl} class implements the {@link GameService} interface
 * to handle the logic and operations for managing a game. This service includes methods
 * for creating a game, checking player availability, and other game-related operations.
 * It interacts with repositories for retrieving game and player information and
 * uses a {@code Board} to manage the game state.
 */
@Service
public class GameServiceImpl implements GameService {

    ConcurrentHashMap<String, Game> cache =  new ConcurrentHashMap<>();

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    /**
     * Creates a new game and assigns the given player as the first player.
     * Checks if the player is already in another game and generates a unique game ID.
     *
     * @param playerId The unique ID of the player who is creating the game.
     * @return A {@link GameDTO} object containing the details of the newly created game.
     * @throws Exception If the player is already in another game or if a unique game ID cannot be generated.
     */
    @Override
    public GameDTO CreateGame(Long playerId) throws Exception {

        Player player = playerService.getPlayerById(playerId);

        // Check if the player is already in another game
        if(playerService.playerInGame(player)){
            throw new Exception("This player is already in game");
        }

        // Generate a unique game ID
        String gameId = GameIdGenerator.generateGameId();
        Optional<Game> optionalGame = gameRepository.findByGameId(gameId);

        // Ensure the game ID is unique by trying 5 times if needed
        if(optionalGame.isPresent()){
            for (int i = 0; i < 5; i++){
                gameId = GameIdGenerator.generateGameId();
                optionalGame = gameRepository.findByGameId(gameId);
                if (optionalGame.isEmpty()){
                    break;
                }
            }
            if (optionalGame.isPresent()){
                throw new Exception("Impossible to generate new Game id");
            }
        }


        // Create a new board for the game
        Board board = new Board();
        String stringBoard = board.toString();

        // Retrieve player data and assign the player to the game
        List<Player> players = new ArrayList<>();

        player.setInGame(true);
        playerRepository.save(player);
        players.add(player);

        // Assign player color
        Map<Long, Color> playerColor = new HashMap<>();
        playerColor.put(playerId, Color.WHITE);

        // Create and save the game
        Game game = Game.builder()
                .gameId(gameId)
                .gameStatus(GameStatus.LOBBY)
                .winner(Winner.TIE)
                .players(players)
                .lastPosition("")
                .currentPlayerId(playerId)
                .playerColors(playerColor)
                .board(stringBoard)
                .build();

        gameRepository.save(game);
        System.out.println(gameId);
        // Return the game details as a DTO
        return GameMapper.toDTO(game);
    }

    /**
     * Allows a player to join an existing game.
     *
     * @param playerId The unique ID of the player joining the game.
     * @param gameId The unique ID of the game the player wants to join.
     * @return {@code true} if the player successfully joins the game, {@code false} otherwise.
     */
    @Override
    public GameDTO joinGame(Long playerId, String gameId) throws Exception {
        // Retrieve the player by ID
        Player player = playerService.getPlayerById(playerId);

        // Check if the player is available to join a new game
        if (playerService.playerInGame(player)) {
            throw new Exception("This player is already in a game");
        }

        // Retrieve the game by ID
        Optional<Game> optionalGame = gameRepository.findByGameId(gameId);
        if (optionalGame.isEmpty()) {
            throw new Exception("Game does not exist");
        }

        Game game = optionalGame.get();

        // Ensure the game is in the lobby state
        if (game.getGameStatus() != GameStatus.LOBBY) {
            throw new Exception("Game is not in the Lobby state");
        }

        // Ensure the game has fewer than two players
        if (game.getPlayers().size() >= 2) {
            throw new Exception("Game already has two players");
        }

        // Mark the player as being in a game
        player.setInGame(true);
        playerRepository.save(player);

        // Add the player to the game
        game.getPlayers().add(player);

        // Assign a color to the player
        Map<Long, Color> playerColors = game.getPlayerColors();
        playerColors.put(playerId, playerColors.containsValue(Color.WHITE) ? Color.BLACK : Color.WHITE);

        // Update the game status if two players are now present
        if (game.getPlayers().size() == 2) {
            game.setGameStatus(GameStatus.READY);
        }
        GameDTO gameDto = GameMapper.toDTO(game);
        cache.put(gameId, game);
        // Save the updated game
        gameRepository.save(game);

        return gameDto;
    }

    /**
     * Allows a player to leave an existing game.
     * @param playerId The unique ID of the player leaving the game.
     * @param gameId The unique ID of the game the player is leaving.
     * @return A {@link GameDTO} containing the details of the game after the player has left.
     */
    @Override
    public boolean leaveGame(Long playerId, String gameId) throws Exception{
        // Check if the game exists
        Optional<Game> optionalGame = gameRepository.findByGameId(gameId);
        if (optionalGame.isEmpty()) {
            throw new Exception("Game does not exist");
        }
        Game game = optionalGame.get();

        // Determine the winner based on the leaving player's color
        Color leavingPlayerColor = game.getPlayerColors().get(playerId);
        if (leavingPlayerColor == null) {
            throw new IllegalStateException("No color assigned to the player with ID " + playerId);
        }

        if (game.getPlayerColors().size() == 2) {

            // Determine the winner based on the leaving player's color
            if (leavingPlayerColor == Color.BLACK) {
                game.setWinner(Winner.WHITE); // Opponent wins
            } else if (leavingPlayerColor == Color.WHITE) {
                game.setWinner(Winner.BLACK); // Opponent wins
            } else {
                throw new IllegalStateException("Unexpected color for player with ID " + playerId);
            }
        }

        // Set the game status to END
        game.setGameStatus(GameStatus.END);

        // Mark both players as inactive
        for (Player player : game.getPlayers()) {
            player.setInGame(false);
            playerRepository.save(player);
        }

        //Save the updated game
        gameRepository.save(game);
        return true;
    }


}
