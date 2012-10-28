package com.timvisee.SimpleShowcase;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SimpleShowcaseShopItem {
	World world;
	Block shop;
	Location location;
	Item item;
	int itemTypeId;
	byte itemData;
	
	/**
	 * Create a item for a shop to put it onto the shop block
	 * @param world The world of the shop block
	 * @param shop The shop block
	 * @param itemId The type ID of the item
	 * @param itemData The data value of the item
	 */
	public SimpleShowcaseShopItem(World world, Block shop, int itemId, byte itemData) {
		Location location = shop.getLocation();
		
		setWorld(world);
		setBlock(shop);
		setTypeId(itemId);
		setData(itemData);
		
		setLocation(shop.getLocation());
		setItem(world.dropItem(location, new ItemStack(getTypeId(), 1, (short) 0, getData())));
		getItem().setVelocity(new Vector(0,0,0));
		
		removeDupedItems();
	}
	
	/*
	 * Getters and setters
	 */
	
	/**
	 * Get the block where the item floats on, this is the shop block 
	* @return The block of the shop where the item floats on
	*/
	public Block getBlock() {
		return this.shop;
	}
	
	/**
	 * Get the data value of the item 
	* @return The data value of the item
	*/
	public byte getData() {
		return itemData;
	}
	
	/**
	 * Get the item as an item variable 
	* @return The item variable
	*/
	public Item getItem() {
		return item;
	}
	
	/**
	 * Get the location of the item 
	* @return The location of the item
	*/
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Get the type ID of the item 
	* @return The type ID of the item
	*/
	public int getTypeId() {
		return itemTypeId;
	}
	
	/**
	 * Get the world of the item 
	* @return The world
	*/
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Set the shop block of the item 
	* @param shop The block of the shop of the item
	*/
	public void setBlock(Block shop) {
		this.shop = shop;
	}
	
	/**
	 * Set the data value of the item 
	* @param data The data value of the item
	*/
	public void setData(byte data) {
		this.itemData = data;
	}
	
	/**
	 * Set the item using an Item variable 
	* @param item An item variable
	*/
	public void setItem(Item item) {
		this.item = item;
		getItem().setVelocity(new Vector(0,0,0));
		setLocation(getBlock().getLocation());
	}
	
	/**
	 * Set the world of the item
	* @param world The world of the item
	*/
	private void setWorld(World world) {
		this.world = world;
	}
	
	/**
	 * Set the location of the item and put it on the right place on the block 
	* @param location The location of the item/shop block
	*/
	private void setLocation(Location location) {
		this.location = location.getBlock().getLocation(); //simply clear everything after the comma.
		Vector vec = this.location.toVector();
		vec.add(new Vector(0.5,0.6,0.5));
		this.location = vec.toLocation(this.location.getWorld());
		
		if(getItem() != null){
			getItem().teleport(this.location);
		}
	}
	
	/**
	 * Set the type ID of the item 
	* @param typeId The type ID of the item
	*/
	public void setTypeId(int typeId) {
		this.itemTypeId = typeId;
	}
	
	
	
	
	/**
	 * Remove all the old shop items who aren't deleted for some reason
	*/
	public void removeDupedItems(){
		Chunk c = getBlock().getChunk();
		for(Entity e:c.getEntities()){
			if(e.getLocation().getBlock().equals(getBlock())&&e instanceof Item && !e.equals(item)){
				e.remove();
			}
			if(e.getLocation().getBlock().equals(getWorld().getBlockAt(getBlock().getX(), getBlock().getY()+1, getBlock().getZ()))&&e instanceof Item && !e.equals(item)){
				e.remove();
			}
		}
	}
	
	/**
	 * Check if the chunk of the items is loaded 
	* @return If the chunk of the items is loaded
	*/
	public boolean isChunkLoaded() {
		return (getBlock()==null?false:getBlock().getWorld().isChunkLoaded(getBlock().getChunk()));
	}
	
	/**
	 * Remove the item, also remove the old shop items which arent deleted to be sure that everyting is removed 
	*/
	public void remove() {
		removeDupedItems();
		getItem().remove();
	}
	
	/**
	 * Respawn the item, put it again on the right place on the block, this is really usefull if any other plugin or thing removed the item, or if water or something replaced the item.
	 * You must also use this to do the changes which are made in the functions to set the type ID and the data value of the item.
	*/
	public void respawn() {
		if(isChunkLoaded()) {
			if(getItem() != null){
				remove();
			}
			
			ItemStack stck = new ItemStack(getTypeId(), 1, (short) 0, getData());
			setItem(getLocation().getWorld().dropItemNaturally(getBlock().getLocation(), stck));
			getItem().setVelocity(new Vector(0,0,0));
		}
	}
}
