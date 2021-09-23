package net.thiccaxe.resonance.network.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.Utf8FrameValidator;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import net.thiccaxe.resonance.Resonance;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpConnection extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private final Resonance resonance;

    private static final String TOKEN_PARAM_KEY = "token";

    private static final WsPathHandler invalidWsPathHandler = (ctx, msg, path) -> {
        sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, ctx.alloc().buffer(0)));
    };
    private final Map<String, WsPathHandler> wsPathMap;

    private final Map<String, HttpPathHandler> httpPathMap;

    public HttpConnection(final Resonance resonance) {
        super();
        this.resonance = resonance;
        this.wsPathMap = Map.of(
                "/server/ws/", this::handleWebsocketAuthAttempt
        );
        this.httpPathMap = Map.of(
                "/server/token/", this::handleQuickAuth
        );

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        final String rawPath = msg.uri();
        final QueryStringDecoder path = new QueryStringDecoder(rawPath);

        System.out.println(path);
        // Check if message is a websocket upgrade request
        final HttpHeaders headers = msg.headers();
        if (
                headers.get(CONNECTION).equalsIgnoreCase(HttpHeaderValues.UPGRADE.toString())
                &&
                headers.get(UPGRADE).equalsIgnoreCase(HttpHeaderValues.WEBSOCKET.toString())
        ) {
            if (headers.get(SEC_WEBSOCKET_KEY) == null) {
                sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, ctx.alloc().buffer(0)));
                return;
            }

            handleWebsocketRequest(ctx, msg, path);
            return;
        }


        handleHttpRequest(ctx, msg, path);

    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg, QueryStringDecoder path) {
        final boolean keepAlive = HttpUtil.isKeepAlive(msg);
        final byte[] uri = path.path().getBytes(StandardCharsets.UTF_8);

        FullHttpResponse response = null;

        for (Map.Entry<String, HttpPathHandler> entry : httpPathMap.entrySet()) {
            if (path.path().equalsIgnoreCase(entry.getKey())) {
                response = entry.getValue().handle(ctx, msg, path);
                break;
            }
        }

        if (response == null) {
            response = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(uri));
            response.headers().set(CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH, uri.length);
        }

        if (keepAlive) {
            if (!msg.protocolVersion().isKeepAliveDefault()) {
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
        } else {
            response.headers().set(CONNECTION, HttpHeaderValues.CLOSE);
        }

        ChannelFuture future = ctx.write(response);

        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void handleWebsocketRequest(ChannelHandlerContext ctx, FullHttpRequest msg, QueryStringDecoder path) {
        for (Map.Entry<String, WsPathHandler> entry : wsPathMap.entrySet()) {
            if (path.path().startsWith(entry.getKey())) {
                entry.getValue().handle(ctx, msg, path);
                return;
            }
        }

        invalidWsPathHandler.handle(ctx, msg, path);
    }

    private void handshakeWebsocketConnection(ChannelHandlerContext ctx, HttpRequest msg) {
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(msg.uri(), null, true);
        WebSocketServerHandshaker handshaker = factory.newHandshaker(msg);
        if (handshaker==null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            ChannelFuture future = handshaker.handshake(ctx.channel(), msg);

            if (future.isSuccess()) {
                ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(), new Utf8FrameValidator());
                ctx.pipeline().replace(this, "webSocketRequestHandler", new WebsocketConnection(resonance));
            } else {
                ctx.close();
            }
        }
    }

    // All three functions below have a small chance of doing file writes. - If the token is invalidated.
    // If this causes major issues this will be updated.
    private void handleWebsocketAuthAttempt(ChannelHandlerContext ctx, HttpRequest msg, QueryStringDecoder path) {
        // Check if token is present
        if (!path.parameters().containsKey(TOKEN_PARAM_KEY)) {
            sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, ctx.alloc().buffer(0)));
        }

        final List<String> tokenStrings = path.parameters().get(TOKEN_PARAM_KEY);
        final String tokenString = tokenStrings.get(tokenStrings.size()-1);

        // Verify Token
        final UUID uuid = resonance.authManager().getLinkedKey(tokenString);

        if (uuid == null) {
            sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, ctx.alloc().buffer(0)));
        }

    }

    private FullHttpResponse handleQuickAuth(ChannelHandlerContext ctx, FullHttpRequest msg, QueryStringDecoder path) {
        // Check if token is present
        if (!path.parameters().containsKey(TOKEN_PARAM_KEY)) {
            sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, ctx.alloc().buffer(0)));
        }

        final List<String> tokenStrings = path.parameters().get(TOKEN_PARAM_KEY);
        final String tokenString = tokenStrings.get(tokenStrings.size()-1);

        // Verify Auth Token
        final UUID token = resonance.quickAuthManager().getLinkedKey(tokenString);

        if (token == null) {
            sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, ctx.alloc().buffer(0)));
        }
        resonance.quickAuthManager().invalidateToken(tokenString);
    }

    private void checkProvidedToken(String tokenString) {

    }


    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @FunctionalInterface
    public interface WsPathHandler {
        void handle(ChannelHandlerContext ctx, FullHttpRequest request, QueryStringDecoder path);
    }

    @FunctionalInterface
    public interface HttpPathHandler {
        FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request, QueryStringDecoder path);
    }

}