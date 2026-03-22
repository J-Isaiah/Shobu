package com.shobu.domain;

public class Game {
    private Board board;
    private Stone sideToMove;

    private Board blackLight;
    private Board blackDark;
    private Board whiteLight;
    private Board whiteDark;

    public Game(Stone sideToMove) {
        this.sideToMove = sideToMove;
    }

    public static Game start() {

        return new Game(Stone.WHITE);
    }

    // TODO: Configure initial board setup and starting side
    public Boolean init() {
        this.blackLight = Board.inital();
        this.blackDark = Board.inital();

        this.whiteLight = Board.inital();
        this.whiteDark = Board.inital();

        return true;

    }

    // TODO: Validate move direction and distance
    // TODO: Implement stone push mechanic
    // TODO: Detect win conditions
    public Stone checkWin(Board board) {
        for 

    }

}
