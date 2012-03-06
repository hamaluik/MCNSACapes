package com.mcnsa.mcnsacapes.commands;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsacapes.MCNSACapes;
import com.mcnsa.mcnsacapes.util.ColourHandler;
import com.mcnsa.mcnsacapes.util.Command;
import com.mcnsa.mcnsacapes.util.CommandInfo;

@CommandInfo(alias = "!enable_mcnsacapes", visible = false)
public class Verify implements Command {
	private static MCNSACapes plugin = null;
	public Verify(MCNSACapes instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// enable them in the player manager
		plugin.playerManager.setPlayerHasMod(player, true);
		
		// now send them all the cape mappings
		plugin.playerManager.sendUpdatesToPlayerWithMod(player);
		
		// and notify them!
		ColourHandler.sendMessage(player, "&aMCNSA capes enabled!");
		return true;
	}
}