import type {TurnPhase} from "../../types/game/MoveTypes.ts";

export function getGameStateMessage(

    turnPhase: TurnPhase,
    winner?: "WHITE" | "BLACK" | null,
): string {
    if (winner) {
        return `${winner} wins!`;
    }

    switch (turnPhase) {
        case "WHITE_PASSIVE":
            return "White's Passive Move";

        case "WHITE_AGGRESSIVE":
            return "White's Aggressive Move";

        case "BLACK_PASSIVE":
            return "Black's Passive Move";

        case "BLACK_AGGRESSIVE":
            return "Black's Aggressive Move";

        default:
            return "Unknown Game State";
    }
}