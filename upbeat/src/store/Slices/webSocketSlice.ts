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
}

interface group {
  list: webSocketMessage[] | undefined;
}

interface webSocketState {
  isConnected: boolean;
  stompClient: Stomp.Client | undefined;
  messages: webSocketMessage[] | undefined;
  onetime: webSocketMessage[] | undefined;
}

const initialState: webSocketState = {
  isConnected: false,
  stompClient: undefined,
  messages: [],
  onetime: [],
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
    setOnetime: (state, action: PayloadAction<webSocketMessage>) => {
      state.onetime = [];
      state.onetime?.push(action.payload);
    },
  },
});

export const { setIsConnected, appendMessage, setStompClient } =
  webSocketSlice.actions;
export default webSocketSlice.reducer;
export const selectWebSocket = (state: RootState) => state.webSocket;
