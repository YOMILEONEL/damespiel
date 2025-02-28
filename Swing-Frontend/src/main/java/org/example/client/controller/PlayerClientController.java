package org.example.client.controller;


import org.example.client.IPlayerModel;
import org.example.client.IVewFactory;
import org.example.client.controller.dto.PlayerDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;

/**
 * PlayerClientController handles player-related operations such as login and registration.
 */
public class PlayerClientController implements IPlayerClientController{
    private final String SERVER_URL = "http://localhost:8080/api/player";
    private final RestTemplate restTemplate;
    private final IPlayerModel playerModel;
    private final IVewFactory viewFactory;
    private final IRoutingController routingController;

    public PlayerClientController(RestTemplate restTemplate , IVewFactory viewFactory, IPlayerModel playerModel, IRoutingController routingController) {
        this.restTemplate = restTemplate;
        this.viewFactory = viewFactory;
        this.playerModel = playerModel;
        this.routingController = routingController;
    }

    /**
     * Validates the login credentials by sending a request to the server.
     *
     * @param name The username.
     * @param password The password.
     * @return A JSON file (ObjectNode) with the entered data and server response.
     */
    @Override
    public void login(String name, String password) {
        if (name.isEmpty() || password.isEmpty()) {
            viewFactory.showMessage("Name or password cannot be empty");
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            String payload = String.format("{\"name\":\"%s\",\"password\":\"%s\"}", name, password);
            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            ResponseEntity<PlayerDto> response = restTemplate.exchange(
                    SERVER_URL + "/login",
                    HttpMethod.POST,
                    request,
                    PlayerDto.class
            );

            playerModel.update(response.getBody());
            routingController.navigate("home");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public void updatePlayerName(String name)  {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Player does't exist!");
        }

        Long id = playerModel.getId();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String payload = String.format("{\"name\":\"%s\"}", name);
            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            ResponseEntity<PlayerDto> response = restTemplate.exchange(
                    SERVER_URL + "/update/" + id,
                    HttpMethod.PUT,
                    request,
                    PlayerDto.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                viewFactory.showMessage("Name successfully modified");
                playerModel.update(response.getBody());
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update name: " + response.getStatusCode());
            }

        } catch (Exception e) {
            viewFactory.showMessage("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Registers a new user by sending a request to the server.
     *
     * @param name The username.
     * @param password The password.
     * @return True if the registration is successful, false otherwise.
     */
    @Override
    public void register(String name, String password) {
        if (name.isEmpty() || password.isEmpty()) {
            viewFactory.showMessage("geben Sie bitte eine Name und ein Passwort"); ;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String body = String.format("{\"name\":\"%s\",\"password\":\"%s\"}", name, password);
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    SERVER_URL + "/registration",
                    HttpMethod.POST,
                    request,
                    String.class
            );
            routingController.navigate("login");
            response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            viewFactory.showMessage(e.getMessage());
            e.printStackTrace();

        }
    }

    public void logout(){
        playerModel.reinitialize();
        routingController.navigate("welcome");
    }




}
