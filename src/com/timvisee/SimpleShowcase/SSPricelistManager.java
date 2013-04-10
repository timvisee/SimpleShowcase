package com.timvisee.simpleshowcase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SSPricelistManager {
	
public static SimpleShowcase plugin;
	
	List<SSPricelistItem> items = new ArrayList<SSPricelistItem>();

	public SSPricelistManager(SimpleShowcase instance) {
		plugin = instance;
	}
	
	public void reloadList() {
		
		long t = System.currentTimeMillis();
		
		File pricelistFolder = new File(plugin.getDataFolder(), "pricelist");
		
		// If the pricelist folder doesn't exists, create one
		if(!pricelistFolder.exists())
			pricelistFolder.mkdirs();
		
		List<File> priceListFiles = Arrays.asList(pricelistFolder.listFiles());
		List<SSPricelistItem> pricelistItems = new ArrayList<SSPricelistItem>();
		
		System.out.println("[SimpleShowcase] Loading pricelists...");
		
		for(File entry : priceListFiles) {
			long t2 = System.currentTimeMillis();
			
			// The file may not be null
			if(entry == null)
				continue;
			
			// The file must exists
			if(!entry.exists())
				continue;
			
			// Load the config
			FileConfiguration c;
		    c = YamlConfiguration.loadConfiguration(entry);
		    
		    // Pricelist item count from this file
		    int count = 0;
		    
		    // The config may not be null
		    if(c == null)
		    	continue;
			
			// Get all the pricelist items from the config file
			Set<String> keys = c.getConfigurationSection("products").getKeys(false);
			
			// The set may not be null
			if(keys == null)
				continue;
			
			for(String s : keys) {
				String priceListName = c.getString("priceListName", "");;
				String itemName = s;
				String productType = c.getString("products." + s + ".product.type", "UNKNOWN");
				String productAPIName = c.getString("products." + s + ".product.apiName", "");
				int productId = c.getInt("products." + s + ".product.id", 0);
				String productName = c.getString("products." + s + ".product.name", "");
				int productTypeId = c.getInt("products." + s + ".product.typeId", 0);
				byte productDataValue = (byte) c.getInt("products." + s + ".product.dataValue", 0);
				double buyPrice = c.getDouble("products." + s + ".buyPrice", -1); 
				double sellPrice = c.getDouble("products." + s + ".sellPrice", -1);
				
				SSPricelistItem i = new SSPricelistItem(priceListName, itemName,
						productType, productAPIName, productId, productName, productTypeId, productDataValue,
						buyPrice, sellPrice);
				
				pricelistItems.add(i);
				count++;
			}
			
			// Calculate the loading duration
			long duration2 = System.currentTimeMillis() - t2;
			
			if(count != 1)
				System.out.println("[SimpleShowcase] " + String.valueOf(count) + " pricelist items loaded from '" + entry.getName() + "', took " + String.valueOf(duration2) + "ms!");
		}

		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		if(pricelistItems.size() != 1)
			System.out.println("[SimpleShowcase] " + String.valueOf(pricelistItems.size()) + " pricelist items loaded, took " + String.valueOf(duration) + "ms!");
		else
			System.out.println("[SimpleShowcase] 1 pricelist item loaded, took " + String.valueOf(duration) + "ms!");
		
		if(pricelistItems != null)
			this.items = pricelistItems;
	}
	
	public void applyItemsToShops() {
		// Make sure there are any pricelist items to apply before running the function
		if(countPricelistItems() == 0) {
			System.out.println("[SimpleShowcase] No pricelist items available to apply!");
			return;
		}
		
		System.out.println("[SimpleShowcase] Applying pricelists to shops!");

		long t = System.currentTimeMillis();
		
		for(SSShop s : plugin.getShopManager().getShops()) {
			if(s.usesPricelist()) {
				if(isPricelistItemWithName(s.getPricelistName(), s.getPricelistItemName())) {
					// Get the pricelist item
					SSPricelistItem i = getItem(s.getPricelistName(), s.getPricelistItemName());
					
					// The pricelist item may not be null
					if(i == null)
						continue;
					
					// Apply the pricelist item to the shop
					applyItemToShop(i, s);
				}
			}
		}
		
		// Calculate the applying duration
		long duration = System.currentTimeMillis() - t;
		
		System.out.println("[SimpleShowcase] Pricelists applied, took " + String.valueOf(duration) + "ms!");
	}
	
	public void applyItemToShop(SSPricelistItem i, SSShop s) {
		
		// The pricelist item may not be null
		if(i == null)
			return;
		
		// Set the shop product settings
		s.setProductType(SSShopProductType.valueOf(i.getProductType()));
		s.setProductApiPlugin(i.getProductAPIName());
		s.setProductId(i.getProductId());
		s.setProductName(i.getProductName());
		s.setProductItemTypeId(i.getProductTypeId(), plugin.getServer());
		s.setProductItemData(i.getProductDataValue(), plugin.getServer());
		
		// Set the shop prices
		if(i.isBuyPriceSet())
			s.setBuyPrice(i.getBuyPrice());
		if(i.isSellPriceSet())
			s.setSellPrice(i.getSellPrice());
	}
	
	public void addPricelistItem(SSPricelistItem i) {
		this.items.add(i);
	}
	
	public List<SSPricelistItem> getPriceList() {
		return this.items;
	}
	
	public boolean isPricelistWithName(String name) {
		for(SSPricelistItem entry : this.items) {
			if(entry.getPricelistName().equals(name))
				return true;
		}
		return false;
	}
	
	public boolean isPricelistItemWithName(String name) {
		for(SSPricelistItem entry : this.items) {
			if(entry.getItemName().equals(name))
				return true;
		}
		return false;
	}
	
	public boolean isPricelistItemWithName(String priceListName, String name) {
		for(SSPricelistItem entry : this.items) {
			if(entry.getPricelistName().equals(priceListName))
				if(entry.getItemName().equals(name))
					return true;
		}
		return false;
	}
	
	public int countItemsInPriceList(String priceListName) {
		int a = 0;
		for(SSPricelistItem entry : this.items) {
			if(entry.getPricelistName().equals(priceListName))
				a++;
		}
		return a;
	}
	
	public int countItemsWithName(String itemName) {
		int a = 0;
		for(SSPricelistItem entry : this.items) {
			if(entry.getItemName().equals(itemName))
				a++;
		}
		return a;
	}
	
	public int countItemsWithName(String priceListName, String itemName) {
		int a = 0;
		for(SSPricelistItem entry : this.items) {
			if(entry.getPricelistName().equals(priceListName))
				if(entry.getItemName().equals(itemName))
					a++;
		}
		return a;
	}
	
	public SSPricelistItem getItemQuery(String query) {
		List<String> args = Arrays.asList(query.split(":"));
		
		if(args.size() == 0)
			return null;
		else if(args.size() == 1) {
			int itemCount = countItemsWithName(query);
			if(itemCount == 0)
				return null;
			else if(itemCount == 1)
				return getItem(query);
			else
				return null;
		} else {
			if(isPricelistWithName(args.get(0)))
				return null;
			else {
				if(isPricelistItemWithName(args.get(0), args.get(1)))
					return getItem(args.get(0), args.get(1));
				else
					return null;
			}
		}
	}
	
	public SSPricelistItem getItem(String name) {
		for(SSPricelistItem entry : this.items) {
			if(entry.getItemName().equals(name))
				return entry;
		}
		return null;
	}
	
	public List<SSPricelistItem> getItems(String name) {
		List<SSPricelistItem> items = new ArrayList<SSPricelistItem>();
		for(SSPricelistItem entry : this.items) {
			if(entry.getItemName().equals(name))
				items.add(entry);
		}
		return items;
	}
	
	public SSPricelistItem getItem(String priceListName, String name) {
		for(SSPricelistItem entry : this.items) {
			if(entry.getPricelistName().equals(priceListName))
				if(entry.getItemName().equals(name))
					return entry;
		}
		return null;
	}
	
	public int countPricelistItems() {
		return this.items.size();
	}
}
