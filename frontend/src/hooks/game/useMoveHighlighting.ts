import {BoardId} from "../../enums/game.ts";
import type {CellSelection} from "../../types/game/Cell.ts";
import type {BoardCoordinate, GameState} from "../../types/game/MoveTypes.ts";
import {getMoveEnd, getSideToMove, isAggressiveMove} from "../../utils/game/movePhase.ts";
import {canSelectStone} from "../../utils/game/canSelectStone.ts";

function useMoveHighlighting(gameState: GameState | null, firstSelection: CellSelection | null, isPendingMove: boolean) {
    let playerColor: string | null;

    const gameInfo = localStorage.getItem(`game:${gameState?.gameId}`);
    if (gameInfo != null) {
        playerColor = JSON.parse(gameInfo).playerColor
    }

    function isSelectedStone(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): boolean {
        // Highlights Selected Stone
        if (!gameState) return false
        if (playerColor != getSideToMove(gameState.turnPhase)) {
            return false
        }
        return firstSelection?.boardId == boardId && firstSelection.position.row == row && firstSelection.position.col == col;

    }

    function isAvailableCellToMove(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate) {
        if (!gameState) return false
        if (playerColor != getSideToMove(gameState.turnPhase)) {
            return false
        }

        if ((firstSelection?.boardId != boardId) || !gameState) {
            return false
        }


        if (isAggressiveMove(gameState.turnPhase)) {

            const legalMovesForBoard = gameState.legalMovesForPlayer[gameState.pendingPassiveMove.boardId][`${gameState.pendingPassiveMove.start.row},${gameState.pendingPassiveMove.start.col}`]

            if (!legalMovesForBoard) {
                return false
            }
            // when aggressive move

            for (const moves of legalMovesForBoard) {
                for (const move of moves.aggressiveMoves) {
                    if (move.direction != gameState.pendingPassiveMove.direction || move.start.row != firstSelection.position.row || move.start.col != firstSelection.position.col) {
                        continue
                    }
                    const endPosition = getMoveEnd(move)
                    if (endPosition.row == row && endPosition.col == col) {

                        return true

                    }
                }


            }
        }
        if (!isAggressiveMove(gameState.turnPhase)) {

            const passiveMoves = gameState.legalMovesForPlayer[boardId]

            const allMoves = passiveMoves[`${firstSelection.position.row},${firstSelection.position.col}`]

            if (!allMoves) return false


            for (const move of allMoves) {

                const endPosition = getMoveEnd(move.passiveMove)
                if (endPosition.row == row && endPosition.col == col) {
                    return true
                }
            }
        }
        return false


    }


    function isMovableStone(
        boardId: BoardId,
        row: BoardCoordinate,
        col: BoardCoordinate
    ): boolean {
        if (firstSelection || isPendingMove) {
            return false;
        }

        return canSelectStone(
            gameState,
            playerColor,
            boardId,
            row,
            col
        );
    }

    return {isAvailableCellToMove, isSelectedStone, isMovableStone}

}

export default useMoveHighlighting