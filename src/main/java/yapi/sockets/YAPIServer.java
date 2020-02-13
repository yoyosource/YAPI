// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class YAPIServer {

    private ServerSocket ss;
    private List<Socket> connections;

    private boolean acceptClient;
    private int maxClient;
    private int port;

    public static void main(String[] args) {
        YAPIServer s = new YAPIServer(111);
    }

    public YAPIServer(int port){
        connections = new ArrayList<>();
        this.port = port;
    }
    public YAPIServer(int port, boolean open){
        connections = new ArrayList<>();
        this.port = port;
        if(open){
            openServer(port);
        }
    }
    /*
        @return If starting failed
        @param port The port in which the server starts
     */
    public boolean openServer(int port){
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            return true;
        }
        return false;
    }
    /*
        @return If the Server is now closed
     */
    public boolean closeServer() {
        try {
            ss.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}