package de.hu.p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class Servent extends Thread {
    private ServerSocket serverSocket;
    private Set<OutgoingPeer> outgoingConnections = new HashSet<>();

    public Servent(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(port));
    }

    public void run(){
        try {
            while(true){
                OutgoingPeer stp = new OutgoingPeer(serverSocket.accept(), this);
                outgoingConnections.add(stp);
                stp.start();
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public Set<OutgoingPeer> getOutgoingConnections() {
        return outgoingConnections;
    }

    public void send(String message) {
        try { outgoingConnections.forEach(t -> t.getPrintWriter().println(message));}
        catch (Exception e) {e.printStackTrace();}
    }
}
