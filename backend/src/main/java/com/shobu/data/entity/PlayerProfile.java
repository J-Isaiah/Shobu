package com.shobu.data.entity;

import com.shobu.service.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "players")
public class PlayerProfile {
    @Id
    @GeneratedValue
    private UUID playerId;
    private String playerName;
    private String playerPassword;

    protected PlayerProfile(){

    }

    public PlayerProfile(String playerName, String playerPassword) {
        this.playerName = playerName;
        this.playerPassword = playerPassword;
    }


    public UUID getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerPassword() {
        return playerPassword;
    }

}
