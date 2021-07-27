package dev.razboy.resonance.packets.serverbound.auth;

import dev.razboy.resonance.packets.MalformedPacketException;
import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.serverbound.ServerBoundPacket;
import org.json.JSONObject;

public class UserInfoPacket extends ServerBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.USER_INFO;
    }

    private String token;
    public String getToken() {
        return token;
    }


    @Override
    public String repr() {
        return getClass().getSimpleName() + "(token=" + token + ")";
    }

    @Override
    public void readJson(JSONObject json) throws MalformedPacketException {
        token = json.getString("bearer");
    }
}
