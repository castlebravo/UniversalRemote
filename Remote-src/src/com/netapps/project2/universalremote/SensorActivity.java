package com.netapps.project2.universalremote;

import java.util.concurrent.SynchronousQueue;
import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;


/**
 * SensorActivity - sensor activity for the Universal Remote application. Acts as a 
 * menu from which sensor data streaming may be started, or sensor data may be cleared
 * on the remote web server.
 * 
 * @author Chris McCarty
 */
public class SensorActivity extends Activity
{
	private Button 				btn_return_home_,
								btn_clr_;
	public ToggleButton			btn_start_stop_;
	private String[] 			connection_info_;
	private boolean 			is_streaming_;
	
	private SynchronousQueue<byte[]> 	outgoing_data_;
	private UDPTask 					udp_task_;
	private SensorReader 				sensor_reader_;
	
	public static int 			port_ = -1;
	public static Context 		context_;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		
		context_ 			= getApplicationContext();
		is_streaming_ 		= false;
		outgoing_data_ 		= new SynchronousQueue<byte[]>();
		
		btn_return_home_	= (Button) findViewById(R.id.btn_return_home);
		btn_clr_			= (Button) findViewById(R.id.btn_clr);
		btn_start_stop_		= (ToggleButton) findViewById(R.id.btn_start_stop);
		
		connection_info_ 	= getIntent().getStringArrayExtra("connection_info");
				
		// requests the UDP port using the API
		new NetworkTask(connection_info_, NetworkTask.GET_UDP_PORT, btn_start_stop_).execute(
						MainActivity.command_queries_.get("request_udp_port"));
		
		// sets the return home button listener
		btn_return_home_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view){
		   		finish();
			}
		});
		
		// sets the clear data button listener
		btn_clr_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view){
				requestSensorDataClear();
			}
		});
		
		// sets the start / stop button listener
		btn_start_stop_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view){
		   		if(is_streaming_)
		   			stopStreaming();
		   		
		   		else if(!is_streaming_)
		   			startStreaming();
			}
		});
	}
	
	@Override
	protected void onDestroy(){
		if(sensor_reader_ != null)
			sensor_reader_.stopReadingSensors();
		if(udp_task_ != null)
			udp_task_.cancel(true);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed(){
		finish();
	    super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		// the return home option returns to the home screen
	    if(item.getItemId() == R.id.menu_home){
		    finish();
	        return true;
	    }else
	    	return super.onOptionsItemSelected(item);
	}
	
	
	
	//========================================================================//
    //============================Helper Functions============================//
	// starts the timer and UDP task running while hiding the clear data button
	private void startStreaming(){
		requestSensorDataClear();
		udp_task_ = new UDPTask(outgoing_data_, connection_info_[0], port_);
		sensor_reader_ = new SensorReader(outgoing_data_, 
					(SensorManager) getSystemService(Context.SENSOR_SERVICE));
   		udp_task_.execute();
   		sensor_reader_.start();
   		btn_clr_.setVisibility(View.GONE);
   		is_streaming_ = true;
	}
	
	// stops the timer and UDP task while making the clear data button visible
	private void stopStreaming(){
		udp_task_.cancel(true);
		sensor_reader_.stopReadingSensors();
		btn_clr_.setVisibility(View.VISIBLE);
		is_streaming_ = false;
	}
	
	// clears the sensor data using the API
	private void requestSensorDataClear(){
		new NetworkTask(connection_info_, NetworkTask.CLEAR_SENSOR_DATA).execute(
				MainActivity.command_queries_.get("request_sensor_data_clear"));
	}
}
