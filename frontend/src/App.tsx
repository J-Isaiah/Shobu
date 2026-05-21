import { Routes, Route } from "react-router-dom";

import "./styles/App.css";

import GameWindow from "./pages/GameWindow.tsx";

function App() {

  return (
      <Routes>
        <Route path="/game" element={<GameWindow/>} />
      </Routes>
  )
}

export default App
