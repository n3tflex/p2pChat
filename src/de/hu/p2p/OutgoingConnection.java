package de.hu.p2p;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OutgoingConnection extends Thread {
    private Servent servent;
    private Socket socket;
    private PrintWriter pw;
    public OutgoingConnection(Socket socket, Servent servent){
        this.servent = servent;
        this.socket = socket;
    }

    public void run(){
        try{
            // Creates a new BufferedReader listening to new messages from the command line
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            while(true) {
                // if BufferedReader detects a new line it is send to the servent
                servent.send(br.readLine());
            }
        } catch (Exception e) { servent.getOutgoingConnections().remove(this); }
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }

    public Socket getSocket(){
        return socket;
    }
}
