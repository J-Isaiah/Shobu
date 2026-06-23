import type {StoneColor} from "../../types/game/MoveTypes.ts";

export default function Stone({
                                  color,
                                  isHighlightedCell,
                              }: {
    color: StoneColor;
    isHighlightedCell: boolean;
}) {
    const src =
        color === "BLACK"
            ? "/black-stone.png"
            : "/white-stone.png";

    return (
        <img
            src={src}
            alt=""
            className={`${isHighlightedCell ? "highlighted-stone" : ""} stone`}
        />
    );
}