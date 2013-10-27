package com.netapps.project2.universalremote;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.concurrent.SynchronousQueue;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;


/**
 * SensorReader - a class extending thread which contains a timer in the form of a 
 * threaded handler postDelay call, which reads from all three sensors, packages the 
 * information into a SensorData object, serializes it, and then enters it into a 
 * synchronous queue for transmission via UDP to the web server at fixed intervals.
 * 
 * @author Chris McCarty
 */
public class SensorReader extends Thread
{
	private SynchronousQueue<byte[]>	outgoing_data_;
	private ByteArrayOutputStream 		baos_;
	private ObjectOutput 				oos_;
	private SensorManager 				sensor_manager_;
	
	private Accelerometer 				accelerometer_;
	private Gyroscope					gyroscope_;
	private Magnetometer				magnetometer_;
	
	private Handler 					handler_;
	private Runnable 					runnable_;
	
	private int 			delay_ = 150;
	private long 			start_time_;
	
	private double[] 		buffer_a_;
	private double[] 		buffer_g_;
	private double[] 		buffer_m_;
	
	
	public SensorReader(SynchronousQueue<byte[]> q, SensorManager sm){
		outgoing_data_ = q;
		handler_ = new Handler();
		
		sensor_manager_ = sm;
		
		accelerometer_ = new Accelerometer(sensor_manager_);
		gyroscope_ = new Gyroscope(sensor_manager_);
		magnetometer_ = new Magnetometer(sensor_manager_);
		
		// this is the timer which creates, serializes, and sends a SensorData
		//     object at a fixed rate
		runnable_ = new Runnable(){
			@Override
		    public void run(){
				try {
					baos_ = new ByteArrayOutputStream();
					oos_ = new ObjectOutputStream(baos_); 
					oos_.writeObject(getSensorData());
					oos_.close();
				}catch(Exception e){
					Log.e("!!!", "SensorReader: " + e.toString());
				}				
				// sends the serialized SensorData object using the synchronous queue
				outgoing_data_.offer(baos_.toByteArray());
				// sets the runnable to run again in "delay_" milliseconds
				handler_.postDelayed(this, delay_);
		    }
		};
	}
	
	@Override
    public void run(){
		startReadingSensors();
    }

	public void startReadingSensors(){
		if(runnable_ == null)
			Log.e("!!!", "SensorReader: start running with null runnable / thread");
		else{
			start_time_ = System.currentTimeMillis();
			accelerometer_.startListening();
			gyroscope_.startListening();
			magnetometer_.startListening();
			runnable_.run();
		}
	}
	
	public void stopReadingSensors(){
		handler_.removeCallbacks(runnable_);
		accelerometer_.stopListening();
		gyroscope_.stopListening();
		magnetometer_.stopListening();
	}
	
	
	
	//========================================================================//
    //============================Helper Functions============================//	
	// creates a new SensorData object using the sensor data from the accelerometer, 
	//     gyroscope, and magnetometer
	private SensorData getSensorData(){
		buffer_a_ = Accelerometer.getData();
		buffer_g_ = Gyroscope.getData();
		buffer_m_ = Magnetometer.getData();
		
		return new SensorData(buffer_a_[0], buffer_a_[1], buffer_a_[2], buffer_g_[0], buffer_g_[1], buffer_g_[2],
				buffer_m_[0], buffer_m_[1], buffer_m_[2], ((long)(System.currentTimeMillis())) - start_time_);
	}
}
