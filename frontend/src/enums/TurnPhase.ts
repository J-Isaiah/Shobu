export const TurnPhase= {
    WHITE_PASSIVE:"WHITE_PASSIVE",
    WHITE_AGGRESSIVE: "WHITE_AGGRESSIVE",
    BLACK_PASSIVE:"BLACK_PASSIVE",
    BLACK_AGGRESSIVE : "BLACK_AGGRESSIVE",
    GAME_OVER : "GAME_OVER",
} as const;

export type Direction = typeof TurnPhase[keyof typeof TurnPhase];