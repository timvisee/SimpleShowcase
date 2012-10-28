package com.timvisee.SimpleShowcase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.timvisee.SimpleShowcase.Metrics;
import com.timvisee.SimpleShowcase.Metrics.Graph;

public class SimpleShowcase extends JavaPlugin {
	
	static final Logger log = Logger.getLogger("Minecraft");
	
	// Additional version info
	private int versionCode = 2;

	private final SimpleShowcaseBlockListener blockListener = new SimpleShowcaseBlockListener(this);
	private final SimpleShowcaseEntityListener entityListener = new SimpleShowcaseEntityListener(this);
	private final SimpleShowcaseInventoryListener inventoryListener = new SimpleShowcaseInventoryListener(this);
	private final SimpleShowcasePlayerListener playerListener = new SimpleShowcasePlayerListener(this);
	private final SimpleShowcaseWorldListener worldListener = new SimpleShowcaseWorldListener(this);
	//private final SimpleShowcaseConfigConverter configConverter = new SimpleShowcaseConfigConverter(this);

    // Shops and Booths manager
	private SSShopManager shops = new SSShopManager(this);
	private SSBoothManager booths = new SSBoothManager(this);
	
	// Permissions and Economy manager
	private PermissionsManager permissionsManager;
	private EconomyManager economyManager;
	
	// Player mode manager
	private PlayerModeManager playerModeManager;
	
	// Command handler
	private final CommandHandler commandHandler = new CommandHandler(this);
	
	private SSPricelistManager pricelistManager;
	
	// Enabled date
	private Date enabledDate = new Date();
	
	File configFile = new File("plugins/SimpleShowcase/config.yml");

	// Update Checker
	boolean isUpdateAvailable = false;
	String newestVersion = "1.0";
	
	public void onEnable() {
		// Reset the enabled date, to calculate the 'running time'
		enabledDate = new Date();
		
		// Autogenerate the default files
		try {
			checkConigFilesExist();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// define the default config path
		this.configFile = new File(getDataFolder(), "config.yml");
		
		// Defining the plugin manager
		PluginManager pm = getServer().getPluginManager();
		
		// TODO Convert old config files
		
		// Load all plugin data
		loadAll();
		
		// Setup the managers
		setupPermissionsManager();
		setupEconomyManager();
		
		// Setup the player mode manager
		setupPlayerModeManager();
		
		// Setup the pricelist manager
		setupPricelistManager();
		
		// Register the event listeners
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.entityListener, this);
		pm.registerEvents(this.inventoryListener, this);
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.worldListener, this);
		
		// Remove all duped show items
		getShopManager().removeAllDupedShopShowItems();
		
		// Spawn all the shop items
		getShopManager().respawnAllShopShowItems();
		
		// Create a timer for respawning the items every time in shops (if needed)
		if(getConfig().getBoolean("shops.showItems.respawn.enabled", true)) {
			getServer().getScheduler().scheduleAsyncRepeatingTask( this, new Runnable() { public void run() { 
				getShopManager().removeAllShopShowItems();
				getShopManager().respawnAllShopShowItems(); 
			} }, getConfig().getInt("shops.showItems.respawn.interval", 4800), getConfig().getInt("shops.showItems.respawn.interval", 4800));
		}
		if(getConfig().getBoolean("shops.showItems.respawnIncorrect.enabled", true)) {
			getServer().getScheduler().scheduleAsyncRepeatingTask( this, new Runnable() { public void run() { getShopManager().respawnAllIncorrectShopShowItems(); } }, getConfig().getInt("shops.showItems.respawnIncorrect.interval", 100), getConfig().getInt("shops.showItems.respawnIncorrect.interval", 300));
		}
		if(getConfig().getBoolean("autoSave.enabled", true)) {
			getServer().getScheduler().scheduleAsyncRepeatingTask( this, new Runnable() { public void run() { saveAll(); } }, getConfig().getInt("autoSave.interval", 6000), getConfig().getInt("autoSave.interval", 6000));
		}
		
		// Setup Metrics
		setupMetrics();
		
