package de.hu.p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

public class Servent extends Thread {
    private ServerSocket serverSocket;
    // HashMap containing all known connections to sendChatMessage our messages to
    private Set<OutgoingConnection> outgoingConnections = new HashSet<>();
    private Set<IncomingConnection> incomingConnections = new HashSet<>();
    public Servent(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(port));
    }

    public void run(){
        try {
            while(true){
                // Is called after another peer is trying to create connection in their joinNetwork() method
                // Via this connection we sendChatMessage the messages
                OutgoingConnection ic = new OutgoingConnection(serverSocket.accept(), this);
                System.out.println("New Connection: " + ic.getSocket().getInetAddress());
                outgoingConnections.add(ic);
                ic.start();
                createIncomingConnection(ic.getSocket().getInetAddress().getHostAddress(), ic.getSocket().getPort());
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public void createIncomingConnection(String url, int port) throws IOException {
        Socket socket = new Socket(url, Integer.valueOf(port));
        IncomingConnection ic = new IncomingConnection(socket);
        incomingConnections.add(ic);
        ic.start();
    }

    public Set<OutgoingConnection> getOutgoingConnections() {
        return outgoingConnections;
    }

    // This method is called when the user enters a new message to the commandline
    public void sendChatMessage(String message) {
        try {
            outgoingConnections.forEach(t ->
                t.getPrintWriter().println(message));}
        catch (Exception e) {e.printStackTrace();}
    }

    public void addOutgoingConnection(String ip, int port) throws IOException {
        OutgoingConnection ic = new OutgoingConnection(new Socket(ip, port), this);
        System.out.println("New outGoingConnection: " + ic.getSocket().getInetAddress());
        outgoingConnections.add(ic);
    }


    public void sendPingMessage(String message){
        // Send message to all known outgoing connections
        try {
            outgoingConnections.forEach(t ->
                    t.getPrintWriter().println(new Ping(1, 3).createPing()));}
        catch (Exception e) {e.printStackTrace();}
    }


    public void sendPongMessage(String message){
        // Send message to all known outgoing connections
        try {
            outgoingConnections.forEach(t ->
            {
                try {
                    t.getPrintWriter().println(new Pong(InetAddress.getLocalHost().getHostAddress(), 4445).createPong());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });}
        catch (Exception e) {e.printStackTrace();}
    }
}
