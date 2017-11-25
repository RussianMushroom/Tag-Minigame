package org.aurora.tag.scoreboard;

import org.aurora.tag.game.TagArena;
import org.aurora.tag.util.GeneralMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/**
 * 
 * @author RussianMushroom
 *
 */
public class CreateScoreboard {
	
	public static void updateScoreboard(Player player, TagArena arena) {
		sendScoreboard(player, arena);
	}
	
	public static void updateScoreboard(TagArena arena) {
		arena.getJoinedPlayers().forEach(player -> {
			sendScoreboard(player, arena);
		});
	}
	
	public static void removeScoreboardFromArena(TagArena arena) {
		arena.getJoinedPlayers().forEach(player -> player.setScoreboard(
				Bukkit.getScoreboardManager().getNewScoreboard()));
	}
	
	public static void removeScoreboardFromPlayer(Player player) {
		player.setScoreboard(
				Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	private static void sendScoreboard(Player player, TagArena arena) {
		ScoreboardManager sbManager = Bukkit.getScoreboardManager();
		Scoreboard sBoard = sbManager.getNewScoreboard();
		Objective obj = sBoard.registerNewObjective(
				"§6§n§l" + (arena.getArena().length() == 16 
				? GeneralMethods.toProperCase(arena.getArena().substring(0, 15))
				: GeneralMethods.toProperCase(arena.getArena())),
				"");
		int count = 1;
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		obj.getScore("1. §2§n§lActive:").setScore(0);
		
		for(Player vPlayer : arena.getVotedPlayers()) {
			if(!arena.getRipPlayers().contains(vPlayer)) {
				obj.getScore("1." + count + " " + vPlayer.getName()).setScore(0);
		 		count++;
			}
		}
		
		count = 1;
		obj.getScore("2. §4§n§lTagged:").setScore(0);
		
		for(Player rPlayer : arena.getRipPlayers()) {
			obj.getScore("2." + count + " " + rPlayer.getName()).setScore(0);
	 		count++;
		}
		
		player.setScoreboard(sBoard);
	}
}
