package de.hu.p2p;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class OutgoingConnection extends Thread {
    private BufferedReader br;
    public OutgoingConnection(Socket s) throws IOException{
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

    public void run(){
        boolean run = true;
        while(run) {
            try {
                JsonObject jo = Json.createReader(br).readObject();
                if(jo.containsKey("username")){
                    System.out.println("["+jo.getString("username")+"]: " +jo.getString("message"));
                }
            } catch (Exception e){
                run = false;
                interrupt();
            }
        }
    }
}
