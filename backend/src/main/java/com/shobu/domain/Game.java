package com.shobu.domain;

public class Game {
    private Board board;
    private Stone sideToMove;

    private Board blackLight;
    private Board blackDark;
    private Board whiteLight;
    private Board whiteDark;

    public Game(Board board, Stone sideToMove) {
        this.board = board;
        this.sideToMove = sideToMove;
    }

    public static Game start() {

        return new Game(Board.empty(), Stone.WHITE);
    }

    // TODO: Configure initial board setup and starting side
    public Boolean init() {

    }

    private Board initBoard(BoardType Color,Stone PlayerSide,){
        int SIZE = 4;
        Stone[][] 
        for (int r = 0; r < SIZE; r++){
            for (int c = 0; c < SIZE; c++){}

        }
    }


    // TODO: Validate move direction and distance
    // TODO: Implement stone push mechanic
    // TODO: Detect win conditions

}
