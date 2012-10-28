package com.timvisee.SimpleShowcase;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SSShopInventoryManager {
	public static SimpleShowcase plugin;
	
	List<SSShopInventory> invs = new ArrayList<SSShopInventory>();
	
	public SSShopInventoryManager(SimpleShowcase instance) {
		plugin = instance;
	}
	
	public void openShopInventory(SSShop s, Player p) {
		if(!isPlayerInShopInventory(p)) {
			SSShopInventory si = new SSShopInventory(s, p, plugin);
			this.invs.add(si);
			si.openShopInventory();
		}
	}
	
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		for(SSShopInventory entry : this.invs) {
			if(entry.isPlayer(p)) {
				entry.onInventoryClick(e);
				return;
			}
		}
	}
	
	public void closeShopInventory(Player p) {
		for(SSShopInventory entry : this.invs) {
			if(entry.isPlayer(p)) {
				entry.closeShopInventory();
				this.invs.remove(entry);
				return;
			}
		}
	}
	
	public void closeAllShopInventories() {
		for(SSShopInventory entry : this.invs) {
			entry.closeShopInventory();
		}
	}
	
	public void updateShopInventory(SSShop s) {
		for(SSShopInventory entry : this.invs) {
			if(entry.isShop(s))
				entry.updateInventory();
		}
	}
	
	public boolean isPlayerInShopInventory(Player p) {
		for(SSShopInventory entry : this.invs) {
			if(entry.isPlayer(p))
				return true;
		}
		return false;
	}
	
	public boolean isShopInventoryOpened(SSShop s) {
		for(SSShopInventory entry : this.invs) {
			if(entry.isShop(s))
				return true;
		}
		return false;
	}
}
