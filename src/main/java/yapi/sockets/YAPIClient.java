package yapi.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class YAPIClient {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean looping = false;

    public static void main(String[] args) {
        YAPIClient yapiClient = new YAPIClient("127.0.0.1", 80, true);
    }

    public YAPIClient(String host, int port) {
        openSocket(host, port, false);
    }

    public YAPIClient(String host, int port, boolean looping) {
        openSocket(host, port, looping);
    }

    private static boolean hostAvailabilityCheck(String host, int port) {
        try (Socket s = new Socket(host, port)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }

    private void openSocket(String host, int port, boolean looping) {
        this.looping = looping;
        while (looping) {
            try {
                if (hostAvailabilityCheck(host, port)) {
                    socket = new Socket(host, port);
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                    break;
                }
            } catch (IOException e) {
                /* ignored */
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void closeSocket() {
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {

        }
    }

}
