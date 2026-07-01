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

export function useMoveController({gameState, makeMove}: useMoveControllerParams) {
    const playerColor = localStorage.getItem("playerColor");

    const [uiError, setUiError] = useState<string | null>(null);
    const [firstSelection, setFirstSelection] = useState<CellSelection | null>(null);

    const [optimisticPassiveArrow, setOptimisticPassiveArrow] = useState<Move | null>(null);
    const [optimisticAggressiveArrow, setOptimisticAggressiveArrow] = useState<Move | null>(null);

    const shouldHideArrows =
        firstSelection && gameState && !isAggressiveMove(gameState.turnPhase);

    const passiveArrow = shouldHideArrows
        ? null
        : optimisticPassiveArrow ?? gameState?.pendingPassiveMove ?? null;

    const aggressiveArrow = shouldHideArrows
        ? null
        : optimisticAggressiveArrow ?? gameState?.lastAggressiveMove ?? null;

    const currentGameState = gameState;

    function resetClick() {
        setFirstSelection(null);
    }

    useEffect(() => {
        if (!gameState) {
            setOptimisticPassiveArrow(null);
            setOptimisticAggressiveArrow(null);
            return;
        }

        setOptimisticPassiveArrow(null);
        setOptimisticAggressiveArrow(null);
    }, [gameState]);

    async function handleCellClick(
        boardId: BoardId,
        row: BoardCoordinate,
        col: BoardCoordinate
    ): Promise<void> {
        setUiError(null);

        const clickedPosition: Position = {row, col};

        if (!currentGameState) return;

        if (!isAggressiveMove(currentGameState.turnPhase)) {
            if (!isOwnBoard(boardId, getSideToMove(currentGameState.turnPhase))) {
                setUiError("Passive move must start on your own board.");
                setFirstSelection(null);
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
                setFirstSelection(null);
                return;
            }

            const move = buildMove(
                firstSelection.boardId,
                firstSelection.position,
                cellSelection.position
            );

            if (isAggressiveMove(currentGameState.turnPhase)) {
                setOptimisticAggressiveArrow(move);
            } else {
                setOptimisticPassiveArrow(move);
                setOptimisticAggressiveArrow(null);
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
    };
}