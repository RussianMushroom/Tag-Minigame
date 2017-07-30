package org.aurora.tag.listener;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.util.InventoryManager;
import org.aurora.tag.util.Timer;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
	{
		
		if (event.getRightClicked().getType() == EntityType.PLAYER)
		{
		Player player = (Player) event.getRightClicked();
		player.performCommand("warp " + ConfigLoader.getDefault("Tag.Arena.Rip"));
	    }
	}      
	
	// Listen to player hitting signs
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block clickedBlock;
		
		if(event.hasBlock()) {
			clickedBlock = event.getClickedBlock();
			
			if(TagManager.getJoinedPlayers().contains(player))
				if(clickedBlock.getState() instanceof Sign) {
					Sign sign = (Sign) clickedBlock.getState();
					
					// Player clicked sign to vote
					if(sign.getLine(0).contains(ConfigLoader.getDefault("Tag.Sign.SignToVote")) 
							&& !TagManager.isActive()) {
						if(TagManager.vote(player)) {
							player.sendMessage(ChatColor.GOLD
											+ "You have successfully voted.");
							TagManager.checkStartTag();
						}
						else
							player.sendMessage(ChatColor.GOLD
									+ "You have already voted. Please wait for the others to vote before the game starts!");
					}
					// Player clicked sign to get bow
					else if (sign.getLine(0).contains(ConfigLoader.getDefault("Tag.Sign.SignToBow"))
							&& TagManager.isActive()
							&& TagManager.getVotedPlayers().contains(player)) {
						if(Timer.bowIsActive()) {
							Timer.startBowTimer();
							player.sendMessage(ChatColor.GOLD
									+ "With great power, comes great responsibility!");
							InventoryManager.setTagBow(player);
						}
						else
							player.sendMessage(ChatColor.GOLD
									+ "Be patient, it will be ready soon!");
						
					}
				}
		}
		
		
	}
}