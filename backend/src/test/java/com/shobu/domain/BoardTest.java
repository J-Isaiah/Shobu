package com.shobu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class BoardTest {
    @Test
    void initialBoard_hasCorrectSetup() {
        Board board = Board.inital();

        for (int c = 0; c < 4; c++) {
            assertEquals(Stone.WHITE, board.getStoneAt(new Position(0, c)));

            assertEquals(Stone.BLACK, board.getStoneAt(new Position(3, c)));
        }

    }

}
