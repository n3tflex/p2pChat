package de.hu.p2p;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.*;
import java.net.Socket;

// TODO pairing, getIP and port
public class PeerIO extends Thread {
    // send
    // read
    // be controlled
    // read in run
    // send in send
    // be controlled in run
    private BufferedReader br;

    private PipedOutputStream pipe_control_ext;
    private PipedInputStream pipe_control_int;

    private Socket socket;
    private PrintWriter pw;
    private int peerID;
    private String peerIP;
    private int peerPort;

    public PeerIO(Socket s) throws IOException {
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        pipe_control_ext = new PipedOutputStream();
        pipe_control_int = new PipedInputStream(pipe_control_ext);
        socket=s;
        pw = new PrintWriter(s.getOutputStream(), true);
        peerID=(int)(Math.random() * 9999);
    }

    // get Pipe you can read from
    public PipedInputStream getPipeOut() {
        return pipe_control_int;
    }

    public void close_peer(){
        try {
            pipe_control_ext.close();
            socket.close();
            System.out.println("Peer ID: " + peerID + " closed.");
        }
        catch (Exception e) {}
    }
    public int getID(){
        return peerID;
    }

    // man sollte send verwenden
    public PrintWriter getPrintWriter(){return pw;}

    public Socket getSocket(){
        return socket;
    }
    // This method is called when the user enters a new message to the commandline
    public void send(String message) {
        try {
            pw.println(message);
            System.out.println(message + " An Peer: " + peerID +" gesendet.");
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public void run() {
        boolean run = true;
        while (run) {
            try {
                if (br.ready()) {
                    // JsonReader detects new incoming message
                    JsonObject jo = Json.createReader(br).readObject();
                    // leite an pipe/overlay weiter
                    Json.createWriter(pipe_control_ext).writeObject(jo);

                    // da ich noch nichts mit der pipe mache
                    if (jo.containsKey("username")) {
                        // Print to command line
                        System.out.println("peerIO [" + jo.getString("username") + "]: " + jo.getString("message"));
                    }
                    if(jo.getString("messageType","").equals("ping")){
                        peerIP=jo.getString("publicIP", "");
                        peerPort=jo.getInt("publicPort", 0);
                        System.out.println("peerIO ping peerIP: "+peerIP + " peerPort: " + peerPort);
                    }
                }
            } catch (Exception e) {
                run = false;
                interrupt();
            }
        }
    }
}
