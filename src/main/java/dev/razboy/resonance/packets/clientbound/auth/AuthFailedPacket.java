package dev.razboy.resonance.packets.clientbound.auth;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONObject;

public class AuthFailedPacket extends ClientBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.AUTH_FAILED;
    }

    @Override
    public String repr() {
        return getClass().getSimpleName() + "()";
    }


    @Override
    public String read() {
        return withIdAction().toString();
    }
}
