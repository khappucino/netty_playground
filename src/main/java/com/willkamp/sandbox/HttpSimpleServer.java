package com.willkamp.sandbox;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;

import java.net.InetSocketAddress;

public class HttpSimpleServer {

  public static void main(String[] args) throws Exception {
    int port = 8000;
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap bootStrap = new ServerBootstrap();
      bootStrap.group(workerGroup)
        .channel(NioServerSocketChannel.class)
        .localAddress(new InetSocketAddress(port))
        .childHandler(new ChannelInitializer<Channel>() {
          @Override
          protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new HttpClientCodec());
            ch.pipeline().addLast(new ChannelInboundHandler() {
              @Override
              public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channelRegistered " + ctx);
              }

              @Override
              public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channelUnregistered " + ctx);
              }

              @Override
              public void channelActive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channelActive " + ctx);
              }

              @Override
              public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channelInactive " + ctx);
              }

              @Override
              public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("channelRead " + ctx + " msg:" + msg);
              }

              @Override
              public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channelReadComplete " + ctx);
              }

              @Override
              public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                System.out.println("userEventTriggered " + ctx + " evt:" + evt);
              }

              @Override
              public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channelWritabilityChanged " + ctx);
              }

              @Override
              public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                System.out.println("exceptionCaught " + ctx);
              }

              @Override
              public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                System.out.println("handlerAdded " + ctx);
              }

              @Override
              public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                System.out.println("handlerRemoved " + ctx);
              }
            });
          }
        });

      ChannelFuture channelFuture = bootStrap.bind().sync();
      channelFuture.channel().closeFuture().sync();
    }
    catch (Exception e) {

    }
    finally {
      workerGroup.shutdownGracefully().sync();
    }
  }
}
