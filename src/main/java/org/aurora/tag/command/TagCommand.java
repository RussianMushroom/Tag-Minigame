package org.aurora.tag.command;

import org.aurora.tag.Tag;
import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.util.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Deals with the use of the tag command.
 * @author RussianMushroom
 *
 */
public class TagCommand {
	
	private static String lobbyWarp;
	
	public static void handle(CommandSender sender, String[] args, Tag plugin) {
		// Set defaults from config file
		setDefaults();
		
		// Check if the user has the necessary permissions
		if(args.length <= 2 && args.length > 0) {
			displayHelpMenu(sender);
		} else {
			if(!(sender instanceof ConsoleCommandSender))
				switch (args[0].toLowerCase()) {
				case "join":
					if(args.length == 2) {
						if(args[1].equalsIgnoreCase("confirm") && sender.hasPermission("tag.join"))
							handleJoin((Player) sender);
						else {
							if(sender.hasPermission("tag.join"))
								if(InventoryManager.isEmpty((Player) sender))
									handleJoin((Player) sender);
								else
									sender.sendMessage("Please clear your inventory. Upon clearing it, use /tag join confirm.");
							else
								notEnoughPermission(sender);
						}
					}		
					break;
				case "start":
					if(sender.hasPermission("tag.start"))
						Bukkit.broadcastMessage(lobbyWarp); // Activate Tag game
					else
						notEnoughPermission(sender);
					break;
				case "help":
					displayHelpMenu(sender);
					break;	
				}
			else
				sender.sendMessage("This command cannot be used from the console!");
		}
		
	}
	
	private static void setDefaults() {
		lobbyWarp = ConfigLoader.getDefault("Tag.Arena.Lobby");
	}
	
	private static void handleJoin(Player player) {
		if(TagManager.addPlayer(player)) {
			player.performCommand("warp " + lobbyWarp);
			player.sendMessage("You have been warped to the Lobby.");
		} else
			player.sendMessage("This current game of Tag is already full. Please wait until it ends before reentering.");
	}
	
	private static void notEnoughPermission(CommandSender sender) {
		sender.sendMessage("You do not have the proper permissions to execute this command!");
	} 
	
	private static void displayHelpMenu(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA 
				+ String.format(
				"%s\n%s\n%s\n\n%s\n%s\n\n%s\n",
				"===============================",
				"  Tag-Minigame help menu: ",
				"===============================",
				"  /tag join    Join the tag lobby.",
				"  /tag start   Force start the tag minigame.",
				"==============================="
				));
	}

}
