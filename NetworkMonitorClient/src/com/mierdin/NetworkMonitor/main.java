package com.mierdin.NetworkMonitor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParserException;

import com.mierdin.NetworkMonitor.R;
import com.mierdin.NetworkMonitor.R.drawable;
import com.mierdin.NetworkMonitor.R.id;
import com.mierdin.NetworkMonitor.R.layout;
import com.mierdin.NetworkMonitor.R.menu;
import com.mierdin.NetworkMonitor.Classes.BackgroundService;
import com.mierdin.NetworkMonitor.Classes.Device;
import com.mierdin.NetworkMonitor.WSOps.getDevicesClass;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class main extends ListActivity {
	static ListView lv1;
	public static ProgressDialog myProgressDialog = null;
	public String[] listViewData = null;
	public static TextView tv1;
    public static ArrayList<Device> deviceList = new ArrayList<Device>();
    private Handler mHandler = new Handler();
    public Intent service;
    Timer timer = new Timer();
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        service = new Intent(this, BackgroundService.class);
        tv1 = (TextView)findViewById(R.id.TextView01);
        
        //TODO: This is a reference to the old code, rename "UpdateTimeTask"
        timer.schedule(new UpdateTimeTask(), 1000, 2000);
    }

    /* Click handler for the listview */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        
		   Handler mHandler = new Handler();
           mHandler.removeCallbacks(mUpdateTimeTask);
           mHandler.postDelayed(mUpdateTimeTask, 100);
           
    	Intent i = new Intent(main.this, actDeviceOptions.class);
        Device selectedDevice = (Device) l.getItemAtPosition(position);
        
        //TODO: This passes a string param to the next activity, make it a Device object
        i.putExtra("hostName", selectedDevice.getHostName());
        
        Log.d("NetworkMonitor", "Viewing options for device.");
        startActivity(i);
    }
         /* Displays a simple message box to the user */
	      void MsgBox(String title, String message) {
	    	    new AlertDialog.Builder(this)
	    	      .setTitle(title)
	    	      .setMessage(message)
	    	      .show();
	    	}    
	     
	     @Override
	     public boolean onCreateOptionsMenu(Menu menu) {
	         MenuInflater inflater = getMenuInflater();
	         inflater.inflate(R.menu.main_menu, menu);
	         return true;
	     }
	  
	     
	     /* Handles menu item selections */
	     public boolean onOptionsItemSelected(MenuItem item) {
             Log.d("NetworkMonitor", item.getTitle().toString());
             if (item.getTitle().toString().compareToIgnoreCase("Refresh") == 0){
	        	 myProgressDialog = ProgressDialog.show(this, "Please wait...", "Polling devices...", true);
	        	 new getMachinesTask().execute();
	         }
	         if (item.getTitle().toString().compareToIgnoreCase("Configure") == 0){
                 Intent i = new Intent(main.this, configmaker_list.class);
                 Log.d("NetworkMonitor", "Manually going to configmaker_list");
                 startActivity(i);
	         }
	         if (item.getTitle().toString().compareToIgnoreCase("Start Service") == 0){
	             timer.schedule(new UpdateTimeTask(), 1000, 2000);
	             startService(new Intent(this, BackgroundService.class));
	         }
	         if (item.getTitle().toString().compareToIgnoreCase("Stop Service") == 0){
	        	 stopService(service);
	        	 timer.cancel();
	         }
	         return false;
	     }
	     
         private Runnable mUpdateTimeTask = new Runnable() {
      	   public void run() {
		       	 DeviceAdapter devAdap = new DeviceAdapter(main.this, R.layout.row, deviceList);    	     	 
		         main.this.setListAdapter(devAdap);
		         devAdap.notifyDataSetChanged();
      	   }
      	};

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
	        	 
	        	 deviceList.clear();
	        	 
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
	        		 
	        		 /* DEFINITIONS
	        		  * props[0] = HOSTNAME
	        		  * props[1] = IP ADDRESS
	        		  * props[2] = MAC ADDRESS
	        		  * props[3] = STATUS
	        		  */
	        		 
	        		 deviceList.add(thisDevice);
	        		 lv_arr[i] = props[0].toString() + " - " + props[3].toString();
	        		 i++;
	        		 
	        	 }

	        	 if (deviceList.size() != 0)
	        	 {
		        	 DeviceAdapter devAdap = new DeviceAdapter(main.this, R.layout.row, deviceList);    	     	 
		             //arAdap = new ArrayAdapter<String>(main.this, android.R.layout.simple_list_item_1, lv_arr);
		             main.this.setListAdapter(devAdap);
		             main.myProgressDialog.dismiss();
	        	 } else {
	        		 		Log.d("NetworkMonitor", "deviceList was EMPTY!!!!!!!!!!");
	        	 }
	         }
	     }
	  
	  class UpdateTimeTask extends TimerTask {
		   public void run() {
	            mHandler.removeCallbacks(mUpdateTimeTask);
	            mHandler.postDelayed(mUpdateTimeTask, 100);
		   }
		}
	  
	  public class DeviceAdapter extends ArrayAdapter<Device> {

	        private ArrayList<Device> items;

	        public DeviceAdapter(Context context, int textViewResourceId, ArrayList<Device> items) {
	                super(context, textViewResourceId, items);
	                this.items = items;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                if (v == null) {
	                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = vi.inflate(R.layout.row, null);
	                }
	                
	                //TODO: Possibly look into why items is sometimes empty at this point. This occurence, however, is rare.
	                if (items.size() != 0) {
		                Device o = items.get(position);
	                		ImageView iv = (ImageView) v.findViewById(R.id.icon);
	                        TextView tt = (TextView) v.findViewById(R.id.toptext);
	                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
	                        
	                        //Set orb color
	                        if (o.getStatus().compareToIgnoreCase("online") == 0)  {
	                        	iv.setImageResource(R.drawable.greensmall);
	                        } else {
	                        	iv.setImageResource(R.drawable.redsmall);
	                        }
	                        
	                        if (tt != null) {
	                              tt.setText("Name: "+o.getHostName());                            }
	                        if(bt != null){
	                              bt.setText("Status: "+ o.getStatus());
	                        }
	                }
	                return v;
	        }
	}
}

