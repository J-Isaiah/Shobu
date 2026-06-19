package com.shobu.domain.enums;

public enum TurnPhase {
    WHITE_PASSIVE(Stone.WHITE),
    WHITE_AGGRESSIVE(Stone.WHITE),
    BLACK_PASSIVE(Stone.BLACK),
    BLACK_AGGRESSIVE(Stone.BLACK),
    GAME_OVER(null);

    private final Stone sideToMove;

    TurnPhase(Stone sideToMove) {
        this.sideToMove = sideToMove;
    }

    public boolean isAggressive(){
        return this == WHITE_AGGRESSIVE || this == BLACK_AGGRESSIVE;
    }


    public Stone getSideToMove() {
        return sideToMove;
    }

    public TurnPhase next() {
        return switch (this) {
            case WHITE_PASSIVE -> WHITE_AGGRESSIVE;
            case WHITE_AGGRESSIVE -> BLACK_PASSIVE;
            case BLACK_PASSIVE -> BLACK_AGGRESSIVE;
            case BLACK_AGGRESSIVE -> WHITE_PASSIVE;

            case GAME_OVER ->
                    throw new IllegalStateException("Game is over");
        };}

    public static TurnPhase passivePhaseFor(Stone stone) {
        return switch (stone) {
            case WHITE -> WHITE_PASSIVE;
            case BLACK -> BLACK_PASSIVE;
        };
    }
}