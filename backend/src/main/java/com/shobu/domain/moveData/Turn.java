package com.shobu.domain.moveData;

public record Turn(
        Move passiveMove,
        Move aggroMove) {

    public Turn {
        if (passiveMove == null || aggroMove == null) {
            throw new IllegalArgumentException("Turn requires both moves");
        }
    }
}