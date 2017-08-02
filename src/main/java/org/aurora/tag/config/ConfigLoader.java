package org.aurora.tag.config;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.aurora.tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 
 * @author RussianMushroom
 *
 */
public class ConfigLoader {
	
	private static Map<String, String> backup = Collections.synchronizedMap(
			new HashMap<String, String>());
	
	public static void load(Tag tag) {
		Tag plugin = tag;
		
		FileConfiguration fConfig = plugin.getConfig();
		
		setConfigAndBackup(fConfig, "Tag.MaxPlayers", Integer.valueOf(10));
		
		// Default arenas
		setConfigAndBackup(fConfig, "Tag.Arena.Lobby", "taglobby");
		setConfigAndBackup(fConfig, "Tag.Arena.Arena", "tagarena");
		setConfigAndBackup(fConfig, "Tag.Arena.Rip", "tagrip");
		                        
		// Time limits          
		setConfigAndBackup(fConfig, "Tag.Timer.TicksBeforeTagStart", Integer.valueOf(140));
		setConfigAndBackup(fConfig, "Tag.Timer.TicksBeforeUpgrade", Integer.valueOf(400));
		setConfigAndBackup(fConfig, "Tag.Timer.GracePeriod", Integer.valueOf(3000));
		
		// Allowed weapons    
		setConfigAndBackup(fConfig, "Tag.Tools.Baton", "STICK");
		setConfigAndBackup(fConfig, "Tag.Tools.BatonName", "Baton");
		setConfigAndBackup(fConfig, "Tag.Tools.ArrowCount", Integer.valueOf(1));
		
		// Sign text
		setConfigAndBackup(fConfig, "Tag.Sign.SignToVote", "[VOTE]");
		setConfigAndBackup(fConfig, "Tag.Sign.SignToBow", "[UPGRADE]");
		
		// Externalise strings
		setConfigAndBackup(fConfig, "Tag.Strings.NotifyPlayersGameStart", "This game of Tag is starting in %d seconds!");
		setConfigAndBackup(fConfig, "Tag.Strings.LastPersonStanding", "Congratulations! You are the last person standing!");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerTagPlayer", "Congratulations! You have tagged %s, they will be warped away. You are one step closer to winning!");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerTaggedByPlayer", "You have been tagged by %s. That means it is gameover for you. Better luck next time!");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerVote", "You have successfully voted.");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerAlreadyVote", "You have already voted. Please wait for the others to vote before the game starts!");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerGetBow", "With great power, comes great responsibility!");
		setConfigAndBackup(fConfig, "Tag.Strings.BowNotReady", "Be patient, it will be ready soon!");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerCannotTeleport", "You cannot teleport while you are in a Tag game. To leave this game, use /tag leave.");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerCannotChangeGameMode", "You cannot change your game mode while you are in a Tag game. To leave this game, use /tag leave.");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerHasLeft", "%s has left the game.");
		setConfigAndBackup(fConfig, "Tag.Strings.GameStart", "[Tag] A new game of Tag has been started. Please wait for this game to end before joining a new one.");
		setConfigAndBackup(fConfig, "Tag.Strings.GameStop", "[Tag] This game of Tag has ended! You may now join a new game.");
		setConfigAndBackup(fConfig, "Tag.Strings.BroadcastWinner", "[Tag] %s has defeated all his foes to win this game of Tag!");
		setConfigAndBackup(fConfig, "Tag.Strings.ConsoleUser", "This command cannot be used from the console!");
		setConfigAndBackup(fConfig, "Tag.Strings.AlreadyInLobby", "You are already in the Lobby, please vote to start the game!");
		setConfigAndBackup(fConfig, "Tag.Strings.AlreadyInActiveGame", "You are already in an active game of Tag. To leave this game, use /tag leave.");
		setConfigAndBackup(fConfig, "Tag.Strings.AlreadyActiveGameWait", "There is already a game of Tag running. Please wait for it to end before joining a new game.");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerClearInventory", "Please clear your inventory. Upon clearing it, use /tag join confirm.");
		setConfigAndBackup(fConfig, "Tag.Strings.AlreadyActive", "There is already a game of Tag running.");
		setConfigAndBackup(fConfig, "Tag.Strings.NotActive", "There is no game of Tag running.");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerChangeGameMode", "Changing your game mode to survival.");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerWarpLobby", "You have been warped to the Lobby.");
		setConfigAndBackup(fConfig, "Tag.Strings.GameIsFull", "This current game of Tag is already full. Please wait until it ends before rejoining.");
		setConfigAndBackup(fConfig, "Tag.Strings.NoPerm", "You do not have the proper permissions to execute this command!");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerNotInGame", "You are not in a Tag game.");
		setConfigAndBackup(fConfig, "Tag.Strings.GetUpgrade", "Upgrades are now active!");
		setConfigAndBackup(fConfig, "Tag.Strings.PlayerLeaves", "You have left the Tag game. You will be warped back to your last known location.");
		setConfigAndBackup(fConfig, "Tag.Strings.MinPlayers", "There need to be at least two players to start a game fo Tag.");
		setConfigAndBackup(fConfig, "Tag.Strings.Grace", "You now have %d seconds to get into position. Good luck!");
		setConfigAndBackup(fConfig, "Tag.Strings.NoGrace", "The grace period is over!");
		
		
		try {
			File configFile = new File(ConfigFile.getConfigPath() + File.separator + ConfigFile.CONFIG);
			if(!configFile.exists())
				fConfig.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDefault(String path) {		
		FileConfiguration fConfig = Bukkit.getServer().getPluginManager()
				.getPlugin("Tag")
				.getConfig();
	
		try {
			fConfig.load(ConfigFile.getConfigFile());
			
			return fConfig.contains(path) ? fConfig.getString(path) : backup.get(path); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		return backup.get(path);
	}
	
	// Add a backup of the values and save it to memory in case the config file cannot be found.
	private static void setConfigAndBackup(FileConfiguration fConfig, String path, Object value) {
		fConfig.set(path, value);
		backup.put(path, value.toString());
	}
	
}