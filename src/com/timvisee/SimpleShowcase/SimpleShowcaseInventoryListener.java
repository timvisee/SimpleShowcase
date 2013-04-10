package com.timvisee.simpleshowcase;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class SimpleShowcaseInventoryListener implements Listener {
	public static SimpleShowcase plugin;

	public SimpleShowcaseInventoryListener(SimpleShowcase instance) {
		plugin = instance;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		if(plugin.getShopManager().getShopInventoryManager().isPlayerInShopInventory(p)) {
			plugin.getShopManager().getShopInventoryManager().onInventoryClick(e, p);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(e.getPlayer() instanceof Player) {
			Player p = (Player) e.getPlayer();
			Inventory inv = e.getInventory();
			
			if(plugin.getShopManager().getShopInventoryManager().isPlayerInShopInventory(p)) {
				plugin.getShopManager().getShopInventoryManager().closeShopInventory(p);
			}
		}
	}
}
