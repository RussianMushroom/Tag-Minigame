package org.aurora.tag.config;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Separate the arenas from the base configuration file for convenience.
 * @author RussianMushroom
 *
 */
public class ArenaConfig {

	private static YamlConfiguration yConfig = new YamlConfiguration(); 
	
	public static void set(String path, Object value) {
		yConfig.set(path, value);
		try {
			yConfig.save(ConfigFile.getArenaFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDefault(String path) {
		try {
			yConfig.load(ConfigFile.getArenaFile());
			return yConfig.getString(path);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void load() {
		try {
			if(!yConfig.contains("Arena")) {
				yConfig.set("Arena", "");
				
				if(!ConfigFile.getArenaFile().exists()) {
					yConfig.save(ConfigFile.getArenaFile());
					Bukkit.getServer().getLogger().info("[Tag] arena.yml has successfully been created!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
			
	public static YamlConfiguration getYamlConfig() {
		try {
			yConfig.load(ConfigFile.getArenaFile());
			return yConfig;
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
