import React from "react";
import LoginPage from "./components/LoginPage";
import WaitingRoom from "./components/WaitingRoom";
import { useAppSelector } from "./store/hooks";
import { selectWebSocket } from "./store/Slices/webSocketSlice";
import GamePage from "./components/GamePage";
import SettingPage from "./components/SettingPage";

function App() {
  const webSocketState = useAppSelector(selectWebSocket);
  return (
    <div className="h-screen flex flex-col justify-center items-center">
      <div className="w-full h-1/2 bg-blue-500"></div>
      <div className="w-full h-1/2 bg-white"></div>
      {webSocketState.isConnected ? <WaitingRoom /> : <LoginPage />}
    </div>
  );
}

export default App;
