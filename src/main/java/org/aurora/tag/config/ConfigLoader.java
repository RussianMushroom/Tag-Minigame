package org.aurora.tag.config;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.aurora.tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 
 * @author RussianMushroom
 *
 */
public class ConfigLoader {
	
	private static Map<String, String> backup = Collections.synchronizedMap(
			new HashMap<String, String>());
	
	public static void load(Tag tag) {
		Tag plugin = tag;
		
		FileConfiguration fConfig = plugin.getConfig();
		
		setConfigAndBackup(fConfig, "Tag.MaxPlayers", Integer.valueOf(10));
		
		// Default arenas
		setConfigAndBackup(fConfig, "Tag.Arena.Lobby", "taglobby");
		setConfigAndBackup(fConfig, "Tag.Arena.Rip", "tagrip");
		                        
		// Time limits          
		setConfigAndBackup(fConfig, "Tag.Timer.TicksBeforeTagStart", Integer.valueOf(150));
		setConfigAndBackup(fConfig, "Tag.Timer.TicksBeforeGetBow", Integer.valueOf(3600));
		
		// Allowed weapons    
		setConfigAndBackup(fConfig, "Tag.Tools.Baton", "STICK");
		setConfigAndBackup(fConfig, "Tag.Tools.Bow", "BOW");
		setConfigAndBackup(fConfig, "Tag.Tools.ArrowCount", Integer.valueOf(1));
		                       
		// Armour colour       
		setConfigAndBackup(fConfig, "Tag.Armour.Colour.R", Integer.valueOf(50));
		setConfigAndBackup(fConfig, "Tag.Armour.Colour.G", Integer.valueOf(50));
		setConfigAndBackup(fConfig, "Tag.Armour.Colour.B", Integer.valueOf(50));
		
		
		try {
			File configFile = new File(ConfigFile.getConfigPath() + File.separator + ConfigFile.CONFIG);
			if(!configFile.exists())
				fConfig.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDefault(String path) {		
		FileConfiguration fConfig = Bukkit.getServer().getPluginManager()
				.getPlugin("Tag")
				.getConfig();
	
		try {
			fConfig.load(ConfigFile.getConfigFile());
			
			return fConfig.contains(path) ? fConfig.getString(path) : backup.get(path); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		return backup.get(path);
	}
	
	// Add a backup of the values and save it to memory in case the config file cannot be found.
	private static void setConfigAndBackup(FileConfiguration fConfig, String path, Object value) {
		fConfig.set(path, value);
		backup.put(path, value.toString());
	}
	
}
