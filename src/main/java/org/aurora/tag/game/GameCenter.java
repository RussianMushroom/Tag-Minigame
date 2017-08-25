
package org.aurora.tag.game;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.leaderboard.LeaderboardManager;
import org.aurora.tag.util.InventoryManager;
import org.aurora.tag.util.MethodBypass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * 
 * @author RussianMushroom
 *
 */
public class GameCenter {
	
	private static List<TagArena> activeGames = new ArrayList<>();
	
	public static void start(String arena) {
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ String.format(
						ConfigLoader.getDefault("Tag.Strings.GameStart"),
						arena
				));
		
		// Warp everyone to the arena
		GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
			MethodBypass.legalWarp(
					ConfigLoader.getDefault("Tag.Arena." + GameCenter.getArena(player).getArena() + ".Warps.Arena"), player, arena);
		}); 
		
		// Clear all inventories and apply items 
		InventoryManager.clearPlayerInventory(false, arena);
		
		InventoryManager.setTagBaton(arena);
		InventoryManager.setArmour(arena);
		
		// Start grace period countdown
		GameCenter.getArena(arena).startGraceTimer();
	}
	
	public static void stop(String arena) {
		if(GameCenter.getArena(arena).isActive())
			Bukkit.broadcastMessage(ChatColor.GOLD
					+ String.format(
							ConfigLoader.getDefault("Tag.Strings.GameStop"),
							arena));
		
		forceStop(arena);
	}
	
	public static void forceStop(String arena) {
		// Clear inventories and set game to inactive
		InventoryManager.clearPlayerInventory(true, arena);
		getArena(arena).deactivate();
		GameCenter.getArena(arena).disableTimers();
	}
	
	public static void stopAll() {
		activeGames.forEach(tagArena -> {
			forceStop(tagArena.getArena());
		});
	}
	
	public static void registerWinner(Player player, String arena) {
		// Update leaderboard
		LeaderboardManager.add(player, true);
		giveMoney(player);
		addCredits(player);
		
		getArena(arena).getVotedPlayers().forEach(p -> {
			MethodBypass.legalWarp(
					ConfigLoader.getDefault("Tag.Arena." + GameCenter.getArena(p).getArena() + ".Warps.Lobby"), p, arena);
			LeaderboardManager.add(p.getPlayer(), false);
		});
		
		// Broadcast the player's win to the server
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ String.format(ConfigLoader.getDefault("Tag.Strings.BroadcastWinner"), player.getName()));
		
		// Reopen the game
		stop(arena);
	}

	private static void giveMoney(Player player) {
		int moneyReward = Integer.parseInt(ConfigLoader.getDefault("Tag.Rewards.Money"));
		
		if(Bukkit.getServer().getPluginManager().getPlugin("Essentials") != null) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
				"economy give " + player.getName() + " " + moneyReward);
			player.sendMessage(ChatColor.GOLD
				+ String.format(ConfigLoader.getDefault("Tag.Strings.PlayerReceiveMoney"), moneyReward));
		}	
	}
	
	private static void addCredits(Player player) {
		if(!Bukkit.getServer().getPluginManager().isPluginEnabled("RewardLib"))
			Bukkit.getServer().getLogger().warning("[Tag] RewardLib was not detected, please enable/add this plugin in order to enable item rewards.");
		else {
			// Implement Credit.addCredit(plugin, player);
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
			return ConfigLoader.getFileConfig().getConfigurationSection("Tag.Arena")
					.getKeys(false)
					.stream()
					.collect(Collectors.toList());
		} catch(NullPointerException e) {
			return new ArrayList<>();
		}
		
	}
	
	public static boolean arenaHasAllArenasSet(TagArena arena) {
		ConfigurationSection cSection = ConfigLoader
				.getFileConfig().getConfigurationSection("Tag.Arenas." + arena.getArena());
		
		return cSection.contains("Arena") && cSection.contains("Rip") && cSection.contains("Lobby");
	}
}
