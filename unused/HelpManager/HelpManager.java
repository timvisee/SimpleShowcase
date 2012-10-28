package com.timvisee.SimpleShowcase.HelpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class HelpManager {
	
	// <command 
	private List<HelpManagerCommand> commands = new ArrayList<HelpManagerCommand>();
	
	public void addCommand(HelpManagerCommand c) {
		commands.add(c);
	}
	
	public List<HelpManagerCommand> getCommands() {
		return this.commands;
	}
	
	public boolean matchAnyCommand(String command) {
		for(HelpManagerCommand c : this.commands) {
			if(c.matchNodes(command))
				return true;
		}
		return false;
	}
	
	public HelpManagerCommand getMatchingCommand(String command) {
		for(HelpManagerCommand c : this.commands) {
			if(c.matchNodes(command))
				return c;
		}
		return null;
	}
	
	public int countMatchingCommands(String command) {
		return getMatchingCommands(command).size();
	}
	
	public List<HelpManagerCommand> getMatchingCommands(String nodes) {
		List<HelpManagerCommand> l = new ArrayList<HelpManagerCommand>();
		
		for(HelpManagerCommand c : this.commands) {
			if(c.matchNodes(nodes))
				l.add(c);
		}
		return l;
	}
	
	public void printHelp(String commandLabel, String nodes, String helpName, CommandSender s) {
		List<HelpManagerCommand> c = getMatchingCommands(nodes);
		
		if(c.size() == 0) {
			// No matching command available
			if(this.commands.size() == 0) {
				s.sendMessage(ChatColor.GREEN + "==========[ " + helpName.toUpperCase() + " HELP ]==========");
				s.sendMessage(ChatColor.DARK_RED + "No help available yet!");
				return;
			} else {
				s.sendMessage(ChatColor.GREEN + "==========[ " + helpName.toUpperCase() + " HELP ]==========");
				s.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				return;
			}
			
		} else if(c.size() == 1) {
			// One matching command available, show help
			HelpManagerCommand i = c.get(0);
			s.sendMessage(ChatColor.GREEN + "==========[ " + helpName.toUpperCase() + " HELP ]==========");
			s.sendMessage(ChatColor.GOLD + "/" + i.getCommandPreview(commandLabel) + ChatColor.WHITE + " : " + i.getShortDescription());
			
			if(i.countArguments() > 0) {
				s.sendMessage("");
				s.sendMessage(ChatColor.GRAY + "Arguments:");
				for(HelpManagerCommandArgument arg : i.getArguments()) {
					s.sendMessage(ChatColor.GOLD + arg.getArgument() + ChatColor.WHITE + " : " + arg.getShortDescription());
				}
			}
			
			if(i.countFlags() > 0) {
				s.sendMessage("");
				s.sendMessage(ChatColor.GRAY + "Flags:");
				for(HelpManagerCommandFlag flag : i.getFlags()) {
					if(flag.isHasArgument())
						s.sendMessage(ChatColor.GOLD + "-" + flag.getFlag() + " <" + flag.getFlagArgumentName() + ">" + ChatColor.WHITE + " : " + flag.getShortDescription());
					else
						s.sendMessage(ChatColor.GOLD + "-" + flag.getFlag() + ChatColor.WHITE + " : " + flag.getShortDescription());
				}
			}
		}
	}
}
