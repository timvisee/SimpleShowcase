package com.timvisee.simpleshowcase;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandlerHelp {
	
	SimpleShowcase plugin;
	
	public CommandHandlerHelp(SimpleShowcase instance) {
		this.plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("simpleshowcase") || commandLabel.equalsIgnoreCase("ss")) {
			// Command arguments should already be combined by the executor of this function
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
				
				// Help commands (/ss help [sub-categories])
				if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
					if(args.length == 1) {
						
						// View the help
						sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " help [args]" + ChatColor.WHITE + " : View help");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop create [name] [flags]" + ChatColor.WHITE + " : Create a new shp");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop select" + ChatColor.WHITE + " : Select a shop by clicking");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop select <name>" + ChatColor.WHITE + " : Select a shop by name");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop selectid <id>" + ChatColor.WHITE + " : Select a shop by id");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop list [page] [flags]" + ChatColor.WHITE + " : List all shops");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop info" + ChatColor.WHITE + " : View all info about a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop remove" + ChatColor.WHITE + " : Remove a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setname <setname>" + ChatColor.WHITE + " : Set the name of a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enable" + ChatColor.WHITE + " : Enable a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disable" + ChatColor.WHITE + " : Disable a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setowner <type> [name]" + ChatColor.WHITE + " : Set the owner of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setlocation" + ChatColor.WHITE + " : Set the location of a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setproduct [type] [value]" + ChatColor.WHITE + " : Set the product of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop linkpricelistitem <name>" + ChatColor.WHITE + " : Link a pricelist to a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop unlinkpricelistitem" + ChatColor.WHITE + " : Unlink the pricelist of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setshowitem [item]" + ChatColor.WHITE + " : Set the show item of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableshowitem" + ChatColor.WHITE + " : Enable the show item of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableshowitem" + ChatColor.WHITE + " : Disable the show item of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enablebuy" + ChatColor.WHITE + " : Enable buying of a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablebuy" + ChatColor.WHITE + " : Disable buying of a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enablesell" + ChatColor.WHITE + " : Enable selling of a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablesell" + ChatColor.WHITE + " : Disable selling of a selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setbuyprice <price>" + ChatColor.WHITE + " : Set the buy price of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setsellprice <price>" + ChatColor.WHITE + " : Set the sell price of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setbuystack <quantity>" + ChatColor.WHITE + " : Set the buy stack ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablebuystack" + ChatColor.WHITE + " : Disable buy stack of a selected ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setsellstack <quantity>" + ChatColor.WHITE + " : Set the sell stack ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablesellstack" + ChatColor.WHITE + " : Disable sell stack of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setminbuyquantity <quantity>" + ChatColor.WHITE + " : Set the min buy ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableminbuyquantity" + ChatColor.WHITE + " : Disable min buy quantity ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setmaxbuyquantity <quantity>" + ChatColor.WHITE + " : Set max buy quantity ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablemaxsellquantity" + ChatColor.WHITE + " : Disable max buy quantity ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setminsellquantity <quantity>" + ChatColor.WHITE + " : Set min sell quantity ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableminsellquantity" + ChatColor.WHITE + " : Disable min sell quantity ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setmaxsellquantity <quantity>" + ChatColor.WHITE + " : Set max sell quantity ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablemaxsellquantity" + ChatColor.WHITE + " : Disable max sell quantity ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableinstantbuy" + ChatColor.WHITE + " : Enable instant buy of a selected ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableinstantbuy" + ChatColor.WHITE + " : Disable instant buy of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setinstantbuyquantity <quantity>" + ChatColor.WHITE + " : Set instant buy ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableinstantsell" + ChatColor.WHITE + " : Enable instant sell of a selected ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableinstantsell" + ChatColor.WHITE + " : Disable instant sell of a...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setinstantsellquantity <quantity>" + ChatColor.WHITE + " : Set instant sell ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth create [name] [flags]" + ChatColor.WHITE + " : Create a booth");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth select" + ChatColor.WHITE + " : Select a booth by clicking");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth select <name>" + ChatColor.WHITE + " : Select a booth by name");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth selectid <id>" + ChatColor.WHITE + " : Selece a booth by id");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth list [page] [flags]" + ChatColor.WHITE + " : List all booths");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth info" + ChatColor.WHITE + " : View info about a selected booth");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth remove" + ChatColor.WHITE + " : Remove a selected booth");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setname <name>" + ChatColor.WHITE + " : Set the name of a selected booth");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth enable" + ChatColor.WHITE + " : Enable a selected booth");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth disable" + ChatColor.WHITE + " : Disable a selected booth");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setlocation" + ChatColor.WHITE + " : Set the location of a selected booth");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setclaimprice <price>" + ChatColor.WHITE + " : Set the claim price of a ...");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " save" + ChatColor.WHITE + " : Save all data");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " reload" + ChatColor.WHITE + " : Reload all data");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " status" + ChatColor.WHITE + " : View all plugin status");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " version" + ChatColor.WHITE + " : View plugin version");
						return true;
						
					} else {
						if(args[1].equals("shop") || args[1].equals("shops") || args[1].equals("s")) {
							if(args.length == 2) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop create [name] [flags]" + ChatColor.WHITE + " : Create a new shp");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop select" + ChatColor.WHITE + " : Select a shop by clicking");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop select <name>" + ChatColor.WHITE + " : Select a shop by name");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop selectid <id>" + ChatColor.WHITE + " : Select a shop by id");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop list [page] [flags]" + ChatColor.WHITE + " : List all shops");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop info" + ChatColor.WHITE + " : View all info about a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop remove" + ChatColor.WHITE + " : Remove a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setname <setname>" + ChatColor.WHITE + " : Set the name of a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enable" + ChatColor.WHITE + " : Enable a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disable" + ChatColor.WHITE + " : Disable a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setowner <type> [name]" + ChatColor.WHITE + " : Set the owner of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setlocation" + ChatColor.WHITE + " : Set the location of a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setproduct [type] [value]" + ChatColor.WHITE + " : Set the product of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop linkpricelistitem <name>" + ChatColor.WHITE + " : Link a pricelist to a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop unlinkpricelistitem" + ChatColor.WHITE + " : Unlink the pricelist of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setshowitem [item]" + ChatColor.WHITE + " : Set the show item of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableshowitem" + ChatColor.WHITE + " : Enable the show item of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableshowitem" + ChatColor.WHITE + " : Disable the show item of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enablebuy" + ChatColor.WHITE + " : Enable buying of a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablebuy" + ChatColor.WHITE + " : Disable buying of a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enablesell" + ChatColor.WHITE + " : Enable selling of a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablesell" + ChatColor.WHITE + " : Disable selling of a selected shop");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setbuyprice <price>" + ChatColor.WHITE + " : Set the buy price of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setsellprice <price>" + ChatColor.WHITE + " : Set the sell price of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setbuystack <quantity>" + ChatColor.WHITE + " : Set the buy stack ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablebuystack" + ChatColor.WHITE + " : Disable buy stack of a selected ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setsellstack <quantity>" + ChatColor.WHITE + " : Set the sell stack ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablesellstack" + ChatColor.WHITE + " : Disable sell stack of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setminbuyquantity <quantity>" + ChatColor.WHITE + " : Set the min buy ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableminbuyquantity" + ChatColor.WHITE + " : Disable min buy quantity ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setmaxbuyquantity <quantity>" + ChatColor.WHITE + " : Set max buy quantity ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablemaxsellquantity" + ChatColor.WHITE + " : Disable max buy quantity ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setminsellquantity <quantity>" + ChatColor.WHITE + " : Set min sell quantity ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableminsellquantity" + ChatColor.WHITE + " : Disable min sell quantity ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setmaxsellquantity <quantity>" + ChatColor.WHITE + " : Set max sell quantity ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablemaxsellquantity" + ChatColor.WHITE + " : Disable max sell quantity ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableinstantbuy" + ChatColor.WHITE + " : Enable instant buy of a selected ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableinstantbuy" + ChatColor.WHITE + " : Disable instant buy of a ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setinstantbuyquantity <quantity>" + ChatColor.WHITE + " : Set instant buy ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableinstantsell" + ChatColor.WHITE + " : Enable instant sell of a selected ...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableinstantsell" + ChatColor.WHITE + " : Disable instant sell of a...");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setinstantsellquantity <quantity>" + ChatColor.WHITE + " : Set instant sell ...");
								return true;
							}
							if(args[2].equalsIgnoreCase("create") || args[2].equalsIgnoreCase("c")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop create [name] [flags]" + ChatColor.WHITE + " : Create a new shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The shop name");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "Flags:");
								sender.sendMessage(ChatColor.GOLD + "-sl" + ChatColor.WHITE + " : Set the location by clicking after creation");
								sender.sendMessage(ChatColor.GOLD + "-pli <pricelistitem>" + ChatColor.WHITE + " : Instantly link a pricelist item");
								return true;
								
							} else if(args[2].equalsIgnoreCase("select") || args[2].equalsIgnoreCase("sel") || args[2].equalsIgnoreCase("s")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop select" + ChatColor.WHITE + " : Select a shop by clicking");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop select <name>" + ChatColor.WHITE + " : Select a shop by name");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The shop name");
								return true;
								
							} else if(args[2].equalsIgnoreCase("selectid") || args[2].equalsIgnoreCase("selid") || args[2].equalsIgnoreCase("sid")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop selectid <id>" + ChatColor.WHITE + " : Select a shop by id");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "id" + ChatColor.WHITE + " : The shop id");
								return true;
								
							} else if(args[2].equalsIgnoreCase("list") || args[2].equalsIgnoreCase("l")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop list [page] [flags]" + ChatColor.WHITE + " : List all shops");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "page" + ChatColor.WHITE + " : The page number");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "Flags:");
								sender.sendMessage(ChatColor.GOLD + "-a <amount>" + ChatColor.WHITE + " : Amount of items per page");
								sender.sendMessage(ChatColor.GOLD + "-ot <server/player/all>" + ChatColor.WHITE + " : The owners to list");
								return true;
								
							} else if(args[2].equalsIgnoreCase("info") || args[2].equalsIgnoreCase("i")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop info" + ChatColor.WHITE + " : View info about a selected shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("delete")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop remove" + ChatColor.WHITE + " : Remove a selected shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setname") || args[2].equalsIgnoreCase("name") || args[2].equalsIgnoreCase("sn") || args[2].equalsIgnoreCase("rename")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setname <name>" + ChatColor.WHITE + " : Set the name of a selected shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The new name");
								return true;
								
							} else if(args[2].equalsIgnoreCase("enable") || args[2].equalsIgnoreCase("e")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enable" + ChatColor.WHITE + " : Enable a selected shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disable") || args[2].equalsIgnoreCase("d")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disable" + ChatColor.WHITE + " : Disable a selected shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setowner") || args[2].equalsIgnoreCase("so") || args[2].equalsIgnoreCase("owner")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setowner <type> [name]" + ChatColor.WHITE + " : Set the owner of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "<type> [name]" + ChatColor.WHITE + " :");
								sender.sendMessage(ChatColor.GOLD + " - Server");
								sender.sendMessage(ChatColor.GOLD + " - Player <name>");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setlocation") || args[2].equalsIgnoreCase("setloc") || args[2].equalsIgnoreCase("sl") ||
									args[2].equalsIgnoreCase("location") || args[2].equalsIgnoreCase("loc") || args[2].equalsIgnoreCase("l") ||
									args[2].equalsIgnoreCase("setblock") || args[2].equalsIgnoreCase("sb") ||
									args[2].equalsIgnoreCase("block") ||
									args[2].equalsIgnoreCase("setshop") || args[2].equalsIgnoreCase("ss") ||
									args[2].equalsIgnoreCase("shop")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setlocation" + ChatColor.WHITE + " : Set the location of a shop by clicking");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setproduct") || args[2].equalsIgnoreCase("setprod") || args[2].equalsIgnoreCase("sp") ||
									args[2].equalsIgnoreCase("product") || args[2].equalsIgnoreCase("prod") || args[2].equalsIgnoreCase("p") ||
									args[2].equalsIgnoreCase("setmerchandise") || args[2].equalsIgnoreCase("setmerch") || args[2].equalsIgnoreCase("sm") ||
									args[2].equalsIgnoreCase("merchandise") || args[2].equalsIgnoreCase("merch")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setproduct [type] [value]" + ChatColor.WHITE + " : Set the product of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "type" + ChatColor.WHITE + " : <item/magicspell/other>");
								sender.sendMessage(ChatColor.GOLD + "value" + ChatColor.WHITE + " : <itemid/itemid:itemdata/spellname>");
								return true;
								
							} else if(args[2].equalsIgnoreCase("linkpricelistitem") || args[2].equalsIgnoreCase("linkpricelist") ||
									args[2].equalsIgnoreCase("lpli") ||args[2].equalsIgnoreCase("lpl") ||
									args[2].equalsIgnoreCase("setpricelistitem") || args[2].equalsIgnoreCase("setpricelist") ||
									args[2].equalsIgnoreCase("spli") ||args[2].equalsIgnoreCase("spl") ||
									args[2].equalsIgnoreCase("pricelistitem") || args[2].equalsIgnoreCase("pricelist") ||
									args[2].equalsIgnoreCase("pli") ||args[2].equalsIgnoreCase("pl")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop linkpricelistitem [name]" + ChatColor.WHITE + " : Link a pricelist item to a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : Pricelist item name or <pricelist:itemname>");
								return true;
								
							} else if(args[2].equalsIgnoreCase("unlinkpricelistitem") || args[2].equalsIgnoreCase("unlinkpricelist") ||
									args[2].equalsIgnoreCase("upli") ||args[2].equalsIgnoreCase("upl") ||
									args[2].equalsIgnoreCase("removepricelistitem") || args[2].equalsIgnoreCase("removepricelist") ||
									args[2].equalsIgnoreCase("rpli") ||args[2].equalsIgnoreCase("rpl")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop unlinkpricelistitem" + ChatColor.WHITE + " : Unlink a pricelist item from a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setshowitem") || args[2].equalsIgnoreCase("setshow") || args[2].equalsIgnoreCase("ssi") ||
									args[2].equalsIgnoreCase("showitem") || args[2].equalsIgnoreCase("show")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setshowitem [item]" + ChatColor.WHITE + " : Set the show item from a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "item" + ChatColor.WHITE + " : Item id or <itemid:itemdata>");
								return true;
								
							} else if(args[2].equalsIgnoreCase("enableshowitem") || args[2].equalsIgnoreCase("enableshow") ||
									args[2].equalsIgnoreCase("esi") || args[2].equalsIgnoreCase("es")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableshowitem" + ChatColor.WHITE + " : Enable the show item of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disableshowitem") || args[2].equalsIgnoreCase("disableshow") ||
									args[2].equalsIgnoreCase("dsi") || args[2].equalsIgnoreCase("ds")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableshowitem" + ChatColor.WHITE + " : Disable the show item of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disableshowitem") || args[2].equalsIgnoreCase("disableshow") ||
									args[2].equalsIgnoreCase("dsi") || args[2].equalsIgnoreCase("ds")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableshowitem" + ChatColor.WHITE + " : Disable the show item of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("enablebuy") || args[2].equalsIgnoreCase("enablebuyable") || args[2].equalsIgnoreCase("eb")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enablebuy" + ChatColor.WHITE + " : Enable buy of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disablebuy") || args[2].equalsIgnoreCase("disablebuyable") || args[2].equalsIgnoreCase("db")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablebuy" + ChatColor.WHITE + " : Disable buy of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("enablesell") || args[2].equalsIgnoreCase("enablesellable") || args[2].equalsIgnoreCase("eb")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enablesell" + ChatColor.WHITE + " : Enable sell of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disablesell") || args[2].equalsIgnoreCase("disablesellable") || args[2].equalsIgnoreCase("ds")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablesell" + ChatColor.WHITE + " : Disable sell of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setbuyprice") || args[2].equalsIgnoreCase("setbuy") ||args[2].equalsIgnoreCase("sb")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setbuyprice <price>" + ChatColor.WHITE + " : Set the buy price of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "price" + ChatColor.WHITE + " : The new price");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setsellprice") || args[2].equalsIgnoreCase("setsell") ||args[2].equalsIgnoreCase("ss")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setsellprice <price>" + ChatColor.WHITE + " : Set the sell price of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "price" + ChatColor.WHITE + " : The new price");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setbuystack") || args[2].equalsIgnoreCase("sbs") ||
									args[2].equalsIgnoreCase("setbuystackquantity") || args[2].equalsIgnoreCase("sbsq")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setbuystack <quantity>" + ChatColor.WHITE + " : Set the buy stack quantity of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "quantity" + ChatColor.WHITE + " : The buy stack quantity");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disablebuystack") || args[2].equalsIgnoreCase("dsbs") ||
									args[2].equalsIgnoreCase("disablebuystackquantity") || args[2].equalsIgnoreCase("dsbsq")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablebuystack" + ChatColor.WHITE + " : Disable the buy stack quantity of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setsellstack") || args[2].equalsIgnoreCase("sss") ||
									args[2].equalsIgnoreCase("setsellstackquantity") || args[2].equalsIgnoreCase("sssq")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setsellstack <quantity>" + ChatColor.WHITE + " : Set the sell stack quantity of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "quantity" + ChatColor.WHITE + " : The sell stack quantity");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disablesellstack") || args[2].equalsIgnoreCase("dsss") ||
									args[2].equalsIgnoreCase("disablesellstackquantity") || args[2].equalsIgnoreCase("dsssq")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablesellstack" + ChatColor.WHITE + " : Disable the sell stack quantity of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setmaximumbuyquantity") || args[2].equalsIgnoreCase("setmaxbuyquantity") || args[2].equalsIgnoreCase("smbq") ||
									args[2].equalsIgnoreCase("setmaximumbuy") || args[2].equalsIgnoreCase("setmaxbuy")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setmaximumbuyquantity <quantity>" + ChatColor.WHITE + " : Set the minimum sell quantity of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "quantity" + ChatColor.WHITE + " : The minimum sell quantity");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disableminimumbuyquantity") || args[2].equalsIgnoreCase("disableminbuyquantity") || args[2].equalsIgnoreCase("dmbq") ||
									args[2].equalsIgnoreCase("disableminimumbuy") || args[2].equalsIgnoreCase("disableminbuy")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableminimumbuyquantity" + ChatColor.WHITE + " : Disable the minimum sell quantity of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setmaximumbuyquantity") || args[2].equalsIgnoreCase("setmaxbuyquantity") || args[2].equalsIgnoreCase("smbq") ||
									args[2].equalsIgnoreCase("setmaximumbuy") || args[2].equalsIgnoreCase("setmaxbuy")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setmaximumbuyquantity <quantity>" + ChatColor.WHITE + " : Set the maximum sell quantity of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "quantity" + ChatColor.WHITE + " : The maximum sell quantity");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disablemaximumbuyquantity") || args[2].equalsIgnoreCase("disablemaxbuyquantity") || args[2].equalsIgnoreCase("dmbq") ||
									args[2].equalsIgnoreCase("disablemaximumbuy") || args[2].equalsIgnoreCase("disablemaxbuy")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablemaximumbuyquantity" + ChatColor.WHITE + " : Disable the maximum sell quantity of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setminimumsellquantity") || args[2].equalsIgnoreCase("setminsellquantity") || args[2].equalsIgnoreCase("smsq") ||
									args[2].equalsIgnoreCase("setminimumsell") || args[2].equalsIgnoreCase("setmaxsell")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setmaximumsellquantity <quantity>" + ChatColor.WHITE + " : Set the minimum sell quantity of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "quantity" + ChatColor.WHITE + " : The minimum sell quantity");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disableminimumsellquantity") || args[2].equalsIgnoreCase("disableminsellquantity") || args[2].equalsIgnoreCase("dmsq") ||
									args[2].equalsIgnoreCase("disableminimumsell") || args[2].equalsIgnoreCase("disableminsell")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableminimumsellquantity" + ChatColor.WHITE + " : Disable the minimum sell quantity of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setmaximumsellquantity") || args[2].equalsIgnoreCase("setmaxsellquantity") || args[2].equalsIgnoreCase("smsq") ||
									args[2].equalsIgnoreCase("setmaximumsell") || args[2].equalsIgnoreCase("setmaxsell")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setmaximumsellquantity <quantity>" + ChatColor.WHITE + " : Set the maximum sell quantity of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "quantity" + ChatColor.WHITE + " : The maximum sell quantity");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disablemaximumsellquantity") || args[2].equalsIgnoreCase("disablemaxsellquantity") || args[2].equalsIgnoreCase("dmsq") ||
									args[2].equalsIgnoreCase("disablemaximumsell") || args[2].equalsIgnoreCase("disablemaxsell")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disablemaximumsellquantity" + ChatColor.WHITE + " : Disable the maximum sell quantity of a shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("enableinstantbuy") || args[2].equalsIgnoreCase("eib")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableinstantbuy" + ChatColor.WHITE + " : Enable instant buy of a selected shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disableinstantbuy") || args[2].equalsIgnoreCase("dib")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableinstantbuy" + ChatColor.WHITE + " : Disable instant buy a selected shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setinstantbuyquantity") || args[2].equalsIgnoreCase("setinstantbuy") ||
									args[2].equalsIgnoreCase("sibq") || args[2].equalsIgnoreCase("ibq")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setinstantbuyquantity <quantity>" + ChatColor.WHITE + " : Set the instant buy quantity of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "quantity" + ChatColor.WHITE + " : The new instant buy quantity");
								return true;
								
							} else if(args[2].equalsIgnoreCase("enableinstantsell") || args[2].equalsIgnoreCase("eis")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop enableinstantsell" + ChatColor.WHITE + " : Enable instant sell of a selected shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disableinstantsell") || args[2].equalsIgnoreCase("dis")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop disableinstantsell" + ChatColor.WHITE + " : Disable instant sell a selected shop");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setinstantsellquantity") || args[2].equalsIgnoreCase("setinstantsell") ||
									args[2].equalsIgnoreCase("sisq") || args[2].equalsIgnoreCase("ibs")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " shop setinstantsellquantity <quantity>" + ChatColor.WHITE + " : Set the instant sell quantity of a shop");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "quantity" + ChatColor.WHITE + " : The new instant sell quantity");
								return true;
								
							}
						}
						if(args[1].equals("booth") || args[1].equals("booths") || args[1].equals("b")) {
							if(args.length == 2) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth create [name] [flags]" + ChatColor.WHITE + " : Create a booth");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth select" + ChatColor.WHITE + " : Select a booth by clicking");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth select <name>" + ChatColor.WHITE + " : Select a booth by name");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth selectid <id>" + ChatColor.WHITE + " : Selece a booth by id");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth list [page] [flags]" + ChatColor.WHITE + " : List all booths");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth info" + ChatColor.WHITE + " : View info about a selected booth");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth remove" + ChatColor.WHITE + " : Remove a selected booth");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setname <name>" + ChatColor.WHITE + " : Set the name of a selected booth");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth enable" + ChatColor.WHITE + " : Enable a selected booth");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth disable" + ChatColor.WHITE + " : Disable a selected booth");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setlocation" + ChatColor.WHITE + " : Set the location of a selected booth");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setclaimprice <price>" + ChatColor.WHITE + " : Set the claim price of a ...");
								return true;
							}
							if(args[2].equalsIgnoreCase("create") || args[2].equalsIgnoreCase("c")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth create [name]" + ChatColor.WHITE + " : Create a new booth");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The booth name");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "Flags:");
								sender.sendMessage(ChatColor.GOLD + "-sl" + ChatColor.WHITE + " : Set the location by clicking after creation");
								return true;
								
							} else if(args[2].equalsIgnoreCase("select") || args[2].equalsIgnoreCase("sel") || args[2].equalsIgnoreCase("s")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth select" + ChatColor.WHITE + " : Select a booth by clicking");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth select <name>" + ChatColor.WHITE + " : Select a booth by name");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The booth name");
								return true;
								
							} else if(args[2].equalsIgnoreCase("selectid") || args[2].equalsIgnoreCase("selid") || args[2].equalsIgnoreCase("sid")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth selectid <id>" + ChatColor.WHITE + " : Select a booth by id");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "id" + ChatColor.WHITE + " : The booth id");
								return true;
								
							} else if(args[2].equalsIgnoreCase("list") || args[2].equalsIgnoreCase("l")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth list [page] [flags]" + ChatColor.WHITE + " : List all booths");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "page" + ChatColor.WHITE + " : The page number");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "Flags:");
								sender.sendMessage(ChatColor.GOLD + "-a <amount>" + ChatColor.WHITE + " : Amount of items per page");
								return true;
								
							} else if(args[2].equalsIgnoreCase("info") || args[2].equalsIgnoreCase("i")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth info" + ChatColor.WHITE + " : View info about a selected booth");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setname") || args[2].equalsIgnoreCase("name") || args[2].equalsIgnoreCase("sn") || args[2].equalsIgnoreCase("rename")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setname <name>" + ChatColor.WHITE + " : Set the name of a selected booth");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The new name");
								return true;
								
							} else if(args[2].equalsIgnoreCase("enable") || args[2].equalsIgnoreCase("e")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth enable" + ChatColor.WHITE + " : Enable a selected booth");
								return true;
								
							} else if(args[2].equalsIgnoreCase("disable") || args[2].equalsIgnoreCase("d")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth disable" + ChatColor.WHITE + " : Disable a selected booth");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setlocation") || args[2].equalsIgnoreCase("setloc") || args[2].equalsIgnoreCase("sl") ||
									args[2].equalsIgnoreCase("location") || args[2].equalsIgnoreCase("loc") || args[2].equalsIgnoreCase("l") ||
									args[2].equalsIgnoreCase("setblock") || args[2].equalsIgnoreCase("sb") ||
									args[2].equalsIgnoreCase("block") ||
									args[2].equalsIgnoreCase("setbooth") ||
									args[2].equalsIgnoreCase("booth")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setlocation" + ChatColor.WHITE + " : Set the location of a booth by clicking");
								return true;
								
							} else if(args[2].equalsIgnoreCase("setclaimprice") || args[2].equalsIgnoreCase("setprice") ||
									args[2].equalsIgnoreCase("scp") || args[2].equalsIgnoreCase("sp")) {
								// View the help
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
								sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " booth setclaimprice <price>" + ChatColor.WHITE + " : Set the claim price of a booth");
								sender.sendMessage(" ");
								sender.sendMessage(ChatColor.GOLD + "price" + ChatColor.WHITE + " : The new claim price");
								return true;
								
							}
						}
						if(args[1].equals("save")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " save" + ChatColor.WHITE + " : Save all data");
							return true;
						}
						if(args[1].equals("reload") || args[1].equals("load")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " reload" + ChatColor.WHITE + " : Reload all data");
							return true;
						}
						if(args[1].equals("status") || args[1].equals("statics") || args[1].equals("stats") || args[1].equals("s")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " status" + ChatColor.WHITE + " : View major plugin status");
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GOLD + "This command is used for troubleshooting problems");
							return true;
						}
						if(args[1].equals("version") || args[1].equals("ver") || args[1].equals("v") ||
								args[1].equals("info") || args[1].equals("i") || 
								args[1].equals("about") || args[1].equals("a")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " version" + ChatColor.WHITE + " : View plugin version info");
							return true;
						}
						
						
						// Check wrong command values
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
						return true;
					}
				}
				
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
			return true;
			
		} else if (commandLabel.equalsIgnoreCase("shop") || commandLabel.equalsIgnoreCase("shops") || commandLabel.equalsIgnoreCase("s")) {
			// Command arguments should already be combined by the executor of this function
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
				
				// Help commands (/ss help [sub-categories])

				if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
					if(args.length == 1) {
						// View the help
						sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " help [args]" + ChatColor.WHITE + " : View help");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " create [name]" + ChatColor.WHITE + " : Create a new shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " select" + ChatColor.WHITE + " : Select a shop by clicking");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " select <name>" + ChatColor.WHITE + " : Select a shop by name");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " selectid <id>" + ChatColor.WHITE + " : Select a shop by id");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " list [page] [flags]" + ChatColor.WHITE + " : List your shops");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " info" + ChatColor.WHITE + " : View your selected shop info");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " remove" + ChatColor.WHITE + " : Remove your selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " setname <setname>" + ChatColor.WHITE + " : Set your sselected hop name");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " enable" + ChatColor.WHITE + " : Enable your selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " disable" + ChatColor.WHITE + " : Disable your selected shop");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " setlocation" + ChatColor.WHITE + " : Set your selected shop location");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " setprice <price>" + ChatColor.WHITE + " : Set your selected shop price");
						return true;
					} else {
						if(args[1].equals("create") || args[1].equals("c")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " create [name]" + ChatColor.WHITE + " : Create a new shop");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The shop name");
							return true;
						}
						if(args[1].equals("select") || args[1].equals("sel") || args[1].equals("s")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " select" + ChatColor.WHITE + " : Select your shop by clicking");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " select <name>" + ChatColor.WHITE + " : Select your shop by name");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The shop name");
							return true;
						}
						if(args[1].equals("selectid") || args[1].equals("selid") || args[1].equals("sid")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " selectid <id>" + ChatColor.WHITE + " : Select your shop by id");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GOLD + "id" + ChatColor.WHITE + " : The shop id");
							return true;
						}
						if(args[1].equals("list") || args[1].equals("l")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " list [page] [flags]" + ChatColor.WHITE + " : List your shops");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GOLD + "page" + ChatColor.WHITE + " : The page number");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GREEN + "Flags:");
							sender.sendMessage(ChatColor.GOLD + "-a <amount>" + ChatColor.WHITE + " : Amount of items per page");
							return true;
						}
						if(args[1].equals("info") || args[1].equals("i")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " info" + ChatColor.WHITE + " : View your selected shop info");
							return true;
						}
						if(args[1].equals("remove") || args[1].equals("delete")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " remove" + ChatColor.WHITE + " : Remove your selected shop");
							return true;
						}
						if(args[1].equals("setname") || args[1].equals("name") || args[1].equals("sn") || args[1].equals("n") || args[1].equals("rename")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " setname <name>" + ChatColor.WHITE + " : Set your selected shop name");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GOLD + "name" + ChatColor.WHITE + " : The new name");
							return true;
						}
						if(args[1].equals("enable") || args[1].equals("e")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " enable" + ChatColor.WHITE + " : Enable your selected shop");
							return true;
						}
						if(args[1].equals("disable") || args[1].equals("d")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " disable" + ChatColor.WHITE + " : Disable your selected shop");
							return true;
						}
						if(args[1].equalsIgnoreCase("setlocation") || args[1].equalsIgnoreCase("setloc") || args[1].equalsIgnoreCase("sl") ||
								args[1].equalsIgnoreCase("location") || args[1].equalsIgnoreCase("loc") || args[1].equalsIgnoreCase("l") ||
								args[1].equalsIgnoreCase("setblock") || args[1].equalsIgnoreCase("sb") ||
								args[1].equalsIgnoreCase("block") ||
								args[1].equalsIgnoreCase("setshop") || args[1].equalsIgnoreCase("ss") ||
								args[1].equalsIgnoreCase("shop")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " setlocation" + ChatColor.WHITE + " : Set your selected shop location");
							return true;
						}
						if(args[1].equals("setprice") || args[1].equals("setbuyprice") || args[1].equals("price") ||
								args[1].equals("sp") || args[1].equals("sbp") || args[1].equals("p")) {
							// View the help
							sender.sendMessage("");
							sender.sendMessage(ChatColor.GREEN + "==========[ SIMPLE SHOWCASE HELP ]==========");
							sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " setprice <price>" + ChatColor.WHITE + " : Set your selected shop price");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GOLD + "price" + ChatColor.WHITE + " : The new product price");
							return true;
						}
						
						// Check wrong command values
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
						return true;
					}
				}
				
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
			return true;
			
		}
		return false;
	}
}