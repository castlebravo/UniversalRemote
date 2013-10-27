package com.netapps.project2.universalremote;

import java.util.ArrayList;



public class ClientManager
{
    private ArrayList<MobileClient> clients_;
    
    
    public ClientManager(){
        clients_ = new ArrayList<>();
    }
    
    public int addClient(){
        clients_.add(new MobileClient());
        return clients_.size() - 1;
    }
    
    public MobileClient getTopClient(){
        return clients_.get(clients_.size() - 1);
    }
}
