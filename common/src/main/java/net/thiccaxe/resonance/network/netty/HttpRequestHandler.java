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

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.SEC_WEBSOCKET_KEY;
import static io.netty.handler.codec.http.HttpHeaderNames.UPGRADE;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private final Resonance resonance;

    public HttpRequestHandler(final Resonance resonance) {
        super();
        this.resonance = resonance;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        final String path = msg.uri();

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

            negotiateWebsocketConnection(ctx, msg);
            return;
        }

        final boolean keepAlive = HttpUtil.isKeepAlive(msg);
        final byte[] uri = path.getBytes(StandardCharsets.UTF_8);

        FullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(uri));
        response.headers().set(CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
                .setInt(CONTENT_LENGTH, uri.length);

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
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void negotiateWebsocketConnection(ChannelHandlerContext ctx, HttpRequest msg) {
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(msg.uri(), null, true);
        WebSocketServerHandshaker handshaker = factory.newHandshaker(msg);
        if (handshaker==null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            ChannelFuture future = handshaker.handshake(ctx.channel(), msg);

            if (future.isSuccess()) {
                ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(), new Utf8FrameValidator());
                ctx.pipeline().replace(this, "webSocketRequestHandler", new WebSocketRequestHandler(resonance));

            } else {
                ctx.close();
            }
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
