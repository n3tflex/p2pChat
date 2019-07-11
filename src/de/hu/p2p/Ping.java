package de.hu.p2p;

import javax.json.Json;
import java.io.StringWriter;
import java.util.UUID;

public class Ping {
    private String ID = Main.ID;
    private String type = "ping";
    private String messageID;
    private int TTL;
    private int hopCount;

    public Ping(){  // Create a message object
        this.TTL = Main.ttl;
        this.hopCount = Main.hop_count;
    }

    public String createPing(){
        StringWriter sw = new StringWriter();
        Json.createWriter(sw).writeObject(Json.createObjectBuilder()
                .add("messageType", type)
                .add("ID", ID)
                .add("messageID", UUID.randomUUID().toString())
                .add("ttl", TTL)
                .add("hopCount", hopCount)
                .build());
        return sw.toString();
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public int getTTL() {
        return TTL;
    }

    public void setTTL(int TTL) {
        this.TTL = TTL;
    }

    public int getHopCount() {
        return hopCount;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }
}
