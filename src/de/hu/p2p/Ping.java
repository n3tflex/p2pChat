package de.hu.p2p;

import javax.json.Json;
import java.io.StringWriter;
import java.util.UUID;

public class Ping {
    private String messageID;
    private int TTL;
    private int hopCount;

    public Ping(int ttl, int hopCount){  // Create a message object
        this.TTL = ttl;
        this.hopCount = hopCount;
    }

    public String createPing(){
        StringWriter sw = new StringWriter();
        Json.createWriter(sw).writeObject(Json.createObjectBuilder()
                .add("messageID", UUID.randomUUID().toString())
                .add("TTL", TTL)
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
