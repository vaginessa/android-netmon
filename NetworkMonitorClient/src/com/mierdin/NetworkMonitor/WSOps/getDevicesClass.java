package com.mierdin.NetworkMonitor.WSOps;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

import com.mierdin.NetworkMonitor.Classes.ConfigLoader;

import android.util.Log;

public class getDevicesClass {

	 
     @SuppressWarnings("unchecked")
	public static ArrayList<String> getMachines() throws IOException, XmlPullParserException, Exception 
     {	 

    	 	String METHOD_NAME = "getDevices";   
    	    String NAMESPACE = "urn:getDevices";    
    	    String URL = ConfigLoader.pathToWS;
    	 	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	        request.newInstance ();
         
            Log.d("NetworkMonitor", "Created KSOAP object");     
            Log.d("NetworkMonitor", "Set properties ");
            SoapSerializationEnvelope envelope = 
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;
            envelope.encodingStyle = "utf-8";
            envelope.enc = SoapSerializationEnvelope.ENC2001;
            envelope.xsd = SoapEnvelope.XSD;
            envelope.xsi = SoapEnvelope.XSI;
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request);
            Log.d("NetworkMonitor", "Sending WS request to " + URL);
            AndroidHttpTransport transport = new AndroidHttpTransport(URL);
            transport.call(URL, envelope);
            Log.d("NetworkMonitor", "Waiting for response");
            java.util.Vector<Object> result = (java.util.Vector<Object>)envelope.getResponse();
            //SoapObject result = (SoapObject )envelope.getResponse();

            ArrayList<String> ret = new ArrayList<String>();
            if (result != null)
            {
            	Log.d("NetworkMonitor", "Recieved response from server");
                for (Object cs : result)
                {
                	try {
                	Log.d("NetworkMonitor","Adding:" + cs.toString());
                	 ret.add(cs.toString());
                	} catch (java.lang.NullPointerException ex) {}
                }
            } else {
                Log.e("NetworkMonitor", "RESPONSE WAS EMPTY");
                throw new Exception();
            }
           
            return ret;
     }  
}

