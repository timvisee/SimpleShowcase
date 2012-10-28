package com.timvisee.SimpleShowcase;

public enum SSShopProductType {
	UNKNOWN("UNKNOWN"),
	ITEM("ITEM"),
	API("API"),
	MAGIC_SPELLS("MAGIC_SPELLS");
	
	private String name;
	
	SSShopProductType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
