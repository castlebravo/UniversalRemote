package com.netapps.project2.universalremote;


/**
 * SensorData - a custom data structure used to organize a set of sensor values. This class is
 * serializable, and is transmitted via UDP to the web server.
 * 
 * @author Chris McCarty
 */
public class SensorData implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private double acl_x;
    private double acl_y;
    private double acl_z;
    
    private double gyro_x;
    private double gyro_y;
    private double gyro_z;
    
    private double mag_x;
    private double mag_y;
    private double mag_z;
    
    private long ms_time;

    public SensorData(double acl_x, double acl_y, double acl_z,
            double gyro_x, double gyro_y, double gyro_z,
            double mag_x, double mag_y, double mag_z, long ms_time) {
        this.acl_x = acl_x;
        this.acl_y = acl_y;
        this.acl_z = acl_z;
        this.gyro_x = gyro_x;
        this.gyro_y = gyro_y;
        this.gyro_z = gyro_z;
        this.mag_x = mag_x;
        this.mag_y = mag_y;
        this.mag_z = mag_z;
        this.ms_time = ms_time;
    }
    

    public long getMs_time(){
        return ms_time;
    }
    public void setMs_time(long ms_time){
        this.ms_time = ms_time;
    }

    
    public double getAcl_x(){
        return acl_x;
    }
    public void setAcl_x(double acl_x){
        this.acl_x = acl_x;
    }

    public double getAcl_y(){
        return acl_y;
    }
    public void setAcl_y(double acl_y){
        this.acl_y = acl_y;
    }

    public double getAcl_z(){
        return acl_z;
    }
    public void setAcl_z(double acl_z){
        this.acl_z = acl_z;
    }

    
    public double getGyro_x(){
        return gyro_x;
    }
    public void setGyro_x(double gyro_x){
        this.gyro_x = gyro_x;
    }

    public double getGyro_y(){
        return gyro_y;
    }
    public void setGyro_y(double gyro_y){
        this.gyro_y = gyro_y;
    }

    public double getGyro_z(){
        return gyro_z;
    }
    public void setGyro_z(double gyro_z){
        this.gyro_z = gyro_z;
    }
    
    public double getMag_x() {
        return mag_x;
    }
    public void setMag_x(double mag_x) {
        this.mag_x = mag_x;
    }

    public double getMag_y() {
        return mag_y;
    }
    public void setMag_y(double mag_y) {
        this.mag_y = mag_y;
    }

    public double getMag_z() {
        return mag_z;
    }
    public void setMag_z(double mag_z) {
        this.mag_z = mag_z;
    }
}
