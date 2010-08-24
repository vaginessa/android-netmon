package com.mierdin.NetworkMonitor.Classes;

public class ConfigItem {
    private String configName;
    private String configValue;
    private Boolean showCheckbox;
    private Boolean showSpinner;

    public ConfigItem(String configName, String configValue, Boolean showCheckbox, Boolean showSpinner)
    {
    	this.configName = configName;
    	this.configValue = configValue;
    	this.showCheckbox = showCheckbox;
    	this.showSpinner = showSpinner;
    }

    public String getConfigName(){
        return this.configName;
	}
    public String getConfigValue(){
        return this.configValue;
	}
    public Boolean getShowCheckbox(){
        return this.showCheckbox;
	}
    public Boolean getShowSpinner(){
        return this.showSpinner;
	}
}
