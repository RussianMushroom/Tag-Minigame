package org.aurora.tag.listener;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.util.InventoryManager;
import org.aurora.tag.util.Timer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import org.bukkit.event.entity.EntityDeathEvent;
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
			
			if(TagManager.getVotedPlayers().contains(tagger)
					&& TagManager.getVotedPlayers().contains(tagged)
					&& !TagManager.getRipPlayers().contains(tagger)
					&& !TagManager.getRipPlayers().contains(tagged)
					&& TagManager.isActive()
					&& !Timer.isGrace()) {
				// Check if tagger used stick
				if(tagger.getInventory().getItemInMainHand().getType() == Material.STICK) {
					tagged.playSound(tagged.getLocation(), Sound.ENTITY_GHAST_SHOOT, 10, 1);
					registerHit(tagger, tagged);
				}
			}
			// If tagger used bow
		} else if(attacker instanceof Arrow && attacked instanceof Player) {
			if(((Arrow) attacker).getShooter() instanceof Player) {
				Player tagger = (Player) ((Arrow) attacker).getShooter();
				Player tagged = (Player) attacked;
				
				if(TagManager.getVotedPlayers().contains(tagger)
						&& TagManager.getVotedPlayers().contains(tagged)
						&& !TagManager.getRipPlayers().contains(tagger)
						&& !TagManager.getRipPlayers().contains(tagged)
						&& TagManager.isActive()
						&& !Timer.isGrace())
					registerHit(tagger, tagged);
			}
		}
	}      
	
	private static void registerHit(Player tagger, Player tagged) {
		TagManager.addRip(tagged);
		tagged.sendMessage(ChatColor.GOLD
				+ String.format(ConfigLoader.getDefault("Tag.Strings.PlayerTaggedByPlayer"), 
						tagger.getName()));
		TagManager.legalWarp(ConfigLoader.getDefault("Tag.Arena.Rip"), tagged);
		
		// Add check to see if everyone has been tagged
		if(TagManager.isLastPersonStanding(tagger)) {
			tagger.sendMessage(ChatColor.GOLD
					+ String.format(ConfigLoader.getDefault("Tag.Strings.LastPersonStanding"), 
							tagged.getName()));
			GameCenter.registerWinner(tagger);
		}
		else
			tagger.sendMessage(ChatColor.GOLD
					+ String.format(ConfigLoader.getDefault("Tag.Strings.PlayerTagPlayer"), 
							tagged.getName()));
	}
	
	// Listen for player hitting signs
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block clickedBlock;
		
		if(event.hasBlock()) {
			clickedBlock = event.getClickedBlock();
			
			if(TagManager.getJoinedPlayers().contains(player)) {
				if(clickedBlock.getState() instanceof Sign) {
					Sign sign = (Sign) clickedBlock.getState();
					
					// Player clicked sign to vote
					if(sign.getLine(0).contains(ConfigLoader.getDefault("Tag.Sign.SignToVote")) 
							&& !TagManager.isActive()) {
						if(TagManager.vote(player)) {
							player.sendMessage(ChatColor.GOLD
											+ ConfigLoader.getDefault("Tag.Strings.PlayerVote"));
							TagManager.checkStartTag();
						}
						else
							player.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.PlayerAlreadyVote"));
					}
					// Player clicked sign to get an upgrade
					else if (sign.getLine(0).contains(ConfigLoader.getDefault("Tag.Sign.SignToUpgrade"))
							&& TagManager.isActive()
							&& TagManager.getVotedPlayers().contains(player)
							&& !Timer.isGrace()) {
						if(Timer.upgradeIsActive()) {
							Timer.startUpgradeTimer();
							player.sendMessage(ChatColor.GOLD
									+ ConfigLoader.getDefault("Tag.Strings.PlayerGetUpgrade"));
							InventoryManager.setUpgrade(player);
						}
						else
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
		if(TagManager.getJoinedPlayers().contains(event.getPlayer())) {
			if(!TagManager.canWarp()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GOLD
						+ ConfigLoader.getDefault("Tag.Strings.PlayerCannotTeleport"));
			}
			TagManager.prohibitWarp();
		}
	}
	
	// Disable game mode-switching while in the event
	@EventHandler
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		if(TagManager.getJoinedPlayers().contains(event.getPlayer())) {
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
		if(TagManager.getJoinedPlayers().contains(event.getPlayer()))
			TagManager.removePlayer(event.getPlayer());
	}
	
	// Die (Special case. Here they are just sent to tagrip to wait for the game to end.)
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			if(TagManager.getJoinedPlayers().contains(player)) {
				TagManager.addRip(player); 
				TagManager.legalWarp(ConfigLoader.getDefault("Tag.Arena.Rip"), player);
			}
		}
	}
	
	// Leave
	@EventHandler 
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if(TagManager.getJoinedPlayers().contains(event.getPlayer()))
			TagManager.removePlayer(event.getPlayer());
	}
	
	// Disable flight
	@EventHandler
	public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
		if(TagManager.getJoinedPlayers().contains(event.getPlayer()))
			event.setCancelled(true);
	}
	
	
}