package com.shobu.domain;

public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    LEFT_DOWN(-1, -1),
    RIGHT(0, 1),
    RIGHT_UP(1, 1);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}