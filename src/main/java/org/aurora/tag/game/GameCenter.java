package org.aurora.tag.game;

import org.aurora.tag.TagManager;
import org.aurora.tag.util.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * 
 * @author RussianMushroom
 *
 */
public class GameCenter {
	
	public static void start() {
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ "A new game of Tag has been started. Please wait for this game to end before joining a new one.");
		
		// Set game to active
		TagManager.activate();
		
		// Clear all inventories and apply items 
		InventoryManager.clearPlayerInventory();
		
		InventoryManager.setTagBaton();
		InventoryManager.setArmour();
	}
	
	public static void stop() {
		Bukkit.broadcastMessage(ChatColor.GOLD
				+ "This game of Tag has ended! You may now join a new game.");
		
		// Set game to inactive
		TagManager.deactivate();
		
		InventoryManager.clearPlayerInventory();
	}
	
	
}
