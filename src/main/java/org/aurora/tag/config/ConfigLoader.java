package org.aurora.tag.config;

import java.io.File;
import java.io.IOException;

import org.aurora.tag.Tag;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 
 * @author RussianMushroom
 *
 */
public class ConfigLoader {

	private static Tag plugin;
	
	public static void load(Tag tag) {
		plugin = tag;
		
		FileConfiguration fConfig = plugin.getConfig();
		
		fConfig.set("Tag.MaxPlayers", Integer.valueOf(10));
		// Default arenas
		fConfig.set("Tag.Arena.Lobby", "taglobby");
		fConfig.set("Tag.Arena.Rip", "tagrip");
		// Time limits
		fConfig.set("Tag.Timer.TicksBeforeTagStart", Integer.valueOf(150));
		
		try {
			File configFile = new File(ConfigFile.getConfigPath() + File.separator + ConfigFile.CONFIG);
			if(!configFile.exists())
				fConfig.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
