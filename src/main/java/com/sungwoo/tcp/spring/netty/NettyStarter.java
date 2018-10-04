package com.sungwoo.tcp.spring.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication(scanBasePackages = "com.sungwoo.tcp")
public class NettyStarter {

    @Bean
    public BlockingQueue queue() {
        return new LinkedBlockingQueue();
    }

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyStarter.class, args);

        NettyServer nettyServer = context.getBean(NettyServer.class);
        nettyServer.start();
    }
}
