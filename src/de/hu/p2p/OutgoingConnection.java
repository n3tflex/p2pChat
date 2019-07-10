package de.hu.p2p;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class OutgoingConnection extends Thread {
    private Servent servent;
    private Socket socket;
    private PrintWriter pw;
    public OutgoingConnection(Socket socket, Servent servent){
        this.servent = servent;
        this.socket = socket;
    }

    public void run(){
        try{
            // Creates a new BufferedReader listening to new messages from the command line
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            while(true) {
                JsonObject jo = Json.createReader(br).readObject();
                if(jo.containsKey("username")){
                    servent.sendChatMessage(jo.toString());
                } else if(jo.containsKey("ttl")){
                    servent.sendPingMessage(jo.toString());
                } else if(jo.containsKey("port")){
                    String id = Main.ID;
                    if(id.equals(jo.getString("ID"))){
                        System.out.println("jo");
                    } else {
                      //  servent.sendPongMessage(Main.port);
                    }
                }
            }
        } catch (Exception e) { servent.getOutgoingConnections().remove(this); }
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }

    public Socket getSocket(){
        return socket;
    }
}
