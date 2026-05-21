import Stone from  "./Stone";
import "./cell.css"
import "./stone.css"
export default function Cell({ stone }: { stone?: "black" | "white" }) {
    return (
        <button className="cell">
            {stone && <Stone color={stone} />}
        </button>
    );
}