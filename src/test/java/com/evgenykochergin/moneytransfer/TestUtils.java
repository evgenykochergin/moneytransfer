package com.evgenykochergin.moneytransfer;

import java.io.IOException;
import java.net.ServerSocket;

public class TestUtils {

    public static int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0);) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new IllegalStateException("Could not find free TCP/IP port");
        }
    }

}
