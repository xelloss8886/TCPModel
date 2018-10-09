package com.sungwoo.tcp.deprecate.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

    // 서버 소켓 포트 번호를 지정합니다.
    private static final int PORT = 8080;

    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup(1);
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try{
            // 서버 부트스트랩을 만듭니다. 이 클래스는 일종의 헬퍼 클래스입니다.
            // 이 클래스를 사용하면 서버에서 Channel을 직접 세팅 할 수 있습니다.
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(parentGroup, childGroup)
                    // 인커밍 커넥션을 액세스하기 위해 새로운 채널을 객체화 하는 클래스 지정합니다.
                    .channel(NioServerSocketChannel.class)
                    // 상세한 Channel 구현을 위해 옵션을 지정할 수 있습니다.
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 새롭게 액세스된 Channel을 처리합니다.
                    // ChannelInitializer는 특별한 핸들러로 새로운 Channel의
                    // 환경 구성을 도와 주는 것이 목적입니다.
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline cp = sc.pipeline();
                            cp.addLast(new EchoServerHandler());
                        }
                    });

            // 인커밍 커넥션을 액세스하기 위해 바인드하고 시작합니다.
            ChannelFuture cf = sb.bind(PORT).sync();

            // 서버 소켓이 닫힐때까지 대기합니다.
            cf.channel().closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}