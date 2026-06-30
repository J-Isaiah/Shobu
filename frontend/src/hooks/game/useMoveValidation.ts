import {useEffect, useState} from "react";
import type {CellSelection} from "../../types/game/Cell.ts";
import {BoardId} from "../../enums/game.ts";
import type {BoardCoordinate, GameState, Move, Position} from "../../types/game/MoveTypes.ts";
import {buildMove, getSideToMove, isAggressiveMove, isOwnBoard} from "../../utils/game/movePhase.ts";
import {canSelectStone} from "../../utils/game/canSelectStone.ts";


interface useMoveControllerParams {
    gameState: GameState | null
    makeMove: ({move}: { move: Move }) => void;
}

export function useMoveController({gameState, makeMove,}: useMoveControllerParams) {
    const playerColor = localStorage.getItem("playerColor")

    const [uiError, setUiError] = useState<string | null>(null)
    const [firstSelection, setFirstSelection] = useState<CellSelection | null>(null)
    const [passiveArrow, setPassiveArrow] = useState<Move | null>(null);
    const [aggressiveArrow, setAggressiveArrow] = useState<Move | null>(null);

    // Handle passive arrow going away after aggressive rerender
    useEffect(() => {
        if (firstSelection != null){
            useEffect(() => {
                setPassiveArrow(null);
                setAggressiveArrow(null);
            }, [gameState?.turnPhase]);
        }
    }, [getSideToMove(gameState?.turnPhase)]);

    const currentGameState = gameState;

    function resetClick() {
        console.log("trying to reset")
        setFirstSelection(null)
    }


    async function handleCellClick(
        boardId: BoardId,
        row: BoardCoordinate,
        col: BoardCoordinate
    ): Promise<void> {
        setUiError(null);

        const clickedPosition: Position = {row, col};

        if (!currentGameState) return

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
        if (
            firstSelection &&
            canSelectStone(
                currentGameState,
                playerColor,
                boardId,
                row,
                col
            )
        ) {
            setFirstSelection(cellSelection);
            return;
        }

        if (firstSelection) {
            if (firstSelection.boardId !== cellSelection.boardId) {
                setUiError("Second selection should be on first selection board");
                setFirstSelection(null)
                return;
            }

            const move = buildMove(
                firstSelection.boardId,
                firstSelection.position,
                cellSelection.position
            );
            if (isAggressiveMove(currentGameState.turnPhase)) {
                setAggressiveArrow(move);
            } else {
                setPassiveArrow(move);
            }
            await makeMove({move});

            setFirstSelection(null);
            return;
        }

        setFirstSelection(cellSelection);
    }

    return {
        uiError,
        setUiError,
        firstSelection,
        passiveArrow,
        aggressiveArrow,
        handleCellClick,
        resetClick,
    };}

