package de.hu.p2p;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class IncomingConnection extends Thread {
    private BufferedReader br;
    public IncomingConnection(Socket s) throws IOException{
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

    public void run(){
        boolean run = true;
        while(run) {
            try {
                if(!br.ready()) {
                    //System.out.println("Sleeping");
                    Thread.sleep(1000);
                    continue;
                }
                // JsonReader detects new incoming message
                JsonObject jo = Json.createReader(br).readObject();
                if(jo.containsKey("username")){
                    // Print to command line
                    System.out.println("["+jo.getString("username")+"]: " +jo.getString("message"));
                }
            } catch (Exception e){
                run = false;
                interrupt();
            }
        }
    }
}
