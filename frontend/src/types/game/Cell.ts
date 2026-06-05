import type {BoardId} from "../../enums/game.ts";
import type {BoardCoordinate, Move} from "./MoveTypes.ts";

export interface CellUiState {
    isSelectable: boolean;
    isSelected: boolean;
    displayLegalMoves: boolean;
}

export type CellSelection = {
    boardId: BoardId;
    position: {
        row: BoardCoordinate;
        col: BoardCoordinate;
    }

};

export type PlayerMoves
    = {
}