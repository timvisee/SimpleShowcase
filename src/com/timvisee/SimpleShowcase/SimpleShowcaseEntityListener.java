package com.timvisee.SimpleShowcase;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class SimpleShowcaseEntityListener implements Listener {
	public static SimpleShowcase plugin;

	public SimpleShowcaseEntityListener(SimpleShowcase instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent e) {
		
		if(e.getEntity() instanceof Item) {
			Item i = (Item) e.getEntity();
			
			if(plugin.getShopManager().isShopShowItem(i)) {
				// Cancel the item from burning (this doesn't work currently, it might later on)
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		List<Block> b = e.blockList();
		
		// TNT may not break the shops
		for(Block entry : b) {
			if(plugin.getShopManager().isShop(entry) || plugin.getBoothManager().isBoothAt(entry)) {
				e.setCancelled(true);
				return;
			}
		}
	}
}
