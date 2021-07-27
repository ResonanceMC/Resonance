package dev.razboy.resonance.packets.clientbound.auth;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONObject;

public class AuthenticatedPacket extends ClientBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.AUTHENTICATED;
    }


    private String token;
    private JSONObject user;



    @Override
    public String read() {
        return withIdActionBody(new JSONObject()
                .put("user", user)
                .put("token", token)).toString();
    }

    @Override
    public String repr() {
        return getClass().getSimpleName() + "(token=" + token + ", user=" + user.toString() + ")";
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void setUser(JSONObject user) {
        this.user = user;
    }
}
