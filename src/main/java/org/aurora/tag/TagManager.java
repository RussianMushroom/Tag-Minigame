package org.aurora.tag;

import java.util.ArrayList;
import java.util.List;

import org.aurora.tag.config.ConfigLoader;
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
			checkStartTag();
			return true;
		}
		return false;
	}
	
	private static void checkStartTag() {
		if(votedPlayers.size() == joinedPlayers.size()) {
			
		}
	}
	
	public static void clearAll() {
		joinedPlayers.clear();
		votedPlayers.clear();
		isActive = false;
	}
	
	public static void activate() {
		isActive = true;
	}
	
	public static void deactivate() {
		isActive = false;
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
	
	
}
