import {Client} from "@stomp/stompjs"


export function createStompClient(): Client {
    return new Client({
        brokerURL: "ws://localhost:8080/ws",
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