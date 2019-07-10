package de.hu.p2p;

import javax.json.Json;
import java.io.StringWriter;
import java.util.UUID;

public class Pong {
    private String ip;
    private int port;
    private String messageID;

    public Pong(String ip, int ttl, String messageID){  // Create a message object
        this.ip = ip;
        this.port = ttl;
        this.messageID = messageID;
    }

    public String createPong(){
        StringWriter sw = new StringWriter();
        Json.createWriter(sw).writeObject(Json.createObjectBuilder()
                .add("messageID", messageID)
                .add("port", port)
                .add("ip", ip)
                .build());
        return sw.toString();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
}
