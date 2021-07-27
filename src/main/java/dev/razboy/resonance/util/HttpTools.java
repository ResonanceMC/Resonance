package dev.razboy.resonance.util;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

public class HttpTools {
    public static boolean upgrade(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (request.headers().get("Connection") == null || request.headers().get("Upgrade") == null) {
            return false;
        }
        if (request.headers().get("Connection").equalsIgnoreCase("upgrade") && request.headers().get("Upgrade").equalsIgnoreCase("websocket")) {
            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws://thiccaxe.net/", null, true);
            WebSocketServerHandshaker handshaker = factory.newHandshaker(request);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                return false;
            }
            handshaker.handshake(ctx.channel(), request);

            return true;

        }
        return false;
    }

    public static String getRemoteAddress(ChannelHandlerContext ctx, FullHttpRequest request) {
        HttpHeaders headers = request.headers();
        String ip = headers.get("X-Forwarded-For");
        if (ip != null) {return ip;}
        return ctx.channel().remoteAddress().toString();
    }

    public static void writeTemplateResponse(ChannelHandlerContext ctx, String remote, FullHttpRequest request) {
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.TEMPORARY_REDIRECT);
        response.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8")
                .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())
                .set(HttpHeaderNames.LOCATION, "https://positional-audio.now.sh" + request.uri());
        if (keepAlive) {
            if (!request.protocolVersion().isKeepAliveDefault()) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
        } else {
            // Tell the client we're going to close the connection.
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        }

        ChannelFuture f = ctx.write(response);

        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
