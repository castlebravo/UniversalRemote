package com.netapps.project2.universalremote;

import java.util.HashMap;

import com.netapps.project2.universalremote.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * MainActivity - main activity for the Universal Remote application. Acts as a 
 * home page with buttons to the "View Data", "Stream Sensor Data", and "Set 
 * Connection Information" sub activities.
 * 
 * @author Chris McCarty
 */
public class MainActivity extends Activity
{
	private final int 	CONNECT_ACTIVITY_REQUEST = 99;
	private Button 		btn_set_addr_,
						btn_start_rec_,
						btn_view_data_;
	
	private boolean 	is_connected_ = false;
	private String[] 	connection_info_;
	
	public static HashMap<String, String> 	command_queries_;
	public static String[] 					commands_ = 
			{"request_udp_port", "request_sensor_data_clear"};
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// always starts the ConnectionActivity unless a connection has
		//     been previously established
		if(!is_connected_){
			startActivityForResult(new Intent(getApplicationContext(),
					ConnectActivity.class), CONNECT_ACTIVITY_REQUEST);
		}
		setContentView(R.layout.activity_main);
		
		btn_set_addr_	= (Button) findViewById(R.id.btn_set_addr);
		btn_start_rec_	= (Button) findViewById(R.id.btn_start_rec);
		btn_view_data_	= (Button) findViewById(R.id.btn_view_data);
		
		// sets the connection information button listener
		btn_set_addr_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view){
				// manually starts the ConnectionActivity
				startActivityForResult(new Intent(getApplicationContext(),
						ConnectActivity.class), CONNECT_ACTIVITY_REQUEST);
			}
		});
		
		// sets the start streaming data button listener
		btn_start_rec_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view){
				// starts the SensorActivity
		   		Intent i = new Intent(getApplicationContext(), SensorActivity.class);
		   		i.putExtra("connection_info", connection_info_);
		    	startActivity(i);
			}
		});
		
		// sets the view data button listener
		btn_view_data_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view){
				// starts the default web view activity with the URL to the charts hosted
				//     by the web server
				Intent i = new Intent(Intent.ACTION_VIEW, 
				    Uri.parse("http://" + connection_info_[0] + ":" + connection_info_[1]));
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_quit, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		// the quit option closes the application
	    if(item.getItemId() == R.id.menu_quit){
		    finish();
	        return true;
	    }else
	    	return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
			// if we are receiving a result from the ConnectActivity
			case CONNECT_ACTIVITY_REQUEST:
				if(resultCode == RESULT_OK){
					// extract the connection information
					connection_info_ = data.getStringArrayExtra("connection_info");
					// get the exposed API and save it in the hash map
					new NetworkTask(connection_info_, NetworkTask.GET_API).execute(commands_);
					is_connected_ = true;
					
			    }else if(resultCode == RESULT_CANCELED){
			        finish();
			    }else
			    	Log.e("!!!", "MainActivity: Unknown resultCode");
				break;
			default:
				Log.e("!!!", "OnActivityResult - RequestCode: " + requestCode + ", ResultCode: " + resultCode);				
		}
	}
}
