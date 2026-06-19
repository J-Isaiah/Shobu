import {useEffect, useRef, useState} from "react";
import {createStompClient} from "../../utils/stompClient.ts";
import type {Client} from "@stomp/stompjs";
import type {GameState, MakeMoveRequest, Move} from "../../types/game/MoveTypes.ts";

async function fetchGameState(gameId: string): Promise<GameState> {
    const response = await fetch(`/api/game/${gameId}/getGameState`);


    if (!response.ok) {
        throw new Error(`Failed to load game: ${response.status}`);
    }
    return await response.json()
}

export function useGameConnection(gameId: string | undefined) {
    const [gameState, setGameState] = useState<GameState | null>(null);
    const [networkError, setNetworkError] = useState<string | null>(null);
    const [isPendingMove, setIsPendingMove] = useState(false);

    const clientRef = useRef<Client | null>(null);

    useEffect(() => {
        if (!gameId) {
            setNetworkError("Missing game id.");
            return;
        }

        fetchGameState(gameId)
            .then(setGameState)
            .catch((error: Error) => setNetworkError(error.message));

        clientRef.current = createStompClient();

        clientRef.current.onConnect = () => {
            clientRef.current?.subscribe(`/topic/game/${gameId}`, (message) => {
                const updatedGameState: GameState = JSON.parse(message.body);

                setGameState(updatedGameState);
                setIsPendingMove(false);
                setNetworkError(null);
            });
        };

        clientRef.current.activate();

        return () => {
            void clientRef.current?.deactivate();
        };
    }, [gameId]);

    function makeMove({ move }: { move: Move }): void {
        if (!gameId) {
            setNetworkError("Missing game id.");
            return;
        }

        const payload: MakeMoveRequest = {
            userId: "e4526351-ff88-4655-b534-c40de2d294b9",
            move,
        };

        clientRef.current?.publish({
            destination: `/app/game/${gameId}/makeMove`,
            body: JSON.stringify(payload),
        });

        setIsPendingMove(true);
        setNetworkError(null);
    }

    return {
        makeMove,
        gameState,
        isPendingMove,
        networkError,
    };
}