package dev.razboy.resonance.packets.clientbound.play;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONObject;

public class OPeerRelaySessionDescPacket extends ClientBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.PEER_RELAY_SESSION_DESC;
    }

    private String peerId;
    private JSONObject description;

    public void setDescription(JSONObject description) {
        this.description = description;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }

    @Override
    public String read() {
        return withIdActionBody(new JSONObject()
                .put("sessionDescription", description)
        ).put("peerId", peerId)
                .toString();
    }
    @Override
    public String repr() {
        return getClass().getSimpleName()  + "(peerId=" + peerId + ", description=" + description.toString() + ")";
    }
}
