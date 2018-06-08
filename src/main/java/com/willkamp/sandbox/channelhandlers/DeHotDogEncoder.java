package com.willkamp.sandbox.channelhandlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class DeHotDogEncoder extends MessageToMessageEncoder<String> {
  @Override
  protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
    if (msg.equalsIgnoreCase("hotdog")) {
      out.add("nothotdog");
    }
    else {
      out.add(msg);
    }

  }
}
