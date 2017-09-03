package org.aurora.tag.listener;

import java.util.Optional;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.game.TagArena;
import org.aurora.tag.leaderboard.LeaderboardManager;
import org.aurora.tag.util.InventoryManager;
import org.aurora.tag.util.MethodBypass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerListener implements Listener {
	
	// Listen for player hitting others or players being hit by arrows
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Entity attacker = event.getDamager();
		Entity attacked = event.getEntity();
		
		if(attacker instanceof Player && attacked instanceof Player) {
			Player tagger = (Player) attacker;
			Player tagged = (Player) attacked;

			registerHit(tagger, tagged);
			
			// If tagger used bow
		} else if(attacker instanceof Arrow && attacked instanceof Player) {
			if(((Arrow) attacker).getShooter() instanceof Player) {
				Player tagger = (Player) ((Arrow) attacker).getShooter();
				Player tagged = (Player) attacked;
				
				registerHit(tagger, tagged);
			}
		}
	}      
	
	private static void registerHit(Player tagger, Player tagged) {

		if(GameCenter.arenaContainsPlayerAsType("joined", tagger)
				&& GameCenter.arenaContainsPlayerAsType("joined", tagged)) {
			TagArena taggerArena = GameCenter.getArena(tagger);
			TagArena taggedArena = GameCenter.getArena(tagged);
			
			if(taggerArena.getArena() == taggedArena.getArena()) {
				if(GameCenter.arenaContainsPlayerAsType("voted", tagger)
						&& GameCenter.arenaContainsPlayerAsType("voted", tagged)
						&& !GameCenter.arenaContainsPlayerAsType("rip", tagger)
						&& !GameCenter.arenaContainsPlayerAsType("rip", tagged)
						&& taggerArena.isActive()
						&& !taggerArena.isGrace()
						&& tagger != tagged) {
					
					
					// Check if tagger used stick or bow
					if(tagger.getInventory().getItemInMainHand().getType() == Material.STICK
							|| tagger.getInventory().getItemInMainHand().getType() == Material.BOW) {
						tagged.playSound(tagged.getLocation(), Sound.ENTITY_GHAST_SHOOT, 10, 1);
						
						taggerArena.addRip(tagged);
						LeaderboardManager.add(tagged, false);
						tagged.sendMessage(ChatColor.GOLD
								+ String.format(ConfigLoader.getDefault("Tag.Strings.PlayerTaggedByPlayer"), 
										tagger.getName()));
						MethodBypass.legalWarp(ConfigLoader.getDefault(
								"Tag.Arena." + GameCenter.getArena(tagged).getArena() + ".Warps.Rip"), tagged, taggedArena.getArena());
						
						// Add check to see if everyone has been tagged
						if(taggerArena.isLastPersonStanding(tagger)) {
							tagger.sendMessage(ChatColor.GOLD
									+ String.format(ConfigLoader.getDefault("Tag.Strings.LastPersonStanding"), 
											tagged.getName()));
							GameCenter.registerWinner(tagger, taggerArena.getArena());
						} else
							tagger.sendMessage(ChatColor.GOLD
									+ String.format(ConfigLoader.getDefault("Tag.Strings.PlayerTagPlayer"), 
											tagged.getName()));
					}
				}
			} else {
				// Tell players that they are not in the same arena
			}
		}
		
	}
	
	// If the player is getting hurt by something, and they are in a Tag game
	@EventHandler
	public void OnEntityDamageEvent(EntityDamageEvent event) {
		Entity attacked = event.getEntity();
		
		if(attacked instanceof Player) {
			if(GameCenter.arenaContainsPlayerAsType("voted", (Player) attacked)
					&& !GameCenter.arenaContainsPlayerAsType("rip", (Player) attacked)) {
				Player player = (Player) attacked;
				if(player.getHealth() - event.getDamage() < 1) {
					event.setCancelled(true);
					MethodBypass.legalWarp(ConfigLoader.getDefault(
							"Tag.Arena." + GameCenter.getArena(player).getArena() + ".Warps.Rip"),
							player,
							GameCenter.getArena(player).getArena());
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
							"heal " + player.getName());
					player.sendMessage(ChatColor.GOLD
							+ ConfigLoader.getDefault("Tag.Strings.PlayerDiesInArena"));

					GameCenter.getArena(player).addRip(player);
				}
			}
		}
	}
	
	// Listen for player hitting signs
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block clickedBlock;
		TagArena arena;
		
		if(event.hasBlock()) {
			clickedBlock = event.getClickedBlock();
			
			
			if(clickedBlock.getState() instanceof Sign) {
				Sign sign = (Sign) clickedBlock.getState();
				
				if(sign.getLine(0).contains(ConfigLoader.getDefault("Tag.Sign.SignToJoin"))) {
					if(!sign.getLine(1).equals("")) {
						event.getPlayer().performCommand("tag join " + sign.getLine(1).toLowerCase());
					}
				} else if(sign.getLine(0).contains(ConfigLoader.getDefault("Tag.Sign.SignToLeave"))) {
					event.getPlayer().performCommand("tag leave");
				}
				
				if(GameCenter.arenaContainsPlayerAsType("joined", event.getPlayer())) {
					arena = GameCenter.getArena(event.getPlayer());
					
						// Player clicked sign to vote
						if(sign.getLine(0).contains(ConfigLoader.getDefault("Tag.Sign.SignToVote")) 
								&& !arena.isActive()) {
							if(arena.vote(player)) {
								player.sendMessage(ChatColor.GOLD
												+ ConfigLoader.getDefault("Tag.Strings.PlayerVote"));
								arena.checkStartTag();
							} else
								player.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.PlayerAlreadyVote"));
						}
						// Player clicked sign to get an upgrade
						else if (sign.getLine(0).contains(ConfigLoader.getDefault("Tag.Sign.SignToUpgrade"))
								&& arena.isActive()
								&& arena.getVotedPlayers().contains(player)
								&& !arena.isGrace()) {
							if(arena.upgradeIsActive()) {
								arena.startUpgradeTimer();
								player.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.PlayerGetUpgrade"));
								InventoryManager.setUpgrade(player);
							} else
								player.sendMessage(ChatColor.GOLD
										+ ConfigLoader.getDefault("Tag.Strings.UpgradeNotReady"));
						}
					}
				}
		}
	}
	
	// Disable teleporting for players in the event
	@EventHandler
	public void OnPlayerTeleportEvent(PlayerTeleportEvent event) {
		if(GameCenter.arenaContainsPlayerAsType("joined", event.getPlayer())) {
			if(!GameCenter.getArena(event.getPlayer()).canWarp()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GOLD
						+ ConfigLoader.getDefault("Tag.Strings.PlayerCannotTeleport"));
			}
			GameCenter.getArena(event.getPlayer()).prohibitWarp();
		} else if(isLocOfPlayer(event.getTo()).isPresent()) {
			Player tpToPlayer = isLocOfPlayer(event.getTo()).get();
			
			if(GameCenter.arenaContainsPlayerAsType("joined", tpToPlayer) && !event.getPlayer().isOp()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GOLD
						+ ConfigLoader.getDefault("Tag.Strings.TpToTag"));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Optional<Player> isLocOfPlayer(Location to) {
		return (Optional<Player>) Bukkit.getServer().getOnlinePlayers()
				.parallelStream()
				.filter(targetPlayer -> targetPlayer.getLocation().equals(to))
				.findFirst();
	}

	// Prevent players from removing armour
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if(event.getSlotType() == InventoryType.SlotType.ARMOR
				&& event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if(GameCenter.arenaContainsPlayerAsType("joined", player))
				event.setCancelled(true);
		}
			
	}
	
	// Disable game mode-switching while in the event
	@EventHandler
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		if(GameCenter.arenaContainsPlayerAsType("joined", event.getPlayer())) {
			if(event.getNewGameMode() != GameMode.SURVIVAL) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GOLD
						+ ConfigLoader.getDefault("Tag.Strings.PlayerCannotChangeGameMode"));
			}
		}
			
	}
	
	// Remove player if they are either kicked, die or leave the game
	// Kick
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event) {
		if(GameCenter.arenaContainsPlayerAsType("joined", event.getPlayer()))
			GameCenter.getArena(event.getPlayer()).removePlayer(event.getPlayer());
	}
	
	// Die
    // Checking the damage. See event EntityDamageByEntityEvent
	
	// Leave
	@EventHandler 
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if(GameCenter.arenaContainsPlayerAsType("joined", event.getPlayer()))
			GameCenter.getArena(event.getPlayer()).removePlayer(event.getPlayer());
	}
	
	// Disable flight
	@EventHandler
	public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
		if(GameCenter.arenaContainsPlayerAsType("joined", event.getPlayer()))
			event.setCancelled(true);
	}
	
	
}