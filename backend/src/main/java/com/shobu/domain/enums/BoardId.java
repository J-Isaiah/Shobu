package com.shobu.domain.enums;

public enum BoardId {
    BLACK_LIGHT,
    BLACK_DARK,
    WHITE_LIGHT,
    WHITE_DARK;

    public BoardShade getShade() {
        return switch (this) {
            case BLACK_LIGHT, WHITE_LIGHT -> BoardShade.LIGHT;
            case BLACK_DARK, WHITE_DARK -> BoardShade.DARK;

        };
    }

    public Stone getBoardOwner() {
        return switch (this) {
            case WHITE_DARK, WHITE_LIGHT -> Stone.WHITE;
            case BLACK_DARK, BLACK_LIGHT -> Stone.BLACK;
        };

    }
}