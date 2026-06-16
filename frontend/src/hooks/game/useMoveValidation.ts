import {useState} from "react";
import type {CellSelection, PlayerMoves} from "../../types/game/Cell.ts";
import {BoardId} from "../../enums/game.ts";
import type {BoardCoordinate, GameState, Move, Position} from "../../types/game/MoveTypes.ts";
import {buildMove, getSideToMove, isAggressiveMove, isOwnBoard} from "../../utils/game/movePhase.ts";
import * as React from "react";


interface useMoveControllerParams {
    gameState: GameState | null
    makeMove: ({move}: { move: Move }) => void;
}

export function useMoveController({gameState, makeMove,  }: useMoveControllerParams) {

    const [uiError, setUiError] = useState<string | null>(null)
    const [playerMoves, setPlayerMoves] = useState<PlayerMoves | null>(null);
    const [firstSelection, setFirstSelection] = useState<CellSelection | null>(null)


    const currentGameState = gameState;


    async function handleCellClick(
        boardId: BoardId,
        row: BoardCoordinate,
        col: BoardCoordinate
    ): Promise<void> {
        setUiError(null);

        const clickedPosition: Position = {row, col};

        if (!isAggressiveMove(currentGameState.turnPhase)) {
            if (!isOwnBoard(boardId, getSideToMove(currentGameState.turnPhase))) {
                setUiError("Passive move must start on your own board.");
                setFirstSelection(null)
                return;
            }
        }

        const cellSelection: CellSelection = {
            boardId,
            position: clickedPosition,
        };

        if (firstSelection) {
            if (firstSelection.boardId !== cellSelection.boardId) {
                setUiError("Second selection should be on first selection board");
                setPlayerMoves(null);
                setFirstSelection(null)
                return;
            }

            const move = buildMove(
                firstSelection.boardId,
                firstSelection.position,
                cellSelection.position
            );

            await makeMove({move});
            setFirstSelection(null);
            return;
        }

        setFirstSelection(cellSelection);
    }

    return {uiError,setUiError, firstSelection, handleCellClick}
}

