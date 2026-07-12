import type {StoneColor} from "../types/game/MoveTypes.ts";
import type {AuthUser} from "../types/ApiResponses/AuthResponses.ts";


type JoinGameProps = { gameId: String, authUser: AuthUser | null }

export async function joinGame({gameId, authUser}: JoinGameProps) {


    const response = await fetch(`/api/game/${gameId}/joinGame`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},

        body: JSON.stringify({
            playerInternalId: authUser?.playerInternalId, userName: authUser?.playerName

        })
    })

    const {playerId, playerColor} = await response.json();
    localStorage.setItem(`game:${gameId}`, JSON.stringify({playerId, playerColor}))

    return gameId


}

type StartGameProps = { authUser: AuthUser | null, selectedStartStone: StoneColor }

export async function startGame({authUser, selectedStartStone}: StartGameProps) {
    const response = await fetch("/api/game/startGame", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            startSide: selectedStartStone,
            startPlayer: {userId: authUser?.playerInternalId, userName: authUser?.playerName}

        })
    });

    if (!response.ok) {
        const error = await response.json();
        console.log(error);
    }

    const {gameId, playerId, playerColor} = await response.json();
    localStorage.setItem(`game:${gameId}`, JSON.stringify({playerId, playerColor}))
    return gameId

};