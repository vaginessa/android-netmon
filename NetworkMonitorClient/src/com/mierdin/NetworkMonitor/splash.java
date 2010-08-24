package com.mierdin.NetworkMonitor;

import com.mierdin.NetworkMonitor.Classes.ConfigLoader;
import com.mierdin.NetworkMonitor.R;
import com.mierdin.NetworkMonitor.R.id;
import com.mierdin.NetworkMonitor.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class splash extends Activity {
	
    void MsgBox(final String title, String message) {
	    new AlertDialog.Builder(this)
	      .setTitle(title)
	      .setMessage(message)
	      .setPositiveButton("OK", new DialogInterface.OnClickListener(){
            	                   @Override
            	                   public void onClick(DialogInterface d, int which) {
            	                           d.dismiss(); 
            	                   }        	    
            	           })
	      .show();
	}    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Button b = (Button) findViewById(R.id.btnContinue);
        
        MsgBox("NOTICE!!!","This application is not yet designed to work correctly. \n\n It is on the Market to provide updates to the few who are using it for testing right now. \n\n There is a server component that is being developed in parallel that this application must have access to in order to run at all. \n\n Thank you for your understanding and patience while we build this app.");
        
        b.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   
               
               String loadConfigResult = ConfigLoader.loadConfig();
               
               if (loadConfigResult == "no config") {
                   Intent i = new Intent(splash.this, configmaker.class);
                   Log.d("NetworkMonitor", "========Not Configured, going to configmaker.");
                   startActivity(i);
               } else {
		           Intent i = new Intent(splash.this, main.class);
                   Log.d("NetworkMonitor", "========Configured, going to start.");
		           startActivity(i);
               }
           } 
        });
    }


}
