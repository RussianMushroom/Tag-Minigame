package org.aurora.tag.command;
import java.util.List;
import java.util.stream.Collectors;

import org.aurora.tag.Tag;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.game.TagArena;
import org.aurora.tag.leaderboard.LeaderboardManager;
import org.aurora.tag.util.GeneralMethods;
import org.aurora.tag.util.InventoryManager;
import org.aurora.tag.util.MethodBypass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mkremins.fanciful.FancyMessage;

/**
 * Deals with the use of the tag command.
 * @author RussianMushroom
 *
 */
public class TagCommand {
	
	private static final int DEFAULT_TOP = 10;
	
	public static void handle(CommandSender sender, String[] args, Tag plugin) {
		
		// Check if the user has the necessary permissions
		if(args.length == 0 || args.length > 3) {
			displayMenu(sender);
		} else {
			for(int i = 0; i < args.length; i++) {
				args[i] = args[i].toLowerCase();
			}
			
			Player player = (Player) sender;
				switch (args[0].toLowerCase()) {
				// /tag join & /tag join confirm
				case "join":
				case "j":
					if(sender instanceof ConsoleCommandSender)
						sender.sendMessage(ChatColor.GOLD
								+ ConfigLoader.getDefault("Tag.Strings.ConsoleUser"));
					else {
						if(sender.hasPermission("tag.join")) {
							TagArena arena;
							if(args.length != 2)
								sender.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.JoinSyntax"));

							else {
								if(GameCenter.availableArenas().contains(args[1])) {
									if(GameCenter.getArena(args[1]) == null) {
										arena = new TagArena(args[1]);
										GameCenter.addGame(arena);
									} else 
										arena = GameCenter.getArena(args[1]);
									
									if(GameCenter.arenaContainsPlayerAsType("joined", player))
											sender.sendMessage(ChatColor.GOLD
													+ ConfigLoader.getDefault("Tag.Strings.AlreadyInActiveGame"));
									else if(arena.getJoinedPlayers().contains(player)
											&& !arena.getVotedPlayers().contains(player)
											&& !arena.isActive())
										sender.sendMessage(ChatColor.GOLD
												+ ConfigLoader.getDefault("Tag.Strings.AlreadyInLobby"));
									else if(arena.getVotedPlayers().contains(player)
											&& !arena.isActive())
										sender.sendMessage(ChatColor.GOLD
												+ ConfigLoader.getDefault("Tag.Strings.PlayerAlreadyVote"));
									else if((arena.getVotedPlayers().contains(player) && arena.isActive()))
										sender.sendMessage(ChatColor.GOLD
												+ ConfigLoader.getDefault("Tag.Strings.AlreadyInActiveGame"));
									else if(arena.isActive())
										sender.sendMessage(ChatColor.GOLD
												+ ConfigLoader.getDefault("Tag.Strings.AlreadyActiveGameWait"));
									else {  
										if(GameCenter.arenaHasAllArenasSet(arena)) {
											handleJoin(player, arena);
											sender.sendMessage(ChatColor.GOLD
													+ ConfigLoader.getDefault("Tag.Strings.InventorySaved"));
										} 
										else
											sender.sendMessage(ChatColor.GOLD
													+ ConfigLoader.getDefault("Tag.Strings.ArenaNotSet"));
									}
									
								} else {
									sender.sendMessage(ChatColor.GOLD
											+ ConfigLoader.getDefault("Tag.Strings.ArenaDoesNotExist"));
								}		
							}
							
						} else
							notEnoughPermission(sender);	
					}	
					break;
					// /tag start
				case "start":	
					if(sender.hasPermission("tag.start")) {
						if(args.length == 2) {
							if(GameCenter.getArena(args[1]) != null) {
								if(GameCenter.getArena(args[1]).isActive())
									sender.sendMessage(ChatColor.GOLD
											+ ConfigLoader.getDefault("Tag.Strings.AlreadyActive"));
								else {
									GameCenter.getArena(args[1]).migrate();
									GameCenter.getArena(args[1]).delayStart();
								}
							}
						} else {
							sender.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.InventorySaved"));
						}
					} else
						notEnoughPermission(sender);
					break;
					// /tag help
				case "help":
					if(sender.hasPermission("tag.help") || sender instanceof ConsoleCommandSender)
						displayHelpMenu(sender);
					else
						notEnoughPermission(sender);
					break;	
				// /tag leave
				case "leave":
					if(sender instanceof ConsoleCommandSender)
						sender.sendMessage(ChatColor.GOLD
								+ ConfigLoader.getDefault("Tag.Strings.ConsoleUser"));
					else {
						if(sender.hasPermission("tag.leave")) {
							if(!GameCenter.arenaContainsPlayerAsType("joined", player))
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.PlayerNotInGame"));
							else {
								MethodBypass.legalWarp("spawn", player,
										GameCenter.getArena(player).getArena());
								InventoryManager.restoreInv(player, GameCenter.getArena(player));
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.PlayerHasLeft"));
								GeneralMethods.displayMessage(GameCenter.getArena(player), 
										String.format(ConfigLoader.getDefault("Tag.Strings.PlayerLeaves"),
												player.getName()));
								GameCenter.getArena(player).removePlayer(player);
							}
						} else
							notEnoughPermission(sender);
					}
					break;
				// /tag stop
				case "stop":
					if(sender.hasPermission("tag.stop")) {
						if(args.length == 2) {
							if(GameCenter.getArena(args[1]) != null) {
								TagArena arena = GameCenter.getArena(args[1]);
								
								if(!arena.isActive())
									sender.sendMessage(ChatColor.GOLD
											+ ConfigLoader.getDefault("Tag.Strings.NotActive"));
								else
									GameCenter.stop(arena.getArena(), false);
							}
							
						} else if(args.length == 1) {
							if(GameCenter.availableArenas().isEmpty())
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.NotActive"));
							else
								GameCenter.getActiveGames().forEach(arena -> {
									GameCenter.stop(arena.getArena(), false);
								});
						}
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
						if(args.length != 3) {
							sender.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.SetSyntax"));
						} else {
							if(!ConfigLoader.getFileConfig().contains("Tag.Arena." + args[2]))
								sender.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.ArenaDoesNotExist"));
							else {
								if (args[1].equalsIgnoreCase("arena")) 
									setArena("Tag.Arena." + args[2] + ".Warps.Arena", sender);					
								else if(args[1].equalsIgnoreCase("lobby")) 
									setArena("Tag.Arena." + args[2] + ".Warps.Lobby", sender);
								else if(args[1].equalsIgnoreCase("rip")) 
									setArena("Tag.Arena." + args[2] + ".Warps.Rip", sender);
							}
						}
					}
					break;
				// /tag leaderboard
				case "leaderboard":
				case "lb":
					if(!sender.hasPermission("tag.leaderboard") && !(sender instanceof ConsoleCommandSender)) 
						notEnoughPermission(sender);
					else {
						displayLeaderboard(sender, (args.length == 2) ? args[1] : DEFAULT_TOP + "");
					}
					break;
				case "createarena":
				case "ca":
					if(!sender.hasPermission("tag.createarena") && !(sender instanceof ConsoleCommandSender))
						notEnoughPermission(sender);
					else if(args.length != 2)
						sender.sendMessage(ChatColor.GOLD
								+ ConfigLoader.getDefault("Tag.Strings.CreateSyntax"));
					else {
						if(!GameCenter.availableArenas().contains(args[1])) {
							ConfigLoader.set("Tag.Arena." + args[1], "");
							ConfigLoader.set("Tag.Arena." + args[1] + ".MaxPlayers", "10");
							sender.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.ArenaCreated"));
						} else {
							sender.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.ArenaAlreadyExists"));
						}
					}
					break;
				case "listarena":
				case "la":
					if(!sender.hasPermission("tag.createarena") && !(sender instanceof ConsoleCommandSender))
						notEnoughPermission(sender);
					else {
						if(GameCenter.availableArenas().isEmpty()) {
							String listedArenas = ConfigLoader.getDefault("Tag.Strings.AreNoArenas");
							sender.sendMessage(ChatColor.GOLD
									+ listedArenas);
						} else {
							String listedArenas = ConfigLoader.getDefault("Tag.Strings.ArenasList");
							sender.sendMessage(ChatColor.GOLD
									+ String.format(listedArenas, GameCenter.availableArenas()
											.stream()
											.map(GeneralMethods::toProperCase)
											.collect(Collectors.joining(", "))));
						}	
					}
					break;
				case "status":
					if(!sender.hasPermission("tag.status") && !(sender instanceof ConsoleCommandSender))
						notEnoughPermission(sender);
					else {
						if(args.length == 2) {
							if(GameCenter.getArena(args[1]) != null) {
								TagArena arena = GameCenter.getArena(args[1]);
								
								// import FancifulText and use the methods to display a json popup of the joined players.
								new FancyMessage(String.format(
										"%s%s%s%s%s%s",
										"===============================\n",
										"  Tag-Minigame " + GeneralMethods.toProperCase(args[1]) + ": \n",
										"===============================\n",
										"  Players joined: " + arena.getJoinedPlayers().size() + "/" + arena.getMaxPlayers() + "\n",
										"  Status: " + (arena.isActive() ? ChatColor.RED + "ACTIVE\n" : ChatColor.GREEN + "OPEN\n"),
										"===============================\n"
										))
									.color(ChatColor.AQUA)
									.tooltip(getJoinedPlayers(arena))
									.send(sender);
								
								/*
								sender.sendMessage(ChatColor.AQUA 
										+ String.format(
										"%s%s%s%s%s%s",
										"===============================\n",
										"  Tag-Minigame " + GeneralMethods.toProperCase(args[1]) + ": \n",
										"===============================\n",
										"  Players joined: " + arena.getJoinedPlayers().size() + "/" + arena.getMaxPlayers() + "\n",
										"  Status: " + (arena.isActive() ? ChatColor.RED + "ACTIVE\n" : ChatColor.GREEN + "OPEN\n"),
										ChatColor.AQUA + "===============================\n"
										));
								*/
							} else {
								String maxPlayers = ConfigLoader.getDefault("Tag.Arena." + args[1] + ".MaxPlayers");
								
								sender.sendMessage(ChatColor.AQUA
										+ "===============================\n"
										+ "  Tag-Minigame " + GeneralMethods.toProperCase(args[1]) + ": \n"
										+ "===============================\n"
										+ "  Players joined: 0/" + maxPlayers + "\n"
										+ "  Status: " + ChatColor.GREEN + "OPEN\n" 
										+ ChatColor.AQUA + "===============================\n"
										);
								
							}
							
						}
						else 
							sender.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.StatusSyntax"));
					}
					break;
			}
		}	
	}
	
