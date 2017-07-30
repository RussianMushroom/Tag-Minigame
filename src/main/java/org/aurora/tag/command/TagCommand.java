package org.aurora.tag.command;

import org.aurora.tag.Tag;
import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.util.InventoryManager;
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
		if(args.length == 0 || args.length > 2) {
			displayMenu(sender);
		} else {
				switch (args[0].toLowerCase()) {
				// /tag join & /tag join confirm
				case "join":
					if(sender instanceof ConsoleCommandSender)
						sender.sendMessage(ChatColor.GOLD
								+ "This command cannot be used from the console!");
					else {
						if(sender.hasPermission("tag.join")) {
							if(TagManager.getJoinedPlayers().contains((Player) sender)
									&& !TagManager.getVotedPlayers().contains((Player) sender)
									&& !TagManager.isActive())
								sender.sendMessage(ChatColor.GOLD
										+ "You are already in the Lobby, please vote to start the game!");
							else if(TagManager.getVotedPlayers().contains((Player) sender)
									&& !TagManager.isActive())
								sender.sendMessage(ChatColor.GOLD
										+ "You have already voted. Please wait for the others to vote before the game starts!");
							else if(TagManager.getVotedPlayers().contains((Player) sender)
									&& TagManager.isActive())
								sender.sendMessage(ChatColor.GOLD
										+ "You are already in an active game of Tag. To leave this game, use /tag leave.");
							else if(TagManager.isActive())
								sender.sendMessage(ChatColor.GOLD
										+ "There is already a game of Tag running. Please wait for it to end before joining a new game.");
							else {
								if(args.length == 2) {
									if(args[1].equalsIgnoreCase("confirm"))
										handleJoin((Player) sender);
								} else {
									if(InventoryManager.isEmpty((Player) sender))
										handleJoin((Player) sender);
									else
										sender.sendMessage(ChatColor.GOLD
												+ "Please clear your inventory. Upon clearing it, use /tag join confirm.");
								}
							}
						} else
							notEnoughPermission(sender);	
					}	
					break;
					// /tag start
				case "start":	
					if(sender.hasPermission("tag.start")) {
						if(TagManager.isActive())
							sender.sendMessage(ChatColor.GOLD
									+ "There is already a game of Tag running.");
						else {
							TagManager.migrate();
							GameCenter.start();	
						}
					} else
						notEnoughPermission(sender);
					break;
					// /tag help
				case "help":
					if(sender.hasPermission("tag.help")) {
						// displayHelpMenu(sender);
						sender.sendMessage("Fuck off, I am still working on this");
					} else
						notEnoughPermission(sender);
					break;	
				// /tag leave
				case "leave":
					if(sender instanceof ConsoleCommandSender)
						sender.sendMessage(ChatColor.GOLD
								+ "This command cannot be used from the console!");
					else {
						if(sender.hasPermission("tag.leave")) {
							if(!TagManager.getJoinedPlayers().contains((Player) sender))
								sender.sendMessage(ChatColor.GOLD
										+ "You are not in a Tag game.");
							else {
								TagManager.removePlayer((Player) sender);
								sender.sendMessage(ChatColor.GOLD
										+ "You have left the Tag game. You will be warped to the Lobby.");
								((Player) sender).performCommand("warp " + lobbyWarp);
							}
						} else
							notEnoughPermission(sender);
					}
					break;
				// /tag stop
				case "stop":
					if(sender.hasPermission("tag.stop")) {
						if(!TagManager.isActive())
							sender.sendMessage(ChatColor.GOLD
									+ "There is no game of Tag running.");
						else
							GameCenter.stop();
					} else
						notEnoughPermission(sender);
				}
		}
		
	}
	
	private static void setDefaults() {
		lobbyWarp = ConfigLoader.getDefault("Tag.Arena.Lobby");
	}
	
	private static void handleJoin(Player player) {
		if(TagManager.addPlayer(player)) {
			player.performCommand("warp " + lobbyWarp);
			player.sendMessage(ChatColor.GOLD + "You have been warped to the Lobby.");
		} else
			player.sendMessage(ChatColor.GOLD 
					+ "This current game of Tag is already full. Please wait until it ends before reentering.");
	}
	
	private static void notEnoughPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD 
				+ "You do not have the proper permissions to execute this command!");
	} 
	
	private static void displayMenu(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA 
				+ String.format(
				"%s%s%s%s%s%s%s%s%s%s%s",
				"===============================\n",
				"  Tag-Minigame Menu: \n",
				"===============================\n",
				"  /tag join     Join the tag lobby.\n",
				"  /tag start    Force start the tag minigame.\n",
				"  /tag leave   Exit from a running game of tag.\n",
				"  /tag help     Learn how to play the game.\n",
				" \n",
				TagManager.isActive() 
					? ChatColor.RED + "     Currently there is a game running.\n"
					: ChatColor.GREEN + "     Currently there is no game running.\n",
				ChatColor.AQUA + "     " + TagManager.getJoinedPlayers().size() 
				+ "/" + TagManager.getMaxPlayers() + " joined.\n",
				"===============================\n"
				));
	}
	
	@SuppressWarnings("unused")
	private static void displayHelpMenu(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA 
				+ String.format(
				"%s%s%s%s%s%s%s%s",
				"===============================\n",
				"  Tag-Minigame Help: \n",
				"===============================\n",
				// Space for 4 lines
				"===============================\n"
				));
	}

}
