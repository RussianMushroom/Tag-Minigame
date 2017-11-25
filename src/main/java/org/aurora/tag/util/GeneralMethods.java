package org.aurora.tag.util;

import org.aurora.tag.game.TagArena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * 
 * @author RussianMushroom
 *
 */
public class GeneralMethods {

	public static void displayMessage(TagArena arena, String message) {
		arena.getVotedPlayers().forEach(player -> {
			player.sendMessage(ChatColor.GOLD + message);
		});
	}
	
	public static String toProperCase(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1, word.length());
	}
	
	public static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static void heal(Player player) {
		player.setHealth(20);
		player.getActivePotionEffects().clear();
	}
	
}
