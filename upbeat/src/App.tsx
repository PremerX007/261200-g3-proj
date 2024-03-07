import React from "react";
import LoginPage from "./components/LoginPage";
import WaitingRoom from "./components/WaitingRoom";
import { useAppSelector } from "./store/hooks";
import { selectWebSocket } from "./store/Slices/webSocketSlice";

function App() {
  const webSocketState = useAppSelector(selectWebSocket);
  return (
    <div className="h-screen flex flex-col justify-center items-center">
      <div className="w-full h-1/2 bg-blue-500"></div>
      <div className="w-full h-1/2 bg-white"></div>
      {webSocketState.isConnected ? <WaitingRoom /> : <LoginPage />}
      {/* <LoginPage /> */}
      {/* <WaitingRoom /> */}
    </div>
  );
}

export default App;
