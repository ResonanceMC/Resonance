package net.thiccaxe.resonance.network.packet.client.login;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.thiccaxe.resonance.network.packet.InboundPacket;
import net.thiccaxe.resonance.network.packet.client.ClientPacket;
import net.thiccaxe.resonance.network.user.WebSocketConnection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AuthTokenPacket implements ClientPacket {

    private String tokenString;

    @Override
    public void read(@NotNull InboundPacket packet) {
        final JsonObject json = packet.getBody();
        if (json.has("token"))
        tokenString = json.getAsJsonPrimitive("token").getAsString();
    }

    public void process(WebSocketConnection ctx) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(0x00));
        //messageId.ifPresent(s -> jsonObject.add("messageId", new JsonPrimitive(s)));

        JsonObject body = new JsonObject();

        body.addProperty("success", false);
        {
            JsonObject user = new JsonObject();
            user.addProperty("online", false);

            {
                JsonObject pos = new JsonObject();
                pos.addProperty("x", 1);
                pos.addProperty("y", 1);
                pos.addProperty("z", 1);
                pos.addProperty("inRange", true);

                {
                    JsonArray rotation = new JsonArray();
                    rotation.add(90);
                    rotation.add(180);
                    pos.add("rotation", rotation);
                }

                user.add("pos", pos);
            }
            {
                JsonObject data = new JsonObject();
                data.addProperty("username", "biggles");
                data.addProperty("uuid", UUID.randomUUID().toString());

                user.add("data", data);
            }

            body.add("user", user);
        }
        jsonObject.add("body", body);
    }
}
