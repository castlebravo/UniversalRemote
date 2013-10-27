package com.netapps.project2.universalremote;

import java.util.ArrayList;



public class UDPListener
{
    private ArrayList<Thread> threads_;
    public static ClientManager manager_;
    public static int port_;
    
    
    public UDPListener(int port){
        threads_ = new ArrayList<>();
        manager_ = new ClientManager();
        port_ = port;
    }
    
    // adds a mobile client to the manager and starts the UDP listener
    public void openUDPPort(){
        manager_.addClient();
        threads_.add(new Thread(new UDPRunnable(port_, manager_.getTopClient())));
        threads_.get(threads_.size()-1).start();
    }
}
