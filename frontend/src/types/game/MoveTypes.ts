import type {BoardId, Direction} from "../../enums/game.ts";

export type StoneColor = "WHITE" | "BLACK" | null;
export type PlayerColor = "WHITE" | "BLACK"
export interface MakeMoveRequest {
    userId: string;
    turn: Turn;
}
export interface Turn{
    passiveMove: Move;
    aggroMove: Move;
}
export interface Position {
    row:BoardCoordinate;
    col:BoardCoordinate;
}
export type BoardCoordinate = 0|1|2|3;
export type MoveDistances=1|2
export interface Move{
    boardId: BoardId;
    start: Position;
    distance: MoveDistances
    direction: Direction;
}

export interface GameState{
    gameId: string;
    sideToMove: "WHITE" | "BLACK";
    updatedGameBoards: Record<BoardId, Board>;
    winner: "WHITE" | "BLACK";
}

export interface Board{
    grid: StoneColor[][]
}

export type MovePhase =
    | "PASSIVE_START"
    | "PASSIVE_END"
    | "AGGRESSIVE_START"
    | "AGGRESSIVE_END";

export interface SelectedCell {
    boardId: BoardId;
    row: 0 | 1 | 2 | 3;
    col: 0 | 1 | 2 | 3;
}

export type OnCellClick = (
    boardId: BoardId,
    row: 0 | 1 | 2 | 3,
    col: 0 | 1 | 2 | 3
) => void;