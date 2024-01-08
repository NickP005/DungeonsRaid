package ennesima;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
import org.bukkit.util.StringUtil;

public class cmdLevel implements CommandExecutor, TabExecutor{
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender,  Command cmd, String arg2,
			 String[] arg3) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("level")) {
			if(arg3[0].equalsIgnoreCase("setNext")) {
				if (p.hasPermission("dungeons.build.setNextLevel")) {
					if(arg3[1]==null || arg3[2]==null || arg3[3]==null) {
						((Player) sender).sendMessage(ChatColor.RED + "Correct syntax: /level setNext <startLevel> <trigger> <nextlevel>");
						return true;
					} 
					String newWorld = new String();
					File mondo = p.getWorld().getWorldFolder();
					String[] split = mondo.toString().split("/");
					if (split[1].equals("DungeonMaps")) {
						String[] mappa = split[2].split("_");
						newWorld = mappa[1];
					} else {
						((Player) p).sendMessage(ChatColor.RED + "You aren't in a dungeon map!");
					}
					String startLevel = arg3[1];
					String levelType = arg3[2];
					String endLevel = arg3[3];
					File dungeonYml = new File("plugins/dungeonsData/_" + newWorld + ".yml");
					FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					List<String> dungeonlist = dungeonConfig.getStringList("levelsList");
					List<String> nextlist = dungeonConfig.getStringList("level."+startLevel+".next");
					if(!dungeonlist.contains(startLevel)) {
						((Player) p).sendMessage(ChatColor.RED + "That level doesn't exists! Try with 'start'");
						return true;
					}
					if(!dungeonlist.contains(endLevel)) {
						dungeonlist.add(endLevel);
						}
					if(!nextlist.contains(levelType+"-"+endLevel)) {
					nextlist.add(levelType+"-"+endLevel);
						}
					//dungeonConfig.set(levelType+"-"+endLevel+".", dungeonlist);
					dungeonConfig.set("levelsList", dungeonlist);
					dungeonConfig.set("level."+startLevel+".aim", "Win!");
					dungeonConfig.set("level."+startLevel+".message", ChatColor.AQUA + "Hey Player! This is the " + startLevel + " level!");
					dungeonConfig.set("level."+startLevel+".next", nextlist);
					saveCustomYml(dungeonConfig,dungeonYml);
					((Player) p).sendMessage(ChatColor.GREEN + "Set the next level for level " + startLevel + " with the trigger " + levelType + " for the dungeon "+newWorld);
				} else {
					((Player) p).sendMessage(ChatColor.DARK_RED + "You don't have the permission to do that!");
				}
			} else if(arg3[0].equalsIgnoreCase("getNext")) {
				if (p.hasPermission("dungeons.build.getNextLevel")) {
					String newWorld = new String();
					File mondo = p.getWorld().getWorldFolder();
					String[] split = mondo.toString().split("/");
					if (split[1].equals("DungeonMaps")) {
						String[] mappa = split[2].split("_");
						newWorld = mappa[1];
					} else {((Player) p).sendMessage(ChatColor.RED + "You aren't in a dungeon map!");}
					String startLevel = arg3[1];
					File dungeonYml = new File("plugins/dungeonsData/_" + newWorld + ".yml");
					FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					List<String> dungeonlist = dungeonConfig.getStringList("levelsList");
					List<String> nextlist = dungeonConfig.getStringList("level."+startLevel+".next");
					if(!dungeonlist.contains(startLevel)) {
						((Player) p).sendMessage(ChatColor.RED + "That level doesn't exists! Try with 'start'");
						return true;}
					((Player) p).sendMessage(ChatColor.GREEN + "Here a list for the next levels of " + ChatColor.YELLOW
							+ "" + startLevel);
					for(int iter=0; iter<nextlist.size(); iter++) {
						String triggercache = nextlist.get(iter).split("-")[0];
						String nextcache = nextlist.get(iter).split("-")[1];
						((Player) p).sendMessage(ChatColor.GRAY + "  " + triggercache + " --> " + nextcache);
					}
				}
			} else if(arg3[0].equalsIgnoreCase("delNext")) {
				if (p.hasPermission("dungeons.build.delNextLevel")) {
					String newWorld = new String();
					File mondo = p.getWorld().getWorldFolder();
					String[] split = mondo.toString().split("/");
					if (split[1].equals("DungeonMaps")) {
						String[] mappa = split[2].split("_");
						newWorld = mappa[1];
					} else {((Player) p).sendMessage(ChatColor.RED + "You aren't in a dungeon map!");}
					String startLevel = arg3[1];
					String levelType = arg3[2];
					String endLevel = arg3[3];
					File dungeonYml = new File("plugins/dungeonsData/_" + newWorld + ".yml");
					FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					List<String> nextlist = dungeonConfig.getStringList("level."+startLevel+".next");
					if(nextlist.contains(levelType+"-"+endLevel)) {
						nextlist.remove(levelType+"-"+endLevel);
							}
					dungeonConfig.set("level."+startLevel+".next", nextlist);
					saveCustomYml(dungeonConfig,dungeonYml);
				}
			} else if(arg3[0].equalsIgnoreCase("utility")) {
				if(arg3[1].equalsIgnoreCase("trigger")) {
					if(arg3[2]==null) {
						((Player) p).sendMessage(ChatColor.RED + "Bad command syntax. Use instead");
						((Player) p).sendMessage(ChatColor.GRAY + "/level utility trigger <level>");
						return true;
					}
					String livelloTrigger = arg3[2];
					ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
					ItemMeta im = item.getItemMeta();
					im.setDisplayName("Trigger for "+livelloTrigger);
					List<String> loreList = new ArrayList<String>();
					loreList.add(ChatColor.BLUE + "Place where is the trigger");//This is the first line of lore
					loreList.add(livelloTrigger);//This is the second line of lore
					im.setLore(loreList);
					item.setItemMeta(im);
					((Player) sender).getInventory().addItem(item);
					ItemStack item2 = new ItemStack(Material.EMERALD_ORE);
					ItemMeta im2 = item2.getItemMeta();
					im2.setDisplayName("Remove trigger for "+livelloTrigger);
					List<String> loreList2 = new ArrayList<String>();
					loreList2.add(ChatColor.BLUE + "Removes trigger");//This is the first line of lore
					loreList2.add(livelloTrigger);//This is the second line of lore
					im2.setLore(loreList2);
					item2.setItemMeta(im2);
					((Player) sender).getInventory().addItem(item2);
				} else if(arg3[1].equalsIgnoreCase("mobSpawn")) {
					if(arg3[2]==null || arg3.length<4) {
						((Player) p).sendMessage(ChatColor.RED + "Bad command syntax. Use instead");
						((Player) p).sendMessage(ChatColor.GRAY + "/level utility mobSpawn <level> <type> [incrementerVariable]");
						return true;
					}
					String level = arg3[2];
					String mobType = arg3[3];
					
					String[] locString = mondo(p);
					String dungeonName = locString[2];
					File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
					FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					List<String> mobList = dungeonConfig.getStringList("mobs.list");
					if(mobList.contains(mobType)) {} else {
						((Player) p).sendMessage(ChatColor.RED + "The mob you specified doesn't exists. Try /level listMobs");
						return true;
					}
					ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
					ItemMeta im = item.getItemMeta();
					im.setDisplayName("Place a "+mobType);
					List<String> loreList = new ArrayList<String>();
					loreList.add(ChatColor.BLUE + "Place to summon a mob in level:");//This is the first line of lore
					loreList.add(level);//This is the second line of lore
					loreList.add(mobType);
					im.setLore(loreList);
					item.setItemMeta(im);
					item.addUnsafeEnchantment(Enchantment.LURE, 1);
					((Player) sender).getInventory().addItem(item);
					ItemStack item2 = new ItemStack(Material.PUMPKIN);
					ItemMeta im2 = item2.getItemMeta();
					im2.setDisplayName("Removes a "+mobType);
					List<String> loreList2 = new ArrayList<String>();
					loreList2.add(ChatColor.BLUE + "Place to undo summon");//This is the first line of lore
					loreList2.add(level);//This is the second line of lore
					loreList2.add(mobType);
					im2.setLore(loreList2);
					item2.setItemMeta(im2);
					item2.addUnsafeEnchantment(Enchantment.LURE, 1);
					((Player) sender).getInventory().addItem(item2);
					//String tipoMob = arg3[2];
					
				} else if(arg3[1].equalsIgnoreCase("setTeleport")) {
					if(arg3[2]==" " || arg3.length!=3) {
						((Player) p).sendMessage(ChatColor.RED + "Bad command syntax. Use instead");
						((Player) p).sendMessage(ChatColor.GRAY + "/level utility setTeleport <level>");
						return true;
					}
					String[] locString = mondo(p);
					String dungeonName = locString[2];
					File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
					FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					String livelloTeleport = arg3[2];
					List<String> levelLists = dungeonConfig.getStringList("levelsList");
					if(!levelLists.contains(livelloTeleport)) {
						((Player) p).sendMessage(ChatColor.RED + "That level doesn't exists!");
						return true;}
					Location locSender = ((Player) sender).getLocation();
					dungeonConfig.set("level."+livelloTeleport+".teleport",locSender);
					saveCustomYml(dungeonConfig,dungeonYml);
					((Player) p).sendMessage(ChatColor.GREEN + "Correctly set the spawn event of "+livelloTeleport+" to "
					+ ChatColor.AQUA + locSender.getBlockX() + " " + locSender.getBlockY() + " " + locSender.getBlockZ());
				}
				
			} else if(arg3[0].equalsIgnoreCase("help")) {
				//customMob miomob = new customMob();
				//miomob.spawnZombie((Player) sender);
				((Player) p).sendMessage(ChatColor.GOLD+"Here's the levels commands for Dungeons");
				((Player) p).sendMessage(ChatColor.GRAY+"/level setNext <startLevel> <trigger> <endLevel>");
				((Player) p).sendMessage(ChatColor.GRAY+"/level delNext <startLevel> <trigger> <endLevel>");
				((Player) p).sendMessage(ChatColor.GRAY+"/level getNext <startLevel>");
			} else if(arg3[0].equalsIgnoreCase("createMob")) {
				cmdCreateMob(arg3, p);
			} else if(arg3[0].equalsIgnoreCase("listMobs")) {
				cmdListMob(arg3, p);
			} else if(arg3[0].equalsIgnoreCase("delMob")) {
				cmdDelMob(arg3, p);
			} else if(arg3[0].equalsIgnoreCase("mob")) {
				cmdMob(arg3, p);
			} else if(arg3[0].equalsIgnoreCase("view")) {
				if(arg3[1]!=null) {
					if(arg3[1].equalsIgnoreCase("triggers")) {
						if(arg3[2]!=null) {
							String Livello = arg3[2];
							String[] locString = ((Player) sender).getWorld().getWorldFolder().toString().split("/");
							String dungeonName = locString[2];
							File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
							FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					  		 List<String> passtrigger = dungeonConfig.getStringList("level."+Livello+".passtrigger");
					  		if(passtrigger.size()==0) {
								p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!] " + 
					  		ChatColor.RESET +ChatColor.GREEN + "There aren't triggers here");
								return true;
							}
					  		for (int i = 0; i < passtrigger.size(); i++) {
					  			Location loccache = getLocationString(passtrigger.get(i));
					  			((Player) sender).sendBlockChange(loccache, Material.DIAMOND_BLOCK,  (byte) 0);
					  		}
						}
					} else if(arg3[1].equalsIgnoreCase("barriers")) {
						if(arg3[2]!=null) {
							String Livello = arg3[2];
							String[] locString = ((Player) sender).getWorld().getWorldFolder().toString().split("/");
							String dungeonName = locString[2];
							File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
							FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
							List<String> blocchisvuotare = dungeonConfig.getStringList("level."+Livello+".barriers");
					  		 //List<String> passtrigger = dungeonConfig.getStringList("level."+Livello+".passtrigger");
							if(blocchisvuotare.size()==0) {
								p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!] " + 
								  		ChatColor.RESET + ChatColor.GREEN + "There aren't barriers here");
								return true;
							}
							if(arg3.length==3) {
								for (int i = 0; i < blocchisvuotare.size(); i++) {
									String blocco = blocchisvuotare.get(i).split(Pattern.quote("$"))[0];
						  			Location loccache = getLocationString(blocco);
						  			((Player) sender).sendBlockChange(loccache, Material.DIAMOND_ORE,  (byte) 0);
						  		}
							} else if(arg3.length==4) {
								for (int i = 0; i < blocchisvuotare.size(); i++) {
									String[] info = blocchisvuotare.get(i).split(Pattern.quote("$"));
									if(info[1].equalsIgnoreCase(arg3[3])) {
										String blocco = blocchisvuotare.get(i).split(Pattern.quote("$"))[0];
										Location loccache = getLocationString(blocco);
							  			((Player) sender).sendBlockChange(loccache, Material.DIAMOND_ORE,  (byte) 0);
									}
						  		}
							}
						}
					} else if(arg3[1].equalsIgnoreCase("mobs")) {
						if(arg3[2]!=null) {
							String Livello = arg3[2];
							String[] locString = ((Player) sender).getWorld().getWorldFolder().toString().split("/");
							String dungeonName = locString[2];
							File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
							FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
							List<String> blocchisvuotare = dungeonConfig.getStringList("level."+Livello+".mobSpawn");
					  		 //List<String> passtrigger = dungeonConfig.getStringList("level."+Livello+".passtrigger");
							if(blocchisvuotare.size()==0) {
								p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!] " + 
								  		ChatColor.RESET + ChatColor.GREEN + "There aren't mobs spawning here");
								return true;
							}
							if(arg3.length==3) {
								for (int i = 0; i < blocchisvuotare.size(); i++) {
									String blocco = blocchisvuotare.get(i).split(Pattern.quote("$"))[0];
						  			Location loccache = getLocationString(blocco);
						  			((Player) sender).sendBlockChange(loccache, Material.JACK_O_LANTERN,  (byte) 0);
						  		}
							} else if(arg3.length==4) {
								for (int i = 0; i < blocchisvuotare.size(); i++) {
									String[] info = blocchisvuotare.get(i).split(Pattern.quote("$"));
									if(info[1].equalsIgnoreCase(arg3[3])) {
										String blocco = blocchisvuotare.get(i).split(Pattern.quote("$"))[0];
										Location loccache = getLocationString(blocco);
							  			((Player) sender).sendBlockChange(loccache, Material.JACK_O_LANTERN,  (byte) 0);
									}
						  		}
							}
						}
					}
				} else {
					((Player) sender).sendMessage(ChatColor.DARK_RED + "Bad syntax");
				}
			} else if(arg3.length==0) {
				((Player) sender).sendMessage(ChatColor.GOLD + "Unknown command. Type " + ChatColor.AQUA + "/level help");
			} else {
				((Player) sender).sendMessage(ChatColor.GOLD + "Unknown command. Type " + ChatColor.AQUA + "/level help");
			}
		}
		return true;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //create new array
		final List<String> completions = new ArrayList<>();
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        //StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
        //sort the list

		//int a = args.length;
		//completions.add(String.valueOf(a));
		
		if(command.getName().equalsIgnoreCase("level")) {
			if(args.length==1) {
				completions.add(args[0]);
				List<String> comandiTAB = new ArrayList<>();
				comandiTAB.add("setNext");
				comandiTAB.add("getNext");
				comandiTAB.add("delNext");
				comandiTAB.add("help");
				comandiTAB.add("utility");
				comandiTAB.add("view");
				comandiTAB.add("createMob");
				comandiTAB.add("listMobs");
				comandiTAB.add("delMob");
				comandiTAB.add("mob");
				StringUtil.copyPartialMatches(args[0], comandiTAB, completions);
			} else if(args.length==2) {
				if(args[0].equalsIgnoreCase("utility")) {
					List<String> comandiTAB = new ArrayList<>();
					comandiTAB.add("trigger");
					comandiTAB.add("setTeleport");
					comandiTAB.add("mobSpawn");
				    StringUtil.copyPartialMatches(args[1], comandiTAB, completions);
				} else if(args[0].equalsIgnoreCase("view")) {
					List<String> comandiTAB = new ArrayList<>();
					comandiTAB.add("triggers");
					comandiTAB.add("barriers");
					comandiTAB.add("mobs");
				    StringUtil.copyPartialMatches(args[1], comandiTAB, completions);
				} else if(args[0].equalsIgnoreCase("getNext")) {
					String[] locString = ((Player) sender).getWorld().getWorldFolder().toString().split("/");
					String dungeonName = locString[2];
					File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
					FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					List<String> levelLists = dungeonConfig.getStringList("levelsList");
					StringUtil.copyPartialMatches(args[1], levelLists, completions);
				} else if(args[0].equalsIgnoreCase("delMob") || args[0].equalsIgnoreCase("mob")) {
					String[] locString = ((Player) sender).getWorld().getWorldFolder().toString().split("/");
					String dungeonName = locString[2];
					File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
					FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
					List<String> levelLists = dungeonConfig.getStringList("mobs.list");
					StringUtil.copyPartialMatches(args[1], levelLists, completions);
				}
				
			} else if(args.length==3) {
				if(args[0].equalsIgnoreCase("utility")) {
					if(args[1].equalsIgnoreCase("trigger")) {
						
					}
				} else if(args[0].equalsIgnoreCase("setNext")) {
					List<String> comandiTAB = new ArrayList<>();
					comandiTAB.add("1s_delay");
					comandiTAB.add("5s_delay");
					comandiTAB.add("block_trigger");
					comandiTAB.add("teleport");
					comandiTAB.add("mob_spawn");
					StringUtil.copyPartialMatches(args[2], comandiTAB, completions);
				}  else if(args[0].equalsIgnoreCase("view")) {
						if(args[1].equalsIgnoreCase("triggers") || args[1].equalsIgnoreCase("barriers")
								|| args[1].equalsIgnoreCase("mobs")) {
							String[] locString = ((Player) sender).getWorld().getWorldFolder().toString().split("/");
							String dungeonName = locString[2];
							File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
							FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
							List<String> levelLists = dungeonConfig.getStringList("levelsList");
							StringUtil.copyPartialMatches(args[2], levelLists, completions);
					}
					
				} else if(args[0].equalsIgnoreCase("mob")) {
					List<String> comandiTAB = new ArrayList<>();
					comandiTAB.add("setMainHand");
					comandiTAB.add("setCurrentArmor");
					comandiTAB.add("setCurrentChestplate");
					StringUtil.copyPartialMatches(args[2], comandiTAB, completions);
				}
			} else if(args.length==4) {
				if(args[0].equalsIgnoreCase("setNext")) {
					
				} else if(args[0].equalsIgnoreCase("view")) {
					if(args[1].equalsIgnoreCase("barriers")) {
						List<String> materials = new ArrayList<String>();
						for(Material m : Material.values()){
						materials.add(m.toString());
						}
						StringUtil.copyPartialMatches(args[3], materials, completions);
					} else if(args[1].equalsIgnoreCase("mobs")) {
						String[] locString = ((Player) sender).getWorld().getWorldFolder().toString().split("/");
						String dungeonName = locString[2];
						File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
						FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
						List<String> mobsList = dungeonConfig.getStringList("mobs.list");
						StringUtil.copyPartialMatches(args[3], mobsList, completions);
					}
				} 
				
			}
		}
        Collections.sort(completions);
        return completions;
    }
	public boolean cmdCreateMob(String [] arg3, Player p) {
		if(arg3.length>1) {
			String[] locString = mondo(p);
			if(!locString[1].equals("DungeonMaps")) {((Player) p).sendMessage(ChatColor.RED + "You aren't in a dungeon map!");
			return true;}
			String dungeonName = locString[2];
			String nomeMob = arg3[1];
			String tipoMob = "zombie";
			File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
			FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
			if(arg3.length>2) {
				for(int iter=0; iter<arg3.length-2; iter++) {
					String elemento = arg3[2+iter];
					if(elemento.equalsIgnoreCase("-type")) {
						iter++;
						tipoMob = arg3[2+iter];
					}
				}
			}
			if(!customMob.possibleMobs.contains(tipoMob)) {
				((Player) p).sendMessage(ChatColor.DARK_RED + "The specified tipe "+tipoMob+" doesn't exists");
				return true;
			}
			List<String> mobsList = dungeonConfig.getStringList("mobs.list");
			if(mobsList.contains(nomeMob)) {
				((Player) p).sendMessage(ChatColor.DARK_RED + "The mob " + nomeMob + " already exists");
				return true;
			}
			mobsList.add(nomeMob);dungeonConfig.set("mobs.list",mobsList);
			dungeonConfig.set("mobs."+nomeMob+".type",tipoMob);
			dungeonConfig.set("mobs."+nomeMob+".health",20);
			saveCustomYml(dungeonConfig,dungeonYml);
			((Player) p).sendMessage(ChatColor.GOLD+"Tipo mob: " + tipoMob + ", nomeMob: " + nomeMob);
			
		} else {
			((Player) p).sendMessage(ChatColor.RED + "Bad syntax. /level createMob <name> [-type {zombie}]");
		}
		return true;
	}
	public boolean cmdDelMob(String [] arg3, Player p) {
		if(arg3.length>1) {
			String[] locString = mondo(p);
			if(!locString[1].equals("DungeonMaps")) {((Player) p).sendMessage(ChatColor.RED + "You aren't in a dungeon map!");
			return true;}
			String dungeonName = locString[2];
			String nomeMob = arg3[1];
			File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
			FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);

			List<String> mobsList = dungeonConfig.getStringList("mobs.list");
			if(!mobsList.contains(nomeMob)) {
				((Player) p).sendMessage(ChatColor.DARK_RED + "The mob " + nomeMob + " doesn't exists");
				return true;
			}
			mobsList.remove(nomeMob);dungeonConfig.set("mobs.list",mobsList);
			saveCustomYml(dungeonConfig,dungeonYml);
			p.sendMessage(ChatColor.GREEN + "Correcly removed " + ChatColor.AQUA + " from the config");
		} else {
			p.sendMessage(ChatColor.RED + "Bad syntax. /level createMob <name> [-type {zombie}]");}
		return true;
	}
	public boolean cmdMob(String [] arg3, Player p) {
		if(arg3.length>2) {
			String[] locString = mondo(p);
			if(!locString[1].equals("DungeonMaps")) {((Player) p).sendMessage(ChatColor.RED + "You aren't in a dungeon map!");
			return true;}
			String dungeonName = locString[2];
			String nomeMob = arg3[1];
			File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
			FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
			List<String> mobsList = dungeonConfig.getStringList("mobs.list");
			if(!mobsList.contains(nomeMob)) {
				p.sendMessage(ChatColor.DARK_RED + "The mob " + nomeMob + " doesn't exists");
				return true;
			}
			if(arg3[2].equalsIgnoreCase("setMainHand")) {
				ItemStack item = p.getEquipment().getItemInMainHand();
				dungeonConfig.set("mobs."+nomeMob+".mainHand",item);
				p.sendMessage(ChatColor.GREEN + "Correcly set the main hand of " + ChatColor.AQUA + nomeMob +
						ChatColor.GREEN + " to your current item in hand");
			} else if(arg3[2].equalsIgnoreCase("setCurrentArmor")) {
				ItemStack boots = p.getEquipment().getBoots();
				ItemStack leggins = p.getEquipment().getLeggings();
				ItemStack chestplate = p.getEquipment().getChestplate();
				ItemStack helmet = p.getEquipment().getHelmet();
				dungeonConfig.set("mobs."+nomeMob+".boots",boots);
				dungeonConfig.set("mobs."+nomeMob+".leggins",leggins);
				dungeonConfig.set("mobs."+nomeMob+".chestplate",chestplate);
				dungeonConfig.set("mobs."+nomeMob+".helmet",helmet);
				p.sendMessage(ChatColor.GREEN + "Correcly set the armor of " + ChatColor.AQUA + nomeMob +
						ChatColor.GREEN + " to your current armor");
			} else if(arg3[2].equalsIgnoreCase("setCurrentChestplate")) {
				ItemStack chestplate = p.getEquipment().getChestplate();
				dungeonConfig.set("mobs."+nomeMob+".chestplate",chestplate);
				p.sendMessage(ChatColor.GREEN + "Correcly set the chestplate of mob " + ChatColor.AQUA + nomeMob +
						ChatColor.GREEN + " to your current chestplate");
			} else if(arg3[2].equalsIgnoreCase("setHealth")) {
				if(arg3.length==4) {
					if(isInteger(arg3[3])) {
						int health = Integer.parseInt(arg3[3]);
						dungeonConfig.set("mobs."+nomeMob+".health",health);
						p.sendMessage(ChatColor.GREEN + "Correcly set the chestplate of mob " + ChatColor.AQUA + nomeMob +
								ChatColor.GREEN + " to your current chestplate");
					}
				} else {
					p.sendMessage(ChatColor.RED + "Please define the health");
				}
			}
			saveCustomYml(dungeonConfig,dungeonYml);
			
		} else {
			p.sendMessage(ChatColor.RED + "Bad syntax. /level mob");}
		return true;
	}
	public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
		  try {
		  ymlConfig.save(ymlFile);
		  } catch (IOException e) {
		  e.printStackTrace();
		  }
		 }
	public void cmdListMob(String [] arg3, Player p) {
		if(arg3.length>0) {
			String[] locString = mondo(p);
			if(!locString[1].equals("DungeonMaps")) {((Player) p).sendMessage(ChatColor.RED + "You aren't in a dungeon map!");
			return;}
			String dungeonName = locString[2];
			File dungeonYml = new File("plugins/dungeonsData/" + dungeonName + ".yml");
			String dungeonNameAvoid = dungeonName.split("_")[1];
			FileConfiguration dungeonConfig = YamlConfiguration.loadConfiguration(dungeonYml);
			List<String> mobsList = dungeonConfig.getStringList("mobs.list");
			if(mobsList.size()==0) {
				((Player) p).sendMessage(ChatColor.GREEN + "There aren't mobs in " + ChatColor.AQUA + dungeonNameAvoid +
						ChatColor.GREEN + ", create one using:");
				p.sendMessage(ChatColor.GRAY + "/level createMob <name> [-type {zombie}]");
				return;
			}
			((Player) p).sendMessage(ChatColor.GREEN + "Here the list of all the registered mobs in " +
			ChatColor.AQUA + dungeonNameAvoid);
			for(int i=0; i<mobsList.size(); i++) {
				String mobType = dungeonConfig.getString("mobs."+mobsList.get(i)+".type");
				((Player) p).sendMessage(ChatColor.GRAY + " " + mobsList.get(i) + " (" +
				ChatColor.DARK_GRAY + mobType + ChatColor.GRAY + ")");
			}
		return;
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
	static public String[] mondo(Player p) {
		File mondo = p.getWorld().getWorldFolder();
		String[] split = mondo.toString().split("/");
		return split;
	}
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
}

