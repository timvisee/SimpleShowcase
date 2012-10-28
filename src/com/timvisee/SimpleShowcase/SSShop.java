package com.timvisee.SimpleShowcase;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SSShop {

	private int id = 0;
	private String name = "shop";
	private boolean enabled = true;
	private SSShopOwnerType ownerType = SSShopOwnerType.SERVER;
	private String ownerName = "";
	
	private String world = "";
	private int x = 0;
	private int y = 0;
	private int z = 0;
	
	private SSShopProductType productType = SSShopProductType.UNKNOWN;
	private String productApiPlugin = "";
	private int productId = 0;
	private String productName = "";
	private int productItemTypeId = 0;
	private byte productItemData = 0;
	
	private int stockTypeId = 0;
	private byte stockDataValue = 0;
	private int stockQuantity = 0;
	
	private String pricelistName = "";
	private String pricelistItemName = "";
	
	private boolean showItem = true;
	private int showItemTypeId = 0;
	private byte showItemData = 0;
	
	private boolean canBuy = true; // If the item could be bought
	private boolean canSell = true; // If the item could be sold
	
	private double buyPrice = 0.00;
	private double sellPrice = 0.00;
	
	private int buyStack = -1;
	private int minBuyQuantity = -1;
	private int maxBuyQuantity = -1;
	private int sellStack = -1;
	private int minSellQuantity = -1;
	private int maxSellQuantity = -1;
	
	private boolean instantBuy = false;
	private int instantBuyQuantity = 1;
	private boolean instantSell = false;
	private int instantSellQuantity = 1;
	
	private double buyDiscount = 0; // How more items you buy, how little the price for each item is
	private int buyDiscountMinQuantity = 1; // The discount will be applied when the user buys this quantity or more
	private double sellFee = 0; // How more items you sell, how more money you'll get for each item
	private int sellFeeMinQuantity = 1; // The fee will be applied when the user buys this quantity or more
	
	private String permsNode = "";
	
	private boolean removeShopWhenEmpty = false; // Remove a player owned shop when it's empty
	
	// Shop item holder
	Item shopItem;
	
	
	/**
	 * Class constructor
	 * @param id shop ID
	 * @param name shop name
	 * @param enabled shop enabled
	 * @param ownerType shop owner type
	 * @param ownerName shop owner name
	 * @param world shop world
	 * @param x shop x coordinate
	 * @param y shop y coordinate
	 * @param z shop z coordinate
	 * @param shopProductType shop product type
	 * @param shopProductItemTypeId shop product item type id
	 * @param shopProductItemData shop product item data values
	 * @param stockTypeId stock items type id
	 * @param stockDataValue stock items data value
 	 * @param stockQuantity stock items quantity
	 * @param priceListName price list name
	 * @param priceListItemName price list item name
	 * @param shopProductName shop product name
	 * @param showItem show an item
	 * @param showItemTypeId show item type id
	 * @param showItemData show item data values
	 * @param canBuy can item be bought
	 * @param canSell can item be sold
	 * @param buyPrice product buy price
	 * @param sellPrice product sell price 
	 * @param buyStack buy the items in a stack
	 * @param minBuyAmount minimum amount of items that can be bought
	 * @param maxBuyAmount maximum amount of items that can be bought each time
	 * @param sellStack sell the items in a stack
	 * @param minSellAmount minimum amount of items that can be sold
	 * @param maxSellAmount maximum amount of items that can be sold each time
	 * @param instantBuy instantly buy the item
	 * @param instantBuyAmount instant buy amount
	 * @param instantSell instantly sell the item
	 * @param instantSellAmount instant sell amount
	 * @param buyDiscount additional discount for bought items
	 * @param buyDiscountMinAmount apply discount when this amount of items has been bought
	 * @param sellFee additional fee for sold items
	 * @param sellFeeMinAmount apply fee when this amount of items has been sold
	 * @param removeShopWhenEmpty remove the shop when it's empty
	 */
	public SSShop(int id, String name, boolean enabled, SSShopOwnerType ownerType, String ownerName,
			String world, int x, int y, int z,
			SSShopProductType shopProductType, String shopProductApiName, int shopProductId, String shopProductName, int shopProductItemTypeId, byte shopProductItemData,
			int stockTypeId, byte stockDataValue, int stockQuantity,
			String priceListName, String priceListItemName,
			boolean showItem, int showItemTypeId, byte showItemData,
			boolean canBuy, boolean canSell,
			double buyPrice, double sellPrice,
			int buyStack, int minBuyQuantity, int maxBuyQuantity, int sellStack, int minSellQuantity, int maxSellQuantity,
			boolean instantBuy, int instantBuyQuantity, boolean instantSell, int instantSellQuantity,
			double buyDiscount, int buyDiscountMinQuantity, double sellFee, int sellFeeMinQuantity,
			String permsNode,
			boolean removeShopWhenEmpty) {
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.ownerType = ownerType;
		this.ownerName = ownerName;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.productType = shopProductType;
		this.productItemTypeId = shopProductItemTypeId;
		this.productItemData = shopProductItemData;
		this.stockTypeId = stockTypeId;
		this.stockDataValue = stockDataValue;
		this.stockQuantity = stockQuantity;
		this.pricelistName = priceListName;
		this.pricelistItemName = priceListItemName;
		this.productName = shopProductName;
		this.showItem = showItem;
		this.showItemTypeId = showItemTypeId;
		this.showItemData = showItemData;
		this.canBuy = canBuy;
		this.canSell = canSell;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.buyStack = buyStack;
		this.minBuyQuantity = minBuyQuantity;
		this.maxBuyQuantity = maxBuyQuantity;
		this.sellStack = sellStack;
		this.minSellQuantity = minSellQuantity;
		this.maxSellQuantity = maxSellQuantity;
		this.instantBuy = instantBuy;
		this.instantBuyQuantity = instantBuyQuantity;
		this.instantSell = instantSell;
		this.instantSellQuantity = instantSellQuantity;
		this.buyDiscount = buyDiscount;
		this.buyDiscountMinQuantity = buyDiscountMinQuantity;
		this.sellFee = sellFee;
		this.sellFeeMinQuantity = sellFeeMinQuantity;
		this.permsNode = permsNode;
		this.removeShopWhenEmpty = removeShopWhenEmpty;
	}
	
	/**
	 * Class constructor
	 * @param id shop ID
	 * @param name shop name
	 * @param world shop world
	 * @param x shop x coordinate
	 * @param y shop y coordinate
	 * @param z shop z coordinate
	 */
	public SSShop(int id, String name, String world, int x, int y, int z) {
		this.id = id;
		this.name = name;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Class constructor
	 * @param id shop ID
	 * @param name shop name
	 */
	public SSShop(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param l location
	 * @return true if the shop is on the right location
	 */
	public boolean isShopAt(Location l) {
		return (isShopBaseAt(l) || isShopCaseAt(l));
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param b block
	 * @return the if the shop is on the right location
	 */
	public boolean isShopAt(Block b) {
		return (isShopBaseAt(b) || isShopCaseAt(b));
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the shop is on the right location
	 */
	public boolean isShopAt(String w, int x, int y, int z) {
		return (isShopBaseAt(w, x, y, z) || isShopCaseAt(w, x, y, z));
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the shop is on the right location
	 */
	public boolean isShopAt(int x, int y, int z) {
		return (isShopBaseAt(x, y, z) || isShopCaseAt(x, y, z));
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param l location
	 * @return true if the shop is on the right location
	 */
	public boolean isShopBaseAt(Location l) {
		return this.isShopBaseAt(l.getBlock());
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param b block
	 * @return the if the shop is on the right location
	 */
	public boolean isShopBaseAt(Block b) {
		return (this.isShopBaseAt(b.getWorld().getName(),
				b.getX(), b.getY(), b.getZ()));
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the shop is on the right location
	 */
	public boolean isShopBaseAt(String w, int x, int y, int z) {
		if(this.world.equals(w)) {
			// The shops is in the right world, check the coordinates
			return (this.isShopBaseAt(x, y, z));
		}
		return false;
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the shop is on the right location
	 */
	public boolean isShopBaseAt(int x, int y, int z) {
		return (this.x == x && this.y == y && this.z == z);
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param l location
	 * @return true if the shop is on the right location
	 */
	public boolean isShopCaseAt(Location l) {
		return this.isShopCaseAt(l.getBlock());
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param b block
	 * @return the if the shop is on the right location
	 */
	public boolean isShopCaseAt(Block b) {
		return (this.isShopCaseAt(b.getWorld().getName(),
				b.getX(), b.getY(), b.getZ()));
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param w the world
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the shop is on the right location
	 */
	public boolean isShopCaseAt(String w, int x, int y, int z) {
		if(this.world.equals(w)) {
			// The shops is in the right world, check the coordinates
			return this.isShopCaseAt(x, y, z);
		}
		return false;
	}
	
	/**
	 * Check if the shop is at the following location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return true if the shop is on the right location
	 */
	public boolean isShopCaseAt(int x, int y, int z) {
		return (this.x == x && this.y == y - 1 && this.z == z);
	}
	
	/**
	 * Get the shop ID
	 * @return shop ID
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Set the shop ID
	 * @param id shop Id
	 */
	protected void setId(int id) {
		// The ID has to be 0 or above
		if(id >= 0) {
			this.id = id;
		}
	}
	
	/**
	 * Get the name of the shop
	 * @return shop name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set the name of the shop
	 * @param name shop name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Check if the current shop is enabled or not
	 * @return true if it's enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Get the owner type of the shop
	 * @return owner type
	 */
	public SSShopOwnerType getOwnerType() {
		return this.ownerType;
	}
	
	/**
	 * Set the owner type of the shop
	 * @param ownerType owner type
	 */
	public void setOwnerType(SSShopOwnerType ownerType) {
		this.ownerType = ownerType;
	}
	
	/**
	 * Get the owner name of the shop
	 * @return owner name
	 */
	public String getOwnerName() {
		return this.ownerName;
	}
	
	/**
	 * Set the owner name of the shop
	 * @param ownerName owner name
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	/**
	 * Check if the shop location is set
	 * @return true if set
	 */
	public boolean isLocationSet() {
		return !this.world.equals("");
	}
	
	/**
	 * Get the world name of the shop
	 * @return world name
	 */
	public String getWorld() {
		return this.world;
	}
	
	/**
	 * Get the world name of the shop
	 * @return world name
	 */
	public String getWorldName() {
		return this.world;
	}
	
	/**
	 * Get the world of the shop
	 * @param s the server
	 * @return the world
	 */
	public World getWorld(Server s) {
		return s.getWorld(this.world);
	}
	
	/**
	 * Set the world name of the shop
	 * @param world world name
	 */
	public void setWorld(String w) {
		this.world = w;
	}
	
	/**
	 * Set the world of the shop
	 * @param world world
	 */
	public void setWorld(World w) {
		this.setWorld(w.getName());
	}
	
	/**
	 * Set the world of the shop
	 * @param block block
	 */
	public void setWorld(Block b) {
		this.setWorld(b.getWorld().getName());
	}
	
	/**
	 * Set the world of the shop
	 * @param loc location
	 */
	public void setWorld(Location l) {
		this.setWorld(l.getWorld().getName());
	}
	
	/**
	 * Get the x coordinate of the shop
	 * @return x coordinate
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Set the x coordinate of the shop
	 * @param x x coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Get the y coordinate of the shop
	 * @return y coordinate
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Set the y coordinate of the shop
	 * @param y y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Get the z coordinate of the shop
	 * @return z coordinate
	 */
	public int getZ() {
		return this.z;
	}
	
	/**
	 * Set the z coordinate of the shop
	 * @param z z coordinate
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * Get the shop base block
	 * @param s the server
	 * @return the base block
	 */
	public Block getBaseBlock(Server s) {
		// check if the location is set
		if(!isLocationSet())
			return null;
		
		// Define the world
		World w = s.getWorld(this.world);
		
		// The world variable may not be null
		if(w == null)
			return null;
		
		return getBaseBlock(w);
	}

	/**
	 * Get the shop case block
	 * @param s the server
	 * @return the case block
	 */
	public Block getCaseBlock(Server s) {
		// check if the location is set
		if(!isLocationSet())
			return null;
		
		// Define the world
		World w = s.getWorld(this.world);
		
		// The world variable may not be null
		if(w == null)
			return null;
		
		return getCaseBlock(w);
	}
	
	/**
	 * Get the shop block
	 * @param s the server
	 * @return the block
	 */
	public List<Block> getBlocks(Server s) {
		// check if the location is set
		if(!isLocationSet())
			return null;
		
		// Define the world
		World w = s.getWorld(this.world);
		
		// The world variable may not be null
		if(w == null)
			return null;
		
		List<Block> blocks = new ArrayList<Block>();
		blocks.add(getBaseBlock(w));
		blocks.add(getCaseBlock(w));
		return blocks;
	}
	
	/**
	 * Get the shop block
	 * @param w the world where the shop is in
	 * @return the block
	 */
	public Block getBaseBlock(World w) {
		// Check if the shop location is set
		if(!isLocationSet())
			return null;
		
		// Define the shop block and the one above
		Block b = w.getBlockAt(this.x, this.y, this.z);
		
		// The block variables may not be null
		if(b == null)
			return null;
		return b;
	}
	
	/**
	 * Get the shop block
	 * @param w the world where the shop is in
	 * @return the block
	 */
	public Block getCaseBlock(World w) {
		// Check if the shop location is set
		if(!isLocationSet())
			return null;
		
		// Define the shop block and the one above
		Block b = w.getBlockAt(this.x, this.y + 1, this.z);
		
		// The block variables may not be null
		if(b == null)
			return null;
		return b;
	}
	
	/**
	 * Get the location of the shop
	 * @param s the server
	 * @return the location. Null if the location isn't set
	 */
	public Location getLocation(Server s) {
		return getBaseBlock(s).getLocation();
	}
	
	/**
	 * Set the block/location of the shop
	 * @param block block
	 * @param s the server
	 */
	public void setLocation(Block block, Server s) {
		this.world = block.getWorld().getName();
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		
		// Remove the show item from the old location if it's spawned
		if(isShowItemSpawned())
			removeShowItem();
		
		// Check if the show item should be spawned, if so spawn it on the new location
		if(shouldShowItemBeSpawned(s))
			spawnShowItem(s);
	}
	
	/**
	 * Set the block/location of the shop
	 * @param b the booth to put the shop on
	 */
	public void setLocation(SSBooth b) {
		setWorld(b.getWorld());
		setX(b.getX());
		setY(b.getY());
		setZ(b.getZ());
	}
	
	/**
	 * Check if any product is set
	 * @return
	 */
	public boolean isProductSet() {
		if(this.productType.equals(SSShopProductType.UNKNOWN))
			return false;
		return true;
	}
	
	/**
	 * Get the shop product type
	 * @return product type
	 */
	public SSShopProductType getProductType() {
		return this.productType;
	}
	
	/**
	 * Set the shop product type
	 * @param productType product type
	 */
	public void setProductType(SSShopProductType productType) {
		this.productType = productType;
	}
	
	/**
	 * Get the shop product api plugin
	 * @return shop product api plugin
	 */
	public String getProductApiPlugin() {
		return this.productApiPlugin;
	}
	
	/**
	 * Set the shop product custom plugin
	 * @param name shop product custom plugin
	 */
	public void setProductApiPlugin(String plugin) {
		this.productApiPlugin = name;
	}
	
	/**
	 * Get the shop product id
	 * @return product id
	 */
	public int getProductId() {
		return this.productId;
	}
	
	/**
	 * Set the shop product id
	 * @param id product id
	 */
	public void setProductId(int id) {
		this.productId = id;
	}
	
	/**
	 * Get the shop product name
	 * @return product name
	 */
	public String getProductName() {
		return this.productName;
	}
	
	/**
	 * Set the shop product name
	 * @param name product name
	 */
	public void setProductName(String name) {
		this.productName = name;
	}
	
	/**
	 * Get the shop product item type id
	 * @return product item type id
	 */
	public int getProductItemTypeId() {
		return this.productItemTypeId;
	}
	
	/**
	 * Set the shop product item type id
	 * @param typeId product item type id
	 */
	public void setProductItemTypeId(int typeId, Server s) {
		this.productItemTypeId = typeId;
		
		// Change the show item if it isn't set yet
		if(showItem()) {
			if(this.showItemTypeId == 0) {
				this.showItemTypeId = typeId;
				
				if(isShowItemSpawned())
					removeShowItem();
				
				if(shouldShowItemBeSpawned(s))
					spawnShowItem(s);
			}
		}
	}
	
	/**
	 * Get the shop product item data value
	 * @return product item data value
	 */
	public byte getProductItemData() {
		return this.productItemData;
	}
	
	public int getStockTypeId() {
		return this.stockTypeId;
	}
	
	public void setStockTypeId(int typeId) {
		this.stockTypeId = typeId;
	}
	
	public byte getStockDataValue() {
		return this.stockDataValue;
	}
	
	public void setStockDataValue(byte dataValue) {
		this.stockDataValue = dataValue;
	}
	
	public int getStockQuantity() {
		return this.stockQuantity;
	}
	
	public void setStockQuantity(int q) {
		this.stockQuantity = q;
	}
	
	/**
	 * Set the shop product item data value
	 * @param itemData product item data value
	 */
	public void setProductItemData(byte itemData, Server s) {
		this.productItemData = itemData;
		
		// Change the show item if it isn't set yet
		if(showItem()) {
			if(this.showItemData == 0) {
				this.showItemData = itemData;
				
				if(isShowItemSpawned())
					removeShowItem();
				
				if(shouldShowItemBeSpawned(s))
					spawnShowItem(s);
			}
		}
	}
	
	public boolean usesPricelist() {
		return (!this.pricelistItemName.equals(""));
	}
	
	public void linkPricelistItem(SSPricelistItem i) {
		setPricelistName(i.getPricelistName());
		setPricelistItemName(i.getItemName());
	}
	
	public void unlinkPricelistItem() {
		setPricelistName("");
		setPricelistItemName("");
	}
	
	public String getPricelistName() {
		return this.pricelistName;
	}
	
	public void setPricelistName(String priceListName) {
		this.pricelistName = priceListName;
	}
	
	public String getPricelistItemName() {
		return this.pricelistItemName;
	}
	
	public void setPricelistItemName(String priceListItemName) {
		this.pricelistItemName = priceListItemName;
	}
	
	/**
	 * Check if any item will be shown on top of the shop block
	 * @return true if any item will be shown
	 */
	public boolean showItem() {
		if(this.ownerType.equals(SSShopOwnerType.PLAYER))
			return (this.stockTypeId != 0 && this.stockQuantity != 0);
		else
			return this.showItem;
	}
	
	/**
	 * Set if any item will be shown on top of the shop block
	 * @param show if any item will be shown
	 */
	public void showItem(boolean show) {
		this.showItem = show;
	}
	
	/**
	 * Get the type id of the item that will be shown on top of the shop
	 * @return item type id
	 */
	public int getShowItemTypeId() {
		if(this.ownerType.equals(SSShopOwnerType.PLAYER))
			if(this.stockTypeId != 0 && this.stockQuantity != 0)
				return this.stockTypeId;
		return this.showItemTypeId;
	}
	
	/**
	 * Set the type id of the item that will be shown on top of the shop
	 * @param typeId item type id
	 */
	public void setShowItemTypeId(int typeId) {
		// The item may not be air
		if(typeId == 0)
			return;
		
		this.showItemTypeId = typeId;
	}
	
	/**
	 * Get the data value of the item that will be shown on top of the shop
	 * @return item data value
	 */
	public byte getShowItemData() {
		if(this.ownerType.equals(SSShopOwnerType.PLAYER)) {
			if(this.stockTypeId != 0 && this.stockQuantity != 0)
				return this.stockDataValue;
		}
		return this.showItemData;
	}
	
	/**
	 * Set the data value of the item that will be shown on top of the shop
	 * @param itemData item data value
	 */
	public void setShowItemData(byte itemData) {
		this.showItemData = itemData;
	}
	
	/**
	 * If the product could be bought
	 * @return true if the product could be bought
	 */
	public boolean canBuy() {
		return this.canBuy;
	}
	
	/**
	 * Set if the product could be bought
	 * @param canBuy if the product could be bought
	 */
	public void setCanBuy(boolean canBuy) {
		this.canBuy = canBuy;
	}
	
	/**
	 * If the product could be sold
	 * @return true if the product could be sold
	 */
	public boolean canSell() {
		return this.canSell;
	}
	
	/**
	 * Set if the product could be sold
	 * @param canSell if the product could be sold
	 */
	public void setCanSell(boolean canSell) {
		this.canSell = canSell;
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
	
	/**
	 * Get the buy price of the product
	 * @return buy price
	 */
	public double getBuyPrice() {
		return this.buyPrice;
	}
	
	/**
	 * Get the proper buy price of the product (fixed with 2 decimals)
	 * @return buy price
	 */
	public String getBuyPriceProper() {
		return moneyToStringProper(this.buyPrice);
	}
	
	/**
	 * Get the buy price of a stack
	 * @return the buy price of a stack
	 */
	public double getBuyPriceStack() {
		if(sellInStacks())
			return getBuyPrice() * getSellStack();
		return getBuyPrice();
	}
	
	/**
	 * Get the proper buy price of a stack
	 * @return the proper buy price of a stack
	 */
	public String getBuyPriceStackProper() {
		return moneyToStringProper(getBuyPriceStack());
	}
	
	/**
	 * Set the buy price of the product
	 * @param buyPrice buy price
	 */
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	/**
	 * Get the sell price of the product
	 * @return sell price
	 */
	public double getSellPrice() {
		return this.sellPrice;
	}
	
	/**
	 * Get the proper sell price of the product (fixed with two decimals)
	 * @return sell price
	 */
	public String getSellPriceProper() {
		return moneyToStringProper(this.sellPrice);
	}
	
	/**
	 * Set the sell price of the product
	 * @param sellPrice sell price
	 */
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	/**
	 * Get the stack amount of the product that will be bought
	 * @return buy stack amount
	 */
	public int getBuyStack() {
		return this.buyStack;
	}
	
	/**
	 * Check if the product is bought in stacks
	 * @return true if bought in stacks
	 */
	public boolean buyInStacks() {
		if(this.buyStack > 1)
			return true;
		return false;
	}
	
	/**
	 * Set the stack amount of the product that will be bought
	 * @param quantity buy stack amount
	 */
	public void setBuyStack(int quantity) {
		this.buyStack = quantity;
	}
	
	/**
	 * Get the minimum quantity of products that could be bought
	 * @return minimum buy quantity
	 */
	public int getMinBuyQuantity() {
		return this.minBuyQuantity;
	}
	
	/**
	 * Set the minimum quantity of the products that could be bought
	 * @param quantity minimum buy quantity
	 */
	public void setMinBuyQuantity(int quantity) {
		this.minBuyQuantity = quantity;
	}
	
	/**
	 * Get the maximum quantity of products that could be bought
	 * @return maximum buy quantity
	 */
	public int getMaxBuyQuantity() {
		return this.maxBuyQuantity;
	}
	
	/**
	 * Set the maximum amount of products that could be bought
	 * @param amount maximum buy amount
	 */
	public void setMaxBuyQuantity(int quantity) {
		this.maxBuyQuantity = quantity;
	}
	
	/**
	 * Get the stack amount of the product that will be sold
	 * @return sell stack amount
	 */
	public int getSellStack() {
		return this.sellStack;
	}
	
	/**
	 * Check if the product is sold in stacks
	 * @return true if sold in stacks
	 */
	public boolean sellInStacks() {
		return (this.sellStack > 1);
	}
	
	/**
	 * Set the stack amount of the product that will be sold
	 * @param amount sell stack amount
	 */
	public void setSellStack(int amount) {
		this.sellStack = amount;
	}
	
	/**
	 * Get the minimum quantity of products that will be sold
	 * @return minimum sell amount
	 */
	public int getMinSellQuantity() {
		return this.minSellQuantity;
	}
	
	/**
	 * Set the minimum amount of products that will be sold
	 * @param amount minimum sell amount
	 */
	public void setMinSellQuantity(int quantity) {
		this.minSellQuantity = quantity;
	}
	
	/**
	 * Get the maximum amount of products that will be sold
	 * @return maximum sell amount
	 */
	public int getMaxSellQuantity() {
		return this.maxSellQuantity;
	}
	
	/**
	 * Set the maximum amount of products that will be sold
	 * @param amount maximum sell amount
	 */
	public void setMaxSellQuantity(int quantity) {
		this.maxSellQuantity = quantity;
	}
	
	/**
	 * Is instant buy enabled
	 * @return true if enabled
	 */
	public boolean instantBuyEnabled() {
		return this.instantBuy;
	}
	
	/**
	 * Set if instant buy is enabled
	 * @param enabled if enabled
	 */
	public void instantBuyEnabled(boolean enabled) {
		this.instantBuy = enabled;
	}
	
	/**
	 * Get the instant buy amount
	 * @return instant buy amount
	 */
	public int getInstantBuyQuantity() {
		return this.instantBuyQuantity;
	}
	
	/**
	 * Set the instant buy amount
	 * @param amount instant buy amount
	 */
	public void setInstantBuyQuantity(int quantity) {
		this.instantBuyQuantity = quantity;
	}
	
	/**
	 * If instant sell is enabled
	 * @return true if enabled
	 */
	public boolean instantSellEnabled() {
		return this.instantSell;
	}
	
	/**
	 * Set if instant sell is enabled
	 * @param enabled is enabled
	 */
	public void instantSellEnabled(boolean enabled) {
		this.instantSell = enabled;
	}
	
	/**
	 * Get the instant sell amount
	 * @return instant sell amount
	 */
	public int getInstantSellQuantity() {
		return this.instantSellQuantity;
	}
	
	/**
	 * Set the instant sell amount
	 * @param amount instant sell amount
	 */
	public void setInstantSellQuantity(int quantity) {
		this.instantSellQuantity = quantity;
	}
	
	/**
	 * Get the product buy discount
	 * @return buy discount
	 */
	public double getBuyDiscount() {
		return this.buyDiscount;
	}
	
	/**
	 * Set the product buy discount
	 * @param discount buy discount
	 */
	public void setBuyDiscount(double discount) {
		this.buyDiscount = discount;
	}
	
	/**
	 * Get the product minimum amount buy discount
	 * @return minimum amount buy discount
	 */
	public int getBuyDiscountMinQuantity() {
		return this.buyDiscountMinQuantity;
	}
	
	/**
	 * Set the product minimum amount buy discount
	 * @param amount minimum amount buy discount
	 */
	public void setBuyDiscountMinQuantity(int quantity) {
		this.buyDiscountMinQuantity = quantity;
	}
	
	/**
	 * Get the product sell fee
	 * @return sell fee
	 */
	public double getSellFee() {
		return this.sellFee;
	}
	
	/**
	 * set the product sell Fee
	 * @param fee sell fee
	 */
	public void setSellFee(double fee) {
		this.sellFee = fee;
	}
	
	/**
	 * Get the product minimum amount sell fee
	 * @return minimum amount sell fee
	 */
	public int getSellFeeMinQuantity() {
		return this.sellFeeMinQuantity;
	}
	
	/**
	 * Set the product minimum quantity sell fee
	 * @param amount minimum quantity sell fee
	 */
	public void setSellFeeMinQuantity(int amount) {
		this.sellFeeMinQuantity = amount;
	}
	
	/**
	 * Get the current permission node of the shop
	 * @return permissions node
	 */
	public String getPermissionNode() {
		return this.permsNode;
	}
	
	/**
	 * Check if the shop uses a permission node
	 * @return true if used
	 */
	public boolean usesPermissionNode() {
		return !this.permsNode.equals("");
	}
	
	/**
	 * Set the permission node of the shop
	 * @param node permission node
	 */
	public void setPermissionNode(String node) {
		this.permsNode = node;
	}
	
	/**
	 * Reset the permission node of a shop
	 */
	public void resetPermissionNode() {
		this.permsNode = "";
	}
	
	/**
	 * If remove the shop when it's empty is enabled
	 * @return true if enabled
	 */
	public boolean removeShopWhenEmptyEnabled() {
		return this.removeShopWhenEmpty;
	}
	
	/**
	 * Set if the shop should be removed if it's empty
	 * @param enabled if enabled
	 */
	public void removeShopWhenEmptyEnabled(boolean enabled) {
		this.removeShopWhenEmpty = enabled;
	}
	
	/**
	 * Get the spawned shop show item
	 * @return the show item, null if it hasn't been spawned yet
	 */
	public Item getShowItem() {
		return this.shopItem;
	}
	
	/**
	 * Spawn the shop show item
	 * @param s the server
	 * @return returns the spawned item. Returns null when an error occurred or when the chunk isn't loaded yet.
	 */
	public void spawnShowItem(Server s) {
		respawnShowItem(s);
	}
	
	/**
	 * Respawn the shop show item
	 * @param s the server
	 * @return returns the spawned item. Returns null when an error occurred or when the chunk isn't loaded yet.
	 */
	public Item respawnShowItem(Server s) {
		// Remove the show item if it has been spawned already
		removeShowItem();
		
		// Remove all duped show items
		removeDupedShowItems(s);
		
		// The item may not be spawned in a non-loaded chunk
		if(!isShopChunkLoaded(s))
			return null;
		
		// The location has to be set
		if(!isLocationSet())
			return null;
		
		// Define the world
		World w = s.getWorld(this.world);
		
		// The world variable may not be null
		if(w == null)
			return null;
		
		// Define the shop block and the one above
		Block b = w.getBlockAt(this.x, this.y, this.z);
		Block b2 = w.getBlockAt(this.x, this.y + 1, this.z);
		
		// The block variables may not be null
		if(b == null || b2 == null)
			return null;
		
		// Define and create an item stack
		int typeId = getShowItemTypeId();
		byte data = getShowItemData();
		ItemStack is = new ItemStack(typeId, 1, (short) 0, data);
		
		// The item may not be and air item
		if(typeId == 0)
			return null;
		
		// Calculate the item location and spawn the items
		Location spawn = b.getLocation();
		spawn.add(0.5, 0.6, 0.5);
		
		// Spawn the item
		Item i = b.getWorld().dropItem(spawn, is);
		i.setPickupDelay(1000000000);

		// Force the item to move up, to prevent it from glitching out of the block on the wrong side, to get it nicely on the block
		i.setVelocity(new Vector(0, 0.1, 0));
	
		this.shopItem = i;
		
		return i;
	}
	
	/**
	 * Check if the show item is in the valid location
	 * @param s the server
	 * @return true if in valid location. Returns false when no item has been spawned yet, when the shop location hasn't been setup yet or when an error occurred.
	 */
	public boolean isShowItemInValidLocation(Server s) {
		// The shop item has to be spawned
		if(!isShowItemSpawned())
			return false;
		
		// Define the world
		World w = s.getWorld(this.world);
		
		// The world variable may not be null
		if(w == null)
			return false;
		
		// Define the shop block and the one above
		Block b = w.getBlockAt(this.x, this.y, this.z);
		Block b2 = w.getBlockAt(this.x, this.y + 1, this.z);
		
		// The block variables may not be null
		if(b == null || b2 == null)
			return false;
		
		// Put the shop item into a variable
		Item i = this.shopItem;
		
		// The item must be inside or above the shop block
		return ((b.getLocation().getBlockX() == i.getLocation().getBlockX() && 
				b.getLocation().getBlockY() == i.getLocation().getBlockY() && 
				b.getLocation().getBlockZ() == i.getLocation().getBlockZ()) ||
				(b2.getLocation().getBlockX() == i.getLocation().getBlockX() && 
				b2.getLocation().getBlockY() == i.getLocation().getBlockY() && 
				b2.getLocation().getBlockZ() == i.getLocation().getBlockZ()));
	}
	
	/**
	 * Check if any show item has been spawned yet
	 * @return true if spawned
	 */
	public boolean isShowItemSpawned() {
		if(this.shopItem == null)
			return false;
		return true;
	}
	
	/**
	 * Check if the shop show item should be spawned at the moment or not
	 * @param s the server
	 * @return true if should be spawned
	 */
	public boolean shouldShowItemBeSpawned(Server s) {
		if(!showItem())
			return false;
		
		if(!isLocationSet())
			return false;
		
		if(getShowItemTypeId() == 0)
			return false;
		
		if(!isShopChunkLoaded(s))
			return false;
		
		return true;
	}
	
	/**
	 * Check if a show item is the show item of this shop
	 * @param i the item
	 * @return true if is show item. False if it isn't spawned yet
	 */
	public boolean isShowItem(Item i) {
		// Check if the show item is spawned yet
		if(!isShowItemSpawned())
			return false;
		
		// Is it the same item
		return getShowItem().equals(i);
	}
	
	/**
	 * Check if the shop item is visible. This will basically check if the item is spawned already, if it's died and if it's valid
	 * @return true when visible
	 */
	public boolean isShowItemVisible() {
		// Check if any show item is spawned
		if(!isShowItemSpawned())
			return false;
		
		// Check if the item 'died'
		if(this.shopItem.isDead())
			return false;
		
		// Check if the item is valid
		if(!this.shopItem.isValid())
			return false;
		
		return true;
	}
	
	/**
	 * Remove a spawned shop item from the block
	 */
	public void removeShowItem() {
		// Don't destroy the item if it isn't even created
		if(!isShowItemSpawned())
			return;
		
		// Remove the shop item
		this.shopItem.remove();
	}
	
	/**
	 * Remove all duped shop items
	 * @param s the server
	 * @return amount of duped items remove. Returns -1 when an error occurred.
	 */
	public int removeDupedShowItems(Server s) {
		// Check if the location is set
		if(!isLocationSet())
			return -1;
		
		// Define the world
		World w = s.getWorld(this.world);
		
		// The world variable may not be null
		if(w == null)
			return -1;
		
		// Define the shop block and the one above
		Block b = w.getBlockAt(this.x, this.y, this.z);
		Block b2 = w.getBlockAt(this.x, this.y + 1, this.z);
		
		// The block variables may not be null
		if(b == null || b2 == null)
			return -1;
		
		// Get all entity's of the current chunk the shop block is in
		Entity[] entities = b.getChunk().getEntities();
		
		// Variable holder, counter for duped items amount
		int dupedItems = 0;
		
		// Check for each entity if it was duped
		for(Entity e : entities) {
			
			// Check if the entity is in the same location as the shop block or just one above
			if(!b.equals(e.getLocation().getBlock()) || !b2.equals(e.getLocation().getBlock())) {
				
				// The entity must be an instance of an Item
				if(e instanceof Item) {
					
					// The entity must be a different item than a current spawned show item
					if(isShowItemSpawned()) {
						if(this.shopItem.equals(e)) {
							continue;
						}
					}
					
					// Try to cast the entity to an item
					Item i;
					ItemStack is;
					try {
						i = (Item) e;
						is = i.getItemStack();
					} catch(ClassCastException ex) {
						// The entity couldn't be casted to an item, continue to the next item
						continue;
					} catch(NullPointerException ex) {
						// The entity couldn't be casted to an item, continue to the next item
						continue;
					}
					
					// The item must have the same type id
					if(is.getTypeId() == this.productItemTypeId) {
						// The item is duped, remove the item
						i.remove();
						
						// Add 1 to the duped items counter
						dupedItems++;
					}
				}
			}
		}
		
		// Return the amount of duped items removed
		return dupedItems;
	}
	
	/**
	 * This will check if all the things which are needed to use the shop are setup
	 * @return true if is set up
	 */
	public boolean isSet() {
		if(this.id < 0)
			return false;
		if(this.name == "")
			return false;
		if(this.ownerType.equals(SSShopOwnerType.UNKNOWN))
			return false;
		if(!isLocationSet()) 
			return false;
		if(this.world == "")
			return false;
		if(this.productType.equals(SSShopProductType.UNKNOWN))
			return false;
		if(this.showItemTypeId < 0)
			return false;
		return true;
	}
	
	/**
	 * Check if the chunk where the shop is placed in is loaded.
	 * @param s the server
	 * @return true if chunk is loaded. Returns false if the location is not set or is not valid
	 */
	public boolean isShopChunkLoaded(Server s) {
		// Check if the location is set
		if(!isLocationSet())
			return false;
		
		// Define the world
		World w = s.getWorld(this.world);
		
		// The world variable may not be null
		if(w == null)
			return false;
		
		// Define the shop block
		Block b = w.getBlockAt(this.x, this.y, this.z);
		
		// The block variable may not be null
		if(b == null)
			return false;
		
		// Get the chunk and check if it's loaded yet
		return b.getChunk().isLoaded();
	}
}
