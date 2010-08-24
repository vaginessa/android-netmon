package com.mierdin.NetworkMonitor;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import com.mierdin.NetworkMonitor.R;
import com.mierdin.NetworkMonitor.R.id;
import com.mierdin.NetworkMonitor.R.layout;
import com.mierdin.NetworkMonitor.Classes.Device;
import com.mierdin.NetworkMonitor.WSOps.telnetClass;
import com.mierdin.NetworkMonitor.WSOps.wakeUpClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class actDeviceOptions extends Activity {
	public static ProgressDialog myProgressDialog = null;
	TextView lblDevice;
	TextView lblIpAddr;
	TextView lblMacAddr;
	TextView lblStatus;
	
	Button btnShutdown;
	Button btnWOL;
	Button btnReboot;
	EditText txtUsername;
	EditText txtPassword;
	String ipAddr;
	String macAddr;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deviceoptions);
        lblDevice = (TextView)findViewById(R.id.lblDevice);
        lblIpAddr = (TextView)findViewById(R.id.lblIpAddr);
        lblMacAddr = (TextView)findViewById(R.id.lblMacAddr);
        lblStatus = (TextView)findViewById(R.id.lblStatus);
        btnShutdown = (Button)findViewById(R.id.btnShutdown);
        btnWOL = (Button)findViewById(R.id.btnWOL);
        btnReboot = (Button)findViewById(R.id.btnReboot);
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        Bundle extras = getIntent().getExtras(); 
        if(extras !=null)
        {
        	lblDevice.setText(extras.getString("hostName"));
        }
        
        for (Device thisDevice : main.deviceList)
        {
    		if (thisDevice.getHostName().compareTo(extras.getString("hostName")) == 0)
        	{
    			ipAddr = thisDevice.getIpAddr();
    			macAddr = thisDevice.getMacAddr();
        		Log.d("NetworkMonitor", "!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        		Log.d("NetworkMonitor", ipAddr);
        		Log.d("NetworkMonitor", thisDevice.getMacAddr());
        		Log.d("NetworkMonitor", thisDevice.getStatus());
        		lblIpAddr.setText(ipAddr);
        		lblMacAddr.setText(thisDevice.getMacAddr());
        		lblStatus.setText(thisDevice.getStatus());
        		if (lblStatus.getText().toString().compareTo("offline") == 0)
        		{
        			lblDevice.setTextColor(0xFFFF0000);
        			lblMacAddr.setTextColor(0xFFFF0000);
        			lblIpAddr.setTextColor(0xFFFF0000);
        			lblStatus.setTextColor(0xFFFF0000);
        		}
        	}
        }
        
        btnShutdown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	if (txtUsername.getText().toString().compareTo("") != 0 && txtPassword.getText().toString().compareTo("") != 0)
            	{
			        myProgressDialog = ProgressDialog.show(actDeviceOptions.this, "Please wait...", "Sending shutdown command...", true);
			        new sendTelnetTask(ipAddr, txtUsername.getText().toString(), txtPassword.getText().toString(), "shutdown -s -t 00").execute();
		        } else {
		        	Toast.makeText(actDeviceOptions.this, "Please enter credentials.", 4000).show();
		        }
            } 
         });
        
        btnReboot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	if (txtUsername.getText().toString().compareTo("") != 0 && txtPassword.getText().toString().compareTo("") != 0)
            	{
	            	myProgressDialog = ProgressDialog.show(actDeviceOptions.this, "Please wait...", "Sending reboot command...", true);
	                new sendTelnetTask(ipAddr, txtUsername.getText().toString(), txtPassword.getText().toString(), "shutdown -r -t 00").execute();
		        } else {
		        	Toast.makeText(actDeviceOptions.this, "Please enter credentials.", 4000).show();
		        }
            } 
         });
        
        btnWOL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	myProgressDialog = ProgressDialog.show(actDeviceOptions.this, "Please wait...", "Sending WOL command...", true);
                new sendWOLTask(ipAddr, macAddr).execute();
            } 
         });
        
    }
}

class sendTelnetTask extends AsyncTask<URL, Integer, Boolean> {
	public String hostName;
	public String username;
	public String password;
	public String command;
	
	public sendTelnetTask(String hostName, String username, String password, String command) {
        //Constructor for passing in hostName
		this.hostName = hostName;
		this.username = username;
		this.password = password;
		this.command = command;
    }

    protected Boolean doInBackground(URL... urls) {
        try {
        	Log.d("NetworkMonitor", "HOSTNAME FOR REBOOT-" + hostName);
 			return telnetClass.sendTelnet(hostName, username, password, command);
 		} catch (IOException e) {
 			Log.d("NetworkMonitor","IO error!!!!!!!!!");
 			e.printStackTrace();
 		} catch (XmlPullParserException e) {
 			Log.d("NetworkMonitor","XmlParser error!!!!!!!!!");
 			e.printStackTrace();
 		} catch (Exception e) {
			// Clean up this error handling
 			Log.d("NetworkMonitor","THERE WAS AN error!!!!!!!!!");
			e.printStackTrace();
		} 
 		return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Boolean result) {
   	
   	 
        actDeviceOptions.myProgressDialog.dismiss();
    }

}
     
class sendWOLTask extends AsyncTask<URL, Integer, Boolean> {
	public String ipAddr;
	public String macAddr;
	
	public sendWOLTask(String ipAddr, String macAddr) {
        //Constructor for passing in hostName
		this.ipAddr = ipAddr;
		this.macAddr = macAddr;
    }

    protected Boolean doInBackground(URL... urls) {
        try {
        	Log.d("NetworkMonitor", "Sending WOL to -" + macAddr);
 			return wakeUpClass.sendWOL(ipAddr, macAddr);
 		} catch (IOException e) {
 			Log.d("NetworkMonitor","IO error!!!!!!!!!");
 			e.printStackTrace();
 		} catch (XmlPullParserException e) {
 			Log.d("NetworkMonitor","XmlParser error!!!!!!!!!");
 			e.printStackTrace();
 		} catch (Exception e) {
			// Clean up this error handling
 			Log.d("NetworkMonitor","THERE WAS AN error!!!!!!!!!");
			e.printStackTrace();
		} 
 		return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Boolean result) {
   	
   	 
        actDeviceOptions.myProgressDialog.dismiss();
    }

}
