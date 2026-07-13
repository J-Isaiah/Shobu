import "../../components/gameOverScreen/gameOver.css"
import type {GameState, StoneColor} from "../../types/game/MoveTypes.ts";
import {rematch} from "../../api/game.ts";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

type GameOverProps = {
    gameWinner: StoneColor;
    gameState: GameState;

}

export default function GameOver({gameWinner,  gameState}: GameOverProps) {
    const navigate = useNavigate();


    const [rematchError, setRematchError] = useState("")

    const gameInfo = localStorage.getItem(`game:${gameState?.gameId}`);
    console.log(gameInfo)

    const parsedGameInfo = gameInfo ? JSON.parse(gameInfo) : null;

    const message = gameWinner === parsedGameInfo.playerColor ? "You Win!" : "YOU LOSE :("


    async function handleRematch() {
        try {
            if (!parsedGameInfo) {
                setRematchError("Missing game information");
                return;
            }

            const {newGameId, error} = await rematch({
                authUser: parsedGameInfo.playerId,
                gameId: gameState.gameId
            });


            if (error) {
                setRematchError(error ?? "Unable to start rematch");
            }

            navigate(`/game/${newGameId}`)
        } catch (error) {
            console.error(error);
            setRematchError("Unable to connect to the server");
        }


    }

    return (
        <div className={"game-over-container"}>
            {rematchError && (
                <div className="rematch-error">
                    {rematchError}
                </div>
            )}            <div className={"game-over-card wood-pattern"}>
                <p className={
                    "game-over-message"
                }>{message}</p>
                <div className={"game-over-button-container"}>
                    <button className={"game-over-button"}>Main Menu</button>
                    <button className="game-over-button" onClick={handleRematch}>
                        Rematch
                    </button>
                </div>

            </div>

        </div>
    )
}