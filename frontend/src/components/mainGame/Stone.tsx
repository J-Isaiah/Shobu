import "./stone.css"
import type {StoneColor} from "../../types/game/MoveTypes.ts";


export default function Stone({ color }: {color:StoneColor}) {
    return <div className={`stone stone--${color}`} />;
}