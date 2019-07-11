package de.hu.p2p;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

public class Servent extends Thread {

    // IP of the running peer
    public static String peerIP;
    // Server socket to create sockets after connection request
    private ServerSocket serverSocket;
    // HashMaps containing all known connections to sendChatMessage our messages to
    private HashMap<String, Connection> connections = new HashMap<>();
    private HashSet<String> seenMessages = new HashSet<>();
    public Servent(int port) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(port));
    }

    public void run(){
        try {
            while(true){
                // Is called after another peer is trying to create connection in their startPeers() method
                Socket socket = serverSocket.accept();
                String ip = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
                // if connecting peer is not yet a known incoming or outgoing connection then add it
                addConnection(ip, socket);
               // sendPongMessage(Main.port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Connection> getConnections() {
        return connections;
    }

    // This method is called when the user enters a new message to the commandline
    public void sendChatMessage(String message) {
        try {
            connections.forEach((k, v) ->
                v.getPrintWriter().println(message));}
        catch (Exception e) {e.printStackTrace();}
    }

    public void addConnection(String ip, int port) throws IOException {
        if(!connections.containsKey(ip)) {
            Connection ic = new Connection(new Socket(ip, port), this);
            connections.put(ip, ic);
            ic.start();
            ic.getPrintWriter().println(new Ping().createPing());
            System.out.println("New outgoing connection: " + ip);
        }
    }

    public void addConnection(String ip, Socket socket) throws IOException {
        if(!connections.containsKey(ip)) {
            Connection ic = new Connection(socket, this);
            connections.put(ip, ic);
            ic.start();
            ic.getPrintWriter().println(new Pong(InetAddress.getLocalHost().getHostAddress(), 1233).createPong());
            System.out.println("New outgoing connection: " + ip);
        }
    }

    public void sendPingMessage(String message){
        // Send message to all known outgoing connections
        try {
            connections.forEach((k,v) ->
                    v.getPrintWriter().println(new Ping().createPing()));}
        catch (Exception e) {e.printStackTrace();}
    }

    public void forwardPingMessage(String message){
        // Send message to all known outgoing connections
        try {
            connections.forEach((k,v) ->
                    v.getPrintWriter().println(message));}
        catch (Exception e) {e.printStackTrace();}
    }

    public void sendPongMessage(int port){
        // Send message to all known outgoing connections
        try {
            connections.forEach((k,v) -> {
                try {
                    v.getPrintWriter().println(new Pong(InetAddress.getLocalHost().getHostAddress(), port).createPong());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });}
        catch (Exception e) {e.printStackTrace();}
    }

    public HashSet<String>  getSeenMessages() {
        return seenMessages;
    }

    public void setSeenMessages(HashSet<String>  seenMessages) {
        this.seenMessages = seenMessages;
    }
}
