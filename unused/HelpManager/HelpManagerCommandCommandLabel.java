package com.timvisee.SimpleShowcase.HelpManager;

import java.util.ArrayList;
import java.util.List;

public class HelpManagerCommandCommandLabel {
	
	private List<String> labels = new ArrayList<String>();
	
	public HelpManagerCommandCommandLabel(String label) {
		addLabel(label);
	}
	
	public HelpManagerCommandCommandLabel(List<String> labels) {
		addLabels(labels);
	}
	
	public void addLabel(String label) {
		this.labels.add(label);
	}
	
	public void addLabels(List<String> labels) {
		for(String entry : labels) {
			this.labels.add(entry);
		}
	}
	
	public List<String> getLabels() {
		return this.labels;
	}
	
	public boolean containsLabel(String label) {
		for(String entry : this.labels) {
			if(entry.equalsIgnoreCase(label))
				return true;
		}
		return false;
	}
	
	public void removeLabel(String label) {
		for(String entry : this.labels) {
			if(entry.equalsIgnoreCase(label)) {
				this.labels.remove(entry);
				break;
			}
		}
	}
}
