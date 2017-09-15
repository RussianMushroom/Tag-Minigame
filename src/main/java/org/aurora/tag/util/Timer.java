package org.aurora.tag.util;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.game.TagArena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

/**
 * Implements a Timer to delay specified events such as the start of a game of Tag.
 * @author RussianMushroom
 */
public class Timer {

	 private Integer taskId;
	 private int count = 3;
	
	 public void startUpgradeTimer(BukkitTask upgradeTask, String arena) {
		 GameCenter.getArena(arena).setUpgradeIsActive(false);
		 Runnable run = () -> {
			 GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
				 player.sendMessage(ChatColor.GOLD
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
	 
	 public void startGraceTimer(BukkitTask graceTask, String tagArena) {
		 TagArena arena = GameCenter.getArena(tagArena);
		 boolean displayHologram = ConfigLoader.getFileConfig().getBoolean("Tag.Options.AllowHolographicCountdown");
		 
		 arena.setGrace(true);
		 
		 arena.getVotedPlayers().forEach(player -> {
			 player.sendMessage(ChatColor.GOLD
					 + String.format(ConfigLoader.getDefault("Tag.Strings.Grace"),
					 Integer.parseInt(ConfigLoader.getDefault("Tag.Timer.GracePeriod")) / 20));
		 });
			 
			 Runnable run = () -> {
				 arena.getVotedPlayers().forEach(player -> {
					 if(displayHologram && Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
						 HologramManager.display(ConfigLoader.getDefault("Tag.Strings.NoGrace"),
								 ConfigLoader.getDefault("Tag.Strings.GracePeriodGoodLuck"), player);
					 else
						 player.sendMessage(ChatColor.GOLD + ConfigLoader.getDefault("Tag.Strings.NoGrace") 
						 	+ " " + ConfigLoader.getDefault("Tag.Strings.GracePeriodGoodLuck"));
					 player.playSound(
							 player.getLocation(),
							 Sound.ENTITY_LIGHTNING_THUNDER, 10, 1);
				 });

				 arena.setGrace(false);
			 };
				 
			 graceTask = Bukkit.getServer().getScheduler().runTaskLater(
					 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
					 run,
					 Long.parseLong(ConfigLoader.getDefault("Tag.Timer.GracePeriod")));
			 
			 // start a timer that starts 3 seconds earlier than the one above
			 long countdown = Long.parseLong(ConfigLoader.getDefault("Tag.Timer.GracePeriod")) - 60; 
			 
			 taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
					 Bukkit.getServer().getPluginManager().getPlugin("Tag"),
					 () -> {
						 displayTimeLeft(arena, displayHologram);
					 }, countdown, 20);
	 }
	 
	 private void displayTimeLeft(TagArena arena, boolean displayHologram) {
		 if(count == 1)
			 cancelCountdownTask(); 
		 
		 // TODO add check for ProtocolLib
		 arena.getVotedPlayers().forEach(player -> {
			 if(displayHologram && Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
				 HologramManager.display(ConfigLoader.getDefault("Tag.Strings.GracePeriodEndsIn"),
						 Integer.toString(count), player);
			 else
				 player.sendMessage(ChatColor.GOLD + ConfigLoader.getDefault("Tag.Strings.GracePeriodEndsIn")
				 	+ ": " + ChatColor.YELLOW + count);
		 });
		 
		 count--;
	 }
	 
	 private void cancelCountdownTask() {
		 Bukkit.getScheduler().cancelTask(taskId);
	 }
	 
	 public void delayStart(String arena) {
		// Set game to active
		GameCenter.getArena(arena).activate();
		GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
			 player.sendMessage(ChatColor.GOLD
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
