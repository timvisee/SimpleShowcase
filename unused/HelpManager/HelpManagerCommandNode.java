package com.timvisee.SimpleShowcase.HelpManager;

import java.util.ArrayList;
import java.util.List;

public class HelpManagerCommandNode {
	
	private List<String> nodes = new ArrayList<String>();
	
	public HelpManagerCommandNode(List<String> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(String node) {
		nodes.add(node);
	}
	
	public String getNode(int i) {
		if(this.nodes.size() < i + 1)
			return "";
		
		return this.nodes.get(i);
	}
	
	public List<String> getNodes() {
		return this.nodes;
	}
	
	public boolean containsNode(String node) {
		for(String entry : this.nodes) {
			if(entry.equalsIgnoreCase(node))
				return true;
		}
		return false;
	}
	
	public void removeNode(String node) {
		for(String entry : this.nodes) {
			if(entry.equalsIgnoreCase(node)) {
				this.nodes.remove(entry);
				break;
			}
		}
	}
}
