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

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        final String path = msg.uri();

        // Check if message is a websocket upgrade request
        if (msg.headers().get("Sec-WebSocket-Key") != null) {
            if (!HttpMethod.GET.equals(msg.method())) {
                sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, ctx.alloc().buffer(0)));
                return;
            }

            //if (wsPathMatcher.match(WsHandlerType.WS, path, ctx).size() == 0) {
            //    sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, ctx.alloc().buffer(0)));
            //    return;
            //}

            //AtomicBoolean cancelled = new AtomicBoolean(false);

            //wsPathMatcher.match(WsHandlerType.WS_BEFORE, path, ctx).forEach(wsPathEntry -> {
                // Run Event code here
            //});

            //if (!cancelled.get()) {
            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(msg.uri(), null, true);
            WebSocketServerHandshaker handshaker = factory.newHandshaker(msg);
            if (handshaker==null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                ChannelFuture future = handshaker.handshake(ctx.channel(), msg);

                if (future.isSuccess()) {
                    ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(), new Utf8FrameValidator());
                    ctx.pipeline().replace(this, "nettylinWebSocketRequestHandler", new WebSocketRequestHandler());

                } else {
                    ctx.close();
                }
            }
            return;
            //}
        }

        final boolean keepAlive = HttpUtil.isKeepAlive(msg);
        final byte[] uri = path.getBytes(StandardCharsets.UTF_8);

        FullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(uri));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
                .setInt(HttpHeaderNames.CONTENT_LENGTH, uri.length);

        if (keepAlive) {
            if (!msg.protocolVersion().isKeepAliveDefault()) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
        } else {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        }

        ChannelFuture future = ctx.write(response);

        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
