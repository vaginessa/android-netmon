package com.mierdin.NetworkMonitor.Classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Environment;
import android.util.Log;

public class ConfigLoader {

	public static Boolean isConfigured;
	public static String pathToWS;
	public static String username;
	public static String password;
	
	public static String loadConfig() {
		 String readString = new String();
		 try{
			 String pathToConfigFile = Environment.getExternalStorageDirectory().toString() + "/NetworkMonitor/config.txt";
			
			 Log.d("NetworkMonitor", "Attempting to read config file at: " + pathToConfigFile);
			   File f = new File(pathToConfigFile);
			   FileInputStream fileIS = new FileInputStream(f);
			   BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
			   //just reading each line and pass it on the debugger
			   while((readString = buf.readLine())!= null){
			      if (readString != null)
			      {
			    	  if (readString.substring(0,8).compareToIgnoreCase("pathToWS") == 0) { 
			    		  pathToWS = readString.substring(9,readString.length()); 
			    	     Log.d("NetworkMonitor", "pathToWS" + readString.substring(9,readString.length()));
			    	  }
			    	  if (readString.substring(0,8).compareToIgnoreCase("username") == 0) { 
			    		  username = readString.substring(9,readString.length()); 
			    	     Log.d("NetworkMonitor", "pathToWS" + readString.substring(9,readString.length()));
			    	  }
			    	  if (readString.substring(0,8).compareToIgnoreCase("password") == 0) { 
			    		  password = readString.substring(9,readString.length()); 
			    	     Log.d("NetworkMonitor", "pathToWS" + readString.substring(9,readString.length()));
			    	  }
			      }
			   }
			   if (pathToWS != "" && username != "" && password != "") { 
				   isConfigured = true; 
				   fileIS.close();
				   buf.close();
				   return "configured";
			   }
			   fileIS.close();
			   buf.close();
			   return "configured";
			} catch (FileNotFoundException e) {
			   Log.d("NetworkMonitor", "CONFIG FILE NOT FOUND!");
			   isConfigured = false;
				return "no config";
			} catch (IOException e1){
			   e1.printStackTrace();
			   Log.d("NetworkMonitor", "CONFIG FILE READ ERROR!");
			   Log.d("NetworkMonitor", e1.toString());
			   isConfigured = false;
				return "no config";
			}
	}
}
