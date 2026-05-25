import Stone from  "./Stone";
import "./cell.css"
import "./stone.css"
import type {StoneColor} from "../../types/game/MoveTypes.ts";
export default function Cell({ stone }: { stone: StoneColor}) {
    return (
        <button className="cell">
            {stone && <Stone color={stone} />}
        </button>
    );
}