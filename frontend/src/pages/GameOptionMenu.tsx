import {useNavigate} from "react-router-dom";

export default function GameOptionMenu() {
    const navigate = useNavigate();

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

        const {gameId, playerId} = await response.json();
        localStorage.setItem("playerId", playerId);


        navigate(`/game/${gameId}`);
    };

    return <button onClick={startGame}>Start Game</button>;
}