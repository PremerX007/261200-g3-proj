import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store.ts";
import Stomp from "stompjs";

export enum messageType {
  COMMAND = "COMMAND",
  JOIN = "JOIN",
  LEAVE = "LEAVE",
  READY = "READY",
  NOTREADY = "NOTREADY",
  START = "START",
}

export enum commandType {
  INIT = "INIT",
  GAME = "GAME",
  END = "END",
}

interface GameState {
  command: commandType;
  nowturn: string;
}

interface GameData {
  playerlist: player[];
  territory: region[][];
}

interface player {
  name: string;
  ownCity: number;
  budget: number;
  lose: boolean;
  color: string;
  constInit: boolean;
  myTurn: boolean;
  useOldStatement: boolean;
}

interface territory {
  reg: region[][];
}

interface region {
  row: number;
  col: number;
  president: string;
  deposit: number;
  interest: number;
  cityCenter: boolean;
  crew: boolean;
}

interface webSocketMessage {
  sender: string;
  content: string;
  timestamp: string;
  type: messageType;
  admin: boolean;
  turn: boolean;
}

interface groupMessage {
  user: webSocketMessage[] | undefined;
  readyPerson: number;
  start: boolean;
}

interface webSocketState {
  isConnected: boolean;
  stompClient: Stomp.Client | undefined;
  messages: webSocketMessage[] | undefined;
  onetime: groupMessage | undefined;
  gameStart: boolean;
  gamedata: GameData | undefined;
  gamestate: GameState | undefined;
}

const initialState: webSocketState = {
  isConnected: false,
  stompClient: undefined,
  messages: [],
  onetime: undefined,
  gameStart: false,
  gamedata: undefined,
  gamestate: undefined,
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
      state.gameStart = action.payload.start;
    },
    setGameData: (state, action: PayloadAction<GameData>) => {
      state.gamedata = action.payload;
      console.log(state.gamedata);
    },
    setGameStateSignal: (state, action: PayloadAction<GameState>) => {
      state.gamestate = action.payload;
    },
  },
});

export const {
  setIsConnected,
  appendMessage,
  setStompClient,
  setStatusMessage,
  setGameData,
  setGameStateSignal,
} = webSocketSlice.actions;
export default webSocketSlice.reducer;
export type { groupMessage, GameData, player };
export const selectWebSocket = (state: RootState) => state.webSocket;
