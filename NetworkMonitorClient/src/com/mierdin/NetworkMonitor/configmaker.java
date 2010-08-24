package com.mierdin.NetworkMonitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.mierdin.NetworkMonitor.Classes.ConfigLoader;
import com.mierdin.NetworkMonitor.R;
import com.mierdin.NetworkMonitor.R.id;
import com.mierdin.NetworkMonitor.R.layout;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class configmaker extends Activity {
	
	public static String pathToConfigFile = Environment.getExternalStorageDirectory() + "/NetworkMonitor/config.txt";
	public String configDir = Environment.getExternalStorageDirectory() + "/NetworkMonitor";
    public File f = new File(pathToConfigFile);
    public File fDir = new File(configDir);
    
    public EditText txtWSLocation;
    public EditText txtUsername;
    public EditText txtPassword;
    //TODO: Might want to document this - don't set the values for these EditTexts here....you get that "cannot instantiate class"
    //error. YOu have to set it in onCreate. The problem is that eclipse won't tell you this is wrong.
    
    // TODO You will need to figure out how the first time configuration is done.
            //make sure it's cool on the first time
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configmaker);
        
        //txtWSLocation =(EditText)findViewById(R.id.txtCMWSLocation);
        txtUsername =(EditText)findViewById(R.id.txtCMUsername);
        txtPassword =(EditText)findViewById(R.id.txtCMPassword);
        Log.d("NetworkMonitor", "1");
        
        //TODO: Error here, figure it out
        if (ConfigLoader.isConfigured) {
            Log.d("NetworkMonitor", "3");
        	txtWSLocation.setText(ConfigLoader.pathToWS);
        	txtUsername.setText(ConfigLoader.username);
        	txtPassword.setText(ConfigLoader.password);
        } else {
            Log.d("NetworkMonitor", "2");
	        MsgBox("First Time Setup", "Please set up the configuration.");
        }
        Log.d("NetworkMonitor", "4");
        if(!fDir.isDirectory()) {
        	fDir.mkdir();
        }
        
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   if(saveConfig()) {
                   Intent i = new Intent(configmaker.this, main.class);
                   startActivity(i);
        	   }
           } 
        });
        Button btnAddSite = (Button) findViewById(R.id.btnAddSite);
        btnAddSite.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
            	   
            	   // START INPUTBOX
            	   final LinearLayout lL = new LinearLayout(configmaker.this);
            	   final EditText txtName = new EditText(configmaker.this);
            	   final EditText txtWS = new EditText(configmaker.this);
            	   txtName.setGravity(Gravity.CENTER);
            	   lL.addView(txtName, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            	   txtWS.setGravity(Gravity.CENTER);
            	   lL.addView(txtWS, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            	   txtName.setText("Connection Name");
            	   txtWS.setText("WebService Location");
            	   new AlertDialog.Builder(configmaker.this)
            	           .setView(lL)
            	           .setTitle("Add Server:")
            	           .setPositiveButton("OK", new DialogInterface.OnClickListener(){
            	                   @Override
            	                   public void onClick(DialogInterface d, int which) {
            	                           d.dismiss();
            	                           Toast.makeText(configmaker.this, "Added:: " + txtName.getText().toString(), Toast.LENGTH_LONG).show(); 
            	                   }        	    
            	           })
            	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            	                   @Override
            	                   public void onClick(DialogInterface d, int which) {
            	                           d.dismiss();
            	                   }
            	           }).create().show();
            	   // END INPUTBOX
        	   }
           
        });
    }
    
    private Boolean saveConfig()
    {
	      File file = new File(pathToConfigFile);
	      if(file.isFile()) { file.delete(); }
          FileWriter f; 
          try { 
              f = new FileWriter(pathToConfigFile); 
              String contents = "pathToWS=" + txtWSLocation.getText() + "\n" +
              					"username=" + txtUsername.getText() + "\n" +
              					"password=" + txtPassword.getText();
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
    
    
    void MsgBox(String title, String message) {
	    new AlertDialog.Builder(this)
	      .setTitle(title)
	      .setMessage(message)
	      .show();
	}    
}