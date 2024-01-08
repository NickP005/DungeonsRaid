package ennesima;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class levels {
	/*
	public void eseguiLivello(String numeroLivello, String idPartita, String dungeonName, Player player) {
		 File customYml = new File("plugins/dungeonsData/dungeons.yml");
		 FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
		 
		 World w = Bukkit.getWorld("/DungeonMaps/" + idPartita);
		 List<String> blocchisvuotare = customConfig.getStringList("dungeons."+dungeonName+".barriers."+numeroLivello);
		 int numeroiter = blocchisvuotare.size();
		 for (int i = 0; i < numeroiter; i++) {
			  String cacheblock = blocchisvuotare.get(i);
			  Location locacache = getLocationString(w, cacheblock);
			  locacache.getBlock().setType(Material.AIR);
			}
		 String messaggio = customConfig.getString(dungeonName + ".levels."+numeroLivello+".message");
		 if(messaggio==null) {} else {
			 ((Player) player).sendMessage(ChatColor.YELLOW + messaggio);
		 }
		 String dungetype = customConfig.getString(dungeonName + ".levels."+numeroLivello+".type");
		 if(dungetype==null) {
			 return;
		 }
		 switch (dungetype)
		 {
		      case " 5_seconds_cooldown":
		    	  BukkitScheduler scheduler = Bukkit.getScheduler();
			        scheduler.scheduleSyncDelayedTask(JavaPlugin.getProvidingPlugin(Main.class) , new Runnable() {
			            @Override
			            public void run() {
			            	eseguiLivello(numeroLivello + 1, idPartita, dungeonName, player);
			            }
			        }, 100L);
		      //Java code
		      ;
		      case "scemo":
		      //Java code
		      ;
		      
		    			  
		      default:
		      //Java code
		    	  return;
		 }
	}
	*/
	static public Location getLocationString(World ww, final String s) {
	    if (s == null || s.trim() == "") {
	        return null;
	    }
	    final String[] parts = s.split(":");
	    if (parts.length == 4) {
	        //final World w = Bukkit.getServer().getWorld(parts[0]);
	        final World w = ww;
	        final int x = Integer.parseInt(parts[1]);
	        final int y = Integer.parseInt(parts[2]);
	        final int z = Integer.parseInt(parts[3]);
	        return new Location(w, x, y, z);
	    }
	    return null;
	    }

	public void eseguiLivello(String Livello, String idPartita, String dungeonName, Player player) {
		File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
		FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
		//((Player)player).sendMessage("Il dungeon si chiama " + dungeonName + " e se nella partita " + idPartita);
		//((Player)player).sendMessage("Il livello si chiama " + Livello);
		World w = Bukkit.getWorld("/DungeonMaps/" + idPartita);
		 List<String> blocchisvuotare = dungeonConfig.getStringList("level."+Livello+".barriers");
		 int numeroiter = blocchisvuotare.size();
		 for (int i = 0; i < numeroiter; i++) {
			  String cacheblock = blocchisvuotare.get(i);
			  //String[] prova = cacheblock.split(Pattern.quote("$"));
			  //((Player)player).sendMessage("cacheblock " + cacheblock + " \n blocco location: " + prova[0]);
			  String blocco = cacheblock.split(Pattern.quote("$"))[0];
			  Material materiale = Material.getMaterial(cacheblock.split(Pattern.quote("$"))[1]);
			  Location locacache = getLocationString(w, blocco);
			  locacache.getBlock().setType(materiale);
			}
		 List<String> nextLevels = dungeonConfig.getStringList("level."+Livello+".next");
		 //((Player)player).sendMessage("Ottenuta la lista dei livelli");
		 int numeroiter2 = nextLevels.size();
		 //((Player)player).sendMessage("La lista e di numero " + numeroiter2);
		 
		 for (int r = 0; r < numeroiter2; r++) {
			  String[] cacheblock2 = nextLevels.get(r).split(Pattern.quote("-"));
			  ((Player)player).sendMessage("Sono al numero " + r);
			  //String[] prova2 = cacheblock2.split(Pattern.quote("-"));
			  //((Player)player).sendMessage("cacheblock " + cacheblock2 + " \n blocco location: " + prova2[0]);
			  String trigger = cacheblock2[0];
			  String endLevel = cacheblock2[1];
			  if(endLevel.equalsIgnoreCase("stop")) {
		    	  ((Player)player).sendMessage(ChatColor.DARK_BLUE+"Stop is starting");
		    	  stopGame(idPartita);
		    	  return;
			  }
				      if(trigger.equalsIgnoreCase("5s_delay")) {
				    	  BukkitScheduler scheduler = Bukkit.getScheduler();
					        scheduler.scheduleSyncDelayedTask(JavaPlugin.getProvidingPlugin(Main.class) , new Runnable() {
					            @Override
					            public void run() {
					            	eseguiLivello(endLevel, idPartita, dungeonName, player);
					            }
					        }, 100L);
				      } else if(trigger.equalsIgnoreCase("1s_delay")) {
				    	  BukkitScheduler scheduler = Bukkit.getScheduler();
					        scheduler.scheduleSyncDelayedTask(JavaPlugin.getProvidingPlugin(Main.class) , new Runnable() {
					            @Override
					            public void run() {
					            	eseguiLivello(endLevel, idPartita, dungeonName, player);
					            }
					        }, 20L);
				      } else if(trigger.equalsIgnoreCase("block_trigger")) {
				    	 File GamedungeonYml = new File("plugins/dungeonsData/games/" + idPartita + ".yml");
				  		 FileConfiguration GamedungeonConfig = YamlConfiguration.loadConfiguration(GamedungeonYml);
				  		 List<String> passtrigger = dungeonConfig.getStringList("level."+Livello+".passtrigger");
				  		 List<String> newpasstrigger = new ArrayList<String>();
				  		for (int i = 0; i < passtrigger.size(); i++) {
				  			newpasstrigger.add(passtrigger.get(i)+"$"+Livello+"$"+endLevel);
				  		}
				  		 GamedungeonConfig.set("triggerblocks", newpasstrigger);
				  		saveCustomYml(GamedungeonConfig,GamedungeonYml);
				      } else if(trigger.equalsIgnoreCase("teleport")) {
				    	  if(dungeonConfig.getLocation("level."+Livello+".teleport")==null) {} else {
				    		  Location loc = dungeonConfig.getLocation("level."+Livello+".teleport");
				    		  Location locNew = new Location(w, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				    				  loc.getYaw(), loc.getPitch());
						      player.teleport(locNew);
				    	  }
					  		eseguiLivello(endLevel, idPartita, dungeonName, player);
					  } else if(trigger.equalsIgnoreCase("mob_spawn")) {
						  List<String> mobsSpawn = dungeonConfig.getStringList("level."+Livello+".mobSpawn");
						  for(int il = 0; il<mobsSpawn.size(); il++) {
							  String[] cacheLevela = mobsSpawn.get(il).split(Pattern.quote("$"));
							  Location loc = getLocationString(cacheLevela[0]);
							  Location newloc = new Location(w, loc.getX(), loc.getY(), loc.getZ());
							  String mobName = cacheLevela[1];
							  customMob miomob = new customMob();
							  Entity entiti = miomob.executeSpawn(newloc, mobName, dungeonConfig);
							  entiti.setCustomName("Generated by test");
						  }
						  eseguiLivello(endLevel, idPartita, dungeonName, player);
					  } else if(trigger.equalsIgnoreCase("give_items")) {
						  List<String> mobsSpawn = dungeonConfig.getStringList("level."+Livello+".mobSpawn");
						  for(int il = 0; il<mobsSpawn.size(); il++) {
							  String[] cacheLevela = mobsSpawn.get(il).split(Pattern.quote("$"));
							  Location loc = getLocationString(cacheLevela[0]);
							  Location newloc = new Location(w, loc.getX(), loc.getY(), loc.getZ());
							  String mobName = cacheLevela[1];
							  customMob miomob = new customMob();
							  Entity entiti = miomob.executeSpawn(newloc, mobName, dungeonConfig);
							  entiti.setCustomName("Generated by test");
						  }
						  eseguiLivello(endLevel, idPartita, dungeonName, player);
					  } 
			}
	}

	public void stopGame(String gameID) {
		//((Player)p).sendMessage("The game has been finished");
    	World unload = Bukkit.getWorld("/DungeonMaps/" + gameID);
    	unloadWorld(unload);
    	File deleteFolder = unload.getWorldFolder();
    	deleteWorld(deleteFolder);
	}
	
	
	public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
		  try {
		  ymlConfig.save(ymlFile);
		  } catch (IOException e) {
		  e.printStackTrace();
		  }
		 }
	private static void unloadWorld(World world) {
        if(!world.equals(null)) {
            for(Player p : world.getPlayers()){
                p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
            Bukkit.getServer().unloadWorld(world, false);
            System.out.println(world.getName() + " unloaded!");
        }
    }
	public boolean deleteWorld(File path) {
	      if(path.exists()) {
	          File files[] = path.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }
	      }
	      return(path.delete());
	}
	static public Location getLocationString(final String s) {
	    if (s == null || s.trim() == "") {
	        return null;
	    }
	    final String[] parts = s.split(":");
	    if (parts.length == 4) {
	        final World w = Bukkit.getServer().getWorld(parts[0]);
	        final int x = Integer.parseInt(parts[1]);
	        final int y = Integer.parseInt(parts[2]);
	        final int z = Integer.parseInt(parts[3]);
	        return new Location(w, x, y, z);
	    }
	    return null;
	    }
}
