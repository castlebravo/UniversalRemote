package com.netapps.project2.universalremote;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;



public class UDPRunnable implements Runnable
{
    private int port_;
    private MobileClient client_;
    
    
    public UDPRunnable(int p, MobileClient mc){
        port_ = p;
        client_ = mc;
    }
    
    @Override
    public void run(){
        try{
            DatagramSocket serverSocket = new DatagramSocket(port_);
            byte[] receiveData = new byte[1028];
            
            // get sensor data fromthe app
            DatagramPacket receivePacket = 
                        new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            
            // handle incoming data
            ObjectInputStream ois_ = new ObjectInputStream(new ByteArrayInputStream(receiveData));
            SensorData received = (SensorData)ois_.readObject();
            ois_.close();
            
            // adds the newly received sensor data
            client_.addSensorData(received);
            
            // sets client connection information
            client_.setSourceInformation(receivePacket.getAddress(),
                        receivePacket.getPort());
            
            // continually listens for incoming packets
            while(true){
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);    // is blocking
                
                // converts the bytes to the SensorData object
                ois_ = new ObjectInputStream(new ByteArrayInputStream(receiveData));
                received = (SensorData)ois_.readObject();
                ois_.close();
            
                client_.addSensorData(received);
            }  
        }catch(Exception e){
            System.out.println("ERROR in UDPRunnable: " + e.toString());
        }
    }    
}
