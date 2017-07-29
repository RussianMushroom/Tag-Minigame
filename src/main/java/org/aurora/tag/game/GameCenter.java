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
		
		// These will be added later when user taps on a sign
		InventoryManager.setTagBaton();
		InventoryManager.setTagBow();
	}
	
	
}
