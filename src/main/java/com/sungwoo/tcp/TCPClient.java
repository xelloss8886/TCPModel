package com.sungwoo.tcp;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

@Slf4j
public class TCPClient {
    public static void main(String[] args) {

        String s = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            s = bufferedReader.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }


        String[] data = s.split(" ");
        String hostname = data[0];
        int port = Integer.parseInt(data[1]);
        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("started TCPClient");

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            Scanner scanner = new Scanner(System.in);
            String text;

            do {
                text = scanner.nextLine();

                writer.println(text);

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String reversedString = reader.readLine();

                System.out.println(reversedString);

            } while (!text.equals("bye"));
            socket.close();

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
