package ennesima;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;



import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin implements Listener{
	public static Main instance;
	
	@Override
	public void onDisable() {
		/*
		File customYml = new File("plugins/dungeonsData/dungeons.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
		List<String> listaDungeon = customConfig.getStringList("dungeons.list");
		int num = listaDungeon.size();
		for(int i = 0; i < num; i++) {
			String cache = listaDungeon.get(i);
			String loaded = customConfig.getString("dungeons._" + cache + ".loaded");
			if(loaded==null || loaded=="" || loaded.equalsIgnoreCase("true")) {
				World unload = Bukkit.getWorld("/DungeonMaps/_" + cache);
		    	unloadWorld(unload);
		    	customConfig.set("dungeons._" + cache + ".loaded", "false");
			}
		}
		saveCustomYml(customConfig,customYml);
		*/
		getLogger().info("Disabilitato");
	}
	  @Override
	  public void onEnable() {
		  instance=this;
		  
		getCommand("dungeons").setExecutor(new comandi());
		getCommand("dk").setExecutor(new comandi());
		getCommand("level").setExecutor(new cmdLevel());
		this.getCommand("level").setTabCompleter(new cmdLevel());
	    getServer().getPluginManager().registerEvents(this, this);
	    Bukkit.getPluginManager().registerEvents(new ascoltami(), this);
	    if (getConfig().getString("colore") == null) {
	    	getConfig().set("colore", "rosso");
	    	saveConfig();
	    } else {
	    	String colore = getConfig().getString("colore");
	    	getLogger().info(colore);
	    }
	    File customYml = new File(getDataFolder()+"/customYmlFile.yml");
	    FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
	    customConfig.set("path.to.boolean", true);
	    saveCustomYml(customConfig,customYml);
	    
	    ////worldUtility mondi = new worldUtility();
	    //mondi.dupeWorld();
	 // The world to copy
	    ////World source = Bukkit.getWorld("world");
	    ////File sourceFolder = source.getWorldFolder();
	     
	    // The world to overwrite when copying
	   //// File targetFolder = new File(Bukkit.getWorldContainer(), "/test/NewWorld");
	    ////mondi.copyWorld(sourceFolder, targetFolder);
	    
	    //Per cancellare qui:
	    //World delete = Bukkit.getWorld("/test/NewWorld");
	    //File deleteFolder = delete.getWorldFolder();
	    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
	    	new placeholders().register();
      }
	    //mondi.deleteWorld(deleteFolder);
	  }

	  @EventHandler
	  public void onLogin(PlayerLoginEvent event) {
	    getLogger().log(Level.INFO, "Player " + event.getPlayer().getName() + " is logging in!");
	     String colore = getConfig().getString("colore");
	    getLogger().info(colore);
	    }
	  public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
		  try {
		  ymlConfig.save(ymlFile);
		  } catch (IOException e) {
		  e.printStackTrace();
		  }
		 }
	  public void impostawarp(String nome, Location location) {
		  File customYml = new File(getDataFolder()+"/customYmlFile.yml");
		  FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
		  customConfig.set("warps."+nome+".posizione", location.toString());
		  salvawarp();
	  }
	  public void salvawarp() {
		    File customYml = new File(getDataFolder()+"/customYmlFile.yml");
		    FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
		    saveCustomYml(customConfig,customYml);
	  }
	  @SuppressWarnings("unused")
	private static void unloadWorld(World world) {
	        if(!world.equals(null)) {
	            for(Player p : world.getPlayers()){
	                p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
	            }
	            Bukkit.getServer().unloadWorld(world, false);
	            System.out.println(world.getName() + " unloaded!");
	        }
	    }
	  
	 
}