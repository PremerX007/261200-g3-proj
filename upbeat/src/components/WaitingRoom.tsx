import React from "react";

const Circle = ({ color }: { color: string }) => {
  // const [count, setCount] = useState(0);

  return (
    <svg height="30" width="30">
      <circle cx="15" cy="15" r="15" fill={color} />
    </svg>
  );
};

function WaitingRoom() {
  return (
    <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-5">
      <div className="bg-blue-200 p-8 rounded-2xl shadow-xl">
        {/* Your login form or content goes here */}
        <h2 className="font-beyonders select-none text-blue-900 drop-shadow-xl text-2xl text-center py-3 mx-60 align-top">
          GAME ROOM
        </h2>

        <div className="flex flex-row my-5">
          <Circle color="red" />
          <h3 className="ml-7 text-black font-bold font-concert text-center text-2xl align-middle">
            Premer
          </h3>
        </div>

        <div className="flex flex-row items-center justify-center">
          <button
            type="submit"
            className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-4 rounded-3xl flex items-center justify-center mx-5"
          >
            ready
          </button>
        </div>
      </div>
    </div>
  );
}

export default WaitingRoom;
