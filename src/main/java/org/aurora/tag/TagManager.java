package org.aurora.tag;

import java.util.ArrayList;
import java.util.List;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.bukkit.entity.Player;

/**
 * 
 * @author RussianMushroom
 *
 */
public class TagManager {

	private static final int MAX_PLAYERS = Integer.parseInt(ConfigLoader.getDefault("Tag.MaxPlayers"));
	private static List<Player> joinedPlayers = new ArrayList<>();
	private static List<Player> votedPlayers = new ArrayList<>();
	private static boolean isActive = false;
	
	public static boolean addPlayer(Player player) {
		if(!(joinedPlayers.size() + 1 > MAX_PLAYERS) && !isActive)
			joinedPlayers.add(player);
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
	
	public static void checkStartTag() {
		if(votedPlayers.size() == joinedPlayers.size()) {
			GameCenter.start();
		}
	}
	
	private static void clearAll() {
		joinedPlayers.clear();
		votedPlayers.clear();
		isActive = false;
	}
	
	public static void activate() {
		isActive = true;
	}
	
	public static void deactivate() {
		clearAll();
	}
	
	public static  void removePlayer(Player player) {
		joinedPlayers.remove(player);
		if(votedPlayers.contains(player))
			votedPlayers.remove(player);
		
		// TODO:  Implement a method to warp player back to previous location
		// and reset their inventory
		
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
	
	// Getters and Setters
	
	public static List<Player> getJoinedPlayers() {
		return joinedPlayers;
	}
	
	public static List<Player> getVotedPlayers() {
		return votedPlayers;
	}
	
	public static boolean isActive() {
		return isActive;
	}
	
	public static int getMaxPlayers() {
		return MAX_PLAYERS;
	}
	
}
