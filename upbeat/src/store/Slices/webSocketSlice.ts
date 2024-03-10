import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store.ts";
import Stomp from "stompjs";

export enum messageType {
  COMMAND = "COMMAND",
  JOIN = "JOIN",
  LEAVE = "LEAVE",
  READY = "READY",
  NOTREADY = "NOTREADY",
}
interface webSocketMessage {
  sender: string;
  content: string;
  timestamp: string;
  type: messageType;
  admin: boolean;
}

interface webSocketState {
  isConnected: boolean;
  stompClient: Stomp.Client | undefined;
  messages: webSocketMessage[] | undefined;
  onetime: groupMessage | undefined;
  gameStart: boolean;
}

interface groupMessage {
  arr: webSocketMessage[] | undefined;
  empty: boolean;
}

const initialState: webSocketState = {
  isConnected: false,
  stompClient: undefined,
  messages: [],
  onetime: undefined,
  gameStart: false,
};

export const webSocketSlice = createSlice({
  name: "webSocket",
  initialState,
  reducers: {
    setIsConnected: (state, action: PayloadAction<boolean>) => {
      state.isConnected = action.payload;
    },
    appendMessage: (state, action: PayloadAction<webSocketMessage>) => {
      state.messages?.push(action.payload);
    },
    setStompClient: (state, action: PayloadAction<Stomp.Client>) => {
      state.stompClient = action.payload;
    },
    setStatusMessage: (state, action: PayloadAction<groupMessage>) => {
      state.onetime = action.payload;
    },
    setGameStart: (state, action: PayloadAction<boolean>) => {
      state.gameStart = action.payload;
    },
  },
});

export const {
  setIsConnected,
  appendMessage,
  setStompClient,
  setStatusMessage,
  setGameStart,
} = webSocketSlice.actions;
export default webSocketSlice.reducer;
export type { groupMessage };
export const selectWebSocket = (state: RootState) => state.webSocket;
