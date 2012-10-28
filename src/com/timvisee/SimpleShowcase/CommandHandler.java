package com.timvisee.SimpleShowcase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class CommandHandler {
	
	SimpleShowcase plugin;
	
	CommandHandler(SimpleShowcase instance) {
		this.plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		SSShopManager sm = plugin.getShopManager();
		SSBoothManager bm = plugin.getBoothManager();
		PermissionsManager pm = plugin.getPermissionsManager();
		
		if (commandLabel.equalsIgnoreCase("simpleshowcase") || commandLabel.equalsIgnoreCase("ss")) {
			// Command arguments should already be combined by the executor of this function
			
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("shop") || args[0].equalsIgnoreCase("shops") || args[0].equalsIgnoreCase("s")) {
				// Check if a second argument is set
				if(args.length < 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				if(args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("c")) {
					// Check if there are enough arguments
					if(args.length < 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission(p, "simpleshowcase.command.shop.create")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					String sname = "";
					
					if(args.length == 3) {
						// The used filled in a name
						sname = args[2].toString();
						
						// Trim the name and check if it's valid
						if(sname.trim().equals("")) {
							sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid name for the shop!");
							return true;
						}
						
						// Check if the name contains illegal characters
						char[] ILLEGAL_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', '\'', ':', '.'};
						if(containsChar(sname, ILLEGAL_CHARACTERS)) {
							sender.sendMessage(ChatColor.DARK_RED + "The name contains illigal characters");
							return true;
						}
						
						// Check if there already is a portal with this name
						if(!plugin.getShopManager().isUniqueServerOwnedName(sname, false)) {
							sender.sendMessage(ChatColor.DARK_RED + sname);
							sender.sendMessage(ChatColor.DARK_RED + "There already is a shop with this name!");
							return true;
						}
						
					} else {
						// Generate an unique name
						sname = plugin.getShopManager().getUniqueServerOwnedName();
					}
					
					SSPricelistItem applyPricelistItem = null;
					if(isFlagSet(args, "pli")) {
						String pli = getFlagArgument(args, "pli");
						
						// Check permissions
						if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.linkpricelistitem")) {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to use pricelist items!");
							return true;
						}
						
						String value = pli;
						List<String> values = Arrays.asList(value.split(":"));
						
						if(values.size() == 1) {
							if(plugin.getPricelistManager().countItemsWithName(values.get(0)) == 0) {
								sender.sendMessage(ChatColor.DARK_RED + values.get(0));
								sender.sendMessage(ChatColor.DARK_RED + "There's no pricelist item with this name!");
								return true;
							} else if(plugin.getPricelistManager().countItemsWithName(values.get(0)) == 1) {
								applyPricelistItem = plugin.getPricelistManager().getItem(values.get(0));
							} else {
								int count = plugin.getPricelistManager().countItemsWithName(values.get(0));
								sender.sendMessage(ChatColor.DARK_RED + values.get(0));
								sender.sendMessage(ChatColor.DARK_RED + String.valueOf(count) + " matching items found, choose one of these:");
								for(SSPricelistItem entry : plugin.getPricelistManager().getItems(values.get(0)))
									sender.sendMessage(ChatColor.GOLD + " - " + ChatColor.YELLOW + entry.getPricelistName() + ":" + entry.getItemName());
								return true;
							}
						} else {
							String pricelistName = values.get(0);
							String itemName = values.get(1);
							
							if(!plugin.getPricelistManager().isPricelistWithName(pricelistName)) {
								sender.sendMessage(ChatColor.DARK_RED + pricelistName + ChatColor.GRAY + ":" + itemName);
								sender.sendMessage(ChatColor.DARK_RED + "There's no pricelist with this name!");
								return true;
							}
							
							if(!plugin.getPricelistManager().isPricelistItemWithName(pricelistName, itemName)) {
								sender.sendMessage(ChatColor.GRAY + pricelistName + ":" + ChatColor.DARK_RED + itemName);
								sender.sendMessage(ChatColor.DARK_RED + "There's no item with this name in this pricelist!");
								return true;
							}
							
							applyPricelistItem = plugin.getPricelistManager().getItem(pricelistName, itemName);
						}
					}
					if(isFlagSet(args, "sl")) {
						// Check permissions
						if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setlocation")) {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to set the location!");
							return true;
						}
					}
					
					// Create/define a new shop
					SSShop s = sm.createShop();
					s.setName(sname);
					s.setOwnerType(SSShopOwnerType.SERVER);
					
					// Apply an price list item if it's set
					if(applyPricelistItem != null) {
						// Create the shop and apply the pricelist instantly
						s.linkPricelistItem(applyPricelistItem);
						plugin.getPricelistManager().applyItemToShop(applyPricelistItem, s);
					}
					
					// Select the new created portal
					plugin.getShopManager().selectShop(p.getName(), s);

					sender.sendMessage(ChatColor.GREEN + "The shop " + ChatColor.GOLD + s.getName() + ChatColor.GREEN + " has been created, and is now selected!");
					
					// If the shop has a pricelist item linked, show some more information about it
					if(applyPricelistItem != null) {
						sender.sendMessage(ChatColor.GREEN + "The pricelist item of the shop has been set to:");
						sender.sendMessage(ChatColor.YELLOW + applyPricelistItem.getPricelistName() + ChatColor.GRAY + " : " +
								ChatColor.YELLOW + applyPricelistItem.getItemName());
					}
					
					if(isFlagSet(args, "sl")) {
						// Put the player in the shop selection mode and return a message
						sender.sendMessage("");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_LOCATION_SELECTION, true);
						sender.sendMessage(ChatColor.GREEN + "Left click a block to put the shop on");
						sender.sendMessage(ChatColor.GREEN + "Right click in the air to get out of the selection mode");
					}
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("select") || args[1].equalsIgnoreCase("sel") || args[1].equalsIgnoreCase("s")) {
					
					// Acceptable amount of arguments?
					if(args.length < 2 || args.length > 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}

					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission(p, "simpleshowcase.command.shop.select")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					if(args.length == 3) {
						// The player filled in a name to select
						String sname = args[2].toString();
						
						List<String> values = Arrays.asList(sname.split(":"));
						
						if(values.size() == 1) {
							// Is there any shop with this name?
							if(plugin.getShopManager().isUniqueServerOwnedName(sname, false)) {
								sender.sendMessage(ChatColor.DARK_RED + sname);
								sender.sendMessage(ChatColor.DARK_RED + "There's no server owned shop available with this name!");
								sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " shop list [page]" + ChatColor.YELLOW + " to list all shops");
								return true;
							}
							
							// Get and select a shop
							SSShop s = plugin.getShopManager().getServerOwned(sname);
							plugin.getShopManager().selectShop(p.getName(), s);
							
							sender.sendMessage(ChatColor.GREEN + "The shop " + ChatColor.GOLD + sname + ChatColor.GREEN + " has been selected");
							return true;
						} else {
							// Is there any shop with this name?
							if(plugin.getShopManager().isUniquePlayerOwnedName(values.get(0), values.get(1))) {
								sender.sendMessage(ChatColor.DARK_RED + values.get(1));
								sender.sendMessage(ChatColor.DARK_RED + "There's no player owned shop from " + ChatColor.GOLD + values.get(0) + ChatColor.DARK_RED + " available with this name!");
								sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " shop list [page]" + ChatColor.YELLOW + " to list all shops");
								return true;
							}
							
							// Get and select a shop
							SSShop s = plugin.getShopManager().getPlayerOwned(values.get(0), values.get(1));
							plugin.getShopManager().selectShop(p.getName(), s);
							
							sender.sendMessage(ChatColor.GREEN + "The shop " + ChatColor.GOLD + values.get(1) + ChatColor.GREEN + " from " + ChatColor.GOLD + values.get(0) + ChatColor.GREEN + " has been selected");
							return true;
						}
						
					} else {
						// The player wants to use the selection by clicking way
						sender.sendMessage("");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_SELECTION, true);
						sender.sendMessage(ChatColor.GREEN + "Left click a shop to select it");
						sender.sendMessage(ChatColor.GREEN + "Right click in the air to get out of the selection mode");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("selectid") || args[1].equalsIgnoreCase("selid") || args[1].equalsIgnoreCase("sid")) {
					
					// Acceptable amount of arguments?
					if(args.length < 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}

					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission(p, "simpleshowcase.command.shop.selectid")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					String sidString = args[2].toString();
					
					// Is the argument ID an valid ID (is it a number)
					if(!isInt(sidString)) {
						sender.sendMessage(ChatColor.DARK_RED + sidString);
						sender.sendMessage(ChatColor.DARK_RED + "The ID has to be a number!");
						return true;
					}
					
					int sid = Integer.parseInt(sidString);
					
					// Is there any portal with this name?
					if(plugin.getShopManager().isUniqueId(sid)) {
						sender.sendMessage(ChatColor.DARK_RED + sidString);
						sender.sendMessage(ChatColor.DARK_RED + "There's no shop available with this ID!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " shop list [page]" + ChatColor.YELLOW + " to list all shops");
						return true;
					}
					
					// Get and select a portal
					SSShop s = plugin.getShopManager().get(sid);
					plugin.getShopManager().selectShop(p.getName(), s);

					sender.sendMessage(ChatColor.GREEN + "The shop " + ChatColor.GOLD + s.getName() + ChatColor.GREEN + " has been selected");
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("l")) {
					
					// Check permissions
					if(sender instanceof Player) {
						if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.list")) {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
							return true;
						}
					}
					
					if(plugin.getShopManager().countShops() == 0) {
						sender.sendMessage(ChatColor.DARK_RED + "There're not shops created yet!");
						return true;
					}
					
					List<SSShop> shops = plugin.getShopManager().getServerOwnedShops();
					int start = 0;
					int amount = 10;
				
					// Flags
					if(isFlagSet(args, "a")) {
						String stra = getFlagArgument(args, "a");
						if(!isInt(stra)) {
							sender.sendMessage(ChatColor.DARK_RED + stra);
							sender.sendMessage(ChatColor.DARK_RED + "The amount has to be a number!");
							return true;
						}
						int a = Integer.valueOf(stra);
						if(a < 1) {
							sender.sendMessage(ChatColor.DARK_RED + stra);
							sender.sendMessage(ChatColor.DARK_RED + "The amount needs to be 1 or higher!");
							return true;
						}
						amount = a;
					}
					if(isFlagSet(args, "ot")) {
						String ot = getFlagArgument(args, "ot");
						if(ot.equalsIgnoreCase("server") || ot.equalsIgnoreCase("s")) {
							shops = plugin.getShopManager().getServerOwnedShops();
							
						} else if(ot.equalsIgnoreCase("player") || ot.equalsIgnoreCase("players") || ot.equalsIgnoreCase("p")) {
							shops = plugin.getShopManager().getPlayerOwnedShops();
							
						} else if(ot.equalsIgnoreCase("all") || ot.equalsIgnoreCase("a") || ot.equalsIgnoreCase("both") || ot.equalsIgnoreCase("both")) {
							shops = plugin.getShopManager().getShops();
						
						} else {
							sender.sendMessage(ChatColor.DARK_RED + ot);
							sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid ownertype flag argument!");
							sender.sendMessage(ChatColor.GOLD + " - server");
							sender.sendMessage(ChatColor.GOLD + " - player");
							sender.sendMessage(ChatColor.GOLD + " - all");
							return true;
						}
					}
					
					int maxPage = (int) Math.ceil(shops.size() / amount) + 1;
					
					// Is any page number entered?
					if(args.length >= 3) {
						String arg2 = args[2].toString();
						if(isInt(arg2)) {
							int pag = Integer.valueOf(arg2);
							// Is the number 1 or higher?
							if(pag < 1) {
								sender.sendMessage(ChatColor.DARK_RED + arg2);
								sender.sendMessage(ChatColor.DARK_RED + "The page number has to be 1 or higher!");
								return true;
							}
							start = (pag - 1) * amount;
							
							if(pag > maxPage) {
								sender.sendMessage(ChatColor.DARK_RED + arg2);
								sender.sendMessage(ChatColor.DARK_RED + "The last page is " + String.valueOf(maxPage) + "!");
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.DARK_RED + arg2);
							sender.sendMessage(ChatColor.DARK_RED + "The page number has to be a number!");
							return true;
						}
					}
					
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GREEN + "==========[ SHOPS " + String.valueOf(start+1) + "/" + String.valueOf(start+amount) + " ]==========");
					for(int i = start; (i < shops.size() && i < start+amount); i++) {
						SSShop s = shops.get(i);
						if(sender instanceof Player) {
							Player p = (Player) sender;
							if(plugin.getShopManager().hasShopSelected(p.getName())) {
								if(plugin.getShopManager().getSelectedShop(p.getName()).getId() == s.getId()) {
									if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
										sender.sendMessage(ChatColor.AQUA + String.valueOf(i+1) +
												". ID: " + ChatColor.DARK_AQUA + String.valueOf(s.getId()) +
												ChatColor.AQUA + "   Name: " + ChatColor.DARK_AQUA + s.getOwnerName() + ":" + s.getName() +
												ChatColor.GRAY + "   (Owner: " + s.getOwnerName() + ")");
									else
										sender.sendMessage(ChatColor.AQUA + String.valueOf(i+1) +
												". ID: " + ChatColor.DARK_AQUA + String.valueOf(s.getId()) +
												ChatColor.AQUA + "   Name: " + ChatColor.DARK_AQUA + s.getName());
									continue;
								}
							}
						}
						if(s.getOwnerType().equals(SSShopOwnerType.PLAYER))
							sender.sendMessage(ChatColor.YELLOW + String.valueOf(i+1) +
									". ID: " + ChatColor.GOLD + String.valueOf(s.getId()) +
									ChatColor.YELLOW + "   Name: " + ChatColor.GOLD + s.getOwnerName() + ":" + s.getName() +
									ChatColor.GRAY + "   (Owner: " + s.getOwnerName() + ")");
						else
							sender.sendMessage(ChatColor.YELLOW + String.valueOf(i+1) +
									". ID: " + ChatColor.GOLD + String.valueOf(s.getId()) +
									ChatColor.YELLOW + "   Name: " + ChatColor.GOLD + s.getName());
					}
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("info") || args[1].equalsIgnoreCase("i")) {
					
					// Check if there are enough arguments
					if(args.length > 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.info")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a portal selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					// Get the selected shop
					SSShop s = plugin.getShopManager().getSelectedShop(p.getName());
					
					sender.sendMessage(ChatColor.GREEN + "==========[ SHOP INFO ]==========");
					sender.sendMessage(ChatColor.GOLD + "Id: " + ChatColor.YELLOW + String.valueOf(s.getId()));
					sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.YELLOW + s.getName());
					
					// Is shop enabled
					if(s.isEnabled())
						sender.sendMessage(ChatColor.GOLD + "Enabled: " + ChatColor.GREEN + "Yes");
					else
						sender.sendMessage(ChatColor.GOLD + "Enabled: " + ChatColor.DARK_RED + "No");
					
					// The owner
					switch (s.getOwnerType()) {
					case SERVER:
						sender.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.YELLOW + "Server");
						break;
					case PLAYER:
						sender.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.YELLOW + "Player: " + ChatColor.WHITE + s.getOwnerName());
						break;
					case OTHER:
						sender.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.DARK_RED + "Other");
						break;
					case UNKNOWN:
					default:
						sender.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.DARK_RED + "Unknown");
					}
					
					// Print location
					if(s.isLocationSet()) {
						
						// If the player is in the same world, show an 'distance' feature
						if(p.getWorld().getName().equals(s.getWorld())) {
							if((int) (s.getLocation(plugin.getServer()).distance(p.getLocation())) == 1)
								sender.sendMessage(ChatColor.GOLD + "Location: " + 
										ChatColor.YELLOW + "W: " + ChatColor.WHITE + s.getWorld() +
										ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(s.getX()) +
										ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(s.getY()) +
										ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(s.getZ()) +
											ChatColor.GRAY + " (1 block away)");
							else
								sender.sendMessage(ChatColor.GOLD + "Location: " + 
										ChatColor.YELLOW + "W: " + ChatColor.WHITE + s.getWorld() +
										ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(s.getX()) +
										ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(s.getY()) +
										ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(s.getZ()) +
											ChatColor.GRAY + " (" + String.valueOf((int) s.getLocation(plugin.getServer()).distance(p.getLocation())) + " blocks away)");
						} else
							sender.sendMessage(ChatColor.GOLD + "Location: " + 
									ChatColor.YELLOW + "W: " + ChatColor.WHITE + s.getWorld() +
									ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(s.getX()) +
									ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(s.getY()) +
									ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(s.getZ()));
					} else
						sender.sendMessage(ChatColor.GOLD + "Location: " + ChatColor.DARK_RED + "Not set");
					
					// Print product settings
					if(s.isProductSet()) {
						
						switch (s.getProductType()) {
						case ITEM:
							sender.sendMessage(ChatColor.GOLD + "Product: " + ChatColor.YELLOW + "Item: " +
									ChatColor.WHITE + String.valueOf(s.getProductItemTypeId()) + ChatColor.GRAY + " : " + ChatColor.WHITE + String.valueOf(s.getProductItemData()));
							break;
						case MAGIC_SPELLS:
							sender.sendMessage(ChatColor.GOLD + "Product: " + ChatColor.YELLOW + "MagicSpell: " +
									ChatColor.WHITE + s.getProductName());
							break;
						case API:
							sender.sendMessage(ChatColor.GOLD + "Product: " + ChatColor.YELLOW + "API: " +
									ChatColor.WHITE + s.getProductApiPlugin());
							break;
						case UNKNOWN:
						default:
							sender.sendMessage(ChatColor.GOLD + "Product: " + ChatColor.DARK_RED + "Not set!");
						}
						
					} else
						sender.sendMessage(ChatColor.GOLD + "Product: " + ChatColor.DARK_RED + "Not set!");
					
					// Pricelist info
					if(s.usesPricelist()) {
						if(plugin.getPricelistManager().isPricelistItemWithName(s.getPricelistName(), s.getPricelistItemName()))
							sender.sendMessage(ChatColor.GOLD + "Pricelist Item: " + ChatColor.YELLOW + s.getPricelistName() + ChatColor.GRAY + " : " + ChatColor.YELLOW + s.getPricelistItemName());
						else
							sender.sendMessage(ChatColor.GOLD + "Pricelist Item: " + ChatColor.YELLOW + s.getPricelistName() + ChatColor.GRAY + " : " + ChatColor.YELLOW + s.getPricelistItemName() +
									ChatColor.GRAY + " (not available, or not loaded!)");
					} else
						sender.sendMessage(ChatColor.GOLD + "Pricelist Item: " + ChatColor.DARK_RED + "None");
					
					// Print show item settings
					if(s.showItem()) {
						if(s.isShowItemSpawned())
							sender.sendMessage(ChatColor.GOLD + "Show item: " + ChatColor.YELLOW + String.valueOf(s.getShowItemTypeId()) + ChatColor.GRAY + " : " + ChatColor.YELLOW + String.valueOf(s.getShowItemData()) +
									ChatColor.GRAY + " (spawned)");
						else
							sender.sendMessage(ChatColor.GOLD + "Show item: " + ChatColor.YELLOW + String.valueOf(s.getShowItemTypeId()) + ChatColor.GRAY + " : " + ChatColor.YELLOW + String.valueOf(s.getShowItemData()) +
									ChatColor.GRAY + " (not spawned)");
					} else
						sender.sendMessage(ChatColor.GOLD + "Show item: " + ChatColor.DARK_RED + "No");
					
					// Print buy/sell ability
					if(s.canBuy())
						sender.sendMessage(ChatColor.GOLD + "Can Buy: " + ChatColor.GREEN + "Yes");
					else
						sender.sendMessage(ChatColor.GOLD + "Can Buy: " + ChatColor.DARK_RED + "No");
					
					if(s.canSell())
						sender.sendMessage(ChatColor.GOLD + "Can Sell: " + ChatColor.GREEN + "Yes");
					else
						sender.sendMessage(ChatColor.GOLD + "Can Sell: " + ChatColor.DARK_RED + "No");
					
					// Print prices
					if(s.canBuy())
						sender.sendMessage(ChatColor.GOLD + "Buy Price: " + ChatColor.YELLOW + s.getBuyPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getBuyPrice(), "Money"));
					else
						sender.sendMessage(ChatColor.GOLD + "Buy Price: " + ChatColor.DARK_RED + "Product can't be bought");
					
					if(s.canSell())
						sender.sendMessage(ChatColor.GOLD + "Sell Price: " + ChatColor.YELLOW + s.getSellPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getSellPrice(), "Money"));
					else
						sender.sendMessage(ChatColor.GOLD + "Sell Price: " + ChatColor.DARK_RED + "Product can't be sold");
					
					// Print instant buy
					if(s.instantBuyEnabled())
						sender.sendMessage(ChatColor.GOLD + "Instant Buy: " + ChatColor.YELLOW + "Quantity: " + ChatColor.WHITE + String.valueOf(s.getInstantBuyQuantity()));
					else
						sender.sendMessage(ChatColor.GOLD + "Instant Buy: " + ChatColor.DARK_RED + "Disabled");
					if(s.instantSellEnabled())
						sender.sendMessage(ChatColor.GOLD + "Instant Sell: " + ChatColor.YELLOW + "Quantity: " + ChatColor.WHITE + String.valueOf(s.getInstantSellQuantity()));
					else
						sender.sendMessage(ChatColor.GOLD + "Instant Sell: " + ChatColor.DARK_RED + "Disabled");
					
					// Buy/sell discount
					if(s.getBuyDiscount() != 0)
						sender.sendMessage(ChatColor.GOLD + "Buy Discount: " + ChatColor.YELLOW + String.valueOf(s.getBuyDiscount()) +
								ChatColor.GRAY + " (min quantity: " + String.valueOf(s.getBuyDiscountMinQuantity()) + ")");
					else
						sender.sendMessage(ChatColor.GOLD + "Buy Discount: " + ChatColor.DARK_RED + "None");
					if(s.getSellFee() != 0)
						sender.sendMessage(ChatColor.GOLD + "Sell Fee: " + ChatColor.YELLOW + String.valueOf(s.getSellFee()) +
								ChatColor.GRAY + " (min quantity: " + String.valueOf(s.getSellFeeMinQuantity()) + ")");
					else
						sender.sendMessage(ChatColor.GOLD + "Sell Fee: " + ChatColor.DARK_RED + "None");
					
					// Print permissions
					if(s.usesPermissionNode())
						sender.sendMessage(ChatColor.GOLD + "Permission Node: " + ChatColor.YELLOW + s.getPermissionNode());
					else
						sender.sendMessage(ChatColor.GOLD + "Permission Node: " + ChatColor.DARK_RED + "Not set");
					
					// Print 'remove item when empty'
					if(s.removeShopWhenEmptyEnabled())
						sender.sendMessage(ChatColor.GOLD + "Remove shop when empty: " + ChatColor.GREEN + "Yes");
					else
						sender.sendMessage(ChatColor.GOLD + "Remove shop when empty: " + ChatColor.DARK_RED + "No");
					return true;
					
				} else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("delete")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.remove")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Remove and deselect the shop
					plugin.getShopManager().removeShop(plugin.getShopManager().getSelectedShop(p.getName()));
					plugin.getShopManager().deselectShop(p.getName());

					sender.sendMessage(ChatColor.GREEN + "The shop has been removed and deselected!");
					return true;
					
				} else if(args[1].equalsIgnoreCase("setname") || args[1].equalsIgnoreCase("sn") || args[1].equalsIgnoreCase("name") || args[1].equalsIgnoreCase("rename")) {
					
					// Check if there are enough arguments
					if(args.length < 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setname")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Is the name valid
					String newName = args[2].toString();
					
					if(newName.equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + newName);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid name");
						return true;
					}
					
					// The name has to be different
					if(plugin.getShopManager().getSelectedShop(p.getName()).getName().equals(newName)) {
						sender.sendMessage(ChatColor.DARK_RED + newName);
						sender.sendMessage(ChatColor.DARK_RED + "The name has to be different!");
						return true;
					}
					
					// Is the name already used?
					if(!plugin.getShopManager().isUniqueServerOwnedName(newName, false)) {
						sender.sendMessage(ChatColor.DARK_RED + newName);
						sender.sendMessage(ChatColor.DARK_RED + "This name is already used by another shop");
						return true;
					}

					// Set the name of the portal
					plugin.getShopManager().getSelectedShop(p.getName()).setName(newName);

					sender.sendMessage(ChatColor.GREEN + "The shop has been renamed to " + ChatColor.GOLD + newName);
					return true;
					
				} else if(args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("e")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.enable")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).isEnabled()) {
						// The shop is already enabled
						sender.sendMessage(ChatColor.GREEN + "The shop is already enabled");
						return true;
					} else {
						// Enable the shop
						plugin.getShopManager().getSelectedShop(p.getName()).setEnabled(true);
						
						// Spawn the show item if needed
						if(plugin.getShopManager().getSelectedShop(p.getName()).showItem())
							plugin.getShopManager().getSelectedShop(p.getName()).spawnShowItem(plugin.getServer());
						
						sender.sendMessage(ChatColor.GREEN + "The shop has been enabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("d")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disable")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(!plugin.getShopManager().getSelectedShop(p.getName()).isEnabled()) {
						// The shop is already disabled
						sender.sendMessage(ChatColor.GREEN + "The shop is already disabled");
						return true;
					} else {
						// Disable the shop
						plugin.getShopManager().getSelectedShop(p.getName()).setEnabled(false);
						
						// Remove the show item if it's spawned
						if(plugin.getShopManager().getSelectedShop(p.getName()).isShowItemSpawned())
							plugin.getShopManager().getSelectedShop(p.getName()).removeShowItem();
						
						sender.sendMessage(ChatColor.GREEN + "The shop has been disabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setowner") || args[1].equalsIgnoreCase("so") || args[1].equalsIgnoreCase("owner")) {
					
					// Check if there are enough arguments
					if(args.length < 3 || args.length > 4) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setowner")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					SSShopOwnerType ownerType = null;
					String ownerName = "";
					
					if(args[2].equalsIgnoreCase("server") || args[2].equalsIgnoreCase("s")) {
						ownerType = SSShopOwnerType.SERVER;
					} else if(args[2].equalsIgnoreCase("player") || args[2].equalsIgnoreCase("p")) {
						ownerType = SSShopOwnerType.PLAYER;
					} else {
						// No valid owner type, show an error message
						sender.sendMessage(ChatColor.DARK_RED + "Invalid owner. Use one of these:");
						sender.sendMessage(ChatColor.YELLOW + " - Server");
						sender.sendMessage(ChatColor.YELLOW + " - Player [player-name]");
						return true;
					}
					
					boolean ownerNameSet = (args.length >= 4);
					
					switch (ownerType) {
					case SERVER:
						// Because the ownertype is 'server' don't save the additional owner name
						break;
					case PLAYER:
						// The player owner type needs to have a player name
						if(!ownerNameSet) {
							sender.sendMessage(ChatColor.DARK_RED + "No player name!");
							return true;
						} else
							ownerName = args[3];
						break;
					default:
						// If a player name has been entered, put it into the variable
						if(ownerNameSet)
							ownerName = args[3];
						break;
					}
					
					// The new owner type and name has to be different
					if(plugin.getShopManager().getSelectedShop(p.getName()).getOwnerType().equals(ownerType) &&
							plugin.getShopManager().getSelectedShop(p.getName()).getOwnerName().equals(ownerName)) {
						if(ownerNameSet)
							sender.sendMessage(ChatColor.DARK_RED + args[2] + " " + args[3]);
						else 
							sender.sendMessage(ChatColor.DARK_RED + args[2]);
						sender.sendMessage(ChatColor.DARK_RED + "You've to use a different owner!");
						return true;
					}
					
					// Set the owner type of the shop and the owner name if entered
					plugin.getShopManager().getSelectedShop(p.getName()).setOwnerType(ownerType);
					if(ownerNameSet)
						plugin.getShopManager().getSelectedShop(p.getName()).setOwnerName(ownerName);
					else
						plugin.getShopManager().getSelectedShop(p.getName()).setOwnerName("");

					// Put the player in the shop selection mode and return a message
					sender.sendMessage("");
					sender.sendMessage(ChatColor.GREEN + "The owner has been modified!");
					return true;
					
				} else if(args[1].equalsIgnoreCase("setlocation") || args[1].equalsIgnoreCase("setloc") || args[1].equalsIgnoreCase("sl") ||
						args[1].equalsIgnoreCase("location") || args[1].equalsIgnoreCase("loc") || args[1].equalsIgnoreCase("l") ||
						args[1].equalsIgnoreCase("setblock") || args[1].equalsIgnoreCase("sb") ||
						args[1].equalsIgnoreCase("block") ||
						args[1].equalsIgnoreCase("setshop") || args[1].equalsIgnoreCase("ss") ||
						args[1].equalsIgnoreCase("shop")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setlocation")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					// Put the player in the shop selection mode and return a message
					sender.sendMessage("");
					plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_LOCATION_SELECTION, true);
					sender.sendMessage(ChatColor.GREEN + "Left click a block to put the shop on");
					sender.sendMessage(ChatColor.GREEN + "Right click in the air to get out of the selection mode");
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("setproduct") || args[1].equalsIgnoreCase("setprod") || args[1].equalsIgnoreCase("sp") ||
						args[1].equalsIgnoreCase("product") || args[1].equalsIgnoreCase("prod") || args[1].equalsIgnoreCase("p") ||
						args[1].equalsIgnoreCase("setmerchandise") || args[1].equalsIgnoreCase("setmerch") || args[1].equalsIgnoreCase("sm") ||
						args[1].equalsIgnoreCase("merchandise") || args[1].equalsIgnoreCase("merch")) {
					
					// Check if there are enough arguments
					if(args.length < 2 || args.length > 4) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setproduct")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(args.length == 2) {
						// No extra arguments, use the 'hold item in hand' way
						// Put the player in the shop selection mode and return a message
						sender.sendMessage("");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PRODUCT_SELECTION, true);
						sender.sendMessage(ChatColor.GREEN + "Hold the product you want to use in your hand");
						sender.sendMessage(ChatColor.GREEN + "Left click to use your current item as product");
						sender.sendMessage(ChatColor.GREEN + "Right click to get out of the product selection mode");
						return true;
					} else if(args.length == 3) {
						// Wrong amount of arguments!
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					} else if(args.length == 4) {
						
						// CHeck if the 4th argument is valid
						if(args[2].equalsIgnoreCase("item") || args[2].equalsIgnoreCase("i")) {
							
							String itemArg = args[3].toString();
							
							// Check if the argument contains a ':' for data values
							if(itemArg.contains(":")) {
								// The argument contains a data value
								if(itemArg.split(":").length != 2) {
									sender.sendMessage(ChatColor.DARK_RED + itemArg);
									sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid item id (and data value)");
									return true;
								}
								
								String itemId = itemArg.split(":")[0];
								String itemData = itemArg.split(":")[1];
								
								if(!isInt(itemId)) {
									sender.sendMessage(ChatColor.DARK_RED + itemId + ChatColor.GRAY + " : " + itemData);
									sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid item id");
									return true;
								}
								
								if(!isInt(itemData)) {
									sender.sendMessage(ChatColor.GRAY + itemId + " : " + ChatColor.DARK_RED + itemData);
									sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid item data value");
									return true;
								}

								int typeId = Integer.parseInt(itemId);
								
								if(typeId == 0) {
									sender.sendMessage(ChatColor.DARK_RED + itemId);
									sender.sendMessage(ChatColor.DARK_RED + "You can't sell air!");
									return true;
								} else if(typeId < 0) {
									sender.sendMessage(ChatColor.DARK_RED + itemId + ChatColor.GRAY + " : " + itemData);
									sender.sendMessage(ChatColor.DARK_RED + "Invalid item id!");
									return true;
								}
								
								byte data = (byte) Integer.parseInt(itemData);
								plugin.getShopManager().getSelectedShop(p.getName()).setProductType(SSShopProductType.ITEM);
								plugin.getShopManager().getSelectedShop(p.getName()).setProductItemTypeId(typeId, plugin.getServer());
								plugin.getShopManager().getSelectedShop(p.getName()).setProductItemData(data, plugin.getServer());
								
								sender.sendMessage(ChatColor.GREEN + "The product has been changed!");
								return true;
								
							} else {
								// The argument doesn't contain a data value
								if(!isInt(itemArg)) {
									sender.sendMessage(ChatColor.DARK_RED + itemArg);
									sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid item id");
									return true;
								}
								
								int typeId = Integer.parseInt(itemArg);
								
								if(typeId == 0) {
									sender.sendMessage(ChatColor.DARK_RED + String.valueOf(typeId));
									sender.sendMessage(ChatColor.DARK_RED + "You can't sell air!");
									return true;
								} else if(typeId < 0) {
									sender.sendMessage(ChatColor.DARK_RED + String.valueOf(typeId));
									sender.sendMessage(ChatColor.DARK_RED + "Invalid item id!");
									return true;
								}
								
								plugin.getShopManager().getSelectedShop(p.getName()).setProductType(SSShopProductType.ITEM);
								plugin.getShopManager().getSelectedShop(p.getName()).setProductItemTypeId(typeId, plugin.getServer());
								plugin.getShopManager().getSelectedShop(p.getName()).setProductItemData((byte) 0, plugin.getServer());
								
								sender.sendMessage(ChatColor.GREEN + "The product has been changed!");
								return true;
							}
							
						} else if(args[2].equalsIgnoreCase("magicspells") || args[2].equalsIgnoreCase("magicspell") ||
								args[2].equalsIgnoreCase("magic") || args[2].equalsIgnoreCase("spell") ||
								args[2].equalsIgnoreCase("ms")) {
							
							String spellName = args[3].toString();
							
							//TODO Check if spell exists
							
							// Check if any argument was entered (by trimming the spaces first!)
							if(spellName.trim().equals("")) {
								sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid spell name");
								return true;
							}
							
							plugin.getShopManager().getSelectedShop(p.getName()).setProductType(SSShopProductType.MAGIC_SPELLS);
							plugin.getShopManager().getSelectedShop(p.getName()).setProductName(spellName);
							
							sender.sendMessage(ChatColor.GREEN + "The product has been changed!");
							return true;
							
						} else {
							// Wrong amount of arguments!
							sender.sendMessage(ChatColor.DARK_RED + "Invalid product type! Use one of the following:");
							sender.sendMessage(ChatColor.YELLOW + " - item");
							sender.sendMessage(ChatColor.YELLOW + " - item <item-id>");
							sender.sendMessage(ChatColor.YELLOW + " - item <item-id:item-data>");
							sender.sendMessage(ChatColor.YELLOW + " - magicspell <spell-name>");
							sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
							return true;
						}
						
					} else {
						// Wrong amount of arguments!
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("linkpricelistitem") || args[1].equalsIgnoreCase("linkpricelist") ||
						args[1].equalsIgnoreCase("lpli") ||args[1].equalsIgnoreCase("lpl") ||
						args[1].equalsIgnoreCase("setpricelistitem") || args[1].equalsIgnoreCase("setpricelist") ||
						args[1].equalsIgnoreCase("spli") ||args[1].equalsIgnoreCase("spl") ||
						args[1].equalsIgnoreCase("pricelistitem") || args[1].equalsIgnoreCase("pricelist") ||
						args[1].equalsIgnoreCase("pli") ||args[1].equalsIgnoreCase("pl")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.linkpricelistitem")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					String value = args[2];
					List<String> values = Arrays.asList(value.split(":"));
					
					if(values.size() == 1) {
						if(plugin.getPricelistManager().countItemsWithName(values.get(0)) == 0) {
							sender.sendMessage(ChatColor.DARK_RED + values.get(0));
							sender.sendMessage(ChatColor.DARK_RED + "There's no pricelist item with this name!");
							return true;
						} else if(plugin.getPricelistManager().countItemsWithName(values.get(0)) == 1) {
							plugin.getShopManager().getSelectedShop(p.getName()).linkPricelistItem(plugin.getPricelistManager().getItem(values.get(0)));
							plugin.getPricelistManager().applyItemToShop(plugin.getPricelistManager().getItem(values.get(0)), plugin.getShopManager().getSelectedShop(p.getName()));
							sender.sendMessage(ChatColor.GREEN + "The pricelist item has been set to:");
							sender.sendMessage(ChatColor.YELLOW + plugin.getPricelistManager().getItem(values.get(0)).getPricelistName() + ChatColor.GRAY + " : " +
									ChatColor.YELLOW + plugin.getPricelistManager().getItem(values.get(0)).getItemName());
							return true;
						} else {
							int count = plugin.getPricelistManager().countItemsWithName(values.get(0));
							sender.sendMessage(ChatColor.DARK_RED + values.get(0));
							sender.sendMessage(ChatColor.DARK_RED + String.valueOf(count) + " matching items found, choose one of these:");
							for(SSPricelistItem entry : plugin.getPricelistManager().getItems(values.get(0))) {
								sender.sendMessage(ChatColor.GOLD + " - " + ChatColor.YELLOW + entry.getPricelistName() + ":" + entry.getItemName());
							}
							return true;
						}
					} else {
						String pricelistName = values.get(0);
						String itemName = values.get(1);
						
						if(!plugin.getPricelistManager().isPricelistWithName(pricelistName)) {
							sender.sendMessage(ChatColor.DARK_RED + pricelistName + ChatColor.GRAY + ":" + itemName);
							sender.sendMessage(ChatColor.DARK_RED + "There's no pricelist with this name!");
							return true;
						}
						
						if(!plugin.getPricelistManager().isPricelistItemWithName(pricelistName, itemName)) {
							sender.sendMessage(ChatColor.GRAY + pricelistName + ":" + ChatColor.DARK_RED + itemName);
							sender.sendMessage(ChatColor.DARK_RED + "There's no item with this name in this pricelist!");
							return true;
						}
						
						plugin.getShopManager().getSelectedShop(p.getName()).linkPricelistItem(plugin.getPricelistManager().getItem(pricelistName, itemName));
						plugin.getPricelistManager().applyItemToShop(plugin.getPricelistManager().getItem(pricelistName, itemName), plugin.getShopManager().getSelectedShop(p.getName()));
						sender.sendMessage(ChatColor.GREEN + "The pricelist item has been set to:");
						sender.sendMessage(ChatColor.YELLOW + plugin.getPricelistManager().getItem(pricelistName, itemName).getPricelistName() + ChatColor.GRAY + " : " +
								ChatColor.YELLOW + plugin.getPricelistManager().getItem(pricelistName, itemName).getItemName());
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("unlinkpricelistitem") || args[1].equalsIgnoreCase("unlinkpricelist") ||
						args[1].equalsIgnoreCase("upli") ||args[1].equalsIgnoreCase("upl") ||
						args[1].equalsIgnoreCase("removepricelistitem") || args[1].equalsIgnoreCase("removepricelist") ||
						args[1].equalsIgnoreCase("rpli") ||args[1].equalsIgnoreCase("rpl")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.unlinkpricelistitem")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).usesPricelist()) {
						plugin.getShopManager().getSelectedShop(p.getName()).unlinkPricelistItem();
						sender.sendMessage(ChatColor.GREEN + "The pricelist item has been unlinked!");
					} else
						sender.sendMessage(ChatColor.DARK_RED + "There's no pricelist linked with this shop!");
					return true;
					
				} else if(args[1].equalsIgnoreCase("setshowitem") || args[1].equalsIgnoreCase("setshow") || args[1].equalsIgnoreCase("ssi") ||
						args[1].equalsIgnoreCase("showitem") || args[1].equalsIgnoreCase("show")) {
					
					// Check if there are enough arguments
					if(args.length < 2 || args.length > 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setshowitem")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(args.length == 2) {
						// No extra arguments, use the 'hold item in hand' way
						// Put the player in the shop selection mode and return a message
						sender.sendMessage("");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_SHOW_ITEM_SELECTION, true);
						sender.sendMessage(ChatColor.GREEN + "Hold the item you want to use in your hand");
						sender.sendMessage(ChatColor.GREEN + "Left click to use your current item as show item");
						sender.sendMessage(ChatColor.GREEN + "Right click to get out of the show item selection mode");
						return true;
					} else if(args.length == 3) {
							
						String itemArg = args[2].toString();
						
						// Check if the argument contains a ':' for data values
						if(itemArg.contains(":")) {
							// The argument contains a data value
							if(itemArg.split(":").length != 2) {
								sender.sendMessage(ChatColor.DARK_RED + itemArg);
								sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid item id (and data value)");
								return true;
							}
							
							String itemId = itemArg.split(":")[0];
							String itemData = itemArg.split(":")[1];
							
							if(!isInt(itemId)) {
								sender.sendMessage(ChatColor.DARK_RED + itemId + ChatColor.GRAY + " : " + itemData);
								sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid item id");
								return true;
							}
							
							if(!isInt(itemData)) {
								sender.sendMessage(ChatColor.GRAY + itemId + " : " + ChatColor.DARK_RED + itemData);
								sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid item data value");
								return true;
							}

							int typeId = Integer.parseInt(itemId);
							byte data = (byte) Integer.parseInt(itemData);
							
							if(typeId == 0) {
								sender.sendMessage(ChatColor.DARK_RED + itemId);
								sender.sendMessage(ChatColor.DARK_RED + "You can't use air as show item!");
								return true;
							} else if(typeId < 0) {
								sender.sendMessage(ChatColor.DARK_RED + itemId + ChatColor.GRAY + " : " + itemData);
								sender.sendMessage(ChatColor.DARK_RED + "Invalid item id!");
								return true;
							}
							
							if(!plugin.getShopManager().getSelectedShop(p.getName()).showItem()) {
								plugin.getShopManager().getSelectedShop(p.getName()).showItem(true);
								sender.sendMessage(ChatColor.YELLOW + "The show item has been " + ChatColor.GREEN + "Enabled");
							}
							
							plugin.getShopManager().getSelectedShop(p.getName()).setShowItemTypeId(typeId);
							plugin.getShopManager().getSelectedShop(p.getName()).setShowItemData(data);
							
							sender.sendMessage(ChatColor.GREEN + "The product has been changed!");
							
							// Remove the old show item
							if(plugin.getShopManager().getSelectedShop(p.getName()).isShowItemSpawned())
								plugin.getShopManager().getSelectedShop(p.getName()).removeShowItem();
							
							// Respawn  the item if its needed
							if(plugin.getShopManager().getSelectedShop(p.getName()).shouldShowItemBeSpawned(plugin.getServer()))
								plugin.getShopManager().getSelectedShop(p.getName()).respawnShowItem(plugin.getServer());
							
							return true;
							
						} else {
							// The argument doesn't contain a data value
							if(!isInt(itemArg)) {
								sender.sendMessage(ChatColor.DARK_RED + itemArg);
								sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid item id");
								return true;
							}
							
							int typeId = Integer.parseInt(itemArg);
							
							if(typeId == 0) {
								sender.sendMessage(ChatColor.DARK_RED + String.valueOf(typeId));
								sender.sendMessage(ChatColor.DARK_RED + "You can't use air as show item!");
								return true;
							} else if(typeId < 0) {
								sender.sendMessage(ChatColor.DARK_RED + String.valueOf(typeId));
								sender.sendMessage(ChatColor.DARK_RED + "Invalid item id!");
								return true;
							}
							
							if(!plugin.getShopManager().getSelectedShop(p.getName()).showItem()) {
								plugin.getShopManager().getSelectedShop(p.getName()).showItem(true);
								sender.sendMessage(ChatColor.YELLOW + "The show item has been " + ChatColor.GREEN + "Enabled");
							}
							
							plugin.getShopManager().getSelectedShop(p.getName()).setShowItemTypeId(typeId);
							plugin.getShopManager().getSelectedShop(p.getName()).setShowItemData((byte) 0);
							
							sender.sendMessage(ChatColor.GREEN + "The product has been changed!");
							
							// Remove the old show item
							if(plugin.getShopManager().getSelectedShop(p.getName()).isShowItemSpawned())
								plugin.getShopManager().getSelectedShop(p.getName()).removeShowItem();
							
							// Respawn  the item if its needed
							if(plugin.getShopManager().getSelectedShop(p.getName()).shouldShowItemBeSpawned(plugin.getServer()))
								plugin.getShopManager().getSelectedShop(p.getName()).respawnShowItem(plugin.getServer());
							
							return true;
						}
						
					} else {
						// Wrong amount of arguments!
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("enableshowitem") || args[1].equalsIgnoreCase("enableshow") ||
						args[1].equalsIgnoreCase("esi") || args[1].equalsIgnoreCase("es")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.enableshowitem")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).showItem()) {
						// The shop is already enabled
						sender.sendMessage(ChatColor.GREEN + "The show item is already enabled");
						return true;
					} else {
						// Enable the shop
						plugin.getShopManager().getSelectedShop(p.getName()).showItem(true);
						
						// Spawn the show item
						if(plugin.getShopManager().getSelectedShop(p.getName()).shouldShowItemBeSpawned(plugin.getServer()))
							plugin.getShopManager().getSelectedShop(p.getName()).spawnShowItem(plugin.getServer());
						
						sender.sendMessage(ChatColor.GREEN + "The show item has been enabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("disableshowitem") || args[1].equalsIgnoreCase("disableshow") ||
						args[1].equalsIgnoreCase("dsi") || args[1].equalsIgnoreCase("ds")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disableshowitem")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(!plugin.getShopManager().getSelectedShop(p.getName()).showItem()) {
						// The shop is already disable
						sender.sendMessage(ChatColor.GREEN + "The show item is already disabled");
						return true;
					} else {
						// Disable the shop
						plugin.getShopManager().getSelectedShop(p.getName()).showItem(false);
						
						// Remove the show item
						if(plugin.getShopManager().getSelectedShop(p.getName()).isShowItemSpawned())
							plugin.getShopManager().getSelectedShop(p.getName()).removeShowItem();
						
						sender.sendMessage(ChatColor.GREEN + "The show item has been disabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("enablebuy") || args[1].equalsIgnoreCase("enablebuyable") || args[1].equalsIgnoreCase("eb")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.enablebuy")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).canBuy()) {
						// The shop is already enabled
						sender.sendMessage(ChatColor.GREEN + "The product was already buyable");
						return true;
					} else {
						// Make the product buyable
						plugin.getShopManager().getSelectedShop(p.getName()).setCanBuy(true);
						
						sender.sendMessage(ChatColor.GREEN + "The product is now buyable");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("disablebuy") || args[1].equalsIgnoreCase("disablebuyable") || args[1].equalsIgnoreCase("db")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disablebuy")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(!plugin.getShopManager().getSelectedShop(p.getName()).canBuy()) {
						// The shop is already disable
						sender.sendMessage(ChatColor.GREEN + "Players couldn't buy this product already");
						return true;
					} else {
						// Disable the shop
						plugin.getShopManager().getSelectedShop(p.getName()).setCanBuy(false);
						
						sender.sendMessage(ChatColor.GREEN + "Players can't buy this product anymore");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("enablesell") || args[1].equalsIgnoreCase("enablesellable") || args[1].equalsIgnoreCase("es")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.enablesell")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).canSell()) {
						// The product is already sellable
						sender.sendMessage(ChatColor.GREEN + "The product was already sellable");
						return true;
					} else {
						// Make the product sellable
						plugin.getShopManager().getSelectedShop(p.getName()).setCanSell(true);
						
						sender.sendMessage(ChatColor.GREEN + "The product is now sellable");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("disablesell") || args[1].equalsIgnoreCase("disablesell") || args[1].equalsIgnoreCase("ds")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disablesell")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(!plugin.getShopManager().getSelectedShop(p.getName()).canSell()) {
						// The shop is already disable
						sender.sendMessage(ChatColor.GREEN + "Players couldn't sell this product already");
						return true;
					} else {
						// Disable the shop
						plugin.getShopManager().getSelectedShop(p.getName()).setCanSell(false);
						
						sender.sendMessage(ChatColor.GREEN + "Players can't sell this product anymore");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setbuyprice") || args[1].equalsIgnoreCase("sbp")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setbuyprice")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Is the price valid
					String newPrice = args[2].toString();
					
					if(newPrice.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid price");
						return true;
					}
					
					// Is the price valid
					if(!isDouble(newPrice)) {
						sender.sendMessage(ChatColor.DARK_RED + newPrice);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid product price");
						return true;
					}
					
					double price = Double.parseDouble(newPrice);

					// The price may not be negative
					if(price < 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(price));
						sender.sendMessage(ChatColor.DARK_RED + "The price may not be negative");
						return true;
					}

					// Set the product buy price
					plugin.getShopManager().getSelectedShop(p.getName()).setBuyPrice(price);

					sender.sendMessage(ChatColor.GREEN + "The product buy price has been changed to " +
							ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
					return true;
					
				} else if(args[1].equalsIgnoreCase("setsellprice") || args[1].equalsIgnoreCase("ssp")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setsellprice")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Is the price valid
					String newPrice = args[2].toString();
					
					if(newPrice.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid price");
						return true;
					}
					
					// Is the price valid
					if(!isDouble(newPrice)) {
						sender.sendMessage(ChatColor.DARK_RED + newPrice);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid product price");
						return true;
					}
					
					double price = Double.parseDouble(newPrice);

					// The price may not be negative
					if(price < 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(price));
						sender.sendMessage(ChatColor.DARK_RED + "The price may not be negative");
						return true;
					}
					
					// Set the product buy price
					plugin.getShopManager().getSelectedShop(p.getName()).setSellPrice(price);

					sender.sendMessage(ChatColor.GREEN + "The product sell price has been changed to " +
							ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
					return true;
					
				} else if(args[1].equalsIgnoreCase("setbuystack") || args[1].equalsIgnoreCase("sbs") ||
						args[1].equalsIgnoreCase("setbuystackquantity") || args[1].equalsIgnoreCase("sbsq")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setbuystack")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Put the quantity argument into a variable
					String newQuantity = args[2].toString();
					
					// The player has enter the quantity
					if(newQuantity.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					// Could the quantity be casted to an integer
					if(!isInt(newQuantity)) {
						sender.sendMessage(ChatColor.DARK_RED + newQuantity);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid product quantity");
						return true;
					}
					
					int quantity = Integer.parseInt(newQuantity);

					// The quantity must be 1 or more
					if(quantity <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The stack quantity has to be 1 or higher");
						return true;
					}
					
					// Set the product buy stack quantity
					plugin.getShopManager().getSelectedShop(p.getName()).setBuyStack(quantity);

					sender.sendMessage(ChatColor.GREEN + "The product buy stack quantity has been set to " +
							ChatColor.GOLD + String.valueOf(quantity));
					return true;
					
				} else if(args[1].equalsIgnoreCase("disablebuystack") || args[1].equalsIgnoreCase("dbs")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disablebuystack")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(!plugin.getShopManager().getSelectedShop(p.getName()).buyInStacks()) {
						// The shop stacking is already disabled
						sender.sendMessage(ChatColor.GREEN + "This was already disabled");
						return true;
					} else {
						// Disable shop product stacking
						plugin.getShopManager().getSelectedShop(p.getName()).setBuyStack(1);
						
						sender.sendMessage(ChatColor.GREEN + "The product won't be bought in stacks anymore");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setsellstack") || args[1].equalsIgnoreCase("sss") ||
						args[1].equalsIgnoreCase("setsellstackquantity") || args[1].equalsIgnoreCase("sssq")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setsellstack")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Put the quantity argument into a variable
					String newQuantity = args[2].toString();
					
					// The player has enter the quantity
					if(newQuantity.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					// Could the quantity be casted to an integer
					if(!isInt(newQuantity)) {
						sender.sendMessage(ChatColor.DARK_RED + newQuantity);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid product quantity");
						return true;
					}
					
					int quantity = Integer.parseInt(newQuantity);

					// The quantity must be 1 or more
					if(quantity <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The stack quantity has to be 1 or higher");
						return true;
					}
					
					// Set the shop sell stack quantity
					plugin.getShopManager().getSelectedShop(p.getName()).setSellStack(quantity);

					sender.sendMessage(ChatColor.GREEN + "The product sell stack quantity has been set to " +
							ChatColor.GOLD + String.valueOf(quantity));
					return true;
					
				} else if(args[1].equalsIgnoreCase("disablesellstack") || args[1].equalsIgnoreCase("dss")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disablesellstack")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(!plugin.getShopManager().getSelectedShop(p.getName()).sellInStacks()) {
						// The shop stacking is already disabled
						sender.sendMessage(ChatColor.GREEN + "This was already disabled");
						return true;
					} else {
						// Disable shop product stacking
						plugin.getShopManager().getSelectedShop(p.getName()).setSellStack(1);
						
						sender.sendMessage(ChatColor.GREEN + "The product won't be sold in stacks anymore");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setminimumbuyquantity") || args[1].equalsIgnoreCase("setminbuyquantity") || args[1].equalsIgnoreCase("smbq") ||
						args[1].equalsIgnoreCase("setminimumbuy") || args[1].equalsIgnoreCase("setminbuy")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setminimumbuyquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Put the quantity argument into a variable
					String newQuantity = args[2].toString();
					
					// The player has enter the quantity
					if(newQuantity.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					// Could the quantity be casted to an integer
					if(!isInt(newQuantity)) {
						sender.sendMessage(ChatColor.DARK_RED + newQuantity);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					int quantity = Integer.parseInt(newQuantity);

					// The quantity must be 1 or more
					if(quantity <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be 1 or higher");
						return true;
					}
					
					// The quantity has to be less or the same then the max buy quantity
					if(quantity > plugin.getShopManager().getSelectedShop(p.getName()).getMaxBuyQuantity() &&
							plugin.getShopManager().getSelectedShop(p.getName()).getMaxBuyQuantity() != -1) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be the same or less than the maximum buy quantity");
						return true;
					}
					
					// Set the shop sell stack quantity
					plugin.getShopManager().getSelectedShop(p.getName()).setMinBuyQuantity(quantity);

					sender.sendMessage(ChatColor.GREEN + "The product minimum buy quantity has been set to " +
							ChatColor.GOLD + String.valueOf(quantity));
					return true;
					
				} else if(args[1].equalsIgnoreCase("disableminimumbuyquantity") || args[1].equalsIgnoreCase("disableminbuyquantity") || args[1].equalsIgnoreCase("dmbq") ||
						args[1].equalsIgnoreCase("disableminimumbuy") || args[1].equalsIgnoreCase("disableminbuy")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disableminimumbuyquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).getMinBuyQuantity() == -1) {
						// The minimum buy quantity was already disabled
						sender.sendMessage(ChatColor.GREEN + "The minimum buy quantity was already disabled");
						return true;
					} else {
						// Disable the minimum buy quantity
						plugin.getShopManager().getSelectedShop(p.getName()).setMinBuyQuantity(-1);
						
						sender.sendMessage(ChatColor.GREEN + "The minimum buy quantity has been disabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setmaximumbuyquantity") || args[1].equalsIgnoreCase("setmaxbuyquantity") || args[1].equalsIgnoreCase("smbq") ||
						args[1].equalsIgnoreCase("setmaximumbuy") || args[1].equalsIgnoreCase("setmaxbuy")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setmaximumbuyquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Put the quantity argument into a variable
					String newQuantity = args[2].toString();
					
					// The player has enter the quantity
					if(newQuantity.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					// Could the quantity be casted to an integer
					if(!isInt(newQuantity)) {
						sender.sendMessage(ChatColor.DARK_RED + newQuantity);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					int quantity = Integer.parseInt(newQuantity);

					// The quantity must be 1 or more
					if(quantity <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be 1 or higher");
						return true;
					}
					
					// The quantity has to be less or the same then the max buy quantity
					if(quantity < plugin.getShopManager().getSelectedShop(p.getName()).getMinBuyQuantity() &&
							plugin.getShopManager().getSelectedShop(p.getName()).getMinBuyQuantity() != -1) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be the same or more than the minimum buy quantity");
						return true;
					}
					
					// Set the shop sell stack quantity
					plugin.getShopManager().getSelectedShop(p.getName()).setMaxBuyQuantity(quantity);

					sender.sendMessage(ChatColor.GREEN + "The product maximum buy quantity has been set to " +
							ChatColor.GOLD + String.valueOf(quantity));
					return true;
					
				} else if(args[1].equalsIgnoreCase("disablemaximumbuyquantity") || args[1].equalsIgnoreCase("disablemaxbuyquantity") || args[1].equalsIgnoreCase("dmbq") ||
						args[1].equalsIgnoreCase("disablemaximumbuy") || args[1].equalsIgnoreCase("disablemaxbuy")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disablemaximumbuyquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).getMaxBuyQuantity() == -1) {
						// The maximum buy quantity was already disabled
						sender.sendMessage(ChatColor.GREEN + "The maximum buy quantity was already disabled");
						return true;
					} else {
						// Disable the maximum buy quantity
						plugin.getShopManager().getSelectedShop(p.getName()).setMaxBuyQuantity(-1);
						
						sender.sendMessage(ChatColor.GREEN + "The maximum buy quantity has been disabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setminimumsellquantity") || args[1].equalsIgnoreCase("setminsellquantity") || args[1].equalsIgnoreCase("smsq") ||
						args[1].equalsIgnoreCase("setminimumsell") || args[1].equalsIgnoreCase("setminsell")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setminimumsellquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Put the quantity argument into a variable
					String newQuantity = args[2].toString();
					
					// The player has enter the quantity
					if(newQuantity.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					// Could the quantity be casted to an integer
					if(!isInt(newQuantity)) {
						sender.sendMessage(ChatColor.DARK_RED + newQuantity);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					int quantity = Integer.parseInt(newQuantity);

					// The quantity must be 1 or more
					if(quantity <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be 1 or higher");
						return true;
					}
					
					// The quantity has to be less or the same then the max sell quantity
					if(quantity > plugin.getShopManager().getSelectedShop(p.getName()).getMaxSellQuantity() &&
							plugin.getShopManager().getSelectedShop(p.getName()).getMaxSellQuantity() != -1) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be the same or less than the maximum sell quantity");
						return true;
					}
					
					// Set the shop sell stack quantity
					plugin.getShopManager().getSelectedShop(p.getName()).setMinSellQuantity(quantity);

					sender.sendMessage(ChatColor.GREEN + "The product minimum sell quantity has been set to " +
							ChatColor.GOLD + String.valueOf(quantity));
					return true;
					
				} else if(args[1].equalsIgnoreCase("disableminimumsellquantity") || args[1].equalsIgnoreCase("disableminsellquantity") || args[1].equalsIgnoreCase("dmbq") ||
						args[1].equalsIgnoreCase("disableminimumsell") || args[1].equalsIgnoreCase("disableminsell")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disableminimumsellquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).getMinSellQuantity() == -1) {
						// The minimum sell quantity was already disabled
						sender.sendMessage(ChatColor.GREEN + "The minimum sell quantity was already disabled");
						return true;
					} else {
						// Disable the minimum sell quantity
						plugin.getShopManager().getSelectedShop(p.getName()).setMinSellQuantity(-1);
						
						sender.sendMessage(ChatColor.GREEN + "The minimum sell quantity has been disabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setmaximumsellquantity") || args[1].equalsIgnoreCase("setmaxsellquantity") || args[1].equalsIgnoreCase("smbq") ||
						args[1].equalsIgnoreCase("setmaximumsell") || args[1].equalsIgnoreCase("setmaxsell")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setmaximumsellquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Put the quantity argument into a variable
					String newQuantity = args[2].toString();
					
					// The player has enter the quantity
					if(newQuantity.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					// Could the quantity be casted to an integer
					if(!isInt(newQuantity)) {
						sender.sendMessage(ChatColor.DARK_RED + newQuantity);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					int quantity = Integer.parseInt(newQuantity);

					// The quantity must be 1 or more
					if(quantity <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be 1 or higher");
						return true;
					}
					
					// The quantity has to be less or the same then the max sell quantity
					if(quantity < plugin.getShopManager().getSelectedShop(p.getName()).getMinSellQuantity() &&
							plugin.getShopManager().getSelectedShop(p.getName()).getMinSellQuantity() != -1) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be the same or more than the minimum sell quantity");
						return true;
					}
					
					// Set the shop sell stack quantity
					plugin.getShopManager().getSelectedShop(p.getName()).setMaxSellQuantity(quantity);

					sender.sendMessage(ChatColor.GREEN + "The product maximum sell quantity has been set to " +
							ChatColor.GOLD + String.valueOf(quantity));
					return true;
					
				} else if(args[1].equalsIgnoreCase("disablemaximumsellquantity") || args[1].equalsIgnoreCase("disablemaxsellquantity") || args[1].equalsIgnoreCase("dmbq") ||
						args[1].equalsIgnoreCase("disablemaximumsell") || args[1].equalsIgnoreCase("disablemaxsell")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disablemaximumsellquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).getMaxSellQuantity() == -1) {
						// The maximum sell quantity was already disabled
						sender.sendMessage(ChatColor.GREEN + "The maximum sell quantity was already disabled");
						return true;
					} else {
						// Disable the maximum sell quantity
						plugin.getShopManager().getSelectedShop(p.getName()).setMaxSellQuantity(-1);
						
						sender.sendMessage(ChatColor.GREEN + "The maximum sell quantity has been disabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("enableinstantbuy") || args[1].equalsIgnoreCase("eib")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.enableinstantbuy")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).instantBuyEnabled()) {
						// The shop instant buy is already enabled
						sender.sendMessage(ChatColor.GREEN + "Instant buy is already enabled");
						return true;
					} else {
						// Enable instant buy
						plugin.getShopManager().getSelectedShop(p.getName()).instantBuyEnabled(true);
						
						sender.sendMessage(ChatColor.GREEN + "Instant buy has been enabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("disableinstantbuy") || args[1].equalsIgnoreCase("dib")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disableinstantbuy")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(!plugin.getShopManager().getSelectedShop(p.getName()).instantBuyEnabled()) {
						// The shop instant buy is already enabled
						sender.sendMessage(ChatColor.GREEN + "Instant buy is already disabled");
						return true;
					} else {
						// Enable instant buy
						plugin.getShopManager().getSelectedShop(p.getName()).instantBuyEnabled(false);
						
						sender.sendMessage(ChatColor.GREEN + "Instant buy has been disabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setinstantbuyquantity") || args[1].equalsIgnoreCase("setinstantbuy") ||
						args[1].equalsIgnoreCase("sibq") || args[1].equalsIgnoreCase("sib")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setinstantbuyquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Put the quantity argument into a variable
					String newQuantity = args[2].toString();
					
					// The player has enter the quantity
					if(newQuantity.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					// Could the quantity be casted to an integer
					if(!isInt(newQuantity)) {
						sender.sendMessage(ChatColor.DARK_RED + newQuantity);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					int quantity = Integer.parseInt(newQuantity);

					// The quantity has to be 1 or more
					if(quantity <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be 1 or higher");
						return true;
					}
					
					// Enable the instant buy feature if it was disabled
					if(!plugin.getShopManager().getSelectedShop(p.getName()).instantBuyEnabled()) {
						plugin.getShopManager().getSelectedShop(p.getName()).instantBuyEnabled(true);
						sender.sendMessage(ChatColor.YELLOW + "The instant buy feature has been " + ChatColor.GREEN + "Enabled");
					}
					
					// Set the shop instant buy quantity
					plugin.getShopManager().getSelectedShop(p.getName()).setInstantBuyQuantity(quantity);

					sender.sendMessage(ChatColor.GREEN + "The instant buy quantity has been changed to " +
							ChatColor.GOLD + String.valueOf(quantity));
					return true;
					
				} else if(args[1].equalsIgnoreCase("enableinstantsell") || args[1].equalsIgnoreCase("eis")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.enableinstantsell")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(plugin.getShopManager().getSelectedShop(p.getName()).instantSellEnabled()) {
						// The shop instant sell is already enabled
						sender.sendMessage(ChatColor.GREEN + "Instant sell is already enabled");
						return true;
					} else {
						// Enable instant sell
						plugin.getShopManager().getSelectedShop(p.getName()).instantSellEnabled(true);
						
						sender.sendMessage(ChatColor.GREEN + "Instant sell has been enabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("disableinstantsell") || args[1].equalsIgnoreCase("dis")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.disableinstantsell")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}

					if(!plugin.getShopManager().getSelectedShop(p.getName()).instantSellEnabled()) {
						// The shop instant sell is already enabled
						sender.sendMessage(ChatColor.GREEN + "Instant sell is already disabled");
						return true;
					} else {
						// Enable instant sell
						plugin.getShopManager().getSelectedShop(p.getName()).instantSellEnabled(false);
						
						sender.sendMessage(ChatColor.GREEN + "Instant sell has been disabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("setinstantsellquantity") || args[1].equalsIgnoreCase("setinstantbuy") ||
						args[1].equalsIgnoreCase("sibq") || args[1].equalsIgnoreCase("sib")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setinstantsellquantity")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Put the quantity argument into a variable
					String newQuantity = args[2].toString();
					
					// The player has enter the quantity
					if(newQuantity.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					// Could the quantity be casted to an integer
					if(!isInt(newQuantity)) {
						sender.sendMessage(ChatColor.DARK_RED + newQuantity);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid quantity");
						return true;
					}
					
					int quantity = Integer.parseInt(newQuantity);

					// The quantity has to be 1 or more
					if(quantity <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(quantity));
						sender.sendMessage(ChatColor.DARK_RED + "The quantity has to be 1 or higher");
						return true;
					}
					
					// Enable the instant buy feature if it was disabled
					if(!plugin.getShopManager().getSelectedShop(p.getName()).instantSellEnabled()) {
						plugin.getShopManager().getSelectedShop(p.getName()).instantSellEnabled(true);
						sender.sendMessage(ChatColor.YELLOW + "The instant buy feature has been " + ChatColor.GREEN + "Enabled");
					}
					
					// Set the shop instant buy quantity
					plugin.getShopManager().getSelectedShop(p.getName()).setInstantSellQuantity(quantity);

					sender.sendMessage(ChatColor.GREEN + "The instant buy quantity has been changed to " +
							ChatColor.GOLD + String.valueOf(quantity));
					return true;
					
				} else if(args[1].equalsIgnoreCase("setbuydiscount") || args[1].equalsIgnoreCase("buydiscount") || args[1].equalsIgnoreCase("sbd")) {
					
					sender.sendMessage(ChatColor.DARK_RED + "This feature will be available in an upcomming version!");
					return true;
					
					/* // Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.setbuydiscount")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getShopManager().hasShopSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " shop select <name>");
						return true;
					}
					
					// Is the price valid
					String newDiscount = args[2].toString();
					
					if(newDiscount.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid discount");
						return true;
					}
					
					// Is the price valid
					if(!isDouble(newDiscount)) {
						sender.sendMessage(ChatColor.DARK_RED + newDiscount);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid product buy discount");
						return true;
					}
					
					double discount = Double.parseDouble(newDiscount);

					// The price may not be negative
					if(discount <= 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(discount));
						sender.sendMessage(ChatColor.DARK_RED + "The discount may not be zero or negative");
						return true;
					}

					// Set the product buy price
					plugin.getShopManager().getSelectedShop(p.getName()).setBuyDiscount(discount);

					sender.sendMessage(ChatColor.GREEN + "The product buy price has been changed to " +
							ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(discount, true));
					return true;*/
					
				} else if(args[1].equalsIgnoreCase("disablebuydiscount") || args[1].equalsIgnoreCase("dbd")) {
					
					sender.sendMessage(ChatColor.DARK_RED + "This feature will be available in an upcomming version!");
					return true;
					
				} else if(args[1].equalsIgnoreCase("setbuydiscountminimumquantity") || args[1].equalsIgnoreCase("setbuydiscountminquantity") || args[1].equalsIgnoreCase("sbdmq")) {
					
					sender.sendMessage(ChatColor.DARK_RED + "This feature will be available in an upcomming version!");
					return true;
					
				} else if(args[1].equalsIgnoreCase("setsellfee") || args[1].equalsIgnoreCase("sellfee") || args[1].equalsIgnoreCase("ssf")) {
					
					sender.sendMessage(ChatColor.DARK_RED + "This feature will be available in an upcomming version!");
					return true;
					
				} else if(args[1].equalsIgnoreCase("disablesellfee") || args[1].equalsIgnoreCase("dsf")) {
					
					sender.sendMessage(ChatColor.DARK_RED + "This feature will be available in an upcomming version!");
					return true;
					
				} else if(args[1].equalsIgnoreCase("setsellfeeminimumquantity") || args[1].equalsIgnoreCase("setsellfeeminquantity") || args[1].equalsIgnoreCase("ssfmq")) {
					
					sender.sendMessage(ChatColor.DARK_RED + "This feature will be available in an upcomming version!");
					return true;
					
				}
				
			} else if(args[0].equalsIgnoreCase("booth") || args[0].equalsIgnoreCase("booths") || args[0].equalsIgnoreCase("b")) {
				
				// Check if a second argument is set
				if(args.length < 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				if(args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("c")) {
					// Check if there are enough arguments
					if(args.length < 2 || args.length > 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission(p, "simpleshowcase.command.booth.create")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					String bname = "";
					
					if(args.length == 3) {
						// The used filled in a name
						bname = args[2].toString();
						
						// Trim the name and check if it's valid
						if(bname.trim().equals("")) {
							sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid name for the booth!");
							return true;
						}
						
						// Check if the name contains illegal characters
						char[] ILLEGAL_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', '\'', ':', '.'};
						if(containsChar(bname, ILLEGAL_CHARACTERS)) {
							sender.sendMessage(ChatColor.DARK_RED + "The name contains illigal characters");
							return true;
						}
						
						// Check if there already is a portal with this name
						if(!plugin.getBoothManager().isUniqueName(bname)) {
							sender.sendMessage(ChatColor.DARK_RED + bname);
							sender.sendMessage(ChatColor.DARK_RED + "There already is a booth with this name!");
							return true;
						}
						
					} else {
						// Generate an unique name
						bname = plugin.getBoothManager().getUniqueName();
					}
					
					// Create/define a new shop
					SSBooth b = bm.createBooth();
					b.setName(bname);
					
					// Select the new created portal
					plugin.getBoothManager().selectBooth(p.getName(), b);

					sender.sendMessage(ChatColor.GREEN + "The booth " + ChatColor.GOLD + b.getName() + ChatColor.GREEN + " has been created, and is now selected!");
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("select") || args[1].equalsIgnoreCase("sel") || args[1].equalsIgnoreCase("s")) {
					
					// Acceptable amount of arguments?
					if(args.length < 2 || args.length > 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}

					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission(p, "simpleshowcase.command.booth.select")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					if(args.length == 3) {
						// The player filled in a name to select
						String bname = args[2].toString();
						
						// Is there any portal with this name?
						if(plugin.getBoothManager().isUniqueName(bname)) {
							sender.sendMessage(ChatColor.DARK_RED + bname);
							sender.sendMessage(ChatColor.DARK_RED + "There's no booth available with this name!");
							sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " booth list [page]" + ChatColor.YELLOW + " to list all booths");
							return true;
						}
						
						// Get and select a booth
						SSBooth b = plugin.getBoothManager().get(bname);
						plugin.getBoothManager().selectBooth(p.getName(), b);
						
						sender.sendMessage(ChatColor.GREEN + "The booth " + ChatColor.GOLD + bname + ChatColor.GREEN + " has been selected");
						return true;
						
					} else {
						// The player wants to use the selection by clicking way
						sender.sendMessage("");
						plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.BOOTH_SELECTION, true);
						sender.sendMessage(ChatColor.GREEN + "Left click a booth to select it");
						sender.sendMessage(ChatColor.GREEN + "Right click in the air to get out of the selection mode");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("selectid") || args[1].equalsIgnoreCase("selid") || args[1].equalsIgnoreCase("sid")) {
					
					// Acceptable amount of arguments?
					if(args.length < 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}

					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission(p, "simpleshowcase.command.booth.selectid")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					String bidString = args[2].toString();
					
					// Is the argument ID an valid ID (is it a number)
					if(!isInt(bidString)) {
						sender.sendMessage(ChatColor.DARK_RED + bidString);
						sender.sendMessage(ChatColor.DARK_RED + "The ID has to be a number!");
						return true;
					}
					
					int bid = Integer.parseInt(bidString);
					
					// Is there any booth with this name?
					if(plugin.getBoothManager().isUniqueId(bid)) {
						sender.sendMessage(ChatColor.DARK_RED + bidString);
						sender.sendMessage(ChatColor.DARK_RED + "There's no booth available with this ID!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " booth list [page]" + ChatColor.YELLOW + " to list all booths");
						return true;
					}
					
					// Get and select a booth
					SSBooth b = plugin.getBoothManager().get(bid);
					plugin.getBoothManager().selectBooth(p.getName(), b);

					sender.sendMessage(ChatColor.GREEN + "The booth " + ChatColor.GOLD + b.getName() + ChatColor.GREEN + " has been selected");
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("l")) {
					
					// Check permissions
					if(sender instanceof Player) {
						if(!pm.hasPermission((Player) sender, "simpleshowcase.command.booth.list")) {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
							return true;
						}
					}
					
					if(plugin.getBoothManager().countBooths() == 0) {
						sender.sendMessage(ChatColor.DARK_RED + "There're not booths created yet!");
						return true;
					}
					
					int start = 0;
					int amount = 10;
				
					// Flags
					if(isFlagSet(args, "a")) {
						String stra = getFlagArgument(args, "a");
						if(!isInt(stra)) {
							sender.sendMessage(ChatColor.DARK_RED + stra);
							sender.sendMessage(ChatColor.DARK_RED + "The amount has to be a number!");
							return true;
						}
						int a = Integer.valueOf(stra);
						if(a < 1) {
							sender.sendMessage(ChatColor.DARK_RED + stra);
							sender.sendMessage(ChatColor.DARK_RED + "The amount needs to be 1 or higher!");
							return true;
						}
						amount = a;
					}
					
					int maxPage = (int) Math.ceil(plugin.getBoothManager().countBooths() / amount) + 1;
					
					// Is any page number entered?
					if(args.length >= 3) {
						String arg2 = args[2].toString();
						if(isInt(arg2)) {
							int pag = Integer.valueOf(arg2);
							// Is the number 1 or higher?
							if(pag < 1) {
								sender.sendMessage(ChatColor.DARK_RED + arg2);
								sender.sendMessage(ChatColor.DARK_RED + "The page number has to be 1 or higher!");
								return true;
							}
							start = (pag - 1) * amount;
							
							if(pag > maxPage) {
								sender.sendMessage(ChatColor.DARK_RED + arg2);
								sender.sendMessage(ChatColor.DARK_RED + "The last page is " + String.valueOf(maxPage) + "!");
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.DARK_RED + arg2);
							sender.sendMessage(ChatColor.DARK_RED + "The page number has to be a number!");
							return true;
						}
					}
					
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GREEN + "==========[ BOOTHS " + String.valueOf(start+1) + "/" + String.valueOf(start+amount) + " ]==========");
					for(int i = start; (i < plugin.getBoothManager().countBooths() && i < start+amount); i++) {
						sender.sendMessage(ChatColor.YELLOW + String.valueOf(i+1) +
								". ID: " + ChatColor.GOLD + String.valueOf(plugin.getBoothManager().getBooths().get(i).getId()) +
								ChatColor.YELLOW + "   Name: " + ChatColor.GOLD + plugin.getBoothManager().getBooths().get(i).getName());
					}
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("info") || args[1].equalsIgnoreCase("i")) {
					
					// Check if there are enough arguments
					if(args.length > 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.booth.info")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a booth selected?
					if(!plugin.getBoothManager().hasBoothSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no booth selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a booth using " + ChatColor.GOLD + "/" + commandLabel + " booth select <name>");
						return true;
					}

					// Get the selected booth
					SSBooth b = plugin.getBoothManager().getSelectedBooth(p.getName());
					
					sender.sendMessage(ChatColor.GREEN + "==========[ BOOTH INFO ]==========");
					sender.sendMessage(ChatColor.GOLD + "Id: " + ChatColor.YELLOW + String.valueOf(b.getId()));
					sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.YELLOW + b.getName());
					
					// Is shop enabled
					if(b.isEnabled()) {
						sender.sendMessage(ChatColor.GOLD + "Enabled: " + ChatColor.GREEN + "Yes");
					} else {
						sender.sendMessage(ChatColor.GOLD + "Enabled: " + ChatColor.DARK_RED + "No");
					}
					
					// Print location
					if(b.isLocationSet()) {
						
						// If the player is in the same world, show an 'distance' feature
						if(p.getWorld().getName().equals(b.getWorld())) {
							if((int) (b.getLocation(plugin.getServer()).distance(p.getLocation())) == 1)
								sender.sendMessage(ChatColor.GOLD + "Location: " + 
										ChatColor.YELLOW + "W: " + ChatColor.WHITE + b.getWorld() +
										ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(b.getX()) +
										ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(b.getY()) +
										ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(b.getZ()) +
											ChatColor.GRAY + " (1 block away)");
							else
								sender.sendMessage(ChatColor.GOLD + "Location: " + 
										ChatColor.YELLOW + "W: " + ChatColor.WHITE + b.getWorld() +
										ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(b.getX()) +
										ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(b.getY()) +
										ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(b.getZ()) +
											ChatColor.GRAY + " (" + String.valueOf((int) b.getLocation(plugin.getServer()).distance(p.getLocation())) + " blocks away)");
						} else
							sender.sendMessage(ChatColor.GOLD + "Location: " + 
									ChatColor.YELLOW + "W: " + ChatColor.WHITE + b.getWorld() +
									ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(b.getX()) +
									ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(b.getY()) +
									ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(b.getZ()));
					} else
						sender.sendMessage(ChatColor.GOLD + "Location: " + ChatColor.DARK_RED + "Not set");
					
					// Claim price
					sender.sendMessage(ChatColor.GOLD + "Claim Price: " + ChatColor.YELLOW + b.getClaimPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(b.getClaimPrice(), "Money"));
					
					// Print permissions
					if(b.usesPermissionNode()) {
						sender.sendMessage(ChatColor.GOLD + "Permission Node: " + ChatColor.YELLOW + b.getPermissionNode());
					} else {
						sender.sendMessage(ChatColor.GOLD + "Permission Node: " + ChatColor.DARK_RED + "Not set");
					}
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("delete")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.booth.remove")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getBoothManager().hasBoothSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no booth selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a booth using " + ChatColor.GOLD + "/" + commandLabel + " booth select <name>");
						return true;
					}
					
					// Remove and deselect the booth
					plugin.getBoothManager().removeBooth(plugin.getBoothManager().getSelectedBooth(p.getName()));
					plugin.getBoothManager().deselectBooth(p.getName());

					sender.sendMessage(ChatColor.GREEN + "The booth has been removed and deselected!");
					return true;
					
				} else if(args[1].equalsIgnoreCase("setname") || args[1].equalsIgnoreCase("sn") || args[1].equalsIgnoreCase("name") || args[1].equalsIgnoreCase("rename")) {
					
					// Check if there are enough arguments
					if(args.length < 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.booth.setname")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getBoothManager().hasBoothSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no booth selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a booth using " + ChatColor.GOLD + "/" + commandLabel + " booth select <name>");
						return true;
					}
					
					// Is the name valid
					String newName = args[2].toString();
					
					if(newName.equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + newName);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid name");
						return true;
					}
					
					// The name has to be different
					if(plugin.getBoothManager().getSelectedBooth(p.getName()).getName().equals(newName)) {
						sender.sendMessage(ChatColor.DARK_RED + newName);
						sender.sendMessage(ChatColor.DARK_RED + "The name has to be different!");
						return true;
					}
					
					// Is the name already used?
					if(!plugin.getBoothManager().isUniqueName(newName)) {
						sender.sendMessage(ChatColor.DARK_RED + newName);
						sender.sendMessage(ChatColor.DARK_RED + "This name is already used by another booth");
						return true;
					}

					// Set the name of the portal
					plugin.getBoothManager().getSelectedBooth(p.getName()).setName(newName);

					sender.sendMessage(ChatColor.GREEN + "The booth has been renamed to " + ChatColor.GOLD + newName);
					return true;
					
				} else if(args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("e")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.booth.enable")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getBoothManager().hasBoothSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no booth selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a booth using " + ChatColor.GOLD + "/" + commandLabel + " booth select <name>");
						return true;
					}

					if(plugin.getBoothManager().getSelectedBooth(p.getName()).isEnabled()) {
						// The shop is already enabled
						sender.sendMessage(ChatColor.GREEN + "The booth is already enabled");
						return true;
					} else {
						// Enable the booth
						plugin.getBoothManager().getSelectedBooth(p.getName()).setEnabled(true);
						
						sender.sendMessage(ChatColor.GREEN + "The booth has been enabled");
						return true;
					}
					
				} else if(args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("d")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.booth.disable")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a booth selected?
					if(!plugin.getBoothManager().hasBoothSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no booth selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a booth using " + ChatColor.GOLD + "/" + commandLabel + " booth select <name>");
						return true;
					}

					if(!plugin.getBoothManager().getSelectedBooth(p.getName()).isEnabled()) {
						// The booth is already disabled
						sender.sendMessage(ChatColor.GREEN + "The booth is already disabled");
						return true;
					} else {
						// Disable the shop
						plugin.getBoothManager().getSelectedBooth(p.getName()).setEnabled(false);
						
						sender.sendMessage(ChatColor.GREEN + "The booth has been disabled");
						return true;
					}
				} else if(args[1].equalsIgnoreCase("setlocation") || args[1].equalsIgnoreCase("setloc") || args[1].equalsIgnoreCase("sl") ||
						args[1].equalsIgnoreCase("location") || args[1].equalsIgnoreCase("loc") || args[1].equalsIgnoreCase("l") ||
						args[1].equalsIgnoreCase("setblock") || args[1].equalsIgnoreCase("sb") ||
						args[1].equalsIgnoreCase("block") ||
						args[1].equalsIgnoreCase("setshop") || args[1].equalsIgnoreCase("ss") ||
						args[1].equalsIgnoreCase("shop")) {
					
					// Check if there are enough arguments
					if(args.length != 2) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.booth.setlocation")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a booth selected?
					if(!plugin.getBoothManager().hasBoothSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no booth selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a booth using " + ChatColor.GOLD + "/" + commandLabel + " booth select <name>");
						return true;
					}

					// Put the player in the shop selection mode and return a message
					sender.sendMessage("");
					plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.BOOTH_LOCATION_SELECTION, true);
					sender.sendMessage(ChatColor.GREEN + "Left click a block to move the booth to");
					sender.sendMessage(ChatColor.GREEN + "Right click in the air to get out of the selection mode");
					
					return true;
					
				} else if(args[1].equalsIgnoreCase("setclaimprice") || args[1].equalsIgnoreCase("setprice") ||
						args[1].equalsIgnoreCase("scp") || args[1].equalsIgnoreCase("sp")) {
					
					// Check if there are enough arguments
					if(args.length != 3) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + " " + args[1] + ChatColor.YELLOW + " to view help");
						return true;
					}
					
					// The sender has to be a player
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
						return true;
					}
					
					Player p = (Player) sender;
					
					// Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.booth.setbuyprice")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
					
					// Is a shop selected?
					if(!plugin.getBoothManager().hasBoothSelected(p.getName())) {
						sender.sendMessage(ChatColor.DARK_RED + "You have no booth selected yet!");
						sender.sendMessage(ChatColor.YELLOW + "Select a booth using " + ChatColor.GOLD + "/" + commandLabel + " booth select <name>");
						return true;
					}
					
					// Is the price valid
					String newPrice = args[2].toString();
					
					if(newPrice.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid claim price");
						return true;
					}
					
					// Is the price valid
					if(!isDouble(newPrice)) {
						sender.sendMessage(ChatColor.DARK_RED + newPrice);
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid claim price");
						return true;
					}
					
					double price = Double.parseDouble(newPrice);

					// The price may not be negative
					if(price < 0) {
						sender.sendMessage(ChatColor.DARK_RED + String.valueOf(price));
						sender.sendMessage(ChatColor.DARK_RED + "The claim price may not be negative");
						return true;
					}

					// Set the product buy price
					plugin.getBoothManager().getSelectedBooth(p.getName()).setClaimPrice(price);

					sender.sendMessage(ChatColor.GREEN + "The claim price has been changed to " +
							ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
					return true;
					
				}
				
			} else if(args[0].equalsIgnoreCase("save")) {
				// Check if there are enough arguments
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// Check permissions
				if(sender instanceof Player) {
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.save")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				if(args.length == 1) {
					sender.sendMessage(ChatColor.YELLOW + "[SimpleShowcase] Saving data...");
					long t = System.currentTimeMillis();
					plugin.saveAll();
					long duration = System.currentTimeMillis() - t;
					sender.sendMessage(ChatColor.YELLOW + "[SimpleShowcase] " + ChatColor.GREEN + "Data succesfuly saved, took " + ChatColor.GOLD + String.valueOf(duration) + "ms" + ChatColor.GREEN + "!");
					return true;
				}
				
				sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
				return true;
				
			} else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("load")) {
				// Check if there are enough arguments
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// Check permissions
				if(sender instanceof Player) {
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.reload")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				if(args.length == 1) {
					sender.sendMessage(ChatColor.YELLOW + "[SimpleShowcase] Reloading data...");
					long t = System.currentTimeMillis();
					// Remove all shopw items first
					plugin.getShopManager().removeAllShopShowItems();
					plugin.loadAll();
					long duration = System.currentTimeMillis() - t;
					sender.sendMessage(ChatColor.YELLOW + "[SimpleShowcase] " + ChatColor.GREEN + "Data succesfuly loaded, took " + ChatColor.GOLD + String.valueOf(duration) + "ms" + ChatColor.GREEN + "!");
					return true;
				}
				
				sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
				return true;
				
			} else if(args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("statics") || args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("s")) {
				// Check if there are enough arguments
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// Check permissions
				if(sender instanceof Player) {
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.status")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}

				// Define some variables with plugin data
				PluginManager pluginManager = plugin.getServer().getPluginManager();
				PluginDescriptionFile pdfFile = plugin.getDescription();
				
				sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE STATUS ]==========");
				
				sender.sendMessage(ChatColor.GRAY + "Plugin Information:");
				
				// Return the amount of loaded shops and booths
				sender.sendMessage(ChatColor.GOLD + "Shops: " + ChatColor.YELLOW + String.valueOf(plugin.getShopManager().countShops()));
				sender.sendMessage(ChatColor.GOLD + "Booths: " + ChatColor.YELLOW + String.valueOf(plugin.getBoothManager().countBooths()));
				sender.sendMessage(ChatColor.GOLD + "Pricelist Items: " + ChatColor.YELLOW + String.valueOf(plugin.getPricelistManager().countPricelistItems()));

				// Return the used permissions system
				if(pm.isEnabled()) {
					sender.sendMessage(ChatColor.GOLD + "Permissions: " + ChatColor.GREEN + pm.getUsedPermissionsSystemType().getName());
				} else
					sender.sendMessage(ChatColor.GOLD + "Permissions: " + ChatColor.DARK_RED + "Disabled");
					
				// Return the used economy system
				if(plugin.getEconomyManager().isEnabled()) {
					sender.sendMessage(ChatColor.GOLD + "Economy: " + ChatColor.GREEN + plugin.getEconomyManager().getUsedEconomySystemType().getName());
				} else
					sender.sendMessage(ChatColor.GOLD + "Economy: " + ChatColor.DARK_RED + "Disabled");
				
				// Return the version info
				sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdfFile.getVersion() +
						" " + ChatColor.GRAY + "(code: " + String.valueOf(plugin.getVersionCode()) + ")");
				
				// Return the running time
				long diff = new Date().getTime() - plugin.getEnabledDate().getTime();
				int millis = (int) (diff % 1000);
				diff/=1000;
				int seconds = (int) (diff % 60);
				diff/=60;
				int minutes = (int) (diff % 60);
				diff/=60;
				int hours = (int) diff;
				String t = ChatColor.WHITE + String.valueOf(millis) + ChatColor.YELLOW + " Millis";
				if(seconds > 0 || minutes > 0 || hours > 0) {
					t = ChatColor.WHITE + String.valueOf(seconds) + ChatColor.YELLOW + " Secconds & " + t;
					if(minutes > 0 || hours > 0) {
						t = ChatColor.WHITE + String.valueOf(minutes) + ChatColor.YELLOW + " Minutes, " + t;
						if(hours > 0)
							t = ChatColor.WHITE + String.valueOf(hours) + ChatColor.YELLOW + " Hours, " + t;
					}
				}
				sender.sendMessage(ChatColor.GOLD + "Time Running: " + ChatColor.YELLOW + t);
				
				sender.sendMessage(ChatColor.GRAY + "Server Information:");
				
				// Return player info
				sender.sendMessage(ChatColor.GOLD + "Players: " + ChatColor.YELLOW + String.valueOf(plugin.getServer().getOnlinePlayers().length) + " / " + String.valueOf(plugin.getServer().getMaxPlayers()));
				sender.sendMessage(ChatColor.GOLD + "Running Plugins: " + ChatColor.YELLOW + String.valueOf(pluginManager.getPlugins().length));
				
				// Return the version info
				sender.sendMessage(ChatColor.GOLD + "Server Version: " + ChatColor.YELLOW + plugin.getServer().getVersion());
				sender.sendMessage(ChatColor.GOLD + "Bukkit Version: " + ChatColor.YELLOW + plugin.getServer().getBukkitVersion());
				
				// Return the current date
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				String d = dateFormat.format(date);
				sender.sendMessage(ChatColor.GOLD + "Server Time: " + ChatColor.YELLOW + d);
				
				sender.sendMessage(ChatColor.GRAY + "Machine Information:");
				
				// Return server/java info
				sender.sendMessage(ChatColor.GOLD + "OS Name: " + ChatColor.YELLOW + System.getProperty("os.name"));
				sender.sendMessage(ChatColor.GOLD + "OS Version: " + ChatColor.YELLOW + System.getProperty("os.version"));
				sender.sendMessage(ChatColor.GOLD + "Java Version: " + ChatColor.YELLOW + System.getProperty("java.version"));
				return true;
				
			} else if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v") ||
					args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i") ||
					args[0].equalsIgnoreCase("about") || args[0].equalsIgnoreCase("a")) {
				// Check if there are enough arguments
				if(args.length < 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}

				PluginDescriptionFile pdfFile = plugin.getDescription();
				sender.sendMessage(ChatColor.YELLOW + "This server is running Simple Showcase v" + pdfFile.getVersion());
				sender.sendMessage(ChatColor.YELLOW + "Simple Showcase is made my Tim Visee - timvisee.com");
				return true;
			} else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
				
				// Run the help command through the help command handler
				CommandHandlerHelp chh = new CommandHandlerHelp(plugin);
				return chh.onCommand(sender, cmd, commandLabel, args);
				
			}
			
		} else if(commandLabel.equalsIgnoreCase("shop") || commandLabel.equalsIgnoreCase("shops") || commandLabel.equalsIgnoreCase("s")) {
			
			// Player owned shop commands
			// Combine arguments with apostrophes
			args = combineArguments(args);
			
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
				// Check if there are enough arguments
				if(args.length < 1 || args.length > 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission(p, "simpleshowcase.command.playerowned.create")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				String sname = "";
				
				if(args.length == 2) {
					// The used filled in a name
					sname = args[1].toString();
					
					// Trim the name and check if it's valid
					if(sname.trim().equals("")) {
						sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid name for your shop!");
						return true;
					}
					
					// Check if the name contains illegal characters
					char[] ILLEGAL_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', '\'', ':', '.'};
					if(containsChar(sname, ILLEGAL_CHARACTERS)) {
						sender.sendMessage(ChatColor.DARK_RED + "The name contains illigal characters");
						return true;
					}
					
					// Check if there already is a portal with this name
					if(!plugin.getShopManager().isUniquePlayerOwnedName(p.getName(), sname)) {
						sender.sendMessage(ChatColor.DARK_RED + sname);
						sender.sendMessage(ChatColor.DARK_RED + "You already have a shop with this name!");
						return true;
					}
				} else {
					// Generate an unique name
					sname = plugin.getShopManager().getUniquePlayerOwnedName(p.getName());
				}
				
				// Create/define a new shop
				SSShop s = sm.createShop();
				s.setName(sname);
				s.setOwnerType(SSShopOwnerType.PLAYER);
				s.setOwnerName(p.getName());
				s.setCanBuy(true);
				s.setCanSell(false);
				s.removeShopWhenEmptyEnabled(true);
				
				// Select the new created portal
				plugin.getShopManager().selectShop(p.getName(), s);

				sender.sendMessage(ChatColor.GREEN + "Your new shop " + ChatColor.GOLD + s.getName() + ChatColor.GREEN + " has been created!");
				sender.sendMessage(ChatColor.GREEN + "Place down a block to place your shop or left click on a booth");
				sender.sendMessage(ChatColor.GREEN + "Right click in the air to cancel this wizard");
				
				plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PLAYEROWNED_CREATION_WIZZARD, false);
				plugin.getPlayerModeManager().setPlayerModeStep(p, 1);
				
				return true;
				
			} else if(args[0].equalsIgnoreCase("select") || args[0].equalsIgnoreCase("sel") || args[0].equalsIgnoreCase("s")) {
				
				// Acceptable amount of arguments?
				if(args.length < 1 || args.length > 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}

				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission(p, "simpleshowcase.command.playerowned.select")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				if(args.length == 2) {
					// The player filled in a name to select
					String sname = args[1].toString();
					
					// Is there any shop with this name?
					if(plugin.getShopManager().isUniquePlayerOwnedName(p.getName(), sname)) {
						sender.sendMessage(ChatColor.DARK_RED + sname);
						sender.sendMessage(ChatColor.DARK_RED + "You don't have a shop with this name!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " list [page]" + ChatColor.YELLOW + " to list all your shops");
						return true;
					}
					
					// Get and select the shop
					SSShop s = plugin.getShopManager().getPlayerOwned(p.getName(), sname);
					plugin.getShopManager().selectShop(p.getName(), s);
					
					sender.sendMessage(ChatColor.GREEN + "Your shop " + ChatColor.GOLD + sname + ChatColor.GREEN + " has been selected");
					return true;
					
				} else {
					// The player wants to use the selection by clicking way
					sender.sendMessage("");
					plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PLAYEROWNED_SELECTION, true);
					sender.sendMessage(ChatColor.GREEN + "Left click your shop to select it");
					sender.sendMessage(ChatColor.GREEN + "Right click in the air to get out of the selection mode");
					return true;
				}
				
			} else if(args[0].equalsIgnoreCase("selectid") || args[0].equalsIgnoreCase("selid") || args[0].equalsIgnoreCase("sid")) {
				
				// Acceptable amount of arguments?
				if(args.length < 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}

				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission(p, "simpleshowcase.command.playerowned.selectid")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				String sidString = args[1].toString();
				
				// Is the argument ID an valid ID (is it a number)
				if(!isInt(sidString)) {
					sender.sendMessage(ChatColor.DARK_RED + sidString);
					sender.sendMessage(ChatColor.DARK_RED + "The ID has to be a number!");
					return true;
				}
				
				int sid = Integer.parseInt(sidString);
				
				// Is there any shop with this name?
				if(plugin.getShopManager().isUniqueId(sid)) {
					sender.sendMessage(ChatColor.DARK_RED + sidString);
					sender.sendMessage(ChatColor.DARK_RED + "You don't have a shop with this ID!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " list [page]" + ChatColor.YELLOW + " to list all your shops");
					return true;
				}
				
				// Is there any shop with this name?
				if(!plugin.getShopManager().get(sid).getOwnerType().equals(SSShopOwnerType.PLAYER) || !plugin.getShopManager().get(sid).getOwnerName().equals(p.getName())) {
					sender.sendMessage(ChatColor.DARK_RED + sidString);
					sender.sendMessage(ChatColor.DARK_RED + "You don't have a shop with this ID!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " list [page]" + ChatColor.YELLOW + " to list all your shops");
					return true;
				}
				
				// Get and select a shop
				SSShop s = plugin.getShopManager().getPlayerOwned(p.getName(), sid);
				plugin.getShopManager().selectShop(p.getName(), s);

				sender.sendMessage(ChatColor.GREEN + "Your shop " + ChatColor.GOLD + s.getName() + ChatColor.GREEN + " has been selected");
				
				return true;
				
			} else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {
					
				// Check permissions
				if(sender instanceof Player) {
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.playerowned.list")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				Player p = (Player) sender;
				
				if(plugin.getShopManager().countPlayerOwnedShops(p.getName()) == 0) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have any shops yet!");
					return true;
				}
				
				int start = 0;
				int amount = 10;
			
				// Flags
				if(isFlagSet(args, "a")) {
					String stra = getFlagArgument(args, "a");
					if(!isInt(stra)) {
						sender.sendMessage(ChatColor.DARK_RED + stra);
						sender.sendMessage(ChatColor.DARK_RED + "The amount has to be a number!");
						return true;
					}
					int a = Integer.valueOf(stra);
					if(a < 1) {
						sender.sendMessage(ChatColor.DARK_RED + stra);
						sender.sendMessage(ChatColor.DARK_RED + "The amount has to be 1 or higher!");
						return true;
					}
					amount = a;
				}
				
				int maxPage = (int) Math.ceil(plugin.getShopManager().countPlayerOwnedShops(p.getName()) / amount) + 1;
				
				// Is any page number entered?
				if(args.length >= 2) {
					String arg2 = args[1].toString();
					if(isInt(arg2)) {
						int pag = Integer.valueOf(arg2);
						// Is the number 1 or higher?
						if(pag < 1) {
							sender.sendMessage(ChatColor.DARK_RED + arg2);
							sender.sendMessage(ChatColor.DARK_RED + "The page number has to be 1 or higher!");
							return true;
						}
						start = (pag - 1) * amount;
						
						if(pag > maxPage) {
							sender.sendMessage(ChatColor.DARK_RED + arg2);
							sender.sendMessage(ChatColor.DARK_RED + "The last page is " + String.valueOf(maxPage) + "!");
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + arg2);
						sender.sendMessage(ChatColor.DARK_RED + "The page number has to be a number!");
						return true;
					}
				}
				
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GREEN + "==========[ MY SHOPS " + String.valueOf(start+1) + "/" + String.valueOf(start+amount) + " ]==========");
				for(int i = start; (i < plugin.getShopManager().countPlayerOwnedShops(p.getName()) && i < start+amount); i++) {
					sender.sendMessage(ChatColor.YELLOW + String.valueOf(i+1) +
							". ID: " + ChatColor.GOLD + String.valueOf(plugin.getShopManager().getPlayerOwnedShops(p.getName()).get(i).getId()) +
							ChatColor.YELLOW + "   Name: " + ChatColor.GOLD + plugin.getShopManager().getPlayerOwnedShops(p.getName()).get(i).getName());
				}
				
				return true;
				
			} else if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
				
				// Check if there are enough arguments
				if(args.length > 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission((Player) sender, "simpleshowcase.command.playerowned.info")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				// Is a shop selected?
				if(!plugin.getShopManager().hasShopSelected(p.getName())) {
					sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
					sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " select [name]");
					return true;
				}

				// Get the selected shop
				SSShop s = plugin.getShopManager().getSelectedShop(p.getName());
				
				sender.sendMessage(ChatColor.GREEN + "==========[ MY SHOP INFO ]==========");
				sender.sendMessage(ChatColor.GOLD + "Id: " + ChatColor.YELLOW + String.valueOf(s.getId()));
				sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.YELLOW + s.getName());
				
				// Is shop enabled
				if(s.isEnabled()) {
					sender.sendMessage(ChatColor.GOLD + "Enabled: " + ChatColor.GREEN + "Yes");
				} else {
					sender.sendMessage(ChatColor.GOLD + "Enabled: " + ChatColor.DARK_RED + "No");
				}
				
				// The owner
				switch (s.getOwnerType()) {
				case SERVER:
					sender.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.YELLOW + "Server");
					break;
				case PLAYER:
					sender.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.YELLOW + s.getOwnerName());
					break;
				case OTHER:
					sender.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.DARK_RED + "Other");
					break;
				case UNKNOWN:
				default:
					sender.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.DARK_RED + "Unknown");
				}
				
				// Print location
				if(s.isLocationSet()) {
					
					// If the player is in the same world, show an 'distance' feature
					if(p.getWorld().getName().equals(s.getWorld())) {
						if((int) (s.getLocation(plugin.getServer()).distance(p.getLocation())) == 1)
							sender.sendMessage(ChatColor.GOLD + "Location: " + 
									ChatColor.YELLOW + "W: " + ChatColor.WHITE + s.getWorld() +
									ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(s.getX()) +
									ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(s.getY()) +
									ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(s.getZ()) +
										ChatColor.GRAY + " (1 block away)");
						else
							sender.sendMessage(ChatColor.GOLD + "Location: " + 
									ChatColor.YELLOW + "W: " + ChatColor.WHITE + s.getWorld() +
									ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(s.getX()) +
									ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(s.getY()) +
									ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(s.getZ()) +
										ChatColor.GRAY + " (" + String.valueOf((int) s.getLocation(plugin.getServer()).distance(p.getLocation())) + " blocks away)");
					} else
						sender.sendMessage(ChatColor.GOLD + "Location: " + 
								ChatColor.YELLOW + "W: " + ChatColor.WHITE + s.getWorld() +
								ChatColor.YELLOW + " X: " + ChatColor.WHITE + String.valueOf(s.getX()) +
								ChatColor.YELLOW + " Y: " + ChatColor.WHITE + String.valueOf(s.getY()) +
								ChatColor.YELLOW + " Z: " + ChatColor.WHITE + String.valueOf(s.getZ()));
				} else
					sender.sendMessage(ChatColor.GOLD + "Location: " + ChatColor.DARK_RED + "Not set");
				
				// Print prices
				sender.sendMessage(ChatColor.GOLD + "Price: " + ChatColor.YELLOW + s.getBuyPriceProper() + " " + plugin.getEconomyManager().getCurrencyName(s.getBuyPrice(), "Money"));
				
				// Print if the item is in stock
				if(s.getStockQuantity() > 0)
					sender.sendMessage(ChatColor.GOLD + "In stock: " + ChatColor.GREEN + "Yes, " + String.valueOf(s.getStockQuantity()) + " products left!");
				else
					sender.sendMessage(ChatColor.GOLD + "In stock: " + ChatColor.DARK_RED + "No");
				//sender.sendMessage(ChatColor.GOLD + "In stock: " + ChatColor.GREEN + "Yes" + ChatColor.GRAY + " (" + ITEMCOUNT + " products left)");
				
				// Print 'remove item when empty'
				if(s.removeShopWhenEmptyEnabled()) {
					sender.sendMessage(ChatColor.GOLD + "Remove shop when empty: " + ChatColor.GREEN + "Yes");
				} else {
					sender.sendMessage(ChatColor.GOLD + "Remove shop when empty: " + ChatColor.DARK_RED + "No");
				}
				return true;
				
			} else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
				
				// Check if there are enough arguments
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission((Player) sender, "simpleshowcase.command.playerowned.remove")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				// Is a shop selected?
				if(!plugin.getShopManager().hasShopSelected(p.getName())) {
					sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
					sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " select [name]");
					return true;
				}
				
				// Remove and deselect the shop
				plugin.getShopManager().removeShop(plugin.getShopManager().getSelectedShop(p.getName()));
				plugin.getShopManager().deselectShop(p.getName());

				sender.sendMessage(ChatColor.GREEN + "Your shop has been removed!");
				return true;
				
			} else if(args[0].equalsIgnoreCase("setname") || args[0].equalsIgnoreCase("sn") || args[0].equalsIgnoreCase("name") || args[0].equalsIgnoreCase("rename")) {
				
				// Check if there are enough arguments
				if(args.length < 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission((Player) sender, "simpleshowcase.command.playerowned.setname")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				// Is a shop selected?
				if(!plugin.getShopManager().hasShopSelected(p.getName())) {
					sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
					sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " select [name]");
					return true;
				}
				
				// Is the name valid
				String newName = args[1].toString();
				
				if(newName.equals("")) {
					sender.sendMessage(ChatColor.DARK_RED + newName);
					sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid name");
					return true;
				}
				
				// The name has to be different
				if(plugin.getShopManager().getSelectedShop(p.getName()).getName().equals(newName)) {
					sender.sendMessage(ChatColor.DARK_RED + newName);
					sender.sendMessage(ChatColor.DARK_RED + "The name has to be different!");
					return true;
				}
				
				// Is the name already used?
				if(!plugin.getShopManager().isUniquePlayerOwnedName(p.getName(), newName)) {
					sender.sendMessage(ChatColor.DARK_RED + newName);
					sender.sendMessage(ChatColor.DARK_RED + "This name is already used by one of your other shops");
					return true;
				}

				// Set the name of the portal
				plugin.getShopManager().getSelectedShop(p.getName()).setName(newName);

				sender.sendMessage(ChatColor.GREEN + "Your shop has been renamed to " + ChatColor.GOLD + newName);
				return true;
				
			} else if(args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("e")) {
				
				// Check if there are enough arguments
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission((Player) sender, "simpleshowcase.command.playerowned.enable")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				// Is a shop selected?
				if(!plugin.getShopManager().hasShopSelected(p.getName())) {
					sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
					sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " select [name]");
					return true;
				}

				if(plugin.getShopManager().getSelectedShop(p.getName()).isEnabled()) {
					// The shop is already enabled
					sender.sendMessage(ChatColor.GREEN + "Your shop is already enabled");
					return true;
				} else {
					// Enable the shop
					plugin.getShopManager().getSelectedShop(p.getName()).setEnabled(true);
					
					// Spawn the show item if needed
					if(plugin.getShopManager().getSelectedShop(p.getName()).showItem())
						plugin.getShopManager().getSelectedShop(p.getName()).spawnShowItem(plugin.getServer());
					
					sender.sendMessage(ChatColor.GREEN + "Your shop has been enabled");
					return true;
				}
				
			} else if(args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("d")) {
				
				// Check if there are enough arguments
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission((Player) sender, "simpleshowcase.command.playerowned.disable")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				// Is a shop selected?
				if(!plugin.getShopManager().hasShopSelected(p.getName())) {
					sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
					sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " select [name]");
					return true;
				}

				if(!plugin.getShopManager().getSelectedShop(p.getName()).isEnabled()) {
					// The shop is already disabled
					sender.sendMessage(ChatColor.GREEN + "Your shop is already disabled");
					return true;
				} else {
					// Disable the shop
					plugin.getShopManager().getSelectedShop(p.getName()).setEnabled(false);
					
					// Remove the show item if it's spawned
					if(plugin.getShopManager().getSelectedShop(p.getName()).isShowItemSpawned())
						plugin.getShopManager().getSelectedShop(p.getName()).removeShowItem();
					
					sender.sendMessage(ChatColor.GREEN + "Your shop has been disabled");
					return true;
				}
				
			} else if(args[0].equalsIgnoreCase("setlocation") || args[0].equalsIgnoreCase("setloc") || args[0].equalsIgnoreCase("sl") ||
					args[0].equalsIgnoreCase("location") || args[0].equalsIgnoreCase("loc") || args[0].equalsIgnoreCase("l") ||
					args[0].equalsIgnoreCase("setblock") || args[0].equalsIgnoreCase("sb") ||
					args[0].equalsIgnoreCase("block") ||
					args[0].equalsIgnoreCase("setshop") || args[0].equalsIgnoreCase("ss") ||
					args[0].equalsIgnoreCase("shop")) {
				
				// Check if there are enough arguments
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission((Player) sender, "simpleshowcase.command.playerowned.setlocation")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				// Is a shop selected?
				if(!plugin.getShopManager().hasShopSelected(p.getName())) {
					sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
					sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " select [name]");
					return true;
				}

				// Put the player in the shop selection mode and return a message
				sender.sendMessage("");
				plugin.getPlayerModeManager().setPlayerMode(p, PlayerModeType.SHOP_PLAYEROWNED_LOCATION_SELECTION, true);
				sender.sendMessage(ChatColor.GREEN + "Place down a block to move your shop or left click on a booth");
				sender.sendMessage(ChatColor.GREEN + "Right click in the air to get out of the location selection mode");
				
				return true;
				
			} else if(args[0].equalsIgnoreCase("setprice") || args[0].equalsIgnoreCase("sp") ||
					args[0].equalsIgnoreCase("setbuyprice") || args[0].equalsIgnoreCase("sbp")) {
				
				// Check if there are enough arguments
				if(args.length != 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
					sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + args[0] + ChatColor.YELLOW + " to view help");
					return true;
				}
				
				// The sender has to be a player
				if(!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_RED + "You could only use this command in-game!");
					return true;
				}
				
				Player p = (Player) sender;
				
				// Check permissions
				if(!pm.hasPermission((Player) sender, "simpleshowcase.command.playerowned.setprice")) {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					return true;
				}
				
				// Is a shop selected?
				if(!plugin.getShopManager().hasShopSelected(p.getName())) {
					sender.sendMessage(ChatColor.DARK_RED + "You have no shop selected yet!");
					sender.sendMessage(ChatColor.YELLOW + "Select a shop using " + ChatColor.GOLD + "/" + commandLabel + " select [name]");
					return true;
				}
				
				// Is the price valid
				String newPrice = args[1].toString();
				
				if(newPrice.trim().equals("")) {
					sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid price");
					return true;
				}
				
				// Is the price valid
				if(!isDouble(newPrice)) {
					sender.sendMessage(ChatColor.DARK_RED + newPrice);
					sender.sendMessage(ChatColor.DARK_RED + "Please enter a valid product price");
					return true;
				}
				
				double price = Double.parseDouble(newPrice);

				// The price may not be negative
				if(price < 0) {
					sender.sendMessage(ChatColor.DARK_RED + String.valueOf(price));
					sender.sendMessage(ChatColor.DARK_RED + "The price may not be negative");
					return true;
				}
				
				// Set the product sell price
				plugin.getShopManager().getSelectedShop(p.getName()).setBuyPrice(price);

				sender.sendMessage(ChatColor.GREEN + "The product price has been changed to " +
						ChatColor.GOLD + plugin.getEconomyManager().toMoneyNotationProper(price, true));
				return true;
				
			} else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
				
				// Run the help command through the help command handler
				CommandHandlerHelp chh = new CommandHandlerHelp(plugin);
				return chh.onCommand(sender, cmd, commandLabel, args);
				
			} else {				
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
		}
		return false;
	}
	
	public String[] combineArguments(String[] args) {
		if(args.length == 0) {
			// No arguments, instantly return
			return args;
		}
		
		List<String> newArgs = new ArrayList<String>();
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].length() == 0 || args[i] == null) {
				// Empty argument, throw it away
				
			} else if((args[i].startsWith("'") || args[i].startsWith("\"")) && args[i].length() > 1) {
				// The argument starts with a apostrophe and has more than 1 character
				// Get the first char (apostrophe type, " or ')
				char c = args[i].charAt(0);
				
				// Does this same argument ends with an apostrophe too, if so return this argument
				if(args[i].endsWith(String.valueOf(c))) {
					// Remove the first and the last character, because these characters are an apostrophe
					newArgs.add(args[i].substring(1, args[i].length() - 1));
				} else {
					
					if(i == args.length - 1) {
						// This is the last argument, and it doesn't end with an apostrophe. Don't do anything with the apostrophe
						newArgs.add(args[i]);
					} else {
						
						// Loop through the rest of the arguments to check if these ends with an apostrophe.
						for(int j = i; j < args.length; j++) {
							
							if(args[j].endsWith(String.valueOf(c))) {
								// This argument does end with an apostrophe.
								int istart = i;
								int iend = j;
								String newArg = "";
								
								// Combine the arguments
								for(int k = istart; k <= iend; k++) {
									if(newArg.length() == 0) {
										// Don't start with a space
										newArg = newArg + args[k];
									} else {
										newArg = newArg + " " + args[k];
									}
								}
								
								// Remove the apostrophes and return the argument
								newArgs.add(newArg.substring(1, newArg.length() - 1));
								
								// Set the main for loop to the last checked argument index
								i = j + 1;
								
							} else {
								if(j == (args.length - 1)) {
									// The last argument doesn't end with an apostrophe. Only return the relative first argument and go a head with the others
									newArgs.add(args[i]);
								}
							}
						}
					}
				}
			} else {
				// Add the argument to the new array
				newArgs.add(args[i]);
			}
		}
		
		// Convert the list to an normal array and return it
		String[] returnArgs = new String[newArgs.size()];
		newArgs.toArray(returnArgs);

		return returnArgs;
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
	
	public boolean isFlagSet(String args[], String flag) {
		for(int i = 0; i < args.length; i++) {
			if(args[i].equalsIgnoreCase("-" + flag) || args[i].equalsIgnoreCase("/" + flag)) {
				return true;
			}
		}
		return false;
	}
	
	public String getFlagArgument(String args[], String flag, String def) {
		for(int i = 0; i < args.length; i++) {
			if(args[i].equalsIgnoreCase("-" + flag) || args[i].equalsIgnoreCase("/" + flag)) {
				if(i+1 < args.length) {
					return args[i+1].toString();
				} else {
					return def;
				}
			}
		}
		return def;
	}
	
	public String getFlagArgument(String args[], String flag) {
		return getFlagArgument(args, flag, "");
	}
}
