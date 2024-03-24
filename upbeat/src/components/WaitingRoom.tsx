import React, { useEffect } from "react";
import useWebSocket from "../customHook/useWebSocket.ts";
import { useState } from "react";
import { useAppDispatch, useAppSelector } from "../store/hooks.ts";
import { selectUsername } from "../store/Slices/usernameSlice.ts";
import {
  selectWebSocket,
  messageType,
  setIsConnected,
} from "../store/Slices/webSocketSlice.ts";
import ReadyIcon from "./ReadyIcon.tsx";
import NotReadyIcon from "./NotReadyIcon.tsx";
import SettingPage from "./SettingPage.tsx";
import Modal from "./Modal.tsx";
import GamePage from "./GamePage.tsx";

const Circle = ({ color }: { color: string }) => {
  return (
    <svg height="40" width="40">
      <circle cx="21" cy="20" r="19" fill={color} />
    </svg>
  );
};

function WaitingRoom() {
  const { sendPlayerStatus, disconnect } = useWebSocket();
  const [playerStatus, setStatus] = useState<boolean>(false);
  const [onSettingPage, setOnSettingPage] = useState<boolean>(false);
  const [open, setOpenModal] = useState<boolean>(false);
  const [openNotReady, setOpenModalNotReady] = useState<boolean>(false);
  const username = useAppSelector(selectUsername);
  const webSocketState = useAppSelector(selectWebSocket);
  const myUser = webSocketState.onetime?.user?.find(
    ({ sender }) => sender === username
  );

  return (
    <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-5">
      {webSocketState.gameStart ? (
        playerStatus ? (
          <GamePage />
        ) : (
          (() => {
            disconnect();
            return null;
          })()
        )
      ) : (
        <div>
          {onSettingPage ? <SettingPage buttonState={setOnSettingPage} /> : ""}
          {open ? (
            <Modal
              open={open}
              onClose={() => setOpenModal(false)}
              header="Message from server"
            >
              <span>
                Game requires more than 1 player to the start the game.
              </span>
            </Modal>
          ) : (
            ""
          )}
          {openNotReady ? (
            <Modal
              open={openNotReady}
              onClose={() => setOpenModalNotReady(false)}
              header="Message from server"
            >
              <span>
                Game requires at least 1 ready player to start the game.
              </span>
            </Modal>
          ) : (
            ""
          )}
          <div className="bg-blue-200 p-8 rounded-2xl shadow-xl">
            {/* Your login form or content goes here */}
            <h2 className="font-beyonders select-none text-blue-900 text-2xl text-center py-3 mx-40 align-top">
              GAME ROOM
            </h2>
            {webSocketState.onetime?.user?.map((message, index) => {
              return (
                <div className="flex flex-row my-5" key={message.sender}>
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
                    type="button"
                    className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-4 rounded-3xl flex items-center justify-center mx-5"
                    onClick={() => {
                      let playerAmount: number | undefined =
                        webSocketState.onetime?.user?.length;
                      let readyAmount: number | undefined =
                        webSocketState.onetime?.readyPerson;
                      if (
                        playerAmount !== undefined &&
                        readyAmount !== undefined
                      ) {
                        if (playerAmount > 1 && readyAmount > 0) {
                          setStatus(true);
                          sendPlayerStatus("start", username);
                        } else if (playerAmount > 1 && readyAmount === 0) {
                          setOpenModalNotReady(true);
                        } else {
                          setOpenModal(true);
                        }
                      }
                    }}
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
      )}
    </div>
  );
}

export default WaitingRoom;
