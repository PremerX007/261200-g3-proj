import React, { useEffect, useState } from "react";
import { Input } from "@material-tailwind/react";
import {
  getInitConfig,
  getUserConfig,
  putUserConfig,
} from "../repositories/restApi.ts";

interface SettingPageProp {
  buttonState: React.Dispatch<React.SetStateAction<boolean>>;
}

interface config {
  m?: string;
  n?: string;
  init_plan_min?: string;
  init_plan_sec?: string;
  init_budget?: string;
  init_center_dep?: string;
  plan_rev_min?: string;
  plan_rev_sec?: string;
  rev_cost?: string;
  max_dep?: string;
  interest_pct?: string;
}

function SettingPage({ buttonState }: SettingPageProp) {
  async function callInitConfig() {
    await getInitConfig()
      .then((response) => {
        setUserConfig(response.data);
      })
      .catch((e) => console.log("error to call player api" + e));
  }

  async function callUserConfig() {
    await getUserConfig()
      .then((response) => {
        setUserConfig(response.data);
      })
      .catch((e) => console.log("error to call player api" + e));
  }

  function updateUserConfig(user: config | undefined) {
    putUserConfig(user)
      .then()
      .catch((e) => console.log("error to call player api" + e));
  }

  const [userConfig, setUserConfig] = useState<config>();
  useEffect(() => {
    callUserConfig();
  }, []);

  return (
    <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-5 backdrop-filter backdrop-blur-sm">
      <div className="bg-white p-8 rounded-2xl">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            updateUserConfig(userConfig);
            buttonState(false);
          }}
        >
          <button
            type="submit"
            className="bg-red-500 ml-auto rounded-md p-2 flex items-end justify-end text-white hover:text-red-500 hover:bg-white hover:ring-2 hover:ring-red-600"
          >
            <svg
              className="h-6 w-6"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
          <h3 className="pb-3 text-black font-concert text-center text-4xl align-middle">
            Game Setting
          </h3>
          <h4 className="text-black font-concert align-middle text-left text-xl py-3">
            Initial construction plan time
          </h4>
          <div className="flex flex-row gap-6">
            <Input
              required
              color="blue"
              label="min"
              type="number"
              crossOrigin=""
              value={userConfig?.init_plan_min}
              onChange={(e) => {
                setUserConfig((previous) => ({
                  ...previous,
                  init_plan_min: e.target.value,
                }));
              }}
            />
            <p className="text-center align-bottom text-2xl">:</p>
            <Input
              required
              color="blue"
              label="sec"
              type="number"
              crossOrigin=""
              value={userConfig?.init_plan_sec}
              onChange={(e) =>
                setUserConfig((previous) => ({
                  ...previous,
                  init_plan_sec: e.target.value,
                }))
              }
            />
          </div>
          <h4 className="text-black font-concert align-middle text-left text-xl py-3">
            Construction plan revisions time
          </h4>
          <div className="flex flex-row gap-6 mb-6">
            <Input
              required
              color="blue"
              label="min"
              type="number"
              crossOrigin=""
              value={userConfig?.plan_rev_min}
              onChange={(e) =>
                setUserConfig((previous) => ({
                  ...previous,
                  plan_rev_min: e.target.value,
                }))
              }
            />
            <p className="text-center align-bottom text-2xl">:</p>
            <Input
              required
              color="blue"
              label="sec"
              type="number"
              crossOrigin=""
              value={userConfig?.plan_rev_sec}
              onChange={(e) =>
                setUserConfig((previous) => ({
                  ...previous,
                  plan_rev_sec: e.target.value,
                }))
              }
            />
          </div>

          <div className="flex w-full flex-col gap-6">
            <Input
              required
              color="blue"
              label="Start plyer budget"
              type="number"
              crossOrigin=""
              value={userConfig?.init_budget}
              onChange={(e) =>
                setUserConfig((previous) => ({
                  ...previous,
                  init_budget: e.target.value,
                }))
              }
            />
            <Input
              required
              color="blue"
              label="Start city center deposit"
              type="number"
              crossOrigin=""
              value={userConfig?.init_center_dep}
              onChange={(e) =>
                setUserConfig((previous) => ({
                  ...previous,
                  init_center_dep: e.target.value,
                }))
              }
            />
            <Input
              required
              color="blue"
              label="Construction plan revision cost"
              type="number"
              crossOrigin=""
              value={userConfig?.rev_cost}
              onChange={(e) =>
                setUserConfig((previous) => ({
                  ...previous,
                  rev_cost: e.target.value,
                }))
              }
            />
            <Input
              required
              color="blue"
              label="Region maximum deposit"
              type="number"
              crossOrigin=""
              value={userConfig?.max_dep}
              onChange={(e) =>
                setUserConfig((previous) => ({
                  ...previous,
                  max_dep: e.target.value,
                }))
              }
            />
            <Input
              required
              color="blue"
              label="Interest rate %"
              type="number"
              crossOrigin=""
              value={userConfig?.interest_pct}
              onChange={(e) =>
                setUserConfig((previous) => ({
                  ...previous,
                  interest_pct: e.target.value,
                }))
              }
            />
          </div>

          <div className="items-center justify-center">
            <button
              type="button"
              className="bg-green-500 text-white select-none hover:bg-white hover:ring hover:ring-green-600 hover:text-green-500 font-beyonders text-xm border px-6 py-2 rounded-3xl flex items-center justify-center mx-auto mt-5"
              onClick={callInitConfig}
            >
              RESET
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SettingPage;
export type { config };
