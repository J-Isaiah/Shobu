import Stone from  "./Stone";
import "./cell.css"
import "./stone.css"
import type {Board, OnCellClick, Position, StoneColor} from "../../types/game/MoveTypes.ts";
export default function Cell({ stone, onClick, boardId, position }: { boardId: BoardId, stone: StoneColor, onClick: OnCellClick , position: Position}) {
    return (
        <button className="cell" onClick={()=>onClick(boardId,position.row, position.col)}>
            {stone && <Stone color={stone} />}
        </button>
    );
}