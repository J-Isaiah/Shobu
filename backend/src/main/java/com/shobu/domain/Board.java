package com.shobu.domain;

public class Board {

    private final Stone[][] grid;

    public Board(Stone[][] grid) {
        if (grid == null) {
            throw new IllegalArgumentException("Grid cannot be null");
        }

        if (grid.length != 4) {
            throw new IllegalArgumentException("Grid must have a row length of 4");
        }
        for (Stone[] grid1 : grid) {
            if (grid1 == null) {
                throw new IllegalArgumentException("Cols Must not be null");
            }
            if (grid1.length != 4) {
                throw new IllegalArgumentException("Grid must have a cols lengh of 4");
            }
        }

        this.grid = copyGrid(grid);

    }

    private static Stone[][] copyGrid(Stone[][] source) {
        Stone[][] copy = new Stone[4][4];
        for (int i = 0; i < 4; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    public static Board empty() {
        return new Board(new Stone[4][4]);
    }

    public Stone getStoneAt(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Postion value must not be null");
        }
        int col = position.getCol();
        int row = position.getRow();
        return grid[row][col];

    }

    public Board applyMove(Position from, Position to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From or To cannot be null when applying a new move");
        }
        Stone stone = this.grid[from.getRow()][from.getCol()];
        if (stone == null) {
            throw new IllegalArgumentException("From position must contain a stone");
        }

        Stone[][] next = copyGrid(grid);
        next[to.getRow()][to.getCol()] = stone;
        next[from.getRow()][from.getCol()] = null;

        return new Board(next);

    }
}
