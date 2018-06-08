package com.willkamp.server.GRPCServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http2.*;

import java.net.InetSocketAddress;

public class GRPCServer {

  public static void main(String[] args) {
    GRPCServer server = new GRPCServer();
    try {
      server.start(8080);
    }
    catch(Exception e) {

    }
  }

  public void start(int port) throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .localAddress(new InetSocketAddress(port))
        .childHandler(new Http2ServerInitializer());

      ChannelFuture channelFuture = bootstrap.bind().sync();
      channelFuture.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully().sync();
      workerGroup.shutdownGracefully().sync();
    }
  }
}
