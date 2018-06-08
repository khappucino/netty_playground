package com.willkamp.server.GRPCServer;

import io.netty.handler.codec.http2.*;

import static io.netty.handler.logging.LogLevel.INFO;

public class Http2HandlerBuilder extends AbstractHttp2ConnectionHandlerBuilder<Http2Handler, Http2HandlerBuilder> {

  private static final Http2FrameLogger logger = new Http2FrameLogger(INFO, Http2Handler.class);

  Http2HandlerBuilder() {
    frameLogger(logger);
  }

  @Override
  public Http2Handler build() {
    return super.build();
  }

  @Override
  protected Http2Handler build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder,
                                         Http2Settings initialSettings) {
    Http2Handler handler = new Http2Handler(decoder, encoder, initialSettings);
    frameListener(handler);
    return handler;
  }
}