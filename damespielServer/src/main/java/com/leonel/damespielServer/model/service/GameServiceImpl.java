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

import static com.leonel.damespielServer.model.Board.mapStringToBoard;


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

    @Override
    public GameDTO switchPlayer(String gameId, Long currentPlayerId) throws Exception {
        Game game = gameRepository.findByGameId(gameId).orElseThrow(
                () -> new RuntimeException("Game does not exist")
        );
        // Liste der Spieler im Spiel (sollte genau 2 sein)
        List<Player> players = game.getPlayers();

        if (players.size() != 2) {
            throw new IllegalStateException("Unable to switch player: invalid number of players in game.");
        }
        if (!(Objects.equals(game.getCurrentPlayerId(), currentPlayerId))) {
            throw new Exception("You are not the currentPlayer");
        }

        // Aktuell aktiven Spieler identifizieren und auf den nächsten wechseln
        for (Player player : players) {
            if (!player.getId().equals(currentPlayerId)) {
                game.setHasCaptured(false);
                game.setLastPosition("");
                game.setCurrentPlayerId(player.getId());
                gameRepository.save(game);// Speichern des Spiels nach dem Wechsel des Spielers
                cache.put(gameId,game);
                System.out.println("Switched player to: " + player.getId());
                return GameMapper.toDTO(game);
            }
        }
        throw new IllegalStateException("Unable to switch player: current player not found in game.");
    }

    @Override
    public GameDTO startGame(Long playerId, String gameId) throws Exception {

        //Check if the game exists
        Optional<Game> optionalGame = gameRepository.findByGameId(gameId);
        if (optionalGame.isEmpty()) {
            throw new Exception("Game does not exist");
        }
        Game game = optionalGame.get();

        //Check if the game is in the READY state
        if (game.getGameStatus() != GameStatus.READY) {
            throw new IllegalStateException("Game is not ready to start. Ensure both players have joined.");
        }


        //Initialize the game board
        Board board = new Board();
        game.setBoard(board.toString());

        // Update the game status and set the current player
        game.setGameStatus(GameStatus.INPROGRESS);
        game.setCurrentPlayerId(playerId);

        cache.put(gameId,game);

        // Save the changes to the database
        gameRepository.save(game);

        // Return the game's details as a GameDTO
        return GameMapper.toDTO(game);
    }

    @Override
    public GameDTO makeMove(String gameId, Long playerId, String fromPosition, String toPosition) throws Exception {
        Game game = gameRepository.findByGameId(gameId).orElseThrow(
                () -> new RuntimeException("Game does not exist")
        );

        if (!game.getCurrentPlayerId().equals(playerId)) {
            throw new Exception("Not the current player's turn.");
        }


        Board board = mapStringToBoard(game.getBoard());


        Color color = game.getPlayerColors().get(playerId);

        int beforCount =0;
        int nextCount =0;

        List<Player> players = game.getPlayers();
        for (Player player : players) {
            if (!player.getId().equals(playerId)) {
                beforCount = board.getEnemyToken(game.getPlayerColors().get(player.getId()));
            }
        }


        try {
            if(game.getLastPosition().isEmpty()){
                board.makeMove(fromPosition, toPosition, color);
                game.setLastPosition(toPosition);
                for (Player player : players) {
                    if (!player.getId().equals(playerId)) {
                        nextCount = board.getEnemyToken(game.getPlayerColors().get(player.getId()));
                    }
                }
                game.setHasCaptured(nextCount==beforCount-1);
            }else{
                if(game.getLastPosition().equals(fromPosition) && game.getHasCaptured()){

                    board.makeMove(fromPosition, toPosition, color);
                    for (Player player : players) {
                        if (!player.getId().equals(playerId)) {
                            nextCount = board.getEnemyToken(game.getPlayerColors().get(player.getId()));                    }
                    }

                    game.setLastPosition(toPosition);
                    game.setHasCaptured(nextCount==beforCount-1);

                }
            }

        } catch (IllegalStateException e) {
            game.setBoard(board.toString());
            gameRepository.save(game);
            throw new Exception(e.getMessage());
        }

        // Board speichern
        game.setBoard(board.toString());

        if(board.isGameOver()){
            game.setGameStatus(GameStatus.END);
            game.setWinner(board.determineWinner());
        }

        gameRepository.save(game);
        cache.put(gameId, game);
        return GameMapper.toDTO(game);
    }


}
