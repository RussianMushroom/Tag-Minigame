package org.aurora.tag.game;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.util.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * 
 * @author RussianMushroom
 *
 */
public class GameCenter {
	
	public static void start() {
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ ConfigLoader.getDefault("Tag.Strings.GameStart"));
		
		// Clear all inventories and apply items 
		InventoryManager.clearPlayerInventory();
		
		InventoryManager.setTagBaton();
		InventoryManager.setArmour();
	}
	
	public static void stop() {
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ ConfigLoader.getDefault("Tag.Strings.GameStop"));
		
		// Clear inventories and set game to inactive
		InventoryManager.clearPlayerInventory();
		
		TagManager.deactivate();
		
	}
	
	public static void registerWinner(Player player) {
		// Warp all the users back to the Lounge and clear their inventories
		TagManager.getJoinedPlayers().forEach(p -> {
			p.performCommand("warp " + ConfigLoader.getDefault("Tag.Arena.Lobby"));
		});
		InventoryManager.clearPlayerInventory();
		
		// Broadcast the player's win to the server
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ String.format(ConfigLoader.getDefault("Tag.Strings.BroadcastWinner"), player.getName()));
		
		// Give the winner their reward
		InventoryManager.setWinnerReward(player);
	
		// Reopen the game
		stop();
	}
	
}
