import {type BoardId, Direction} from "../../enums/game.ts";
import type {Move, PlayerColor, Position, TurnPhase} from "../../types/game/MoveTypes.ts";

export function buildMove(
    boardId: BoardId,
    start: Position,
    end: Position
): Move {
    const rowDiff = end.row - start.row;
    const colDiff = end.col - start.col;

    const absRowDiff = Math.abs(rowDiff);
    const absColDiff = Math.abs(colDiff);

    const isStraight = absRowDiff === 0 || absColDiff === 0;
    const isDiagonal = absRowDiff === absColDiff;

    if (!isStraight && !isDiagonal) {
        throw new Error("Move must be straight or diagonal");
    }

    const distance = Math.max(absRowDiff, absColDiff);

    if (distance !== 1 && distance !== 2) {
        throw new Error("Move distance must be 1 or 2");
    }

    let direction: Direction;

    if (rowDiff < 0 && colDiff === 0) direction = Direction.UP;
    else if (rowDiff > 0 && colDiff === 0) direction = Direction.DOWN;
    else if (rowDiff === 0 && colDiff < 0) direction = Direction.LEFT;
    else if (rowDiff === 0 && colDiff > 0) direction = Direction.RIGHT;
    else if (rowDiff < 0 && colDiff < 0) direction = Direction.UP_LEFT;
    else if (rowDiff < 0 && colDiff > 0) direction = Direction.UP_RIGHT;
    else if (rowDiff > 0 && colDiff < 0) direction = Direction.DOWN_LEFT;
    else if (rowDiff > 0 && colDiff > 0) direction = Direction.DOWN_RIGHT;
    else throw new Error("Invalid move");

    return {
        boardId,
        start,
        distance,
        direction,
    };
}

export function isOwnBoard(boardId: BoardId, sideToMove: PlayerColor): boolean {
    return boardId.startsWith(sideToMove);
}

export function isAggressiveMove(turnPhase: TurnPhase):boolean{
    console.log(turnPhase)
    return turnPhase.includes("AGGRESSIVE")
}

export function getSideToMove(turnPhase: TurnPhase):PlayerColor{
   return turnPhase.startsWith("WHITE") ? "WHITE": "BLACK"

}
