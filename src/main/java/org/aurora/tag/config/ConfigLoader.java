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
  
  private static FileConfiguration fConfig;
  
  private static Map<String, String> backup = Collections.synchronizedMap(
      new HashMap<String, String>());
  
  public static void load(Tag tag) {
    Tag plugin = tag;
    fConfig = plugin.getConfig();
    
    // Default arenas
    // These are set by /tag set arena|rip|lobby - check if they exists upon enable
    // setConfigAndBackup(fConfig, "Tag.Arena.Lobby", "");
    // setConfigAndBackup(fConfig, "Tag.Arena.Arena", "");
    // setConfigAndBackup(fConfig, "Tag.Arena.Rip", "");
                            
    // Time limits          
    setConfigAndBackup(fConfig, "Tag.Timer.TicksBeforeTagStart", Integer.valueOf(140));
    setConfigAndBackup(fConfig, "Tag.Timer.TicksBeforeUpgrade", Integer.valueOf(400));
    setConfigAndBackup(fConfig, "Tag.Timer.GracePeriod", Integer.valueOf(1000));
    
    // Minigame world name
    setConfigAndBackup(fConfig, "Tag.Minigame.WorldName", "");
    
    // Allowed weapons    
    setConfigAndBackup(fConfig, "Tag.Tools.Baton", "STICK");
    setConfigAndBackup(fConfig, "Tag.Tools.BlindPotionName", "I can't see a thing.");
    setConfigAndBackup(fConfig, "Tag.Tools.SpeedPotionName", "I've got speed!");
    setConfigAndBackup(fConfig, "Tag.Tools.JumpPotionName", "How low can you go?");
    setConfigAndBackup(fConfig, "Tag.Tools.BatonName", "Baton");
    setConfigAndBackup(fConfig, "Tag.Tools.ArrowCount", Integer.valueOf(1));
    setConfigAndBackup(fConfig, "Tag.Tools.ChanceToGetPumpkin", Double.valueOf(0.1));
    setConfigAndBackup(fConfig, "Tag.Tools.ChanceToGetEasterEgg", Double.valueOf(0.05));
    
    // Sign text
    setConfigAndBackup(fConfig, "Tag.Sign.SignToVote", "[VOTE]");
    setConfigAndBackup(fConfig, "Tag.Sign.SignToUpgrade", "[UPGRADE]");
    setConfigAndBackup(fConfig, "Tag.Sign.SignToJoin", "[JOIN]");
    setConfigAndBackup(fConfig, "Tag.Sign.SignToLeave", "[LEAVE]");
    
    // Economy
    setConfigAndBackup(fConfig, "Tag.Rewards.Money", Integer.valueOf(10));
    
    // Arenas
    setConfigAndBackup(fConfig, "Tag.Arena", "");
    
    // Externalise strings
    setConfigAndBackup(fConfig, "Tag.Strings.NotifyPlayersGameStart", "This game of Tag is starting in %d seconds!");
    setConfigAndBackup(fConfig, "Tag.Strings.LastPersonStanding", "Congratulations! You are the last person standing!");
    setConfigAndBackup(fConfig, "Tag.Strings.NotInGame", "You are not in a Tag game!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerNotWinner", "You cannot retrieve someone else's reward!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerClaimedReward", "You have already claimed your reward!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerTagPlayer", "Congratulations! You have tagged %s, they will be warped away. You are one step closer to winning!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerTaggedByPlayer", "You have been tagged by %s. That means it is gameover for you. Better luck next time!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerVote", "You have successfully voted.");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerAlreadyVote", "You have already voted. Please wait for the others to vote before the game starts!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerGetUpgrade", "With great power, comes great responsibility!");
    setConfigAndBackup(fConfig, "Tag.Strings.UpgradeNotReady", "Be patient, it will be ready soon!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerCannotTeleport", "You cannot teleport while you are in a Tag game. To leave this game, use /tag leave.");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerCannotChangeGameMode", "You cannot change your game mode while you are in a Tag game. To leave this game, use /tag leave.");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerHasLeft", "%s has left the game.");
    setConfigAndBackup(fConfig, "Tag.Strings.GameStart", "[Tag] A new game of Tag has been started in the arena %s. Please wait for this game to end before joining a new one.");
    setConfigAndBackup(fConfig, "Tag.Strings.GameStop", "[Tag] This game of Tag has ended in the arena %s! You may now join a new game.");
    setConfigAndBackup(fConfig, "Tag.Strings.BroadcastWinner", "[Tag] %s has defeated all their foes to win this game of Tag!");
    setConfigAndBackup(fConfig, "Tag.Strings.ConsoleUser", "This command cannot be used from the console!");
    setConfigAndBackup(fConfig, "Tag.Strings.AlreadyInLobby", "You are already in the Lobby, please vote to start the game!");
    setConfigAndBackup(fConfig, "Tag.Strings.AlreadyInActiveGame", "You are already in an active game of Tag. To leave this game, use /tag leave.");
    setConfigAndBackup(fConfig, "Tag.Strings.AlreadyActiveGameWait", "There is already a game of Tag running. Please wait for it to end before joining a new game.");
    setConfigAndBackup(fConfig, "Tag.Strings.InventorySaved", "Your inventory has been saved!");
    setConfigAndBackup(fConfig, "Tag.Strings.AlreadyActive", "There is already a game of Tag running.");
    setConfigAndBackup(fConfig, "Tag.Strings.NotActive", "There is no game of Tag running.");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerChangeGameMode", "Changing your game mode to survival.");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerWarpLobby", "You have been warped to the Lobby.");
    setConfigAndBackup(fConfig, "Tag.Strings.GameIsFull", "This current game of Tag is already full. Please wait until it ends before rejoining.");
    setConfigAndBackup(fConfig, "Tag.Strings.NoPerm", "You do not have the proper permissions to execute this command!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerNotInGame", "You are not in a Tag game.");
    setConfigAndBackup(fConfig, "Tag.Strings.GetUpgrade", "Upgrades are now active!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerLeaves", "You have left the Tag game. You will be warped back to your last known location.");
    setConfigAndBackup(fConfig, "Tag.Strings.MinPlayers", "There need to be at least two players to start a game of Tag.");
    setConfigAndBackup(fConfig, "Tag.Strings.Grace", "You now have %d seconds to get into position. Good luck!");
    setConfigAndBackup(fConfig, "Tag.Strings.NoGrace", "The grace period is over!");
    setConfigAndBackup(fConfig, "Tag.Strings.ArenaAdded", "The warp was successfully added!");
    setConfigAndBackup(fConfig, "Tag.Strings.SetSyntax", "Invalid syntax: /tag set [arena | lobby | rip] [arena name].");
    setConfigAndBackup(fConfig, "Tag.Strings.JoinSyntax", "Invalid syntax: /tag join [arena name].");
    setConfigAndBackup(fConfig, "Tag.Strings.CreateSyntax", "Invalid syntax: /tag createarena [arena name].");
    setConfigAndBackup(fConfig, "Tag.Strings.StartSyntax", "Invalid Syntax: /tag start [arena name]");
    setConfigAndBackup(fConfig, "Tag.Strings.StatusSyntax", "Invalid Syntax: /tag status [arena name]");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerScore", "%s has won %d time(s) and lost %s time(s)!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerNoScore", "%s has not played a game of Tag before!");
    setConfigAndBackup(fConfig, "Tag.Strings.IllegalCommandUsed", "You are not allowed to use this command while in a game of Tag!");
    setConfigAndBackup(fConfig, "Tag.Strings.WrongServer", "The Tag-Minigame Arena is not on this server!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerReceiveMoney", "You have been awarded %s currency points for winning! Don't spend it all in one place!");
    setConfigAndBackup(fConfig, "Tag.Strings.ArenaDoesNotExist", "This arena does not exist, please use /tag createarena [arena name] to create one!");
    setConfigAndBackup(fConfig, "Tag.Strings.ArenaCreated", "The arena has been created!");
    setConfigAndBackup(fConfig, "Tag.Strings.ArenaAlreadyExists", "This arena already exists.");
    setConfigAndBackup(fConfig, "Tag.Strings.ArenasList", "Available arenas: %s");
    setConfigAndBackup(fConfig, "Tag.Strings.AreNoArenas", "There are no arenas!");
    setConfigAndBackup(fConfig, "Tag.Strings.ArenaNotSet", "This arena does not have all its warps set!");
    setConfigAndBackup(fConfig, "Tag.Strings.PlayerDiesInArena", "You have died while in a game of Tag! You will be warped to Tag RIP for the duration of the game.");
    setConfigAndBackup(fConfig, "Tag.Strings.TpToTag", "You cannot teleport to a Tag player!");
    
    save(false);

  }
  
  public static void set(String path, String value) {
    setConfigAndBackup(fConfig, path, value);
    save(true);
  }
  
  
  private static void save(boolean overwrite) {
    try {
      File configFile = new File(ConfigFile.getConfigPath() + File.separator + ConfigFile.CONFIG);
      
      if(overwrite || !configFile.exists())
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
    } catch (IOException | InvalidConfigurationException e) {
      return backup.get(path);
    }
  }
  
  /**
   * Add a backup of the values and save it to memory in case the config file cannot be found.
   * @param fConfig
   * @param path
   * @param value
   */
  private static void setConfigAndBackup(FileConfiguration fConfig, String path, Object value) {
    fConfig.set(path, value);
    
    if(!backup.containsKey(path))
      backup.put(path, value.toString());
    else
      backup.replace(path, value.toString());
  }
  
  public static FileConfiguration getFileConfig() {
    return fConfig;
  }
  
}