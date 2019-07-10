package de.hu.p2p;

import javax.json.Json;
import java.io.*;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter username and port for this peer (space separated)");
        String[] setup = br.readLine().split(" ");
        Server server = new Server(setup[1]);
        server.start();
        new Main().updateListingPeers(br, setup[0], server);
    }

    public void updateListingPeers(BufferedReader br, String username, Server server) throws Exception {
        System.out.println("Enter hostename and port (space separated localhost:9000 localhost:90001) (s to skip)");
        String input = br.readLine();
        String[] setup = input.split(" ");
        if(!input.equals("s")) for (int i = 0; i < setup.length; i++){
            String[] address = setup[i].split(":");
            Socket s = null;
            try {
                s = new Socket(address[0], Integer.valueOf(address[1]));
                new Peer(s).start();
            } catch(Exception e) {
                if(s != null) s.close();
                else System.out.println("invalid input");
            }
        }
        send(br, username, server);
    }

    public void send(BufferedReader br, String username, Server server){
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
                updateListingPeers(br, username, server);
            } else {
                StringWriter sw = new StringWriter();
                Json.createWriter(sw).writeObject(Json.createObjectBuilder()
                        .add("username", username)
                        .add("message", input)
                        .build());
                server.send(sw.toString());
            }
        }
        System.out.println("EXIT");
        System.exit(0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
