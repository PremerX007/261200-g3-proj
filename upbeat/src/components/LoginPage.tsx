import { useState } from "react";
import AnimateLogo from "./AnimateLogo";

function LoginPage() {
  const [username, setUsername] = useState<string>("");

  return (
    <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-5">
      <div className="bg-blue-200 p-8 rounded-2xl shadow-xl">
        {/* Your login form or content goes here */}
        <AnimateLogo />
        <h2 className="font-beyonders select-none text-blue-900 drop-shadow-xl text-5xl text-center mx-auto my-5 py-3">
          UPBEAT
        </h2>

        <div className="my-7 px-6 items-center justify-center">
          <input
            type="text"
            placeholder="Enter your name"
            className="block w-full px-4 py-2 border rounded-2xl outline outline-offset-2 outline-blue-900"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>

        <div className="flex flex-row">
          <button
            type="submit"
            className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-4 rounded-3xl flex items-center justify-center mx-5"
          >
            Login
          </button>
          <button
            type="submit"
            className="bg-red-500 text-white select-none hover:bg-white hover:ring hover:ring-red-600 hover:text-red-500 font-beyonders text-xm border px-4 py-2 rounded-3xl flex items-center justify-center mx-5"
          >
            Game Setting
          </button>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
