package ennesima;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import org.bukkit.WorldCreator;

//facendo extends JavaPlugin hai creato una nuova classe principale. Non va bene. Quello che devi semplicemente
//fare è prendere la directory del mondo copiarla ed incollarla nella cartella /plugins/DungeonsRaid/worldcache
public class worldUtility { 

	public void dupeWorld() {
		Main maiin = new Main();
		File dataFolder = new File(maiin.getDataFolder().getPath());
		String strData = dataFolder.toString();
		String[] split = strData.toString().split(File.pathSeparator);
		String rootFolder = split[split.length - 3];
		File root = new File(rootFolder);

		File srcDir = new File(root+File.pathSeparator+"world");
		if (!srcDir.exists()) {
		    Bukkit.getLogger().warning("Il backup non esiste");
		    //File destDir = new File(root+File.pathSeparator+"prova");
		    return;
		}
		File destDir = new File(root+File.pathSeparator+"prova");
		try {
		    FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException ex) {
		     ex.printStackTrace();
		}
		Bukkit.getServer().createWorld(new WorldCreator("world"));

	}
	
	public void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
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


}
