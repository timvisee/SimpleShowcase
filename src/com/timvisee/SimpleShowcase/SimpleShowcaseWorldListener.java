package com.timvisee.simpleshowcase;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class SimpleShowcaseWorldListener implements Listener {
	public static SimpleShowcase plugin;

	public SimpleShowcaseWorldListener(SimpleShowcase instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onWorldLoad(WorldLoadEvent e) {
		String w = e.getWorld().getName();

		// Remove all duped items in the new loaded world
		plugin.getShopManager().removeAllDupedShopShowItemsInWorld(w);
		
		// Spawn all shop show items in the new loaded world
		plugin.getShopManager().respawnAllShopShowItemsInWorld(w);
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		Chunk c = e.getChunk();
		
		// Remove all dumped items in the new loaded chunk
		plugin.getShopManager().removeAllDupedShopShowItemsInChunk(c);
			
		// Spawn all shop show items in a new loaded chunk
		plugin.getShopManager().respawnAllShopShowItemsInChunk(c);
	}
	
	@EventHandler
	public void onWorldUnload(WorldUnloadEvent e) {
		String w = e.getWorld().getName();

		// Remove all shop show items in the new loaded world
		plugin.getShopManager().removeAllShopShowItemsInWorld(w);
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e) {
		Chunk c = e.getChunk();

		// Remove all shop show items in a new loaded chunk
		plugin.getShopManager().removeAllDupedShopShowItemsInChunk(c);
	}
}
