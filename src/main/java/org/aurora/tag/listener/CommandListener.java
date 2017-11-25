package org.aurora.tag.listener;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginManager;

public class CommandListener implements Listener{

	// Prevent certain commands from other plugins that make the game unfair
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage().split(" ")[0];
		PluginManager pManager = Bukkit.getServer().getPluginManager();
		
		// Blanket check: check if player has joined any arena
		if(GameCenter.arenaContainsPlayerAsType("joined", event.getPlayer())) {
			if(pManager.getPlugin("Essentials").isEnabled()) 
				prohibitEssentials(command, event);
		}
	}
	
	private void prohibitEssentials(String command, PlayerCommandPreprocessEvent event) {
		if(command.equalsIgnoreCase("near")) {
			event.setCancelled(true);
			sendErrorMessage(event.getPlayer());
		}
	}
	
	private void sendErrorMessage(Player sender) {
		sender.sendMessage(ChatColor.GOLD
				+ ConfigLoader.getDefault("Tag.Strings.IllegalCommandUsed"));
	}
	
}
