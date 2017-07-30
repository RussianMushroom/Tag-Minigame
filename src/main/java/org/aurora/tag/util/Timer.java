package org.aurora.tag.util;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

/**
 * 
 * @author RussianMushroom
 *
 */
public class Timer {
	
	private static BukkitTask bBowTask;
	private static boolean bowIsActive = true;
	
	 public static void startBowTimer() {
		 bowIsActive = false;
		 Runnable run = () -> {
			 TagManager.getVotedPlayers().forEach(player -> {
				 player.sendMessage(ChatColor.GOLD
						 + "You can now get your upgrade!");
			 });
			 bowIsActive = true;
		 };
		 
		 bBowTask = Bukkit.getServer().getScheduler().runTaskLater(
				 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
				 run,
				 Long.parseLong(ConfigLoader.getDefault("Tag.Timer.TicksBeforeGetBow"))
				 );
	 }
	 
	public static void disableTimers() {
		bBowTask.cancel();
	}
	
	public static boolean bowIsActive() {
		return bowIsActive;
	}
	
}
