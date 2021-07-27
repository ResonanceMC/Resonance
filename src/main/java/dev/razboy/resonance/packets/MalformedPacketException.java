package dev.razboy.resonance.packets;

import org.json.JSONException;

public class MalformedPacketException extends JSONException {
    public MalformedPacketException(String message) {
        super(message);
    }

    public MalformedPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedPacketException(Throwable cause) {
        super(cause);
    }
}
