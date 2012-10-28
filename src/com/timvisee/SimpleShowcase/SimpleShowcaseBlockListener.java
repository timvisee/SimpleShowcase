package com.timvisee.SimpleShowcase;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class SimpleShowcaseBlockListener implements Listener {
	public static SimpleShowcase plugin;

	public SimpleShowcaseBlockListener(SimpleShowcase instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onBlockPistonExtend(BlockPistonExtendEvent e) {
		List<Block> b = e.getBlocks();
		
		// Pistons may not move the shop
		for(Block entry : b) {
			if(plugin.getShopManager().isShop(entry) || plugin.getBoothManager().isBoothAt(entry)) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onBlockPistonRetract(BlockPistonRetractEvent e) {
		Block b = e.getRetractLocation().getBlock();
		
		// Pistons may not move the shop
		if(plugin.getShopManager().isShop(b) || plugin.getBoothManager().isBoothAt(b)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Block b = e.getBlock();
		Player p = e.getPlayer();
		
		// Player owned shop location selection mode - select
		if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION)) {
			
			if(!e.isCancelled()) {
				// Make sure no shop is already placed here
				if(plugin.getShopManager().isShop(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "There already is a shop placed here! Choose another block");
					return;
				}
				
				// Get the selected shop of the player and get it's location
				plugin.getShopManager().getSelectedShop(p.getName()).setLocation(b, plugin.getServer());
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "Your shop has been moved!");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Player owned shop creation wizzard - step 1
		if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_CREATION_WIZZARD) &&
				(plugin.getPlayerModeManager().getPlayerModeStep(p) == 1 || plugin.getPlayerModeManager().getPlayerModeStep(p) == 2)) {
			
			if(!e.isCancelled()) {
				// Make sure no shop is already placed here
				if(plugin.getShopManager().isShop(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "There already is a shop placed here! Choose another block");
					return;
				}
				
				// Get the selected shop of the player and get it's location
				plugin.getShopManager().getSelectedShop(p.getName()).setLocation(b, plugin.getServer());
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "Your shop location has been set!");
				p.sendMessage(ChatColor.YELLOW + "Enter your product sell price into the chat");
				p.sendMessage(ChatColor.YELLOW + "Example: " + ChatColor.GOLD + "1.23" + ChatColor.YELLOW + " will set the price to " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(1.23, true));
				plugin.getPlayerModeManager().setPlayerModeStep(p, 3);
				return;
			}
		}
		
		// Players may not place a block where a shop is placed
		if(plugin.getShopManager().isShop(b)) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.DARK_RED + "You don't have permission to place a block in a shop!");
			
		} else if(plugin.getBoothManager().isBoothAt(b)) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.DARK_RED + "You don't have permission to place a block in a booth!");
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		Player p = e.getPlayer();
		
		// Players may not break shops and booths 
		if(plugin.getShopManager().isShop(b)) {
			
			SSShop s = plugin.getShopManager().getShopAt(b);
			
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER) &&
					s.getOwnerName().equals(p.getName())) {

				if(s.getStockQuantity() > 0) {
					p.sendMessage(ChatColor.DARK_RED + "Your shop need to be empty before you're able to break it!");
					e.setCancelled(true);
					return;
				} else {
					s.removeShowItem();
					plugin.getShopManager().removeShop(s);
					p.sendMessage(ChatColor.DARK_RED + "You removed your shop!");
					return;
				}
				
			} else {
				p.sendMessage(ChatColor.DARK_RED + "You don't have permission to break this shop!");
				e.setCancelled(true);
				return;
			}
			
		} else if(plugin.getBoothManager().isBoothAt(b)) {
			p.sendMessage(ChatColor.DARK_RED + "You don't have permission to break this booth!");
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent e) {
		Block b = e.getBlock();
		
		if(plugin.getShopManager().isShop(b) || plugin.getBoothManager().isBoothAt(b)) {
			e.setCancelled(true);
		}
	}
}
