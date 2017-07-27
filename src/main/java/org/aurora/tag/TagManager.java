package org.aurora.tag;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;

/**
 * 
 * @author RussianMushroom
 *
 */
public class TagManager {

	//@config
	private static final int MAX_PLAYERS = 10;
	private static List<Player> joinedPlayers = new ArrayList<>();
	private static List<Player> votedPlayers = new ArrayList<>();
	private static boolean isActive = false;
	
	public static boolean addPlayer(Player player) {
		if(!(joinedPlayers.size() + 1 > MAX_PLAYERS))
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
		if(votedPlayers.size() == joinedPlayers.size())
	}
	
	// Getters and Setters
	
	public static List<Player> getJoinedPlayers() {
		return joinedPlayers;
	}
	
	public static boolean isActive() {
		return isActive;
	}
	
	
}
