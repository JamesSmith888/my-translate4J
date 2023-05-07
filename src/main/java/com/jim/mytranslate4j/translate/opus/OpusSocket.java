package com.jim.mytranslate4j.translate.opus;

import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.net.SocketException;

/**
 * @author jim
 */
@Slf4j
public class OpusSocket {

    private static Socket socket;

    public static Socket getSocket() throws SocketException {
        if (socket == null) {
            log.error("OpusSocket.socket is null");
            throw new SocketException("OpusSocket.socket is null");
        }

        return socket;
    }

    public static void setSocket(Socket socket) {
        if (OpusSocket.socket != null) {
            log.warn("OpusSocket.socket is not null, will be ignored");
            return;
        }

        OpusSocket.socket = socket;
    }
}
