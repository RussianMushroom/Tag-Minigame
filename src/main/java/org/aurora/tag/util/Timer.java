package org.aurora.tag.util;

import org.aurora.tag.Tag;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.game.TagArena;
import org.aurora.tag.manager.PacketManager;
import org.aurora.tag.scoreboard.CreateScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 * Implements a Timer to delay specified events such as the start of a game of Tag.
 * @author RussianMushroom
 */
public class Timer {

	private int count = 3;
	private BukkitTask upgradeTimer;
	private BukkitTask graceTimer;
	private int countdownRepeatTimer;

	public void createUpgradeTimer(TagArena arena) {
		final Runnable run = () -> {
			arena.getVotedPlayers().forEach(player -> {
				player.sendMessage(ChatColor.GOLD
						+ ConfigLoader.getDefault("Tag.Strings.GetUpgrade"));
			});
			arena.setUpgradeIsActive(true);
		};

		arena.setUpgradeIsActive(false);

		upgradeTimer = Bukkit.getServer().getScheduler().runTaskLater(Tag.getPlugin(),
				run,
				Long.parseLong(ConfigLoader.getDefault("Tag.Timer.TicksBeforeUpgrade")));
	}

	public void stopUpgradeTimer() {
		if(upgradeTimer != null) upgradeTimer.cancel();
	}


	public void createGraceTimer(TagArena arena) {
		boolean displayHologram = ConfigLoader.getFileConfig().getBoolean("Tag.Options.AllowHolographicCountdown");
		arena.setGrace(true);

		arena.getVotedPlayers().forEach(player -> {
			player.sendMessage(ChatColor.GOLD
					+ String.format(ConfigLoader.getDefault("Tag.Strings.Grace"),
							Integer.parseInt(ConfigLoader.getDefault("Tag.Timer.GracePeriod")) / 20));
			// Display scoreboard to players
			CreateScoreboard.updateScoreboard(player, arena);
		});

		Runnable run = () -> {
			arena.getVotedPlayers().forEach(player -> {
				if(displayHologram && Bukkit.getServer().getPluginManager().getPlugin("ProtocolLib").isEnabled())
					PacketManager.displayHologram(ConfigLoader.getDefault("Tag.Strings.NoGrace"),
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

		graceTimer = Bukkit.getServer().getScheduler().runTaskLater(
				Tag.getPlugin(),
				run,
				Long.parseLong(ConfigLoader.getDefault("Tag.Timer.GracePeriod")));
	}

	public void stopGraceTimer() {
		if(graceTimer != null) graceTimer.cancel();
	}

	public void startTimeLeft(TagArena arena) {
		// start a timer that starts 3 seconds earlier than the one above
		boolean displayHologram = ConfigLoader.getFileConfig().getBoolean("Tag.Options.AllowHolographicCountdown");
		long countdown = Long.parseLong(ConfigLoader.getDefault("Tag.Timer.GracePeriod")) - 60;
		count = 3;
		countdownRepeatTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
				Tag.getPlugin(),
				() -> {
					if(count == 1) stopDisplayTimeLeft();

					for(Player player : arena.getVotedPlayers()) {
						if(displayHologram && Bukkit.getServer().getPluginManager().getPlugin("ProtocolLib").isEnabled())
							PacketManager.displayHologram(ConfigLoader.getDefault("Tag.Strings.GracePeriodEndsIn"),
									Integer.toString(count), player);
						else
							player.sendMessage(ChatColor.GOLD + ConfigLoader.getDefault("Tag.Strings.GracePeriodEndsIn")
							+ ": " + ChatColor.YELLOW + count);
					} 
					count--;
				},
				countdown,
				20);

	}

	public void stopDisplayTimeLeft() {
		Bukkit.getServer().getScheduler().cancelTask(countdownRepeatTimer);
	}

	public void delayStart(TagArena arena) {
		arena.getVotedPlayers().forEach(player -> {
			player.sendMessage(ChatColor.GOLD
					+ String.format(ConfigLoader.getDefault("Tag.Strings.NotifyPlayersGameStart"),
							Integer.parseInt(ConfigLoader.getDefault("Tag.Timer.TicksBeforeTagStart")) / 20));
		});

		Runnable run = () -> GameCenter.start(arena);

		Bukkit.getServer().getScheduler().runTaskLater(
				Tag.getPlugin(),
				run,
				Long.parseLong(ConfigLoader.getDefault("Tag.Timer.TicksBeforeTagStart"))
				);
	}
}
