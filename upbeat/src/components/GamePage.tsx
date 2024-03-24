import React, { useEffect, useState } from "react";
import { HexGrid, Layout, Hexagon, Text, Pattern } from "react-hexgrid";
import { Editor, Monaco } from "@monaco-editor/react";
import {
  constRest,
  getUserConfig,
  postConstCheck,
  postInitConst,
  postNewConst,
  postReuseStatus,
} from "../repositories/restApi";
import {
  selectWebSocket,
  messageType,
  GameData,
  player,
  commandType,
  region,
} from "../store/Slices/webSocketSlice.ts";
import Modal from "./Modal";
import { useAppSelector } from "../store/hooks.ts";
import { selectUsername } from "../store/Slices/usernameSlice.ts";
import { gameConfig } from "./SettingPage.tsx";
import { Checkbox, Typography } from "@material-tailwind/react";
import InitPlanGif from "../assets/oggy.gif";
import GameStartGif from "../assets/running.gif";
import TurnGif from "../assets/think.gif";
import TimerCom from "./Timer.tsx";

const Circle = ({ color }: { color: string }) => {
  return (
    <svg height="30" width="26">
      <circle cx="14" cy="18" r="12" fill={color} />
    </svg>
  );
};

const HelpIcon = ({ height, width }: { height?: number; width?: number }) => {
  return (
    <svg
      viewBox="0 0 24 24"
      fill="gray"
      height={height ? height : 50}
      width={width ? width : 50}
    >
      <path d="M12 2C6.486 2 2 6.486 2 12s4.486 10 10 10 10-4.486 10-10S17.514 2 12 2zm1 16h-2v-2h2v2zm.976-4.885c-.196.158-.385.309-.535.459-.408.407-.44.777-.441.793v.133h-2v-.167c0-.118.029-1.177 1.026-2.174.195-.195.437-.393.691-.599.734-.595 1.216-1.029 1.216-1.627a1.934 1.934 0 00-3.867.001h-2C8.066 7.765 9.831 6 12 6s3.934 1.765 3.934 3.934c0 1.597-1.179 2.55-1.958 3.181z" />
    </svg>
  );
};

const ReadyIcon = () => {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width="28" height="32">
      <circle
        r="27"
        fill="#FFF"
        stroke="#000"
        strokeWidth="0"
        transform="matrix(0.4 0 0 0.4 16 18)"
        vectorEffect="non-scaling-stroke"
      ></circle>
      <path
        fill="#6BBE66"
        fillRule="evenodd"
        strokeWidth="0"
        d="M0-25.31c13.98 0 25.31 11.33 25.31 25.31 0 13.98-11.33 25.31-25.31 25.31-13.98 0-25.31-11.33-25.31-25.31 0-13.98 11.33-25.31 25.31-25.31zM-11.2.67c.34-1.97 2.59-3.06 4.36-2 .16.1.31.21.46.34l.01.01c.8.76 1.69 1.56 2.57 2.34l.76.68 9-9.44c.53-.55.93-.91 1.73-1.09 2.76-.61 4.7 2.76 2.74 4.83L-.77 8.1c-1.06 1.13-2.94 1.23-4.08.15-.65-.6-1.36-1.22-2.07-1.84-1.24-1.08-2.5-2.18-3.53-3.26-.63-.61-.89-1.63-.75-2.48z"
        transform="matrix(0.50 0 0 0.50 15 18)"
        vectorEffect="non-scaling-stroke"
      ></path>
    </svg>
  );
};

const NotReadyIcon = () => {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width="28" height="32">
      <circle
        r="27"
        fill="#FFF"
        stroke="#000"
        strokeWidth="0"
        transform="matrix(0.4 0 0 0.4 16 18)"
        vectorEffect="non-scaling-stroke"
      ></circle>
      <path
        fill="#FF4141"
        fillRule="evenodd"
        d="M0-25.31c13.98 0 25.31 11.33 25.31 25.31 0 13.98-11.33 25.31-25.31 25.31-13.98 0-25.31-11.33-25.31-25.31 0-13.98 11.33-25.31 25.31-25.31zM4.95-9.18a2.88 2.88 0 014.11-.01c1.14 1.14 1.14 3 .01 4.15L4.1 0l4.97 5.05c1.12 1.14 1.11 2.99-.03 4.13-1.14 1.14-2.98 1.14-4.1-.01L0 4.16l-4.95 5.02a2.88 2.88 0 01-4.11.01c-1.14-1.14-1.14-3-.01-4.15L-4.1 0l-4.98-5.05c-1.12-1.14-1.11-2.99.03-4.13 1.14-1.14 2.98-1.14 4.1.01l4.94 5.01 4.96-5.02z"
        transform="matrix(0.50 0 0 0.50 15 18)"
        vectorEffect="non-scaling-stroke"
      ></path>
    </svg>
  );
};

