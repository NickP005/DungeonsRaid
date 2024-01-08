package ennesima;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.StringUtil;


public class comandi implements CommandExecutor, TabExecutor{
	@SuppressWarnings("unused")
	private World world;
	//@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender,  Command cmd, String arg2,
			 String[] arg3) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("dk")) {
			if (p.hasPermission("dungeons.openkey")) {
				ItemStack item = p.getInventory().getItemInMainHand();
				if(item.getType().name().equalsIgnoreCase("TRIPWIRE_HOOK")) {} else {
					((Player)p).sendMessage(ChatColor.RED+"You have to have the Key in your main hand!");
					return true;
				}
				//String itemname = item.getType().name();
				//((Player)p).sendMessage(itemname);
				ItemMeta meta = item.getItemMeta();
				
				 if (meta.hasLore() == true) {
						int howmany = item.getAmount() - 1;
						ItemStack newitem = item;
						newitem.setAmount(howmany);
						//String name = meta.getDisplayName();
					 List<String> lore = meta.getLore();
					 String primariga = lore.get(0);
					 ////
					    Random r = new Random();
					    String alfabeto = "QWERTYUIOPASDFGHJKLZXCVBNM";
					    String random = "";
					    for (int i = 0; i < 3; i++) {
					        random = random+alfabeto.charAt(r.nextInt(alfabeto.length()));
					    } // stampa tre caratteri per definire la nuova famiglia
					 String newWorld = random + p.getName() + "-" + primariga;
				     worldUtility mondi = new worldUtility();
					 World source = Bukkit.getWorld("/DungeonMaps/" + primariga);
					 File sourceFolder = source.getWorldFolder();
					 File targetFolder = new File(Bukkit.getWorldContainer(), "/DungeonMaps/" + newWorld);
					 mondi.copyWorld(sourceFolder, targetFolder);
					 ////
					 Bukkit.createWorld(WorldCreator.name("/DungeonMaps/" + newWorld));
					 World w = Bukkit.getWorld("/DungeonMaps/" + newWorld);
					 
					 File customYml = new File("plugins/dungeonsData/dungeons.yml");
					 FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
					 Location dungeloc = customConfig.getLocation(primariga + ".spawn");
					 
					 //List<String> blocchisvuotare = customConfig.getStringList("dungeons."+primariga+".barriers.1");
					 //int numeroiter = blocchisvuotare.size();
					 //for (int i = 0; i < numeroiter; i++) {
					//	  String cacheblock = blocchisvuotare.get(i);
					//	  Location locacache = getLocationString(w, cacheblock);
					//	  locacache.getBlock().setType(Material.AIR);
					//	}
					 //levels levelMain = new levels();
					 levels object = new levels();
					 object.eseguiLivello("start", newWorld, primariga, p);
					 
					 p.teleport(new Location(w, dungeloc.getX(), dungeloc.getY(), dungeloc.getZ(), dungeloc.getYaw(), dungeloc.getPitch()));
					 p.getInventory().setItemInMainHand(newitem);
					 //Con questo dopo tot ticks distruggera il mondo
					 //int minuti = 1;
					 BukkitScheduler scheduler = Bukkit.getScheduler();
				        scheduler.scheduleSyncDelayedTask(JavaPlugin.getProvidingPlugin(Main.class) , new Runnable() {
				            @Override
				            public void run() {
				            	World unload = Bukkit.getWorld("/DungeonMaps/" + newWorld);
				            	File deleteFolder = unload.getWorldFolder();
				            	if(Files.exists(deleteFolder.toPath())) {
				            		((Player)p).sendMessage("The game has been finished");
					            	levels leveli = new levels();
					            	leveli.stopGame(newWorld);
				            	}
				            	//World unload = Bukkit.getWorld("/DungeonMaps/" + newWorld);
				            	//unloadWorld(unload);
				            	//File deleteFolder = unload.getWorldFolder();
				            	//deleteWorld(deleteFolder);
				            }
				        }, 1400L); //tempo massimo 12 minuti
				 }
				//String provi = meta.getLore().get(0);
				
				} else {((Player) sender).sendMessage(ChatColor.RED + "You don't have the permissions to do that!");}
			//Bukkit.getScheduler ().runTaskLater((Plugin) this, () -> p.setExp(12), 40); //20 ticks equal 1 second
			return true;
		} else if(cmd.getName().equalsIgnoreCase("dungeons")) {
			if(arg3[0].equalsIgnoreCase("key")) {
				if (p.hasPermission("dungeons.admin.give")) {
					if(arg3.length <= 1) {
						((Player) sender).sendMessage("uso corretto /dungeons key <key> <player>");
						return true;
					} 
					if(Bukkit.getServer().getPlayer(arg3[2]) == null) {
						((Player) sender).sendMessage("Player non esistente");
					}
					String nomekey = arg3[1];
					File customYml = new File("plugins/dungeonsData/dungeons.yml");
					FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
					List<String> dungeonlist = customConfig.getStringList("dungeons.list");
					if(dungeonlist.contains(nomekey)) {} else {
						((Player) sender).sendMessage(ChatColor.DARK_RED + "This dungeon doesn't exist!");
						return true;
					}
					Player chiavea = Bukkit.getPlayer(arg3[2]);
					((Player) sender).sendMessage(nomekey);
					((Player) sender).sendMessage(chiavea.getName());
					
					ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK);
					ItemMeta im = item.getItemMeta();
					item.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
					im.setDisplayName(ChatColor.RED + nomekey);
					List<String> loreList = new ArrayList<String>();
					loreList.add("_" + nomekey);//This is the first line of lore
					//loreList.add(ChatColor.BLUE + "Descrizione 2");//This is the second line of lore
					im.setLore(loreList);
					item.setItemMeta(im);
					
					((Player) chiavea).getInventory().addItem(item);
				} else {((Player) sender).sendMessage(ChatColor.RED + "You don't have the permissions to do that!");}
			} else if(arg3[0].equalsIgnoreCase("setspawn")) {
				if (p.hasPermission("dungeons.admin.setspawn")) {
					File mondo = p.getWorld().getWorldFolder();
					//((Player) sender).sendMessage(mondo.toString());
					String[] split = mondo.toString().toString().split("/");
					if(split[1].equals("DungeonMaps")) {
						Location playerloc = p.getLocation();
						File customYml = new File("plugins/dungeonsData/dungeons.yml");
						FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
						customConfig.set(split[2] + ".spawn", playerloc);
						saveCustomYml(customConfig,customYml);
					} else {
						((Player) sender).sendMessage("To set the dungeon spawn you have to be in a Dungeon World!");
					}
				} else {((Player) sender).sendMessage(ChatColor.RED + "You don't have the permissions to do that!");}
			} else if(arg3[0].equalsIgnoreCase("new")) {
				if (p.hasPermission("dungeons.admin.create")) {
					String newWorld = arg3[1];
					worldUtility mondi = new worldUtility();
				    World source = Bukkit.getWorld("_VoidForDungeons");
				    File sourceFolder = source.getWorldFolder();
				    File targetFolder = new File(Bukkit.getWorldContainer(), "/DungeonMaps/_" + newWorld);
				    mondi.copyWorld(sourceFolder, targetFolder);
				    
				    File customYml = new File("plugins/dungeonsData/dungeons.yml");
					  FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
					  List<String> dungeonlist = customConfig.getStringList("dungeons.list");
					  dungeonlist.add(newWorld);
					  customConfig.set("dungeons.list", dungeonlist);
					  saveCustomYml(customConfig,customYml);
				    
					  File dungeonYml = new File("plugins/dungeonsData/_" + newWorld + ".yml");
					  FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					  dungeonConfig.set("displayName", newWorld);
					  dungeonConfig.set("descriptionAlert", null);
					  List<String> levels = new ArrayList<>();
					  levels.add("start");
					  levels.add("stop");
					  dungeonConfig.set("levelsList", levels);
					  saveCustomYml(dungeonConfig,dungeonYml);
					  saveCustomYml(customConfig,customYml);
					  
				    ((Player) sender).sendMessage("Teleporting..");
				    Bukkit.createWorld(WorldCreator.name("/DungeonMaps/_" + newWorld));
					World w = Bukkit.getWorld("/DungeonMaps/_" + newWorld);
					p.teleport(new Location(w, 0, 75, 0, 0, 0));
				} else {
					((Player) sender).sendMessage(ChatColor.RED + "You don't have the permissions to do that!");
				}
			} else if(arg3[0].equalsIgnoreCase("go")) {
				if (p.hasPermission("dungeons.admin.teleport")) {
					String primariga = arg3[1];
					File customYml = new File("plugins/dungeonsData/dungeons.yml");
					FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
					List<String> dungeonlist = customConfig.getStringList("dungeons.list");
					if(dungeonlist.contains(primariga)) {
						((Player) sender).sendMessage(ChatColor.GREEN + "Teleporting...");
					} else {
						((Player) sender).sendMessage(ChatColor.DARK_RED + "This dungeon doesn't exist!");
						return true;
					}
					Bukkit.createWorld(WorldCreator.name("/DungeonMaps/_" + primariga));
					 World w = Bukkit.getWorld("/DungeonMaps/_" + primariga);
					 Location dungeloc = customConfig.getLocation("_" + primariga + ".spawn");
					 if(dungeloc==null) {
						 ((Player) sender).sendMessage(ChatColor.YELLOW + "The Dungeon Spawn was not yet defined. Please define doing:");
						 ((Player) sender).sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "/dungeons setspawn " + primariga);
						 p.teleport(new Location(w, 0, 75, 0, 0, 0));
					 } else {
					 p.teleport(new Location(w, dungeloc.getX(), dungeloc.getY(), dungeloc.getZ(), dungeloc.getYaw(), dungeloc.getPitch()));
					 }
				} else {((Player) sender).sendMessage(ChatColor.RED + "You don't have the permissions to do that!");}
			} else if(arg3[0].equalsIgnoreCase("barrier")) {
				if (p.hasPermission("dungeons.edit.getBarrier")) {
					String level = arg3[1];
					String Maaterial = arg3[2];
					ItemStack item = new ItemStack(Material.IRON_BLOCK);
					ItemMeta im = item.getItemMeta();
					item.addUnsafeEnchantment(Enchantment.LOYALTY, 1);
					im.setDisplayName("Dungeons_Helper");
					List<String> loreList = new ArrayList<String>();
					loreList.add("barrier_helper");//This is the first line of lore
					loreList.add(ChatColor.BLUE + "Place it where you need a barrier ");//This is the second line of lore
					im.setLore(loreList);
					item.setItemMeta(im);
					((Player) sender).getInventory().addItem(item);
					
					ItemStack item2 = new ItemStack(Material.REDSTONE_BLOCK);
					ItemMeta im2 = item2.getItemMeta();
					item2.addUnsafeEnchantment(Enchantment.LOYALTY, 1);
					im2.setDisplayName("Dungeons Helper");
					List<String> loreList2 = new ArrayList<String>();
					loreList2.add("barrier_helper");//This is the first line of lore
					loreList2.add(ChatColor.BLUE + "Place it where you remove a barrier ");//This is the second line of lore
					im2.setLore(loreList2);
					item2.setItemMeta(im2);
					((Player) sender).getInventory().addItem(item2);
					
					File customYml = new File("plugins/dungeonsData/dungeons.yml");
					FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
					customConfig.set("admin.players." + sender.getName() + ".barrierLevel", level);
					customConfig.set("admin.players." + sender.getName() + ".materialLevel", Maaterial);
					saveCustomYml(customConfig,customYml);
					//((Player) chiavea).getInventory().addItem(item);
				}
			} else if(arg3[0].equalsIgnoreCase("setmessage")) {
				String dungeonName = arg3[1];
				String messaggio = "";
				int numeroLivello = Integer.valueOf(arg3[2]);
				for (int i = 3; i < arg3.length; i++) {messaggio = messaggio + " " + arg3[i];}
				File customYml = new File("plugins/dungeonsData/dungeons.yml");
				FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
				customConfig.set("_" + dungeonName + ".levels."+numeroLivello+".message", messaggio);
				saveCustomYml(customConfig,customYml);
			} else if(arg3[0].equalsIgnoreCase("settype")) {
				String dungeonName = arg3[1];
				String messaggio = "";
				int numeroLivello = Integer.valueOf(arg3[2]);
				for (int i = 3; i < arg3.length; i++) {messaggio = messaggio + " " + arg3[i];}
				File customYml = new File("plugins/dungeonsData/dungeons.yml");
				FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
				customConfig.set("_" + dungeonName + ".levels."+numeroLivello+".type", messaggio);
				saveCustomYml(customConfig,customYml);
			}
			else {
				((Player) sender).sendMessage(ChatColor.GOLD + "Here the possible command list");
				((Player) sender).sendMessage(ChatColor.WHITE + "/dungeons go [DungeonName]");
				((Player) sender).sendMessage(ChatColor.WHITE + "/dungeons key [DungeonName] [Player]");
				((Player) sender).sendMessage(ChatColor.WHITE + "/dungeons new [DungeonName]");
				((Player) sender).sendMessage(ChatColor.WHITE + "/dungeons setspawn [DungeonName]");
			}
			
		} 
		return true;
	}

	public List<String> onTabComplete(CommandSender sender,  Command cmd, String arg2,
			 String[] arg3) {
		//Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("dungeons")) {
			if(arg3.length==1) {
				ArrayList<String> list = new ArrayList<String>();
				list.add("new"); 
				list.add("setspawn");
				list.add("setmessage");
				list.add("setType");
				list.add("barrier");
				list.add("go");
				list.add("key");
				return list;
			} else if(arg3.length==2){
				if(arg3[0].equalsIgnoreCase("setspawn") || arg3[0].equalsIgnoreCase("key") || arg3[0].equalsIgnoreCase("go")
						|| arg3[0].equalsIgnoreCase("setmessage") || arg3[0].equalsIgnoreCase("setType")
						) {
					File customYml = new File("plugins/dungeonsData/dungeons.yml");
					FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
					List<String> dungeonlist = customConfig.getStringList("dungeons.list");
					return dungeonlist;
					
				} 
				
			} else if(arg3.length==3) {
				if(arg3[0].equalsIgnoreCase("barrier")) {
					final List<String> completions = new ArrayList<>();
					List<String> materials = new ArrayList<String>();
					for(Material m : Material.values()){
					materials.add(m.toString());
					}
					StringUtil.copyPartialMatches(arg3[2], materials, completions);
					Collections.sort(completions);
			        return completions;
				}
				
			}
		}

		return null;
	}
	
	public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
		  try {
		  ymlConfig.save(ymlFile);
		  } catch (IOException e) {
		  e.printStackTrace();
		  }
		 }
	//private static void unloadWorld(World world) {
    //    if(!world.equals(null)) {
    //        for(Player p : world.getPlayers()){
    //            p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    //        }
    //        Bukkit.getServer().unloadWorld(world, false);
    //        System.out.println(world.getName() + " unloaded!");
    //    }
    //}
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
	//Modificato
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

}
