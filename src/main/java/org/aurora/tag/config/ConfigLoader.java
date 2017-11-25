package org.aurora.tag.config;

import java.io.File;
import java.io.IOException;

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
  
  public static void load(Tag tag) {
    Tag plugin = tag;
    fConfig = plugin.getConfig();
    
    // Default arenas
    // These are set by /tag set arena|rip|lobby - check if they exists upon enable
    // setConfigAndBackup(fConfig, "Tag.Arena.Lobby", "");
    // setConfigAndBackup(fConfig, "Tag.Arena.Arena", "");
    // setConfigAndBackup(fConfig, "Tag.Arena.Rip", "");
                            
    // Time limits          
    fConfig.set("Tag.Timer.TicksBeforeTagStart", Integer.valueOf(140));
    fConfig.set("Tag.Timer.TicksBeforeUpgrade", Integer.valueOf(400));
    fConfig.set("Tag.Timer.GracePeriod", Integer.valueOf(1000));
    
    // Allowed weapons    
    fConfig.set("Tag.Tools.Baton", "STICK");
    fConfig.set("Tag.Tools.BlindPotionName", "I can't see a thing.");
    fConfig.set("Tag.Tools.SpeedPotionName", "I've got speed!");
    fConfig.set("Tag.Tools.JumpPotionName", "How low can you go?");
    fConfig.set("Tag.Tools.BatonName", "Baton");
    fConfig.set("Tag.Tools.ArrowCount", Integer.valueOf(1));
    fConfig.set("Tag.Tools.ChanceToGetPumpkin", Double.valueOf(0.1));
    fConfig.set("Tag.Tools.ChanceToGetEasterEgg", Double.valueOf(0.05));
    
    // Sign text
    fConfig.set("Tag.Sign.SignToVote", "[VOTE]");
    fConfig.set("Tag.Sign.SignToUpgrade", "[UPGRADE]");
    fConfig.set("Tag.Sign.SignToJoin", "[JOIN]");
    fConfig.set("Tag.Sign.SignToLeave", "[LEAVE]");
    
    // Economy
    fConfig.set("Tag.Rewards.Money", Integer.valueOf(10));
    fConfig.set("Tag.Rewards.AllowRewardsRelativeToPlayerAmount", Boolean.valueOf(false));
    fConfig.set("Tag.Rewards.AmountOfCreditsPerGame", 1);
    
    // Misc
    fConfig.set("Tag.Options.AllowHolographicCountdown", Boolean.valueOf(false));
    fConfig.set("Tag.Options.GameModeAfterFinishGame", "SURVIVAL");
    fConfig.set("Tag.Options.GameModeWhileInRIP", "SPECTATOR");
    
    // Externalise strings
    fConfig.set("Tag.Strings.NotifyPlayersGameStart", "This game of Tag is starting in %d seconds!");
    fConfig.set("Tag.Strings.LastPersonStanding", "Congratulations! You are the last person standing!");
    fConfig.set("Tag.Strings.NotInGame", "You are not in a Tag game!");
    fConfig.set("Tag.Strings.PlayerTagPlayer", "Congratulations! You have tagged %s, they will be warped away. You are one step closer to winning!");
    fConfig.set("Tag.Strings.PlayerTaggedByPlayer", "You have been tagged by %s. That means it is gameover for you. Better luck next time!");
    fConfig.set("Tag.Strings.PlayerVote", "You have successfully voted.");
    fConfig.set("Tag.Strings.PlayerAlreadyVote", "You have already voted. Please wait for "
    		+ "the others to vote before the game starts!");
    fConfig.set("Tag.Strings.PlayerGetUpgrade", "With great power, comes great responsibility!");
    fConfig.set("Tag.Strings.UpgradeNotReady", "Be patient, it will be ready soon!");
    fConfig.set("Tag.Strings.PlayerCannotTeleport", "You cannot teleport while you are in a Tag game. To leave this game, use /tag leave.");
    fConfig.set("Tag.Strings.PlayerCannotChangeGameMode", "You cannot change your game mode while you are in a Tag game. To leave this game, use /tag leave.");
    fConfig.set("Tag.Strings.PlayerHasLeft", "You have left the game.");
    fConfig.set("Tag.Strings.GameStart", "[Tag] A new game of Tag has been started in the arena %s. Please wait for this game to end before joining a new one.");
    fConfig.set("Tag.Strings.GameStop", "[Tag] This game of Tag has ended in the arena %s! You may now join a new game.");
    fConfig.set("Tag.Strings.BroadcastWinner", "[Tag] %s has defeated all their foes to win this game of Tag!");
    fConfig.set("Tag.Strings.ConsoleUser", "This command cannot be used from the console!");
    fConfig.set("Tag.Strings.AlreadyInLobby", "You are already in the Lobby, please vote to start the game!");
    fConfig.set("Tag.Strings.AlreadyInActiveGame", "You are already in an active game of Tag. To leave this game, use /tag leave.");
    fConfig.set("Tag.Strings.AlreadyActiveGameWait", "There is already a game of Tag running. Please wait for it to end before joining a new game.");
    fConfig.set("Tag.Strings.InventorySaved", "Your inventory has been saved!");
    fConfig.set("Tag.Strings.AlreadyActive", "There is already a game of Tag running.");
    fConfig.set("Tag.Strings.NotActive", "There is no game of Tag running.");
    fConfig.set("Tag.Strings.PlayerChangeGameMode", "Changing your game mode to survival.");
    fConfig.set("Tag.Strings.PlayerWarpLobby", "You have been warped to the Lobby.");
    fConfig.set("Tag.Strings.GameIsFull", "This t game of Tag is already full. Please wait until it ends before rejoining.");
    fConfig.set("Tag.Strings.NoPerm", "You do not have the proper permissions to execute this command!");
    fConfig.set("Tag.Strings.PlayerNotInGame", "You are not in a Tag game.");
    fConfig.set("Tag.Strings.GetUpgrade", "Upgrades are now active!");
    fConfig.set("Tag.Strings.PlayerLeaves", "You have left the Tag game. You will be warped back to your last known location.");
    fConfig.set("Tag.Strings.MinPlayers", "There need to be at least two players to start a game of Tag.");
    fConfig.set("Tag.Strings.Grace", "You now have %d seconds to get into position. Good luck!");
    fConfig.set("Tag.Strings.GracePeriodEndsIn", "The grace period ends in");
    fConfig.set("Tag.Strings.NoGrace", "The grace period is over!");
    fConfig.set("Tag.Strings.GracePeriodGoodLuck", "Good luck!");
    fConfig.set("Tag.Strings.ArenaAdded", "The warp was successfully added!");
    fConfig.set("Tag.Strings.SetSyntax", "Invalid syntax: /tag set [arena | lobby | rip] [arena name].");
    fConfig.set("Tag.Strings.JoinSyntax", "Invalid syntax: /tag join [arena name].");
    fConfig.set("Tag.Strings.CreateSyntax", "Invalid syntax: /tag createarena [arena name].");
    fConfig.set("Tag.Strings.StartSyntax", "Invalid syntax: /tag start [arena name]");
    fConfig.set("Tag.Strings.StatusSyntax", "Invalid syntax: /tag status [arena name]");
    fConfig.set("Tag.Strings.PlayerScore", "%s has won %d time(s) and lost %s time(s)!");
    fConfig.set("Tag.Strings.PlayerNoScore", "%s has not played a game of Tag before!");
    fConfig.set("Tag.Strings.IllegalCommandUsed", "You are not allowed to use this command while in a game of Tag!");
    fConfig.set("Tag.Strings.WrongServer", "The Tag-Minigame Arena is not on this server!");
    fConfig.set("Tag.Strings.NotifyAmountToWin", "In this game there is %d currency up for grabs!");
    fConfig.set("Tag.Strings.PlayerReceiveMoney", "You have been awarded %s currency points for winning! Don't spend it all in one place!");
    fConfig.set("Tag.Strings.ArenaDoesNotExist", "This arena does not exist, please use /tag createarena [arena name] to create one!");
    fConfig.set("Tag.Strings.ArenaCreated", "The arena has been created!");
    fConfig.set("Tag.Strings.ArenaAlreadyExists", "This arena already exists.");
    fConfig.set("Tag.Strings.ArenasList", "Available arenas: %s");
    fConfig.set("Tag.Strings.AreNoArenas", "There are no arenas!");
    fConfig.set("Tag.Strings.ArenaNotSet", "This arena does not have all its warps set!");
    fConfig.set("Tag.Strings.PlayerDiesInArena", "You have died while in a game of Tag! You will be warped to Tag RIP for the duration of the game.");
    fConfig.set("Tag.Strings.TpToTag", "You cannot teleport to a Tag player!");
    fConfig.set("Tag.Strings.PlayerTagPlayer", "%s has been tagged by %s.");
    fConfig.set("Tag.Strings.PlayerLeaves", "%s has left this game of Tag!");
    fConfig.set("Tag.Strings.PlayerDiesByCactus", "%s has been stabbed to death by a cactus! Brutal!");
    fConfig.set("Tag.Strings.PlayerDiesByDrowning", "%s thought they were a fish!");
    fConfig.set("Tag.Strings.PlayerDiesByFalling", "%s fell from the stars!");
    fConfig.set("Tag.Strings.PlayerDiesByBurning", "%s played with fire and lost!");
    
    save(false);

  }
  
  public static void set(String path, Object value) {
    fConfig.set(path, value);
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
      
      return fConfig.getString(path); 
    } catch (IOException | InvalidConfigurationException e) {
    	Bukkit.getServer().getLogger().severe(path + " could not be found in the config file! Make sure it has been created properly.");
    	return "";
    }
  }
  
  public static FileConfiguration getFileConfig() {
    return fConfig;
  }
  
}