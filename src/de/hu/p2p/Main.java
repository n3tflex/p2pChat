package de.hu.p2p;

import javax.json.Json;
import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Main {
    private String[] stablePeers = new String[]{"127.0.0.1:4444"};
    private static boolean newNetwork = true;
    private static Servent servent;
    public static String ID = UUID.randomUUID().toString();
    public static int port;
    public static int ttl;
    public static int hop_count;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter username and port for this peer (space separated)");
        String[] setup = br.readLine().split(" ");
        port = Integer.valueOf(setup[1]);
        servent = new Servent(port);
        servent.start();
        new Main().startPeers(br, setup[0], servent);
    }

    // Called to join the network with a defined username und servent with chosen port
    public void startPeers(BufferedReader br, String username, Servent servent) throws Exception {
        if(!newNetwork){
            for (String stablePeer : stablePeers) {
                String[] url = stablePeer.split(":");
                servent.addConnection(url[0], Integer.valueOf(url[1]));
            }
        }
        startChat(br, username, servent);
    }

    public void startChat(BufferedReader br, String username, Servent servent){
        try {
            System.out.println("Send messages (e to exit and c to setup ne clients):");
        boolean run = true;
        while(run) {
            String input = br.readLine();
            if (input.equals("e")) {
                run = false;
                break;
            } else if (input.equals("c")) {
                System.out.println("updateListingPeers");
                startPeers(br, username, servent);
            } else {
                // Create a message object
                StringWriter sw = new StringWriter();
                Json.createWriter(sw).writeObject(Json.createObjectBuilder()
                        .add("username", username)
                        .add("message", input)
                        .add("ID", Main.ID)
                        .build());
                // Send message to all known outgoing connections
                servent.sendChatMessage(sw.toString());
            }
        }
        System.out.println("EXIT");
        System.exit(0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
