package com.netapps.project2.universalremote;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


/**
 * Gyroscope - gyroscope sensor class for the Universal Remote application. Currently it
 * is nearly identical to the Accelerometer and Magnetometer classes, but eventually gyroscope
 * specific pre-processing will occur here.
 * 
 * @author Chris McCarty
 */
public class Gyroscope implements SensorEventListener
{
	private SensorManager 	sensor_manager_;
	private Sensor 			gyroscope_;
	private int 			rate_ = 50000; 	// 20 times / second
	private static double 	x = 0, 
							y = 0, 
							z = 0;
	
	
	public Gyroscope(SensorManager sm){
		sensor_manager_ = sm;
		
		if(sensor_manager_.getSensorList(Sensor.TYPE_GYROSCOPE).size() > 0){
			gyroscope_ = sensor_manager_.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		}else
			Log.e("!!!", "Gyroscope: no gyroscope detected");
	}
	
	public void startListening(){
		sensor_manager_.registerListener(this, gyroscope_, rate_);
	}

	public void stopListening(){
		sensor_manager_.unregisterListener(this);
	}
	
	public static synchronized double[] getData(){
		return new double[]{x, y, z};
	}

	@Override
	public void onSensorChanged(SensorEvent e){
		if(e.sensor.getType()==Sensor.TYPE_GYROSCOPE){
			x = e.values[0];
			y = e.values[1];
			z = e.values[2];	
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor s, int i){}
}
