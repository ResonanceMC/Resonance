package net.thiccaxe.resonance.network.packet;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class InboundPacket {

    private final int id;
    private final JsonObject body;
    private final Optional<String> messageId;
    private final Optional<String> bearer;

    public InboundPacket(int id, @NotNull JsonObject body) {
        this(id, body, Optional.empty(), Optional.empty());
    }

    public InboundPacket(int id, @NotNull JsonObject body, @NotNull Optional<String> messageId, @NotNull Optional<String> bearer ) {
        this.id = id;
        this.body = body;
        this.messageId = messageId;
        this.bearer = bearer;
    }

    public int getId() {
        return id;
    }

    public @NotNull JsonObject getBody() {
        return body;
    }

    public Optional<String> getMessageId() {
        return messageId;
    }

    public Optional<String> getBearer() {
        return bearer;
    }
}
