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



    return (
        <div className="gameSpace">
            <div className="opponentArea">
                <Board color={"dark"}></Board>
                <Board color={"light"}></Board>
            </div>

            <div className="playerArea">
                <Board color={"light"}></Board>
                <Board color={"dark"}></Board>
            </div>


        </div>
    )
}