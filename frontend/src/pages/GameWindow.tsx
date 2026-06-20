import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css";
import {useParams} from "react-router-dom";
import {BoardId,} from "../enums/game.ts";
import {useGameConnection} from "../hooks/game/useGameConnection.ts";
import useMoveHighlighting from "../hooks/game/useMoveHighlighting.ts";
import {useMoveController} from "../hooks/game/useMoveValidation.ts";
import {canSelectStone} from "../utils/game/canSelectStone.ts";


export default function GameWindow() {
    const {gameId} = useParams();


    const {makeMove, gameState, networkError,isPendingMove} = useGameConnection(gameId)


    const {
        uiError,
        setUiError,
        firstSelection,
        handleCellClick,
    } = useMoveController({
        gameState,
        makeMove,
    });
    const {
        isMovableStone,
        isSelectedStone,
        isAvailableCellToMove
    } = useMoveHighlighting(setUiError, gameState, firstSelection,isPendingMove)


    if (gameState === null) {
        return <div>Loading game...</div>;
    }


    return (
        <div className="gameSpace">
            <div className="errorMessage">{uiError ? uiError : ""}</div>

            <div className="errorMessage">{networkError ? networkError : ""}</div>
            {<div className="errorMessage">{gameState.turnPhase}</div>}

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