package week03.gateway.outbound.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyHttpClient.class);

    private Channel channel;

    private final String host;
    private final int port;

    public NettyHttpClient(String host, int port) {
        this.host = host;
        this.port = port;

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline cp = socketChannel.pipeline();
                    cp.addLast(new HttpResponseDecoder());
                    cp.addLast(new HttpRequestEncoder());
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(this.host, this.port).sync();
            channel = channelFuture.channel();
            logger.info("Netty client has been started!");
        } catch (InterruptedException e) {
            logger.error("Netty client start failed : ", e);
        }
    }

    public Channel getChannel() {
        return channel;
    }
}
