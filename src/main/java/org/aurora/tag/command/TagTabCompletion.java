package org.aurora.tag.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aurora.tag.game.GameCenter;
import org.aurora.tag.util.GeneralMethods;
import org.bukkit.command.CommandSender;

/**
 * @author RussianMushroom
 *
 */
public class TagTabCompletion {
	
	private static final List<String> COMMAND_LIST = Arrays.asList("start", "join", "j", "stop", "status");

	/**
	 * Only have tab completion for the following commands: 
	 * "/tag start [arena name]"
	 * "/tag join [arena name]"
	 * "/tag stop [arena name]"
	 * "/tag status [arena name]"
	 * @param sender
	 * @param alias
	 * @param args
	 * @return
	 */
	public static List<String> complete(CommandSender sender, String[] args) {
		List<String> arenas = new ArrayList<>();
		if(args.length == 2) {
			if(COMMAND_LIST.contains(args[0])) {
				GameCenter.availableArenas().forEach(arenaName -> {
					if(arenaName.startsWith(args[1].toLowerCase()))
						arenas.add(GeneralMethods.toProperCase(arenaName));
				});
			}
		}
		
		return arenas;
	}
	
}
