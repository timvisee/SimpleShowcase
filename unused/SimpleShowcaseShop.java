package com.timvisee.SimpleShowcase;

import org.bukkit.World;
import org.bukkit.block.Block;

public class SimpleShowcaseShop {	
	World world;
	Block block;
	int itemTypeId;
	byte itemData;
	double buyPrice;
	double sellPrice;
	
	/**
	 * Create a simple showcase shop
	 * @param world The world of the shop block
	 * @param shop The shop block
	 * @param itemTypeId The type ID of the item
	 * @param itemData The data value of the item
	 * @param buyPrice The buy price of the item
	 * @param sellPrice The sell price of the item
	 */
	public SimpleShowcaseShop(World world, Block shop, int itemTypeId, byte itemData, double buyPrice, double sellPrice) {
		setWorld(world);
		setBlock(shop);
		setItemTypeId(itemTypeId);
		setItemData(itemData);
		setBuyPrice(buyPrice);
		setSellPrice(sellPrice);
		
		SimpleShowcase.addShopItem(getWorld(), getBlock(), getItemTypeId(), getItemData());
	}
	
	/**
	 * Set the world of the shop
	 * @param world The world of the shop
	 */
	public void setWorld(World world) {
		this.world = world;
	}
	
	/**
	 * Get the world of the shop
	 * @return The world
	 */
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Set the shop block
	 * @param block The block of the shop
	 */
	public void setBlock(Block block) {
		this.block = block;
	}
	
	/**
	 * Get the block of the shop
	 * @return The block of the shop
	 */
	public Block getBlock() {
		return this.block;
	}
	
	/**
	 * Set the type ID of the item of the shop
	 * @param itemTypeId The type ID of the item
	 */
	public void setItemTypeId(int itemTypeId) {
		this.itemTypeId = itemTypeId;
	}
	
	/**
	 * Get the type ID of the shop item
	 * @return The type ID of the shop item
	 */
	public int getItemTypeId() {
		return this.itemTypeId;
	}
	
	/**
	 * Set the data value of the shop item
	 * @param itemData The data value
	 */
	public void setItemData(byte itemData) {
		this.itemData = itemData;
	}
	
	/**
	 * Get the data value of the shop item
	 * @return The data value
	 */
	public byte getItemData() {
		return this.itemData;
	}
	
	/**
	 * Set the buy price of the shop item
	 * @param buyPrice The buy price
	 */
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	/**
	 * Get the buy price of the shop item
	 * @return The buy price
	 */
	public double getBuyPrice() {
		return this.buyPrice;
	}
	
	/**
	 * Set the sell price of the shop item
	 * @param sellPrice The sell price
	 */
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	/**
	 * Get the sell price of the shop item
	 * @return The sell price
	 */
	public double getSellPrice() {
		return this.sellPrice;
	}
	
	/**
	 * Spawn the item of the shop onto the shop block
	 */
	public void spawnItem() {
		respawnItem();
	}
	
	/**
	 * Use this function before you want to remove the shop
	 */
	public void remove() {
		SimpleShowcase.removeShopItem(getWorld(), getBlock());
	}
	
	/**
	 * Respawn the item of the shop onto the block, this is usefull when the item is replaced, removed or picked up for some reason.
	 */
	public void respawnItem() {
		SimpleShowcase.removeShopItem(getWorld(), getBlock());
		SimpleShowcase.simpleShowcaseShopItems.add(new SimpleShowcaseShopItem(getWorld(), getBlock(), getItemTypeId(), getItemData()));
	}
}
