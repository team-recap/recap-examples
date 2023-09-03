import { atom } from "recoil";

export type Message = {
  sender: string;
  senderProfileImageUrl: string;
  originalMessage: string;
  summarizedMessage: string;
};

export enum Tab {
  Input,
  Summary,
}

export const State = {
  webSocket: atom<WebSocket | null>({
    key: "GlobalState.websocket",
    default: null,
  }),
  messages: atom<Message[]>({
    key: "GlobalState.messages",
    default: [],
  }),
  tab: atom<Tab>({
    key: "GlobalState.tab",
    default: Tab.Input,
  }),
};
