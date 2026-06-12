import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css";
import {useParams} from "react-router-dom";
import {buildMove, getMoveEnd, getSideToMove, isAggressiveMove, isOwnBoard} from "../utils/game/movePhase.ts";
import {useEffect, useState} from "react";
import type {BoardCoordinate, GameState, MakeMoveRequest, Move, Position,} from "../types/game/MoveTypes.ts";
import {BoardId,} from "../enums/game.ts";
import type {CellSelection, PlayerMoves} from "../types/game/Cell.ts";


export default function GameWindow() {
    const [gameState, setGameState] = useState<GameState | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [playerMoves, setPlayerMoves] = useState<PlayerMoves | null>(null);
    const [firstSelection, setFirstSelection] = useState<CellSelection | null>(null);


    const {gameId} = useParams();


    const getGameState = async (): Promise<void> => {
        const response = await fetch(`/api/game/${gameId}/getGameState`);

        if (!response.ok) {
            setErrorMessage(`Failed to load game: ${response.status}`);
            return;
        }

        const gameState: GameState = await response.json();
        setGameState(gameState);
    };

    useEffect(() => {
        void getGameState();
    }, [gameId]);

    if (gameState === null) {
        return <div>Loading game...</div>;
    }
    const currentGameState = gameState;

    const makeMove = async ({move}: { move: Move }): Promise<void> => {
        const payload: MakeMoveRequest = {
            userId: "e4526351-ff88-4655-b534-c40de2d294b9",
            move: move

        };

        const response = await fetch(`/api/game/${gameId}/makeMove`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const errorBody = await response.json();
            setErrorMessage(`Move failed: ${errorBody.message}`);
            return;
        }
        console.log(await response)
        const gameState = await response.json()

        setGameState(gameState);

        setErrorMessage(null);

    };


    function isMovableStone(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): boolean {
        // Highlights passive and aggressive stones

        if (firstSelection) {
            return false
        }

        if (gameState === null) {
            return false
        }
        if (!isAggressiveMove(gameState.turnPhase)) {
            // if (gameState.legalMovesForPlayer[boardId][`${row},${col}`]) {
            const legalMovesForBoard = gameState.legalMovesForPlayer[boardId]

            if (legalMovesForBoard && legalMovesForBoard[`${row},${col}`]) {
                return true;
            }


        }
        if (gameState.pendingPassiveMove == null) {
            return false
        }

        const allAggressiveMoves = gameState.legalMovesForPlayer[gameState.pendingPassiveMove.boardId][`${gameState.pendingPassiveMove.start.row},${gameState.pendingPassiveMove.start.col}`][0] ?? []
        if (!allAggressiveMoves) {
            return false
        }

        for (const aggressiveMove of allAggressiveMoves.aggressiveMoves) {
            if (aggressiveMove.start.row == row && aggressiveMove.start.col == col && aggressiveMove.boardId == boardId) {
                return true
            }
        }
        return false


    }

    function isSelectedStone(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): boolean {
        // Highlights Selected Stone
        return firstSelection?.boardId == boardId && firstSelection.position.row == row && firstSelection.position.col == col;

    }

    function isAvailableCellToMove(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate) {

        if ((firstSelection?.boardId != boardId) || !gameState) {
            return false
        }
        console.log(gameState)


        if (isAggressiveMove(gameState.turnPhase)) {

            const legalMovesForBoard = gameState.legalMovesForPlayer[gameState.pendingPassiveMove.boardId][`${gameState.pendingPassiveMove.start.row},${gameState.pendingPassiveMove.start.col}`]

            if (!legalMovesForBoard) {
                return false
            }
            // when aggressive move

            for (const moves of legalMovesForBoard) {
                for (const move of moves.aggressiveMoves) {
                    if (move.direction != gameState.pendingPassiveMove.direction || move.start.row != firstSelection.position.row || move.start.col != firstSelection.position.col) {
                        continue
                    }
                    const endPosition = getMoveEnd(move)
                    if (endPosition.row == row && endPosition.col == col) {

                        return true

                    }
                }


            }
        }
        if (!isAggressiveMove(gameState.turnPhase)) {

            const passiveMoves = gameState.legalMovesForPlayer[boardId]

            const allMoves = passiveMoves[`${firstSelection.position.row},${firstSelection.position.col}`]


            for (const move of allMoves) {

                const endPosition = getMoveEnd(move.passiveMove)
                if (endPosition.row == row && endPosition.col == col) {
                    return true
                }
            }
        }
        return false


    }


    async function handleCellClick(
        boardId: BoardId,
        row: BoardCoordinate,
        col: BoardCoordinate
    ): Promise<void> {
        setErrorMessage(null);

        const clickedPosition: Position = {row, col};

        if (!isAggressiveMove(currentGameState.turnPhase)) {
            if (!isOwnBoard(boardId, getSideToMove(currentGameState.turnPhase))) {
                setErrorMessage("Passive move must start on your own board.");
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
                setErrorMessage("Second selection should be on first selection board");
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
            <div className="errorMessage">{errorMessage ? errorMessage : ""}</div>
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