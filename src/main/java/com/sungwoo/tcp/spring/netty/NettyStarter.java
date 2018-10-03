package com.sungwoo.tcp.spring.netty;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NettyStarter {
    public static void main(String[] args) {
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class)){
            context.registerShutdownHook();
            NettyServer nettyServer = context.getBean(NettyServer.class);
            nettyServer.start();
        }
    }
}
