import type {BoardId, Direction} from "../../enums/game.ts";

export type StoneColor = "WHITE" | "BLACK" | null;
export type PlayerColor = "WHITE" | "BLACK"

export interface MakeMoveRequest {
    userId: string;
    move: Move;
}

export interface Position {
    row: BoardCoordinate;
    col: BoardCoordinate;
}

export type BoardCoordinate = 0 | 1 | 2 | 3;
export type MoveDistances = 1 | 2

export interface Move {
    boardId: BoardId;
    start: Position;
    distance: MoveDistances
    direction: Direction;
}

export interface GameState {
    gameId: string;
    turnPhase: TurnPhase;
    updatedGameBoards: Record<BoardId, Board>;
    winner: "WHITE" | "BLACK" | null;
    legalMovesForPlayer: LegalMovesForPlayer;
    pendingPassiveMove: Move
    lastAggressiveMove: Move
}

export interface Board {
    grid: StoneColor[][]
}

export interface LegalMove {
    passiveMove: Move;
    aggressiveMoves: Move[];
}

export type LegalMovesForPlayer = Record<
    BoardId,
    Record<string, LegalMove[]>
>;
export type TurnPhase =
    | "WHITE_PASSIVE"
    | "WHITE_AGGRESSIVE"
    | "BLACK_PASSIVE"
    | "BLACK_AGGRESSIVE"
    | "GAME_OVER";

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