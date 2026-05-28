import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import type {
    BoardCoordinate,
    GameState,
    MakeMoveRequest,
    Move,
    MovePhase,
    PlayerColor,
    Position,
    SelectedCell,
} from "../types/game/MoveTypes.ts";
import { BoardId, Direction } from "../enums/game.ts";

function buildMove(
    boardId: BoardId,
    start: Position,
    end: Position
): Move {
    const rowDiff = end.row - start.row;
    const colDiff = end.col - start.col;

    const absRowDiff = Math.abs(rowDiff);
    const absColDiff = Math.abs(colDiff);

    const isStraight = absRowDiff === 0 || absColDiff === 0;
    const isDiagonal = absRowDiff === absColDiff;

    if (!isStraight && !isDiagonal) {
        throw new Error("Move must be straight or diagonal");
    }

    const distance = Math.max(absRowDiff, absColDiff);

    if (distance !== 1 && distance !== 2) {
        throw new Error("Move distance must be 1 or 2");
    }

    let direction: Direction;

    if (rowDiff < 0 && colDiff === 0) direction = Direction.UP;
    else if (rowDiff > 0 && colDiff === 0) direction = Direction.DOWN;
    else if (rowDiff === 0 && colDiff < 0) direction = Direction.LEFT;
    else if (rowDiff === 0 && colDiff > 0) direction = Direction.RIGHT;
    else if (rowDiff < 0 && colDiff < 0) direction = Direction.UP_LEFT;
    else if (rowDiff < 0 && colDiff > 0) direction = Direction.UP_RIGHT;
    else if (rowDiff > 0 && colDiff < 0) direction = Direction.DOWN_LEFT;
    else if (rowDiff > 0 && colDiff > 0) direction = Direction.DOWN_RIGHT;
    else throw new Error("Invalid move");

    return {
        boardId,
        start,
        distance,
        direction,
    };
}

function isOwnBoard(boardId: BoardId, sideToMove: PlayerColor): boolean {
    return boardId.startsWith(sideToMove);
}

export default function GameWindow() {
    const [phase, setPhase] = useState<MovePhase>("PASSIVE_START");
    const [selectedStart, setSelectedStart] = useState<SelectedCell | null>(null);
    const [passiveMove, setPassiveMove] = useState<Move | null>(null);
    const [gameState, setGameState] = useState<GameState | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const { gameId } = useParams();

    const getGameState = async (): Promise<void> => {
        const response = await fetch(`/api/game/${gameId}/getGameState`);

        if (!response.ok) {
            setErrorMessage(`Failed to load game: ${response.status}`);
            return;
        }

        setGameState(await response.json());
    };

    useEffect(() => {
        void getGameState();
    }, [gameId]);

    const makeMove = async ({
                                passiveMove,
                                aggressiveMove,
                            }: {
        passiveMove: Move;
        aggressiveMove: Move;
    }): Promise<void> => {
        const payload: MakeMoveRequest = {
            userId: "e4526351-ff88-4655-b534-c40de2d294b9",
            turn: {
                passiveMove,
                aggressiveMove,
            },
        };
        console.log("payload", payload);

        const response = await fetch(`/api/game/${gameId}/makeMove`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const errorBody = await response.json();
            setErrorMessage(`Move failed: ${errorBody.message}`);
            console.log("Error", errorBody.message)
            setPassiveMove(null);
            setSelectedStart(null);
            setPhase("PASSIVE_START");
            return;
        }

        setGameState(await response.json());
        setPassiveMove(null);
        setSelectedStart(null);
        setPhase("PASSIVE_START");
        setErrorMessage(null);
    };

    function handleCellClick(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): void {
        setErrorMessage(null);

        const clickedPosition: Position = { row, col };

        if (phase === "PASSIVE_START") {
            if (!isOwnBoard(boardId, gameState!.sideToMove)) {
                setErrorMessage("Passive move must start on your own board.");
                return;
            }

            setSelectedStart({ boardId, row, col });
            setPhase("PASSIVE_END");
            return;
        }

        if (phase === "PASSIVE_END" && selectedStart) {
            try {
                const move = buildMove(
                    selectedStart.boardId,
                    { row: selectedStart.row, col: selectedStart.col },
                    clickedPosition
                );

                setPassiveMove(move);
                setSelectedStart(null);
                setPhase("AGGRESSIVE_START");
            } catch (error) {
                setErrorMessage(error instanceof Error ? error.message : "Invalid passive move.");
            }

            return;
        }

        if (phase === "AGGRESSIVE_START") {
            setSelectedStart({ boardId, row, col });
            setPhase("AGGRESSIVE_END");
            return;
        }

        if (phase === "AGGRESSIVE_END" && selectedStart && passiveMove) {
            try {
                const aggressiveMove = buildMove(
                    selectedStart.boardId,
                    { row: selectedStart.row, col: selectedStart.col },
                    clickedPosition
                );

                void makeMove({
                    passiveMove,
                    aggressiveMove,
                });
            } catch (error) {
                setErrorMessage(error instanceof Error ? error.message : "Invalid aggressive move.");
            }
        }
    }

    if (gameState === null) {
        return <div>Loading...</div>;
    }

    return (
        <div className="gameSpace">
            {errorMessage && <div className="errorMessage">{errorMessage}</div>}
            {gameState.sideToMove && <div className="errorMessage">{gameState.sideToMove}</div>}

            <div className="blackSide">
                <Board
                    color="dark"
                    board={gameState.updatedGameBoards.BLACK_DARK}
                    boardId={BoardId.BLACK_DARK}
                    onCellClick={handleCellClick}
                />

                <Board
                    color="light"
                    board={gameState.updatedGameBoards.BLACK_LIGHT}
                    boardId={BoardId.BLACK_LIGHT}
                    onCellClick={handleCellClick}
                />
            </div>

            <div className="whiteSide">
                <Board
                    color="light"
                    board={gameState.updatedGameBoards.WHITE_LIGHT}
                    boardId={BoardId.WHITE_LIGHT}
                    onCellClick={handleCellClick}
                />

                <Board
                    color="dark"
                    board={gameState.updatedGameBoards.WHITE_DARK}
                    boardId={BoardId.WHITE_DARK}
                    onCellClick={handleCellClick}
                />
            </div>
        </div>
    );
}