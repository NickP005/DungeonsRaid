package ennesima;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;


public class ascoltami implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.giveExp(10);
		p.sendMessage("Ciao, ti ho appena dato dell'esperienza");
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
	Player player = event.getPlayer();
	ItemStack item = player.getEquipment().getItemInMainHand();
	//Block block = event.getBlockPlaced();
	Material materiale = item.getType();
	if(materiale.equals(Material.TRIPWIRE_HOOK)) {
		if(item.getItemMeta().hasLore()) {
			event.setCancelled(true);
			((Player) player).sendMessage(ChatColor.LIGHT_PURPLE + "You can't place this key. Want to use it? Go to the \n" + ChatColor.GOLD + "Dungeons Gate Master");
		}
	} else if(materiale.equals(Material.IRON_BLOCK)) {
		if(item.getItemMeta().hasLore()) {
			Block block = event.getBlockPlaced();
			double Xcoor = block.getLocation().getX();
			double Ycoor = block.getLocation().getY();
			double Zcoor = block.getLocation().getZ();
			((Player) player).sendMessage("You placed the Iron Tool! At " + Xcoor + " " + Ycoor + " " + Zcoor);
			
			File customYml = new File("plugins/dungeonsData/dungeons.yml");
			FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
			//customConfig.set("admin.players." + player.getName() + ".barrierLevel", level);
			String barrierLevel = customConfig.getString("admin.players." + player.getName() + ".barrierLevel");
			String Material = customConfig.getString("admin.players." + player.getName() + ".materialLevel");
			((Player) player).sendMessage(barrierLevel);
			File mondo = player.getWorld().getWorldFolder();
			//((Player) sender).sendMessage(mondo.toString());
			String[] split = mondo.toString().toString().split("/");
			if (split[1].equals("DungeonMaps")) {
				//String Mondo = split[2];
				File dungeonYml = new File("plugins/dungeonsData/" + split[2] + ".yml");
				FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
				((Player) player).sendMessage(split[2]);
				List<String> blocchisvuotare = dungeonConfig.getStringList("level."+barrierLevel+".barriers");
				blocchisvuotare.add(getStringLocation(block.getLocation())+"$"+Material);
				dungeonConfig.set("level."+barrierLevel+".barriers", blocchisvuotare);
				saveCustomYml(dungeonConfig,dungeonYml);
				//List<String> listablocchi = customConfig.getStringList("dungeons."+Mondo+".barriers."+barrierLevel);
				//List<Location> blocklist = (List<Location>) customConfig.getLocation("dungeons."+Mondo+".barriers."+barrierLevel);
				//listablocchi.add(getStringLocation(block.getLocation())+Material);
				//customConfig.set("dungeons."+Mondo+".barriers."+barrierLevel, listablocchi);
				//saveCustomYml(customConfig,customYml);
			} else {
				event.setCancelled(true);
				((Player) player).sendMessage("You aren't in a dungeon map!");
			}
		}
	} else if(materiale.equals(Material.REDSTONE_BLOCK)) {
		if(item.getItemMeta().hasLore()) {
			Block block = event.getBlockPlaced();
			double Xcoor = block.getLocation().getX();
			double Ycoor = block.getLocation().getY();
			double Zcoor = block.getLocation().getZ();
			((Player) player).sendMessage("You placed the Redstone Tool! At " + Xcoor + " " + Ycoor + " " + Zcoor);
			
			File customYml = new File("plugins/dungeonsData/dungeons.yml");
			FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
			//customConfig.set("admin.players." + player.getName() + ".barrierLevel", level);
			String barrierLevel = customConfig.getString("admin.players." + player.getName() + ".barrierLevel");
			String Material = customConfig.getString("admin.players." + player.getName() + ".materialLevel");
			((Player) player).sendMessage(barrierLevel);
			File mondo = player.getWorld().getWorldFolder();
			//((Player) sender).sendMessage(mondo.toString());
			String[] split = mondo.toString().toString().split("/");
			if (split[1].equals("DungeonMaps")) {
				File dungeonYml = new File("plugins/dungeonsData/" + split[2] + ".yml");
				FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
				((Player) player).sendMessage(split[2]);
				List<String> blocchisvuotare = dungeonConfig.getStringList("level."+barrierLevel+".barriers");
				blocchisvuotare.remove(getStringLocation(block.getLocation())+"$"+Material);
				dungeonConfig.set("level."+barrierLevel+".barriers", blocchisvuotare);
				saveCustomYml(dungeonConfig,dungeonYml);
			} else {
				((Player) player).sendMessage("You aren't in a dungeon map!");
				event.setCancelled(true);
			}
		} } else if(materiale.equals(Material.EMERALD_BLOCK)) {
			if(item.getItemMeta().hasLore()) {
				List<String> descrizione = item.getItemMeta().getLore();
				if(descrizione.get(0).equals(ChatColor.BLUE + "Place where is the trigger")) {
					String livello = descrizione.get(1);
					String[] split = player.getWorld().getWorldFolder().toString().toString().split("/");
					if(player.getWorld().getWorldFolder().toString().split("/")[1].equals("DungeonMaps")) {
						Location bL = event.getBlockPlaced().getLocation();
						
						File dungeonYml = new File("plugins/dungeonsData/" + split[2] + ".yml");
						FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
						
						List<String> triggerblocks = dungeonConfig.getStringList("level."+livello+".passtrigger");
						triggerblocks.add(getStringLocation(event.getBlock().getLocation()));
						dungeonConfig.set("level."+livello+".passtrigger", triggerblocks);
						saveCustomYml(dungeonConfig,dungeonYml);
						
						((Player) player).sendMessage(ChatColor.GREEN + "Trigger block for level " + ChatColor.AQUA
								+ livello + ChatColor.GREEN + " placed at "
										+ ChatColor.GRAY + bL.getBlockX() + " " + bL.getBlockY() + " " + bL.getBlockZ());
					}  else {
					((Player) player).sendMessage(ChatColor.RED + "Use that in a Dungeon Map");
					event.setCancelled(true);
					}
				}
			}
		} else if(materiale.equals(Material.EMERALD_ORE)) {
			if(item.getItemMeta().hasLore()) {
				List<String> descrizione = item.getItemMeta().getLore();
				if(descrizione.get(0).equals(ChatColor.BLUE + "Removes trigger")) {
					String livello = descrizione.get(1);
					if(player.getWorld().getWorldFolder().toString().split("/")[1].equals("DungeonMaps")) {
						Location bL = event.getBlockPlaced().getLocation();
						((Player) player).sendMessage(ChatColor.GREEN + "Removed trigger block for level " + ChatColor.AQUA
						+ livello + ChatColor.GREEN + " at "
								+ ChatColor.GRAY + bL.getBlockX() + " " + bL.getBlockY() + " " + bL.getBlockZ());
					}  else {
					((Player) player).sendMessage(ChatColor.RED + "Use that in a Dungeon Map");
					event.setCancelled(true);
					}
				}
			}
		} else if(materiale.equals(Material.CARVED_PUMPKIN)) {
			if(item.getItemMeta().hasLore()) {
				List<String> descrizione = item.getItemMeta().getLore();
				if(descrizione.get(0).equals(ChatColor.BLUE + "Place to summon a mob in level:")) {
					String livello = descrizione.get(1);
					String mobType = descrizione.get(2);
					String[] split = player.getWorld().getWorldFolder().toString().toString().split("/");
					if(player.getWorld().getWorldFolder().toString().split("/")[1].equals("DungeonMaps")) {
						Location bL = event.getBlockPlaced().getLocation();
						File dungeonYml = new File("plugins/dungeonsData/" + split[2] + ".yml");
						FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
						
						List<String> mobList = dungeonConfig.getStringList("mobs.list");
						if(mobList.contains(mobType)) {} else {
							((Player) player).sendMessage(ChatColor.RED + "The mob you specified doesn't exists. Try /level listMobs");
							event.setCancelled(true);
							return;
						}
						List<String> triggerblocks = dungeonConfig.getStringList("level."+livello+".mobSpawn");
						triggerblocks.add(getStringLocation(event.getBlock().getLocation()) + "$" + mobType);
						dungeonConfig.set("level."+livello+".mobSpawn", triggerblocks);
						saveCustomYml(dungeonConfig,dungeonYml);
						
						((Player) player).sendMessage(ChatColor.GREEN + "Placed a " + mobType + " in " + ChatColor.AQUA
								+ livello + ChatColor.GREEN + "  at "
										+ ChatColor.GRAY + bL.getBlockX() + " " + bL.getBlockY() + " " + bL.getBlockZ());
					}  else {
					((Player) player).sendMessage(ChatColor.RED + "Use that in a Dungeon Map");
					event.setCancelled(true);
					}
				}
			}
		}
	
	else {
		event.setCancelled(false);
	}

	}
	@EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        Location toInt = new Location(to.getWorld(), to.getBlockX(), to.getBlockY(), to.getBlockZ());
        if(from!=to) {
        	String[] locString = to.getWorld().getWorldFolder().toString().split("/");
        	if(locString[1].equals("DungeonMaps")) {
        		String nomepartita = locString[2];
        		if(nomepartita.split("-").length==2) {
        			String position = getStringLocation(toInt);
        			File GamedungeonYml = new File("plugins/dungeonsData/games/" + nomepartita + ".yml");
			  		FileConfiguration GamedungeonConfig = YamlConfiguration.loadConfiguration(GamedungeonYml);
			  		List<String> triggerblocks = GamedungeonConfig.getStringList("triggerblocks");
			  		for (int i = 0; i < triggerblocks.size(); i++) {
			  			String cacheblock = triggerblocks.get(i).split(Pattern.quote("$"))[0];
			  			String startLevel = triggerblocks.get(i).split(Pattern.quote("$"))[1];
			  			String endLevel = triggerblocks.get(i).split(Pattern.quote("$"))[2];
			  			Location loccache = getLocationString(cacheblock);
			  			Location newLocCache = new Location(to.getWorld(), loccache.getBlockX(), loccache.getBlockY(), loccache.getBlockZ());
			  			String finalcache = getStringLocation(newLocCache);
			  			if(finalcache.equals(position)) {
			  				((Player) event.getPlayer()).sendMessage(ChatColor.GREEN + "Hai attivato il livello " + startLevel);
			  				//List<String> newTriggers = triggerblocks;
			  				for (int i2 = 0; i2 < triggerblocks.size(); i2++) {
								String startLevel2 = triggerblocks.get(i2).split(Pattern.quote("$"))[1];
					  			if(startLevel2.equals(startLevel)) {
					  				triggerblocks.remove(i2);
					  				i2 = i2-1;
					  			}
			  				}
			  				//triggerblocks=newTriggers;
			  				GamedungeonConfig.set("triggerblocks", triggerblocks);
			  				saveCustomYml(GamedungeonConfig,GamedungeonYml);
			  				((Player) event.getPlayer()).sendMessage("Funzia");
			  				levels livelli = new levels();
			  				livelli.eseguiLivello(endLevel, nomepartita, nomepartita.split("-")[1], event.getPlayer());
			  				((Player) event.getPlayer()).sendMessage(ChatColor.GREEN + "Ora eseguo il livello " + endLevel + " e nome partita " + nomepartita.split("-")[1]);
			  			}
			  		}
			  		
			  		
        		}
        	}
        	
        }
        
