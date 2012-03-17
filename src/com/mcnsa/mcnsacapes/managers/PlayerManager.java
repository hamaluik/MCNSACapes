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
		
		// now handle reloads
		Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
		for(int i = 0; i < onlinePlayers.length; i++) {
			// send them the super-secret reload command
			ColourHandler.sendMessage(onlinePlayers[i], "&4 &c &2 &a &0 ");
		}
	}
	
	public void setPlayerCape(Player player, String cape) {
		capeMappings.put(player, cape);
		sendSingleUpdateToPlayersWithMod(player);
		//sendUpdatesToPlayersWithMod();
	}
	
	public void removePlayerCape(Player player) {
		if(capeMappings.containsKey(player)) {
			capeMappings.remove(player);
			sendSingleUpdateToPlayersWithMod(player);
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
	
	public void sendSingleUpdateToPlayerWithMod(Player player, Player capeWearer) {
		if(capeMappings.containsKey(capeWearer)) {
			ColourHandler.sendMessage(player, "&4 &c &2 &a setcape:" + capeWearer.getName() + ":" + capeMappings.get(capeWearer));
		}
	}
	
	public void sendSingleUpdateToPlayersWithMod(Player capeWearer) {
		for(int i = 0; i < playersWithMod.size(); i++) {
			sendSingleUpdateToPlayerWithMod(playersWithMod.get(i), capeWearer);
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
