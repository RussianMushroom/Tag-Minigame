package org.aurora.tag.command;

import org.bukkit.command.CommandSender;

public class TagCommand {
	
	public static void handle(CommandSender sender, String[] args) {
		// Check if the user has the necessary permissions
		if(args[0].equalsIgnoreCase("join")) {
			if(sender.hasPermission("tag.join"))
				
			else
				notEnoughPermission(sender);
		}
		else if(args[1].equalsIgnoreCase("start")) {
			if(sender.hasPermission("tag.start"))
				
			else
				notEnoughPermission(sender);
		}
	}
	
	private static void notEnoughPermission(CommandSender sender) {
		sender.sendMessage("You do not have the proper permissions to execute this command!");
	}
	
	private static void displayHelpMenu(CommandSender sender) {
		sender.sendMessage(String.format(
				"%s\n%s\n%s\n\n%s\n%s\n\n%s\n",
				"=====================",
				"----Tag-Minigame----",
				"=====================",
				"/tag join\t Join the tag lobby.",
				"/tag start \t Force start the tag minigame",
				"====================="
				));
	}

}
