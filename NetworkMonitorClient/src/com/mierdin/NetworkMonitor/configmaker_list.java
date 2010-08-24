package com.mierdin.NetworkMonitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.mierdin.NetworkMonitor.Classes.ConfigItem;
import com.mierdin.NetworkMonitor.Classes.ConfigLoader;
import com.mierdin.NetworkMonitor.Classes.Device;
import com.mierdin.NetworkMonitor.main.DeviceAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class configmaker_list extends ListActivity { 
	
	public static String pathToConfigFile = Environment.getExternalStorageDirectory() + "/NetworkMonitor/config.txt";
	public String configDir = Environment.getExternalStorageDirectory() + "/NetworkMonitor";
    public File f = new File(pathToConfigFile);
    public File fDir = new File(configDir);
    public ArrayList<ConfigItem> confItemList = new ArrayList<ConfigItem>();
    public ArrayAdapter<?> timeAdapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configmaker_list);

        timeAdapter = ArrayAdapter.createFromResource(this, R.array.timeValues , android.R.layout.simple_spinner_item); 
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        if (ConfigLoader.isConfigured) {
	        confItemList.add(new ConfigItem("Webservice Location", ConfigLoader.pathToWS , false, false));
	        confItemList.add(new ConfigItem("Username", ConfigLoader.username , false, false));
	        confItemList.add(new ConfigItem("Password", ConfigLoader.password , false, false));
	        confItemList.add(new ConfigItem("Auto-start Service", "", true, false));
	        confItemList.add(new ConfigItem("Refresh Interval", "", false, true));
        } else {
            Toast.makeText(this, "Please configure the connection", Toast.LENGTH_LONG).show();
	        confItemList.add(new ConfigItem("Webservice Location", "" , false, false));
	        confItemList.add(new ConfigItem("Username", "" , false, false));
	        confItemList.add(new ConfigItem("Password", "" , false, false));
	        confItemList.add(new ConfigItem("Auto-start Service", "", true, false));
	        confItemList.add(new ConfigItem("Refresh Interval", "", true, false));
        }
        
        if(!fDir.isDirectory()) {
        	fDir.mkdir();
        }
        
        ConfigItemAdapter confAdap = new ConfigItemAdapter(this, R.layout.row, confItemList);    	     	 
        this.setListAdapter(confAdap);
    }
    
    /* Click handler for the listview */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ConfigItem confItem = (ConfigItem) l.getItemAtPosition(position);
        if (confItem.getConfigName().equalsIgnoreCase("Webservice Location")) {
        	
        }

        saveConfig();
    }
    
    private Boolean saveConfig()
    {
    	String pathToWS = "";
    	String username = "";
    	String password = "";
    	
    	for(ConfigItem confItem : confItemList){ 
    		if (confItem.getConfigName().equalsIgnoreCase("Webservice Location")) { pathToWS = confItem.getConfigValue(); }
    		if (confItem.getConfigName().equalsIgnoreCase("Username")) { username = confItem.getConfigValue(); }
    		if (confItem.getConfigName().equalsIgnoreCase("Password")) { password = confItem.getConfigValue(); }
    	}
	      File file = new File(pathToConfigFile);
	      if(file.isFile()) { file.delete(); }
          FileWriter f; 
          try { 
              f = new FileWriter(pathToConfigFile); 
              String contents = "pathToWS=" + pathToWS + "\n" +
              					"username=" + username + "\n" +
              					"password=" + password;
              f.write(contents); 
              f.flush(); 
              f.close(); 
              ConfigLoader.loadConfig();
    	      return true;
	      } catch (IOException e1) { 
	               //TODO Auto-generated catch block 
	              e1.printStackTrace(); 
	              return false;
	      } 
    }
    
	 public class ConfigItemAdapter extends ArrayAdapter<ConfigItem> {

	        private ArrayList<ConfigItem> items;

	        public ConfigItemAdapter(Context context, int textViewResourceId, ArrayList<ConfigItem> items) {
	                super(context, textViewResourceId, items);
	                this.items = items;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                if (v == null) {
	                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = vi.inflate(R.layout.row_configure, null);
	                }
	                
	                //TODO: Possibly look into why items is sometimes empty at this point. This occurence, however, is rare.
	                if (items.size() != 0) {
		                ConfigItem o = items.get(position);
		                
		                TextView cn = (TextView) v.findViewById(R.id.lblConfigName);
	                    TextView cv = (TextView) v.findViewById(R.id.lblConfigValue);
	                    CheckBox cb = (CheckBox) v.findViewById(R.id.cb1);
                        Spinner sp = (Spinner)findViewById(R.id.spnr1);
	                    
	                        if (o.getShowCheckbox())  {
	                        	cb.setVisibility(View.VISIBLE);
	                        } else {
	                        	cb.setVisibility(View.GONE);
	                        }
	                        
	                        if (o.getShowSpinner()) {
	                        	//sp.setVisibility(View.VISIBLE);
	                        if (timeAdapter.isEmpty()){	
	                        			Log.d("NetworkMonitor", "IT IS EMPTY!!!");
	                        }
	                        	
		                        sp.setAdapter(timeAdapter);
		                        
		                        
		                        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
		                            @Override
		                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		                                // your code here
		                            }

		                            @Override
		                            public void onNothingSelected(AdapterView<?> parentView) {
		                                // your code here
		                            }

		                        });
		                        
	                        } else {
	                        	//sp.setVisibility(View.GONE);
	                        }
	                        
	                        if (cn != null) {
	                        		cn.setText(o.getConfigName());                            }
	                        if(cv != null){
	                        	if (o.getConfigName().equalsIgnoreCase("Password")){
	                        		cv.setText("********");
	                        	} else {
	                        		cv.setText(o.getConfigValue());
	                        	}
	                        }
	                }
	                return v;
	        }
	}
}
