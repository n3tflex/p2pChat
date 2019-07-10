package de.hu.p2p;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;


public class IncomingConnection extends Thread {
    private BufferedReader br;
    private Servent servent;
    private String ip;
    public IncomingConnection(Socket s, Servent servent) throws IOException{
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.servent = servent;
        this.ip=(((InetSocketAddress) s.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
    }

    public void run(){
        boolean run = true;
        while(run) {
            try {
                // JsonReader detects new incoming message
                JsonObject jo = Json.createReader(br).readObject();
                if(jo.containsKey("username")){
                    // Print to command line
                    System.out.println("["+jo.getString("username")+"]: " +jo.getString("message"));
                } else if(jo.containsKey("port")){
                    System.out.println("new Ping");
                    servent.sendPongMessage(1234);
                } else if(jo.containsKey("ttl")){
                    System.out.println("new Ping");

                }
            } catch (Exception e){
                run = false;
                interrupt();
            }
        }
    }


}
