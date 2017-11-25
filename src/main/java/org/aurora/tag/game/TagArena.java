package org.aurora.tag.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.aurora.tag.config.ArenaConfig;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.manager.InventoryManager;
import org.aurora.tag.scoreboard.CreateScoreboard;
import org.aurora.tag.util.MethodBypass;
import org.aurora.tag.util.Timer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	private boolean canChangeGameMode = false;
	private boolean isGrace = true;
	private boolean upgradeIsActive = true;
	private final Timer timer = new Timer();
	private String arena;
	private final GameMode LOBBY_GAMEMODE = GameMode.valueOf(
			(ConfigLoader.getFileConfig() != null
			? ConfigLoader.getDefault("Tag.Options.GameModeAfterFinishGame")
			: "SURVIVAL"));
	private final GameMode RIP_GAMEMODE = GameMode.valueOf(
			(ConfigLoader.getFileConfig() != null
			? ConfigLoader.getDefault("Tag.Options.GameModeWhileInRIP")
			: "SPECTATOR"));
	
	public TagArena(String arena) {
		this.arena = arena;
		this.MAX_PLAYERS = Integer.parseInt(ArenaConfig.getDefault("Arena." + this.arena + ".MaxPlayers"));
	}
	
	public boolean addPlayer(Player player) {
		if(!(joinedPlayers.size() + 1 > MAX_PLAYERS) && !isActive) {
			joinedPlayers.add(player);
		} else return false;
		
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
		if(!ripPlayers.contains(player)) {
			ripPlayers.add(player);
			MethodBypass.legalChangeGameMode(RIP_GAMEMODE, player, this);
		}
	}
	
	public void prohibitWarp() {
		canWarp = false;
	}
	
	public void prohibitGameModeChange() {
		canChangeGameMode = false;
	}
	
	// Switch the Tag game on and off
	public void activate() {
		isActive = true;
	}
	
	public void deactivate() {
		// warp all player to spawn
		joinedPlayers.forEach(player -> {
			MethodBypass.legalWarp("spawn", player, this);
			MethodBypass.legalChangeGameMode(LOBBY_GAMEMODE, player, this);
		});
		
		clearAll();
		isActive = false;
	}
	
	public void deactivateWithVote() {
		// warp all player to the lobby
		joinedPlayers.forEach(player -> {
			MethodBypass.legalWarp(
					ArenaConfig.getDefault("Arena." + arena.toLowerCase() + ".Warps.Lobby"),
					player, this);

			MethodBypass.legalChangeGameMode(LOBBY_GAMEMODE, player, this);
		});
		votedPlayers.clear();
		ripPlayers.clear();

		// Clear map with player's inventory
		clearPlayerInv();
		isActive = false;
	}
	
	private void clearAll() {
		joinedPlayers.clear();
		votedPlayers.clear();
		ripPlayers.clear();
	}
	
	public void checkStartTag() {
		if(votedPlayers.size() == joinedPlayers.size()) {
			// Need at least two players to start a game
			if(!(votedPlayers.size() < 2))
				delayStart();
		}
	}
	
	public void removePlayer(Player player) {
		// reset inventory
		InventoryManager.restoreInv(player, this);
		// warp player that left to spawn
		MethodBypass.legalWarp("spawn", player, this);
		// remove scoreboard from player
		CreateScoreboard.removeScoreboardFromPlayer(player);
		
		joinedPlayers.remove(player);
		
		if(votedPlayers.contains(player))
			votedPlayers.remove(player);
		if(ripPlayers.contains(player))
			ripPlayers.remove(player);

		CreateScoreboard.updateScoreboard(this);
		// Check if game is active and is last person to leave
		if(isActive && votedPlayers.isEmpty())
			GameCenter.stop(this, false);
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
		timer.stopGraceTimer();
		timer.stopUpgradeTimer();
		timer.stopDisplayTimeLeft();
	}
	
	public void startGraceTimer() {
		timer.createGraceTimer(this);
		timer.startTimeLeft(this);
	}
	
	public void startUpgradeTimer() {
		timer.createUpgradeTimer(this);
	}
	
	public void delayStart() {
		activate();
		Timer delayStartTimer = new Timer();
		delayStartTimer.delayStart(this);
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
	
	public boolean canChangeGameMode() {
		return canChangeGameMode;
	}
	
	public String getArena() {
		return arena;
	}
	
	public boolean arenaIsEmpty() {
		return votedPlayers.size() == ripPlayers.size();
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
	
	public void setCanWarp(boolean canWarp) {
		this.canWarp = canWarp;
	}
	
	public void setCanChangeGameMode(boolean canChangeGameMode) {
		this.canChangeGameMode = canChangeGameMode;
	}
	
}
