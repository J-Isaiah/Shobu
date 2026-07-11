import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import "./GameOptionMenu.css"
import type {GetStatsResponse} from "../types/data/stats.ts";
import LoginPopup from "../components/mainMenu/LoginPopup.tsx";
import type {AuthUser} from "../types/ApiResponses/AuthResponses.ts";
import {toTitleCase} from "../utils/toTitleCase.ts";

function extractGameId(input: string): string {
    return input.split("/").pop() ?? "";
}

export default function GameOptionMenu() {
    const navigate = useNavigate();
    const [joinString, setJoinString] = useState("")
    const [showLogin, setShowLogin] = useState(false)
    const [selectedStartStone] = useState("WHITE")
    const [authUser, setAuthUser] = useState<AuthUser | null>(
        getStoredAuthUser
    );

    const [stats, setStats] = useState<GetStatsResponse | null>(null);





    useEffect(() => {
        async function fetchStats() {
            const response = await fetch("/api/stats");
            const data: GetStatsResponse = await response.json();
            setStats(data);
            console.log(data)
        }


        fetchStats();
    }, []);

    const startGame = async () => {

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

        navigate(`/game/${gameId}`);
    };

    const joinGame = async () => {
        const gameId = extractGameId(joinString)

        const response = await fetch(`/api/game/${gameId}/joinGame`, {
            method: "POST",
            headers: {"Content-Type": "application/json"}
        })

        const {playerId, playerColor} = await response.json();
        localStorage.setItem(`game:${gameId}`,JSON.stringify({playerId, playerColor}))
        navigate(`/game/${gameId}`)


    }
    return <>
        {showLogin && <LoginPopup setAuthUser={setAuthUser} onClose={() => setShowLogin(false)}/>}
        <button disabled={!!authUser} className="wood-pattern login" onClick={() => {
            setShowLogin(true)
        }}>{
            authUser != null ? `Welcome: ${toTitleCase(authUser.playerName)}` : "LogIn"}</button>
        <div className="home-page">
            <div className="title-container wood-pattern">
                <div className="title">
                    SHŌBU
                </div>
            </div>

            <div className="wood-pattern game-count">Over <b>{stats?.totalGamesPlayed}</b> Games Played</div>
            <div>
                <button className="wood-pattern start-game" onClick={startGame}>Start Game</button>
            </div>
            <div className="wood-pattern or-container">
                <div className="or-text">
                    OR
                </div>
            </div>
            <div className="input-field">
                <input type="text"
                       className="wood-pattern input"
                       value={joinString}
                       onChange={(e) => {
                           setJoinString(e.target.value)
                       }}
                       placeholder={"Game URL"}/>
            </div>
            <div>
                <button className="wood-pattern join-button" onClick={joinGame}>Join Game</button>
            </div>
            <div className="stats-container">
                <div className="wood-pattern win-stats">
                    <div className="win-text">
                        Isaiah's Wins:
                    </div>
                    <div className="win-number">
                        {stats?.isaiahWins ?? 0}

                    </div>
                </div>

                <div className="wood-pattern win-stats">
                    <div className="win-text">
                        Julia's Wins:
                    </div>
                    <div className="win-number">{stats?.juliaWins ?? 0}</div>
                </div>


            </div>
        </div>
    </>
}

function getStoredAuthUser(): AuthUser | null {
    const value = localStorage.getItem("authUser");
    console.log(value)

    if (!value) {
        return null;
    }

    try {
        return JSON.parse(value) as AuthUser;
    } catch {
        localStorage.removeItem("authUser");
        return null;
    }
}