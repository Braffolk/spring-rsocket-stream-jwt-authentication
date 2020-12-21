import {
    MESSAGE_RSOCKET_ROUTING,
    encodeAndAddCustomMetadata, encodeAndAddWellKnownMetadata
} from "rsocket-core";

function bearerToken(token) {
    const buffer = Buffer.alloc(1 + token.length);
    buffer.writeUInt8(1 | 0x80, 0);
    buffer.write(token, 1, "utf-8");
    return buffer;
}

function encodeRoute(route) {
    const length = Buffer.byteLength(route, 'utf8');
    const buffer = Buffer.alloc(1);
    buffer.writeInt8(length);
    return Buffer.concat([buffer, Buffer.from(route, 'utf8')]);
}

export default class Metadata {
    constructor(json) {
        this.route = json['route'];
        this.auth = json['auth'];
    }

    toMetadata() {
        let metadata = Buffer.alloc(0);
        if (this.auth && this.auth["type"] === 'bearer') {
            metadata = encodeAndAddCustomMetadata(
                metadata,
                "message/x.rsocket.authentication.v0",
                bearerToken(this.auth["token"]),
            );
        }
        if (this.route) {
            metadata = encodeAndAddWellKnownMetadata(
                metadata,
                MESSAGE_RSOCKET_ROUTING,
                encodeRoute(this.route)
            );
        }
        return metadata;
    }
}