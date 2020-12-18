import RSocketWebSocketClient from "rsocket-websocket-client";
import {
  RSocketClient,
  MESSAGE_RSOCKET_COMPOSITE_METADATA,
  BufferEncoders,
  MAX_STREAM_ID,
} from "rsocket-core";
import Metadata from "./metadata";
import {useState} from "react";

const wsUrl = "ws://localhost:7000";
const randomJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
    "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
    "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
const responseRoute = "response";
const streamRoute = "stream";

function Test() {
  const [client, setClient] = useState(null);
  const [socket, setSocket] = useState(null);

  const [msgCompositeResponse, setMsgCompositeResponse] = useState("");
  const [msgCompositeStream, setMsgCompositeStream] = useState("");

  function compositeStreamRequest(socket, callback) {
    // requestStream never triggers the authenticate method
    const metadata = new Metadata({
      route: streamRoute,
      auth: {type: "bearer", token: randomJwt}
    }).toMetadata();
    socket.requestStream({
      metadata
    }).subscribe({
      "onComplete": () => callback("composite request stream onComplete"),
      "onError": (error) => callback("composite request stream onError: " + String(error)),
      "onNext": (streamMessage) => callback("composite request stream onNext"),
      "onSubscribe": (sub) => {
        callback("composite request stream onSubscribe");
        sub.request(MAX_STREAM_ID)
      }
    });
  }

  function compositeResponseRequest(socket, callback) {
    // request response triggers the authenticate method
    const metadata = new Metadata({
      route: responseRoute,
      auth: {type: "bearer", token: randomJwt}
    }).toMetadata();
    socket.requestResponse({
      metadata
    }).subscribe({
      "onComplete": () => callback("composite request response onComplete"),
      "onError": (error) => callback("composite request response onError: " + String(error) ),
      "onNext": (streamMessage) => callback("composite request response onNext"),
      "onSubscribe": (sub) => callback("composite request response onSubscribe")
    });
  }

  if(client == null) {
    const c = new RSocketClient({
      "setup": {
        "keepAlive": 120000,
        "lifetime": 180000,
        "dataMimeType": "application/json",
        "metadataMimeType": MESSAGE_RSOCKET_COMPOSITE_METADATA.string
      },
      "transport": new RSocketWebSocketClient({
        "url": wsUrl
      }, BufferEncoders)
    });
    setClient(c);
    c.connect().then(
        (socket) => {
          setSocket(socket);
          console.log("composite connection succeeded!");
        },
        (error) => {
          console.log("composite initial connection failed");
        }
    );
    console.log("connecting!");
  }
  if(socket != null && client != null) {
    return <div>
      <button onClick={() => compositeStreamRequest(socket, (msg) => {setMsgCompositeStream(msg)})}>Composite Stream</button>
      <div>{msgCompositeStream}</div>
      <button onClick={() => compositeResponseRequest(socket, (msg) => {setMsgCompositeResponse(msg)})}>Composite Response</button>
      <div>{msgCompositeResponse}</div>
    </div>;
  } else {
    return <div>Connecting!</div>;
  }
}

export default Test;
