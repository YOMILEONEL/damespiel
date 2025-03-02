package org.example.client.controller;


import org.example.client.IGameModel;
import org.example.client.IPlayerModel;
import org.example.client.IVewFactory;
import org.example.client.controller.dto.GameDTO;
import org.example.client.model.enumaration.GameStatus;
import org.example.client.model.enumaration.Winner;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * GameClientController handles client-side logic and communicates with the GameServerController.
 */
public class GameClientController implements IGameClientController{

    private final String SERVER_URL = "http://localhost:8080/api/game";
    private final RestTemplate restTemplate;
    private final IGameModel gameModel;
    private final IPlayerModel playerModel;
    private final IVewFactory viewFactory;
    private final IRoutingController routingController;
    boolean hasJoin = false;

    public GameClientController(RestTemplate restTemplate, IVewFactory viewFactory, IPlayerModel playerModel, IGameModel gameModel,IRoutingController routingController) {
        this.restTemplate = restTemplate;
        this.viewFactory = viewFactory;
        this.gameModel = gameModel;
        this.playerModel = playerModel;
        this.routingController = routingController;
    }

    /**
     * Sends a request to the server to create a new game.
     *
     * @return A JSON file (ObjectNode) containing details of the created game and server response.
     */
    @Override
    public void createGame()  {
        try {
            ResponseEntity<GameDTO> response = restTemplate.postForEntity(
                    SERVER_URL + "/create/" + playerModel.getId(),
                    null,
                    GameDTO.class
            );

            GameDTO game = response.getBody();
            hasJoin = false;
            gameModel.update(game);
            getGameStatus(game.getGameId(), hasJoin);
            this.routingController.navigate("waiting");
        } catch (Exception e) {
            viewFactory.showMessage("Das server hat ein problem warten sie ein bisschen");
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * Sends a request to the server to join an existing game.
     *
     * @param gameId The ID of the game to join.
     * @return A JSON file (ObjectNode) containing details of the game the player joined and server response.
     */
    @Override
    public void joinGame(String gameId) {
        try {
            ResponseEntity<GameDTO> response = restTemplate.postForEntity(
                    SERVER_URL + String.format("/join/%s/%d", gameId, playerModel.getId()),
                    null,
                    GameDTO.class
            );
            GameDTO game = response.getBody();
            gameModel.update(game);
            hasJoin = true;
            routingController.navigate("waiting");
            getGameStatus(gameId, hasJoin);
        } catch (RuntimeException e) {
            viewFactory.showMessage("Diese spielId existiert nicht");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void makeMove(String oldPosition, String newPosition){
            ResponseEntity<GameDTO> response = restTemplate.postForEntity(
                    SERVER_URL + String.format("/move/%s/%d/%s/%s", gameModel.getGameId(), playerModel.getId(),oldPosition  ,newPosition ),
                    null,
                    GameDTO.class
            );
    }

    @Override
    public void getGameStatus(String gameId,boolean hasJoin) {
        System.out.println("🔄 Attente de mise à jour du statut du jeu...");

        CompletableFuture.supplyAsync(() -> {
            ResponseEntity<GameDTO> response = restTemplate.getForEntity(
                    String.format("%s/status/%s/%d", SERVER_URL, gameId, playerModel.getId()),
                    GameDTO.class
            );
            return response.getBody();
        }).thenAccept(game -> {
            if (game == null) {
                System.out.println("❌ Erreur: game est null");
                return;
            }
            System.out.println("✔ Nouveau statut reçu: " + 3);

            if(game.getGameStatus() == GameStatus.END){
                if(game.getWinner() == Winner.TIE ){
                    viewFactory.showMessage("Niemand hat gewonnen");
                }
                else if(game.getWinner() == Winner.NONE){
                    viewFactory.showMessage("Das Spiel wurde gelöscht");
                }else {
                    if(game.getWinner().toString() == game.getPlayerColors().get(playerModel.getId()).toString()){
                        viewFactory.showMessage(playerModel.getName()+" hat gewonnen ");
                    }else {
                        viewFactory.showMessage(playerModel.getName() +" hat verloren ");
                    }
                }
                SwingUtilities.invokeLater(gameModel::reinitialize);
                routingController.navigate("home");
                return;
            }
            System.out.println("✔ Nouveau statut reçu: " + 2);

            if(!hasJoin && game.getGameStatus() == GameStatus.INPROGRESS){

                SwingUtilities.invokeLater(() -> {
                    gameModel.update(game);
                });
                getGameStatus(gameId, false);
            }
            System.out.println("✔ Nouveau statut reçu: " + 1);

            if(hasJoin && gameModel.getGameStatus() == GameStatus.READY && game.getGameStatus() == GameStatus.INPROGRESS){
                routingController.navigate("gameBoard");

                SwingUtilities.invokeLater(() -> {
                    gameModel.update(game);
                });
                getGameStatus(gameId, true);
            }

            if(hasJoin && game.getGameStatus() == GameStatus.INPROGRESS){

                SwingUtilities.invokeLater(() -> {
                    gameModel.update(game);
                });
                getGameStatus(gameId, true);
            }

            System.out.println("✔ Nouveau statut reçu: " + game.getGameStatus());


            if(!hasJoin && game.getGameStatus() == GameStatus.READY){
                SwingUtilities.invokeLater(() -> {
                    gameModel.update(game);

                });
            }
        }).exceptionally(ex -> {
            if (ex.getMessage().contains("503")) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                getGameStatus(gameId, hasJoin);
            }
            return null;
        });
    }

    @Override
    public void endTurn(){
        ResponseEntity<GameDTO> response = restTemplate.postForEntity(
                SERVER_URL + String.format("/switchPlayer/%s/%d", gameModel.getGameId(), playerModel.getId()),
                null,
                GameDTO.class
        );
    }

    @Override
    public void leaveGame() {
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity(
                    SERVER_URL + String.format("/leave/%s/%d", gameModel.getGameId(), playerModel.getId()) ,
                    null,
                    Boolean.class
            );
            if(response.getBody()){
                viewFactory.showMessage(playerModel.getName() +" hat das Spiel gelassen");
            }
            routingController.navigate("home");
        } catch (Exception e) {
            viewFactory.showMessage("Something is wrong");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void startGame() {
        try {
            ResponseEntity<GameDTO> response = restTemplate.postForEntity(
                    SERVER_URL + String.format("/start/%s/%d", gameModel.getGameId(), playerModel.getId()) ,
                    null,
                    GameDTO.class
            );
            GameDTO game = response.getBody();
            gameModel.update(game);
            getGameStatus(game.getGameId(), hasJoin);
            routingController.navigate("gameBoard");
        } catch (Exception e) {
            routingController.navigate("home");
            viewFactory.showMessage("Somethings is wrong");
            throw new RuntimeException(e.getMessage());
        }
    }

}
