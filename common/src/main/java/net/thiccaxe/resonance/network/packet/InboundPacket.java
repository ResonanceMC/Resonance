package net.thiccaxe.resonance.network.packet;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class InboundPacket {

    private final int id;
    private final JsonObject body;

    public InboundPacket(int id, @NotNull JsonObject body) {
        this.id = id;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public @NotNull JsonObject getBody() {
        return body;
    }
}
