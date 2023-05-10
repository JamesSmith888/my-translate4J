package org.jim.mytranslate4j.translate.opus;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author jim
 */
@Component
public class Translator {

    public String translate(String content) {
        try {
            Socket socket = OpusSocket.getSocket();
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            // 发送翻译内容(需要加上\n，否则python脚本无法读取)
            out.println(content + "\n");
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Translator translator = new Translator();
        System.out.println(translator.translate("Why Spring?\n" +
                "Spring makes programming Java quicker, easier, and safer for everybody. Spring’s focus on speed, simplicity, and productivity has made it the world's most popular Java framework."));
    }
}
