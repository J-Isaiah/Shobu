// canSelectStone.ts

import { BoardId } from "../../enums/game.ts";
import type { BoardCoordinate, GameState } from "../../types/game/MoveTypes.ts";
import { getSideToMove, isAggressiveMove } from "./movePhase.ts";

export function canSelectStone(
    gameState: GameState | null,
    playerColor: string | null,
    boardId: BoardId,
    row: BoardCoordinate,
    col: BoardCoordinate
): boolean {
    if (!gameState) {
        return false;
    }

    if (playerColor != getSideToMove(gameState.turnPhase)) {
        return false;
    }

    if (!isAggressiveMove(gameState.turnPhase)) {
        const legalMovesForBoard =
            gameState.legalMovesForPlayer[boardId];

        return !!legalMovesForBoard?.[`${row},${col}`];
    }

    if (gameState.pendingPassiveMove == null) {
        return false;
    }

    const aggressiveMoveGroup =
        gameState.legalMovesForPlayer[
            gameState.pendingPassiveMove.boardId
            ][
            `${gameState.pendingPassiveMove.start.row},${gameState.pendingPassiveMove.start.col}`
            ][0];

    if (!aggressiveMoveGroup) {
        return false;
    }

    return aggressiveMoveGroup.aggressiveMoves.some(
        move =>
            move.boardId === boardId &&
            move.start.row === row &&
            move.start.col === col
    );
}