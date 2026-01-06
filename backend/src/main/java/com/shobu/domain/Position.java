package com.shobu.domain;

import java.util.Objects;

public final class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        if (row < 0 || row >= 4) {
            throw new IllegalArgumentException("Row Out of Bounds");
        }
        if (col < 0 || col >= 4) {
            throw new IllegalArgumentException("Col Out of Bounds");
        }

        this.row = row;
        this.col = col;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Position other))
            return false;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}