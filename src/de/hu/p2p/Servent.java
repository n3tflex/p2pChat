package de.hu.p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class Servent extends Thread {
    public static String peerIP;
    private ServerSocket serverSocket;
    // HashMap containing all known connections to sendChatMessage our messages to
    private Set<OutgoingConnection> outgoingConnections = new HashSet<>();
    private Set<IncomingConnection> incomingConnections = new HashSet<>();
    public Servent(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(port));
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            System.out.println(n.getDisplayName());
            System.out.println(n.getName());
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                peerIP = i.getHostAddress();
                System.out.println(i.getHostAddress());
            }
        }
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
                //createIncomingConnection(ic.getSocket().getInetAddress().getHostAddress(), ic.getSocket().getPort());
            }
        } catch (Exception e) {e.printStackTrace();}
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
        ic.start();
    }


    public void sendPingMessage(String message){
        // Send message to all known outgoing connections
        try {
            outgoingConnections.forEach(t ->
                    t.getPrintWriter().println(new Ping(1, 3).createPing()));}
        catch (Exception e) {e.printStackTrace();}
    }


    public void sendPongMessage(int port){
        // Send message to all known outgoing connections

        try {
            outgoingConnections.forEach(t ->
            {
                try {
                    t.getPrintWriter().println(new Pong(InetAddress.getLocalHost().getHostAddress(), port).createPong());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });}
        catch (Exception e) {e.printStackTrace();}
    }
}
