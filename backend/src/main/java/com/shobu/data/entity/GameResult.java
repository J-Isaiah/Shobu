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

    private UUID whitePlayerId;
    private UUID blackPlayerId;
    private UUID winningPlayerId;

    @Enumerated(EnumType.STRING)
    private Stone winner;

    protected GameResult() {
    }

    public GameResult(
            UUID whitePlayerId,
            UUID blackPlayerId,
            UUID winningPlayerId,
            Stone winner
    ) {
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.winningPlayerId = winningPlayerId;
        this.winner = winner;
    }

    public Long getId() {
        return id;
    }

    public UUID getWhitePlayerId() {
        return whitePlayerId;
    }

    public UUID getBlackPlayerId() {
        return blackPlayerId;
    }

    public UUID getWinningPlayerId() {
        return winningPlayerId;
    }

    public Stone getWinner() {
        return winner;
    }
}