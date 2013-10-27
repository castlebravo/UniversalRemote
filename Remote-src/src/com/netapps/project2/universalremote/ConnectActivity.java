package com.netapps.project2.universalremote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * ConnectActivity - connection activity for the Universal Remote application. Acts as a 
 * menu from which the IP and TCP port can be entered, and the connection validated.
 * 
 * @author Chris McCarty
 */
public class ConnectActivity extends Activity
{
	private Button 				btn_connect_;
	private EditText			field_ip_,
								field_port_;
	
	private String 				ip_str_ = "",
								port_str_ = "";
	
	public static LinearLayout 	connect_elements_,
								success_elements_;
	public static ProgressBar 	connect_progress_;
	public static Button 		btn_continue_;
	public static Context 		context_;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		context_ = getApplicationContext();
		
		btn_connect_	= (Button) findViewById(R.id.btn_connect);
		btn_continue_	= (Button) findViewById(R.id.btn_continue);
		field_ip_ 		= (EditText) findViewById(R.id.field_ip);
		field_port_ 	= (EditText) findViewById(R.id.field_port);
		
		connect_elements_ = (LinearLayout) findViewById(R.id.connection_elements);
		connect_progress_ = (ProgressBar) findViewById(R.id.connect_progress);
		success_elements_ = (LinearLayout) findViewById(R.id.success_elements);
		
		
		// sets the connect button listener
		btn_connect_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view){
				if(informationIsGood(field_ip_.getText().toString(),
						field_port_.getText().toString())){
					
					ip_str_ = field_ip_.getText().toString();
					port_str_ = field_port_.getText().toString();
					
					// attempts to validate the specified connection information
					new NetworkTask(new String[]{ip_str_, port_str_}, 
							NetworkTask.CHECK_CONNECTION).execute();
				}else{
					Toast.makeText(getApplicationContext(),
						"Please enter a valid ip address and port number", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		// sets the continue button listener
		btn_continue_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view){
				// closes this activity with the specified IP address and port put
				//     with the intent for retrieval in MainActivity
				Intent i = new Intent();
				i.putExtra("connection_result", "success");
				i.putExtra("connection_info", 
					new String[]{field_ip_.getText().toString(), field_port_.getText().toString()});
				setResult(RESULT_OK, i);     
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed(){
		// specifies RESULT_CANCELLED for MainActivity to see
		//     and end the application, as connection information
		//     had not been entered and validated
	    Intent i = new Intent();
	    i.putExtra("connection_result", "back_button");
	    setResult(Activity.RESULT_CANCELED, i);
	    super.onBackPressed();
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
	    	// specifies RESULT_CANCELLED for MainActivity to see
			//     and end the application, as connection information
			//     had not been entered and validated
	    	Intent i = new Intent();
		    i.putExtra("connection_result", "quit_button");
		    setResult(Activity.RESULT_CANCELED, i);
		    finish();
	        return true;
	    }else
	    	return super.onOptionsItemSelected(item);
	}
	
	
	
    //========================================================================//
    //============================Helper Functions============================//
	// validates the IPV4 address and TCP port based on two regexes
	private boolean informationIsGood(String ip, String port){
		if(!ip.matches("[0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}"))
			return false;
		if(!port.matches("[0-9]{1,5}"))
			return false;
		return true;
	}
}
