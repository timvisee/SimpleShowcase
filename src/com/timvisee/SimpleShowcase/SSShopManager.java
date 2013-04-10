package com.timvisee.simpleshowcase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;

public class SSShopManager {

	public static SimpleShowcase plugin;

	SSShopInventoryManager invManager;
	
	private List<SSShop> shops = new ArrayList<SSShop>();

	// Selected shops of players. <PlayerName, PortalId>
	private final HashMap<String, Integer> selectedShops = new HashMap<String, Integer>();

	// Get the last used shop
	private static HashMap<String, Integer> lastUsedShop = new HashMap<String, Integer>();

	/**
	 * Constructor
	 * @param instance
	 */
	public SSShopManager(SimpleShowcase instance) {
		plugin = instance;
		
		// Setup the inventory manager
		this.invManager = new SSShopInventoryManager(plugin);
	}
	
	public SSShopInventoryManager getShopInventoryManager() {
		return this.invManager;
	}
	
	/**
	 * Create a new shop
	 * @return the new created shop
	 */
	public SSShop createShop() {
		SSShop s = new SSShop(getUniqueId(), getUniqueServerOwnedName());
		addShop(s, true);
		
		// Return the added shop
		return this.shops.get(this.shops.size() - 1);
	}
	
	/**
	 * Add a new shop
	 * @param s new shop
	 * @param overwrite true to overwrite the other shop if there already was a shop on this location
	 */
	public void addShop(SSShop s, boolean overwrite) {
		// The ID has to be unique, or else problems will be caused. If the ID isn't unique another unique ID will be used for the new shop
		if(!isUniqueId(s.getId())) {
			System.out.println("[SimpleShowcase] Force changed shop ID, the ID wasn't unique!");
			s.setId(getUniqueId());
		}
		
		// If there already is a shop on this location, does it needs to be overwritten?
		if(isShopAt(s.getWorld(), s.getX(), s.getY(), s.getZ())) {
			if(overwrite) {
				SSShop stemp = getShopAt(s.getWorld(), s.getX(), s.getY(), s.getZ());
				
				// The variable may not be null
				if(stemp != null)
					removeShop(stemp);
				
				// The older shop has probably been removed, add the shop to the list
				this.shops.add(s);
			}
		} else {
			// There isn't a shop already on this location add the shop
			this.shops.add(s);
		}
	}
	
	/**
	 * Remove a shop
	 * @param b the location
	 */
	public void removeShop(Block b) {
		this.removeShop(b.getLocation());
	}
	
	/**
	 * Remove a shop
	 * @param l the location
	 */
	public void removeShop(Location l) {
		for(SSShop s : this.shops) {
			if(s.getX() == l.getBlockX() &&
					s.getY() == l.getBlockY() &&
					s.getZ() == l.getBlockZ()) {
				this.shops.remove(s);
				return;
			}
		}
	}
	
	/**
	 * Remove a shop
	 * @param s the shop
	 */
	public void removeShop(SSShop s) {
		// Remove the shop show item first
		if(s.isShowItemSpawned())
			s.removeShowItem();
		
		// Finally remove the shop itself
		shops.remove(s);
	}
	
	/**
	 * Get a list of all the shops
	 * @return all the shops
	 */
	public List<SSShop> getShops() {
		return this.shops;
	}
	
	public List<SSShop> getServerOwnedShops() {
		List<SSShop> l = new ArrayList<SSShop>();
		for(SSShop s : this.shops) {
			if(s.getOwnerType().equals(SSShopOwnerType.SERVER))
				l.add(s);
		}
		return l;
	}
	
	public List<SSShop> getPlayerOwnedShops() {
		List<SSShop> l = new ArrayList<SSShop>();
		for(SSShop s : this.shops) {
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				l.add(s);
		}
		return l;
	}

	
	/**
	 * Get a list of all the player owned shops
	 * @param player the player
	 * @return the player owned shops of a player
	 */
	public List<SSShop> getPlayerOwnedShops(String player) {
		List<SSShop> l = new ArrayList<SSShop>();
		for(SSShop s : this.shops) {
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				if(s.getOwnerName().equals(player))
					l.add(s);
		}
		return l;
	}
	
