package org.aurora.tag.command;

import java.io.IOException;

import org.aurora.tag.Tag;
import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Deals with the use of the tag command.
 * @author RussianMushroom
 *
 */
public class TagCommand {
	
	private static String lobbyWarp = "taglobby";
	
	public static void handle(CommandSender sender, String[] args, Tag plugin) {
		// Set defaults from config file
		setDefaults(plugin);
		
		// Check if the user has the necessary permissions
		if(args.length != 1) {
			displayHelpMenu(sender);
		} else {
			if(!(sender instanceof ConsoleCommandSender))
				switch (args[0].toLowerCase()) {
				case "join":
					if(sender.hasPermission("tag.join"))
						handleJoin((Player) sender);
					else
						notEnoughPermission(sender);
					break;
				case "start":
					if(sender.hasPermission("tag.start"))
						Bukkit.broadcastMessage("okay");// Activate Tag game
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
	
	private static void setDefaults(Tag plugin) {
		FileConfiguration fConfig = plugin.getConfig();
		
		try {
			fConfig.load(ConfigFile.getConfigFile());
			
			// Set defaults
			lobbyWarp = fConfig.getString("Tag.Arena.Lobby");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private static void handleJoin(Player player) {
		if(TagManager.addPlayer(player)) {
			//@config
			player.sendMessage("You have been warped to the Lobby.");
			player.performCommand("warp " + lobbyWarp);
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
