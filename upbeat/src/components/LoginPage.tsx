import { useState } from "react";
import AnimateLogo from "./AnimateLogo";
import { useDispatch } from "react-redux";
import useWebSocket from "../customHook/useWebSocket.ts";
import { setUsername as sliceSetUsername } from "../store/Slices/usernameSlice.ts";
import { getPlayer, setServer } from "../repositories/restApi.ts";
import { groupMessage } from "../store/Slices/webSocketSlice.ts";
import Modal from "./Modal.tsx";
import TimerCom from "./TimerComLegacy.tsx";
import { selectWebSocket } from "../store/Slices/webSocketSlice.ts";
import { useAppSelector } from "../store/hooks.ts";

function LoginPage() {
  const [username, setUsername] = useState<string>("");
  const [open, setOpenModal] = useState<boolean>(true);
  const [isDuplicatePlayer, setDupicate] = useState<boolean>(false);
  const [isFullPlayer, setFullPlayer] = useState<boolean>(false);
  const [isUsernameRule, setUsernameRule] = useState<boolean>(false);
  const [isGameStarted, setGameStarted] = useState<boolean>(false);
  const dispatch = useDispatch();
  const { connect } = useWebSocket();
  const webSocketState = useAppSelector(selectWebSocket);
  let arrayGroup: groupMessage;
  setServer("http://localhost:8080");

  return (
    <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-5">
      <div className="bg-blue-200 p-8 rounded-2xl shadow-xl">
        <AnimateLogo />
        <h2 className="font-beyonders select-none text-blue-900 drop-shadow-xl text-5xl text-center mx-auto my-5 py-3">
          UPBEAT
        </h2>

        {webSocketState.gameStart ? (
          <Modal
            open={open}
            onClose={() => setOpenModal(false)}
            header="Message from server"
            content="You were kicked out of the waiting room. Because you weren't ready"
          ></Modal>
        ) : (
          ""
        )}

        {isGameStarted ? (
          <Modal
            open={open}
            onClose={() => setOpenModal(false)}
            header="Message from server"
            content="The game is in progress. Please wait for the next turn."
          ></Modal>
        ) : (
          ""
        )}

        {isUsernameRule ? (
          <Modal
            open={open}
            onClose={() => setOpenModal(false)}
            header="Message from server"
            content="Player username must be less than 20 character."
          ></Modal>
        ) : isFullPlayer ? (
          <Modal
            open={open}
            onClose={() => setOpenModal(false)}
            header="Message from server"
            content="Waiting room is full. Please wait a moment."
          ></Modal>
        ) : isDuplicatePlayer ? (
          <Modal
            open={open}
            onClose={() => setOpenModal(false)}
            header="Message from server"
            content="Player username is already, please user another username."
          ></Modal>
        ) : (
          ""
        )}

        <form
          onSubmitCapture={async (e) => {
            e.preventDefault();
            if (webSocketState.gameStart) {
              setGameStarted(true);
              setOpenModal(true);
              return;
            }
            if (username.length > 20) {
              setUsernameRule(true);
              setOpenModal(true);
              return;
            }
            await getPlayer()
              .then((response) => {
                arrayGroup = Array.isArray(response.data)
                  ? response.data
                  : [response.data];
                console.log(response.data);
              })
              .catch((e) => console.log("error to call player api" + e));
            if (arrayGroup[0].user.length === 4) {
              setFullPlayer(true);
              setOpenModal(true);
            } else if (arrayGroup[0].user.length > 0) {
              if (
                arrayGroup[0].user.find(
                  ({ sender }) =>
                    sender.toUpperCase() === username.toUpperCase()
                )
              ) {
                setDupicate(true);
                setOpenModal(true);
              } else {
                dispatch(sliceSetUsername(username));
                connect(username);
              }
            } else {
              dispatch(sliceSetUsername(username));
              connect(username);
            }
          }}
        >
          <div className="my-7 px-6 items-center justify-center">
            <input
              type="text"
              placeholder="Enter your name"
              className="block w-full px-4 py-2 border rounded-2xl outline outline-offset-2 outline-blue-900 bg-white text-black"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>

          <div className="flex flex-row justify-center">
            <button
              type="submit"
              className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-4 rounded-3xl flex items-center justify-center mx-5"
            >
              Login
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
