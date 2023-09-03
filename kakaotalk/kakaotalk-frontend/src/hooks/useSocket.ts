import { State } from "@states/State";
import { useCallback, useEffect } from "react";
import { useRecoilCallback, useRecoilValue } from "recoil";

type Message = {
  type: string;
  text: string;
};

export default function useSocket() {
  const webSocket = useRecoilValue(State.webSocket);
  const messages = useRecoilValue(State.messages);

  const onMessage = useRecoilCallback(({ set }) => (event: MessageEvent) => {
    let message = JSON.parse(event.data);

    if (message.type === "DATA") {
      set(State.messages, message.messages);
    }
  });

  const onCloseOrError = useRecoilCallback(
    ({ set }) =>
      (eventOrError) => {
        console.log(eventOrError);
        set(State.webSocket, null);
      },
    []
  );

  useEffect(() => {
    if (!webSocket) {
      return;
    }

    const sendPingInterval = setInterval(() => {
      if (webSocket.readyState !== 1) {
        return;
      }
      webSocket.send(
        JSON.stringify({
          type: "PING",
          text: "",
        })
      );
    }, 20000);
    webSocket.addEventListener("close", onCloseOrError);
    webSocket.addEventListener("error", onCloseOrError);
    webSocket.addEventListener("message", onMessage);
    return () => {
      clearInterval(sendPingInterval);
      webSocket.removeEventListener("close", onCloseOrError);
      webSocket.removeEventListener("error", onCloseOrError);
      webSocket.removeEventListener("message", onMessage);
    };
  }, [onMessage, onCloseOrError, webSocket]);

  const getOrCreateSocket = useRecoilCallback(
    ({ set }) =>
      () => {
        const createWebsocket = () => {
          const url = process.env.REACT_APP_API_URL;
          return new WebSocket(`wss://${url}/ws`);
        };

        if (!webSocket) {
          const webSocket = createWebsocket();
          set(State.webSocket, webSocket);
          return webSocket;
        }
        return webSocket;
      },
    [webSocket]
  );

  const sendMessage = useCallback(
    (message: Message) => {
      const webSocket = getOrCreateSocket();
      const sendJoinMessage = () => {
        webSocket.send(JSON.stringify(message));
      };

      switch (webSocket.readyState) {
        case 0: {
          webSocket.addEventListener("open", () => {
            sendJoinMessage();
          });
          break;
        }
        case 1: {
          sendJoinMessage();
          break;
        }
        default:
          throw new Error("unreachable");
      }
    },
    [getOrCreateSocket]
  );

  const sendInput = useCallback(
    (inputText: string) => {
      if (!inputText) {
        return;
      }

      sendMessage({
        type: "DATA",
        text: inputText,
      });
    },
    [sendMessage]
  );

  return {
    webSocket,
    sendInput,
    messages,
  };
}
