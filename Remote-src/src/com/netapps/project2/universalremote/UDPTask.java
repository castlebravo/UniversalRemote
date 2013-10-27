package com.netapps.project2.universalremote;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.SynchronousQueue;
import android.os.AsyncTask;
import android.util.Log;


/**
 * UDPTask - an extension of an AsyncTask which constantly attempts to take a 
 * serialized SensorData object from a synchronous queue, and then transmit it
 * to the web server via UDP.
 * 
 * @author Chris McCarty
 */
public class UDPTask extends AsyncTask<Void, Integer, Void>
{	
	private SynchronousQueue<byte[]> outgoing_data_;
	private DatagramSocket server_;
	private byte[] buffer_;
	
	private int port_;
	private String ip_str_;
	private InetAddress ip_;
	
	
	public UDPTask(SynchronousQueue<byte[]> q, String ip, int p){
		ip_str_ = ip;
		port_ = p;
		outgoing_data_ = q;
	}

	@Override
	protected Void doInBackground(Void... v){
		try {
			ip_ = InetAddress.getByName(ip_str_);
			server_ = new DatagramSocket();
			
			// loops until cancelled
			for(int i = 1; !this.isCancelled(); ++i){
				// blocking call attempting to take from the synchronized queue
				buffer_ = outgoing_data_.take();
				// send the serialized sensor data to the server
				server_.send(new DatagramPacket(buffer_,
						buffer_.length, ip_, port_));
				publishProgress(i);		// used for debugging
			}
		}catch(Exception e){
			Log.e("!UDPTask!", e.toString());
		}
		return null;
	}

	@Override	// only used for debugging
	protected void onProgressUpdate(Integer... progress){
		if(progress[0] % 10 == 0){
			//Log.d("!!!", "UDPTask: " + progress[0] + " data points");
		}
    }
}
