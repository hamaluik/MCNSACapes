package com.mcnsa.mcnsacapes;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcnsa.mcnsacapes.listeners.PlayerListener;
import com.mcnsa.mcnsacapes.managers.CommandManager;
import com.mcnsa.mcnsacapes.managers.ConfigManager;
import com.mcnsa.mcnsacapes.managers.PlayerManager;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MCNSACapes extends JavaPlugin {
	// load the minecraft logger
	Logger log = Logger.getLogger("Minecraft");

	// APIs
	public PermissionManager permissions = null;
	
	// configuration
	public ConfigManager configManager = null;

	// and commands
	public CommandManager commandManager = null;

	// keep track of listeners
	public PlayerListener playerListener = null;
	
	// and finally, the player manager
	public PlayerManager playerManager = null;

	public void onEnable() {
		// set up APIs
		this.setupPermissions();
		
		// load the config
		configManager = new ConfigManager(this);
		// load configuration
		// and save it again (for defaults)
		this.getConfig().options().copyDefaults(true);
		if(!configManager.load(getConfig())) {
			// shit
			// BAIL
			error("configuration failed - bailing");
			getServer().getPluginManager().disablePlugin(this);
		}
		this.saveConfig();
		configManager.load(getConfig());
		
		// setup things
		commandManager = new CommandManager(this);
		
		// set up listeners
		playerListener = new PlayerListener(this);
		
		// and the manager!
		playerManager = new PlayerManager(this);
		
		log("plugin enabled!");
	}
	
	public void onDisable() {
		log("plugin disabled!");
	}

	// for simpler logging
	public void log(String info) {
		log.info("[MCNSACapes] " + info);
	}

	// for error reporting
	public void error(String info) {
		log.info("[MCNSACapes] <ERROR> " + info);
	}

	// for debugging
	// (disable for final release)
	public void debug(String info) {
		log.info("[MCNSACapes] <DEBUG> " + info);
	}

	// load the permissions plugin
	public void setupPermissions() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
			this.permissions = PermissionsEx.getPermissionManager();
			log("permissions successfully loaded!");
		}
		else {
			error("PermissionsEx not found!");
		}
	}

	// just an interface function for checking permissions
	// if permissions are down, default to OP status.
	public boolean hasPermission(Player player, String permission) {
		if(permissions != null) {
			return permissions.has(player, "mcnsacapes." + permission);
		}
		else {
			return player.isOp();
		}
	}
}