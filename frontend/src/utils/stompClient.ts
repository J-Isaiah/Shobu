import { Client } from "@stomp/stompjs";

export function createStompClient(): Client {
    const protocol = window.location.protocol === "https:" ? "wss" : "ws";
    const brokerURL = `${protocol}://${window.location.host}/ws`;

    return new Client({
        brokerURL,
        reconnectDelay: 5000,

        debug: (msg) => {
            console.log("[STOMP]", msg);
        },

        onConnect: () => {
            console.log("CONNECTED");
        },

        onStompError: (frame) => {
            console.error("STOMP ERROR", frame.headers["message"], frame.body);
        },

        onWebSocketError: (event) => {
            console.error("WEBSOCKET ERROR", event);
        },

        onWebSocketClose: (event) => {
            console.error("WEBSOCKET CLOSED", event);
        },
    });
}