package week03.gateway.inbound;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week03.gateway.outbound.netty.NettyClientHttpOutboundHandler;
import week03.gateway.outbound.netty.NettyHttpClient;

public class GatewayInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GatewayInboundHandler.class);

    private final NettyHttpClient nettyHttpClient;

    public GatewayInboundHandler(NettyHttpClient nettyHttpClient) {
        this.nettyHttpClient = nettyHttpClient;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        logger.info("Handle gateway uri is {}", request.uri());
        Channel channel = nettyHttpClient.getChannel();
        if(!(channel.pipeline().last() instanceof NettyClientHttpOutboundHandler)){
            channel.pipeline().addLast(new NettyClientHttpOutboundHandler(ctx));
        }
        logger.info("Channel is {}", channel);
        logger.info("Pipeline is {}", nettyHttpClient.getChannel().pipeline());
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, request.uri());
        channel.writeAndFlush(httpRequest);
    }
}
