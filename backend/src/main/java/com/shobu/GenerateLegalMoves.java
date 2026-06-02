package com.shobu;

import com.shobu.domain.Game;
import com.shobu.domain.Position;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Direction;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.moveData.LegalMove;
import com.shobu.domain.moveData.Move;
import com.shobu.domain.moveData.PassiveMoveInformation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateLegalMoves {
    private final Game game;
    private final Stone sideToMove;

    public GenerateLegalMoves(Game game) {
        this.game = game;
        this.sideToMove = game.getSideToMove();
    }

    public List<LegalMove> generateLegalMoves() {
        List<LegalMove> legalMoves = new ArrayList<>();

        for (PassiveMoveInformation passiveMoveInformation : generateFlatPassiveMoves()) {
            List<Move> aggressiveMoves = generateAggressiveMoves(passiveMoveInformation);

            if (!aggressiveMoves.isEmpty()) {
                legalMoves.add(new LegalMove(
                        passiveMoveInformation.move(),
                        aggressiveMoves
                ));
            }
        }

        return legalMoves;
    }

    private List<PassiveMoveInformation> generateFlatPassiveMoves() {
        List<PassiveMoveInformation> passiveMoves = new ArrayList<>();

        for (BoardId boardId : BoardId.values()) {
            if (boardId.getBoardOwner() != sideToMove) {
                continue;
            }

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    Position start = new Position(row, col);

                    if (game.getBoardById(boardId).getStoneAt(start) != sideToMove) {
                        continue;
                    }

                    for (int distance = 1; distance <= 2; distance++) {
                        for (Direction direction : Direction.values()) {
                            Move passiveMove = new Move(boardId, start, distance, direction);
                            Game stateAfterPassive = game.tryMakeMove(passiveMove);

                            if (stateAfterPassive != null) {
                                passiveMoves.add(new PassiveMoveInformation(
                                        boardId,
                                        passiveMove,
                                        stateAfterPassive
                                ));
                            }
                        }
                    }
                }
            }
        }

        return passiveMoves;
    }

    private List<Move> generateAggressiveMoves(PassiveMoveInformation passiveMoveInformation) {
        List<Move> aggressiveMoves = new ArrayList<>();
        Game stateAfterPassive = passiveMoveInformation.stateAfterPassive();

        for (BoardId boardId : passiveMoveInformation.boardId().aggressiveBoards()) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    Position start = new Position(row, col);

                    if (stateAfterPassive.getBoardById(boardId).getStoneAt(start) != sideToMove) {
                        continue;
                    }

                    Move aggressiveMove = new Move(
                            boardId,
                            start,
                            passiveMoveInformation.move().distance(),
                            passiveMoveInformation.move().direction()
                    );

                    if (stateAfterPassive.tryMakeMove(aggressiveMove) != null) {
                        aggressiveMoves.add(aggressiveMove);
                    }
                }
            }
        }

        return aggressiveMoves;
    }

    public EnumMap<BoardId, Map<Position, List<LegalMove>>> generateLegalMovesByBoardAndPosition() {
        EnumMap<BoardId, Map<Position, List<LegalMove>>> movesByBoard =
                new EnumMap<>(BoardId.class);

        for (LegalMove legalMove : generateLegalMoves()) {
            Move passiveMove = legalMove.passiveMove();

            movesByBoard
                    .computeIfAbsent(passiveMove.boardId(), ignored -> new HashMap<>())
                    .computeIfAbsent(passiveMove.start(), ignored -> new ArrayList<>())
                    .add(legalMove);
        }

        return movesByBoard;
    }
}