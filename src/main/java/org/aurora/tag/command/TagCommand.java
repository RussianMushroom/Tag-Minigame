package org.aurora.tag.command;
import java.util.List;

import org.aurora.tag.Tag;
import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.leaderboard.LeaderboardManager;
import org.aurora.tag.util.InventoryManager;
import org.aurora.tag.util.Timer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
				case "j":
					if(sender instanceof ConsoleCommandSender)
						sender.sendMessage(ChatColor.GOLD
								+ ConfigLoader.getDefault("Tag.Strings.ConsoleUser"));
					else {
						if(sender.hasPermission("tag.join")) {
							if(TagManager.getJoinedPlayers().contains((Player) sender)
									&& !TagManager.getVotedPlayers().contains((Player) sender)
									&& !TagManager.isActive())
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.AlreadyInLobby"));
							else if(TagManager.getVotedPlayers().contains((Player) sender)
									&& !TagManager.isActive())
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.PlayerAlreadyVote"));
							else if(TagManager.getVotedPlayers().contains((Player) sender)
									&& TagManager.isActive())
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.AlreadyInActiveGame"));
							else if(TagManager.isActive())
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.AlreadyActiveGameWait"));
							else {
								if(args.length == 2) {
									if(args[1].equalsIgnoreCase("confirm"))
										handleJoin((Player) sender);
								} else {
									if(InventoryManager.isEmpty((Player) sender))
										handleJoin((Player) sender);
									else
										sender.sendMessage(ChatColor.GOLD
												+ ConfigLoader.getDefault("Tag.Strings.PlayerClearInventory"));
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
									+ ConfigLoader.getDefault("Tag.Strings.AlreadyActive"));
						else {
							TagManager.migrate();
							Timer.delayStart();
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
								+ ConfigLoader.getDefault("Tag.Strings.ConsoleUser"));
					else {
						if(sender.hasPermission("tag.leave")) {
							if(!TagManager.getJoinedPlayers().contains((Player) sender))
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.PlayerNotInGame"));
							else {
								TagManager.removePlayer((Player) sender);
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.PlayerLeaves"));
								((Player) sender).performCommand("back");
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
									+ ConfigLoader.getDefault("Tag.Strings.NotActive"));
						else
							GameCenter.stop();
					} else
						notEnoughPermission(sender);
					
					break;
				// /tag set [arena|rip|lobby]
				case "set":
					if(sender instanceof ConsoleCommandSender)
						sender.sendMessage(ChatColor.GOLD
								+ ConfigLoader.getDefault("Tag.Strings.ConsoleUser"));
					else if(!sender.hasPermission("tag.set")) 
						notEnoughPermission(sender);
					else {
						if(args.length != 2) {
							sender.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.SetSyntax"));
						} else {
							if (args[1].equalsIgnoreCase("arena")) 
								setArena("Tag.Arena.Arena", sender);					
							else if(args[1].equalsIgnoreCase("lobby")) 
								setArena("Tag.Arena.Lobby", sender);
							else if(args[1].equalsIgnoreCase("rip")) 
								setArena("Tag.Arena.Rip", sender);
						}
					}
					break;
				/*
				// /tag leaderboard
				case "leaderboard":
				case "lb":
					if(!sender.hasPermission("tag.leaderboard"))
						notEnoughPermission(sender);
					else {
						if(!LeaderboardManager.getLeaderboardTop().isPresent())
							Bukkit.getServer().getLogger().warning("[Tag] leaderboard.yml was not detected!");
						else 
							displayLeaderboard(sender, args[1]);
					}
					*/
				}
		}
		
	}
	
	
	private static void setArena(String path, CommandSender sender) {
		Location location = ((Player) sender).getLocation();
		
		ConfigLoader.set(
				path,
				String.format("%s_%s,%s,%s",
						((Player) sender).getWorld().getName(),
						location.getBlockX(),
						location.getBlockY(),
						location.getBlockZ())
				);
		sender.sendMessage(ChatColor.GOLD
				+ ConfigLoader.getDefault("Tag.Strings.ArenaAdded"));
	}
	
	private static void setDefaults() {
		lobbyWarp = ConfigLoader.getDefault("Tag.Arena.Lobby");
	}
	
	private static void handleJoin(Player player) {
		if(TagManager.addPlayer(player)) {
			if(player.getGameMode() != GameMode.SURVIVAL) {
				player.sendMessage(ChatColor.GOLD
						+ ConfigLoader.getDefault("Tag.Strings.PlayerChangeGameMode"));
				player.setGameMode(GameMode.SURVIVAL);
			}
			
			TagManager.legalWarp(lobbyWarp, player);
			player.sendMessage(ChatColor.GOLD + ConfigLoader.getDefault("Tag.Strings.PlayerWarpLobby"));
		} else
			player.sendMessage(ChatColor.GOLD 
					+ ConfigLoader.getDefault("Tag.Strings.GameIsFull"));
	}
	
	private static void notEnoughPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD 
				+ ConfigLoader.getDefault("Tag.Strings.NoPerm"));
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
				"     Status: " + (TagManager.isActive() 
					? ChatColor.RED + "ACTIVE.\n"
					: ChatColor.GREEN + "OPEN.\n"),
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
				"  The Tag-Minigame is a game where ",
				// Space for 4 lines
				"===============================\n"
				));
	}
	
	@SuppressWarnings("unused")
	private static void displayLeaderboard(CommandSender sender, String top) {
		List<String> leaderboard = LeaderboardManager.getLeaderboardTop().get();
		
		// Make sure that there are no NPE's
		int defaultSize = isInteger(top) ? Integer.parseInt(top) : 10;
		defaultSize = defaultSize > leaderboard.size() ? leaderboard.size() : defaultSize;
		
		StringBuilder sBuilder = new StringBuilder();
		int count = 0;
		
		sBuilder.append(ChatColor.GOLD + "Leaderboard: \n");
		for(String stats : leaderboard) {
			String player = stats.split("~")[0];
			int[] wins = LeaderboardManager.stringToIntArray(stats.split("~")[1].split("_"));
			sBuilder.append(String.format(ChatColor.GOLD + "[%s]: W:%s L:%s\n", 
					player, 
					ChatColor.GREEN + "" + wins[0],
					ChatColor.RED + "" + wins[1]));
			count++;
			if(count == defaultSize)
				break;
		}
		
		sender.sendMessage(sBuilder.toString());
	}
	
	
	private static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
