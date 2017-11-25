
package org.aurora.tag.game;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.aurora.tag.config.ArenaConfig;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.manager.InventoryManager;
import org.aurora.tag.manager.LeaderboardManager;
import org.aurora.tag.scoreboard.CreateScoreboard;
import org.aurora.tag.util.GeneralMethods;
import org.aurora.tag.util.MethodBypass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.russianmushroom.rewardlib.reward.ApplyReward;

/**
 * 
 * @author RussianMushroom
 *
 */
public class GameCenter {
	
	private static List<TagArena> activeGames = new ArrayList<>();
	
	public static void start(TagArena arena) {
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ String.format(
						ConfigLoader.getDefault("Tag.Strings.GameStart"),
						GeneralMethods.toProperCase(arena.getArena())));
		
		// Warp everyone to the arena
		arena.getVotedPlayers().forEach(player -> {
			MethodBypass.legalWarp(
					ArenaConfig.getDefault("Arena." + GameCenter.getArena(player).getArena() + ".Warps.Arena"), 
					player, 
					arena);
			if(getRelativeReward(arena.getVotedPlayers().size(), arena) 
					> ConfigLoader.getFileConfig().getInt("Tag.Rewards.Money")) {
				player.sendMessage(ChatColor.GOLD
						+ String.format(ConfigLoader.getDefault("Tag.Strings.NotifyAmountToWin"),
								getRelativeReward(arena.getVotedPlayers().size(), arena)));
			}
		}); 
		
		// Clear all inventories and apply items 
		InventoryManager.clearPlayerInventory(false, arena);
		
		InventoryManager.setTagBaton(arena);
		InventoryManager.setArmour(arena);
		
		// Start grace period countdown
		arena.startGraceTimer();
	}
	
	public static void registerWinner(Player player, TagArena arena) {
		// Update leaderboard
		LeaderboardManager.add(player, true);
		giveMoney(player, arena.getVotedPlayers().size(), arena);
		addCredits(player);
		
		// Broadcast the player's win to the server
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ String.format(ConfigLoader.getDefault("Tag.Strings.BroadcastWinner"), player.getName()));
		
		// Reopen the game however dont remove players from Joined list so that they can rejoin by clicking the sign
		stop(arena, true);
	}
	
	public static void stop(TagArena arena, boolean allowRejoinBySign) {
		if(arena.isActive()) {
			Bukkit.broadcastMessage(ChatColor.GOLD
					+ String.format(
							ConfigLoader.getDefault("Tag.Strings.GameStop"),
							GeneralMethods.toProperCase(arena.getArena())));
			
			forceStop(arena, allowRejoinBySign);
		}
	}
	
	public static void forceStop(TagArena arena, boolean allowRejoinBySign) {
		// Clear inventories and set gamemode to the one in the config and set game to inactive
		InventoryManager.clearPlayerInventory(true, arena);
		// Remove scoreboards
		CreateScoreboard.removeScoreboardFromArena(arena);
		
		if(allowRejoinBySign)
			arena.deactivateWithVote();
		else arena.deactivate();
		
		arena.disableTimers();
	}
	
	public static void stopAll() {
		activeGames.forEach(tagArena -> {
			forceStop(tagArena, false);
		});
	}

	private static void giveMoney(Player player, int amountOfPlayers, TagArena arena) {
		int moneyReward = Integer.parseInt(ConfigLoader.getDefault("Tag.Rewards.Money"));
		int relativeReward = getRelativeReward(amountOfPlayers, arena);
		
		if(Bukkit.getServer().getPluginManager().getPlugin("Essentials") != null) {
			if(ConfigLoader.getFileConfig().getBoolean("Tag.Rewards.AllowRewardsRelativeToPlayerAmount")) {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						"economy give " + player.getName() + " " + relativeReward);
				player.sendMessage(ChatColor.GOLD
					+ String.format(ConfigLoader.getDefault("Tag.Strings.PlayerReceiveMoney"), moneyReward));
			} else {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						"economy give " + player.getName() + " " + moneyReward);
				player.sendMessage(ChatColor.GOLD
					+ String.format(ConfigLoader.getDefault("Tag.Strings.PlayerReceiveMoney"), moneyReward));
			}	
		}	
	}
	
	public static int getRelativeReward(int amountOfPlayers, TagArena arena) {
		int baseAmount = ConfigLoader.getFileConfig().getInt("Tag.Rewards.Money");
		int totalWins = 0,
			totalLosses = 0;
		// Fallback returnAmount is 3
		int returnAmount = 0;
		
		for(Player player : arena.getVotedPlayers()) {
			if(LeaderboardManager.getPlayerStat(player).isPresent()) {
				int[] winsLosses = LeaderboardManager.getPlayerStat(player).get();
				totalWins += winsLosses[0];
				totalLosses += winsLosses[1];	
			}
		}
		
		returnAmount = 
				(int) (((baseAmount + amountOfPlayers) 
						* Math.pow(Math.E, ((totalWins / 100) - (totalLosses / 100)))) + 3);
		
		return returnAmount < 3 ? 3 : returnAmount;
	}

	private static void addCredits(Player player) {
		if(!Bukkit.getServer().getPluginManager().isPluginEnabled("RewardLib"))
			Bukkit.getServer().getLogger().warning("[Tag] RewardLib was not detected, please enable/add this plugin in order to enable item rewards.");
		else {
			ApplyReward.apply(player, ConfigLoader.getFileConfig().getInt("Tag.Rewards.AmountOfCreditsPerGame"));
		}
			
	}
	
	public static boolean containsArena(TagArena arena) {
		if(activeGames.isEmpty())
			return false;
		else {
			boolean containsArena = false;
			for(TagArena activeArena : activeGames) {
				if(activeArena.getArena().equals(arena.getArena())) {
					containsArena = true;
					break;
				}
			}
			return containsArena;
		}
	}
	
	public static List<TagArena> getActiveGames() {
		return activeGames;
	}
	
	public static void addGame(TagArena arena) {
		activeGames.add(arena);
	}
	
	public static TagArena getArena(String arena) {
		try {
			return activeGames.parallelStream()
					.filter(activeArena -> activeArena.getArena().equals(arena))
					.findFirst()
					.get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public static TagArena getArena(Player player) {
		try {
			return activeGames.parallelStream()
					.filter(activeArena -> activeArena.getJoinedPlayers().contains(player))
					.findFirst()
					.get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public static boolean arenaContainsPlayerAsType(String type, Player player) {
		List<Player> players = new ArrayList<>();
			
		for(TagArena activeArena : activeGames) {
			if(type.equals("joined"))
				players = activeArena.getJoinedPlayers();
			else if(type.equals("voted"))
				players = activeArena.getVotedPlayers();
			else if(type.equals("rip"))
				players = activeArena.getRipPlayers();
			
			
			if(players.contains(player))
				return true;
		}
		return false;
	}
	
	public static List<String> availableArenas() {
		try {
			return ArenaConfig.getYamlConfig().getConfigurationSection("Arena")
					.getKeys(false)
					.stream()
					.collect(Collectors.toList());
		} catch(NullPointerException e) {
			return new ArrayList<>();
		}
		
	}
	
	public static boolean arenaHasAllArenasSet(TagArena arena) {
		try {
			Set<String> subkeys = ArenaConfig
					.getYamlConfig()
					.getConfigurationSection("Arena." + arena.getArena() + ".Warps")
					.getKeys(false);
			
			return subkeys.contains("Arena") && subkeys.contains("Rip") && subkeys.contains("Lobby");
		} catch (NullPointerException e) {
			return false;
		}
	}
}
