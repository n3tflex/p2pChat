package de.hu.p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class Servent extends Thread {
    private ServerSocket serverSocket;
    // HashMap containing all known connections to send our messages to
    private Set<OutgoingConnection> outgoingConnections = new HashSet<>();

    public Servent(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(port));
    }

    public void run(){
        try {
            while(true){
                // Is called after another peer is trying to create connection in their joinNetwork() method
                // Via this connection we send the messages
                OutgoingConnection ic = new OutgoingConnection(serverSocket.accept(), this);
                System.out.println("New Connection: " + ic.getSocket().getInetAddress());
                outgoingConnections.add(ic);
                ic.start();
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public Set<OutgoingConnection> getOutgoingConnections() {
        return outgoingConnections;
    }

    // This method is called when the user enters a new message to the commandline
    public void send(String message) {
        try {
            outgoingConnections.forEach(t ->
                t.getPrintWriter().println(message));}
        catch (Exception e) {e.printStackTrace();}
    }
}
