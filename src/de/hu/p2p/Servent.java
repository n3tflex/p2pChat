package de.hu.p2p;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;

public class Servent extends Thread {

    // IP of the running peer
    public static String peerIP;
    // Server socket to create sockets after connection request
    private ServerSocket serverSocket;
    // HashMaps containing all known connections to sendChatMessage our messages to
    private HashMap<String, OutgoingConnection> outgoingConnections = new HashMap<>();
    private HashMap<String, IncomingConnection> incomingConnections = new HashMap<>();

    public Servent(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(port));
    }

    public void run(){
        try {
            while(true){
                // Is called after another peer is trying to create connection in their startPeers() method
                Socket socket = serverSocket.accept();
                String ip = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
                // if connecting peer is not yet a known incoming or outgoing connection then add it
                if(!incomingConnections.containsKey(ip)){
                    addIncomingConnection(ip, 4445);
                }
                if(!outgoingConnections.containsKey(ip)){
                    addOutgoingConnection(ip, socket);
                    sendPongMessage(Main.port);
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public HashMap<String, OutgoingConnection> getOutgoingConnections() {
        return outgoingConnections;
    }

    // This method is called when the user enters a new message to the commandline
    public void sendChatMessage(String message) {
        try {
            outgoingConnections.forEach((k, v) ->
                v.getPrintWriter().println(message));}
        catch (Exception e) {e.printStackTrace();}
    }

    public void addOutgoingConnection(String ip, Socket socket) {
        OutgoingConnection ic = new OutgoingConnection(socket, this);
        outgoingConnections.put(ip, ic);
        ic.start();
        System.out.println("New outgoing connection: " + ip);
    }

    public void addIncomingConnection(String ip, int port) throws IOException {
        IncomingConnection ic = new IncomingConnection(new Socket(ip, port), this);
        incomingConnections.put(ip, ic);
        ic.start();
    }

    public void sendPingMessage(String message){
        // Send message to all known outgoing connections
        try {
            outgoingConnections.forEach((k,v) ->
                    v.getPrintWriter().println(new Ping(1, 3).createPing()));}
        catch (Exception e) {e.printStackTrace();}
    }

    public void sendPongMessage(int port){
        // Send message to all known outgoing connections

        try {
            outgoingConnections.forEach((k,v) -> {
                try {
                    v.getPrintWriter().println(new Pong(InetAddress.getLocalHost().getHostAddress(), port).createPong());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });}
        catch (Exception e) {e.printStackTrace();}
    }
}
