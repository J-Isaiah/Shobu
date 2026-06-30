import "./MoveArrow.css";
import type {Point} from "../../hooks/useBoardMeasurements.ts";

type MoveArrowProps = {
    to: Point
    from: Point,
    label?: "passive" | "aggressive";
};

export default function MoveArrow({to, from, label}: MoveArrowProps) {
    return (
        <svg className="move-arrow-overlay">
            <defs>
                <marker
                    id={`arrow-head-${label ?? "default"}`}
                    markerWidth="12"
                    markerHeight="12"
                    refX="10"
                    refY="6"
                    orient="auto"
                    markerUnits="strokeWidth"
                >
                    <path d="M2,2 L10,6 L2,10 Z" className={`arrow-head ${label ?? ""}`}/>
                </marker>
            </defs>

            <line
                x1={from.x}
                y1={from.y}
                x2={to.x}
                y2={to.y}
                className={`move-arrow ${label ?? ""}`}
                markerEnd={`url(#arrow-head-${label ?? "default"})`}
            />
        </svg>
    );
}