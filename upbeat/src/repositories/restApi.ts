import axios, { AxiosResponse } from "axios";
import { groupMessage } from "../store/Slices/webSocketSlice";
import { gameConfig } from "../components/SettingPage";

interface constRest {
  result?: string;
}

export type Response<T> = Promise<AxiosResponse<T>>;
function getAxiosInstance() {
  return axios.create({ baseURL: getServer() });
}

export function getServer(): string {
  return window.localStorage.getItem("server") || import.meta.env.VITE_SERVER;
}

export async function getPlayer(): Response<groupMessage> {
  return getAxiosInstance().get(`/player`);
}

export async function getInitConfig(): Response<gameConfig> {
  return getAxiosInstance().get(`/game/config/reset`);
}

export async function getUserConfig(): Response<gameConfig> {
  return getAxiosInstance().get(`/game/config`);
}

export async function postInitConst(
  msg: string | undefined
): Response<constRest> {
  return getAxiosInstance().post(`/game/plan/init`, msg, {
    headers: { "Content-Type": "application/json" },
  });
}
export async function postNewConst(
  msg: string | undefined
): Response<constRest> {
  return getAxiosInstance().post(`/game/plan/setter`, msg, {
    headers: { "Content-Type": "application/json" },
  });
}

export async function postConstCheck(
  msg: string | undefined
): Response<constRest> {
  return getAxiosInstance().post(`/game/plan/check`, msg, {
    headers: { "Content-Type": "text/plain" },
  });
}

export function putUserConfig(
  user: gameConfig | undefined
): Response<gameConfig> {
  const userConfig = JSON.stringify({
    m: user?.m,
    n: user?.n,
    init_plan_min: user?.init_plan_min,
    init_plan_sec: user?.init_plan_sec,
    init_budget: user?.init_budget,
    init_center_dep: user?.init_center_dep,
    plan_rev_min: user?.plan_rev_min,
    plan_rev_sec: user?.plan_rev_sec,
    rev_cost: user?.rev_cost,
    max_dep: user?.max_dep,
    interest_pct: user?.interest_pct,
  });
  return getAxiosInstance().put(`/game/config/set`, userConfig, {
    headers: { "Content-Type": "application/json" },
  });
}

export function setServer(server: string | null): void {
  if (!server) return window.localStorage.removeItem("server");
  window.localStorage.setItem("server", server);
}

export type { constRest };
