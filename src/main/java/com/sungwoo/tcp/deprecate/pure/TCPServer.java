package com.sungwoo.tcp.deprecate.pure;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class TCPServer {

    private static final int SERVER_PORT = 3000;

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            System.out.println("Server is listening on port " + SERVER_PORT);
            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println("New client connected");

                executorService.submit(new ServerThread(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
