package de.hu.p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class Server extends Thread {
    private ServerSocket ss;
    private Set<ServerThreadPool> serverThreadPool = new HashSet<ServerThreadPool>();

    public Server(String port) throws IOException {
        ss = new ServerSocket(Integer.valueOf(port));
    }

    public void run(){
        try {
            while(true){
                ServerThreadPool stp = new ServerThreadPool(ss.accept(), this);
                serverThreadPool.add(stp);
                stp.start();
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public Set<ServerThreadPool> getServerThreadPool() {
        return serverThreadPool;
    }

    public void send(String message) {
        try { serverThreadPool.forEach(t -> t.getPrintWriter().println(message));}
        catch (Exception e) {e.printStackTrace();}
    }
}
