package com.shobu.domain;

import com.shobu.GenerateLegalMoves;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Direction;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.moveData.LegalMove;
import com.shobu.domain.moveData.Move;
import com.shobu.domain.moveData.PassiveMoveInformation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateLegalMovesTest {

     @Test
     void generateLegalMoves_whenWhiteStarts_hasExpectedPassiveMoves() {
          Game game = Game.start(Stone.WHITE);
          GenerateLegalMoves generator = new GenerateLegalMoves(game);

          List<Move> passiveMoves = generator.generateLegalMoves().stream()
                  .map(LegalMove::passiveMove)
                  .toList();

          assertEquals(36, passiveMoves.size());

          assertTrue(passiveMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 0), 1, Direction.UP)));
          assertTrue(passiveMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 0), 2, Direction.UP)));
          assertTrue(passiveMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 1), 1, Direction.UP_LEFT)));
          assertTrue(passiveMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 2), 1, Direction.UP_RIGHT)));

          assertFalse(passiveMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 0), 1, Direction.DOWN)));
          assertFalse(passiveMoves.contains(new Move(BoardId.BLACK_LIGHT, new Position(0, 0), 1, Direction.DOWN)));
     }
     @Test
     void generateLegalMoves_whenWhiteMovesPassiveForwardOne_hasEightAggressiveMoves() {
          Game game = Game.start(Stone.WHITE);
          GenerateLegalMoves generator = new GenerateLegalMoves(game);

          Move passiveMove = new Move(
                  BoardId.WHITE_LIGHT,
                  new Position(3, 0),
                  1,
                  Direction.UP
          );

          LegalMove legalMove = generator.generateLegalMoves().stream()
                  .filter(option -> option.passiveMove().equals(passiveMove))
                  .findFirst()
                  .orElseThrow();

          List<Move> aggressiveMoves = legalMove.aggressiveMoves();

          assertEquals(8, aggressiveMoves.size());

          assertTrue(aggressiveMoves.contains(new Move(BoardId.WHITE_DARK, new Position(3, 0), 1, Direction.UP)));
          assertTrue(aggressiveMoves.contains(new Move(BoardId.WHITE_DARK, new Position(3, 1), 1, Direction.UP)));
          assertTrue(aggressiveMoves.contains(new Move(BoardId.WHITE_DARK, new Position(3, 2), 1, Direction.UP)));
          assertTrue(aggressiveMoves.contains(new Move(BoardId.WHITE_DARK, new Position(3, 3), 1, Direction.UP)));

          assertTrue(aggressiveMoves.contains(new Move(BoardId.BLACK_DARK, new Position(3, 0), 1, Direction.UP)));
          assertTrue(aggressiveMoves.contains(new Move(BoardId.BLACK_DARK, new Position(3, 1), 1, Direction.UP)));
          assertTrue(aggressiveMoves.contains(new Move(BoardId.BLACK_DARK, new Position(3, 2), 1, Direction.UP)));
          assertTrue(aggressiveMoves.contains(new Move(BoardId.BLACK_DARK, new Position(3, 3), 1, Direction.UP)));
     }


}
