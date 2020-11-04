package week03.gateway.outbound.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyClientHttpOutboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHttpOutboundHandler.class);

    private final ChannelHandlerContext gatewayChannelHandlerContext;

    public NettyClientHttpOutboundHandler(ChannelHandlerContext gatewayChannelHandlerContext) {
        this.gatewayChannelHandlerContext = gatewayChannelHandlerContext;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpContent) {
            HttpContent response = (HttpContent) msg;
            String result = response.content().toString(CharsetUtil.UTF_8);
            logger.info("result is {}", result);
            this.writeBack(result);
        }
    }

    private void writeBack(String msg) {
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(msg.getBytes(StandardCharsets.UTF_8)));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
        } catch (Exception e) {
            logger.error("filed :", e);
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (gatewayChannelHandlerContext.channel().isActive()) {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                gatewayChannelHandlerContext.writeAndFlush(response);
            } else {
                gatewayChannelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
