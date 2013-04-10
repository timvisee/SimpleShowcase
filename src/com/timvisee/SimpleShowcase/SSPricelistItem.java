package com.timvisee.simpleshowcase;

public class SSPricelistItem {
	
	private String pricelistName = "";
	private String itemName = "";
	
	private String productType = "";
	private String productAPIName = "";
	private int productId = 0;
	private String productName = "";
	private int productTypeId = 0;
	private byte productDataValue = 0;
	
	private double buyPrice = -1;
	private double sellPrice = -1;

	SSPricelistItem(String pricelistName, String itemName,
			String productType, String productAPIName, int productId, String productName, int productTypeId, byte productDataValue,
			double buyPrice, double sellPrice) {
		this.pricelistName = pricelistName;
		this.itemName = itemName;
		this.productType = productType;
		this.productAPIName = productAPIName;
		this.productId = productId;
		this.productName = productName;
		this.productTypeId = productTypeId;
		this.productDataValue = productDataValue;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
	}

	public String getPricelistName() {
		return pricelistName;
	}

	public void setPricelistName(String priceListName) {
		this.pricelistName = priceListName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductAPIName() {
		return productAPIName;
	}

	public void setProductAPIName(String productAPIName) {
		this.productAPIName = productAPIName;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public byte getProductDataValue() {
		return productDataValue;
	}

	public void setProductDataValue(byte productDataValue) {
		this.productDataValue = productDataValue;
	}
	
	public boolean isBuyPriceSet() {
		return (buyPrice >= 0);
	}
	
	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		if(buyPrice >= 0)
			this.buyPrice = buyPrice;
	}

	public boolean isSellPriceSet() {
		return (sellPrice >= 0);
	}
	
	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		if(sellPrice >= 0)
			this.sellPrice = sellPrice;
	}
}
