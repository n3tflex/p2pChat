package de.hu.p2p;

import javax.json.Json;
import java.io.*;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter username and port for this peer (space separated)");
        String[] setup = br.readLine().split(" ");
        Servent servent = new Servent(setup[1]);
        servent.start();
        new Main().joinNetwork(br, setup[0], servent);
    }

    // Called to join the network with a defined username und servent with choosen port
    public void joinNetwork(BufferedReader br, String username, Servent servent) throws Exception {
        System.out.println("Enter hostname and port (space separated localhost:9000 localhost:90001) (s to skip)");
        String input = br.readLine();
        String[] setup = input.split(" ");
        if(!input.equals("s")) for (int i = 0; i < setup.length; i++){
            String[] address = setup[i].split(":");
            Socket socket = null;
            try {
                // Creates a new incoming connection to receive messages from
                socket = new Socket(address[0], Integer.valueOf(address[1]));
                new IncomingConnection(socket).start();
            } catch(Exception e) {
                if(socket != null) socket.close();
                else
                    System.out.println("invalid input");
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
                joinNetwork(br, username, servent);
            } else {
                // Create a message object
                StringWriter sw = new StringWriter();
                Json.createWriter(sw).writeObject(Json.createObjectBuilder()
                        .add("username", username)
                        .add("message", input)
                        .build());
                // Send message to all known outgoing connections
                servent.send(sw.toString());
            }
        }
        System.out.println("EXIT");
        System.exit(0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
