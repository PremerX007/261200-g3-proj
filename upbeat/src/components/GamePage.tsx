import React, { useState } from "react";
import { HexGrid, Layout, Hexagon, Text, Pattern } from "react-hexgrid";

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

  const [type, setType] = useState<string>("");

  return (
    <div className="h-screen w-screen flex flex-col justify-center items-center">
      <div className="w-full h-1/4">
        <h2 className="font-beyonders select-none text-blue-900 drop-shadow-xl text-5xl text-center mx-auto my-5 py-3">
          UPBEAT
        </h2>
      </div>
      <div className="h-4/5 w-screen flex flex-row justify-center items-center">
        <div className="w-1/6 h-full bg-blue-500 flex flex-col justify-center items-center">
          <h4 className="font-beyonders text-white text-xm my-5">Player 1</h4>
        </div>
        <HexGrid width={960} height={700} style={{ border: "5px solid black" }}>
          <Layout
            size={{ x: 5, y: 5 }}
            flat={true}
            spacing={1.05}
            origin={{ x: 0, y: 0 }}
          >
            {hexgrid9.map((hex, i) => (
              <Hexagon
                key={i}
                q={hex.q}
                r={hex.r}
                s={hex.s}
                style={{ fill: "#E6E6FA" }}
              >
                <Text className="fill-gray-800" style={{ fontSize: "3px" }}>
                  {hex.i},{hex.j}
                </Text>
              </Hexagon>
            ))}
          </Layout>
        </HexGrid>
      </div>
      <div className="h-screen w-screen flex flex-row justify-center items-center">
        <div className="w-1/6 h-full bg-blue-100 flex flex-col justify-center items-center">
          <h4 className="font-beyonders text-black text-xm my-5">
            Player List
          </h4>
        </div>
        <div
          className="h-full bg-blue-300 flex flex-col justify-center items-center"
          style={{ width: "60rem" }}
        >
          <form
            className="flex flex-row justify-center items-center"
            style={{ width: "50rem" }}
            onSubmitCapture={(e) => {
              e.preventDefault();
            }}
          >
            <div className="flex flex-row justify-centerv items-center w-full">
              <div className="my-7 px-6 items-center justify-center w-full">
                <input
                  type="text"
                  placeholder="Enter your command here..."
                  className="block w-full px-4 py-2 border rounded-2xl outline outline-offset-2 outline-blue-900"
                  value={type}
                  onChange={(e) => setType(e.target.value)}
                  required
                />
              </div>

              <div className="flex flex-row justify-center">
                <button
                  type="submit"
                  className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xs border px-6 py-4 rounded-3xl flex items-center justify-center mx-2 my-4"
                >
                  Submit
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default GamePage;
