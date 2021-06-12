package net.thiccaxe.resonance.network.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.incubator.channel.uring.IOUring;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;
import net.thiccaxe.resonance.network.http.HttpRequestHandler;
import net.thiccaxe.resonance.network.websocket.WebSocketPacketDecoder;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class NettyServer {

    private static final Logger LOGGER = Logger.getLogger("NettyServer");

    private boolean initialized = false;

    private EventLoopGroup bossGroup, workerGroup;
    private ServerBootstrap bootstrap;

    private int port;

    private ServerSocketChannel serverChannel;

    public void init() {
        if (initialized) {
            throw new IllegalStateException("Netty server has already been initialized!");
        }
        this.initialized = true;
        Class<? extends ServerChannel> channel;

        // Find boss/worker event group
        {
            if (IOUring.isAvailable()) {
                bossGroup = new IOUringEventLoopGroup(1);
                workerGroup = new IOUringEventLoopGroup(1);

                channel = IOUringServerSocketChannel.class;

                LOGGER.info("Using io_uring");
            } else if (Epoll.isAvailable()) {
                bossGroup = new EpollEventLoopGroup(1);
                workerGroup = new EpollEventLoopGroup(1);

                channel = EpollServerSocketChannel.class;

                LOGGER.info("Using epoll");
            } else if (KQueue.isAvailable()) {
                bossGroup = new KQueueEventLoopGroup(1);
                workerGroup = new KQueueEventLoopGroup(1);

                channel = KQueueServerSocketChannel.class;

                LOGGER.info("Using kqueue");
            } else {
                bossGroup = new NioEventLoopGroup(1);
                workerGroup = new NioEventLoopGroup(1);

                channel = NioServerSocketChannel.class;

                LOGGER.info("Using NIO");
            }
        }




        bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(channel)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(@NotNull SocketChannel ch) {
                        ChannelConfig config = ch.config();
                        config.setOption(ChannelOption.TCP_NODELAY, true);
                        config.setOption(ChannelOption.SO_KEEPALIVE, true);
                        config.setAllocator(ByteBufAllocator.DEFAULT);

                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast("http-server-codec", new HttpServerCodec());
                        pipeline.addLast("chunked-write-handler", new ChunkedWriteHandler());
                        pipeline.addLast("http-object-aggregator", new HttpObjectAggregator(64*1024));
                        pipeline.addLast("http-request-handler", new HttpRequestHandler("/ws"));
                        pipeline.addLast("ws-server-proto-handler", new WebSocketServerProtocolHandler("/ws"));
                        pipeline.addLast("websocket-packet-decoder", new WebSocketPacketDecoder());
                    }
                });
    }

    public void start(int port) {
        this.port = port;
        try {
            ChannelFuture cf = bootstrap.bind(new InetSocketAddress(port)).sync();

            if (!cf.isSuccess()) {
                throw new IllegalStateException("Unable to bind server at :" + port);
            }

            this.serverChannel = (ServerSocketChannel) cf.channel();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
