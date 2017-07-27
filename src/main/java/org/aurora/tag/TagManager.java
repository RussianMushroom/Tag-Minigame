package org.aurora.tag;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * 
 * @author RussianMushroom
 *
 */
public class TagManager {

	//@config
	private static final int MAX_PLAYERS = 10;
	private static List<Player> joinedPlayers = new ArrayList<>();
	private static boolean isActive = false;
	
	public static boolean addPlayer(Player player) {
		if(!(joinedPlayers.size() + 1 > MAX_PLAYERS))
			joinedPlayers.add(player);
		else
			return false;
		return true;
	}
	
	
	// Getters and Setters
	
	public static List<Player> getJoinedPlayers() {
		return joinedPlayers;
	}
	
	public static boolean isActive() {
		return isActive;
	}
	
	
}
