package dev.razboy.resonance.packets.serverbound.auth;

import dev.razboy.resonance.packets.MalformedPacketException;
import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.serverbound.ServerBoundPacket;
import org.json.JSONObject;

public class LogoutPacket extends ServerBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.LOGOUT;
    }
    @Override
    public String repr() {
        return getClass().getSimpleName() + "()";
    }

    @Override
    public void readJson(JSONObject json) throws MalformedPacketException {

    }


}
