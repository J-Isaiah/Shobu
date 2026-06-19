import type { Dispatch, SetStateAction } from "react";
import {BoardId} from "../../enums/game.ts";
import type {CellSelection} from "../../types/game/Cell.ts";
import type {BoardCoordinate, GameState} from "../../types/game/MoveTypes.ts";
import {getMoveEnd, getSideToMove, isAggressiveMove} from "../../utils/game/movePhase.ts";

export function useMoveHighlighting(setUiError: Dispatch<SetStateAction<string | null>>, gameState: GameState | null, isPendingMove: boolean, firstSelection: CellSelection | null) {
    const playerColor = localStorage.getItem("playerColor")


    function isMovableStone(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): boolean {
        // Highlights passive and aggressive stones

        if (firstSelection || isPendingMove) {
            return false
        }

        if (gameState === null) {
            return false
        }
        if (playerColor != getSideToMove(gameState.turnPhase)){
            return false
        }
        if (!isAggressiveMove(gameState.turnPhase) ) {
            // if (gameState.legalMovesForPlayer[boardId][`${row},${col}`]) {
            const legalMovesForBoard = gameState.legalMovesForPlayer[boardId]

            if (legalMovesForBoard && legalMovesForBoard[`${row},${col}`]) {
                return true;
            }


        }
        if (gameState.pendingPassiveMove == null) {
            return false
        }

        const allAggressiveMoves = gameState.legalMovesForPlayer[gameState.pendingPassiveMove.boardId][`${gameState.pendingPassiveMove.start.row},${gameState.pendingPassiveMove.start.col}`][0] ?? []
        if (!allAggressiveMoves) {
            return false
        }

        for (const aggressiveMove of allAggressiveMoves.aggressiveMoves) {
            if (aggressiveMove.start.row == row && aggressiveMove.start.col == col && aggressiveMove.boardId == boardId) {
                return true
            }
        }
        return false


    }

    function isSelectedStone(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): boolean {
        // Highlights Selected Stone
        if (playerColor != getSideToMove(gameState.turnPhase)){
            return false
        }
        return firstSelection?.boardId == boardId && firstSelection.position.row == row && firstSelection.position.col == col;

    }

    function isAvailableCellToMove(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate) {
        if (!gameState) return false
        if (playerColor != getSideToMove(gameState.turnPhase)){
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

    return {isAvailableCellToMove, isSelectedStone, isMovableStone}

}