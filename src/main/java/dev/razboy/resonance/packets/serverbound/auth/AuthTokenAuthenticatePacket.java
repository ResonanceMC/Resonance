package dev.razboy.resonance.packets.serverbound.auth;

import dev.razboy.resonance.packets.MalformedPacketException;
import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.serverbound.ServerBoundPacket;
import org.json.JSONObject;

public class AuthTokenAuthenticatePacket extends ServerBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.AUTHENTICATE;
    }

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String repr() {
        return getClass().getSimpleName() + "(token=" + authToken + ")";
    }


    @Override
    public void readJson(JSONObject json) throws MalformedPacketException {
        JSONObject body = json.getJSONObject("body");
        authToken = body.get("token").toString();
    }
}
