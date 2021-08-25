package net.thiccaxe.resonance.network.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;

public class NettyServer {

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final SslContext sslContext;


    public NettyServer() {
        this(new NioEventLoopGroup(1), new NioEventLoopGroup(2), null);
    }

    public NettyServer(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        this(bossGroup, workerGroup, null);
    }


    public NettyServer(EventLoopGroup bossGroup, EventLoopGroup workerGroup, SslContext sslContext) {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.sslContext = sslContext;
    }

    public void start(int port) {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(sslContext));

            Channel channel = bootstrap.bind(port).channel();

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
