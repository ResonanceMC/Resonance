package net.thiccaxe.resonance.network.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import net.thiccaxe.resonance.Resonance;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class WebSocketRequestHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final Resonance resonance;

    private static final ObjectMapper mapper = new ObjectMapper();

    public WebSocketRequestHandler(final Resonance resonance) {
        super();
        this.resonance = resonance;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame textWebSocketFrame) {
            final String message = textWebSocketFrame.retain().text();

            ObjectNode node = mapper.readValue(message, ObjectNode.class);


            final UUID uuid = UUID.fromString(node.get("uuid").asText());

            Signer signer = HMACSigner.newSHA256Signer(resonance.configManager().mainConfig().getKey() + "$" + uuid); // Salt

            JWT jwt = new JWT()
                    .setIssuer("resonance@resonance.dev")
                    .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                    .setSubject(uuid.toString())
                    .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(4));

            String encodedJWT = JWT.getEncoder().encode(jwt, signer);

            node = JsonNodeFactory.instance.objectNode();

            node.set("jwt", JsonNodeFactory.instance.textNode(encodedJWT));

            ctx.write(new TextWebSocketFrame(mapper.writeValueAsString(node)));

            //ctx.write(encodedJWT);

        }
    }
}
