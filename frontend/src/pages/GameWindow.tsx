import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css"
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import type {GameState} from "../types/game/MoveTypes.ts";

export default function GameWindow(){
    const [gameState, setGameState] = useState<GameState|null>(null)
    const { gameId } = useParams();

    const getGameState = async () => {
        const response =await  fetch(`/api/game/${gameId}/getGameState`)
        if (!response.ok){
            console.log("Error",response)
            return
        }
        setGameState(await response.json())
    }
    useEffect(() => {void getGameState()}, [gameId])
    console.log(gameState)
    if (gameState === null) {
        return <div>Loading...</div>;
    }



    return (
        <div className="gameSpace">
            <div className="opponentArea">
                <Board color={"dark"} board={gameState?.updatedGameBoards.BLACK_DARK}></Board>
                <Board color={"light"} board={gameState?.updatedGameBoards.BLACK_LIGHT}></Board>
            </div>

            <div className="playerArea">
                <Board color={"light"} board={gameState?.updatedGameBoards.WHITE_LIGHT}></Board>
                <Board color={"dark"} board={gameState?.updatedGameBoards.WHITE_DARK}></Board>
            </div>


        </div>
    )
}