package de.hu.p2p;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

public class Connection extends Thread {
    private Servent servent;
    private Socket socket;
    private PrintWriter pw;
    private String ip;
    private BufferedReader br;

    public Connection(Socket socket, Servent servent){
        this.servent = servent;
        this.socket = socket;
        this.ip=(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
        try {
            this.pw = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            // Creates a new BufferedReader listening to new messages from the command line
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            while (true) {
                JsonObject jo = Json.createReader(br).readObject();
                if (!Main.ID.equals(jo.getString("ID"))) {
                    incoming(jo);
                } else {
                    outgoing(jo);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            servent.getConnections().remove(ip);
        }
    }

    public void outgoing(JsonObject jo) {
        if(jo.containsKey("username")){
            servent.sendChatMessage(jo.toString());
        } else if(jo.getString("messageType").equals("ping")){
            if(!servent.getSeenMessages().contains(jo.getString("messageID"))){
                servent.forwardPingMessage(updatePing(jo));
                servent.getSeenMessages().add(jo.getString("messageID"));
            }
        } else if(jo.getString("messageType").equals("pong")){
        }
    }

    public void incoming(JsonObject jo) {
        if(jo.containsKey("username")){
            System.out.println("["+jo.getString("username")+"]: " +jo.getString("message"));
        } else if(jo.getString("messageType").equals("ping")){
            if(!servent.getSeenMessages().contains(jo.getString("messageID"))){
                servent.forwardPingMessage(updatePing(jo));
                servent.getSeenMessages().add(jo.getString("messageID"));
            }
        } else if(jo.getString("messageType").equals("pong")){
        }
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }

    private String updatePing(JsonObject jo){
        StringWriter sw = new StringWriter();
        Json.createWriter(sw).writeObject(Json.createObjectBuilder()
                .add("messageType", jo.getString("messageType"))
                .add("ID", Main.ID)
                .add("messageID",  jo.getString("messageID"))
                .add("ttl", jo.getInt("ttl") - 1)
                .add("hopCount", jo.getInt("hopCount"))
                .build());
        return sw.toString();
    }
}