	/**
	 * Get a shop at the location of a block<br>
	 * If no shop was found null will be returned
	 * @param b the block
	 * @return the shop, or null when no shop was found on this location
	 */
	public SSShop getShopAt(Block b) {
		for(SSShop s : this.shops) {
			if(s.isShopAt(b)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Get a shop at any location<br>
	 * If no shop was found null will be returned
	 * @param l the location
	 * @return the shop, or null when no shop was found on this location
	 */
	public SSShop getShopAt(Location l) {
		for(SSShop s : this.shops) {
			if(s.isShopAt(l)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Get a shop at any location<br>
	 * If no shop was found null will be returned
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return the shop, or null when no shop was found on this location
	 */
	public SSShop getShopAt(String w, int x, int y, int z) {
		// Return null if no world was passed
		if(w == "")
			return null;
		
		for(SSShop s : this.shops) {
			if(s.isShopAt(w, x, y ,z)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Get a shop by it's show item<br>
	 * If no shop was found null will be returned
	 * @param showItem the shops show item
	 * @return the shop, or null when no shop was found with this show item
	 */
	public SSShop getShopByShowItem(Item showItem) {
		if(showItem == null)
			return null;
		
		for(SSShop s : this.shops) {
			if(s.isShowItem(showItem))
				return s;
		}
		return null;
	}
	
	/**
	 * Is there any shop at any location of a block
	 * @param b the block
	 * @return true when there's a shop at the location
	 */
	public boolean isShop(Block b) {
		if(this.getShopAt(b) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Is there any shop at any location of a block
	 * @param b the block
	 * @return true when there's a shop at the location
	 */
	public boolean isShopAt(Block b) {
		if(this.getShopAt(b) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Is there any shop at any location
	 * @param l the location
	 * @return true when there's a shop at any location
	 */
	public boolean isShopAt(Location l) {
		if(this.getShopAt(l) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Is there any shop at any location
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true when there's a shop at any location
	 */
	public boolean isShopAt(String w, int x, int y, int z) {
		// Return false if no world was passed
		if(w == "")
			return false;
		
		if(this.getShopAt(w, x, y, z) == null) {
			return false;
		}
		return true;
	}
	
	public boolean isShopBaseAt(Block b) {
		for(SSShop entry : this.shops) {
			if(entry.isShopBaseAt(b))
				return true;
		}
		return false;
	}
	
	/**
	 * Count the amount of shops created
	 * @return amount of shops
	 */
	public int countShops() {
		return this.shops.size();
	}

	
	/**
	 * Count the amount of player owned shops created
	 * @param player the player
	 * @return amount of shops
	 */
	public int countPlayerOwnedShops(String player) {
		int i = 0;
		for(SSShop s : this.shops) {
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				if(s.getOwnerName().equals(player))
					i++;
		}
		return i;
	}
	
	/**
	 * Count the amount of shops created and set
	 * @return amount of set shops
	 */
	public int countSetShops() {
		int i = 0;
		for(SSShop s : this.shops) {
			if(s.isSet())
				i++;
		}
		return i;
	}
	
	/**
	 * Get the shop by id
	 * @param id shop id
	 * @return shop
	 */
	public SSShop get(int id) {
		for(SSShop s : this.shops) {
			if(s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Get the shop by name
	 * @param name shop name
	 * @return the shop, null if no shop was found
	 */
	public SSShop get(String name) {
		for(SSShop s : this.shops) {
			if(s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Get the shop by name
	 * @param name shop name
	 * @return the shop, null if no shop was found
	 */
	public SSShop getServerOwned(String name) {
		for(SSShop s : this.shops) {
			if(s.getOwnerType().equals(SSShopOwnerType.SERVER))
				if(s.getName().equals(name))
					return s;
		}
		return null;
	}
	
	/**
	 * Get a playerowned shop by name
	 * @param player the player
	 * @param name the name
	 * @return the shop, null if no shop was found
	 */
	public SSShop getPlayerOwned(String player, String name) {
		for(SSShop s : this.shops) {
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				if(s.getOwnerName().equals(player))
					if(s.getName().equals(name))
						return s;
		}
		return null;
	}
	
	/**
	 * Get a playerowned shop by id
	 * @param player the player
	 * @param id the id
	 * @return the shop, null if no shop was found
	 */
	public SSShop getPlayerOwned(String player, int id) {
		for(SSShop s : this.shops) {
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				if(s.getOwnerName().equals(player))
					if(s.getId() == id)
						return s;
		}
		return null;
	}
	
	/**
	 * Get an unique id for a new portal
	 * @return an unique ID
	 */
	public int getUniqueId() {
		int i = 0;
		while(true) {
			if(isUniqueId(i)) {
				return i;
			}
			i++;
		}
	}
	
	/**
	 * Check if this ID is already used by another shop
	 * @param id id
	 * @return false if unique
	 */
	public boolean isUniqueId(int id) {
		for(SSShop s : this.shops) {
			if(s.getId() == id)
				return false;
		}
		return true;
	}
	
	/**
	 * Get an unique name for a new shop
	 * @return an unique name
	 */
	public String getUniqueServerOwnedName() {
		int i = 1;
		while(true) {
			String name = "shop" + String.valueOf(i);
			if(isUniqueServerOwnedName(name, false))
				return name;
			i++;
		}
	}
	
	/**
	 * Get an unique name for a new player owned shop
	 * @return an unique name
	 */
	public String getUniquePlayerOwnedName(String player) {
		int i = 1;
		while(true) {
			String name = "shop" + String.valueOf(i);
			if(isUniquePlayerOwnedName(player, name))
				return name;
			i++;
		}
	}
	
	/**
	 * Check if this name is already used by another shop
	 * @param id id
	 * @return false if unique
	 */
	public boolean isUniqueServerOwnedName(String name, boolean checkPlayerOwnedShops) {
		for(SSShop s : this.shops) {
			if(!checkPlayerOwnedShops || s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				if(s.getName().equals(name))
					return false;
		}
		return true;
	}
	
	/**
	 * Check if this name is already used by another player owned shop of a user
	 * @param id id
	 * @return false if unique
	 */
	public boolean isUniquePlayerOwnedName(String player, String name) {
		for(SSShop s : this.shops) {
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				if(s.getOwnerName().equals(player))
					if(s.getName().equals(name))
						return false;
		}
		return true;
	}
	
	public void selectShop(String player, SSShop s) {
		deselectShop(player);
		
		selectedShops.put(player, s.getId());
	}
	
	public SSShop getSelectedShop(String player) {
		if(!hasShopSelected(player)) {
			return null;
		}
		
		int id = selectedShops.get(player);
		for(int i = 0; i < this.shops.size(); i++) {
			if(this.shops.get(i).getId() == id) {
				return this.shops.get(i);
			}
		}
		
		return null;
	}
	
	public boolean hasShopSelected(String player) {
		if(!this.selectedShops.containsKey(player))
			return false;
		else {
			// Make sure the selected shop does still exists
			return !this.isUniqueId(selectedShops.get(player));
		}
	}
	
	public void deselectShop(String player) {
		if(hasShopSelected(player)) {
			selectedShops.remove(player);
		}
	}
	
	public void setLastUsedShop(String player, SSShop s) {
		forgetLastUsedShop(player);
		
		lastUsedShop.put(player, s.getId());
	}
	
	public SSShop getLastUsedShop(String player) {
		if(!hasLastUsedShop(player)) {
			return null;
		}
		
		int id = lastUsedShop.get(player);
		for(int i = 0; i < this.shops.size(); i++) {
			if(this.shops.get(i).getId() == id) {
				return this.shops.get(i);
			}
		}
		
		return null;
	}
	
	public boolean hasLastUsedShop(String player) {
		if(!lastUsedShop.containsKey(player))
			return false;
		else {
			// Make sure the selected shop does still exists
			return !this.isUniqueId(lastUsedShop.get(player));
		}
	}
	
	public void forgetLastUsedShop(String player) {
		if(hasLastUsedShop(player)) {
			lastUsedShop.remove(player);
		}
	}
	
	/**
	 * Count all spawned shop show items
	 * @return amount of show items spawned
	 */
	public int countSpawnedShopShowItems() {
		int i = 0;
		// For each shop
		for(SSShop s : this.shops) {
			// If the shop show item has been spawned, count it
			if(s.isShowItemSpawned())
				i++;
		}
		return i;
	}
	
	/**
	 * Check if an item is an show item of any shop
	 * @param i
	 * @return
	 */
	public boolean isShopShowItem(Item i) {
		for(SSShop s : this.shops) {
			// Check if the item is an shop show item
			if(s.isShowItem(i))
				return true;
		}
		return false;
	}
	
	/**
	 * Remove all duped shop show items
	 */
	public void removeAllDupedShopShowItems() {
		// For each shop
		for(SSShop s : this.shops) {
			// Remove the duped the show items
			s.removeDupedShowItems(plugin.getServer());
		}
	}
	
	/**
	 * Remove all duped shop show items in a world
	 * @param w the world name
	 */
	public void removeAllDupedShopShowItemsInWorld(String w) {
		// For each shop
		for(SSShop s : this.shops) {
			// Is the current shop in this world
			if(s.getWorld().equals(w)) {
				// Remove the duped the show items
				s.removeDupedShowItems(plugin.getServer());
			}
		}
	}
	
	/**
	 * Remove all duped shop show items in a chunk
	 * @param c the chunk
	 */
	public void removeAllDupedShopShowItemsInChunk(Chunk c) {
		// For each shop
		for(SSShop s : this.shops) {
			// Is the current shop in this chunk
			Block sb = s.getBaseBlock(plugin.getServer());
			if(sb == null)
				continue;
			
			if(sb.getChunk().equals(c)) {
				// Remove the duped the show items
				s.removeDupedShowItems(plugin.getServer());
			}
		}
	}
	
	/**
	 * Respawn all shop show items
	 */
	public void respawnAllShopShowItems() {
		// For each shop
		for(SSShop s : this.shops) {
			// Respawn the show item
			if(s.shouldShowItemBeSpawned(plugin.getServer()))
				s.respawnShowItem(plugin.getServer());
		}
	}
	
	/**
	 * Respawn all shop show items
	 */
	public void respawnAllIncorrectShopShowItems() {
		// For each shop
		for(SSShop s : this.shops) {
			// Respawn the show item if it's in the wrong location
			if(s.shouldShowItemBeSpawned(plugin.getServer()))
				if(!s.isShowItemInValidLocation(plugin.getServer()))
					s.respawnShowItem(plugin.getServer());
		}
	}
	
	/**
	 * Respawn all shop show items in a world
	 * @param w the world name
	 */
	public void respawnAllShopShowItemsInWorld(String w) {
		// For each shop
		for(SSShop s : this.shops) {
			// Is the current shop in this world
			if(s.getWorld().equals(w)) {
				// Respawn the shop show item
				s.respawnShowItem(plugin.getServer());
			}
		}
	}
	
	/**
	 * Respawn all shop show items in a chunk
	 * @param c the chunk
	 */
	public void respawnAllShopShowItemsInChunk(Chunk c) {
		// For each shop
		for(SSShop s : this.shops) {
			// Is the current shop in this chunk
			Block sb = s.getBaseBlock(plugin.getServer());
			if(sb == null)
				continue;
			
			if(sb.getChunk().equals(s)) {
				// Respawn the shop show item
				s.respawnShowItem(plugin.getServer());
			}
		}
	}
	
	/**
	 * Remove all shop show items
	 */
	public void removeAllShopShowItems() {
		// For each shop
		for(SSShop s : this.shops) {
			// Remove the show item
			s.removeShowItem();
		}
	}
	
	/**
	 * Remove all shop show items in a world
	 * @param w the world name
	 */
	public void removeAllShopShowItemsInWorld(String w) {
		// For each shop
		for(SSShop s : this.shops) {
			// Is the current shop in this world
			if(s.getWorld().equals(w)) {
				// Remove the shop show item
				s.removeShowItem();
			}
		}
	}
	
	/**
	 * Remove all shop show items in a chunk
	 * @param c the chunk
	 */
	public void removeAllShopShowItemsInChunk(Chunk c) {
		// For each shop
		for(SSShop s : this.shops) {
			// Is the current shop in this chunk
			if(s.getBaseBlock(plugin.getServer()).getChunk().equals(s)) {
				// Remove the shop show item
				s.removeShowItem();
			}
		}
	}
	
	/**
	 * This will check if there's any object in the shop list which could cause problems
	 * @return true if valid
	 */
	public boolean isListValid() {
		for(Object s : this.shops) {
			if(s == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Save all the shop to an external file
	 */
	public void save() {
		// Define the default shops data file
		File f = new File(plugin.getDataFolder(), "data/shops.yml");
		this.save(f);
	}
	
	/**
	 * Safe all the shops to an external file
	 * @param f external file
	 */
	public void save(File f) {
		// Check if the shops file exists
		if(!f.exists()) {
			System.out.println("[SimpleShowcase] Shops data file doesn't exist. Creating a new file...");
		}
		
		long t = System.currentTimeMillis();
		
		// Show an message in the console
		System.out.println("[SimpleShowcase] Saving shops...");
		
		// Define the new config file holder to put all the shops in
		YamlConfiguration config = new YamlConfiguration();
		
		// Generate blank shops section first, to prevent bugs while loading if no shops where added
		config.createSection("shops");
		
		// Put each item/shop into the file
		for(SSShop s : this.shops) {
			// Put all the shop settings into the file
			String pid = String.valueOf(s.getId());
			config.set("shops." + pid + ".name", s.getName());
			config.set("shops." + pid + ".enabled", s.isEnabled());
			config.set("shops." + pid + ".owner.type", s.getOwnerType().getName());
			config.set("shops." + pid + ".owner.name", s.getOwnerName());
			config.set("shops." + pid + ".loc.w", s.getWorldName());
			config.set("shops." + pid + ".loc.x", s.getX());
			config.set("shops." + pid + ".loc.y", s.getY());
			config.set("shops." + pid + ".loc.z", s.getZ());
			config.set("shops." + pid + ".product.type", s.getProductType().getName());
			config.set("shops." + pid + ".product.apiPlugin", s.getProductApiPlugin());
			config.set("shops." + pid + ".product.id", s.getProductId());
			config.set("shops." + pid + ".product.name", s.getProductName());
			config.set("shops." + pid + ".product.typeId", s.getProductItemTypeId());
			config.set("shops." + pid + ".product.dataValue", s.getProductItemData());
			config.set("shops." + pid + ".stock.itemTypeId", s.getStockTypeId());
			config.set("shops." + pid + ".stock.itemDataValue", s.getStockDataValue());
			config.set("shops." + pid + ".stock.quantity", s.getStockQuantity());
			config.set("shops." + pid + ".pricelist.listName", s.getPricelistName());
			config.set("shops." + pid + ".pricelist.itemName", s.getPricelistItemName());
			config.set("shops." + pid + ".shopItem.show", s.showItem());
			config.set("shops." + pid + ".shopItem.typeId", s.getShowItemTypeId());
			config.set("shops." + pid + ".shopItem.dataValue", s.getShowItemData());
			config.set("shops." + pid + ".canBuy", s.canBuy());
			config.set("shops." + pid + ".canSell", s.canSell());
			config.set("shops." + pid + ".buyPrice", s.getBuyPrice());
			config.set("shops." + pid + ".sellPrice", s.getSellPrice());
			config.set("shops." + pid + ".quantityLimits.buyStack", s.getBuyStack());
			config.set("shops." + pid + ".quantityLimits.minBuyQuantity", s.getMinBuyQuantity());
			config.set("shops." + pid + ".quantityLimits.maxBuyQuantity", s.getMaxBuyQuantity());
			config.set("shops." + pid + ".quantityLimits.sellStack", s.getSellStack());
			config.set("shops." + pid + ".quantityLimits.minSellQuantity", s.getMinSellQuantity());
			config.set("shops." + pid + ".quantityLimits.maxSellQuantity", s.getMaxSellQuantity());
			config.set("shops." + pid + ".instant.instantBuy", s.instantBuyEnabled());
			config.set("shops." + pid + ".instant.instantBuyQuantity", s.getInstantBuyQuantity());
			config.set("shops." + pid + ".instant.instantSell", s.instantSellEnabled());
			config.set("shops." + pid + ".instant.instantSellQuantity", s.getInstantSellQuantity());
			config.set("shops." + pid + ".discount.buyDiscount", s.getBuyDiscount());
			config.set("shops." + pid + ".discount.buyDiscountMinQuantity", s.getBuyDiscountMinQuantity());
			config.set("shops." + pid + ".discount.sellFee", s.getSellFee());
			config.set("shops." + pid + ".discount.sellFeeMinQuantity", s.getSellFeeMinQuantity());
			config.set("shops." + pid + ".permissionNode", s.getPermissionNode());
			config.set("shops." + pid + ".removeShopWhenEmpty", s.removeShopWhenEmptyEnabled());
		}
		
		// Add the version code to the file
		config.set("version", plugin.getVersionCode());
		
		// Convert the file to a FileConfiguration and safe the file
		FileConfiguration fileConfig = config;
		try {
			fileConfig.save(f);
		} catch (IOException e) {
			System.out.println("[SimpleShowcase] Error while saving the shop data file!");
			e.printStackTrace();
			return;
		}

		// Calculate the save duration
		long duration = System.currentTimeMillis() - t;
		
		// Show an message in the console
		if(this.shops.size() == 1)
			System.out.println("[SimpleShowcase] 1 shop saved, took " + String.valueOf(duration) + "ms!");
		else
			System.out.println("[SimpleShowcase] " + String.valueOf(this.shops.size()) + " shops saved, took " + String.valueOf(duration) + "ms!");
	}
	
	/**
	 * Load all shop from an external file
	 */
	public void load() {
		// Define the default shops file path
		File f = new File(plugin.getDataFolder(), "data/shops.yml");
		this.load(f);
	}
	
	/**
	 * Load all the shops from an external file
	 * @param f external file
	 */
	public void load(File f) {		
		// Check if the shops file exists
		if(!f.exists()) {
			System.out.println("[SimpleShowcase] Shops data file doesn't exist!");
			return;
		}
		
		long t = System.currentTimeMillis();
		
		// Remove all shop show items
		removeAllShopShowItems();
		
		// Show an message in the console
		System.out.println("[SimpleShowcase] Loading shops...");
		
		// Load the shops file
		YamlConfiguration c = new YamlConfiguration();
		try {
			c.load(f);
		} catch (FileNotFoundException e) {
			System.out.println("[SimpleShowcase] Error while loading shops data file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("[SimpleShowcase] Error while loading shops data file!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.out.println("[SimpleShowcase] Error while loading shops data file!");
			e.printStackTrace();
		}
		
		// Create a list to put the new shops in
		List<SSShop> newShops = new ArrayList<SSShop>();
		
		// Get all the items
		Set<String> keys = c.getConfigurationSection("shops").getKeys(false);
		for(String s : keys) {
			
			// Get all the settings from the current item and add it to the new list
			int id = Integer.parseInt(s);
			String name = c.getString("shops." + s + ".name", "shop");
			boolean enabled = c.getBoolean("shops." + s + ".name", true);
			SSShopOwnerType ownerType = SSShopOwnerType.valueOf(c.getString("shops." + s + ".owner.type", "UNKNOWN"));
			String ownerName = c.getString("shops." + s + ".owner.name", "");
			
			String world = c.getString("shops." + s + ".loc.w", "world");
			int x = c.getInt("shops." + s + ".loc.x", 0);
			int y = c.getInt("shops." + s + ".loc.y", 0);
			int z = c.getInt("shops." + s + ".loc.z", 0);
			
			SSShopProductType shopProductType = SSShopProductType.valueOf(c.getString("shops." + s + ".product.type", "UNKNOWN"));
			String shopProductApiPlugin = c.getString("shops." + s + ".product.apiPlugin", "");
			int shopProductId = c.getInt("shops." + s + ".product.id", 0);
			String shopProductName = c.getString("shops." + s + ".product.name", "");
			int shopProductItemTypeId = c.getInt("shops." + s + ".product.typeId", 0);
			byte shopProductItemData = (byte) c.getInt("shops." + s + ".product.dataValue", 0);
			
			int stockTypeId = c.getInt("shops." + s + ".stock.itemTypeId", 0);
			byte stockDataValue = (byte) c.getInt("shops." + s + ".stock.itemDataValue", 0);
			int stockQuantity = c.getInt("shops." + s + ".stock.quantity", 0);
			
			String pricelistName = c.getString("shops." + s + ".pricelist.listName", "");
			String pricelistItemName = c.getString("shops." + s + ".pricelist.itemName", "");
			
			boolean showItem = c.getBoolean("shops." + s + ".shopItem.show", true);
			int showItemTypeId = c.getInt("shops." + s + ".shopItem.typeId", 0);
			byte showItemData = (byte) c.getInt("shops." + s + ".shopItem.dataValue", 0);
			
			boolean canBuy = c.getBoolean("shops." + s + ".canBuy", true); // If the item could be bought
			boolean canSell = c.getBoolean("shops." + s + ".canSell", true); // If the item could be sold
			
			double buyPrice = c.getDouble("shops." + s + ".buyPrice", 1.00);
			double sellPrice = c.getDouble("shops." + s + ".sellPrice", 1.00);
			
			int buyStack = c.getInt("shops." + s + ".quantityLimits.buyStack", -1);
			int minBuyQuantity = c.getInt("shops." + s + ".quantityLimits.minBuyQuantity", -1);
			int maxBuyQuantity = c.getInt("shops." + s + ".quantityLimits.maxBuyQuantity", -1);
			int sellStack = c.getInt("shops." + s + ".quantityLimits.sellStack", -1);
			int minSellQuantity = c.getInt("shops." + s + ".quantityLimits.minSellQuantity", -1);
			int maxSellQuantity = c.getInt("shops." + s + ".quantityLimits.maxSellQuantity", -1);
			
			boolean instantBuy = c.getBoolean("shops." + s + ".instant.instantBuy", false);
			int instantBuyQuantity = c.getInt("shops." + s + ".instant.instantBuyQuantity", 1);
			boolean instantSell = c.getBoolean("shops." + s + ".instant.instantSell", false);
			int instantSellQuantity = c.getInt("shops." + s + ".instant.intstantSellQuantity", 1);
			
			double buyDiscount = c.getInt("shops." + s + ".discount.buyDiscount", 1); // How more items you buy, how little the price for each item is
			int buyDiscountMinQuantity = c.getInt("shops." + s + ".discount.buyDiscountMinQuantity", -1); // The discount will be applied when the user buys this quantity or more
			double sellFee = c.getInt("shops." + s + ".discount.sellFee", 1); // How more items you sell, how more money you'll get for each item
			int sellFeeMinQuantity = c.getInt("shops." + s + ".discount.sellFreeMinQuantity", -1); // The fee will be applied when the user buys this quantity or more
			
			String permsNode = c.getString("shops." + s + ".permissionNode", "");
			
			boolean removeShopWhenEmpty = c.getBoolean("shops." + s + ".removeShopWhenEmpty", false); // Remove a player owned shop when it's empty
			
			SSShop newShop = new SSShop(
					id, name, enabled, ownerType, ownerName,
					world, x, y, z,
					shopProductType, shopProductApiPlugin, shopProductId, shopProductName, shopProductItemTypeId, shopProductItemData,
					stockTypeId, stockDataValue, stockQuantity,
					pricelistName, pricelistItemName,
					showItem, showItemTypeId, showItemData,
					canBuy, canSell,
					buyPrice, sellPrice,
					buyStack, minBuyQuantity, maxBuyQuantity, sellStack, minSellQuantity, maxSellQuantity,
					instantBuy, instantBuyQuantity, instantSell, instantSellQuantity,
					buyDiscount, buyDiscountMinQuantity, sellFee, sellFeeMinQuantity,
					permsNode,
					removeShopWhenEmpty);
			newShops.add(newShop);
		}
		
		// Overwrite the old shop list with this new one
		this.shops = newShops;

		// Spawn all the shop items of the new loaded shops
		respawnAllShopShowItems();
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		// Show a message in the console
		if(this.shops.size() == 1)
			System.out.println("[SimpleShowcase] 1 shop loaded, took " + String.valueOf(duration) + "ms!");
		else
			System.out.println("[SimpleShowcase] " + String.valueOf(this.shops.size()) + " shops loaded, took " + String.valueOf(duration) + "ms!");
	}
}
