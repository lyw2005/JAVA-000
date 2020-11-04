package week03.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class HttpHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("exception caught :", cause);
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
                String uri = fullHttpRequest.uri();
                logger.info("Uri is {}", uri);
                if (uri.contains("/test")) {
                    this.doExecuteMethod(fullHttpRequest, ctx);
                }
            }
        } catch (Exception e) {
            logger.error("An error in channel read :", e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    private void doExecuteMethod(FullHttpRequest request, ChannelHandlerContext context) {
        String result = "Hello, Netty Server";
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getBytes(StandardCharsets.UTF_8)));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
            //Thread.sleep(20);
        } catch (Exception e) {
            logger.error("failed :", e);
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (context.channel().isActive()) {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                context.writeAndFlush(response);
            } else {
                context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
