package org.aurora.tag.util;

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
	
	 public void startUpgradeTimer(BukkitTask upgradeTask, String arena) {
		 GameCenter.getArena(arena).setUpgradeIsActive(false);
		 Runnable run = () -> {
			 GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
				 player.getPlayer().sendMessage(ChatColor.GOLD
						 + ConfigLoader.getDefault("Tag.Strings.GetUpgrade"));
			 });
			 GameCenter.getArena(arena).setUpgradeIsActive(true);
		 };
		 				
		 upgradeTask = Bukkit.getServer().getScheduler().runTaskLater(
				 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
				 run,
				 Long.parseLong(ConfigLoader.getDefault("Tag.Timer.TicksBeforeUpgrade"))
				 );
	 }
	 
	 public void startGraceTimer(BukkitTask graceTask, String arena) {
		 GameCenter.getArena(arena).setGrace(true);
		 
		 GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
			 player.getPlayer().sendMessage(ChatColor.GOLD
					 + String.format(ConfigLoader.getDefault("Tag.Strings.Grace"),
					 Integer.parseInt(ConfigLoader.getDefault("Tag.Timer.GracePeriod")) / 20));
		 });
			 
			 Runnable run = () -> {
				 GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
					 player.getPlayer().sendMessage(ChatColor.GOLD
							 + ConfigLoader.getDefault("Tag.Strings.NoGrace"));
					 player.getPlayer().playSound(
							 player.getPlayer().getLocation(),
							 Sound.ENTITY_LIGHTNING_THUNDER, 10, 1);
				 });

				 GameCenter.getArena(arena).setGrace(false);
			 };
				 
			 graceTask = Bukkit.getServer().getScheduler().runTaskLater(
					 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
					 run,
					 Long.parseLong(ConfigLoader.getDefault("Tag.Timer.GracePeriod")));
	 }
	 
	 public void delayStart(String arena) {
		// Set game to active
		GameCenter.getArena(arena).activate();
		GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
			 player.getPlayer().sendMessage(ChatColor.GOLD
					 + String.format(ConfigLoader.getDefault("Tag.Strings.NotifyPlayersGameStart"),
							 Integer.parseInt(ConfigLoader.getDefault("Tag.Timer.TicksBeforeTagStart")) / 20));
		 });
		
		 Runnable run = () -> GameCenter.start(arena);
		 
		 Bukkit.getServer().getScheduler().runTaskLater(
				 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
				 run,
				 Long.parseLong(ConfigLoader.getDefault("Tag.Timer.TicksBeforeTagStart"))
				 );
	 }
}
