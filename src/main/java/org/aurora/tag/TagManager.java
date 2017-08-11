package org.aurora.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.util.InventoryManager;
import org.aurora.tag.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Deals with elements necessary for the Tag minigame.
 * @author RussianMushroom
 */
public class TagManager {

	private static final int MAX_PLAYERS = Integer.parseInt(ConfigLoader.getDefault("Tag.MaxPlayers"));
	private static List<Player> joinedPlayers = new ArrayList<>();
	private static List<Player> votedPlayers = new ArrayList<>();
	private static List<Player> ripPlayers = new ArrayList<>();
	private static List<Player> winners = new ArrayList<>();
	private static boolean isActive = false;
	private static boolean canWarp = false;
	
	public static boolean addPlayer(Player player) {
		if(!(joinedPlayers.size() + 1 > MAX_PLAYERS) && !isActive) {
			joinedPlayers.add(player);
		}
		else
			return false;
		return true;
	}
	
	public static boolean vote(Player player) {
		if(!votedPlayers.contains(player)) {
			votedPlayers.add(player);
			return true;
		}
		return false;
	}
	
	public static void addRip(Player player) {
		if(!ripPlayers.contains(player))
			ripPlayers.add(player);
		// Check if all players are in rip
		// if so, end the game without a winner
		if(votedPlayers.size() == ripPlayers.size())
			GameCenter.stop();
	}
	
	// Deal with warps so as to bypass the listener
	public static void legalWarp(String warp, Player player) {
		if(warp.equals("") || warp == null) {
			Bukkit.getServer().getLogger().warning("[Tag] This warp could not be loaded from the config file, please make sure that all warps have been set."
					+ "Do do such, use /tag set [arena | lobby | rip]");
			return;
		}
		
		String worldName = warp.split("_")[0];
		String[] warpList = warp.split("_")[1].split(",");
		Location arenaLocation = new Location(Bukkit.getWorld(worldName),
				Integer.parseInt(warpList[0]),
				Integer.parseInt(warpList[1]),
				Integer.parseInt(warpList[2]));
		if(!canWarp)
			canWarp = true;
		player.teleport(arenaLocation);
	}
	
	public static void prohibitWarp() {
		canWarp = false;
	}
	
	// Switch the Tag game on and off
	
	public static void activate() {
		isActive = true;
	}
	
	public static void deactivate() {
		clearAll();
	}
	
	private static void clearAll() {
		joinedPlayers.clear();
		votedPlayers.clear();
		ripPlayers.clear();
		// Clear map with player's inventory
		InventoryManager.getPlayerInv().clear();
		isActive = false;
	}
	
	public static void checkStartTag() {
		if(votedPlayers.size() == joinedPlayers.size()) {
			// Need at least two players to start a game
			if(!(votedPlayers.size() < 2))
				Timer.delayStart();
		}
	}
	
	public static  void removePlayer(Player player) {
		joinedPlayers.remove(player);
		if(votedPlayers.contains(player))
			votedPlayers.remove(player);
		if(ripPlayers.contains(player))
			ripPlayers.remove(player);
	
		player.performCommand("back");
		
		// Check if game is active and is last person to leave
		if(isActive && votedPlayers.isEmpty())
			GameCenter.stop();
	}
	
	public static void migrate() {
		joinedPlayers.forEach(player -> {
			if(!votedPlayers.contains(player))
				votedPlayers.add(player);
		});
	}
	
	public static boolean isLastPersonStanding(Player player) {
		if(!ripPlayers.contains(player)) 
			if(ripPlayers.size() + 1 == votedPlayers.size())
				return true;
		return false;
	}
	
	public static Player getRandomPlayer(List<Player> playerList) {
		return playerList.get(ThreadLocalRandom.current().nextInt(0, playerList.size() - 1));
	}
	
	public static void claim(Player player) {
		winners.remove(player);
	}
	
	// Getters and Setters
	
	public static List<Player> getJoinedPlayers() {
		return joinedPlayers;
	}
	
	public static List<Player> getVotedPlayers() {
		return votedPlayers;
	}
	
	public static List<Player> getRipPlayers() {
		return ripPlayers;
	}
	
	public static List<Player> getWinners() {
		return winners;
	}
	
	public static void addWinner(Player player) {
		winners.add(player);
	}
	
	public static int getMaxPlayers() {
		return MAX_PLAYERS;
	}
	
	public static boolean isActive() {
		return isActive;
	}

	public static boolean canWarp() {
		return canWarp;
	}
	
}
