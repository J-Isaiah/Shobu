import "./stone.css"
import type {StoneColor} from "../../types/game/MoveTypes.ts";


export default function Stone({ color,isHighlightedCell}: {color:StoneColor, isHighlightedCell: boolean}) {
    return <div className={` ${isHighlightedCell?  "highlighted-stone":  ""} stone stone--${color}`} />;
}