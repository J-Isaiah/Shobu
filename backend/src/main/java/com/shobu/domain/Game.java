package com.shobu.domain;

import java.util.EnumMap;
import java.util.Map;

import com.shobu.domain.errors.InvalidMoveException;
import com.shobu.domain.moveData.Turn;

public class Game {
    private final Stone sideToMove;
    private final EnumMap<BoardId, Board> boards;

    public Game(Stone sideToMove, Map<BoardId, Board> boards) {
        if (sideToMove == null || boards == null) {
            throw new IllegalArgumentException("Game fields cannot be null");
        }

        for (BoardId id : BoardId.values()) {
            if (!boards.containsKey(id) || boards.get(id) == null) {
                throw new IllegalArgumentException("Game must contain all boards");
            }
        }

        this.sideToMove = sideToMove;
        this.boards = new EnumMap<>(boards);
    }

    public static Game start(Stone sideToMove) {
        EnumMap<BoardId, Board> boards = new EnumMap<>(BoardId.class);

        boards.put(BoardId.BLACK_LIGHT, Board.inital());
        boards.put(BoardId.BLACK_DARK, Board.inital());
        boards.put(BoardId.WHITE_LIGHT, Board.inital());
        boards.put(BoardId.WHITE_DARK, Board.inital());

        return new Game(sideToMove, boards);
    }

    public Game makeMove(Turn turn) {
        validateTurn(turn);

        Board newPassiveBoard = getBoardById(turn.passiveMove().boardId())
                .applyMove(
                        turn.passiveMove().start(),
                        turn.passiveMove().direction(),
                        turn.passiveMove().distance(),
                        sideToMove);

        Board newAggressiveBoard = getBoardById(turn.aggroMove().boardId())
                .applyMove(
                        turn.aggroMove().start(),
                        turn.aggroMove().direction(),
                        turn.aggroMove().distance(),
                        sideToMove);

        EnumMap<BoardId, Board> nextBoards = new EnumMap<>(boards);
        nextBoards.put(turn.passiveMove().boardId(), newPassiveBoard);
        nextBoards.put(turn.aggroMove().boardId(), newAggressiveBoard);

        Stone nextSide = sideToMove == Stone.WHITE ? Stone.BLACK : Stone.WHITE;

        return new Game(nextSide, nextBoards);
    }

    private void validateTurn(Turn turn) {
        if (turn == null) {
            throw new IllegalArgumentException("Turn cannot be null");
        }

        if (turn.passiveMove().boardId().getBoardOwner() != sideToMove) {
            throw new InvalidMoveException("Passive move must be on current player's home board");
        }

        if (turn.passiveMove().boardId().getShade() == turn.aggroMove().boardId().getShade()) {
            throw new InvalidMoveException("Aggressive move must be on opposite shade");
        }

        if (turn.passiveMove().distance() != turn.aggroMove().distance()) {
            throw new InvalidMoveException("Aggressive move distance must match passive move distance");
        }

        if (turn.passiveMove().direction() != turn.aggroMove().direction()) {
            throw new InvalidMoveException("Aggressive move direction must match passive move direction");
        }
    }

    public Stone getSideToMove() {
        return sideToMove;
    }

    public Board getBoardById(BoardId id) {
        return boards.get(id);
    }
}