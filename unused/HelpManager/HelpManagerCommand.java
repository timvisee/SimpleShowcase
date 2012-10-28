package com.timvisee.SimpleShowcase.HelpManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class HelpManagerCommand {
	
	List<HelpManagerCommandNode> nodes = new ArrayList<HelpManagerCommandNode>(); // The command nodes
	List<HelpManagerCommandArgument> args = new ArrayList<HelpManagerCommandArgument>(); // The command arguments
	List<HelpManagerCommandFlag> flags = new ArrayList<HelpManagerCommandFlag>(); // The command flags
	
	String shortDesc = "";
	String desc = "";
	
	public HelpManagerCommand(String shortDesc, String desc) {
		this.shortDesc = shortDesc;
		this.desc = desc;
	}
	
	public HelpManagerCommand() {
		
	}
	
	public void addNode(HelpManagerCommandNode node) {
		this.nodes.add(node);
	}
	
	public void addNodes(List<HelpManagerCommandNode> nodes) {
		for(HelpManagerCommandNode entry : nodes) {
			this.nodes.add(entry);
		}
	}
	
	public List<HelpManagerCommandNode> getNodes() {
		return this.nodes;
	}
	
	public boolean containsAnyNode(List<String> nodes) {
		for(String n : nodes) {
			if(containsNode(n))
				return true;
		}
		return false;
	}
	
	public boolean containsNode(String node) {
		for(HelpManagerCommandNode entry : this.nodes) {
			for(String entryNode : entry.getNodes()) {
				if(entryNode.equalsIgnoreCase(node))
					return true;
			}
		}
		return false;
	}
	
	public boolean containsNode(HelpManagerCommandNode node) {
		return this.nodes.contains(node);
	}
	
	public void removeNode(HelpManagerCommandNode node) {
		if(this.nodes.contains(node))
			this.nodes.remove(node);
	}
	
	public boolean hasNodes() {
		return (this.nodes.size() == 0) ? false : true;
	}
	
	public int countNodes() {
		return this.nodes.size();
	}
	
	public void addArgument(HelpManagerCommandArgument arg) {
		this.args.add(arg);
	}
	
	public void addArguments(List<HelpManagerCommandArgument> args) {
		for(HelpManagerCommandArgument entry : args) {
			this.args.add(entry);
		}
	}
	
	public List<HelpManagerCommandArgument> getArguments() {
		return this.args;
	}
	
	public boolean containsArgument(String arg) {
		for(HelpManagerCommandArgument entry : this.args) {
			if(entry.getArgument().equalsIgnoreCase(arg))
				return true;
		}
		return false;
	}
	
	public boolean containsArgument(HelpManagerCommandArgument arg) {
		return this.args.contains(arg);
	}
	
	public void removeArgument(HelpManagerCommandArgument arg) {
		if(this.args.contains(arg))
			this.args.remove(arg);
	}
	
	public boolean hasArguments() {
		return (this.args.size() == 0) ? false : true;
	}
	
	public int countArguments() {
		return this.args.size();
	}
	
	public void addFlag(HelpManagerCommandFlag flag) {
		this.flags.add(flag);
	}
	
	public void addFlags(List<HelpManagerCommandFlag> flags) {
		for(HelpManagerCommandFlag entry : flags) {
			this.flags.add(entry);
		}
	}
	
	public List<HelpManagerCommandFlag> getFlags() {
		return this.flags;
	}
	
	public boolean containsFlag(String flag) {
		for(HelpManagerCommandFlag entry : this.flags) {
			if(entry.getFlag().equalsIgnoreCase(flag))
				return true;
		}
		return false;
	}
	
	public boolean containsFlag(HelpManagerCommandFlag flag) {
		return this.flags.contains(flag);
	}
	
	public void removeFlag(HelpManagerCommandFlag flag) {
		if(this.flags.contains(flag))
			this.flags.remove(flag);
	}
	
	public boolean hasFlags() {
		return (this.flags.size() == 0) ? false : true;
	}
	
	public int countFlags() {
		return this.flags.size();
	}
	
	public void setShortDescription(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	
	public String getShortDescription() {
		return this.shortDesc;
	}
	
	public void setDescription(String desc) {
		this.desc = desc;
	}
	
	public String getDescription() {
		return this.desc;
	}
	
	/**
	 * Get the command preview to put into the help screen
	 * @return the command preview
	 */
	public String getCommandPreview(String commandLabel) {
		// The command label
		String c = commandLabel;
		
		// The command nodes
		String cnew = "";
		for(HelpManagerCommandNode entry : this.nodes) {
			if(cnew == "")
				cnew = entry.getNode(0);
			else
				cnew = cnew + " " + entry.getNode(0);
		}
		if(cnew != "") {
			c = c + " " + cnew;
		}
		
		// The command arguments
		cnew = "";
		for(HelpManagerCommandArgument entry : this.args) {
			if(cnew == "") {
				if(entry.isOptional())
					cnew = "[" + entry.getArgument() + "]";
				else
					cnew = "<" + entry.getArgument() + ">";
			} else {
				if(entry.isOptional())
					cnew = cnew + " [" +  entry.getArgument() + "]";
				else
					cnew = cnew + " <" +  entry.getArgument() + ">";
			}
		}
		if(cnew != "") {
			c = c + " " + cnew;
		}
		
		if(hasFlags())
			c = c + " [flags]";
		
		return c;
	}
	
	/**
	 * Check if the command matches with this command
	 * @param c the command
	 * @return false if it doesn't match
	 */
	public boolean matchNodes(String c) {
		c = c.trim();
		
		List<String> args = Arrays.asList(c.split(" "));
		
		// If nothing, return true because it matches
		if(args.size() == 0)
			return true;
		
		for(int i = 0; i < this.nodes.size(); i++) {
			boolean match = false;
			for(String entry : this.nodes.get(i).getNodes()) {
				// If no command arguments are left, return if the previous matched
				if(i + 1 >= args.size())
					return match;
				
				if(entry.equalsIgnoreCase(args.get(i))) {
					match = true;
					break;
				}
			}
			
			if(!match)
				return false;
		}
		return true;
	}
}
