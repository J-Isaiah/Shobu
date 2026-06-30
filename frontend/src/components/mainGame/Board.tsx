import "./Board.css";
import type { BoardColor } from "../../types/game/board.ts";
import Cell from "./Cell";
import type {
    Board,
    BoardCoordinate,
    Move,
    OnCellClick,
} from "../../types/game/MoveTypes.ts";
import type { BoardId } from "../../enums/game.ts";
import { useBoardMeasurements, getCellCenter } from "../../hooks/useBoardMeasurements.ts";
import MoveArrow from "./MoveArrow";
import { getMoveEnd } from "../../utils/game/movePhase"; // <-- wherever yours lives

function toBoardCoordinate(value: number): BoardCoordinate {
    if (value < 0 || value > 3) {
        throw new Error("Invalid board coordinate");
    }

    return value as BoardCoordinate;
}

type Props = {
    passiveArrow: Move | null;
    aggressiveArrow: Move | null;
    color: BoardColor;
    board: Board;
    boardId: BoardId;
    onCellClick: OnCellClick;
    isHighlightedStone: (
        boardId: BoardId,
        row: BoardCoordinate,
        col: BoardCoordinate
    ) => boolean;
    isHighlightedCell: (
        boardId: BoardId,
        row: BoardCoordinate,
        col: BoardCoordinate
    ) => boolean;
    isAvailableCellToMove: (
        boardId: BoardId,
        row: BoardCoordinate,
        col: BoardCoordinate
    ) => boolean;
};

export default function Board({
                                  color,
                                  board,
                                  boardId,
                                  onCellClick,
                                  isHighlightedStone,
                                  isHighlightedCell,
                                  isAvailableCellToMove,
                                  passiveArrow,
                                  aggressiveArrow,
                              }: Props) {
    const { boardRef, measurements } = useBoardMeasurements();

    const playerColor = localStorage.getItem("playerColor");
    const shouldRotate = playerColor === "BLACK";

    const rowIndexes = shouldRotate ? [3, 2, 1, 0] : [0, 1, 2, 3];
    const colIndexes = shouldRotate ? [3, 2, 1, 0] : [0, 1, 2, 3];

    return (
        <div
            ref={boardRef}
            className={`board board--${color} board-pattern`}
        >
            {passiveArrow?.boardId === boardId && (() => {
                const end = getMoveEnd(passiveArrow);

                return (
                    <MoveArrow
                        label="passive"
                        from={getCellCenter(
                            passiveArrow.start.row,
                            passiveArrow.start.col,
                            measurements.cellSize
                        )}
                        to={getCellCenter(
                            end.row,
                            end.col,
                            measurements.cellSize
                        )}
                    />
                );
            })()}

            {aggressiveArrow?.boardId === boardId && (() => {
                const end = getMoveEnd(aggressiveArrow);

                return (
                    <MoveArrow
                        label="aggressive"
                        from={getCellCenter(
                            aggressiveArrow.start.row,
                            aggressiveArrow.start.col,
                            measurements.cellSize
                        )}
                        to={getCellCenter(
                            end.row,
                            end.col,
                            measurements.cellSize
                        )}
                    />
                );
            })()}

            {rowIndexes.map((rowIndex) =>
                colIndexes.map((colIndex) => {
                    const rowCoord = toBoardCoordinate(rowIndex);
                    const colCoord = toBoardCoordinate(colIndex);

                    return (
                        <Cell
                            id={`${boardId}-${rowIndex}-${colIndex}`}
                            key={`${rowIndex}-${colIndex}`}
                            stone={board.grid[rowIndex][colIndex]}
                            onClick={onCellClick}
                            boardId={boardId}
                            position={{ row: rowCoord, col: colCoord }}
                            isHighlightedStone={isHighlightedStone(boardId, rowCoord, colCoord)}
                            isHighlightedCell={isHighlightedCell(boardId, rowCoord, colCoord)}
                            isAvailableCellToMove={isAvailableCellToMove(boardId, rowCoord, colCoord)}
                        />
                    );
                })
            )}
        </div>
    );
}