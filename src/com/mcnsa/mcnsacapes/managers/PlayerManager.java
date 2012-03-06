package com.mcnsa.mcnsacapes.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsacapes.MCNSACapes;
import com.mcnsa.mcnsacapes.util.ColourHandler;

public class PlayerManager {
	private MCNSACapes plugin;
	private HashMap<Player, String> capeMappings = new HashMap<Player, String>();
	private ArrayList<Player> playersWithMod = new ArrayList<Player>();
	
	public PlayerManager(MCNSACapes instance) {
		plugin = instance;
	}
	
	public void setPlayerCape(Player player, String cape) {
		capeMappings.put(player, cape);
		sendUpdatesToPlayersWithMod();
	}
	
	public void removePlayerCape(Player player) {
		if(capeMappings.containsKey(player)) {
			capeMappings.remove(player);
			sendUpdatesToPlayersWithMod();
		}
	}
	
	public void setPlayerHasMod(Player player, boolean has) {
		if(has && !playersWithMod.contains(player)) {
			playersWithMod.add(player);
		}
		else if(!has && playersWithMod.contains(player)) {
			playersWithMod.remove(player);
		}
	}
	
	public void sendUpdatesToPlayerWithMod(Player player) {
		ColourHandler.sendMessage(player, "&4 &c &2 &a capeserver:" + plugin.configManager.options.capeServer);
		for(Player capeWearer: capeMappings.keySet()) {
			ColourHandler.sendMessage(player, "&4 &c &2 &a setcape:" + capeWearer.getName() + ":" + capeMappings.get(capeWearer));
		}
	}
	
	public void sendUpdatesToPlayersWithMod() {
		for(int i = 0; i < playersWithMod.size(); i++) {
			sendUpdatesToPlayerWithMod(playersWithMod.get(i));
		}
	}
}
