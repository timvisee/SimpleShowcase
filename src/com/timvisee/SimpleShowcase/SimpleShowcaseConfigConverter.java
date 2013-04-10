package com.timvisee.simpleshowcase;

import org.bukkit.configuration.Configuration;

public class SimpleShowcaseConfigConverter {
	
	SimpleShowcase plugin;
	
	/**
	 * Class constructor
	 */
	public SimpleShowcaseConfigConverter(SimpleShowcase instance) {
		plugin = instance;
	}
	
	public boolean isOldConfigFile(Configuration c) {
		if(doesConfigContainVersionInfo(c))
			return (getConfigVersion(c) < plugin.getVersionCode());
		return false;
	}
	
	/**
	 * Check if the config file contains it's version information
	 * @param c the config file
	 * @return true if the file contains version information
	 */
	public boolean doesConfigContainVersionInfo(Configuration c) {
		// The config file may not be null
		if(c == null)
			return false;
		
		if(c.isSet("configVersion") || c.isSet("version")) {
			if(c.isInt("configVersion") || c.isSet("version")) {
				return (c.getInt("configVersion", c.getInt("version", -1)) >= 0);
			}
		}
		return false;
	}
	
	/**
	 * Get the version code of a config file
	 * @param c the config file
	 * @return -1 when the config file doesn't have an configVersion node
	 */
	public int getConfigVersion(Configuration c) {
		// The config file may not be null
		if(doesConfigContainVersionInfo(c)) {
			return c.getInt("configVersion", c.getInt("version", -1));
		}
		return -1;
	}
}
