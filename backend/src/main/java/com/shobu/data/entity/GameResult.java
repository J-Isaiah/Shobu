package com.shobu.data.entity;

import com.shobu.domain.enums.Stone;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "game_results")
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int moveCount;
    private UUID whitePlayerId;
    private UUID blackPlayerId;
    private UUID winningPlayerId;

    @Enumerated(EnumType.STRING)
    private Stone winner;
}