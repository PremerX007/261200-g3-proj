import React from "react";
import useWebSocket from "../customHook/useWebSocket.ts";
import { useState } from "react";
import { useAppSelector } from "../store/hooks.ts";
import { selectUsername } from "../store/Slices/usernameSlice.ts";
import {
  selectWebSocket,
  messageType,
} from "../store/Slices/webSocketSlice.ts";

const Circle = ({ color }: { color: string }) => {
  // const [count, setCount] = useState(0);

  return (
    <svg height="37" width="37">
      <circle cx="18" cy="20" r="17" fill={color} />
    </svg>
  );
};

function WaitingRoom() {
  const username = useAppSelector(selectUsername);
  const webSocketState = useAppSelector(selectWebSocket);
  const myUser = webSocketState.onetime?.arr?.find(
    ({ sender }) => sender === username
  );
  return (
    <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-5">
      <div className="bg-blue-200 p-8 rounded-2xl shadow-xl">
        {/* Your login form or content goes here */}
        <h2 className="font-beyonders select-none text-blue-900 drop-shadow-xl text-2xl text-center py-3 mx-60 align-top">
          GAME ROOM
        </h2>

        {webSocketState.onetime?.arr?.map((message, index) => {
          return (
            <div className="flex flex-row my-5">
              <div>
                <Circle color={message.admin ? "red" : "blue"} />
              </div>
              <h3 className="ml-7 text-black font-concert text-center text-4xl align-middle">
                {message.sender.toUpperCase()}
              </h3>
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
              >
                Game Setting
              </button>
            </div>
          ) : (
            <button
              type="submit"
              className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-4 rounded-3xl flex items-center justify-center mx-5"
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
