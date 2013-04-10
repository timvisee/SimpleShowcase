package com.timvisee.simpleshowcase;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SSBooth {

	private int id = 0;
	private String name = "booth";
	private boolean enabled = true;
	
	private String world = "";
	private int x = 0;
	private int y = 0;
	private int z = 0;
	
	private double claimPrice = 0.00;
	
	private String permsNode = "";
	
	SSBooth(int id, String name, boolean enabled,
			String world, int x, int y, int z,
			double claimPrice,
			String permsNode) {
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.claimPrice = claimPrice;
		this.permsNode = permsNode;
	}
	
	SSBooth(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Check if the booth is at the following location
	 * @param l location
	 * @return true if the booth is on the right location
	 */
	public boolean isBoothAt(Location l) {
		return (isBoothBaseAt(l) || isBoothCaseAt(l));
	}
	
	/**
	 * Check if the booth is at the following location
	 * @param b block
	 * @return the if the booth is on the right location
	 */
	public boolean isBoothAt(Block b) {
		return (isBoothBaseAt(b) || isBoothCaseAt(b));
	}
	
	/**
	 * Check if the booth is at the following location
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the booth is on the right location
	 */
	public boolean isBoothAt(String w, int x, int y, int z) {
		return (isBoothBaseAt(w, x, y, z) || isBoothCaseAt(w, x, y, z));
	}
	
	/**
	 * Check if the booth is at the following location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the booth case is on the right location
	 */
	public boolean isBoothAt(int x, int y, int z) {
		return (isBoothBaseAt(x, y, z) || isBoothCaseAt(x, y, z));
	}
	
	/**
	 * Check if the booth is at the following location
	 * @param l location
	 * @return true if the booth is on the right location
	 */
	public boolean isBoothBaseAt(Location l) {
		return this.isBoothBaseAt(l.getBlock());
	}
	
	/**
	 * Check if the booth is at the following location
	 * @param b block
	 * @return the if the booth is on the right location
	 */
	public boolean isBoothBaseAt(Block b) {
		return (this.isBoothBaseAt(b.getWorld().getName(),
				b.getX(), b.getY(), b.getZ()));
	}
	
	/**
	 * Check if the booth is at the following location
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the booth is on the right location
	 */
	public boolean isBoothBaseAt(String w, int x, int y, int z) {
		if(this.world.equals(w)) {
			// The booths is in the right world, check the coordinates
			return (this.isBoothBaseAt(x, y, z));
		}
		return false;
	}
	
	/**
	 * Check if the booth case is at the following location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the booth case is on the right location
	 */
	public boolean isBoothBaseAt(int x, int y, int z) {
		return (this.x == x && this.y == y && this.z == z);
	}
	
	/**
	 * Check if the booth case is at the following location
	 * @param l location
	 * @return true if the booth case is on the right location
	 */
	public boolean isBoothCaseAt(Location l) {
		return this.isBoothCaseAt(l.getBlock());
	}
	
	/**
	 * Check if the booth case is at the following location
	 * @param b block
	 * @return the if the booth case is on the right location
	 */
	public boolean isBoothCaseAt(Block b) {
		return (this.isBoothCaseAt(b.getWorld().getName(),
				b.getX(), b.getY(), b.getZ()));
	}
	
	/**
	 * Check if the booth case is at the following location
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the booth case is on the right location
	 */
	public boolean isBoothCaseAt(String w, int x, int y, int z) {
		if(this.world.equals(w)) {
			// The booths is in the right world, check the coordinates
			return (this.isBoothCaseAt(x, y, z));
		}
		return false;
	}
	
	/**
	 * Check if the booth is at the following location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the booth is on the right location
	 */
	public boolean isBoothCaseAt(int x, int y, int z) {
		return (this.x == x && this.y == y - 1 && this.z == z);
	}
	
	public int getId() {
		return this.id;
	}
	
	protected void setId(int id) {
		if(id >= 0)
			this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		if(!name.trim().equals(""))
			this.name = name;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Check if the booth location is set
	 * @return true if set
	 */
	public boolean isLocationSet() {
		return !this.world.equals("");
	}
	
	/**
	 * Get the world name of the booth
	 * @return world name
	 */
	public String getWorld() {
		return this.world;
	}
	
	/**
	 * Get the world name of the booth
	 * @return world name
	 */
	public String getWorldName() {
		return this.world;
	}
	
	/**
	 * Get the world of the booth
	 * @param s the server
	 * @return the world
	 */
	public World getWorld(Server s) {
		return s.getWorld(this.world);
	}
	
	/**
	 * Set the world name of the booth
	 * @param world world name
	 */
	public void setWorld(String w) {
		this.world = w;
	}
	
	/**
	 * Set the world of the booth
	 * @param world world
	 */
	public void setWorld(World w) {
		this.setWorld(w.getName());
	}
	
	/**
	 * Set the world of the booth
	 * @param block block
	 */
	public void setWorld(Block b) {
		this.setWorld(b.getWorld().getName());
	}
	
	/**
	 * Set the world of the booth
	 * @param loc location
	 */
	public void setWorld(Location l) {
		this.setWorld(l.getWorld().getName());
	}
	
	/**
	 * Get the x coordinate of the booth
	 * @return x coordinate
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Set the x coordinate of the booth
	 * @param x x coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Get the y coordinate of the booth
	 * @return y coordinate
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Set the y coordinate of the booth
	 * @param y y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Get the z coordinate of the booth
	 * @return z coordinate
	 */
	public int getZ() {
		return this.z;
	}
	
	/**
	 * Set the z coordinate of the booth
	 * @param z z coordinate
	 */
	public void setZ(int z) {
		this.z = z;
	}
	
	/**
	 * Get the booth block
	 * @param s the server
	 * @return the block
	 */
	public Block getBlock(Server s) {
		// check if the location is set
		if(!isLocationSet())
			return null;
		
		// Define the world
		World w = s.getWorld(this.world);
		
		// The world variable may not be null
		if(w == null)
			return null;
		
		return getBlock(w);
	}
	
	/**
	 * Get the booth block
	 * @param w the world where the booth is in
	 * @return the block
	 */
	public Block getBlock(World w) {
		// Check if the booth location is set
		if(!isLocationSet())
			return null;
		
		// Define the booth block and the one above
		Block b = w.getBlockAt(this.x, this.y, this.z);
		
		// The block variables may not be null
		if(b == null)
			return null;
		return b;
	}
	
	/**
	 * Get the location of the booth
	 * @param s the server
	 * @return the location. Null if the location isn't set
	 */
	public Location getLocation(Server s) {
		return getBlock(s).getLocation();
	}
	
	/**
	 * Set the block/location of the booth
	 * @param block block
	 * @param s the server
	 */
	public void setLocation(Block block, Server s) {
		this.world = block.getWorld().getName();
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
	}
	
	public double stringToDouble(String doubleString) {
		doubleString = doubleString.replace(",", ".");
		BigDecimal d = new BigDecimal(doubleString);
		return d.doubleValue();
	}
	@SuppressWarnings("unused")
	private double roundMoney(double money) {
	    DecimalFormat twoDForm = new DecimalFormat("#0.##");
	    return stringToDouble(twoDForm.format(money));
	}
	private String moneyToStringProper(double money) {
		DecimalFormat twoDForm = new DecimalFormat("#0.00");
	    return twoDForm.format(money);
	}
	
	public double getClaimPrice() {
		return this.claimPrice;
	}
	
	public String getClaimPriceProper() {
		return moneyToStringProper(this.claimPrice);
	}
	
	public void setClaimPrice(double price) {
		this.claimPrice = price;
	}
	
	/**
	 * Get the current permission node of the booth
	 * @return permissions node
	 */
	public String getPermissionNode() {
		return this.permsNode;
	}
	
	/**
	 * Check if the booth uses a permission node
	 * @return true if used
	 */
	public boolean usesPermissionNode() {
		return !this.permsNode.equals("");
	}
	
	/**
	 * Set the permission node of the booth
	 * @param node permission node
	 */
	public void setPermissionNode(String node) {
		this.permsNode = node;
	}
	
	/**
	 * Reset the permission node of a booth
	 */
	public void resetPermissionNode() {
		this.permsNode = "";
	}
}
