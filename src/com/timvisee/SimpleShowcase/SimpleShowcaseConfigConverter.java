package com.timvisee.SimpleShowcase;

import org.bukkit.configuration.Configuration;

public class SimpleShowcaseConfigConverter {
	
	SimpleShowcase plugin;
	
	/**
	 * Class constructor
	 */
	public SimpleShowcaseConfigConverter(SimpleShowcase instance) {
		plugin = instance;
	}
	
	/**
	 * Check if the config file contains it's version information
	 * @param c the config file
	 * @return true if the file contains version information
	 */
	public boolean doesConfigContainsVersionInfo(Configuration c) {
		if(c.isSet("configVersion")) {
			if(c.isInt("configVersion")) {
				if(c.getInt("configVersion", -1) >= 0) {
					return true;
				}
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
		if(doesConfigContainsVersionInfo(c)) {
			return c.getInt("configVersion", -1);
		}
		return -1;
	}
}
