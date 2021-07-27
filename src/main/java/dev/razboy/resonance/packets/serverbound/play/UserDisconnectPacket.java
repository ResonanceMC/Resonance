package dev.razboy.resonance.packets.serverbound.play;

import dev.razboy.resonance.packets.MalformedPacketException;
import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.serverbound.ServerBoundPacket;
import org.json.JSONObject;

public class UserDisconnectPacket extends ServerBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.USER_DISCONNECT;
    }

    @Override
    public void readJson(JSONObject json) throws MalformedPacketException {}
}
