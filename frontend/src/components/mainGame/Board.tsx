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

export default function Board({color, board, boardId, onCellClick, isHighlightedStone, isHighlightedCell,isAvailableCellToMove }: {
    color: BoardColor,
    board: Board,
    boardId: BoardId,
    onCellClick: OnCellClick,
    isHighlightedStone: (boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate) => boolean;
    isHighlightedCell:(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate) => boolean
    isAvailableCellToMove : (boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate) => boolean
}) {

    const playerColor = localStorage.getItem("playerColor")
    const shouldRotate = playerColor === "BLACK";

    const rowIndexes = shouldRotate ? [3, 2, 1, 0] : [0, 1, 2, 3];
    const colIndexes = shouldRotate ? [3, 2, 1, 0] : [0, 1, 2, 3];
    return (
        <div className={`board board--${color} board-pattern`}>
            {rowIndexes.map((rowIndex) =>
                colIndexes.map((colIndex) => {
                    const rowCoord = toBoardCoordinate(rowIndex);
                    const colCoord = toBoardCoordinate(colIndex);

                    return (
                        <Cell
                            key={`${rowIndex}-${colIndex}`}
                            stone={board.grid[rowIndex][colIndex]}
                            onClick={onCellClick}
                            boardId={boardId}
                            position={{ row: rowCoord, col: colCoord }}
                            isHighlightedStone={isHighlightedStone(boardId, rowCoord, colCoord)}
                            isHighlightedCell={isHighlightedCell(boardId, rowCoord, colCoord)}
                            isAvailableCellToMove={isAvailableCellToMove(boardId, rowCoord, colCoord)}
                            isRotated={shouldRotate}
                        />
                    );
                })
            )}
        </div>
    );
}