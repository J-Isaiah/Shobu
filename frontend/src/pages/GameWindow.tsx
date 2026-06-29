import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css";
import {useParams} from "react-router-dom";
import {BoardId,} from "../enums/game.ts";
import {useGameConnection} from "../hooks/game/useGameConnection.ts";
import useMoveHighlighting from "../hooks/game/useMoveHighlighting.ts";
import {useMoveController} from "../hooks/game/useMoveValidation.ts";
import {getGameStateMessage} from "../utils/game/cleanMoveDialog.ts";


export default function GameWindow() {
    const {gameId} = useParams();
    const playerColor = localStorage.getItem("playerColor")


    const {makeMove, gameState, networkError, isPendingMove} = useGameConnection(gameId)


    const {
        // uiError,
        firstSelection,
        handleCellClick,
        resetClick
    } = useMoveController({
        gameState,
        makeMove,
    });
    const {
        isMovableStone,
        isSelectedStone,
        isAvailableCellToMove
    } = useMoveHighlighting(gameState, firstSelection, isPendingMove)


    if (gameState === null) {
        return <div>Loading game...</div>;
    }
    const topDark = playerColor == "WHITE" ? BoardId.BLACK_DARK : BoardId.WHITE_DARK;
    const topLight = playerColor == "WHITE" ? BoardId.BLACK_LIGHT : BoardId.WHITE_LIGHT;
    const bottomLight = playerColor == "WHITE" ? BoardId.WHITE_LIGHT : BoardId.BLACK_LIGHT;
    const bottomDark = playerColor == "WHITE" ? BoardId.WHITE_DARK : BoardId.BLACK_DARK;


    return (
        <div className="game-space" onClick={resetClick}>
            <div className="game-id-dialog wood-pattern">
                <div className="text-dialog">Invite Code: {gameId}</div>
            </div>
            {/*<div className="errorMessage">{uiError ? uiError : ""}</div>*/}

            <div className="errorMessage">{networkError ? networkError : ""}</div>
            <div className="boards" onClick={(event) => {
                event.stopPropagation()
            }}>
                <Board
                    color="dark"
                    board={gameState.updatedGameBoards[topDark]}
                    boardId={topDark}
                    onCellClick={handleCellClick}
                    isHighlightedStone={isMovableStone}
                    isHighlightedCell={isSelectedStone}
                    isAvailableCellToMove={isAvailableCellToMove}
                />

                <Board
                    color="light"
                    board={gameState.updatedGameBoards[topLight]}
                    boardId={topLight}
                    onCellClick={handleCellClick}

                    isAvailableCellToMove={isAvailableCellToMove}
                    isHighlightedCell={isSelectedStone}
                    isHighlightedStone={isMovableStone}
                />

                <Board
                    color="light"
                    board={gameState.updatedGameBoards[bottomLight]}
                    boardId={bottomLight}
                    onCellClick={handleCellClick}

                    isAvailableCellToMove={isAvailableCellToMove}
                    isHighlightedCell={isSelectedStone}
                    isHighlightedStone={isMovableStone}
                />

                <Board
                    color="dark"
                    board={gameState.updatedGameBoards[bottomDark]}
                    boardId={bottomDark}
                    onCellClick={handleCellClick}
                    isHighlightedStone={isMovableStone}
                    isHighlightedCell={isSelectedStone}
                    isAvailableCellToMove={isAvailableCellToMove}
                />

            </div>
            <div className="game-state-dialog wood-pattern">
                <div className="text-dialog">{getGameStateMessage(gameState.turnPhase, gameState.winner)}</div>
            </div>
        </div>
    );
}