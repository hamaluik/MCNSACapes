package com.mcnsa.mcnsacapes.managers;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsacapes.MCNSACapes;
import com.mcnsa.mcnsacapes.commands.*;
import com.mcnsa.mcnsacapes.util.ColourHandler;
import com.mcnsa.mcnsacapes.util.Command;
import com.mcnsa.mcnsacapes.util.CommandInfo;

public class CommandManager {
	private MCNSACapes plugin;

	// and the commands
	public HashMap<String, InternalCommand> commands = new HashMap<String, InternalCommand>();
	
	public CommandManager(MCNSACapes instance) {
		plugin = instance;

		// develop the list of all commands here!
		// TODO: dynamically load commands ALA CommandBook
		//plugin.debug("registering commands...");
		registerCommand(new Verify(plugin));
		registerCommand(new Cape(plugin));
		registerCommand(new Reload(plugin));
		//plugin.debug("commands all registered!");
	}

	// register new command
	public void registerCommand(Command command) {
		// get the class
		Class<? extends Command> cls = command.getClass();
		//plugin.debug("registering command: " + cls.getSimpleName());
		
		// get the class's annotations
		Annotation[] annotations = cls.getAnnotations();
		for(int i = 0; i < annotations.length; i++) {
			if(annotations[i] instanceof CommandInfo) {
				// we found our annotation!
				CommandInfo ci = (CommandInfo)annotations[i];
				
				// create the internal command
				InternalCommand ic = new InternalCommand(ci.alias(), ci.permission(), ci.usage(), ci.description(), ci.visible(), command);
				commands.put(ci.alias(), ic);
				
				// we're done
				return;
			}
		}
	}
	
	// handle commands
	public Boolean handleCommand(Player player, String command) {
		// get the actual command
		//plugin.debug(player.getName() + " sent command: " + command);
		
		// strip off the proceeding "/"
		command = command.substring(1);
		
		// tokenize it
		String[] tokens = command.split("\\s");
		
		// get the command
		if(tokens.length < 1) {
			// we're not handling it
			return false;
		}
		tokens[0] = tokens[0].toLowerCase();
		
		// find the command
		if(!commands.containsKey(tokens[0])) {
			// we're not handling it
			//plugin.debug("not handling command: " + tokens[0]);
			return false;
		}
		
		// make sure they have permission first
		if(!commands.get(tokens[0]).permissions.equals("") && !plugin.hasPermission(player, commands.get(tokens[0]).permissions)) {
			// return a message if they don't have permission
			plugin.log(player.getName() + " attempted to use command: " + tokens[0] + " without permission!");
			ColourHandler.sendMessage(player, "&cYou don't have permission to do that!");
			// we handled it, but they don't have perms
			return true;
		}
		
		// we have the command, send it in!
		String sArgs = new String("");
		//plugin.debug("handling command: " + tokens[0]);
		// make sure we have args
		if(command.length() > (1 + tokens[0].length())) {
			// substring out the args
			sArgs = command.substring(1 + tokens[0].length());
			//plugin.debug("with arguments: " + sArgs);
		}
		
		// and handle the command!
		if(commands.get(tokens[0]).command.handle(player, sArgs)) {
			// we handled it!
			//plugin.debug("command " + tokens[0] + " handled successfully!");
			return true;
		}
		
		// they didn't use it properly! let them know!
		//plugin.debug("command " + tokens[0] + " NOT handled successfully");
		ColourHandler.sendMessage(player, "&cInvalid usage! &aCorrect usage: &3/" + commands.get(tokens[0]).alias + " &b" + commands.get(tokens[0]).usage + " &7(" + commands.get(tokens[0]).description + ")");
		return true;
	}
	
	// return a sorted list of commands
	public InternalCommand[] listCommands() {
		// count the number of invisible commands
		//plugin.debug("counting number of invisible commands");
		int numInvisible = 0;
		for(String cmd: commands.keySet()) {
			if(!commands.get(cmd).visible) {
				numInvisible++;
			}
		}
		
		// create the list
		//plugin.debug("creating command list array");
		InternalCommand[] cList = new InternalCommand[commands.size() - numInvisible];
		
		// get them all!
		int i = 0;
		//plugin.debug("getting all visible commands");
		for(String cmd: commands.keySet()) {
			// add only the visible ones!
			if(commands.get(cmd).visible) {
				cList[i] = commands.get(cmd);
				i += 1;
			}
		}
		
		// sort the array
		//plugin.debug("sorting command list");
		Arrays.sort(cList, new CommandComp());
		
		// and return!
		return cList;
	}

	public class InternalCommand {
		public String alias = new String("");
		public String permissions = new String("");
		public String usage = new String("");
		public String description = new String("");
		public Boolean visible = new Boolean(true);
		public Command command = null;
	
		public InternalCommand(String _alias, String _perms, String _usage, String _desc, boolean _visible, Command _command) {
			alias = _alias;
			permissions = _perms;
			usage = _usage;
			description = _desc;
			visible = _visible;
			command = _command;
		}
	}
	
	class CommandComp implements Comparator<InternalCommand> {
		public int compare(InternalCommand a, InternalCommand b) {
			return a.alias.compareTo(b.alias);
		}
	}
}
