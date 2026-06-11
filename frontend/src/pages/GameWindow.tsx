import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css";
import {useParams} from "react-router-dom";
import {buildMove, getSideToMove, isAggressiveMove, isOwnBoard} from "../utils/game/movePhase.ts";
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


    function isHighlightedStone(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): boolean {
        // Highlights passive and aggressive stones

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
        if (gameState.pendingPassiveMove ==  null){
            return false
        }

        const allAggressiveMoves = gameState.legalMovesForPlayer[gameState.pendingPassiveMove.boardId][`${gameState.pendingPassiveMove.start.row},${gameState.pendingPassiveMove.start.col}`][0]

        for (const  aggressiveMove of allAggressiveMoves.aggressiveMoves){
            if (aggressiveMove.start.row == row && aggressiveMove.start.col == col && aggressiveMove.boardId == boardId)
            {return true}
        }
        return false





    }

    function isSelectableStone(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): Boolean {


    }


    function handleCellClick(boardId: BoardId, row: BoardCoordinate, col: BoardCoordinate): void {
        setErrorMessage(null);

        const clickedPosition: Position = {row, col};

        if (!isAggressiveMove(currentGameState.turnPhase)) {
            if (!isOwnBoard(boardId, getSideToMove(currentGameState.turnPhase))) {
                setErrorMessage("Passive move must start on your own board.");
                return;
            }

        }
        const cellSelection: CellSelection = {
            boardId,
            position: {
                row: clickedPosition.row,
                col: clickedPosition.col,
            }


        }

        if (firstSelection) {


            if (firstSelection.boardId !== cellSelection.boardId) {
                setErrorMessage("Second selection should be on first selection board ")
                setPlayerMoves(null)
                return;
            }

            const move = buildMove(
                firstSelection.boardId, firstSelection.position, cellSelection.position
            )

            void makeMove({move})
            setFirstSelection(null)

            return
        }
        setFirstSelection(cellSelection)

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
                    isHighlightedStone={isHighlightedStone}
                />

                <Board
                    color="light"
                    board={gameState.updatedGameBoards.BLACK_LIGHT}
                    boardId={BoardId.BLACK_LIGHT}
                    onCellClick={handleCellClick}

                    isHighlightedStone={isHighlightedStone}
                />
            </div>

            <div className="whiteSide">
                <Board
                    color="light"
                    board={gameState.updatedGameBoards.WHITE_LIGHT}
                    boardId={BoardId.WHITE_LIGHT}
                    onCellClick={handleCellClick}

                    isHighlightedStone={isHighlightedStone}
                />

                <Board
                    color="dark"
                    board={gameState.updatedGameBoards.WHITE_DARK}
                    boardId={BoardId.WHITE_DARK}
                    onCellClick={handleCellClick}
                    isHighlightedStone={isHighlightedStone}
                />
            </div>
        </div>
    );
}