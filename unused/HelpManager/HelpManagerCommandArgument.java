package com.timvisee.SimpleShowcase.HelpManager;

public class HelpManagerCommandArgument {
	
	private String arg = "";
	private boolean isOptional = false;
	private String shortDesc = "";
	private String desc = "";
	
	HelpManagerCommandArgument(String arg, boolean isOptional, String shortDesc, String desc) {
		this.arg = arg;
		this.isOptional = isOptional;
		this.shortDesc = shortDesc;
		this.desc = desc;
	}

	public String getArgument() {
		return arg;
	}

	public void setArgument(String arg) {
		this.arg = arg;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public String getShortDescription() {
		return shortDesc;
	}

	public void setShortDescription(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}
}
