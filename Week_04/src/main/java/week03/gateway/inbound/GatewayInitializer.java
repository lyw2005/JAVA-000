package week03.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import week03.gateway.outbound.netty.NettyHttpClient;

public class GatewayInitializer extends ChannelInitializer<SocketChannel> {

    private final NettyHttpClient nettyHttpClient;

    public GatewayInitializer(NettyHttpClient nettyHttpClient) {
        this.nettyHttpClient = nettyHttpClient;
    }

    @Override
    protected void initChannel(SocketChannel SocketChannel) throws Exception {
        ChannelPipeline pc = SocketChannel.pipeline();
        pc.addLast(new HttpServerCodec());
        pc.addLast(new HttpObjectAggregator(1024 * 1024));
        pc.addLast(new GatewayInboundFilter());
        pc.addLast(new GatewayInboundHandler(nettyHttpClient));
    }
}
