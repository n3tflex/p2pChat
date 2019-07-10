package de.hu.p2p;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IncomingConnection extends Thread {
    private Servent servent;
    private Socket socket;
    private PrintWriter pw;
    public IncomingConnection(Socket socket, Servent servent){
        this.servent = servent;
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            while(true) servent.send(br.readLine());
        } catch (Exception e) { servent.getOutgoingConnections().remove(this); }
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }
}
