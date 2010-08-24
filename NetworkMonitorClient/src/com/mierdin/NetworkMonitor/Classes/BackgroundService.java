package com.mierdin.NetworkMonitor.Classes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParserException;

import com.mierdin.NetworkMonitor.main;
import com.mierdin.NetworkMonitor.WSOps.getDevicesClass;
import com.mierdin.NetworkMonitor.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BackgroundService extends Service {
	public ArrayList<Device> oldList = new ArrayList<Device>();
	
	private static final long INTERVAL = 30000;
	private static Timer timer = new Timer();
	public void onCreate() {
		super.onCreate();
		startservice();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        generateNotification("Network Monitor", "Service is running...", true);
	}

	private void startservice() {
		timer.scheduleAtFixedRate( new TimerTask() {
		public void run() {
		//Do whatever you want to do every “INTERVAL”
	         Log.d("NetworkMonitor", "REFRESHING DEVICES FROM SERVICE");
      	     main.deviceList.clear();
      	     ArrayList<String> devList = new ArrayList<String>();
      	     
			try {
				 devList = getDevicesClass.getMachines();
		     	 String[] lv_arr = new String[devList.size()];
		     	 int i = 0;
		     	 for (String item : devList)
		     	 {
		     		 item = item.replace("[", "");
		     		 item = item.replace("]", "");
			   
		     		 String[] props = item.split(",");
		     		 
		     		 Device thisDevice = new Device(props[0].toString().trim(), 
								        				 props[1].toString().trim(), 
								        				 props[2].toString().trim(), 
								        				 props[3].toString().trim());
		     		 
		     		 main.deviceList.add(thisDevice);
		     		 lv_arr[i] = props[0].toString() + " - " + props[3].toString();
		     		 i++;
		     	 }
		     	 ArrayList<Device> changedDevices = getChangedDevices(main.deviceList);
		     	 if (!changedDevices.isEmpty())
		     	 {
		     		 for (Device changedDevice : changedDevices)
		     		 {
		     			generateNotification("Device Status Change", changedDevice.getHostName() + " is now " + changedDevice.getStatus(), false);
		     		 }
		     	 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}, 0, INTERVAL);
		; }

	private void generateNotification(String title, String message, Boolean isOngoing)
	{
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.icon;
		CharSequence tickerText = title;
		long when = System.currentTimeMillis();
		int NOTIFICATION_ID = 1;
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		if(isOngoing)
		{
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			NOTIFICATION_ID = 2;
		}
		Context context = getApplicationContext();
		CharSequence contentTitle = title;
		CharSequence contentText = message;
		Intent notificationIntent = new Intent(this, main.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		mNotificationManager.notify(NOTIFICATION_ID, notification);
		
	}
	
    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(2);  
    }
	
	private ArrayList<Device> getChangedDevices(ArrayList<Device> newList)
	{
		ArrayList<Device> changedDevices = new ArrayList<Device>();
		if (!oldList.isEmpty())
		{
			for (Device thisOldDevice : oldList)
			{
				for (Device thisNewDevice : newList)
				{
					if (thisOldDevice.getHostName().compareToIgnoreCase(thisNewDevice.getHostName()) == 0)
					{
			     		if (thisOldDevice.getStatus().compareToIgnoreCase(thisNewDevice.getStatus()) != 0)
						{
							changedDevices.add(thisNewDevice);
						}
					}
				}
			}
		}
		oldList.clear();
		for (Device thisDev : newList)
		{
    		 oldList.add(thisDev);
		}
		return changedDevices;
	}

	// Some method required by the Service class I'm extending.
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 /* subclass for implementing AsyncTask for retrieving a list of network devices */
	  class getMachinesTask extends AsyncTask<URL, Integer, ArrayList<String>> {
	         protected ArrayList<String> doInBackground(URL... urls) {
	             try {
	    	        Log.d("NetworkMonitor", "REFRESHING DEVICES FROM SERVICE3");
	      			return getDevicesClass.getMachines();
	      		} catch (IOException e) {
	      			Log.e("NetworkMonitor","IO ERROR - " + e.toString());
	      			e.printStackTrace();
	      		} catch (XmlPullParserException e) {
	    			// Clean up this error handling
	      			Log.e("NetworkMonitor","XML PARSING ERROR - " + e.toString());
	      			e.printStackTrace();
	      		} catch (Exception e){
	      			Log.d("NetworkMonitor","ERROR - " + e.toString());
	      		}
	      		Log.d("NetworkMonitor","getMachinesTask(Async) completed.");
	      		return null;
	         }

	         protected void onProgressUpdate(Integer... progress) {
	             //setProgressPercent(progress[0]);
	         }

	         protected void onPostExecute(ArrayList<String> result) {
	        	  main.deviceList.clear();
	        	 String[] lv_arr = new String[result.size()];
	        	 int i = 0;
	        	 for (String item : result)
	        	 {
	        		 item = item.replace("[", "");
	        		 item = item.replace("]", "");
		   
	        		 String[] props = item.split(",");
	        		 
	        		 Device thisDevice = new Device(props[0].toString().trim(), 
							        				 props[1].toString().trim(), 
							        				 props[2].toString().trim(), 
							        				 props[3].toString().trim());
	        		 
	        		 main.deviceList.add(thisDevice);
	        		 lv_arr[i] = props[0].toString() + " - " + props[3].toString();
	        		 i++;
	        	 }
	         }
	  }
}
