package dev.razboy.resonance.packets.serverbound.play;

import dev.razboy.resonance.packets.MalformedPacketException;
import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.serverbound.ServerBoundPacket;
import org.json.JSONObject;

public class PeerRelayIceCandidatePacket extends ServerBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.PEER_RELAY_ICE_CANDIDATE;
    }
    private String peerId;
    private JSONObject iceCandidate;
    @Override
    public void readJson(JSONObject json) throws MalformedPacketException {
        peerId = json.getString("peer_id");
        JSONObject body = json.getJSONObject("body");
        iceCandidate = body.getJSONObject("ice_candidate");
    }

    public String getPeerId() {
        return peerId;
    }

    public JSONObject getIceCandidate() {
        return iceCandidate;
    }
    @Override
    public String repr() {
        return getClass().getSimpleName()  + "(peerId=" + peerId + ", peer=" + iceCandidate.toString() + ")";
    }
}
