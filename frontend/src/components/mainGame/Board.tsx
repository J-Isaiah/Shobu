import "./Board.css";
import type {BoardColor} from "../../types/game/board.ts";
import Cell from "./Cell";
import type {Board, BoardCoordinate, OnCellClick} from "../../types/game/MoveTypes.ts";
import type {BoardId} from "../../enums/game.ts";

function toBoardCoordinate(value: number): BoardCoordinate {
    if (value < 0 || value > 3) {
        throw new Error("Invalid board coordinate");
    }

    return value as BoardCoordinate;
}

export default function Board({color, board, boardId, onCellClick, isHighlighted}: {
    color: BoardColor,
    board: Board,
    boardId: BoardId,
    onCellClick: OnCellClick,
    isHighlighted: (boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate) => boolean;
}) {

    return (
        <div className={`board board--${color}`}>
            {board.grid.map((row, rowIndex) =>
                row.map((stone, colIndex) => (
                    <Cell
                        key={`${rowIndex}-${colIndex}`}
                        stone={stone}
                        onClick={onCellClick}
                        boardId={boardId}
                        position={{row: toBoardCoordinate(rowIndex), col: toBoardCoordinate(colIndex)}}
                        isHighlighted={isHighlighted(boardId, toBoardCoordinate(rowIndex), toBoardCoordinate(colIndex))}
                    />
                ))
            )}
        </div>
    );
}