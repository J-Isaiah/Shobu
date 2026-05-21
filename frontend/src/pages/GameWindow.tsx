import Board from "../components/mainGame/Board.tsx";
import "./gameWindow.css"

export default function GameWindow(){
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