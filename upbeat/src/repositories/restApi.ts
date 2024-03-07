import axios, { AxiosResponse } from "axios";
import { groupMessage } from "../store/Slices/webSocketSlice";

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

export function setServer(server: string | null): void {
  if (!server) return window.localStorage.removeItem("server");
  window.localStorage.setItem("server", server);
}
