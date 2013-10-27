package com.netapps.project2.universalremote;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * NetworkTask - an extension of an AsyncTask which is capable of doing several different
 * HTTP GET operations based on a constant integer action specified on instantiation. It is
 * used to read the exposed API of the web server, make specific requests based on the
 * API, and validate that a connection can be made to the specified IP and port.
 * 
 * @author Chris McCarty
 */
public class NetworkTask extends AsyncTask<String, Void, HashMap<String, String>>
{
	public static final int GET_API = 0,			// gets the API from the remote web server
							GET_UDP_PORT = 1,		// uses the API to retrieve the UDP port
							CLEAR_SENSOR_DATA = 2,	// uses the API to request a sensor data clear
							CHECK_CONNECTION = 3;	// validates the connection
	
	private int 			action_;
	private String[] 		connection_info_;
	private String 			base_url_;
	private ToggleButton 	button_;
	
	
	public NetworkTask(String[] info, int action){
		connection_info_ = info;
		base_url_ = info[0] + ":" + info[1];
		action_ = action;
	}
	
	public NetworkTask(String[] info, int action, ToggleButton b){
		button_ = b;
		base_url_ = info[0] + ":" + info[1];
		action_ = action;
	}
	
	@Override
	protected void onPreExecute(){
		switch(action_){
			case CHECK_CONNECTION:
				// hide the normal UI and show the progress bar
				ConnectActivity.connect_elements_.setVisibility(View.GONE);
				ConnectActivity.connect_progress_.setVisibility(View.VISIBLE);
				break;
				
			default:
				break;
		}
	}
	
	@Override
	protected HashMap<String, String> doInBackground(String... params){
		switch(action_){
			case GET_API:
				
				return getCommandQuery(getResponse(base_url_ + "/api"), params);
				
			case GET_UDP_PORT:
				// visits the URL as specified by the API, which displays the UDP port,
				//     and stores it in a hash map
				HashMap<String, String> port = new HashMap<String, String>();
				port.put("port", (getResponse(base_url_ + params[0])).trim());
				return port;
				
			case CLEAR_SENSOR_DATA:
				// visits the URL as specified by the API, which causes the server to
				//     wipe the sensor data
				getResponse(base_url_ + params[0]);
				return null;
				
			case CHECK_CONNECTION:
				// attempts to connect with the given connection information
				HashMap<String, String> status = new HashMap<String, String>();
				try {
					if(InetAddress.getByName(connection_info_[0]).isReachable(3000)){
						new Socket(connection_info_[0], Integer.parseInt(connection_info_[1]));
						status.put("connected","true");
					}
				}catch(ConnectException e){
					status.put("connected","false");
				}catch(Exception e){
					Log.e("!!!", "NetworkTask: " + e.toString());
				}
				return status;
				
			default:
				Log.e("!!!", "NetworkTask: doInBackground - Unknown command given");
				return null;
		}
	}
	
	@Override
	protected void onPostExecute(HashMap<String, String> results){		
		switch(action_){
			case GET_API:
				// sets the API hash map in MainActivity
				MainActivity.command_queries_ = results;
				break;
			
			case GET_UDP_PORT:
				// if the port was received successfully, enable the start button
				if(results.get("port").matches("[0-9]{1,5}")){
					SensorActivity.port_ = Integer.parseInt(results.get("port"));
					while(SensorActivity.port_ < 0){
						Log.e("!!!", "NetworkTask: UDP port was set but change was not reflected, trying again...");
						SensorActivity.port_ = Integer.parseInt(results.get("port"));
					}
					button_.setVisibility(View.VISIBLE);
				// if not, toast an error message
				}else{
					Log.e("!!!", "NetworkTask: port not received correctly - " + results.get("port"));
					Toast.makeText(SensorActivity.context_, "A connection could not be established, " +
							"please go back and enter your connection information", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case CLEAR_SENSOR_DATA:
				break;
				
			case CHECK_CONNECTION:
				// hide the progress bar
				ConnectActivity.connect_progress_.setVisibility(View.GONE);
				if(results.get("connected").equals("true")){
					// show the continue button and text view
					ConnectActivity.success_elements_.setVisibility(View.VISIBLE);
				}else{
					// show the connection information elements and toast an error message
					ConnectActivity.connect_elements_.setVisibility(View.VISIBLE);
					Toast.makeText(ConnectActivity.context_, "A connection could not be established, " +
						"please check your information and try again", Toast.LENGTH_SHORT).show();
				}
				break;
				
			default:
				Log.e("!!!", "NetworkTask: onPostExecute - Unknown command given");
		}
	}
	
	
	
	//========================================================================//
    //============================Helper Functions============================//
	// parses the XML API into a hash map of known commands paired with String queries
	//     to append to the base URL which will cause the command to occur
	private HashMap<String, String> getCommandQuery(String xml, String[] commands){
		HashMap<String, String> command_queries = new HashMap<String, String>();
		for(int i = 0; i < commands.length; ++i){
			int i_start = (xml.indexOf(commands[i]) + commands[i].length() + 33);
			int i_end = xml.indexOf("</query>", i_start);
			command_queries.put(commands[i], xml.substring(i_start, i_end));
		}
		return command_queries;
	}
	
	// executes the HTTP GET to the specified URL and returns the response
	private String getResponse(String url){
		String 			resp = "";
		HttpGet 		request;
		HttpClient 		client = new DefaultHttpClient();
	    HttpResponse 	response;
	    StringBuilder 	builder;
	    BufferedReader 	inputreader;
	    
	    request = new HttpGet("http://" + url);
	    
		try {
			response = client.execute(request);
		    HttpEntity entity = response.getEntity();

		    if(entity != null){
		    	builder = new StringBuilder();
		        InputStream inputstream = entity.getContent();
		        inputreader = new BufferedReader(new InputStreamReader(inputstream));

		        // start retrieving and building the response
		        String line = null;
		        try {
		            while((line = inputreader.readLine()) != null){
		                builder.append(line + "\n");
		            }
		        }catch(Exception e){
		        	Log.e("!!!", "NetworkTask: readLine() - " + e.toString());
		        }finally{
		            try {
		            	inputstream.close();
		            }catch(Exception e){
		            	Log.e("!!!", "NetworkTask: close() - " + e.toString());
		            }
		        }
		        resp = builder.toString();
		    }
		}catch(Exception e){
			Log.e("!!!", "NetworkTask: getResponse() - " + e.toString());
		}
		return resp;
	}
}