//do something
}
	@EventHandler
	  public void onEntityDeath(EntityDeathEvent event){
	      Entity entity = event.getEntity();
	      if(event.getEntity().getKiller() instanceof Entity) {
		      Player killer = event.getEntity().getKiller();
		      //entity.getMetadata("prova").get(0).value();
		      if (entity.hasMetadata(killer.getName())) {
	              ((Player) killer).sendMessage("You killed your mob!!");
	          }
	      }
      }
	@EventHandler
	public void onTarget(EntityTargetEvent event) {
	    if((event.getEntity() instanceof Zombie)) {
	    	
	    	if((event.getTarget() instanceof Zombie)) {
	    		event.setCancelled(false);
	    	}
				

	        if(event.getTarget() instanceof Player) {
	            // Don't target allies
	        	if (event.getEntity().hasMetadata(event.getTarget().getName())) {
	        		event.setCancelled(true);
	            } else {
	            }
	        } 
	    }
	}
	
	@EventHandler
	public void setTarget(EntityTargetEvent event) {
		if((event.getEntity() instanceof Zombie)) {
			Zombie zombie = (Zombie) event.getEntity();
            for(Entity e1 : zombie.getNearbyEntities(5, 5, 5))
            {
            if(e1.getType() == EntityType.SKELETON) {
            event.setTarget((LivingEntity) e1);
            event.setCancelled(false);
            }
            }
		}
	}
	
	public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
		  try {
		  ymlConfig.save(ymlFile);
		  } catch (IOException e) {
		  e.printStackTrace();
		  }
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
	static public String getStringLocation(final Location l) {
	    if (l == null) {
	        return "";
	    }
	    return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
	    }
}
