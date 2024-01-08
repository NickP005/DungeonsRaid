package ennesima;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

public class customMob {
	@SuppressWarnings("serial")
	public static ArrayList<String> possibleMobs = new ArrayList<String>() {{
	    add("zombie");
	    add("skeleton");
	}};
    public void spawnZombie(Player p) {
    	
        Zombie z = (Zombie) p.getWorld().spawnEntity(p.getLocation(),
                EntityType.ZOMBIE);
        z.setMetadata(p.getName(), new FixedMetadataValue(Main.instance, p.getName()));
        z.setMetadata(p.getName(), new FixedMetadataValue(Main.instance, "custom"));
        z.setCustomName(ChatColor.RED + "Protector of " + p.getName() + "\n" + " second row");
        z.setCustomNameVisible(true);
	//public customMob( World world) {
		//super(entitytypes, world);
	//}
    }
    @SuppressWarnings("deprecation")
	public Entity executeSpawn(Location loc, String mobName, FileConfiguration dungeonConfig) {
    	String tipo = dungeonConfig.getString("mobs."+mobName+".type");
    	if(tipo.equalsIgnoreCase("zombie")) {
    		Zombie z = (Zombie) loc.getWorld().spawnEntity(loc,
                    EntityType.ZOMBIE);
    		if(dungeonConfig.getItemStack("mobs."+mobName+".mainHand")!=null && !dungeonConfig.getItemStack("mobs."+mobName+".mainHand").getType().equals(Material.AIR)) {
    			z.getEquipment().setItemInMainHand(dungeonConfig.getItemStack("mobs."+mobName+".mainHand"));
    		}
    		if(dungeonConfig.getItemStack("mobs."+mobName+".boots")!=null && !dungeonConfig.getItemStack("mobs."+mobName+".boots").getType().equals(Material.AIR)) {
    			z.getEquipment().setBoots(dungeonConfig.getItemStack("mobs."+mobName+".boots"));
    		}
    		if(dungeonConfig.getItemStack("mobs."+mobName+".leggins")!=null && !dungeonConfig.getItemStack("mobs."+mobName+".leggins").getType().equals(Material.AIR)) {
    			z.getEquipment().setLeggings(dungeonConfig.getItemStack("mobs."+mobName+".leggins"));
    		}
    		if(dungeonConfig.getItemStack("mobs."+mobName+".chestplate")!=null && !dungeonConfig.getItemStack("mobs."+mobName+".chestplate").getType().equals(Material.AIR)) {
    			z.getEquipment().setChestplate(dungeonConfig.getItemStack("mobs."+mobName+".chestplate"));
    		}
    		if(dungeonConfig.getItemStack("mobs."+mobName+".helmet")!=null && !dungeonConfig.getItemStack("mobs."+mobName+".helmet").getType().equals(Material.AIR)) {
    			z.getEquipment().setHelmet(dungeonConfig.getItemStack("mobs."+mobName+".helmet"));
    		}
    		z.setMaxHealth(dungeonConfig.getDouble("mobs."+mobName+".health"));
    		z.setHealth(dungeonConfig.getDouble("mobs."+mobName+".health"));
    		return z;
    	} else {
    		return null;
    	}
    }
}
