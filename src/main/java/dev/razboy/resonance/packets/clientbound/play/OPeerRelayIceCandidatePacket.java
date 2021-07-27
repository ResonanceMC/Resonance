package dev.razboy.resonance.packets.clientbound.play;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONObject;

public class OPeerRelayIceCandidatePacket extends ClientBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.PEER_RELAY_ICE_CANDIDATE;
    }

    private String peerId;
    private JSONObject iceCandidate;

    public void setIceCandidate(JSONObject iceCandidate) {
        this.iceCandidate = iceCandidate;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }

    @Override
    public String read() {
        return withIdActionBody(new JSONObject()
                .put("ice_candidate", iceCandidate)
        ).put("peer_id", peerId)
                .toString();
    }
    @Override
    public String repr() {
        return getClass().getSimpleName()  + "(peerId=" + peerId + ", peer=" + iceCandidate.toString() + ")";
    }
}
