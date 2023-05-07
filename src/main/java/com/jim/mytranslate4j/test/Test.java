package com.jim.mytranslate4j.test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 12345);


        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        out.println("hello my name is jim\n");

        while (true) {
            System.out.println("waiting for response");
            String line = in.readLine();
            System.out.println(line);
            if (line == null) {
                break;
            }
            System.out.println(line);
        }

        Thread.sleep(100000);

    }
}
