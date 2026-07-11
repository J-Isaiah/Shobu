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

function sameMove(a: Move | null | undefined, b: Move | null | undefined): boolean {
    if (!a || !b) return false;

    return (
        a.boardId === b.boardId &&
        a.distance === b.distance &&
        a.direction === b.direction &&
        a.start.row === b.start.row &&
        a.start.col === b.start.col
    );
}

export function useMoveController({gameState, makeMove}: useMoveControllerParams) {
    let playerColor: string | null;

    const gameInfo = localStorage.getItem(`game:${gameState?.gameId}`);
    if (gameInfo != null) {
        playerColor = JSON.parse(gameInfo).playerColor
    }

    const [uiError, setUiError] = useState<string | null>(null);
    const [firstSelection, setFirstSelection] = useState<CellSelection | null>(null);

    const [optimisticPassiveArrow, setOptimisticPassiveArrow] = useState<Move | null>(null);
    const [optimisticAggressiveArrow, setOptimisticAggressiveArrow] = useState<Move | null>(null);

    const [dismissedLastAggressiveArrow, setDismissedLastAggressiveArrow] = useState<Move | null>(null);

    const shouldHideArrows =
        firstSelection && gameState && !isAggressiveMove(gameState.turnPhase);

    const passiveArrow = shouldHideArrows
        ? null
        : optimisticPassiveArrow ?? gameState?.pendingPassiveMove ?? null;

    const serverAggressiveArrow =
        sameMove(dismissedLastAggressiveArrow, gameState?.lastAggressiveMove)
            ? null
            : gameState?.lastAggressiveMove ?? null;

    const aggressiveArrow = shouldHideArrows
        ? null
        : optimisticAggressiveArrow ?? serverAggressiveArrow;

    const currentGameState = gameState;

    function resetClick() {
        setFirstSelection(null);
    }

    useEffect(() => {
        if (!gameState) {
            setOptimisticPassiveArrow(null);
            setOptimisticAggressiveArrow(null);
            setDismissedLastAggressiveArrow(null);
            return;
        }

        setOptimisticPassiveArrow(null);
        setOptimisticAggressiveArrow(null);

        if (
            dismissedLastAggressiveArrow &&
            !sameMove(dismissedLastAggressiveArrow, gameState.lastAggressiveMove)
        ) {
            setDismissedLastAggressiveArrow(null);
        }
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
                setDismissedLastAggressiveArrow(currentGameState.lastAggressiveMove ?? null);
                setOptimisticPassiveArrow(move);
                setOptimisticAggressiveArrow(null);
            }

            await makeMove({move});

            setFirstSelection(null);
            return;
        }

        if (!isAggressiveMove(currentGameState.turnPhase)) {
            setDismissedLastAggressiveArrow(currentGameState.lastAggressiveMove ?? null);
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