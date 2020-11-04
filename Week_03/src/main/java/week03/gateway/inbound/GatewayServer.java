package week03.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week03.gateway.outbound.netty.NettyHttpClient;

public class GatewayServer {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServer.class);

    private final int port;
    private final int bossCount;
    private final int workerCount;

    public GatewayServer(int port, int bossCount, int workerCount) {
        this.port = port;
        this.bossCount = bossCount;
        this.workerCount = workerCount;
    }

    public void startGateway() {

        EventLoopGroup boosGroup = new NioEventLoopGroup(this.bossCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(this.workerCount);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new GatewayInitializer(new NettyHttpClient("127.0.0.1", 8808)));

            Channel channel = bootstrap.bind(this.port).sync().channel();
            logger.info("Gateway sever has been started! use port {}, boos count {}, worker count {}", this.port, this.bossCount, this.workerCount);

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Start gateway server failed : ", e);
        } finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }

    }
}