const hexgrid9 = (map: GameData) => [
  // columm1
  { q: -4, r: -2, s: 6, i: 1, j: 1, reg: map.territory[1][1] },
  { q: -4, r: -1, s: 5, i: 2, j: 1, reg: map.territory[2][1] },
  { q: -4, r: 0, s: 4, i: 3, j: 1, reg: map.territory[3][1] },
  { q: -4, r: 1, s: 3, i: 4, j: 1, reg: map.territory[4][1] },
  { q: -4, r: 2, s: 2, i: 5, j: 1, reg: map.territory[5][1] },
  { q: -4, r: 3, s: 1, i: 6, j: 1, reg: map.territory[6][1] },
  { q: -4, r: 4, s: 0, i: 7, j: 1, reg: map.territory[7][1] },
  { q: -4, r: 5, s: -1, i: 8, j: 1, reg: map.territory[8][1] },
  { q: -4, r: 6, s: -2, i: 9, j: 1, reg: map.territory[9][1] },
  // columm2
  { q: -3, r: -3, s: 6, i: 1, j: 2, reg: map.territory[1][2] },
  { q: -3, r: -2, s: 5, i: 2, j: 2, reg: map.territory[2][2] },
  { q: -3, r: -1, s: 4, i: 3, j: 2, reg: map.territory[3][2] },
  { q: -3, r: 0, s: 3, i: 4, j: 2, reg: map.territory[4][2] },
  { q: -3, r: 1, s: 2, i: 5, j: 2, reg: map.territory[5][2] },
  { q: -3, r: 2, s: 1, i: 6, j: 2, reg: map.territory[6][2] },
  { q: -3, r: 3, s: 0, i: 7, j: 2, reg: map.territory[7][2] },
  { q: -3, r: 4, s: -1, i: 8, j: 2, reg: map.territory[8][2] },
  { q: -3, r: 5, s: -2, i: 9, j: 2, reg: map.territory[9][2] },
  // columm3
  { q: -2, r: -3, s: 5, i: 1, j: 3, reg: map.territory[1][3] },
  { q: -2, r: -2, s: 4, i: 2, j: 3, reg: map.territory[2][3] },
  { q: -2, r: -1, s: 3, i: 3, j: 3, reg: map.territory[3][3] },
  { q: -2, r: 0, s: 2, i: 4, j: 3, reg: map.territory[4][3] },
  { q: -2, r: 1, s: 1, i: 5, j: 3, reg: map.territory[5][3] },
  { q: -2, r: 2, s: 0, i: 6, j: 3, reg: map.territory[6][3] },
  { q: -2, r: 3, s: -1, i: 7, j: 3, reg: map.territory[7][3] },
  { q: -2, r: 4, s: -2, i: 8, j: 3, reg: map.territory[8][3] },
  { q: -2, r: 5, s: -3, i: 9, j: 3, reg: map.territory[9][3] },
  // columm4
  { q: -1, r: -4, s: 5, i: 1, j: 4, reg: map.territory[1][4] },
  { q: -1, r: -3, s: 4, i: 2, j: 4, reg: map.territory[2][4] },
  { q: -1, r: -2, s: 3, i: 3, j: 4, reg: map.territory[3][4] },
  { q: -1, r: -1, s: 2, i: 4, j: 4, reg: map.territory[4][4] },
  { q: -1, r: 0, s: 1, i: 5, j: 4, reg: map.territory[5][4] },
  { q: -1, r: 1, s: 0, i: 6, j: 4, reg: map.territory[6][4] },
  { q: -1, r: 2, s: -1, i: 7, j: 4, reg: map.territory[7][4] },
  { q: -1, r: 3, s: -2, i: 8, j: 4, reg: map.territory[8][4] },
  { q: -1, r: 4, s: -3, i: 9, j: 4, reg: map.territory[9][4] },
  // columm5
  { q: 0, r: -4, s: 4, i: 1, j: 5, reg: map.territory[1][5] },
  { q: 0, r: -3, s: 3, i: 2, j: 5, reg: map.territory[2][5] },
  { q: 0, r: -2, s: 2, i: 3, j: 5, reg: map.territory[3][5] },
  { q: 0, r: -1, s: 1, i: 4, j: 5, reg: map.territory[4][5] },
  { q: 0, r: 0, s: 0, i: 5, j: 5, reg: map.territory[5][5] },
  { q: 0, r: 1, s: -1, i: 6, j: 5, reg: map.territory[6][5] },
  { q: 0, r: 2, s: -2, i: 7, j: 5, reg: map.territory[7][5] },
  { q: 0, r: 3, s: -3, i: 8, j: 5, reg: map.territory[8][5] },
  { q: 0, r: 4, s: -4, i: 9, j: 5, reg: map.territory[9][5] },
  // columm6
  { q: 1, r: -5, s: 4, i: 1, j: 6, reg: map.territory[1][6] },
  { q: 1, r: -4, s: 3, i: 2, j: 6, reg: map.territory[2][6] },
  { q: 1, r: -3, s: 2, i: 3, j: 6, reg: map.territory[3][6] },
  { q: 1, r: -2, s: 1, i: 4, j: 6, reg: map.territory[4][6] },
  { q: 1, r: -1, s: 0, i: 5, j: 6, reg: map.territory[5][6] },
  { q: 1, r: 0, s: -1, i: 6, j: 6, reg: map.territory[6][6] },
  { q: 1, r: 1, s: -2, i: 7, j: 6, reg: map.territory[7][6] },
  { q: 1, r: 2, s: -3, i: 8, j: 6, reg: map.territory[8][6] },
  { q: 1, r: 3, s: -4, i: 9, j: 6, reg: map.territory[9][6] },
  // columm7
  { q: 2, r: -5, s: 3, i: 1, j: 7, reg: map.territory[1][7] },
  { q: 2, r: -4, s: 2, i: 2, j: 7, reg: map.territory[2][7] },
  { q: 2, r: -3, s: 1, i: 3, j: 7, reg: map.territory[3][7] },
  { q: 2, r: -2, s: 0, i: 4, j: 7, reg: map.territory[4][7] },
  { q: 2, r: -1, s: -1, i: 5, j: 7, reg: map.territory[5][7] },
  { q: 2, r: 0, s: -2, i: 6, j: 7, reg: map.territory[6][7] },
  { q: 2, r: 1, s: -3, i: 7, j: 7, reg: map.territory[7][7] },
  { q: 2, r: 2, s: -4, i: 8, j: 7, reg: map.territory[8][7] },
  { q: 2, r: 3, s: -5, i: 9, j: 7, reg: map.territory[9][7] },
  // columm8
  { q: 3, r: -6, s: 3, i: 1, j: 8, reg: map.territory[1][8] },
  { q: 3, r: -5, s: 2, i: 2, j: 8, reg: map.territory[2][8] },
  { q: 3, r: -4, s: 1, i: 3, j: 8, reg: map.territory[3][8] },
  { q: 3, r: -3, s: 0, i: 4, j: 8, reg: map.territory[4][8] },
  { q: 3, r: -2, s: -1, i: 5, j: 8, reg: map.territory[5][8] },
  { q: 3, r: -1, s: -2, i: 6, j: 8, reg: map.territory[6][8] },
  { q: 3, r: 0, s: -3, i: 7, j: 8, reg: map.territory[7][8] },
  { q: 3, r: 1, s: -4, i: 8, j: 8, reg: map.territory[8][8] },
  { q: 3, r: 2, s: -5, i: 9, j: 8, reg: map.territory[9][8] },
  // columm9
  { q: 4, r: -6, s: 2, i: 1, j: 9, reg: map.territory[1][9] },
  { q: 4, r: -5, s: 1, i: 2, j: 9, reg: map.territory[2][9] },
  { q: 4, r: -4, s: 0, i: 3, j: 9, reg: map.territory[3][9] },
  { q: 4, r: -3, s: -1, i: 4, j: 9, reg: map.territory[4][9] },
  { q: 4, r: -2, s: -2, i: 5, j: 9, reg: map.territory[5][9] },
  { q: 4, r: -1, s: -3, i: 6, j: 9, reg: map.territory[6][9] },
  { q: 4, r: 0, s: -4, i: 7, j: 9, reg: map.territory[7][9] },
  { q: 4, r: 1, s: -5, i: 8, j: 9, reg: map.territory[8][9] },
  { q: 4, r: 2, s: -6, i: 9, j: 9, reg: map.territory[9][9] },
];

