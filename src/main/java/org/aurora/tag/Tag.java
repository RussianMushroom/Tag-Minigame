package org.aurora.tag;

import org.aurora.tag.command.TagCommand;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.listener.TagListener;
import org.bukkit.Bukkit;
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
		// Create config file
		ConfigLoader.load(this);
		getServer().getPluginManager().registerEvents(new TagListener(), this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("tag"))
			TagCommand.handle(sender, args, this);
		
		return super.onCommand(sender, command, label, args);
	}

	
}

