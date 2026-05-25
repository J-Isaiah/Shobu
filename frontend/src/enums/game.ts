export const BoardId = {
    BLACK_LIGHT: "BLACK_LIGHT",
    BLACK_DARK: "BLACK_DARK",
    WHITE_LIGHT: "WHITE_LIGHT",
    WHITE_DARK: "WHITE_DARK",
} as const;

export type BoardId = typeof BoardId[keyof typeof BoardId];


export const Direction = {
    UP: { row: -1, col: 0 },
    DOWN: { row: 1, col: 0 },

    LEFT: { row: 0, col: -1 },
    RIGHT: { row: 0, col: 1 },

    UP_LEFT: { row: -1, col: -1 },
    UP_RIGHT: { row: -1, col: 1 },

    DOWN_LEFT: { row: 1, col: -1 },
    DOWN_RIGHT: { row: 1, col: 1 },
} as const;

export type Direction =
    typeof Direction[keyof typeof Direction];