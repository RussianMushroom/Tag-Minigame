package org.aurora.tag.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class taglistener 
{
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
	{
		EntityType eType = event.getType();
		if (eType.equals(PLAYER))
		{
		getName.performCommand("warp tagrip");//@config
	    }
	}      
}