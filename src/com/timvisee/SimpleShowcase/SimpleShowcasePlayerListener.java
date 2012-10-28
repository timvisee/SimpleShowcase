package com.timvisee.SimpleShowcase;

import java.text.DecimalFormat;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SimpleShowcasePlayerListener implements Listener {
	public static SimpleShowcase plugin;

	public SimpleShowcasePlayerListener(SimpleShowcase instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();
		Player p = e.getPlayer();

		// Shop selection mode - select
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Make sure the block is a shop
				if(!plugin.getShopManager().isShop(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This isn't a shop!");
					return;
				}
				
				// Set the selected shop of a player
				plugin.getShopManager().selectShop(p.getName(), plugin.getShopManager().getShopAt(b));
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "The shop " + ChatColor.GOLD + plugin.getShopManager().getSelectedShop(p.getName()).getName() + ChatColor.GREEN + " has been selected!");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		// Shop selection mode - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Shop location selection mode - select
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_LOCATION_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
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
				p.sendMessage(ChatColor.GREEN + "The shop has been moved!");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		// Shop location selection mode - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_LOCATION_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Shop product selection mode - select
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PRODUCT_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Get the current item in the hand of the player
				ItemStack is = p.getItemInHand();
				
				// Is the item allowed to sell as product
				if(is.getTypeId() == 0) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "You don't have any item in your hand");
					return;
				}
				
				// Get the selected shop and change the product
				plugin.getShopManager().getSelectedShop(p.getName()).setProductType(SSShopProductType.ITEM);
				plugin.getShopManager().getSelectedShop(p.getName()).setProductItemTypeId(is.getTypeId(), plugin.getServer());
				plugin.getShopManager().getSelectedShop(p.getName()).setProductItemData(is.getData().getData(), plugin.getServer());
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "The shop product has been modified!");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		// Shop product selection mode - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PRODUCT_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Shop show item selection mode - select
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SHOW_ITEM_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Get the current item in the hand of the player
				ItemStack is = p.getItemInHand();
				
				// Is the item allowed to sell as product
				if(is.getTypeId() == 0) {
					
					// Disable the show item
					plugin.getShopManager().getSelectedShop(p.getName()).showItem(false);
					
					// Remove the show item if it's spawned
					if(plugin.getShopManager().getSelectedShop(p.getName()).isShowItemSpawned())
						plugin.getShopManager().getSelectedShop(p.getName()).removeShowItem();
					
					p.sendMessage("");
					p.sendMessage(ChatColor.YELLOW + "The shop show item has been " + ChatColor.DARK_RED + "Disabled");
					plugin.getPlayerModeManager().resetPlayerMode(p, true);
					return;
					
				} else {
					
					p.sendMessage("");
				
					// Enable the show item if it wasn't enabled yet
					if(!plugin.getShopManager().getSelectedShop(p.getName()).showItem()) {
						plugin.getShopManager().getSelectedShop(p.getName()).showItem(true);
						p.sendMessage(ChatColor.YELLOW + "The shop show item has been " + ChatColor.GREEN + "Enabled");
					}
					
					// Get the selected shop and change the product
					plugin.getShopManager().getSelectedShop(p.getName()).setShowItemTypeId(is.getTypeId());
					plugin.getShopManager().getSelectedShop(p.getName()).setShowItemData(is.getData().getData());
					
					// Remove the show item if it's spawned
					if(plugin.getShopManager().getSelectedShop(p.getName()).isShowItemSpawned())
						plugin.getShopManager().getSelectedShop(p.getName()).removeShowItem();
					
					// Check if the show item should be spawned, if so spawn the new item
					if(plugin.getShopManager().getSelectedShop(p.getName()).shouldShowItemBeSpawned(plugin.getServer()))
						plugin.getShopManager().getSelectedShop(p.getName()).spawnShowItem(plugin.getServer());
					
					// Show an message and disable the current player mode
					p.sendMessage(ChatColor.GREEN + "The shop product has been modified!");
					plugin.getPlayerModeManager().resetPlayerMode(p, true); 
					return;
				}
			}
		}
		// Shop show item selection mode - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PRODUCT_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Player owned shop creation wizard - step 1
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_CREATION_WIZZARD) &&
					plugin.getPlayerModeManager().getPlayerModeStep(p) == 1) {
				// Cancel the event first
				e.setCancelled(true);
				
				if(!plugin.getBoothManager().isBoothAt(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This isn't a booth!");
					return;
				}
				
				// Make sure the booth base is selected, not the case
				Block boothBase;
				if(plugin.getBoothManager().isBoothBaseAt(b)) {
					boothBase = b;
				} else {
					boothBase = b.getWorld().getBlockAt(b.getX(), b.getY() - 1, b.getZ());
				}
				
				SSBooth booth = plugin.getBoothManager().getBoothAt(boothBase);
				
				if(plugin.getBoothManager().isBoothUsed(booth)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This booth is already used!");
					return;
				}
				
				if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.booth.claim", p.isOp())) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return;
				}
				
				// Show a confirmation message
				p.sendMessage("");
				if(booth.getClaimPrice() <= 0.00) {
					p.sendMessage(ChatColor.YELLOW + "This booth is free to use!");
					p.sendMessage(ChatColor.GREEN + "Left click the booth again to move your shop!");
				} else {
					p.sendMessage(ChatColor.YELLOW + "To claim this booth it costs you " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(booth.getClaimPrice(), true) + ChatColor.YELLOW + "!");
					p.sendMessage(ChatColor.GREEN + "Left click the booth again to move your shop!");
				}
				plugin.getBoothManager().setLastUsedBooth(p.getName(), booth);
				plugin.getPlayerModeManager().setPlayerModeStep(p, 2);
				return;
			}
		}
		// Player owned shop creation wizard - step 2
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_CREATION_WIZZARD) &&
					plugin.getPlayerModeManager().getPlayerModeStep(p) == 2) {
				// Cancel the event first
				e.setCancelled(true);
				
				if(!plugin.getBoothManager().isBoothAt(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This isn't a booth!");
					return;
				}
				
				SSBooth booth = plugin.getBoothManager().getBoothAt(b);
				
				// The player need to have a last used booth
				if(!plugin.getBoothManager().hasLastUsedBooth(p.getName())) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "Something went wrong! Please try it again.");
					return;
				}
				
				// The booth has to be the same as the previous one!
				if(!booth.equals(plugin.getBoothManager().getLastUsedBooth(p.getName()))) {
					p.sendMessage("");
					if(booth.getClaimPrice() <= 0.00) {
						p.sendMessage(ChatColor.YELLOW + "This booth is free to use!");
						p.sendMessage(ChatColor.GREEN + "Left click the booth again to move your shop!");
					} else {
						p.sendMessage(ChatColor.YELLOW + "To claim this booth it costs you " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(booth.getClaimPrice(), true) + ChatColor.YELLOW + "!");
						p.sendMessage(ChatColor.GREEN + "Left click the booth again to move your shop!");
					}
					plugin.getBoothManager().setLastUsedBooth(p.getName(), booth);
				}
				
				if(plugin.getBoothManager().isBoothUsed(booth)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This booth is already used!");
					return;
				}
				
				if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.booth.claim", p.isOp())) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return;
				}
				
				// Check if the player has enough money
				if(booth.getClaimPrice() > 0.00) {
					if(!plugin.getEconomyManager().isEnabled()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "No supported economy system found, you are not able to claim this booth!");
						p.sendMessage(ChatColor.DARK_RED + "Please contact a server administrator!");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION, false);
						return;
					}
					if(!plugin.getEconomyManager().hasEnoughMoney(p.getName(), booth.getClaimPrice())) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "You don't have enough money to claim this booth!");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION, false);
						return;
					}
				}
				
				// Charge the player
				double price = booth.getClaimPrice();
				if(price > 0.00) {
					plugin.getEconomyManager().withdrawMoney(p.getName(), price);
					p.sendMessage("");
					p.sendMessage(ChatColor.GREEN + "You've paid " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true) + ChatColor.GREEN + " to claim this booth!");
				}
				
				// Move the shop
				plugin.getShopManager().getSelectedShop(p.getName()).setLocation(booth);
				
				p.sendMessage(ChatColor.GREEN + "Your shop location has been set!");
				p.sendMessage(ChatColor.YELLOW + "Enter your product sell price into the chat");
				p.sendMessage(ChatColor.YELLOW + "Example: " + ChatColor.GOLD + "1.23" + ChatColor.YELLOW + " will set the price to " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(1.23, true));
				plugin.getPlayerModeManager().setPlayerModeStep(p, 3);
				return;
			}
		}
		// Player Owned Shop creation wizard - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_CREATION_WIZZARD)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Player owned shop selection mode - select
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Make sure the block is a shop
				if(!plugin.getShopManager().isShop(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This isn't a shop!");
					return;
				}
				
				// Make sure the player is the owner of the shop
				if(!plugin.getShopManager().getShopAt(b).getOwnerType().equals(SSShopOwnerType.PLAYER) ||
						!plugin.getShopManager().getShopAt(b).getOwnerName().equals(p.getName())) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This isn't your shop!");
					return;
				}
				
				// Set the selected shop of a player
				plugin.getShopManager().selectShop(p.getName(), plugin.getShopManager().getShopAt(b));
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "Your shop " + ChatColor.GOLD + plugin.getShopManager().getSelectedShop(p.getName()).getName() + ChatColor.GREEN + " has been selected!");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		// Shop selection mode - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Shop location selection mode - booth
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				if(!plugin.getBoothManager().isBoothAt(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This isn't a booth!");
					return;
				}
				
				// Make sure the booth base is selected, not the case
				Block boothBase;
				if(plugin.getBoothManager().isBoothBaseAt(b)) {
					boothBase = b;
				} else {
					boothBase = b.getWorld().getBlockAt(b.getX(), b.getY() - 1, b.getZ());
				}
				
				SSBooth booth = plugin.getBoothManager().getBoothAt(boothBase);
				
				if(plugin.getBoothManager().isBoothUsed(booth)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This booth is already used!");
					return;
				}
				
				if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.booth.claim", p.isOp())) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return;
				}
				
				// Show a confirmation message
				p.sendMessage("");
				if(booth.getClaimPrice() <= 0.00) {
					p.sendMessage(ChatColor.YELLOW + "This booth is free to use!");
					p.sendMessage(ChatColor.GREEN + "Left click the booth again to move your shop!");
				} else {
					p.sendMessage(ChatColor.YELLOW + "To claim this booth it costs you " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(booth.getClaimPrice(), true) + ChatColor.YELLOW + "!");
					p.sendMessage(ChatColor.GREEN + "Left click the booth again to move your shop!");
				}
				plugin.getBoothManager().setLastUsedBooth(p.getName(), booth);
				plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION_BOOTH_CONFIRMATION, false);
				return;
				
				/* // Show an message and disable the current player mode
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "Your shop has been moved!");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;*/
			}
		}
		// Shop location selection mode - booth confirmation
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION_BOOTH_CONFIRMATION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				if(!plugin.getBoothManager().isBoothAt(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This isn't a booth!");
					return;
				}
				
				SSBooth booth = plugin.getBoothManager().getBoothAt(b);
				
				// The player need to have a last used booth
				if(!plugin.getBoothManager().hasLastUsedBooth(p.getName())) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "Something went wrong! Please try it again.");
					return;
				}
				
				// The booth has to be the same as the previous one!
				if(!booth.equals(plugin.getBoothManager().getLastUsedBooth(p.getName()))) {
					p.sendMessage("");
					if(booth.getClaimPrice() <= 0.00) {
						p.sendMessage(ChatColor.YELLOW + "This booth is free to use!");
						p.sendMessage(ChatColor.GREEN + "Left click the booth again to move your shop!");
					} else {
						p.sendMessage(ChatColor.YELLOW + "To claim this booth it costs you " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(booth.getClaimPrice(), true) + ChatColor.YELLOW + "!");
						p.sendMessage(ChatColor.GREEN + "Left click the booth again to move your shop!");
					}
					plugin.getBoothManager().setLastUsedBooth(p.getName(), booth);
				}
				
				if(plugin.getBoothManager().isBoothUsed(booth)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This booth is already used!");
					return;
				}
				
				if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.booth.claim", p.isOp())) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return;
				}
				
				// Check if the player has enough money
				if(booth.getClaimPrice() > 0.00) {
					if(!plugin.getEconomyManager().isEnabled()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "No supported economy system found, you are not able to claim this booth!");
						p.sendMessage(ChatColor.DARK_RED + "Please contact a server administrator!");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION, false);
						return;
					}
					if(!plugin.getEconomyManager().hasEnoughMoney(p.getName(), booth.getClaimPrice())) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "You don't have enough money to claim this booth!");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION, false);
						return;
					}
				}
				
				// Charge the player
				double price = booth.getClaimPrice();
				if(price > 0.00) {
					plugin.getEconomyManager().withdrawMoney(p.getName(), price);
					p.sendMessage("");
					p.sendMessage(ChatColor.GREEN + "You've paid " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true) + ChatColor.GREEN + " to claim this booth!");
				}
				
				// Move the shop
				plugin.getShopManager().getSelectedShop(p.getName()).setLocation(booth);
				
				p.sendMessage(ChatColor.GREEN + "Your shop has been moved!");
				
				// Reset the players' mode
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
		}
		// Shop location selection mode - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION) || 
					plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION_BOOTH_CONFIRMATION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Booth selection mode - select
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.BOOTH_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Make sure the block is a shop
				if(!plugin.getBoothManager().isBoothAt(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "This isn't a booth!");
					return;
				}
				
				// Set the selected shop of a player
				plugin.getBoothManager().selectBooth(p.getName(), plugin.getBoothManager().getBoothAt(b));
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "The booth " + ChatColor.GOLD + plugin.getBoothManager().getSelectedBooth(p.getName()).getName() + ChatColor.GREEN + " has been selected!");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		// Booth selection mode - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.BOOTH_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Booth location selection mode - select
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.BOOTH_LOCATION_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Make sure no shop is already placed here
				if(plugin.getBoothManager().isBoothAt(b)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "There already is a booth placed here! Choose another block");
					return;
				}
				
				// Get the selected booth of the player and get it's location
				plugin.getBoothManager().getSelectedBooth(p.getName()).setLocation(b, plugin.getServer());
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				p.sendMessage(ChatColor.GREEN + "The booth has been moved!");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		// Booth location selection mode - cancel
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.BOOTH_LOCATION_SELECTION)) {
				// Cancel the event first
				e.setCancelled(true);
				
				// Show an message and disable the current player mode
				p.sendMessage("");
				plugin.getPlayerModeManager().resetPlayerMode(p, true);
				return;
			}
		}
		
		// Shop buy transactions
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			// Is the shop player owned, if it is cancel the event and show more info about the shop
			if(plugin.getShopManager().isShop(b)) {
				if(plugin.getShopManager().getShopAt(b).getOwnerType().equals(SSShopOwnerType.PLAYER)) {
					SSShop s = plugin.getShopManager().getShopAt(b);
					String ownerName = s.getOwnerName();
					if(p.getName().equals(ownerName)) {
						//e.setCancelled(true); // Don't cancel the event to allow shop breaking
						p.sendMessage("");
						p.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/shop select " + s.getName() + ChatColor.YELLOW + " to select your shop");
						p.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/shop help" + ChatColor.YELLOW + " for a list of commands");
						return;
					}
				}
			}
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_BUY_TRANSACTION)) {
				if(plugin.getShopManager().isShop(b)) {
					if(plugin.getShopManager().hasLastUsedShop(p.getName())) {
						if(plugin.getShopManager().getLastUsedShop(p.getName()).getId() != plugin.getShopManager().getShopAt(b).getId()) {
							// If the player selects another shop make sure he is shown the price and things again
							plugin.getShopManager().forgetLastUsedShop(p.getName());
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
						}
					} else {
						// If the player selects another shop make sure he is shown the price and things again
						plugin.getShopManager().forgetLastUsedShop(p.getName());
						plugin.getPlayerModeManager().resetPlayerMode(p, false);
					}
						
				} else {
					// The player didn't used a shop yet, reset it's mode to fix it
					plugin.getShopManager().forgetLastUsedShop(p.getName());
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
				}
			}
			
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.NONE) ||
					plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SELL_TRANSACTION) ||
					plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SELL_TRANSACTION)) {
				if(plugin.getShopManager().isShop(b)) {
					SSShop s = plugin.getShopManager().getShopAt(b);

					// Cancel the event first
					e.setCancelled(true);
					
					// Check if the shop is enabled
					if(!s.isEnabled()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "This shop is disabled!");
						return;
					}
					
					// Is the shop player owned
					if(!s.getOwnerType().equals(SSShopOwnerType.PLAYER)) {
						
						// Check permissions
						if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.shop.buy")) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
							return;
						}
						
						// Check custom shop permissions
						if(s.usesPermissionNode()) {
							String permsNode = s.getPermissionNode();
							if(!plugin.getPermissionsManager().hasPermission(p, permsNode)) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
								return;
							}
						}
						
						// Check if any items are sold
						if(!s.canBuy()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "This shop doesn't sell any products!");
							return;
						}
						
						if(!s.isProductSet()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "The product of this shop has not been set yet!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
						
						// Set the players last used shop
						plugin.getShopManager().setLastUsedShop(p.getName(), s);
						
						//TODO show buy discount
						
						// Show a message to the user, depending on if instant buy and stacking is enabled
						if(!s.instantBuyEnabled()) {
							if(!s.buyInStacks()) {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "This product cost " + ChatColor.GOLD + s.getBuyPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getBuyPrice()));
								p.sendMessage(ChatColor.YELLOW + "Click the shop again to buy the product");
							} else {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "This product will be sold in stacks of " + ChatColor.GOLD + s.getBuyStack());
								p.sendMessage(ChatColor.YELLOW + "Each stack cost " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(s.getBuyPrice() * s.getBuyStack(), true));
								p.sendMessage(ChatColor.YELLOW + "Click the shop again to buy the product");
							}
						} else {
							if(!s.buyInStacks()) {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "This product cost " + ChatColor.GOLD + s.getBuyPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getBuyPrice()));
								p.sendMessage(ChatColor.YELLOW + "Click the shop again to instantly buy " + ChatColor.GOLD + s.getInstantBuyQuantity() + ChatColor.YELLOW + " products");
							} else {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "This product will be sold in stacks of " + ChatColor.GOLD + s.getBuyStack());
								p.sendMessage(ChatColor.YELLOW + "Each stack cost " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(s.getBuyPrice() * s.getBuyStack(), true));
								p.sendMessage(ChatColor.YELLOW + "Click the shop again to instantly buy " + ChatColor.GOLD + s.getInstantBuyQuantity() + ChatColor.YELLOW + " stacks");
							}
						}
						
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_BUY_TRANSACTION, false);
	
						return;
						
					} else {
						
						// Check permissions
						if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.shop.buy")) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
							return;
						}
						
						// Check custom shop permissions
						if(s.usesPermissionNode()) {
							String permsNode = s.getPermissionNode();
							if(!plugin.getPermissionsManager().hasPermission(p, permsNode)) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
								return;
							}
						}
						
						// Check if any items are sold
						if(!s.canBuy()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "This shop doesn't sell any products!");
							return;
						}
						
						// Are items in stock
						if(s.getStockQuantity() <= 0 || s.getStockTypeId() == 0) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "Not enough items in stock!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
						
						// Set the players last used shop
						plugin.getShopManager().setLastUsedShop(p.getName(), s);
						
						//TODO show buy discount
						
						// Show a message to the user, depending on if instant buy and stacking is enabled
						if(!s.instantBuyEnabled()) {
							if(!s.buyInStacks()) {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "This product cost " + ChatColor.GOLD + s.getBuyPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getBuyPrice()));
								p.sendMessage(ChatColor.YELLOW + "Click the shop again to buy the product");
							} else {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "This product will be sold in stacks of " + ChatColor.GOLD + s.getBuyStack());
								p.sendMessage(ChatColor.YELLOW + "Each stack cost " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(s.getBuyPrice() * s.getBuyStack(), true));
								p.sendMessage(ChatColor.YELLOW + "Click the shop again to buy the product");
							}
						} else {
							if(!s.buyInStacks()) {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "This product cost " + ChatColor.GOLD + s.getBuyPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getBuyPrice()));
								p.sendMessage(ChatColor.YELLOW + "Click the shop again to instantly buy " + ChatColor.GOLD + s.getInstantBuyQuantity() + ChatColor.YELLOW + " products");
							} else {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "This product will be sold in stacks of " + ChatColor.GOLD + s.getBuyStack());
								p.sendMessage(ChatColor.YELLOW + "Each stack cost " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(s.getBuyPrice() * s.getBuyStack(), true));
								p.sendMessage(ChatColor.YELLOW + "Click the shop again to instantly buy " + ChatColor.GOLD + s.getInstantBuyQuantity() + ChatColor.YELLOW + " stacks");
							}
						}
						
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_BUY_TRANSACTION, false);
	
						return;
					}
				}
			}
			
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_BUY_TRANSACTION)) {
				if(plugin.getShopManager().isShop(b)) {
					SSShop s = plugin.getShopManager().getShopAt(b);

					// Cancel the event first
					e.setCancelled(true);
					
					// Check if the shop is enabled
					if(!s.isEnabled()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "This shop is disabled!");
						plugin.getPlayerModeManager().resetPlayerMode(p, false);
						return;
					}
					
					// Is the shp player owned
					if(!s.getOwnerType().equals(SSShopOwnerType.PLAYER)) {
					
						// Check permissions
						if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.shop.buy")) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
						
						// Check custom shop permissions
						if(s.usesPermissionNode()) {
							String permsNode = s.getPermissionNode();
							if(!plugin.getPermissionsManager().hasPermission(p, permsNode)) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
								plugin.getPlayerModeManager().resetPlayerMode(p, false);
								return;
							}
						}
						
						// Check if any items are sold
						if(!s.canBuy()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "This shop doesn't sell any products!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
						
						//TODO show buy discount
						
						// Show a message to the user, depending on if instant buy and stacking is enabled
						if(!s.instantBuyEnabled()) {
							if(!s.buyInStacks()) {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "How much products do you want to buy?");
								p.sendMessage(ChatColor.YELLOW + "Enter the amount into the chat");
								p.sendMessage(ChatColor.YELLOW + "Right click in the air to cancel the transaction");
								plugin.getPlayerModeManager().setPlayerModeStep(p, 2);
								return;
							} else {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "How much stacks do you want to buy?");
								p.sendMessage(ChatColor.YELLOW + "Enter the amount into the chat");
								p.sendMessage(ChatColor.YELLOW + "Right click in the air to cancel the transaction");
								plugin.getPlayerModeManager().setPlayerModeStep(p, 2);
								return;
							}
						} else {
							
							
							// Check if the product is set
							if(!s.isProductSet()) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "The product of this shop has not been set!");
								plugin.getPlayerModeManager().resetPlayerMode(p, false);
								return;
							}
							
							int quantity = s.getInstantBuyQuantity();
							int itemCount = quantity;
							
							if(s.buyInStacks()) {
								itemCount = itemCount * s.getBuyStack();
							}
							
							double price = s.getBuyPrice() * itemCount;
							//TODO apply buy discount
							
							// Has the player enough money
							if(!plugin.getEconomyManager().hasEnoughMoney(p.getName(), price)) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "You don't have " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true) + ChatColor.DARK_RED + "!");
								return;
							}
							
							// Check if the player has enough space
							int spaceLeft = getPlayerInventoryItemSpace(p, s.getProductItemTypeId(), s.getProductItemData());
							if(quantity > spaceLeft) {
								p.sendMessage("");
								if(spaceLeft == 0)
									p.sendMessage(ChatColor.DARK_RED + "You don't have space left in your inventory!");
								else if(spaceLeft == 1)
									p.sendMessage(ChatColor.DARK_RED + "You only have space for " + ChatColor.GOLD + "1" + ChatColor.DARK_RED + " product in your inventory!");
								else
									p.sendMessage(ChatColor.DARK_RED + "You only have space for " + ChatColor.GOLD + String.valueOf(spaceLeft) + ChatColor.DARK_RED + " products in your inventory!");
								return;
							}
							
							// Add the items to the player inventory
							addItemsToPlayerInventory(p, s.getProductItemTypeId(), s.getProductItemData(), itemCount);
							
							// Charge the player
							plugin.getEconomyManager().withdrawMoney(p.getName(), price);
							
							// Show an message to the player
							p.sendMessage("");
							if(!s.buyInStacks())
								p.sendMessage(ChatColor.GREEN + "You bought " + ChatColor.GOLD + String.valueOf(itemCount) + ChatColor.GREEN + " products for " +
									ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
							else
								p.sendMessage(ChatColor.GREEN + "You bought " + ChatColor.GOLD + String.valueOf(quantity) + ChatColor.GREEN + " stacks for " +
										ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
						}
					
					} else {
						
						// Check permissions
						if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.shop.buy")) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
						
						// Check custom shop permissions
						if(s.usesPermissionNode()) {
							String permsNode = s.getPermissionNode();
							if(!plugin.getPermissionsManager().hasPermission(p, permsNode)) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
								plugin.getPlayerModeManager().resetPlayerMode(p, false);
								return;
							}
						}
						
						// Check if any items are sold
						if(!s.canBuy()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "This shop doesn't sell any products!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
						
						// Are items in stock
						if(s.getStockQuantity() <= 0 || s.getStockTypeId() == 0) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "Not enough items in stock!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
						
						//TODO show buy discount
						
						// Show a message to the user, depending on if instant buy and stacking is enabled
						if(!s.instantBuyEnabled()) {
							if(!s.buyInStacks()) {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "How much products do you want to buy?");
								p.sendMessage(ChatColor.YELLOW + "Enter the amount into the chat");
								p.sendMessage(ChatColor.YELLOW + "Right click in the air to cancel the transaction");
								plugin.getPlayerModeManager().setPlayerModeStep(p, 2);
								return;
							} else {
								p.sendMessage("");
								p.sendMessage(ChatColor.YELLOW + "How much stacks do you want to buy?");
								p.sendMessage(ChatColor.YELLOW + "Enter the amount into the chat");
								p.sendMessage(ChatColor.YELLOW + "Right click in the air to cancel the transaction");
								plugin.getPlayerModeManager().setPlayerModeStep(p, 2);
								return;
							}
						} else {
							
							
							// Check if the product is set
							if(!s.isProductSet()) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "The product of this shop has not been set!");
								plugin.getPlayerModeManager().resetPlayerMode(p, false);
								return;
							}
							
							int quantity = s.getInstantBuyQuantity();
							int itemCount = quantity;
							
							if(s.buyInStacks()) {
								itemCount = itemCount * s.getBuyStack();
							}
							
							// Does the shop contain enough items
							if(s.getStockQuantity() < itemCount) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "This shop doesn't have enough items in stock!");
								return;
							}
							
							double price = s.getBuyPrice() * itemCount;
							//TODO apply buy discount
							
							// Has the player enough money
							if(!plugin.getEconomyManager().hasEnoughMoney(p.getName(), price)) {
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_RED + "You don't have " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true) + ChatColor.DARK_RED + "!");
								return;
							}
							
							// Check if the player has enough space
							int spaceLeft = getPlayerInventoryItemSpace(p, s.getProductItemTypeId(), s.getProductItemData());
							if(quantity > spaceLeft) {
								p.sendMessage("");
								if(spaceLeft == 0)
									p.sendMessage(ChatColor.DARK_RED + "You don't have space left in your inventory!");
								else if(spaceLeft == 1)
									p.sendMessage(ChatColor.DARK_RED + "You only have space for " + ChatColor.GOLD + "1" + ChatColor.DARK_RED + " product in your inventory!");
								else
									p.sendMessage(ChatColor.DARK_RED + "You only have space for " + ChatColor.GOLD + String.valueOf(spaceLeft) + ChatColor.DARK_RED + " products in your inventory!");
								return;
							}
							
							// Add the items to the player inventory
							addItemsToPlayerInventory(p, s.getProductItemTypeId(), s.getProductItemData(), itemCount);
							
							// Remove the items from the shop stock
							s.setStockQuantity(s.getStockQuantity() - itemCount);
							
							// Charge the player
							plugin.getEconomyManager().withdrawMoney(p.getName(), price);
							
							// Deposit the money to the owner
							plugin.getEconomyManager().depositMoney(s.getOwnerName(), price);
							
							// Show an message to the player
							if(!s.buyInStacks())
								p.sendMessage(ChatColor.GREEN + "You bought " + ChatColor.GOLD + String.valueOf(itemCount) + ChatColor.GREEN + " products for " +
									ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
							else
								p.sendMessage(ChatColor.GREEN + "You bought " + ChatColor.GOLD + String.valueOf(quantity) + ChatColor.GREEN + " stacks for " +
										ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));

							// If the owner is online, show a message that someone bought his products
							Player owner = plugin.getServer().getPlayerExact(s.getOwnerName());
							if(owner != null) {
								if(owner.isOnline()) {
									owner.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.YELLOW + " bought " + ChatColor.GOLD + String.valueOf(itemCount) + ChatColor.YELLOW + " products from your shop!");
									owner.sendMessage(ChatColor.YELLOW + "You received " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true) + ChatColor.YELLOW + "!");
								}
							}

							// Update the shop inventory to prevent item duplication
							plugin.getShopManager().getShopInventoryManager().updateShopInventory(s);
							
							// Remove the show item if the palyer owned shop is emptry
							if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
								if(s.getStockQuantity() <= 0)
									s.removeShowItem();
							
							// Should the shop be removed if it's empty
							if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
								if(s.getStockQuantity() <= 0)
									if(s.removeShopWhenEmptyEnabled())
										plugin.getShopManager().removeShop(s);
							
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
					}
				}
			}
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_BUY_TRANSACTION)) {
				if(plugin.getPlayerModeManager().getPlayerModeStep(p) == 2) {
					
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "Transaction cancelled!");
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
					return;
				}
			}
		}
		
		// Shop sell transactions
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			// IS this the shop owner? If it is open the shops inventory
			if(plugin.getShopManager().isShop(b)) {
				if(plugin.getShopManager().getShopAt(b).getOwnerType().equals(SSShopOwnerType.PLAYER)) {
					SSShop s = plugin.getShopManager().getShopAt(b);
					String ownerName = s.getOwnerName();
					if(p.getName().equals(ownerName)) {
						e.setCancelled(true);
						plugin.getShopManager().getShopInventoryManager().openShopInventory(s, p);
						return;
					} else {
						e.setCancelled(true);
						p.sendMessage("");
						p.sendMessage(ChatColor.YELLOW + "This is the shop of " + ChatColor.GOLD + ownerName);
						p.sendMessage(ChatColor.YELLOW + "Left click the shop to buy an item");
						return;
					}
				}
			}
			
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.NONE)) {
				if(plugin.getShopManager().isShop(b)) {
					if(plugin.getShopManager().hasLastUsedShop(p.getName())) {
						if(plugin.getShopManager().getLastUsedShop(p.getName()).getId() != plugin.getShopManager().getShopAt(b).getId()) {
							// If the player selects another shop make sure he is shown the price and things again
							plugin.getShopManager().forgetLastUsedShop(p.getName());
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
						}
					} else {
						// If the player selects another shop make sure he is shown the price and things again
						plugin.getShopManager().forgetLastUsedShop(p.getName());
						plugin.getPlayerModeManager().resetPlayerMode(p, false);
					}
						
				} else {
					// The player didn't used a shop yet, reset it's mode to fix it
					plugin.getShopManager().forgetLastUsedShop(p.getName());
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
				}
			}
			
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.NONE) ||
					plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_BUY_TRANSACTION) ||
					plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_BUY_TRANSACTION)) {
				if(plugin.getShopManager().isShop(b)) {
					SSShop s = plugin.getShopManager().getShopAt(b);

					// Cancel the event first
					e.setCancelled(true);
					
					// Check if the shop is enabled
					if(!s.isEnabled()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "This shop is disabled!");
						return;
					}
					
					// Check permissions
					if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.shop.sell")) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
						return;
					}
					
					// Check custom shop permissions
					if(s.usesPermissionNode()) {
						String permsNode = s.getPermissionNode();
						if(!plugin.getPermissionsManager().hasPermission(p, permsNode)) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
							return;
						}
					}
					
					// Check if any items are sold
					if(!s.canSell()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "You can't sell any items to this shop!");
						return;
					}
					
					if(!s.isProductSet()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "The product of this shop has not been set yet!");
						plugin.getPlayerModeManager().resetPlayerMode(p, false);
						return;
					}
					
					// Set the players last used shop
					plugin.getShopManager().setLastUsedShop(p.getName(), s);
					
					//TODO show buy discount
					
					// Show a message to the user, depending on if instant buy and stacking is enabled
					if(!s.instantSellEnabled()) {
						if(!s.sellInStacks()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.YELLOW + "Sell each product for " + ChatColor.GOLD + s.getSellPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getSellPrice()));
							p.sendMessage(ChatColor.YELLOW + "Click the shop again to sell the product");
						} else {
							p.sendMessage("");
							p.sendMessage(ChatColor.YELLOW + "This product has to be sold in stacks of " + ChatColor.GOLD + s.getSellStack());
							p.sendMessage(ChatColor.YELLOW + "Sell each stack for " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(s.getSellPrice() * s.getSellStack(), true));
							p.sendMessage(ChatColor.YELLOW + "Click the shop again to sell the product");
						}
					} else {
						if(!s.sellInStacks()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.YELLOW + "Sell each product for " + ChatColor.GOLD + s.getSellPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getSellPrice()));
							p.sendMessage(ChatColor.YELLOW + "Click the shop again to instantly sell " + ChatColor.GOLD + s.getInstantSellQuantity() + ChatColor.YELLOW + " products");
						} else {
							p.sendMessage("");
							p.sendMessage(ChatColor.YELLOW + "This product has to be sold in stacks of " + ChatColor.GOLD + s.getSellStack());
							p.sendMessage(ChatColor.YELLOW + "Sell each stack for " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(s.getSellPrice() * s.getSellStack(), true));
							p.sendMessage(ChatColor.YELLOW + "Click the shop again to instantly sell " + ChatColor.GOLD + s.getInstantSellQuantity() + ChatColor.YELLOW + " stacks");
						}
					}
					
					plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_SELL_TRANSACTION, false);
					return;
						
				}
			}
			
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SELL_TRANSACTION)) {
				if(plugin.getShopManager().isShop(b)) {
					SSShop s = plugin.getShopManager().getShopAt(b);

					// Cancel the event first
					e.setCancelled(true);
					
					// Check if the shop is enabled
					if(!s.isEnabled()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "This shop is disabled!");
						plugin.getPlayerModeManager().resetPlayerMode(p, false);
						return;
					}
					
					// Check permissions
					if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.shop.sell")) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
						plugin.getPlayerModeManager().resetPlayerMode(p, false);
						return;
					}
					
					// Check custom shop permissions
					if(s.usesPermissionNode()) {
						String permsNode = s.getPermissionNode();
						if(!plugin.getPermissionsManager().hasPermission(p, permsNode)) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
					}
					
					// Check if any items are sold
					if(!s.canSell()) {
						p.sendMessage("");
						p.sendMessage(ChatColor.DARK_RED + "You can't sell any products to this shop!");
						plugin.getPlayerModeManager().resetPlayerMode(p, false);
						return;
					}
					
					//TODO show buy discount
					
					// Show a message to the user, depending on if instant buy and stacking is enabled
					if(!s.instantSellEnabled()) {
						if(!s.sellInStacks()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.YELLOW + "How much products do you want to sell?");
							p.sendMessage(ChatColor.YELLOW + "Enter the amount into the chat");
							p.sendMessage(ChatColor.YELLOW + "Right click in the air to cancel the transaction");
							plugin.getPlayerModeManager().setPlayerModeStep(p, 2);
							return;
						} else {
							p.sendMessage("");
							p.sendMessage(ChatColor.YELLOW + "How much stacks do you want to sell?");
							p.sendMessage(ChatColor.YELLOW + "Enter the amount into the chat");
							p.sendMessage(ChatColor.YELLOW + "Right click in the air to cancel the transaction");
							plugin.getPlayerModeManager().setPlayerModeStep(p, 2);
							return;
						}
					} else {
						
						// Check if the product is set
						if(!s.isProductSet()) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + "The product of this shop has not been set!");
							plugin.getPlayerModeManager().resetPlayerMode(p, false);
							return;
						}
						
						int quantity = s.getInstantSellQuantity();
						int itemCount = quantity;
						
						if(s.sellInStacks()) {
							itemCount = itemCount * s.getSellStack();
						}
						
						double price = s.getSellPrice() * itemCount;
						//TODO apply buy discount
						
						// Check if the player has enough space
						// Check if the player has enough products
						int invCount = getPlayerInventoryItemCount(p, s.getProductItemTypeId(), s.getProductItemData());
						if(quantity > invCount) {
							p.sendMessage("");
							p.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
							if(invCount == 0)
								p.sendMessage(ChatColor.DARK_RED + "You don't have any products of this kind!");
							else if(invCount == 1)
								p.sendMessage(ChatColor.DARK_RED + "You only have " + ChatColor.GOLD + "1" + ChatColor.DARK_RED + " product of this kind!");
							else
								p.sendMessage(ChatColor.DARK_RED + "You only have " + ChatColor.GOLD + String.valueOf(invCount) + ChatColor.DARK_RED + " products of this kind!");
							return;
						}
						
						// Add the items to the player inventory
						removeItemsFromPlayerInventory(p, s.getProductItemTypeId(), s.getProductItemData(), itemCount);
						
						// Deposit the money to the player
						plugin.getEconomyManager().depositMoney(p.getName(), price);
						
						// Show an message to the player
						p.sendMessage("");
						if(!s.sellInStacks())
							p.sendMessage(ChatColor.GREEN + "You sold " + ChatColor.GOLD + String.valueOf(itemCount) + ChatColor.GREEN + " products for " +
								ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
						else
							p.sendMessage(ChatColor.GREEN + "You sold " + ChatColor.GOLD + String.valueOf(quantity) + ChatColor.GREEN + " stacks for " +
									ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
						return;
					}
				}
			}
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SELL_TRANSACTION)) {
				if(plugin.getPlayerModeManager().getPlayerModeStep(p) == 2) {
					
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "Transaction cancelled!");
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		
		// Check if any shop was used and if the player is in the buy transaction mode first, if not return
		if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_BUY_TRANSACTION)) {
			if(!plugin.getShopManager().hasLastUsedShop(p.getName())) {
				// If the player selects another shop make sure he is shown the price and things again
				plugin.getShopManager().forgetLastUsedShop(p.getName());
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
		}
		
		if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_BUY_TRANSACTION) &&
				plugin.getPlayerModeManager().getPlayerModeStep(p) == 2) {
			SSShop s = plugin.getShopManager().getLastUsedShop(p.getName());

			// Cancel the event first
			e.setCancelled(true);
			
			// Check if the shop is enabled
			if(!s.isEnabled()) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "This shop is disabled!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			// Check permissions
			if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.shop.buy")) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			if(!s.getOwnerType().equals(SSShopOwnerType.PLAYER)) {
				// Check if the product is set
				if(!s.isProductSet()) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "The product of this shop has not been set!");
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
					return;
				}
			} else {
				// Are items in stock
				if(s.getStockQuantity() <= 0 || s.getStockTypeId() == 0) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "Not enough items in stock!");
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
					return;
				}
			}	
			
			// Check custom shop permissions
			if(s.usesPermissionNode()) {
				String permsNode = s.getPermissionNode();
				if(!plugin.getPermissionsManager().hasPermission(p, permsNode)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
					return;
				}
			}
			
			// Check if any items are sold
			if(!s.canBuy()) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "This shop doesn't sell any products!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			// Check if the entered value is valid
			String quantityArg = e.getMessage();
			if(!isInt(quantityArg)) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + quantityArg);
				p.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity!");
				return;
			}
			
			int quantity = Integer.parseInt(quantityArg);

			if(s.getMinBuyQuantity() > 0 && s.getMinBuyQuantity() > quantity) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + quantityArg);
				p.sendMessage(ChatColor.DARK_RED + "You've to buy at least " + ChatColor.GOLD + String.valueOf(s.getMinBuyQuantity()) + ChatColor.DARK_RED + " products!");
				return;
			}
			if(s.getMaxBuyQuantity() > 0 && s.getMaxBuyQuantity() < quantity) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + quantityArg);
				p.sendMessage(ChatColor.DARK_RED + "You can only buy a maximum of " + ChatColor.GOLD + String.valueOf(s.getMaxBuyQuantity()) + ChatColor.DARK_RED + " products at once!");
				return;
			}
			
			if(quantity <= 0) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "Transaction cancelled!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			int itemCount = quantity;
			
			if(s.buyInStacks()) {
				itemCount = itemCount * s.getBuyStack();
			}
			
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER)) {
				// Are enough items in stock
				if(s.getStockQuantity() < itemCount) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "Not enough items in stock!");
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
					return;
				}
			}	
			
			double price = s.getBuyPrice() * itemCount;
			//TODO apply buy discount
			
			// Has the player enough money
			if(!plugin.getEconomyManager().hasEnoughMoney(p.getName(), price)) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "You don't have " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true) + ChatColor.DARK_RED + "!");
				return;
			}
			
			// Check if the player has enough space
			int spaceLeft = getPlayerInventoryItemSpace(p, s.getProductItemTypeId(), s.getProductItemData());
			if(quantity > spaceLeft) {
				p.sendMessage("");
				if(spaceLeft == 0)
					p.sendMessage(ChatColor.DARK_RED + "You don't have space left in your inventory!");
				else if(spaceLeft == 1)
					p.sendMessage(ChatColor.DARK_RED + "You only have space for " + ChatColor.GOLD + "1" + ChatColor.DARK_RED + " product in your inventory!");
				else
					p.sendMessage(ChatColor.DARK_RED + "You only have space for " + ChatColor.GOLD + String.valueOf(spaceLeft) + ChatColor.DARK_RED + " products in your inventory!");
				return;
			}
			
			// Add the items to the player inventory
			if(!s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				addItemsToPlayerInventory(p, s.getProductItemTypeId(), s.getProductItemData(), itemCount);
			else
				addItemsToPlayerInventory(p, s.getStockTypeId(), s.getStockDataValue(), itemCount);
			
			// Charge the player
			plugin.getEconomyManager().withdrawMoney(p.getName(), price);
			
			// If the shop is player owned, deposit he money to the owner
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER)) {
				// Decrease the stock quantity
				s.setStockQuantity(s.getStockQuantity() - itemCount);
				
				// If the owner is online, show a message that someone bought his products
				Player owner = plugin.getServer().getPlayerExact(s.getOwnerName());
				if(owner != null) {
					if(owner.isOnline()) {
						owner.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.YELLOW + " bought " + ChatColor.GOLD + String.valueOf(quantity) + ChatColor.YELLOW + " products from your shop!");
						owner.sendMessage(ChatColor.YELLOW + "You received " + ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true) + ChatColor.YELLOW + "!");
					}
				}
				
				// Deposit the money to the player
				plugin.getEconomyManager().depositMoney(s.getOwnerName(), price);
			}
			
			// Show an message to the player
			p.sendMessage("");
			if(!s.buyInStacks())
				p.sendMessage(ChatColor.GREEN + "You bought " + ChatColor.GOLD + String.valueOf(itemCount) + ChatColor.GREEN + " products for " +
						ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
			else
				p.sendMessage(ChatColor.GREEN + "You bought " + ChatColor.GOLD + String.valueOf(itemCount) + ChatColor.GREEN + " stacks for " +
						ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
			
			// Update the shop inventory to prevent item duplication
			plugin.getShopManager().getShopInventoryManager().updateShopInventory(s);
			
			// Remove the show item if the palyer owned shop is emptry
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				if(s.getStockQuantity() <= 0)
					s.removeShowItem();
			
			// Should the shop be removed if it's empty
			if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
				if(s.getStockQuantity() <= 0)
					if(s.removeShopWhenEmptyEnabled())
						plugin.getShopManager().removeShop(s);
			
			// Reset the player mode to get him out of the shop transaction mode
			plugin.getPlayerModeManager().resetPlayerMode(p, false);
		}
		
		// Check if any shop was used and if the player is in the buy transaction mode first, if not return
		if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SELL_TRANSACTION)) {
			if(!plugin.getShopManager().hasLastUsedShop(p.getName())) {
				// If the player selects another shop make sure he is shown the price and things again
				plugin.getShopManager().forgetLastUsedShop(p.getName());
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
		}
		
		if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_SELL_TRANSACTION) &&
				plugin.getPlayerModeManager().getPlayerModeStep(p) == 2) {
			SSShop s = plugin.getShopManager().getLastUsedShop(p.getName());

			// Cancel the event first
			e.setCancelled(true);
			
			// Check if the shop is enabled
			if(!s.isEnabled()) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "This shop is disabled!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			// Check permissions
			if(!plugin.getPermissionsManager().hasPermission(p, "simpleshowcase.shop.sell")) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			// Check if the product is set
			if(!s.isProductSet()) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "The product of this shop has not been set!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			// Check custom shop permissions
			if(s.usesPermissionNode()) {
				String permsNode = s.getPermissionNode();
				if(!plugin.getPermissionsManager().hasPermission(p, permsNode)) {
					p.sendMessage("");
					p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this shop!");
					plugin.getPlayerModeManager().resetPlayerMode(p, false);
					return;
				}
			}
			
			// Check if any items are sold
			if(!s.canSell()) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "You can't sell your items to this shop!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			// Check if the entered value is valid
			String quantityArg = e.getMessage();
			if(!isInt(quantityArg)) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + quantityArg);
				p.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity!");
				return;
			}
			
			int quantity = Integer.parseInt(quantityArg);

			if(s.getMinSellQuantity() > 0 && s.getMinSellQuantity() > quantity) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + quantityArg);
				p.sendMessage(ChatColor.DARK_RED + "You've to sell at least " + ChatColor.GOLD + String.valueOf(s.getMinSellQuantity()) + ChatColor.DARK_RED + " products!");
				return;
			}
			if(s.getMaxSellQuantity() > 0 && s.getMaxSellQuantity() < quantity) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + quantityArg);
				p.sendMessage(ChatColor.DARK_RED + "You can only sell a maximum of " + ChatColor.GOLD + String.valueOf(s.getMaxSellQuantity()) + ChatColor.DARK_RED + " products at once!");
				return;
			}
			
			if(quantity <= 0) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "Transaction cancelled!");
				plugin.getPlayerModeManager().resetPlayerMode(p, false);
				return;
			}
			
			int itemCount = quantity;
			
			if(s.sellInStacks()) {
				itemCount = itemCount * s.getSellStack();
			}
			
			double price = s.getSellPrice() * itemCount;
			//TODO apply buy discount
			
			// Check if the player has enough products
			int invCount = getPlayerInventoryItemCount(p, s.getProductItemTypeId(), s.getProductItemData());
			if(quantity > invCount) {
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
				if(invCount == 0)
					p.sendMessage(ChatColor.DARK_RED + "You don't have any products of this kind!");
				else if(invCount == 1)
					p.sendMessage(ChatColor.DARK_RED + "You only have " + ChatColor.GOLD + "1" + ChatColor.DARK_RED + " product of this kind!");
				else
					p.sendMessage(ChatColor.DARK_RED + "You only have " + ChatColor.GOLD + String.valueOf(invCount) + ChatColor.DARK_RED + " products of this kind!");
				return;
			}
			
			// Remove the items from the player inventory
			removeItemsFromPlayerInventory(p, s.getProductItemTypeId(), s.getProductItemData(), itemCount);
			
			// Deposit the money to the player
			plugin.getEconomyManager().depositMoney(p.getName(), price);
			
			// Show an message to the player
			p.sendMessage("");
			if(itemCount == 1)
				p.sendMessage(ChatColor.GREEN + "You sold " + ChatColor.GOLD + "1" + ChatColor.GREEN + " product for " +
						ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
			else
				p.sendMessage(ChatColor.GREEN + "You sold " + ChatColor.GOLD + String.valueOf(itemCount) + ChatColor.GREEN + " products for " +
						ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
			
			// Reset the player mode to get him out of the shop transaction mode
			plugin.getPlayerModeManager().resetPlayerMode(p, false);
		}
		

		
		if(plugin.getPlayerModeManager().getPlayerMode(p).equals(PlayerModeType.SHOP_PLAYEROWNED_CREATION_WIZZARD) &&
				plugin.getPlayerModeManager().getPlayerModeStep(p) == 3) {
			// Cancel the event first
			e.setCancelled(true);
			
			// Is a shop selected?
			if(!plugin.getShopManager().hasShopSelected(p.getName())) {
				p.sendMessage(ChatColor.DARK_RED + "Whoops, something went wrong!");
				return;
			}
			
			// Is the price valid
			String newPrice = e.getMessage();
			
			if(newPrice.trim().equals("")) {
				p.sendMessage(ChatColor.DARK_RED + "Please enter a valid price");
				return;
			}
			
			// Is the price valid
			if(!isDouble(newPrice)) {
				p.sendMessage(ChatColor.DARK_RED + newPrice);
				p.sendMessage(ChatColor.DARK_RED + "Please enter a valid product price");
				return;
			}
			
			double price = Double.parseDouble(newPrice);

			// The price may not be negative
			if(price < 0) {
				p.sendMessage(ChatColor.DARK_RED + String.valueOf(price));
				p.sendMessage(ChatColor.DARK_RED + "The price may not be negative");
				return;
			}
			
			// Set the product sell price
			plugin.getShopManager().getSelectedShop(p.getName()).setBuyPrice(price);

			p.sendMessage(ChatColor.GREEN + "The product price has been changed to " +
					ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));

			p.sendMessage(ChatColor.GREEN + "Your shop has been created and set up!");
			p.sendMessage(ChatColor.GREEN + "Right click your shop to add products to your shop!");
			
			// Reset the player mode to get him out of the shop transaction mode
			plugin.getPlayerModeManager().resetPlayerMode(p, false);
			
			return;
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		Item i = e.getItem();
		
		// If the item is an show item, cancel the event to block the player from picking up the item
		if(plugin.getShopManager().isShopShowItem(i)) {
			e.setCancelled(true);
		}
	}
	
	public static double roundMoney(double money) {
	    DecimalFormat twoDForm = new DecimalFormat("#0.##");
	    return plugin.stringToDouble(twoDForm.format(money));
	}
	public static String fixMoney(double money) {
		DecimalFormat twoDForm = new DecimalFormat("#0.00");
	    return twoDForm.format(money);
	}
	public static String getMoneyString(double money) {
	    return fixMoney(money) + " Silver";
	}
	
	public boolean containsChar(String s, char[] c) {
		for(int i = 0; i < c.length; i++) {
			if(containsChar(s, c[i])) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsChar(String s, char c) {
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == c) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInt(String string) {
        try {
			@SuppressWarnings("unused")
			int i = Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
	
	public boolean isDouble(String string) {
        try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
	
	public boolean stringIsInt(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        } catch (Exception ex) {
        	return false;
        }
        return true;
    }
	
	public boolean stringIsByte(String string) {
        try {
            Byte.valueOf(string);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
	
	public boolean stringIsDouble(String string) {
        try {
            Double.valueOf(string);
        } catch (Exception ex) {
            return false;
        }
        return true;
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
	
	/**
	 * Get the space left in a players inventory for an item
	 * @param player the player
	 * @param itemTypeId the item type id
	 * @param itemData the item data
	 * @return the space left for this item
	 */
	public int getPlayerInventoryItemSpace(Player player, int itemTypeId, byte itemData) {
		Inventory inv = player.getInventory();
		
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
	
	/**
	 * Count the items in the players inventory of a specific items
	 * @param player the player
	 * @param itemTypeId item type id
	 * @param itemData item data
	 * @return the item count
	 */
	public int getPlayerInventoryItemCount(Player player, int itemTypeId, byte itemData){
		Inventory inv = player.getInventory();
		int tot = 0;
		for(int i = 0; i<inv.getSize(); i++){
			ItemStack stack = inv.getItem(i);
			if((stack!=null) && (stack.getTypeId() == itemTypeId)&& (stack.getData().getData() == itemData)) {
				tot+=stack.getAmount();
			}
		}
		return tot;
	}

	/**
	 * Remove items from the players inventory
	 * @param player the player
	 * @param itemTypeId the item id
	 * @param itemData the data
	 * @param a the amount
	 */
	public void removeItemsFromPlayerInventory(Player player, int itemTypeId, byte itemData, int a) {
		Inventory inv = player.getInventory();
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
		
		player.updateInventory();
	}
	
	/**
	 * Add items into a players inventory
	 * @param player the player
	 * @param typeId the item type id
	 * @param data the item data
	 * @param a the amount
	 * @return return the number of items that did not fit
	 */
	public int addItemsToPlayerInventory(Player p, int typeId, byte data, int a) {
		PlayerInventory inv = p.getInventory();
		
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
