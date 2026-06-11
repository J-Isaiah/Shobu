package com.shobu.domain;

import java.util.EnumMap;
import java.util.Map;

import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.enums.TurnPhase;
import com.shobu.domain.errors.InvalidMoveException;
import com.shobu.domain.moveData.Move;

public class Game {
    private final TurnPhase turnPhase;
    private final Move pendingPassiveMove;
    private final EnumMap<BoardId, Board> boards;

    private final Stone winner;
    public Game(TurnPhase turnPhase , Map<BoardId, Board> boards) {
        this(turnPhase,null, boards,  null);
    }
    public Game(TurnPhase turnPhase,Move pendingPassiveMove, Map<BoardId, Board> boards) {
        this(turnPhase,pendingPassiveMove, boards,  null);
    }
    public Game(TurnPhase turnPhase,Move pendingPassiveMove, Map<BoardId, Board> boards, Stone winner) {
        if (turnPhase== null || boards == null) {
            throw new IllegalArgumentException("Game fields cannot be null");
        }

        for (BoardId id : BoardId.values()) {
            if (!boards.containsKey(id) || boards.get(id) == null) {
                throw new IllegalArgumentException("Game must contain all boards");
            }
        }

        this.turnPhase = turnPhase;
        this.pendingPassiveMove = pendingPassiveMove;
        this.boards = new EnumMap<>(boards);
        this.winner = winner;
    }

    public static Game start(Stone startColor) {
        EnumMap<BoardId, Board> boards = new EnumMap<>(BoardId.class);

        boards.put(BoardId.BLACK_LIGHT, Board.inital());
        boards.put(BoardId.BLACK_DARK, Board.inital());
        boards.put(BoardId.WHITE_LIGHT, Board.inital());
        boards.put(BoardId.WHITE_DARK, Board.inital());

        return new Game(TurnPhase.passivePhaseFor(startColor), boards);
    }

    public Game makeMove(Move move) {
        validateTurn(move);

        Board newBoard= getBoardById(move.boardId())
                .applyMove(
                        move.start(),
                        move.direction(),
                        move.distance(),
                        turnPhase);


        Stone winner = checkBoardWin(newBoard);

        EnumMap<BoardId, Board> nextBoards = new EnumMap<>(boards);
        nextBoards.put(move.boardId(), newBoard);

       TurnPhase nextPhase = turnPhase.next();


        if (winner != null) {
            return new Game(TurnPhase.GAME_OVER,move, nextBoards, winner);
        }

        return new Game(nextPhase,move, nextBoards);
    }

    private void validateTurn(Move move) {
        if (move== null) {
            throw new IllegalArgumentException("Turn cannot be null");
        }

        if (!turnPhase.isAggressive() && boards.get(move.boardId()).moveWouldPush(move)){
            throw new InvalidMoveException("cannot push a stone during passive stage");
        }

        if (!turnPhase.isAggressive() && move.boardId().getBoardOwner() != turnPhase.getSideToMove()) {

            throw new InvalidMoveException("Passive move must be on current player's home board.tsx");
        }

        if (this.turnPhase.isAggressive() && move.boardId().getShade() == this.pendingPassiveMove.boardId().getShade()) {
            throw new InvalidMoveException("Aggressive move must be on opposite shade");
        }

        if (this.turnPhase.isAggressive()&&this.pendingPassiveMove.distance() != move.distance()) {
            throw new InvalidMoveException("Aggressive move distance must match passive move distance");
        }

        if (this.turnPhase.isAggressive()&&pendingPassiveMove.direction() != move.direction()) {
            throw new InvalidMoveException("Aggressive move direction must match passive move direction");
        }
    }

    public Stone getSideToMove() {
        return turnPhase.getSideToMove();
    }

    public Board getBoardById(BoardId id) {
        return boards.get(id);
    }

    public Stone checkBoardWin(Board board) {
        int white = 0;
        int black = 0;

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Stone cell = board.getStoneAt(new Position(r, c));

                if (cell == Stone.WHITE) {
                    white++;
                } else if (cell == Stone.BLACK) {
                    black++;
                }
            }
        }

        if (white == 0)
            return Stone.BLACK;
        if (black == 0)
            return Stone.WHITE;

        return null;
    }

    public Stone getWinner() {
        return this.winner;
    }

    public Map<BoardId, Board> getBoards() {
        return new EnumMap<>(boards);
    }

    public TurnPhase getTurnPhase() {
        return this.turnPhase;
    }

    public Game tryMakeMove(Move move) {
        try {
            return makeMove(move);
        } catch (RuntimeException e) {
            return null;
        }
    }
    public Move getPendingPassiveMove() {
        return pendingPassiveMove;
    }
}