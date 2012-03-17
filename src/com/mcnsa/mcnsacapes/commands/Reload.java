package com.mcnsa.mcnsacapes.commands;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsacapes.MCNSACapes;
import com.mcnsa.mcnsacapes.util.ColourHandler;
import com.mcnsa.mcnsacapes.util.Command;
import com.mcnsa.mcnsacapes.util.CommandInfo;

@CommandInfo(alias = "reloadcapes", description = "reloads all capes")
public class Reload implements Command {
	private static MCNSACapes plugin = null;
	public Reload(MCNSACapes instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {	
		// update everyone's cape
		Player[] online = plugin.getServer().getOnlinePlayers();
		for(int i = 0; i < online.length; i++) {
			// see if they have perms for a custom cape
			if(plugin.hasPermission(online[i], "custom")) {
				plugin.playerManager.setPlayerCapeNoUpdate(online[i], online[i].getName().toLowerCase());
			}
			// see if they have perms for a mod cape
			else if(plugin.hasPermission(online[i], "mod")) {
				plugin.playerManager.setPlayerCapeNoUpdate(online[i], "mod");
			}
			// see if they have perms for a donor cape
			else if(plugin.hasPermission(online[i], "donor")) {
				plugin.playerManager.setPlayerCapeNoUpdate(online[i], "donor");
			}
			// they shouldn't have a cape?
			else {
				plugin.playerManager.removePlayerCapeNoUpdate(online[i]);
			}
		}
		
		// send them all the cape mappings
		plugin.playerManager.sendUpdatesToPlayersWithMod();
		
		// and notify them!
		ColourHandler.sendMessage(player, "&aCapes reloaded!");
		return true;
	}
}
