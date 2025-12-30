package com.shobu.domain;

public final class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        if (row < 0 || row >=4){
            throw new IllegalArgumentException("Row Out of Bounds");
        }
        if (col<0 || col>=4){
            throw new IllegalArgumentException("Col Out of Bounds");
        }

        this.row = row;
        this.col = col;


    }
    @Override
    public boolean equals(Object o){
    if (this == o) return true;
    return row == o.row && col == o.col;
    }
}