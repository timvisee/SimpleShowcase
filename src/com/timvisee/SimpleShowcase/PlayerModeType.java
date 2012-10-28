package com.timvisee.SimpleShowcase;

public enum PlayerModeType {
	UNKNOWN("Unknown"),
	NONE("None"),
	SHOP_BUY_TRANSACTION("Shop buy transaction"),
	SHOP_SELL_TRANSACTION("Shop sell transaction"),
	SHOP_SELECTION("Shop selection"),
	SHOP_LOCATION_SELECTION("Shop location selection"),
	SHOP_PRODUCT_SELECTION("Shop product selection"),
	SHOP_SHOW_ITEM_SELECTION("Shop show item selection"),
	SHOP_PLAYEROWNED_CREATION_WIZZARD("Shop creation wizzard"),
	SHOP_PLAYEROWNED_SELECTION("Shop selection"),
	SHOP_PLAYEROWNED_LOCATION_SELECTION("Shop location selection"),
	SHOP_PLAYEROWNED_LOCATION_SELECTION_BOOTH_CONFIRMATION("Shop location selection booth confirmation"),
	BOOTH_SELECTION("Booth selection"),
	BOOTH_LOCATION_SELECTION("Booth location selection");
	
	public String name;
	
	PlayerModeType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
