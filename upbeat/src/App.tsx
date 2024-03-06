import React from "react";
import LoginPage from "./components/LoginPage";

function App() {
  return (
    <div className="h-screen flex flex-col justify-center items-center">
      <div className="w-full h-1/2 bg-blue-500"></div>
      <div className="w-full h-1/2 bg-white"></div>
      <LoginPage />
    </div>
  );
}

export default App;
