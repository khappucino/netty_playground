package com.willkamp.server.GRPCServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.util.internal.ObjectUtil.checkNotNull;

public class Http1Handler extends SimpleChannelInboundHandler<FullHttpRequest> {
  private final String establishApproach;

  Http1Handler(String establishApproach) {
    this.establishApproach = checkNotNull(establishApproach, "establishApproach");
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
    if (HttpUtil.is100ContinueExpected(req)) {
      ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
    }
    boolean keepAlive = HttpUtil.isKeepAlive(req);

    ByteBuf content = ctx.alloc().buffer();
    content.writeBytes(Http2Handler.RESPONSE_BYTES.duplicate());
    ByteBufUtil.writeAscii(content, " - via " + req.protocolVersion() + " (" + establishApproach + ")");

    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
    response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
    response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

    if (!keepAlive) {
      ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    } else {
      response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
      ctx.write(response);
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}