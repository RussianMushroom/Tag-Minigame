package org.aurora.tag;

import java.util.ArrayList;
import java.util.List;

import org.aurora.tag.command.TagCommand;
import org.aurora.tag.command.TagTabCompletion;
import org.aurora.tag.config.ArenaConfig;
import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.listener.CommandListener;
import org.aurora.tag.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author RussianMushroom
 */
public class Tag extends JavaPlugin {
	
	@Override
	public void onEnable() {
		// Create config file
		ConfigLoader.load(this);
		ArenaConfig.load();
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new CommandListener(), this);

		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		GameCenter.stopAll();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("tag"))
			TagCommand.handle(sender, args, this);
		
		return super.onCommand(sender, command, label, args);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(command.getName().equalsIgnoreCase("tag"))
			return TagTabCompletion.complete(sender, args);
		else
			return new ArrayList<>();
	}

	public static Plugin getPlugin() {
		return Bukkit.getPluginManager().getPlugin("Tag");
	}
}