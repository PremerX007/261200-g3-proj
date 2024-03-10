import React, { useRef, useState } from "react";
import { HexGrid, Layout, Hexagon, Text, Pattern } from "react-hexgrid";
import { Editor, Monaco } from "@monaco-editor/react";
import { constRest, postConstCheck } from "../repositories/restApi";
import Modal from "./Modal";
import Logo from "../assets/icon.png";

function GamePage() {
  const hexgrid9 = [
    // columm1
    { q: -4, r: -2, s: 6, i: 1, j: 1 },
    { q: -4, r: -1, s: 5, i: 2, j: 1 },
    { q: -4, r: 0, s: 4, i: 3, j: 1 },
    { q: -4, r: 1, s: 3, i: 4, j: 1 },
    { q: -4, r: 2, s: 2, i: 5, j: 1 },
    { q: -4, r: 3, s: 1, i: 6, j: 1 },
    { q: -4, r: 4, s: 0, i: 7, j: 1 },
    { q: -4, r: 5, s: -1, i: 8, j: 1 },
    { q: -4, r: 6, s: -2, i: 9, j: 1 },
    // columm2
    { q: -3, r: -3, s: 6, i: 1, j: 2 },
    { q: -3, r: -2, s: 5, i: 2, j: 2 },
    { q: -3, r: -1, s: 4, i: 3, j: 2 },
    { q: -3, r: 0, s: 3, i: 4, j: 2 },
    { q: -3, r: 1, s: 2, i: 5, j: 2 },
    { q: -3, r: 2, s: 1, i: 6, j: 2 },
    { q: -3, r: 3, s: 0, i: 7, j: 2 },
    { q: -3, r: 4, s: -1, i: 8, j: 2 },
    { q: -3, r: 5, s: -2, i: 9, j: 2 },
    // columm3
    { q: -2, r: -3, s: 5, i: 1, j: 3 },
    { q: -2, r: -2, s: 4, i: 2, j: 3 },
    { q: -2, r: -1, s: 3, i: 3, j: 3 },
    { q: -2, r: 0, s: 2, i: 4, j: 3 },
    { q: -2, r: 1, s: 1, i: 5, j: 3 },
    { q: -2, r: 2, s: 0, i: 6, j: 3 },
    { q: -2, r: 3, s: -1, i: 7, j: 3 },
    { q: -2, r: 4, s: -2, i: 8, j: 3 },
    { q: -2, r: 5, s: -3, i: 9, j: 3 },
    // columm4
    { q: -1, r: -4, s: 5, i: 1, j: 4 },
    { q: -1, r: -3, s: 4, i: 2, j: 4 },
    { q: -1, r: -2, s: 3, i: 3, j: 4 },
    { q: -1, r: -1, s: 2, i: 4, j: 4 },
    { q: -1, r: 0, s: 1, i: 5, j: 4 },
    { q: -1, r: 1, s: 0, i: 6, j: 4 },
    { q: -1, r: 2, s: -1, i: 7, j: 4 },
    { q: -1, r: 3, s: -2, i: 8, j: 4 },
    { q: -1, r: 4, s: -3, i: 9, j: 4 },
    // columm5
    { q: 0, r: -4, s: 4, i: 1, j: 5 },
    { q: 0, r: -3, s: 3, i: 2, j: 5 },
    { q: 0, r: -2, s: 2, i: 3, j: 5 },
    { q: 0, r: -1, s: 1, i: 4, j: 5 },
    { q: 0, r: 0, s: 0, i: 5, j: 5 },
    { q: 0, r: 1, s: -1, i: 6, j: 5 },
    { q: 0, r: 2, s: -2, i: 7, j: 5 },
    { q: 0, r: 3, s: -3, i: 8, j: 5 },
    { q: 0, r: 4, s: -4, i: 9, j: 5 },
    // columm6
    { q: 1, r: -5, s: 4, i: 1, j: 6 },
    { q: 1, r: -4, s: 3, i: 2, j: 6 },
    { q: 1, r: -3, s: 2, i: 3, j: 6 },
    { q: 1, r: -2, s: 1, i: 4, j: 6 },
    { q: 1, r: -1, s: 0, i: 5, j: 6 },
    { q: 1, r: 0, s: -1, i: 6, j: 6 },
    { q: 1, r: 1, s: -2, i: 7, j: 6 },
    { q: 1, r: 2, s: -3, i: 8, j: 6 },
    { q: 1, r: 3, s: -4, i: 9, j: 6 },
    // columm7
    { q: 2, r: -5, s: 3, i: 1, j: 7 },
    { q: 2, r: -4, s: 2, i: 2, j: 7 },
    { q: 2, r: -3, s: 1, i: 3, j: 7 },
    { q: 2, r: -2, s: 0, i: 4, j: 7 },
    { q: 2, r: -1, s: -1, i: 5, j: 7 },
    { q: 2, r: 0, s: -2, i: 6, j: 7 },
    { q: 2, r: 1, s: -3, i: 7, j: 7 },
    { q: 2, r: 2, s: -4, i: 8, j: 7 },
    { q: 2, r: 3, s: -5, i: 9, j: 7 },
    // columm8
    { q: 3, r: -6, s: 3, i: 1, j: 8 },
    { q: 3, r: -5, s: 2, i: 2, j: 8 },
    { q: 3, r: -4, s: 1, i: 3, j: 8 },
    { q: 3, r: -3, s: 0, i: 4, j: 8 },
    { q: 3, r: -2, s: -1, i: 5, j: 8 },
    { q: 3, r: -1, s: -2, i: 6, j: 8 },
    { q: 3, r: 0, s: -3, i: 7, j: 8 },
    { q: 3, r: 1, s: -4, i: 8, j: 8 },
    { q: 3, r: 2, s: -5, i: 9, j: 8 },
    // columm9
    { q: 4, r: -6, s: 2, i: 1, j: 9 },
    { q: 4, r: -5, s: 1, i: 2, j: 9 },
    { q: 4, r: -4, s: 0, i: 3, j: 9 },
    { q: 4, r: -3, s: -1, i: 4, j: 9 },
    { q: 4, r: -2, s: -2, i: 5, j: 9 },
    { q: 4, r: -1, s: -3, i: 6, j: 9 },
    { q: 4, r: 0, s: -4, i: 7, j: 9 },
    { q: 4, r: 1, s: -5, i: 8, j: 9 },
    { q: 4, r: 2, s: -6, i: 9, j: 9 },
  ];

  const Circle = ({ color }: { color: string }) => {
    return (
      <svg height="30" width="26">
        <circle cx="14" cy="18" r="12" fill={color} />
      </svg>
    );
  };

  const Circle2 = ({ color }: { color: string }) => {
    return (
      <svg>
        <circle cx="0" cy="0" r="3" fill={color} />
      </svg>
    );
  };
  const [type, setType] = useState<string>("");
  const [opCheckPlan, setOperateCheckplan] = useState<boolean>(false);
  const [open, setOpenModal] = useState<boolean>(true);
  const [msgFromServer, setMessageFromServer] = useState<string | undefined>(
    ""
  );
  const [typedPlan, setTypedPlan] = useState<string | undefined>(
    "# Write your construction plan here"
  );

  return (
    <div className="h-screen w-screen flex flex-col justify-center items-center bg-white">
      {opCheckPlan ? (
        <Modal
          open={open}
          onClose={() => setOpenModal(false)}
          header="Message from server"
          content={msgFromServer}
        ></Modal>
      ) : (
        ""
      )}
      ;
      <div className="mx-auto mt-24 mb-5">
        <h2 className="font-beyonders select-none text-blue-900 drop-shadow-xl text-3xl text-center mx-auto">
          UPBEAT
        </h2>
      </div>
      <div className="flex flex-row w-full h-full justify-between">
        <div className="flex flex-col justify-start w-full h-full">
          <div className="bg-blue-500 flex flex-col h-4/6 px-5 py-5">
            <h4 className="font-beyonders text-white text-xm">Player</h4>
          </div>

          <div className="bg-blue-100 flex flex-col h-2/6 px-5 py-5">
            <h4 className="font-beyonders text-black text-xm">Player turn</h4>
            <div className="flex flex-row my-3">
              <Circle color="blue" />
              <h3 className="ml-4 text-black font-concert text-center text-2xl align-middle">
                {/* {message.sender.toUpperCase()} */}
                Premer
              </h3>
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
                  {hexgrid9.map((hex, i) => (
                    <Hexagon
                      key={i}
                      q={hex.q}
                      r={hex.r}
                      s={hex.s}
                      style={{
                        fill: "#E6E6FA",
                        strokeWidth: "0.1",
                        stroke: "#000000",
                      }}
                      onMouseOver={() => {
                        console.log(hex.i + " " + hex.j);
                      }}
                    >
                      {/* <circle cx="0" cy="0" r="2.5" fill="red" /> */}
                      {/* <polygon points="0,-2.5 -2,2 2,2" fill="lime" /> */}
                      <Text
                        className="fill-gray-500"
                        style={{ fontSize: "1.5px", strokeOpacity: "0" }}
                      >
                        {hex.i},{hex.j}
                      </Text>
                    </Hexagon>
                  ))}
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
          <div className="bg-blue-100 w-full h-2/6 px-4 py-11 flex flex-col items-center">
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
            <button
              type="button"
              className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-3 rounded-3xl flex items-center justify-center mx-auto my-2"
              onClick={() => console.log(typedPlan)}
            >
              sent
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default GamePage;
