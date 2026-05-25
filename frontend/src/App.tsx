import { Routes, Route } from "react-router-dom";

import "./styles/App.css";

import GameWindow from "./pages/GameWindow.tsx";
import GameOptionMenu from "./pages/GameOptionMenu.tsx";

function App() {

  return (
      <Routes>
        <Route path="/game" element={<GameOptionMenu/>} />

          <Route path="/game/:gameId" element={<GameWindow/>} />
      </Routes>
  )
}

export default App
