package com.shobu.domain;

import com.shobu.GenerateLegalMoves;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Direction;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.moveData.Move;
import com.shobu.domain.moveData.MoveInformation;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GenerateLegalMovesTest {

     @Test
     void generateLegalPassiveMoves_whenWhiteStarts_hasExpectedMoves() {
          Game game = Game.start(Stone.WHITE);
          GenerateLegalMoves generator = new GenerateLegalMoves(game);

          var movesByBoard = generator.generateLegalPassiveMoves();

          List<Move> allMoves = movesByBoard.values().stream()
                  .flatMap(positionMap -> positionMap.values().stream())
                  .flatMap(List::stream)
                  .map(MoveInformation::move)
                  .toList();

          assertEquals(36, allMoves.size());

          assertTrue(allMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 0), 1, Direction.UP)));
          assertTrue(allMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 0), 2, Direction.UP)));
          assertTrue(allMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 1), 1, Direction.UP_LEFT)));
          assertTrue(allMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 2), 1, Direction.UP_RIGHT)));

          assertFalse(allMoves.contains(new Move(BoardId.WHITE_LIGHT, new Position(3, 0), 1, Direction.DOWN)));
          assertFalse(allMoves.contains(new Move(BoardId.BLACK_LIGHT, new Position(0, 0), 1, Direction.DOWN)));
     }



}
