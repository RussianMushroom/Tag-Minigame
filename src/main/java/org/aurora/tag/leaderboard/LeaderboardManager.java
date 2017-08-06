package org.aurora.tag.leaderboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.aurora.tag.config.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LeaderboardManager {

	private static FileConfiguration fConfig = Bukkit.getPluginManager().getPlugin("Tag").getConfig();
	
	// FIXME Doesn't exclusively save leaderboard.
	public static void add(Player player, boolean won) {
		int[] playerWin;
		
		if(getPlayerStat(player).isPresent()) {
			playerWin = getPlayerStat(player).get();
			
			if(won)
				playerWin[0]++;
			else 
				playerWin[1]++;
		} else
			playerWin = won ? new int[] {1, 0} :  new int[] {0, 1};
			
			// Save
			save(player, playerWin[0] + "_" + playerWin[1]);
	}
	
	public static Optional<List<String>> getLeaderboardTop() {
		try {
			fConfig.load(ConfigFile.getLeaderboardFile());
			
			if(!fConfig.contains("Leaderboard")) {
				return Optional.empty();
			} else {	
				List<String> leaderboard = new ArrayList<>();
				
				for(String key : fConfig.getConfigurationSection("Leaderboard").getKeys(false)) {
					leaderboard.add(key + "~" +  fConfig.getString("Leaderboard." + key).split("_"));
				}
				
				leaderboard = leaderboard.stream()
					.sorted(LeaderboardManager::sortStats)
					.collect(Collectors.toList());
										
				return Optional.of(leaderboard);
			}
		} catch (IOException | InvalidConfigurationException e) {
			return Optional.empty();
		}
	}
	
	private static void save(Player player, String win) {
		try {
			fConfig.set("Leaderboard." + player.getName(), win);
			fConfig.save(ConfigFile.getLeaderboardFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Optional<int[]> getPlayerStat(Player player) {
		try {
			fConfig.load(ConfigFile.getLeaderboardFile());
			return fConfig.contains("Leaderboard." + player.getName()) ? 
					Optional.of(stringToIntArray(fConfig.getString("Leaderboard." + player.getName()).split("_")))
					: Optional.empty();
		} catch (IOException | InvalidConfigurationException e) {
			return Optional.empty();
		}
	}
	
	public static int[] stringToIntArray(String[] stringArray) {
		return Arrays.asList(stringArray)
				.stream()
				.mapToInt(Integer::parseInt)
				.toArray();
	}
	
	private static String[] getStats(String stats) {
		return stats.split("~")[1].split("_");
	}
	
	private static int sortStats(String current, String next) {
		Bukkit.broadcastMessage(current);
		int[] currentArray = stringToIntArray(getStats(current)),
				nextArray = stringToIntArray(getStats(next));
		
		return Integer.compare(
				(currentArray[0] - currentArray[1]),
				(nextArray[0] - nextArray[1]));
	}
}
