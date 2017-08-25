package org.aurora.tag.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.util.Timer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

/**
 * Deals with elements necessary for the Tag minigame.
 * @author RussianMushroom
 */
public class TagArena {

	private final int MAX_PLAYERS;
	private List<Player> joinedPlayers = new ArrayList<>();
	private List<Player> votedPlayers = new ArrayList<>();
	private List<Player> ripPlayers = new ArrayList<>();
	private Map<Player, ItemStack[]> playerInv = new HashMap<>();
	private boolean isActive = false;
	private boolean canWarp = false;
	private boolean isGrace = true;
	private boolean upgradeIsActive = true;
	private BukkitTask upgradeTask;
	private BukkitTask graceTask;
	private String arena;
	
	public TagArena(String arena) {
		this.arena = arena;
		this.MAX_PLAYERS = Integer.parseInt(ConfigLoader.getDefault("Tag.Arena." + this.arena + ".MaxPlayers"));
	}
	
	public boolean addPlayer(Player player) {
		if(!(joinedPlayers.size() + 1 > MAX_PLAYERS) && !isActive) {
			joinedPlayers.add(player);
		}
		else
			return false;
		return true;
	}
	
	public boolean vote(Player player) {
		if(!votedPlayers.contains(player)) {
			votedPlayers.add(player);
			return true;
		}
		return false;
	}
	
	public void addRip(Player player) {
		if(!ripPlayers.contains(player))
			ripPlayers.add(player);
		// Check if all players are in rip
		// if so, end the game without a winner
		if(votedPlayers.size() == ripPlayers.size())
			GameCenter.stop(this.arena);
	}
	
	public void prohibitWarp() {
		canWarp = false;
	}
	
	// Switch the Tag game on and off
	
	public void activate() {
		isActive = true;
	}
	
	public void deactivate() {
		clearAll();
		isActive = false;
	}
	
	private void clearAll() {
		joinedPlayers.clear();
		votedPlayers.clear();
		ripPlayers.clear();
		// Clear map with player's inventory
		clearPlayerInv();
	}
	
	public void checkStartTag() {
		if(votedPlayers.size() == joinedPlayers.size()) {
			// Need at least two players to start a game
			if(!(votedPlayers.size() < 2))
				delayStart();
		}
	}
	
	public void removePlayer(Player player) {
		joinedPlayers.remove(player);
		if(votedPlayers.contains(player))
			votedPlayers.remove(player);
		if(ripPlayers.contains(player))
			ripPlayers.remove(player);
		
		// Check if game is active and is last person to leave
		if(isActive && votedPlayers.isEmpty())
			GameCenter.stop(this.arena);
	}
	
	public void migrate() {
		joinedPlayers.forEach(player -> {
			if(!votedPlayers.contains(player))
				votedPlayers.add(player);
		});
	}
	
	public boolean isLastPersonStanding(Player player) {
		if(!ripPlayers.contains(player)) 
			if(ripPlayers.size() + 1 == votedPlayers.size())
				return true;
		return false;
	}
	
	public Player getRandomPlayer(List<Player> playerList) {
		return playerList.get(ThreadLocalRandom.current().nextInt(0, playerList.size() - 1));
	}
	
	public void disableTimers() {
		if(upgradeTask != null)
			upgradeTask.cancel();
		if(graceTask != null)
			graceTask.cancel();
	}
	
	public void startGraceTimer() {
		Timer graceTimer = new Timer();
		graceTimer.startGraceTimer(graceTask, this.arena);
	}
	
	public void startUpgradeTimer() {
		Timer upgradeTimer = new Timer();
		upgradeTimer.startUpgradeTimer(upgradeTask, this.arena);
	}
	
	public void delayStart() {
		Timer delayStartTimer = new Timer();
		delayStartTimer.delayStart(arena);
	}
	
	// Getters and Setters
	
	public List<Player> getJoinedPlayers() {
		return joinedPlayers;
	}
	
	public List<Player> getVotedPlayers() {
		return votedPlayers;
	}
	
	public List<Player> getRipPlayers() {
		return ripPlayers;
	}
	
	public int getMaxPlayers() {
		return MAX_PLAYERS;
	}
	
	public boolean isActive() {
		return isActive;
	}

	public boolean canWarp() {
		return canWarp;
	}
	
	public String getArena() {
		return arena;
	}
	
	public Map<Player, ItemStack[]> getPlayerInv() {
		return playerInv;
	}
	
	public void addPlayerInv(Player player, ItemStack[] inv) {
		this.playerInv.put(player, inv);
	}
	
	public void clearPlayerInv() {
		playerInv.clear();
	}
	
	public boolean upgradeIsActive() {
		return upgradeIsActive;
	}
	
	public boolean isGrace() {
		return isGrace;
	}
	
	public void setGrace(boolean isGrace) {
		this.isGrace = isGrace;
	}
	
	public void setUpgradeIsActive(boolean upgradeIsActive) {
		this.upgradeIsActive = upgradeIsActive;
	}
	
	public boolean isCanWarp() {
		return canWarp;
	}
	
	public void setCanWarp(boolean canWarp) {
		this.canWarp = canWarp;
	}
	
}