		// Plugin has been enabled. Show a message in the console
		PluginDescriptionFile pdfFile = getDescription();
		log.info("[SimpleShowcase] Simple Showcase v" + pdfFile.getVersion() + " Enabled");
	}

	public void onDisable() {
		// Remove all shop show items
		log.info("[SimpleShowcase] Removing " + String.valueOf(getShopManager().countSpawnedShopShowItems()) + " shop items...");
		getShopManager().removeAllShopShowItems();
		
		// Cloase all player shop inventories
		getShopManager().getShopInventoryManager().closeAllShopInventories();
		
		// Save shop list (will be saved on new creation!)
		saveAll();
		
		// Show disabled message
		log.info("[SimpleShowcase] Simple Showcase Disabled");
	}

	
	public void checkConigFilesExist() throws Exception {
		if(!getDataFolder().exists()) {
			log.info("[SimpleShowcase] Creating new SimpleShowcase folder");
			getDataFolder().mkdirs();
		}
		File configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			log.info("[SimpleShowcase] Generating default config file");
			copy(getResource("res/DefaultFiles/config.yml"), configFile);
		}
		File dataFolder = new File(getDataFolder(), "data");
		if(!dataFolder.exists()) {
			log.info("[SimpleShowcase] Generating default data folder");
			dataFolder.mkdirs();
		}
		File pricelistFolder = new File(getDataFolder(), "pricelist");
		if(!pricelistFolder.exists()) {
			log.info("[SimpleShowcase] Generating new 'pricelist' folder");
			pricelistFolder.mkdirs();
			copy(getResource("res/DefaultFiles/pricelist/pricelist_main.yml"), new File(pricelistFolder, "pricelist_main.yml"));
			copy(getResource("res/DefaultFiles/pricelist/pricelist_example.yml"), new File(pricelistFolder, "pricelist_example.yml"));
		}
	}
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/*public boolean checkUpdates() {
		SafeCreeperUpdateChecker scuc = new SafeCreeperUpdateChecker(this);
		isUpdateAvailable = scuc.checkUpdates();
		newestVersion = scuc.getLastVersion();
		
		if(isUpdateAvailable) {
			log.info("[SafeCreeper] New version available, version " + newestVersion + ".");
		}
		
		return isUpdateAvailable;
	}*/
	
	/**
	 * Setup the metrics statics feature
	 * @return false if an error occurred
	 */
	public boolean setupMetrics() {
		try {
		    Metrics metrics = new Metrics(this);
		    // Construct a graph, which can be immediately used and considered as valid
		    // Amount of shops created
		    Graph graph = metrics.createGraph("Shops and Booths created");
		    graph.addPlotter(new Metrics.Plotter("Shops") {
	            @Override
	            public int getValue() {
	            	return getShopManager().countShops();
	            }
		    });
		    graph.addPlotter(new Metrics.Plotter("Booths") {
	            @Override
	            public int getValue() {
	            	return getBoothManager().countBooths();
	            }
		    });
		    graph.addPlotter(new Metrics.Plotter("Pricelist Items") {
	            @Override
	            public int getValue() {
	            	return getPricelistManager().countPricelistItems();
	            }
		    });
		    
		    // Used permissions systems
		    Graph graph2 = metrics.createGraph("Permisison Plugin Usage");
		    graph2.addPlotter(new Metrics.Plotter(getPermissionsManager().getUsedPermissionsSystemType().getName()) {
	            @Override
	            public int getValue() {
	            	return 1;
	            }
		    });
		    
		    // Used economy systems
		    Graph graph3 = metrics.createGraph("Economy Plugin Usage");
		    graph3.addPlotter(new Metrics.Plotter(getEconomyManager().getUsedEconomySystemType().getName()) {
	            @Override
	            public int getValue() {
	            	return 1;
	            }
		    });
		    metrics.start();
		    return true;
		} catch (IOException e) {
		    // Failed to submit the statics :-(
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Setup the economy manager
	 */
	public void setupEconomyManager() {
		// Setup the economy manager
		this.economyManager = new EconomyManager(this.getServer(), "SimpleShowcase");
		this.economyManager.setup();
	}
	
	/**
	 * Get the economy manager
	 * @return economy manager
	 */
	public EconomyManager getEconomyManager() {
		return this.economyManager;
	}
	
	/**
	 * Setup the player mode manager
	 */
	public void setupPlayerModeManager() {
		this.playerModeManager = new PlayerModeManager();
	}
	
	/**
	 * Get the player mode manager
	 * @return player mode manager
	 */
	public PlayerModeManager getPlayerModeManager() {
		return this.playerModeManager;
	}
	
	/**
	 * Setup the permissions manager
	 */
	public void setupPermissionsManager() {
		// Setup the permissions manager
		this.permissionsManager = new PermissionsManager(this.getServer(), "SimpleShowcase");
		this.permissionsManager.setup();
	}
	
	/**
	 * Get the permissions manager
	 * @return permissions manager
	 */
	public PermissionsManager getPermissionsManager() {
		return this.permissionsManager;
	}
	
	/**
	 * Get the shop manager
	 * @return shop manager
	 */
	public SSShopManager getShopManager() {
		return this.shops;
	}
	
	/**
	 * Get the booth manager
	 * @return booth manager
	 */
	public SSBoothManager getBoothManager() {
		return this.booths;
	}
	
	public void setupPricelistManager() {
		if(this.pricelistManager == null) {
			this.pricelistManager = new SSPricelistManager(this);
			this.pricelistManager.reloadList();
			this.pricelistManager.applyItemsToShops();
		}	
	}
	
	public SSPricelistManager getPricelistManager() {
		return this.pricelistManager;
	}
	
	/**
	 * Get the current version code of the plugin. The version code is usually used in config files
	 * @return the version code
	 */
	public int getVersionCode() {
		return this.versionCode;
	}
	
	/**
	 * Get the date when the plugin was enabled
	 * @return the enabled date
	 */
	public Date getEnabledDate() {
		return this.enabledDate;
	}
	
	/**
	 * Reload all data and systems
	 */
	public void reloadAll() {
		// Reload all the files
		loadAll();
		
		// Reload the permissions and economy managers
		setupPermissionsManager();
		setupEconomyManager();
	}
	
	/**
	 * Reload the shops from the external file
	 */
	public void reloadShops() {
		loadShops();
	}
	
	/**
	 * Reload the permissions system
	 */
	public void reloadPermissionsSystem() {
		setupPermissionsManager();
	}
	
	/**
	 * Reload the economy system
	 */
	public void reloadEconomySystem() {
		setupEconomyManager();
	}
	
	/**
	 * Save all the data, for example the shops
	 */
	public void saveAll() {
		// Save the shops
		saveShops();
		saveBooths();
	}
	
	/**
	 * Save the list with shops
	 */
	public void saveShops() {
		shops.save();
	}
	
	/**
	 * Save the list with booths
	 */
	public void saveBooths() {
		booths.save();
	}
	
	/**
	 * Load all the data, for example the shops
	 */
	public void loadAll() {
		// Reload the config files
		if(this.configFile.exists()) {
			try {
				getConfig().load(this.configFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
			log.info("[SimpleShowcase] Loaded config.yml file!");
		} else {
			log.info("[SimpleShowcase] Config.yml file not found, unable to reload!");
		}
		
		
		// Load the shops and booths
	    loadShops();
	    loadBooths();
	    
	    // Load and apply the pricelist
	    if(getPricelistManager() == null)
	    	setupPricelistManager();
	    else {
	    	getPricelistManager().reloadList();
			getPricelistManager().applyItemsToShops();
	    }
			
	}
	
	/**
	 * Load the list with shops
	 */
	public void loadShops() {
		shops.load();
	}
	
	/**
	 * Load the list with booths
	 */
	public void loadBooths() {
		booths.load();
	}
	
	/**
	 * Convert a string to a double variable on the right way
	 * @param doubleString The double string
	 * @return The double
	 */
	public double stringToDouble(String doubleString) {
		doubleString = doubleString.replace(",", ".");
		BigDecimal d = new BigDecimal(doubleString);
		return d.doubleValue();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Pass the command to the command handler
		return commandHandler.onCommand(sender, cmd, commandLabel, args);
	}
}
