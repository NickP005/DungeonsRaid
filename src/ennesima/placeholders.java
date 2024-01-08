package ennesima;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class placeholders extends PlaceholderExpansion {

    /*
    The identifier, shouldn't contain any _ or %
     */
    public String getIdentifier() {
        return "tutorial";
    }

    public String getPlugin() {
        return null;
    }


    /*
     The author of the Placeholder
     This cannot be null
     */
    public String getAuthor() {
        return "Banbeucmas";
    }

    /*
     Same with #getAuthor() but for versioon
     This cannot be null
     */

    public String getVersion() {
        return "SomeMagicalVersion";
    }

    /*
    Use this method to setup placeholders
    This is somewhat similar to EZPlaceholderhook
     */
    public String onPlaceholderRequest(Player player, String identifier) {
        /*
         %tutorial_onlines%
         Returns the number of online players
          */
        if(identifier.equalsIgnoreCase("onlines")){
            return String.valueOf(Bukkit.getOnlinePlayers().size());
        }
 
        /*
        Check if the player is online,
        You should do this before doing anything regarding players
         */
        if(player == null){
            return "";
        }
 
        /*
        %tutorial_name%
        Returns the player name
         */
        if(identifier.equalsIgnoreCase("mio_placeholder")){
            return "CiaoMondooo";
        }
 
 
        return null;
    }
}
 