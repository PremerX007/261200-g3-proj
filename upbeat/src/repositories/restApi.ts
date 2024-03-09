import axios, { AxiosResponse } from "axios";
import { groupMessage } from "../store/Slices/webSocketSlice";
import { config } from "../components/SettingPage";
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

export async function getInitConfig(): Response<config> {
  return getAxiosInstance().get(`/game/config/reset`);
}

export async function getUserConfig(): Response<config> {
  return getAxiosInstance().get(`/game/config`);
}

export function putUserConfig(user: config | undefined): Response<config> {
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
