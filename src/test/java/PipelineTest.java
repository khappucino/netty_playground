import com.willkamp.server.EchoServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

public class PipelineTest extends Assert {
  @Test
  public void testPipeline() {
    EventLoopGroup bossG = new NioEventLoopGroup();
    EventLoopGroup workerG = new NioEventLoopGroup();
    ServerBootstrap bs = new ServerBootstrap();
    bs.group(bossG, workerG);
    bs.channel(NioServerSocketChannel.class);
    bs.localAddress(new InetSocketAddress(2345));
    bs.handler(new EchoServerHandler());
    bs.childHandler(new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new EchoServerHandler());
      }
    });
    try {
      ChannelFuture bindFuture = bs.bind();
      bindFuture.awaitUninterruptibly();
      System.out.println("woo");
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
