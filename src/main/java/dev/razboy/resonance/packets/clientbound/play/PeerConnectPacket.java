package dev.razboy.resonance.packets.clientbound.play;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONObject;

public class PeerConnectPacket extends ClientBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.PEER_CONNECT;
    }
    @Override
    public String repr() {
        return getClass().getSimpleName() + "(user=" + user.toString() + ")";
    }
    private JSONObject user;

    public void setUser(JSONObject user) {
        this.user = user;
    }


    @Override
    public String read() {
        return withIdActionBody(user).toString();
    }
}
