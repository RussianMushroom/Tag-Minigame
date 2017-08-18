package org.aurora.tag.leaderboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.aurora.tag.config.ConfigFile;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class LeaderboardManager {
	
	private static YamlConfiguration yConfig = getYamlConfig();
	
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
	
	public static Optional<List<String[]>> getLeaderboardTop(int maxSize) {
		try {
			yConfig.load(ConfigFile.getLeaderboardFile());
			
			if(!yConfig.contains("Leaderboard")) {
				return Optional.empty();
			} else {	
				Map<List<Integer>, String[]> leaderboard = new HashMap<>();
				
				 yConfig.getConfigurationSection("Leaderboard").getKeys(false).forEach(key -> {
					 
					 int[] playerScore = stringToIntArray(yConfig.getString("Leaderboard." + key).split("_"));
					 String[] scoreArray = new String[] {key, yConfig.getString("Leaderboard." + key)};
					 
					 leaderboard.put(Arrays.asList(playerScore[0], playerScore[1]), scoreArray);
				 });
				 
				 if(leaderboard.size() < maxSize)
					 maxSize = leaderboard.size();
				 
				 return Optional.of(leaderboard.keySet()
						 .stream()
						 .sorted(Comparator.comparing((List<Integer> x) -> x.get(0)).reversed())
						 .map(x -> leaderboard.get(x))
						 .limit((long) maxSize)
						 .collect(Collectors.toList()));
			}
		} catch (IOException | InvalidConfigurationException e) {
			return Optional.empty();
		}
	}
	
	private static void save(Player player, String win) {
		try {
			yConfig.set("Leaderboard." + player.getName(), win);
			yConfig.save(ConfigFile.getLeaderboardFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Optional<int[]> getPlayerStat(Player player) {
		try {
			yConfig.load(ConfigFile.getLeaderboardFile());
			return yConfig.contains("Leaderboard." + player.getName()) ? 
					Optional.of(stringToIntArray(yConfig.getString("Leaderboard." + player.getName()).split("_")))
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
	
	private static YamlConfiguration getYamlConfig() {
		return new YamlConfiguration();
	}
}