	private static String getJoinedPlayers(TagArena arena) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("Joined players: \n");
		
		arena.getJoinedPlayers()
			.stream()
			.map(player -> player.getName())
			.sorted()
			.forEach(player -> {
				sBuilder.append("- " + player + "\n");
			});
		
		return sBuilder.toString();
	}
	
	
	private static void setArena(String path, CommandSender sender) {
		Player player = (Player) sender;
		Location location = (player).getLocation();
		
		ConfigLoader.set(
				path,
				String.format("%s_%s,%s,%s",
						(player).getWorld().getName(),
						location.getBlockX(),
						location.getBlockY(),
						location.getBlockZ())
				);
		sender.sendMessage(ChatColor.GOLD
				+ ConfigLoader.getDefault("Tag.Strings.ArenaAdded"));
	}
	
	private static void handleJoin(Player player, TagArena arena) {
		if(arena.addPlayer(player)) {
			if(player.getGameMode() != GameMode.SURVIVAL) {
				player.sendMessage(ChatColor.GOLD
						+ ConfigLoader.getDefault("Tag.Strings.PlayerChangeGameMode"));
				player.setGameMode(GameMode.SURVIVAL);
			}
			
			MethodBypass.legalWarp(ConfigLoader.getDefault(
					"Tag.Arena." + arena.getArena() + ".Warps.Lobby"),
					player,
					GameCenter.getArena(player).getArena());
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
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			// Add implementation of TextComponent
		}
		
		sender.sendMessage(ChatColor.AQUA 
				+ String.format(
				"%s%s%s%s%s%s%s%s",
				"===============================\n",
				"  Tag-Minigame Menu: \n",
				"===============================\n",
				"  /tag join [arena name]\n Join the tag lobby.\n",
				"  /tag start [arena name]\n Force start the tag minigame.\n",
				"  /tag leave  Exit from a running game of tag.\n",
				"  /tag help  Learn how to play the game.\n",
				"===============================\n"
				));
	}
	
	private static void displayHelpMenu(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA 
				+ String.format(
				"%s%s%s%s%s%s",
				"===============================\n",
				"  Tag-Minigame Help: \n",
				"===============================\n",
				"  Play Tag with others on the server.\n",
				"  Tag them and be the last player standing to receive a reward.\n",
				"===============================\n"
				));
	}
	
	private static void displayLeaderboard(CommandSender sender, String top) {
		int defaultSize = DEFAULT_TOP;
		
		if(!GeneralMethods.isInteger(top))
			sender.sendMessage(ChatColor.GOLD
					+ "Invalid number! Using default: " + DEFAULT_TOP);
		else 
			defaultSize = Integer.parseInt(top);

		if(!LeaderboardManager.getLeaderboardTop(defaultSize).isPresent()) {
			Bukkit.getServer().getLogger().warning("[Tag] leaderboard.yml was not detected!");
			return;
		}
		
		List<List<String[]>> leaderboard = LeaderboardManager.getLeaderboardTop(defaultSize).get();
		StringBuilder sBuilder = new StringBuilder();
		int count = 1;
		
		sBuilder.append(ChatColor.AQUA + "===============================\n");
		sBuilder.append(ChatColor.AQUA + "  Tag-Minigame Leaderboard: \n");
		sBuilder.append(ChatColor.AQUA + "===============================\n");
		for(List<String[]> stats : leaderboard) {
			if(count > defaultSize)
				break;
			
			for(String[] currentStat : stats) {
				String player = currentStat[0];
				int[] wins = LeaderboardManager.stringToIntArray(currentStat[1].split("_"));
				
				sBuilder.append(String.format(ChatColor.AQUA + "  [%d]: %s - %s %s\n", 
						count,
						player, 
						ChatColor.GREEN + "Wins: " + wins[0],
						ChatColor.RED + "Losses: " + wins[1]));
				count++;
			}
		}
		sBuilder.append(ChatColor.AQUA + "===============================\n");
		
		sender.sendMessage(sBuilder.toString());
	}
}
