package org.aurora.tag.util;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

/**
 * Implements a Timer to delay specified events such as the start of a game of Tag.
 * @author RussianMushroom
 */
public class Timer {
	
	private static BukkitTask upgradeTask;
	private static boolean upgradeIsActive = true;
	private static boolean isGrace = true;
	
	 public static void startUpgradeTimer() {
		 upgradeIsActive = false;
		 Runnable run = () -> {
			 TagManager.getVotedPlayers().forEach(player -> {
				 player.sendMessage(ChatColor.GOLD
						 + ConfigLoader.getDefault("Tag.Strings.GetUpgrade"));
			 });
			 upgradeIsActive = true;
		 };
		 				
		 upgradeTask = Bukkit.getServer().getScheduler().runTaskLater(
				 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
				 run,
				 Long.parseLong(ConfigLoader.getDefault("Tag.Timer.TicksBeforeGetBow"))
				 );
	 }
	 
	 public static void startGraceTimer() {
		 TagManager.getVotedPlayers().forEach(player -> {
			 player.sendMessage(ChatColor.GOLD
					 + String.format(ConfigLoader.getDefault("Tag.Strings.Grace"),
					 Integer.parseInt(ConfigLoader.getDefault("Tag.Timer.GradePeriod"))));
		 });
			 
			 Runnable run = () -> {
				 isGrace = false;
				 TagManager.getVotedPlayers().forEach(player -> {
					 player.sendMessage(ChatColor.GOLD
							 + ConfigLoader.getDefault("Tag.Strings.NoGrace"));
					 player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 10, 1);
				 });
			 };
				 
			 Bukkit.getServer().getScheduler().runTaskLater(
					 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
					 run,
					 Long.parseLong(ConfigLoader.getDefault("Tag.Timer.GracePeriod")));
	 }
	 
	 public static void delayStart() {
		// Set game to active
		TagManager.activate();
		TagManager.getVotedPlayers().forEach(player -> {
			 player.sendMessage(ChatColor.GOLD
					 + String.format(ConfigLoader.getDefault("Tag.Strings.NotifyPlayersGameStart"),
							 Integer.parseInt(ConfigLoader.getDefault("Tag.Timer.TicksBeforeTagStart")) / 20));
		 });
		
		 Runnable run = () -> GameCenter.start();
		 
		 Bukkit.getServer().getScheduler().runTaskLater(
				 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
				 run,
				 Long.parseLong(ConfigLoader.getDefault("Tag.Timer.TicksBeforeTagStart"))
				 );
	 }
	 
	public static void disableTimers() {
		if(upgradeTask != null)
			upgradeTask.cancel();
	}
	
	public static boolean upgradeIsActive() {
		return upgradeIsActive;
	}
	
	public static boolean isGrace() {
		return isGrace;
	}
	
}
