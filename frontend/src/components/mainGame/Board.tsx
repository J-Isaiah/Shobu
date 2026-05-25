import "./Board.css";
import type { BoardColor } from "../../types/game/board.ts";
import Cell from "./Cell";
import type {Board} from "../../types/game/MoveTypes.ts";

export default function Board({ color, board }: { color: BoardColor, board: Board }) {
    console.log("Board:", color, board);
    console.log("Grid:", board.grid);

    return (
        <div className={`board board--${color}`}>
            {board.grid.map((row, rowIndex) =>
                row.map((stone, colIndex) => (
                    <Cell
                        key={`${rowIndex}-${colIndex}`}
                        stone={stone}
                    />
                ))
            )}
        </div>
    );
}