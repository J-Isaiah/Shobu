import type {BoardId, Direction} from "../../enums/game.ts";

export type Stone = "WHITE" | "BLACK" | null;
export interface Turn{
    passiveMove: Move;
    aggroMove: Move;
}
export interface Position {
    row: 0|1|2|3;
    col: 0|1|2|3;
}
export interface Move{
    boardId: BoardId;
    start: Position;
    distance: 1|2;
    direction: Direction;
}

export interface GameState{
    gameId: string;
    sideToMove: "WHITE" | "BLACK";
    updatedGameBoards: Record<BoardId, Board>;
    winner: "WHITE" | "BLACK";
}

export interface Board{
    grid: Stone[][]
}
