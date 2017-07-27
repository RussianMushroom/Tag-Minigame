package org.aurora.tag;

import org.aurora.tag.command.TagCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author RussianMushroom
 *
 */
public class Tag extends JavaPlugin {
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("tag"))
			TagCommand.handle(sender, args);
		return super.onCommand(sender, command, label, args);
	}
	
}

