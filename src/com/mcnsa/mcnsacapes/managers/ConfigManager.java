package com.mcnsa.mcnsacapes.managers;

//import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

import com.mcnsa.mcnsacapes.MCNSACapes;

public class ConfigManager {
	// store the main plugin for later access
	static MCNSACapes plugin = null;
	public ConfigOptions options = new ConfigOptions();
	public ConfigManager(MCNSACapes instance) {
		plugin = instance;
	}

	// load the configuration
	public Boolean load(FileConfiguration config) {
		// load the chat radius
		//plugin.debug("loading options...");
		
		options.capeServer = config.getString("cape-server", "http://capes.mcnsa.com");
		//options.defaultCapeRanks = (ArrayList<String>)config.getStringList("default-cape-ranks");
		
		// successful
		return true;
	}

	// create a "class" in here to store config options!
	public class ConfigOptions {
		public String capeServer = new String("http://capes.mcnsa.com");
		//public ArrayList<String> defaultCapeRanks = new ArrayList<String>();
	}
}
