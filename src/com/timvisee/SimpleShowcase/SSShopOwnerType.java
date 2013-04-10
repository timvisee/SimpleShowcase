package com.timvisee.simpleshowcase;

public enum SSShopOwnerType {
	UNKNOWN("UNKNOWN"),
	SERVER("SERVER"),
	PLAYER("PLAYER"),
	OTHER("OTHER");
	
	private String name;
	
	SSShopOwnerType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
