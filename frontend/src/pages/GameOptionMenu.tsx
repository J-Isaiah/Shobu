import {useNavigate} from "react-router-dom";
import {useState} from "react";

function extractGameId(input: string): string {
    return input.split("/").pop() ?? "";
}

export default function GameOptionMenu() {
    const navigate = useNavigate();
    const [joinString, setJoinString] = useState("")

    const startGame = async () => {
        const payload = {
            startSide: "WHITE_PASSIVE",
            player1: {"userId": "1497e843-a462-4ec1-ad2d-9dc85b0a694a", "userName": "Isaiah"},
            player2: {"userId": "1497e843-a462-4ec1-ad2d-9dc85b0a694a", "userName": "NotISaiah"}
        };

        const response = await fetch("/api/game/startGame", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const error = await response.json();
            console.log(error);
        }

        const {gameId, playerId, playerColor} = await response.json();
        localStorage.setItem("playerId", playerId);

        localStorage.setItem("playerColor", playerColor);

        navigate(`/game/${gameId}`);
    };

    const joinGame = async () => {
        const gameId = extractGameId(joinString)

        const response = await fetch(`/api/game/${gameId}/joinGame`,{
            method: "POST",
            headers: {"Ccontent-Type": "application/json"}
        })

        const {playerId, playerColor}= await response.json();
        localStorage.setItem("playerId", playerId)
        localStorage.setItem("playerColor", playerColor)
        navigate(`/game/${gameId}`)



    }
    return <>
        <div>
            <div>
                <button onClick={startGame}>Start Game</button>
            </div>
            <div>
                <input type="text"
                       value={joinString}
                       onChange={(e) => {
                           setJoinString(e.target.value)
                       }}
                placeholder={"Game URL"}/>
            </div>
            <div>
                <button onClick={joinGame}>Join Game</button>
            </div>
        </div>
    </>

}