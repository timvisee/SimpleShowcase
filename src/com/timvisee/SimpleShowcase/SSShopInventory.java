package com.timvisee.SimpleShowcase;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SSShopInventory {
	public static SimpleShowcase plugin;
	
	private SSShop s;
	private Player p;
	private Block caseBlock;
	private BlockState caseState;

	public SSShopInventory(SSShop s, Player p, SimpleShowcase instance) {
		this.s = s;
		this.p = p;
		plugin = instance;
	}
	
	public boolean isPlayer(Player p) {
		return this.p.equals(p);
	}
	
	public boolean isShop(SSShop s) {
		return this.s.equals(s);
	}
	
	public void openShopInventory() {
		// Remove the show item temprary
		if(s.isShowItemSpawned())
			s.removeShowItem();
		
		// Save the block state first
		this.caseBlock = s.getCaseBlock(plugin.getServer());
		this.caseState = this.caseBlock.getState();
		
		this.caseBlock.setType(Material.AIR);
		this.caseBlock.setType(Material.CHEST);
		
		Chest c = (Chest) this.caseBlock.getState();
		c.getBlockInventory().clear();
		
		// Add the stock items
		if(s.getStockQuantity() != 0 && s.getStockTypeId() != 0)
			addItemsToInventory(c.getBlockInventory(), s.getStockTypeId(), s.getStockDataValue(), s.getStockQuantity());
		
		this.p.openInventory(c.getBlockInventory());
		
		// Send fake block update to players to hide the chest
		for(Player entry : plugin.getServer().getOnlinePlayers()) {
			p.sendBlockChange(this.s.getLocation(plugin.getServer()), this.caseState.getTypeId(), this.caseState.getRawData());
		}
	}
	
	public void closeShopInventory() {
		this.p.closeInventory();
		
		Chest c = (Chest) this.caseBlock.getState();
		
		Inventory inv = c.getInventory();
		
		removeOtherItems(inv);
		
		if(getInventoryItemCount(inv) == 0) {
			// Handle an empty inventory
			s.setStockTypeId(0);
			s.setStockDataValue((byte) 0);
			s.setStockQuantity(0);
		} else {
			s.setStockTypeId(getFirstInventoryItemStack(inv).getTypeId());
			s.setStockDataValue(getFirstInventoryItemStack(inv).getData().getData());
			s.setStockQuantity(getInventoryItemCount(inv));
		}
		
		c.getBlockInventory().clear();
		this.caseBlock.setTypeId(this.caseState.getTypeId());
		this.caseBlock.setData(this.caseState.getRawData());
		
		// Respawn the show item
		if(s.shouldShowItemBeSpawned(plugin.getServer())) {
			System.out.println("respawn");
			s.respawnShowItem(plugin.getServer());
		}
	}
	
	public void onInventoryClick(InventoryClickEvent e) {
		Chest c = (Chest) this.caseBlock.getState();
		
		Inventory inv = e.getInventory();
		
		removeOtherItems(inv);
		
		if(getInventoryItemCount(inv) == 0) {
			// Handle an empty inventory
			s.setStockTypeId(0);
			s.setStockDataValue((byte) 0);
			s.setStockQuantity(0);
			return;
		} else {
			s.setStockTypeId(getFirstInventoryItemStack(inv).getTypeId());
			s.setStockDataValue(getFirstInventoryItemStack(inv).getData().getData());
			s.setStockQuantity(getInventoryItemCount(inv));
		}
	}
	
	public void updateInventory() {
		Chest c = (Chest) this.caseBlock.getState();
		
		if(this.s.getStockQuantity() == 0 || this.s.getStockTypeId() <= 0)
			c.getInventory().clear();
		else {
			c.getInventory().clear();
			addItemsToInventory(c.getInventory(), s.getStockTypeId(), s.getStockDataValue(), s.getStockQuantity());
		}
	}
	
	public void removeOtherItems(Inventory inv) {
		int stacksRejected = 0;
		
		if(s.getStockTypeId() != 0) {
			for(int i = 0; i < inv.getSize(); i++) {
				if(inv.getItem(i) == null)
					continue;
				
				if(inv.getItem(i).getTypeId() == 0)
					continue;
				
				if(inv.getItem(i).getTypeId() != s.getStockTypeId() ||
						inv.getItem(i).getData().getData() != s.getStockDataValue()) {
					
					int space = getInventoryItemSpace(this.p.getInventory(), inv.getItem(i).getTypeId(), inv.getItem(i).getData().getData());
					
					stacksRejected++;
					
					if(space >= inv.getItem(i).getAmount()) {
						this.p.getInventory().addItem(inv.getItem(i));
						inv.setItem(i, null);
					} else if(space >= inv.getItem(i).getAmount()) {
						int addAmount = inv.getItem(i).getAmount() - space;
						int dropAmount = inv.getItem(i).getAmount() - addAmount;
						ItemStack newItemStack = new ItemStack(inv.getItem(i));
						newItemStack.setAmount(addAmount);
						ItemStack dropItemStack = new ItemStack(inv.getItem(i));
						dropItemStack.setAmount(dropAmount);
						this.p.getInventory().addItem(newItemStack);
						this.p.getWorld().dropItem(this.p.getLocation(), dropItemStack);
						this.p.updateInventory();
						inv.setItem(i, null);
					}
				}
			}
		}
		
		if(stacksRejected > 0)
			this.p.sendMessage(ChatColor.DARK_RED + "Your shop may only contain one type of products!");
	}
	
	public ItemStack getFirstInventoryItemStack(Inventory inv) {
		for(int i = 0; i < inv.getSize(); i++) {
			if(inv.getItem(i) == null)
				continue;
			
			if(inv.getItem(i).getTypeId() == 0)
				continue;
			return inv.getItem(i);
		}
		return null;
	}
	
	public boolean doesInventoryContainOtherItems(Inventory inv, int typeId, byte dataValue) {
		for(int i = 0; i < inv.getSize(); i++) {
			if(inv.getItem(i) == null)
				continue;
			
			if(inv.getItem(i).getTypeId() == 0)
				continue;
			
			if(inv.getItem(i).getTypeId() != typeId)
				return true;
			
			if(inv.getItem(i).getData().getData() != dataValue)
				return true;
		}
		return false;
	}
	
	/**
	 * Get the maximum stack size of an item
	 * @param itemTypeId the type id
	 * @return the maximum stack size
	 */
	public int getItemMaxStackSize(int itemTypeId) {
		ItemStack is = new ItemStack(itemTypeId, 1);
		return is.getMaxStackSize();
	}
	
	public int getInventoryItemSpace(Inventory inv, int itemTypeId, byte itemData) {
		
		int freeSpace = 0;
		
		for(int i = 0; i < inv.getSize(); i++) {
			ItemStack stack = inv.getItem(i);
			
			// The Item Stack may not be null
			if(stack == null) {
				freeSpace += getItemMaxStackSize(itemTypeId);
				continue;
			}
			
			// If the stack contains zero items, continue
			if(stack.getAmount() == 0) {
				// There is place for a full stack of the item
				freeSpace += getItemMaxStackSize(itemTypeId);
				continue;
			}
			
			// The stack must contain the same items
			if(stack.getTypeId() == itemTypeId && stack.getData().getData() == itemData) {
				// Calculate the space left and add it to the variable
				int a = getItemMaxStackSize(itemTypeId) - stack.getAmount();
				freeSpace += a;
			}
		}
		
		if (freeSpace < 0)
			freeSpace = 0;
		return freeSpace;
	}
	
	public int getInventoryItemCount(Inventory inv){
		int total = 0;
		for(int i = 0; i<inv.getSize(); i++){
			ItemStack stack = inv.getItem(i);
			if(stack!=null) {
				total+=stack.getAmount();
			}
		}
		return total;
	}
	
	public int getInventoryItemCount(Inventory inv, int itemTypeId, byte itemData){
		int total = 0;
		for(int i = 0; i<inv.getSize(); i++){
			ItemStack stack = inv.getItem(i);
			if((stack!=null) && (stack.getTypeId() == itemTypeId)&& (stack.getData().getData() == itemData)) {
				total+=stack.getAmount();
			}
		}
		return total;
	}

	public void removeItemsFromInventory(Inventory inv, int itemTypeId, byte itemData, int a) {
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) != null) {
				ItemStack stack = inv.getItem(i).clone();
				if(stack.getTypeId() == itemTypeId && stack.getData().getData() == itemData){
					if(stack.getAmount() > a){
						stack.setAmount(stack.getAmount() - a);
						inv.setItem(i, stack);
						return;
					} else {
						a -= stack.getAmount();
						inv.setItem(i, null);
					}
				}
			}
		}
	}
	
	public int addItemsToInventory(Inventory inv, int typeId, byte data, int a) {
		while (a > 0){
			ItemStack stack = new ItemStack(typeId, 1, (short) 0, data);
			int max = getItemMaxStackSize(typeId);
			
			if(a >= max && max != -1)
				stack.setAmount(max);
			else
				stack.setAmount(a);
			
			a -= stack.getAmount();
			
			Map<Integer, ItemStack> notFitting = inv.addItem(stack);
			if(notFitting.size()>0){
				a+=notFitting.get(0).getAmount();
				break;
			}
		}
		
		// Return the amount of items that did not fit
		return a;
	}
}
