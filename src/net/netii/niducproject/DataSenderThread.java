package net.netii.niducproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class DataSenderThread extends Thread implements Runnable {
	private HttpClient httpclient;
	private HttpPost httppost;
	private List<NameValuePair> nameValuePairs;
	
	private final String cashdeskId;
	private final String cardPaid;
	private final String size;
	private final String date;
	private final String time;	
	
	public DataSenderThread(String cashdeskId, String cardPaid, String size, String date, String time){
		this.cashdeskId = cashdeskId;
		this.cardPaid = cardPaid;
		this.size = size;
		this.date = date;
		this.time = time;
		start();
	}
	
	@Override
	public void run() {		
        httpclient = new DefaultHttpClient();
    	
        httppost = new HttpPost("http://niducproject.netii.net/index.php");
            
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("cashdesk_id", cashdeskId));
        nameValuePairs.add(new BasicNameValuePair("card_paid", cardPaid));
        nameValuePairs.add(new BasicNameValuePair("shopping_size", size));
        nameValuePairs.add(new BasicNameValuePair("shopping_date", date));
        nameValuePairs.add(new BasicNameValuePair("shopping_time", time));
        
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);					
		}catch(Exception e){
        	Log.i("sql", e.getMessage());		        
		}
	}

}

