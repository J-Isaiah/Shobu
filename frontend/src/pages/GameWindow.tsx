import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css";
import {useParams} from "react-router-dom";
import {buildMove, getMoveEnd, getSideToMove, isAggressiveMove, isOwnBoard} from "../utils/game/movePhase.ts";
import {useState} from "react";
import type {BoardCoordinate, Position,} from "../types/game/MoveTypes.ts";
import {BoardId,} from "../enums/game.ts";
import type {CellSelection, PlayerMoves} from "../types/game/Cell.ts";
import {useGameConnection} from "../hooks/game/useGameConnection.ts";
import {useMoveHighlighting} from "../hooks/game/useMoveHighlighting.ts";


export default function GameWindow() {
    const {gameId} = useParams();

    const [uiError, setUiError] = useState<string | null>(null)
    const [playerMoves, setPlayerMoves] = useState<PlayerMoves | null>(null);
    const [firstSelection, setFirstSelection] = useState<CellSelection | null>(null);

    const {makeMove, gameState, isPendingMove, networkError} = useGameConnection(gameId)
    const {
        isMovableStone,
        isSelectedStone,
        isAvailableCellToMove
    } = useMoveHighlighting(setUiError, gameState, isPendingMove, firstSelection)


    if (gameState === null) {
        return <div>Loading game...</div>;
    }
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


    return (
        <div className="gameSpace">
            <div className="errorMessage">{uiError ? uiError : ""}</div>
            {<div className="errorMessage">{currentGameState.turnPhase}</div>}

            <div className="blackSide">
                <Board
                    color="dark"
                    board={gameState.updatedGameBoards.BLACK_DARK}
                    boardId={BoardId.BLACK_DARK}
                    onCellClick={handleCellClick}
                    isHighlightedStone={isMovableStone}
                    isHighlightedCell={isSelectedStone}
                    isAvailableCellToMove={isAvailableCellToMove}
                />

                <Board
                    color="light"
                    board={gameState.updatedGameBoards.BLACK_LIGHT}
                    boardId={BoardId.BLACK_LIGHT}
                    onCellClick={handleCellClick}

                    isAvailableCellToMove={isAvailableCellToMove}
                    isHighlightedCell={isSelectedStone}
                    isHighlightedStone={isMovableStone}
                />
            </div>

            <div className="whiteSide">
                <Board
                    color="light"
                    board={gameState.updatedGameBoards.WHITE_LIGHT}
                    boardId={BoardId.WHITE_LIGHT}
                    onCellClick={handleCellClick}

                    isAvailableCellToMove={isAvailableCellToMove}
                    isHighlightedCell={isSelectedStone}
                    isHighlightedStone={isMovableStone}
                />

                <Board
                    color="dark"
                    board={gameState.updatedGameBoards.WHITE_DARK}
                    boardId={BoardId.WHITE_DARK}
                    onCellClick={handleCellClick}
                    isHighlightedStone={isMovableStone}
                    isHighlightedCell={isSelectedStone}
                    isAvailableCellToMove={isAvailableCellToMove}
                />
            </div>
        </div>
    );
}