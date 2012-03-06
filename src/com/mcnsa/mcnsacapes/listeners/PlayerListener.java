package com.mcnsa.mcnsacapes.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mcnsa.mcnsacapes.MCNSACapes;

public class PlayerListener implements Listener {
	MCNSACapes plugin = null;
	public PlayerListener(MCNSACapes instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	// handle joins
	@EventHandler(priority = EventPriority.LOWEST)
	public void joinHandler(PlayerJoinEvent event) {
		// see if they have a default cape
		for(int i = 0; i < plugin.configManager.options.defaultCapeRanks.size(); i++) {
			// see if their rank matches this one
			if(plugin.permissions.getUser(event.getPlayer()).inGroup(plugin.configManager.options.defaultCapeRanks.get(i))) {
				// they have a default cape, set it!
				plugin.playerManager.setPlayerCape(event.getPlayer(), plugin.configManager.options.defaultCapeRanks.get(i).toLowerCase());
			}
		}
	}
	
	// handle quits
	@EventHandler(priority = EventPriority.LOWEST)
	public void quitHandler(PlayerQuitEvent event) {
		// see if they have a default cape
		plugin.playerManager.removePlayerCape(event.getPlayer());
		
		// also, remove them from the "have mod" list
		plugin.playerManager.setPlayerHasMod(event.getPlayer(), false);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocessHandler(PlayerCommandPreprocessEvent event) {
		// if the command is cancelled, back out
		if(event.isCancelled()) return;
		
		// intercept the command
		if(plugin.commandManager.handleCommand(event.getPlayer(), event.getMessage())) {
			// we handled it, cancel it
			event.setCancelled(true);
		}
	}
}
