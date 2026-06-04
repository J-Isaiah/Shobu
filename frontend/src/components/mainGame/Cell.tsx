import Stone from "./Stone";
import "./cell.css"
import "./stone.css"
import type {OnCellClick, Position, StoneColor} from "../../types/game/MoveTypes.ts";
import type {BoardId} from "../../enums/game.ts";

export default function Cell({stone, onClick, boardId, position, isHighlighted}: {
    boardId: BoardId,
    stone: StoneColor,
    onClick: OnCellClick,
    position: Position
    isHighlighted: boolean
}) {

    console.log("isHighlighted", isHighlighted);
    return (
        <button className={` ${isHighlighted ? "highlighted" : "cell"}`} onClick={() => onClick(boardId, position.row, position.col)}>
            {stone && <Stone color={stone}/>}
        </button>
    );
}