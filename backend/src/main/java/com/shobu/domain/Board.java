package com.shobu.domain;

import com.shobu.domain.errors.CannotPushOwnPieceException;
import com.shobu.domain.errors.InvalidMoveException;
import com.shobu.domain.errors.PieceOutOfBoundsException;
//TODO: Make sure that you cannot push a player during your passive move
public class Board {
    private record MovePath(
            int oneRow,
            int oneCol,
            int twoRow,
            int twoCol, int threeRow, int threeCol) {
    }

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
            board[0][c] = Stone.BLACK;;
            board[3][c] = Stone.WHITE;

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

    public Board applyMove(Position from, Direction direction, int distance, Stone sideToMove, MoveType moveType) {

        if (from == null || direction == null) {
            throw new IllegalArgumentException("From Position Direction Or distance cannot be null");
        }
        if (distance <= 0 || distance > 2) {
            throw new IllegalArgumentException("Distance Cannot be less then or equal to one");
        }

        int newRow = from.getRow() + direction.dx * distance;
        int newCol = from.getCol() + direction.dy * distance;

        if (!isInBounds(newRow, newCol, this.grid)) {
            throw new PieceOutOfBoundsException("Current move set moves piece out side of playable area");
        }

        Stone stone = this.grid[from.getRow()][from.getCol()];
        if (stone == null) {
            throw new IllegalArgumentException("From position must contain a stone");
        }

        if (stone != sideToMove) {
            throw new InvalidMoveException("You cannot select an opponants stone to move");
        }
        int oneMoveRow = from.getRow() + direction.dx;
        int oneMoveCol = from.getCol() + direction.dy;
        int twoMoveRow = from.getRow() + direction.dx * 2;
        int twoMoveCol = from.getCol() + direction.dy * 2;
        int thirdMoveCol = from.getCol() + direction.dy * 3;

        int thirdMoverow = from.getRow() + direction.dx * 3;

        MovePath path = new MovePath(oneMoveRow, oneMoveCol, twoMoveRow, twoMoveCol, thirdMoverow, thirdMoveCol);

        boolean oneSpaceMove = this.grid[oneMoveRow][oneMoveCol] != null;
        boolean twoSpaceMove = distance == 2 && grid[twoMoveRow][twoMoveCol] != null;
        boolean needsPush = oneSpaceMove || twoSpaceMove;
        if (needsPush) {
            if (moveType == MoveType.PASSIVE) {
                throw new InvalidMoveException("Cannot push stone during the your passive move");

            }
            return pushStone(from, direction, distance, sideToMove, this.grid, path);
        }

        Stone[][] next = copyGrid(grid);
        next[newRow][newCol] = stone;
        next[from.getRow()][from.getCol()] = null;

        return new Board(next);

    }

    private Board pushStone(Position from, Direction direction, int distance, Stone sideToMove, Stone[][] grid,
            MovePath path) {
        Stone stoneToMove = grid[from.getRow()][from.getCol()];
        if (stoneToMove != sideToMove) {
            throw new CannotPushOwnPieceException("Must Move Your Own Stone First");
        }

        // Check if stone that needs to be pushed is blocked
        boolean isOnePushBlocked = distance == 1 && isInBounds(path.twoRow(), path.twoCol(), grid)
                && grid[path.twoRow()][path.twoCol()] != null;

        boolean isLongTwoPushBlocked = distance == 2 && isInBounds(path.threeRow(), path.threeCol(), grid)
                && grid[path.threeRow()][path.threeCol()] != null;

        boolean isShortTwoBlockedClose = distance == 2 && grid[path.oneRow()][path.oneCol()] != null
                && grid[path.twoRow()][path.twoCol()] != null;

        if (isOnePushBlocked || isLongTwoPushBlocked || isShortTwoBlockedClose) {
            System.out.println(
                    "one=" + isOnePushBlocked
                            + " longTwo=" + isLongTwoPushBlocked
                            + " shortTwo=" + isShortTwoBlockedClose + "ghost piece"
                            + grid[path.threeRow()][path.threeCol()] != null);
            throw new InvalidMoveException("Stone Blocked cannot push");
        }

        Stone[][] next = copyGrid(grid);

        Stone rockToMove = null;

        if (distance == 1) {

            rockToMove = next[path.oneRow()][path.oneCol()]; // Get Rock color to

        } else if (distance == 2) {
            rockToMove = next[path.oneRow()][path.oneCol()] != null ? next[path.oneRow()][path.oneCol()]
                    : next[path.twoRow()][path.twoCol()]; // Get Rock color to
        }

        if (rockToMove == sideToMove) {
            throw new InvalidMoveException("Cannot Push Same Color Stone");
        }

        // Normal Push
        if (distance == 1) {
            if (isInBounds(path.twoRow(), path.twoCol(), grid)) {
                // Move Opp Stone Out Of the way
                next[path.oneRow()][path.oneCol()] = null;
                next[path.twoRow()][path.twoCol()] = rockToMove;

                // Set prev stone position to null

            } else {
                next[path.oneRow()][path.oneCol()] = null;
            }
        } else if (distance == 2) {
            next[path.oneRow()][path.oneCol()] = null;
            next[path.twoRow()][path.twoCol()] = null;
            if (isInBounds(path.threeRow(), path.threeCol(), grid)) {
                // Move Opp Stone Out Of the way

                next[path.threeRow()][path.threeCol()] = rockToMove;

            } else {
                next[path.twoRow()][path.twoCol()] = null;
            }
        }

        next[from.getRow() + direction.dx * distance][from.getCol() + direction.dy * distance] = stoneToMove;
        next[from.getRow()][from.getCol()] = null;

        return new Board(next);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  0 1 2 3\n");

        for (int r = 0; r < 4; r++) {
            sb.append(r).append(" ");

            for (int c = 0; c < 4; c++) {
                Stone s = grid[r][c];
                sb.append(s == null ? "." : s.name().charAt(0));
                sb.append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private boolean isInBounds(int row, int col, Stone[][] grid) {
        return row >= 0 && row < grid.length &&
                col >= 0 && col < grid[0].length;
    }
    public Stone[][] getGrid() {
        return copyGrid(this.grid);
    }
}
