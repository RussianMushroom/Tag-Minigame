package org.aurora.tag.config;

import java.io.File;
import java.nio.file.Path;

import org.bukkit.Bukkit;

/**
 * 
 * @author RussianMushroom
 *
 */
public class ConfigFile {

	private static final Path CONFIG_PATH = new File("plugins/Tag/").toPath();
	public static final String CONFIG = "config.yml";
	public static final String LEADERBOARD = "leaderboard.yml";
	
	public static Path getConfigPath() {
		if(!CONFIG_PATH.toFile().exists())
			if(CONFIG_PATH.toFile().mkdir())
				Bukkit.getServer().getLogger()
					.info("[Tag] The config file has been successfully created!");
			
		return CONFIG_PATH;
	}
	
	public static File getConfigFile() {
		return new File(CONFIG_PATH + File.separator + CONFIG);
	}
	
	public static File getLeaderboardFile()  {
		return new File(CONFIG_PATH + File.separator + LEADERBOARD);
	}
	
}