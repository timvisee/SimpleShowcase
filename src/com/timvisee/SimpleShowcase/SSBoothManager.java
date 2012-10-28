package com.timvisee.SimpleShowcase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SSBoothManager {

	public static SimpleShowcase plugin;

	List<SSBooth> booths = new ArrayList<SSBooth>();
	
	// Selected booths of players. <PlayerName, PortalId>
	private final HashMap<String, Integer> selectedBooths = new HashMap<String, Integer>();

	// Get the last used booth
	public static HashMap<String, Integer> lastUsedBooth = new HashMap<String, Integer>();
	
	/**
	 * Constructor
	 * @param instance
	 */
	public SSBoothManager(SimpleShowcase instance) {
		plugin = instance;
	}
	
	/**
	 * Create a new booth
	 * @return the new created booth
	 */
	public SSBooth createBooth() {
		SSBooth b = new SSBooth(getUniqueId(), getUniqueName());
		addBooth(b, true);
		
		// Return the added booth
		return this.booths.get(this.booths.size() - 1);
	}
	
	/**
	 * Add a new booth
	 * @param s new booth
	 * @param overwrite true to overwrite the other booth if there already was a booth on this location
	 */
	public void addBooth(SSBooth s, boolean overwrite) {
		// The ID has to be unique, or else problems will be caused. If the ID isn't unique another unique ID will be used for the new booth
		if(!isUniqueId(s.getId())) {
			System.out.println("[SimpleShowcase] Force changed booth ID, the ID wasn't unique!");
			s.setId(getUniqueId());
		}
		
		// If there already is a booth on this location, does it needs to be overwritten?
		if(isBoothAt(s.getWorld(), s.getX(), s.getY(), s.getZ())) {
			if(overwrite) {
				SSBooth stemp = getBoothAt(s.getWorld(), s.getX(), s.getY(), s.getZ());
				
				// The variable may not be null
				if(stemp != null)
					removeBooth(stemp);
				
				// The older booth has probably been removed, add the booth to the list
				this.booths.add(s);
			}
		} else {
			// There isn't a booth already on this location add the booth
			this.booths.add(s);
		}
	}
	
	/**
	 * Remove a booth
	 * @param b the location
	 */
	public void removeBooth(Block b) {
		this.removeBooth(b.getLocation());
	}
	
	/**
	 * Remove a booth
	 * @param l the location
	 */
	public void removeBooth(Location l) {
		for(SSBooth s : this.booths) {
			if(s.getX() == l.getBlockX() &&
					s.getY() == l.getBlockY() &&
					s.getZ() == l.getBlockZ()) {
				this.booths.remove(s);
				return;
			}
		}
	}
	
	/**
	 * Remove a booth
	 * @param s the booth
	 */
	public void removeBooth(SSBooth s) {
		// Finally remove the booth itself
		booths.remove(s);
	}
	
	/**
	 * Get a list of all the booths
	 * @return all the booths
	 */
	public List<SSBooth> getBooths() {
		return this.booths;
	}
	
	/**
	 * Get a booth at the location of a block<br>
	 * If no booth was found null will be returned
	 * @param b the block
	 * @return the booth, or null when no booth was found on this location
	 */
	public SSBooth getBoothAt(Block b) {
		for(SSBooth s : this.booths) {
			if(s.isBoothAt(b)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Get a booth at any location<br>
	 * If no booth was found null will be returned
	 * @param l the location
	 * @return the booth, or null when no booth was found on this location
	 */
	public SSBooth getBoothAt(Location l) {
		for(SSBooth s : this.booths) {
			if(s.isBoothAt(l)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Get a booth at any location<br>
	 * If no booth was found null will be returned
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return the booth, or null when no booth was found on this location
	 */
	public SSBooth getBoothAt(String w, int x, int y, int z) {
		// Return null if no world was passed
		if(w == "")
			return null;
		
		for(SSBooth s : this.booths) {
			if(s.isBoothAt(w, x, y ,z)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Is there any booth at any location of a block
	 * @param b the block
	 * @return true when there's a booth at the location
	 */
	public boolean isBoothAt(Block b) {
		if(this.getBoothAt(b) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Is there any booth at any location
	 * @param l the location
	 * @return true when there's a booth at any location
	 */
	public boolean isBoothAt(Location l) {
		if(this.getBoothAt(l) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Is there any booth at any location
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true when there's a booth at any location
	 */
	public boolean isBoothAt(String w, int x, int y, int z) {
		// Return false if no world was passed
		if(w == "")
			return false;
		
		if(this.getBoothAt(w, x, y, z) == null) {
			return false;
		}
		return true;
	}
	
	public boolean isBoothUsed(Block b) {
		if(!isBoothAt(b))
			return false;
		
		return isBoothUsed(getBoothAt(b));
	}
	
	public boolean isBoothUsed(SSBooth b) {
		for(SSShop s : plugin.getShopManager().getShops()) {
			if(s.getWorld() == b.getWorld() &&
					s.getX() == b.getX() &&
					s.getY() == b.getY() &&
					s.getZ() == b.getZ())
				return true;
		}
		return false;
	}
	
	public boolean isBoothBaseAt(Block b) {
		for(SSBooth entry : this.booths) {
			if(entry.isBoothBaseAt(b))
				return true;
		}
		return false;
	}
	
	/**
	 * Count the amount of booths created
	 * @return amount of booths
	 */
	public int countBooths() {
		return this.booths.size();
	}
	
	/**
	 * Get the booth by id
	 * @param id booth id
	 * @return booth
	 */
	public SSBooth get(int id) {
		for(SSBooth s : this.booths) {
			if(s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Get the booth by name
	 * @param name booth name
	 * @return the booth, null if no booth was found
	 */
	public SSBooth get(String name) {
		for(SSBooth s : this.booths) {
			if(s.getName().equals(name)) {
				return s;
			}
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
	 * Check if this ID is already used by another booth
	 * @param id id
	 * @return false if unique
	 */
	public boolean isUniqueId(int id) {
		for(SSBooth s : this.booths) {
			if(s.getId() == id)
				return false;
		}
		return true;
	}
	
	/**
	 * Get an unique name for a new booth
	 * @return an unique name
	 */
	public String getUniqueName() {
		int i = 1;
		while(true) {
			String name = "booth" + String.valueOf(i);
			if(isUniqueName(name))
				return name;
			i++;
		}
	}
	
	/**
	 * Check if this name is already used by another booth
	 * @param id id
	 * @return false if unique
	 */
	public boolean isUniqueName(String name) {
		for(SSBooth s : this.booths) {
			if(s.getName().equals(name))
				return false;
		}
		return true;
	}
	
	public void selectBooth(String player, SSBooth b) {
		deselectBooth(player);
		
		selectedBooths.put(player, b.getId());
	}
	
	public SSBooth getSelectedBooth(String player) {
		if(!hasBoothSelected(player)) {
			return null;
		}
		
		int id = selectedBooths.get(player);
		for(int i = 0; i < this.booths.size(); i++) {
			if(this.booths.get(i).getId() == id) {
				return this.booths.get(i);
			}
		}
		
		return null;
	}
	
	public boolean hasBoothSelected(String player) {
		if(!this.selectedBooths.containsKey(player))
			return false;
		else {
			// Make sure the selected booth does still exists
			return !this.isUniqueId(selectedBooths.get(player));
		}
	}
	
	public void deselectBooth(String player) {
		if(hasBoothSelected(player)) {
			selectedBooths.remove(player);
		}
	}
	
	public void setLastUsedBooth(String player, SSBooth b) {
		forgetLastUsedBooth(player);
		
		lastUsedBooth.put(player, b.getId());
	}
	
	public SSBooth getLastUsedBooth(String player) {
		if(!hasBoothSelected(player)) {
			return null;
		}
		
		int id = lastUsedBooth.get(player);
		for(int i = 0; i < this.booths.size(); i++) {
			if(this.booths.get(i).getId() == id) {
				return this.booths.get(i);
			}
		}
		
		return null;
	}
	
	public boolean hasLastUsedBooth(String player) {
		if(!lastUsedBooth.containsKey(player))
			return false;
		else {
			// Make sure the selected booth does still exists
			return !this.isUniqueId(lastUsedBooth.get(player));
		}
	}
	
	public void forgetLastUsedBooth(String player) {
		if(hasBoothSelected(player)) {
			lastUsedBooth.remove(player);
		}
	}
	
	/**
	 * Save all the booth to an external file
	 */
	public void save() {
		// Define the default booths data file
		File f = new File(plugin.getDataFolder(), "data/booths.yml");
		this.save(f);
	}
	
	/**
	 * Safe all the booths to an external file
	 * @param f external file
	 */
	public void save(File f) {
		// Check if the booths file exists
		if(!f.exists()) {
			System.out.println("[SimpleShowcase] Booth data file doesn't exist. Creating a new file...");
		}
		
		long t = System.currentTimeMillis();
		
		// Show an message in the console
		System.out.println("[SimpleShowcase] Saving booths...");
		
		// Define the new config file holder to put all the booths in
		YamlConfiguration config = new YamlConfiguration();
		
		// Generate blank booths section first, to prevent bugs while loading if no booths where added
		config.createSection("booths");
		
		// Put each item/booth into the file
		for(SSBooth b : this.booths) {
			// Put all the booth settings into the file
			String pid = String.valueOf(b.getId());
			config.set("booths." + pid + ".name", b.getName());
			config.set("booths." + pid + ".enabled", b.isEnabled());
			config.set("booths." + pid + ".loc.w", b.getWorldName());
			config.set("booths." + pid + ".loc.x", b.getX());
			config.set("booths." + pid + ".loc.y", b.getY());
			config.set("booths." + pid + ".loc.z", b.getZ());
			config.set("booths." + pid + ".claimPrice", b.getClaimPrice());
			config.set("booths." + pid + ".permissionNode", b.getPermissionNode());
		}
		
		// Add the version code to the file
		config.set("version", plugin.getVersionCode());
		
		// Convert the file to a FileConfiguration and safe the file
		FileConfiguration fileConfig = config;
		try {
			fileConfig.save(f);
		} catch (IOException e) {
			System.out.println("[SimpleShowcase] Error while saving the booth data file!");
			e.printStackTrace();
			return;
		}

		// Calculate the save duration
		long duration = System.currentTimeMillis() - t;
		
		// Show an message in the console
		if(this.booths.size() == 1)
			System.out.println("[SimpleShowcase] 1 booth saved, took " + String.valueOf(duration) + "ms!");
		else
			System.out.println("[SimpleShowcase] " + String.valueOf(this.booths.size()) + " booths saved, took " + String.valueOf(duration) + "ms!");
	}
	
	/**
	 * Load all booth from an external file
	 */
	public void load() {
		// Define the default booths file path
		File f = new File(plugin.getDataFolder(), "data/booths.yml");
		this.load(f);
	}
	
	/**
	 * Load all the booths from an external file
	 * @param f external file
	 */
	public void load(File f) {		
		// Check if the booths file exists
		if(!f.exists()) {
			System.out.println("[SimpleShowcase] Booth data file doesn't exist!");
			return;
		}
		
		long t = System.currentTimeMillis();
		
		// Show an message in the console
		System.out.println("[SimpleShowcase] Loading booths...");
		
		// Load the booths file
		YamlConfiguration c = new YamlConfiguration();
		try {
			c.load(f);
		} catch (FileNotFoundException e) {
			System.out.println("[SimpleShowcase] Error while loading booths data file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("[SimpleShowcase] Error while loading booths data file!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.out.println("[SimpleShowcase] Error while loading booths data file!");
			e.printStackTrace();
		}
		
		// Create a list to put the new booths in
		List<SSBooth> newBooths = new ArrayList<SSBooth>();
		
		// Get all the items
		Set<String> keys = c.getConfigurationSection("booths").getKeys(false);
		for(String s : keys) {
			
			// Get all the settings from the current item and add it to the new list
			int id = Integer.parseInt(s);
			String name = c.getString("booths." + s + ".name", "booth");
			boolean enabled = c.getBoolean("booths." + s + ".name", true);
			
			String world = c.getString("booths." + s + ".loc.w", "world");
			int x = c.getInt("booths." + s + ".loc.x", 0);
			int y = c.getInt("booths." + s + ".loc.y", 0);
			int z = c.getInt("booths." + s + ".loc.z", 0);
			
			double claimPrice = c.getDouble("booths." + s + ".claimPrice", 0.00);
			
			String permsNode = c.getString("booths." + s + ".permissionNode", "");
			
			SSBooth newBooth = new SSBooth(id, name, enabled,
					world, x, y, z,
					claimPrice,
					permsNode);
			
			newBooths.add(newBooth);
		}
		
		// Overwrite the old booth list with this new one
		this.booths = newBooths;
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		// Show a message in the console
		if(this.booths.size() == 1)
			System.out.println("[SimpleShowcase] 1 booth loaded, took " + String.valueOf(duration) + "ms!");
		else
			System.out.println("[SimpleShowcase] " + String.valueOf(this.booths.size()) + " booths loaded, took " + String.valueOf(duration) + "ms!");
	}
}
