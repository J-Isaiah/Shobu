package com.shobu.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name="players")
public class Player {
    @Id
    @GeneratedValue
    private UUID playerId;
    private String playerName;
}
