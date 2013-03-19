package com.example.login;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginMainActivity extends Activity {

	ProgressBar pb;
	private static String loginStatus = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
		pb = (ProgressBar)findViewById(R.id.LoginprogressBar);
		pb.setVisibility(View.GONE);
	}

	public void signin(View V) {
		EditText user_id  = (EditText)findViewById(R.id.userIdEditText);
		EditText password = (EditText)findViewById(R.id.passwordEditText);
		
		if(haveNetworkConnection())
		{
			if(user_id.getText().toString().length()<1 || password.getText().toString().length()<1)
			{
				Toast.makeText(this, "Please provide all information.", Toast.LENGTH_LONG).show();
			}
			else 
			{
				pb.setVisibility(View.VISIBLE);
				new MyAsyncTask().execute(user_id.getText().toString(),password.getText().toString());	
			}
		}
		else
		{
			Toast.makeText(this, "Sorry! No internet connection.", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
		//===================================================================================================================================
		//sending EmailAddress and Password to server
		//===================================================================================================================================
		private class MyAsyncTask extends AsyncTask<String, Integer, Double>{

			String responseBody = null;
			@Override
			protected Double doInBackground(String... params) {
				// TODO Auto-generated method stub
				postData(params[0],params[1]);
				return null;
			}

			protected void onPostExecute(Double result){
				pb.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
				
				if(responseBody!=null)
				{
					processResponce(responseBody);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Empty Responce.", Toast.LENGTH_LONG).show();
				}

			}
			protected void onProgressUpdate(Integer... progress){
				pb.setProgress(progress[0]);
			}

			public void postData(String emailId,String passwrd) {
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://203.153.37.13:89/AndroidService.asmx/ValidateLogin");

				try {
					// Data that I am sending
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("EmailId", emailId));
					nameValuePairs.add(new BasicNameValuePair("Password", passwrd));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					responseBody = EntityUtils.toString(response.getEntity());

					Log.d("result", responseBody);
				} 
				catch (Throwable t ) {
					//Toast.makeText( getApplicationContext(),""+t,Toast.LENGTH_LONG).show();
					Log.d("Error Time of Login",t+"");
				} 
			}
		}
		//===================================================================================================================================
		//END sending EmailAddress and Password to server 
		//===================================================================================================================================
		
		
		//===================================================================================================================================
		//processing the XML got from server
		//===================================================================================================================================
		private void processResponce(String responceFromServer) 
		{
			//Do what ever with your server response
		}
		//===================================================================================================================================
		//processing the XML got from server
		//===================================================================================================================================
		
		
		//===================================================================================================================================
		//check packet data and wifi
		//===================================================================================================================================
		private boolean haveNetworkConnection() 
		{
			boolean haveConnectedWifi = false;
			boolean haveConnectedMobile = false;

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (NetworkInfo ni : netInfo) 
			{
				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
					if (ni.isConnected())
						haveConnectedWifi = true;
				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
					if (ni.isConnected())
						haveConnectedMobile = true;
			}
			return haveConnectedWifi || haveConnectedMobile;
		}
		//====================================================================================================================================
		//checking packet data and wifi END
		//====================================================================================================================================
}
