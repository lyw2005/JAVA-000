package week03.gateway.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week03.gateway.filter.HttpRequestFilter;

public class GatewayInboundFilter extends ChannelInboundHandlerAdapter implements HttpRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(GatewayInboundFilter.class);

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        String uri = fullRequest.uri();
        logger.info("Before filter the uri is {}", uri);
        if (uri.equals("/")) {
            fullRequest.setUri("/test");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            this.filter(request, ctx);
            ctx.fireChannelRead(msg);
        } else {
            throw new IllegalArgumentException("Object not supported!");
        }
    }
}
