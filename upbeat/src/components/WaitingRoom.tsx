import React from "react";
import useWebSocket from "../customHook/useWebSocket.ts";
import { useState } from "react";
import { useAppSelector } from "../store/hooks.ts";
import { selectUsername } from "../store/Slices/usernameSlice.ts";
import {
  selectWebSocket,
  messageType,
} from "../store/Slices/webSocketSlice.ts";
import ReadyIcon from "./ReadyIcon.tsx";
import NotReadyIcon from "./NotReadyIcon.tsx";
import SettingPage from "./SettingPage.tsx";

const Circle = ({ color }: { color: string }) => {
  return (
    <svg height="40" width="40">
      <circle cx="21" cy="20" r="19" fill={color} />
    </svg>
  );
};

function WaitingRoom() {
  const { sendPlayerStatus } = useWebSocket();
  const [playerStatus, setStatus] = useState<boolean>(false);
  const [onSettingPage, setOnSettingPage] = useState<boolean>(false);
  const username = useAppSelector(selectUsername);
  const webSocketState = useAppSelector(selectWebSocket);
  const myUser = webSocketState.onetime?.arr?.find(
    ({ sender }) => sender === username
  );
  return (
    <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-5">
      {onSettingPage ? <SettingPage buttonState={setOnSettingPage} /> : ""}
      <div className="bg-blue-200 p-8 rounded-2xl shadow-xl">
        {/* Your login form or content goes here */}
        <h2 className="font-beyonders select-none text-blue-900 text-2xl text-center py-3 mx-40 align-top">
          GAME ROOM
        </h2>
        {webSocketState.onetime?.arr?.map((message, index) => {
          return (
            <div className="flex flex-row my-5">
              {message.admin ? (
                <div>
                  <Circle color="blue" />
                </div>
              ) : message.type === "READY" ? (
                <ReadyIcon />
              ) : (
                <NotReadyIcon />
              )}

              <h3 className="ml-7 text-black font-concert text-center text-4xl align-middle">
                {message.sender.toUpperCase()}
              </h3>
              <div className="justify-items-end"></div>
            </div>
          );
        })}

        <div className="flex flex-row items-center justify-center">
          {myUser?.admin ? (
            <div className="flex flex-row">
              <button
                type="submit"
                className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-4 rounded-3xl flex items-center justify-center mx-5"
              >
                start
              </button>
              <button
                type="submit"
                className="bg-red-500 text-white select-none hover:bg-white hover:ring hover:ring-red-600 hover:text-red-500 font-beyonders text-xm border px-4 py-2 rounded-3xl flex items-center justify-center mx-5"
                onClick={() => {
                  setOnSettingPage(true);
                }}
              >
                Game Setting
              </button>
            </div>
          ) : playerStatus ? (
            <button
              type="submit"
              className="bg-red-500 text-white select-none hover:bg-white hover:ring hover:ring-red-600 hover:text-red-500 font-beyonders text-xm border px-4 py-4 rounded-3xl flex items-center justify-center mx-5"
              onClick={() => {
                setStatus(false);
                sendPlayerStatus("notready", username);
              }}
            >
              not ready
            </button>
          ) : (
            <button
              type="submit"
              className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-4 rounded-3xl flex items-center justify-center mx-5"
              onClick={() => {
                setStatus(true);
                sendPlayerStatus("ready", username);
              }}
            >
              ready
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

export default WaitingRoom;
