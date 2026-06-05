import Stone from "./Stone";
import "./cell.css"
import "./stone.css"
import type {OnCellClick, Position, StoneColor} from "../../types/game/MoveTypes.ts";
import type {BoardId} from "../../enums/game.ts";

export default function Cell({stone, onClick, boardId, position, isHighlightedStone}: {
    boardId: BoardId,
    stone: StoneColor,
    onClick: OnCellClick,
    position: Position
    isHighlightedStone: boolean
}) {

    return (
        <button className={` ${isHighlightedStone ? "highlighted" : "cell"}`} onClick={() => onClick(boardId, position.row, position.col)}>
            {stone && <Stone color={stone}/>}
        </button>
    );
}