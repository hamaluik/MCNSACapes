package com.mcnsa.mcnsacapes.commands;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsacapes.MCNSACapes;
import com.mcnsa.mcnsacapes.util.ColourHandler;
import com.mcnsa.mcnsacapes.util.Command;
import com.mcnsa.mcnsacapes.util.CommandInfo;

@CommandInfo(alias = "reloadcapes", description = "reloads your capes")
public class Reload implements Command {
	private static MCNSACapes plugin = null;
	public Reload(MCNSACapes instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {		
		// send them all the cape mappings
		plugin.playerManager.sendUpdatesToPlayerWithMod(player);
		
		// and notify them!
		ColourHandler.sendMessage(player, "&aCapes reloaded!");
		return true;
	}
}
