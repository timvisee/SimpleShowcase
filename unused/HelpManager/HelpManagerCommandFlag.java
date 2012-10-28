package com.timvisee.SimpleShowcase.HelpManager;

public class HelpManagerCommandFlag {
	
	private String flag = "";
	private boolean hasArgument = true;
	private String flagArgName = "";
	private String shortDesc = "";
	private String desc = "";
	
	HelpManagerCommandFlag(String flag, boolean hasArgument, String flagArgName, String shortDesc, String desc) {
		this.flag = flag;
		this.hasArgument = hasArgument;
		this.flagArgName = flagArgName;
		this.shortDesc = shortDesc;
		this.desc = desc;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean isHasArgument() {
		return hasArgument;
	}

	public void setHasArgument(boolean hasArgument) {
		this.hasArgument = hasArgument;
	}

	public String getFlagArgumentName() {
		return flagArgName;
	}

	public void setFlagArgumentName(String flagArgName) {
		this.flagArgName = flagArgName;
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
