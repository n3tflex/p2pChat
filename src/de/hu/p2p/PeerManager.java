package de.hu.p2p;

import javax.json.Json;
import java.io.IOException;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class PeerManager extends Thread {
    private Set<PeerIO> peers = new HashSet<>();
    private ServerSocket serverSocket;
    private int port=0;
    // test connections and close bad ones
    // stop peer process (close socket, peer should stop)

    public PeerManager(int port) throws IOException {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Couldn't create Server Socket: " + port);
        }
    }

    public void connect_to_peer(String ip, int port) {
        try {
            PeerIO ic = new PeerIO(new Socket(ip,port));
            System.out.println("New Connection: " + ic.getSocket().getInetAddress() + " Port: " + ic.getSocket().getPort() + " id: " + ic.getID());
            peers.add(ic);
            ic.start();
            pairing(ic);
        } catch (Exception e) {
            System.out.println("No peer at " + ip + ":" + port + "found.");
        }
    }

    public void send_all(String message) {
        for (PeerIO peer : peers) {
            peer.send(message);
        }
    }

    public void pairing(PeerIO peer){
        StringWriter sw = new StringWriter();
        Json.createWriter(sw).writeObject(Json.createObjectBuilder()
                .add("messageType", "ping")
                .add("ID", peer.getID())
                .add("publicIP", serverSocket.getInetAddress().getHostAddress() )
                .add("publicPort", port)
                .build());
        peer.send(sw.toString());
    }
    public void run() {
        try {
            while (true) {
                // Is called after another peer is trying to create connection in their joinNetwork() method
                // Via this connection we send the messages
                PeerIO ic = new PeerIO(serverSocket.accept());
                System.out.println("New Connection: " + ic.getSocket().getInetAddress() + " Port: " + ic.getSocket().getPort() + " id: " + ic.getID());
                peers.add(ic);
                ic.start();
                pairing(ic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
