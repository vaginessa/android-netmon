package com.mierdin.NetworkMonitor.Classes;

    public class Device
    {
        private String hostName;
        private String ipAddr;
        private String macAddr;
        private String status;

        public Device(String hostName, String ipAddr, String macAddr, String status)
        {
        	this.hostName = hostName;
        	this.ipAddr = ipAddr;
        	this.macAddr = macAddr;
        	this.status = status;
        }

        public String getHostName(){
            return this.hostName;
		}
        public String getIpAddr(){
            return this.ipAddr;
		}
        public String getMacAddr(){
            return this.macAddr;
		}
        public String getStatus(){
            return this.status;
		}
        public void setHostName(String status){
        	this.status = status;
		}
    }

