package dev.razboy.resonance.packets.clientbound.play;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONObject;

public class OUserInfoPacket extends ClientBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.USER_INFO;
    }


    private String token;
    private JSONObject user;
    public void setToken(String token) {
        this.token = token;
    }
    public void setUser(JSONObject user) {
        this.user = user;
    }

    @Override
    public String repr() {
        return getClass().getSimpleName() + "(token=" + token + ", user=" + user.toString() + ")";
    }


    @Override
    public String read() {
        return withIdActionBody(new JSONObject()
                //put("token", token)
                .put("user", user)
                ).toString();
    }
}
