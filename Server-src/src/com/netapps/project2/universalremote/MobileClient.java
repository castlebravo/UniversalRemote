package com.netapps.project2.universalremote;

import java.net.InetAddress;
import java.util.ArrayList;


public class MobileClient
{
    private ArrayList<SensorData> sensor_data_;
    //private InetAddress ip_address_;      // for later use
    //private int port_;                    // for later use
    
    private int MAX_DATA_POINTS = 200;
    
    
    public MobileClient(){
        sensor_data_ = new ArrayList<SensorData>();
    }
    
    public void setSourceInformation(InetAddress ip, int p){
        //ip_address_ = ip;     // for later use
        //port_ = p;            // for later use
    }
    
    public void addSensorData(SensorData sd){
        if(sensor_data_.size() >= MAX_DATA_POINTS)
            sensor_data_.remove(0);
        sensor_data_.add(sd);
    }
    
    public String getFormattedData(){
        String toReturn = 
        "google.visualization.Query.setResponse(\n" +
        "  {'version':'0.6','reqId':'0','status':'ok',\n" +
        "    'table':{\n" +
        "     'cols':[{'id':'time','label':'Time','type':'number'}, \n" +
               buildChartColumns("a", "component") + ",\n" +
               buildChartColumns("g", "component") + ",\n" +
               buildChartColumns("m", "component") + "],\n" +
        "     'rows':[";
        
        // gets a static array representing the sensor data array list and copy
        //     the data from each object into the correct string form
        Object[] data = sensor_data_.toArray();
        for(int i = 0; i < data.length; ++i){
            SensorData sd = (SensorData)data[i];
            toReturn += "{'c':[{'v':" + sd.getMs_time() + "},{'v':" + 
                  sd.getAcl_x() + "},{'v':" + sd.getAcl_y() + "},{'v':" + 
                  sd.getAcl_z() + "},{'v':" + sd.getGyro_x() + "},{'v':" + 
                  sd.getGyro_y() + "},{'v':" + sd.getGyro_z() + "},{'v':" + 
                  sd.getMag_x() + "},{'v':" + sd.getMag_y() + "},{'v':" + 
                  sd.getMag_z() + "}]}";
            if(i < data.length - 1)
                toReturn += ",";
        }
        toReturn += 
        "    ]\n" +
        "  }\n" +
        "});";
        return toReturn;        
    }
    
    public void clearData(){
        sensor_data_.clear();
    }
    
    
    
    //========================================================================//
    //============================Helper Functions============================//
    private String buildChartColumns(String postfix, String label){
        return
    "             {'id':'x" + postfix + "','label':'x " + label + "','type':'number'},\n" +
    "             {'id':'y" + postfix + "','label':'y " + label + "','type':'number'},\n" +
    "             {'id':'z" + postfix + "','label':'z " + label + "','type':'number'}";
    }
}
