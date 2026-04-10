package com.shobu.domain;

import com.shobu.domain.errors.*;;

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

    public static Board inital() {

        Stone[][] board = new Stone[4][4];

        for (int c = 0; c < 4; c++) {
            board[0][c] = Stone.WHITE;
            board[3][c] = Stone.BLACK;

        }

        return new Board(board);

    }

    public Stone getStoneAt(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Postion value must not be null");
        }
        int col = position.getCol();
        int row = position.getRow();
        return grid[row][col];

    }

    public Board applyMove(Position from, Direction direction, int distance) {

        if (from == null || direction == null) {
            throw new IllegalArgumentException("From Position Direction Or distance cannot be null");
        }
        if (distance <= 0 || distance > 2) {
            throw new IllegalArgumentException("Distance Cannot be less then or equal to one");
        }

        int newRow = from.getRow() + direction.dx * distance;
        int newCol = from.getCol() + direction.dy * distance;

        if (newRow >= 4 || newRow < 0 || newCol >= 4 || newCol < 0) {
            throw new PieceOutOfBoundsException("Current move set moves piece out side of playable area");
        }

        Stone stone = this.grid[from.getRow()][from.getCol()];
        if (stone == null) {
            throw new IllegalArgumentException("From position must contain a stone");
        }

        Stone oppStoneColor = (stone == Stone.BLACK) ? Stone.WHITE : Stone.BLACK;

        // Push Check one space
        int step1Row = from.getRow() + direction.dx;
        int step1Col = from.getCol() + direction.dy;

        int step2Row = from.getRow() + direction.dx * 2;
        int step2Col = from.getCol() + direction.dy * 2;

        if (this.grid[step1Row][step1Col] != null || this.grid[step2Row][step2Col] != null) {
            this.pushStone(from, direction, distance, stone, oppStoneColor);
        }

        Stone[][] next = copyGrid(grid);
        next[from.getRow() + direction.dx * distance][from.getCol() + direction.dy * distance] = stone;
        next[from.getRow()][from.getCol()] = null;

        return new Board(next);

    }

    private Board pushStone(Position from, Direction direction, int distance, Stone curStone,
            // TODO Push Stone detection and error handling
            // TODO: Push stone for distance 1
            // TODO Push Stone FOr Distance 2
            Stone oppStoneColor) {
        if (this.grid[newRow][newCol] == curStone) {
            throw new CannotPushOwnPieceException(
                    String.format(
                            "Your Piece: %s attempted to push same color piece, you must move the opposite piece: %s",
                            curStone,
                            oppStoneColor));

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Stone s = grid[r][c];
                sb.append(s == null ? "." : s.name().charAt(0));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
