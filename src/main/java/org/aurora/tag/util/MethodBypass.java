package org.aurora.tag.util;

import org.aurora.tag.game.TagArena;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author RussianMushroom
 */
public class MethodBypass {

	// Deal with warps so as to bypass the listener
	public static void legalWarp(String warp, Player player, TagArena arena) {
		if(warp.equals("") || warp == null) {
			Bukkit.getServer().getLogger().warning(
					"[Tag] This warp could not be loaded from the config file, please make sure that all warps have been set."
							+ "To do such, use /tag set [arena | lobby | rip]");
			return;
		} else if(warp.equalsIgnoreCase("spawn")) {
			if(!arena.canWarp())
				arena.setCanWarp(true);
			
			player.performCommand("spawn");
			return;
		} else {
			String worldName = warp.split("_")[0];
			String[] warpList = warp.split("_")[1].split(",");
			Location arenaLocation = new Location(Bukkit.getWorld(worldName), Integer.parseInt(warpList[0]),
					Integer.parseInt(warpList[1]), Integer.parseInt(warpList[2]));
			
			if(!arena.canWarp())
				arena.setCanWarp(true);
			
			player.teleport(arenaLocation);
		}
	}

	public static void legalChangeGameMode(GameMode mode, Player player, TagArena arena) {
		if(!arena.canChangeGameMode())
			arena.setCanChangeGameMode(true);
			
		player.setGameMode(mode);
	}
}