function GamePage() {
  const [opCheckPlan, setOperateCheckplan] = useState<boolean>(false);
  const [open, setOpenModal] = useState<boolean>(true);
  const [msgFromServer, setMessageFromServer] = useState<string | undefined>(
    ""
  );
  const [typedPlan, setTypedPlan] = useState<string | undefined>(
    "# Write your construction plan here"
  );
  const username = useAppSelector(selectUsername);
  const webSocketState = useAppSelector(selectWebSocket);

  const [isRevPlan, setIsRevPlan] = useState(false);
  const [starterHelpModal, setStarterModal] = useState(true);
  const [isInitPlan, setIsInitPlan] = useState(false);
  const [openmodalStateInit, setOpenModalStateInit] = useState(true);
  const [openmodalStateGame, setOpenModalStateGame] = useState(true);
  const [openmodalTurnGame, setOpenModalTurnGame] = useState(true);

  const [reusePlan, setReusePlan] = useState(false);
  const [region, setRegion] = useState<region>();
  const [reghover, setRegHover] = useState(false);
  const [checkboxReuseplan, setCheckboxReuseplan] = useState(false);
  const president = webSocketState.gamedata?.playerlist.find(
    (e) => e.name === username
  );
  const whonowturn = webSocketState.gamedata?.playerlist.find(
    (e) => e.name === webSocketState.gamestate?.nowturn
  );

  const [userConfig, setUserConfig] = useState<gameConfig>();
  const handleActiveInitPlan = () => {
    setIsInitPlan(!isInitPlan);
  };
  const [timeInitPlan, setTimeInitPlan] = useState({
    min: 4,
    sec: 0,
  });
  const [timeRev, setTimeRev] = useState({
    min: 2,
    sec: 0,
  });
  const handleActiveRevPlan = () => {
    setIsRevPlan(!isRevPlan);
  };
  useEffect(() => {
    if (webSocketState.gamestate?.nowturn === username) {
      setCheckboxReuseplan(true);
    }
  }, [webSocketState.gamestate?.nowturn]);

  return (
    <div className="h-screen w-screen flex flex-col justify-center items-center bg-white">
      {webSocketState.gamestate?.command === commandType.INIT ? (
        <Modal
          open={openmodalStateInit}
          onClose={() => setOpenModalStateInit(false)}
          header=""
        >
          <div className="flex flex-col justify-center items-center">
            <img src={InitPlanGif} alt="oggy" className="rounded-3xl mb-5" />
            <p className="text-3xl text-gray-700 font-bold">
              Initial Constrction Plan
            </p>
            <p className="text-xm">Use your imagination to design it 😆</p>
          </div>
        </Modal>
      ) : webSocketState.gamestate?.command === commandType.GAME ? (
        <>
          {() => setOpenModalStateGame(true)}
          <Modal
            open={openmodalStateGame}
            onClose={() => setOpenModalStateGame(false)}
            header=""
          >
            <div className="flex flex-col justify-center items-center">
              <img
                src={GameStartGif}
                alt="running"
                className="rounded-3xl mb-5"
              />
              <p className="text-3xl text-gray-700 font-bold">Game Start</p>
              <p className="text-xm">
                Quickly press OK and go see your crew 🚩
              </p>
            </div>
          </Modal>
        </>
      ) : (
        ""
      )}

      {webSocketState.gamestate?.command === commandType.GAME &&
      webSocketState.gamestate?.nowturn === username &&
      !webSocketState.gamestate?.reusePlan ? (
        <>
          {() => setOpenModalTurnGame(true)}
          <Modal
            open={openmodalTurnGame}
            onClose={() => setOpenModalTurnGame(false)}
            header=""
          >
            <div className="flex flex-col justify-center items-center">
              <img src={TurnGif} alt="thinking" className="rounded-3xl mb-5" />
              <p className="text-3xl text-gray-700 font-bold">
                This is your turn
              </p>
              <p className="text-xm">Was your old plan bad? Think again 😱🤔</p>
            </div>
          </Modal>
        </>
      ) : (
        ""
      )}

      {starterHelpModal ? (
        <Modal
          open={starterHelpModal}
          size={"md"}
          onClose={() => {
            setOpenModal(false);
            setStarterModal(false);
          }}
          header="Welcome"
        >
          <>
            <div className="flex flex-row items-center overflow-y-auto max-h-96">
              <span className="text-black">
                First, this is button you should know
              </span>
              <span className="font-bold px-1 text-red-500">
                You can read this again in help button
              </span>
              <HelpIcon height={30} width={30} />
            </div>
            <div className="flex flex-row my-2 mt-5">
              <div className="px-20 justify-center flex items-center">
                <Checkbox
                  crossOrigin={undefined}
                  className="border-black"
                  defaultChecked
                  label={
                    <div>
                      <Typography
                        color="black"
                        className="font-medium"
                        placeholder={""}
                      >
                        Reuse Plan
                      </Typography>
                    </div>
                  }
                />
              </div>
              <div className="inline-block align-middle pl-6">
                <span className="text-black">
                  If you need to reuse old plan. Select this.
                </span>
                <br />
                <span className="text-blue-900">
                  (when your turn will auto eval construction plan)
                </span>
                <br />
                <span className="text-black">
                  If you need to sent new plan. Unselect this.
                </span>
                <br />
                <span className="text-pink-500">
                  (this checkbox will show after first turn)
                </span>
              </div>
            </div>
            <div className="flex flex-row my-2">
              <div className="px-11">
                <button
                  type="button"
                  className="bg-blue-900 text-white select-none hover:bg-white hover:ring hover:ring-blue-900 hover:text-blue-900 font-beyonders text-xm border px-6 py-3 rounded-3xl flex items-center justify-center mx-auto my-2"
                >
                  check plan
                </button>
              </div>
              <div className="inline-block align-middle mt-5">
                <span className="text-black">
                  Check your construction plan while not your turn.
                </span>
              </div>
            </div>
            <div className="flex flex-row">
              <div className="px-24">
                <button
                  type="button"
                  className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-3 rounded-3xl flex items-center justify-center mx-auto my-2"
                >
                  sent
                </button>
              </div>
              <div className="inline-block align-middle mt-2">
                <span className="text-black">Sent your construction plan.</span>
                <br />
                <span className="text-red-500">
                  (this button will not show if not your turn)
                </span>
              </div>
            </div>
          </>
        </Modal>
      ) : (
        ""
      )}

      {opCheckPlan ? (
        <Modal
          open={open}
          onClose={() => {
            setOpenModal(false);
            setOperateCheckplan(false);
          }}
          header="Message from server"
        >
          <span>{msgFromServer}</span>
        </Modal>
      ) : (
        ""
      )}

      <div className="flex flex-row items-start justify-start w-full mt-24 mb-3">
        <div className="font-beyonders select-none text-blue-900 drop-shadow-xl text-3xl text-center py-2 ml-5 mr-4">
          <h2>UPBEAT</h2>
        </div>
        <button
          onClick={() => {
            setStarterModal(true);
            setOpenModal(true);
          }}
        >
          <HelpIcon />
        </button>
        {/* <div className="ml-auto mr-10 flex flex-row">
          <div className="text-right mr-5">
            <h3 className="font-serif">You have time to</h3>
            <h3 className="font-serif">init your construction plan</h3>
          </div>
          <TimerCom
            initialMinutes={timeRev.min}
            initialSeconds={timeRev.sec}
            onReset={handleActiveRevPlan}
            isActive={isRevPlan}
            setIsActive={setIsRevPlan}
          ></TimerCom>
        </div> */}
      </div>
      <div className="flex flex-row w-full h-full justify-between">
        <div className="flex flex-col justify-start w-full h-full">
          <div className="bg-[#cecefd] flex flex-col h-3/6 px-5 py-5">
            <h2 className="font-beyonders text-black text-xm mb-4">Player</h2>
            <h4 className="font-concert text-black text-2xl pb-2">
              Name :{" "}
              {president !== undefined
                ? president?.name.toLocaleUpperCase()
                : "null"}
            </h4>
            <h4 className="font-concert text-black text-2xl pb-2">
              Budget :{" "}
              {president !== undefined
                ? Math.ceil(president?.budget).toLocaleString()
                : "null"}
            </h4>
            <h4 className="font-concert text-black text-2xl pb-2">
              My Regions :{" "}
              {president !== undefined
                ? Math.ceil(president?.ownCity).toLocaleString()
                : "null"}
            </h4>
            <div className="font-concert text-black text-2xl pb-2 flex flex-row">
              <span className="pr-3">Citycenter :</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="26" height="30">
                <polygon
                  points="12,2 24,24 2,24"
                  fill="lime"
                  style={{ strokeWidth: "0.8", stroke: "#000000" }}
                />
              </svg>
            </div>
            <div className="font-concert text-black text-2xl pb-2 flex flex-row">
              <span className="pr-3">Crew :</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="27" height="30">
                <circle
                  cx="14"
                  cy="18"
                  r="12"
                  fill={president?.crewColor}
                  style={{ strokeWidth: "0.8", stroke: "#000000" }}
                />
              </svg>
            </div>
          </div>

          <div className="bg-blue-800 font-concert flex flex-col h-1/6 px-5 py-5 justify-center">
            {reghover ? (
              <div className="text-white text-2xl">
                <h2>
                  Region Position : [{region?.row},{region?.col}]
                </h2>
                <span>
                  {"President : "}
                  {region?.president === null
                    ? "** No player **"
                    : region?.president.toUpperCase()}
                  <br></br>
                </span>
                <span>
                  {"Deposit : "}
                  {region?.president === username || region?.president === null
                    ? Math.ceil(region?.deposit)
                    : "*****"}
                </span>
              </div>
            ) : (
              <span className="text-white text-center align-middle text-2xl">
                [ Hover Hexagon To See Infomation ]
              </span>
            )}
          </div>

          <div className="bg-[#E6E6FA] flex flex-col h-2/6 px-5 py-5">
            <h4 className="font-beyonders text-black text-xm">Player turn</h4>
            <div className="flex flex-col my-3">
              {webSocketState.gamedata?.playerlist.map((player) => {
                return (
                  <div className="flex flex-row" key={player.name}>
                    <Circle color={player.playerColor} />
                    {webSocketState.gamestate ? (
                      webSocketState.gamestate.command === commandType.INIT ? (
                        player.constInit ? (
                          <ReadyIcon />
                        ) : (
                          <NotReadyIcon />
                        )
                      ) : null
                    ) : null}

                    <p className="ml-4 text-black font-concert text-center text-2xl align-middle mr-2">
                      {player.name.toUpperCase()}
                    </p>
                    {webSocketState.gamestate ? (
                      webSocketState.gamestate.command === commandType.GAME ? (
                        player.myTurn ? (
                          <span role="img" className="text-2xl">
                            👈
                          </span>
                        ) : (
                          ""
                        )
                      ) : null
                    ) : null}
                  </div>
                );
              })}
            </div>
          </div>
        </div>

        <div className="flex flex-col justify-items-center items-center">
          <div className="items-center h-4/6">
            <HexGrid
              width={960}
              height={600}
              style={{ border: "4px solid black", position: "relative" }}
            >
              <Layout
                size={{ x: 5, y: 5 }}
                flat={true}
                spacing={1.05}
                origin={{ x: 0, y: 0 }}
              >
                <g style={{ position: "absolute" }}>
                  {webSocketState.gamedata !== undefined
                    ? hexgrid9(webSocketState.gamedata).map((hex, i) => {
                        const regpresident =
                          webSocketState.gamedata?.playerlist.find(
                            (e) => e.name === hex.reg.president
                          );
                        const colorfill = regpresident
                          ? regpresident.playerColor
                          : "#E6E6FA";
                        return (
                          <Hexagon
                            key={i}
                            q={hex.q}
                            r={hex.r}
                            s={hex.s}
                            style={{
                              fill: colorfill,
                              strokeWidth: "0.1",
                              stroke: "#000000",
                              fillOpacity:
                                reghover &&
                                region ===
                                  webSocketState.gamedata?.territory[hex.i][
                                    hex.j
                                  ]
                                  ? 0.5
                                  : 1,
                              transition: "fill-opacity 0.15s ease",
                            }}
                            onMouseOver={() => {
                              setRegion(
                                webSocketState.gamedata?.territory[hex.i][hex.j]
                              );
                              setRegHover(true);
                            }}
                            onMouseLeave={() => setRegHover(false)}
                          >
                            {hex.reg.crew ? (
                              username === hex.reg.president ? (
                                <circle
                                  cx="0"
                                  cy="0"
                                  r="2.5"
                                  fill={regpresident?.crewColor}
                                />
                              ) : hex.reg.president === null ? (
                                <circle
                                  cx="0"
                                  cy="0"
                                  r="2.5"
                                  fill={whonowturn?.crewColor}
                                />
                              ) : (
                                ""
                              )
                            ) : (
                              ""
                            )}
                            {hex.reg.cityCenter &&
                            username === regpresident?.name ? (
                              <polygon points="0,-2.5 -2,2 2,2" fill="lime" />
                            ) : (
                              ""
                            )}
                            <Text
                              className="fill-gray-500"
                              style={{ fontSize: "1.5px", strokeOpacity: "0" }}
                            >
                              {hex.i},{hex.j}
                            </Text>
                          </Hexagon>
                        );
                      })
                    : ""}
                </g>
                <Pattern
                  id="pattern1"
                  link="https://picsum.photos/200?image=80"
                ></Pattern>
              </Layout>
            </HexGrid>
          </div>

          <div className="h-2/6 w-full justify-center items-center">
            <Editor
              height="100%"
              width="100%"
              theme="vs-dark"
              defaultValue={typedPlan}
              options={{ fontSize: 17 }}
              onChange={(e) => setTypedPlan(e)}
            />
          </div>
        </div>

        <div className="flex flex-col justify-end items-end w-full">
          <div className="w-full h-4/6 items-end bg-black">fdgd</div>
          <div className="bg-blue-100 w-full h-2/6 px-4 py-3 flex-col justify-center">
            <div className="flex items-center justify-center mx-auto">
              {checkboxReuseplan ? (
                <Checkbox
                  crossOrigin={undefined}
                  className="border-black"
                  defaultChecked
                  onClick={async () => {
                    setReusePlan(!reusePlan);
                    console.log(reusePlan);
                    await postReuseStatus(
                      JSON.stringify({
                        sender: username,
                        reuse: reusePlan,
                        status: "",
                      })
                    );
                  }}
                  label={
                    <div>
                      <Typography
                        color="black"
                        className="font-medium"
                        placeholder={""}
                      >
                        Reuse Plan
                      </Typography>
                    </div>
                  }
                />
              ) : null}
            </div>
            <button
              type="button"
              className="bg-blue-900 text-white select-none hover:bg-white hover:ring hover:ring-blue-900 hover:text-blue-900 font-beyonders text-xm border px-6 py-3 rounded-3xl flex items-center justify-center mx-auto my-2"
              onClick={async () => {
                await postConstCheck(typedPlan)
                  .then((res) => setMessageFromServer(res.data.result))
                  .catch((e) => alert(e));
                setOperateCheckplan(true);
                setOpenModal(true);
              }}
            >
              check plan
            </button>
            {webSocketState.gamestate?.command === commandType.INIT ||
            (webSocketState.gamestate?.nowturn === username &&
              !webSocketState.gamestate.reusePlan) ? (
              <button
                type="button"
                className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-3 rounded-3xl flex items-center justify-center mx-auto my-2"
                onClick={async () => {
                  if (webSocketState.gamestate?.command === commandType.INIT) {
                    await postInitConst(
                      JSON.stringify({
                        sender: username,
                        plan: typedPlan,
                        time_min:
                          window.localStorage.getItem("countdownMinutes"),
                        time_sec:
                          window.localStorage.getItem("countdownSeconds"),
                      })
                    )
                      .then((res) => setMessageFromServer(res.data.result))
                      .catch((e) => alert(e));
                    setOperateCheckplan(true);
                    setOpenModal(true);
                  } else if (
                    webSocketState.gamestate?.command === commandType.GAME
                  ) {
                    await postNewConst(
                      JSON.stringify({
                        sender: username,
                        plan: typedPlan,
                        time_min:
                          window.localStorage.getItem("countdownMinutes"),
                        time_sec:
                          window.localStorage.getItem("countdownSeconds"),
                      })
                    )
                      .then((res) => setMessageFromServer(res.data.result))
                      .catch((e) => alert(e));
                    setOperateCheckplan(true);
                    setOpenModal(true);
                  }
                }}
              >
                sent
              </button>
            ) : null}
          </div>
        </div>
      </div>
    </div>
  );
}

export default GamePage;
