package org.aurora.tag.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TagListener implements Listener 
{
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
	{
		
		if (event.getRightClicked().getType() == EntityType.PLAYER)
		{
		Player player = (Player) event.getRightClicked();
		player.performCommand("warp whereever");
	    }
	}      
}