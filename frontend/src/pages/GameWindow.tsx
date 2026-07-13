import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css";
import {useParams} from "react-router-dom";
import {BoardId,} from "../enums/game.ts";
import {useGameConnection} from "../hooks/game/useGameConnection.ts";
import useMoveHighlighting from "../hooks/game/useMoveHighlighting.ts";
import {useMoveController} from "../hooks/game/useMoveValidation.ts";
import {getGameStateMessage} from "../utils/game/cleanMoveDialog.ts";
import {useEffect, useState,} from "react";
import {getStoredAuthUser} from "../utils/auth/auth.ts";
import {joinGame} from "../api/game.ts";
import GameOver from "../components/gameOverScreen/GameOver.tsx";


export default function GameWindow() {
    const [uiErrorMessage, setUiErrorMessage] = useState<string>("")
    const {gameId} = useParams();

    useEffect(() => {
        async function autoJoin() {
            if (!gameId) return;

            const existingGameInfo = localStorage.getItem(`game:${gameId}`);

            if (existingGameInfo) {
                return;
            }

            try {
                await joinGame({
                    gameId,
                    authUser: getStoredAuthUser(),
                });
            } catch (err) {
                const message =
                    err instanceof Error
                        ? err.message
                        : "Unable to join this game.";

                setUiErrorMessage(message);
                console.error(err);
            }
        }

        autoJoin();
    }, [gameId]);

    if (!gameId) {
        throw new Error("Missing gameId");
    }

    let playerColor;

    const gameInfo = localStorage.getItem(`game:${gameId}`);

    if (gameInfo != null) {
        playerColor = JSON.parse(gameInfo).playerColor
    }

    const {
        makeMove,
        gameState,
        networkError,
        isPendingMove
    } = useGameConnection(gameId)

    const {
        // uiError,
        firstSelection,
        handleCellClick,
        resetClick,
        passiveArrow,
        aggressiveArrow
    } = useMoveController({
        gameState,
        makeMove,
    });

    const {
        isMovableStone,
        isSelectedStone,
        isAvailableCellToMove
    } = useMoveHighlighting(
        gameState,
        firstSelection,
        isPendingMove
    )

    const topDark =
        playerColor == "WHITE"
            ? BoardId.BLACK_DARK
            : BoardId.WHITE_DARK;

    const topLight =
        playerColor == "WHITE"
            ? BoardId.BLACK_LIGHT
            : BoardId.WHITE_LIGHT;

    const bottomLight =
        playerColor == "WHITE"
            ? BoardId.WHITE_LIGHT
            : BoardId.BLACK_LIGHT;

    const bottomDark =
        playerColor == "WHITE"
            ? BoardId.WHITE_DARK
            : BoardId.BLACK_DARK;

    if (uiErrorMessage) {
        return (
            <div className="game-space">
                <div className="game-error-card wood-pattern">
                    <h2>Unable to join game</h2>
                    <p>{uiErrorMessage}</p>
                </div>
            </div>
        );
    }

    if (gameState === null) {
        return (
            <div className="game-space">
                <div className="game-error-card wood-pattern">
                    <h2>Loading game</h2>
                    <p>Loading.........</p>
                </div>
            </div>
        );
    }

    return (
        <div className="game-space" onClick={resetClick}>
            {gameState.winner && <GameOver gameWinner={gameState.winner} gameState={gameState}></GameOver>}
            <div className="game-id-dialog wood-pattern">
                <div className="text-dialog">
                    Invite Code: {gameId}
                </div>
            </div>

            {networkError && (
                <div
                    className="game-error-message"
                    role="alert"
                >
                    {networkError}
                </div>
            )}

            <div
                className="boards"
                onClick={(event) => {
                    event.stopPropagation()
                }}
            >
                <Board
                    boardOwner={"opponent"}
                    aggressiveArrow={aggressiveArrow}
                    passiveArrow={passiveArrow}
                    color="dark"
                    board={gameState.updatedGameBoards[topDark]}
                    boardId={topDark}
                    onCellClick={handleCellClick}
                    isHighlightedStone={isMovableStone}
                    isHighlightedCell={isSelectedStone}
                    isAvailableCellToMove={isAvailableCellToMove}
                    gameId={gameId}
                />

                <Board

                    boardOwner={"opponent"}
                    aggressiveArrow={aggressiveArrow}
                    passiveArrow={passiveArrow}
                    color="light"
                    board={gameState.updatedGameBoards[topLight]}
                    boardId={topLight}
                    onCellClick={handleCellClick}
                    isAvailableCellToMove={isAvailableCellToMove}
                    isHighlightedCell={isSelectedStone}
                    isHighlightedStone={isMovableStone}
                    gameId={gameId}
                />

                <Board
                    aggressiveArrow={aggressiveArrow}

                    boardOwner={"player"}
                    passiveArrow={passiveArrow}
                    color="light"
                    board={gameState.updatedGameBoards[bottomLight]}
                    boardId={bottomLight}
                    onCellClick={handleCellClick}
                    isAvailableCellToMove={isAvailableCellToMove}
                    isHighlightedCell={isSelectedStone}
                    isHighlightedStone={isMovableStone}
                    gameId={gameId}
                />

                <Board

                    boardOwner={"player"}
                    aggressiveArrow={aggressiveArrow}
                    passiveArrow={passiveArrow}
                    color="dark"
                    board={gameState.updatedGameBoards[bottomDark]}
                    boardId={bottomDark}
                    onCellClick={handleCellClick}
                    isHighlightedStone={isMovableStone}
                    isHighlightedCell={isSelectedStone}
                    isAvailableCellToMove={isAvailableCellToMove}
                    gameId={gameId}
                />
            </div>

            <div className="game-state-dialog wood-pattern">
                <div className="text-dialog">
                    {getGameStateMessage(
                        gameState.turnPhase,
                        gameState.winner
                    )}
                </div>
            </div>
        </div>
    );
}