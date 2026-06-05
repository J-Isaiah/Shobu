
export const BoardId = {
    BLACK_LIGHT: "BLACK_LIGHT",
    BLACK_DARK: "BLACK_DARK",
    WHITE_LIGHT: "WHITE_LIGHT",
    WHITE_DARK: "WHITE_DARK",
} as const;

export type BoardId = typeof BoardId[keyof typeof BoardId];



export const Direction = {
    UP: "UP",
    DOWN: "DOWN",

    LEFT: "LEFT",
    RIGHT: "RIGHT",

    UP_LEFT: "UP_LEFT",
    UP_RIGHT: "UP_RIGHT",

    DOWN_LEFT: "DOWN_LEFT",
    DOWN_RIGHT:"DOWN_RIGHT",
} as const;

export type Direction =
    typeof Direction[keyof typeof Direction];

