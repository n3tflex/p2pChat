package de.hu.p2p;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class IncomingConnection extends Thread {
    private BufferedReader br;
    private Servent servent;
    private Socket socket;
    private String ip;
    private PrintWriter pw;
    public IncomingConnection(Socket s, Servent servent) throws IOException{
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.pw = new PrintWriter(s.getOutputStream(), true);
        this.servent = servent;
        this.socket = s;
        this.ip = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
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
                    System.out.println("new pong");
                    servent.addIncomingConnection(ip , jo.getInt("port"));
                } else if(jo.containsKey("ttl")){
                    System.out.println("new Ping");

                }
            } catch (Exception e){
                run = false;
                interrupt();
            }
        }
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }
}
