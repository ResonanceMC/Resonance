package dev.razboy.resonance.packets.serverbound.play;

import dev.razboy.resonance.packets.MalformedPacketException;
import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.serverbound.ServerBoundPacket;
import org.json.JSONObject;

public class PeerRelaySessionDescPacket extends ServerBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.PEER_RELAY_SESSION_DESC;
    }
    private String peerId;
    private JSONObject description;
    @Override
    public void readJson(JSONObject json) throws MalformedPacketException {
        peerId = json.getString("peerId");
        JSONObject body = json.getJSONObject("body");
        description = body.getJSONObject("sessionDescription");
    }

    public String getPeerId() {
        return peerId;
    }

    public JSONObject getDescription() {
        return description;
    }
    @Override
    public String repr() {
        return getClass().getSimpleName()  + "(peerId=" + peerId + ", description=" + description.toString() + ")";
    }
}
