package net.thiccaxe.resonance.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;

    public NettyServerInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslContext != null) {
            pipeline.addLast("sslContext", sslContext.newHandler(ch.alloc()));
        }

        pipeline.addLast("httpServerCodec", new HttpServerCodec())
                .addLast("httpObjectAggregator", new HttpObjectAggregator(65536))
                .addLast("nettylinHttpRequestHandler", new HttpRequestHandler());
    }
}
