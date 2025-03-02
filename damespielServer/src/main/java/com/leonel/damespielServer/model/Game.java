package com.leonel.damespielServer.model;


import com.leonel.damespielServer.model.enumeration.Color;
import com.leonel.damespielServer.model.enumeration.GameStatus;
import com.leonel.damespielServer.model.enumeration.Winner;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;


    @Column(unique = true, nullable = false, updatable = false)
    String gameId;

    GameStatus gameStatus;

    Winner winner;

    @OneToMany
    @JoinColumn(name = "playerId")
    List<Player> players;

    Long currentPlayerId ;

    @ElementCollection
    @CollectionTable(name = "player_colors", joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyColumn(name = "player_id")
    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    Map<Long, Color> playerColors;

    String board;

    String lastPosition;

    boolean hasCaptured;


    public boolean getHasCaptured() {
        return hasCaptured;
    }
}
