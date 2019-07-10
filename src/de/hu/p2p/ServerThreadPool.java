package de.hu.p2p;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadPool extends Thread {
    private Server server;
    private Socket socket;
    private PrintWriter pw;
    public ServerThreadPool(Socket socket, Server server){
        this.server = server;
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            while(true) server.send(br.readLine());
        } catch (Exception e) { server.getServerThreadPool().remove(this); }
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }
}
