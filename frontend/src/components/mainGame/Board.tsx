import "./Board.css";
import type { BoardColor } from "../../types/game/board";
import Cell from "./Cell";

export default function Board({ color }: { color: BoardColor }) {
    return (
        <div className={`board board--${color}`}>
            {Array.from({ length: 16 }).map((_, index) => (
                <Cell
                    key={index}
                    stone="black"
                />
            ))}
        </div>
    );
}