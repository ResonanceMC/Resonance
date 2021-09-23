package net.thiccaxe.resonance.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import net.thiccaxe.resonance.Resonance;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;
    private final Resonance resonance;

    public NettyServerInitializer(final Resonance resonance, SslContext sslContext) {
        this.sslContext = sslContext;
        this.resonance = resonance;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslContext != null) {
            pipeline.addLast("sslContext", sslContext.newHandler(ch.alloc()));
        }

        pipeline.addLast("httpServerCodec", new HttpServerCodec())
                .addLast("httpObjectAggregator", new HttpObjectAggregator(65536))
                .addLast("httpRequestHandler", new HttpConnection(resonance));
    }
}
