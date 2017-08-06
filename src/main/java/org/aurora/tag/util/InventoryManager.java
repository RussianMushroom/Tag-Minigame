package org.aurora.tag.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import io.netty.util.internal.ThreadLocalRandom;


/**
 * Handles the player's inventory.
 * @author RussianMushroom
 *
 */
public class InventoryManager {

	public static void clearPlayerInventory() {
		TagManager.getVotedPlayers().forEach(player -> {
			player.getInventory().clear();
		});
	}
	
	public static boolean isEmpty(Player player) {
		return Arrays.asList(player.getInventory().getContents())
				.stream()
				.filter(iStack -> {
					return (iStack == null || iStack.getType() == Material.AIR) ? false : true;
				})
				.count() == 0;
	}
	
	public static void setTagBaton() {
		Material baton = Material.valueOf(ConfigLoader.getDefault("Tag.Tools.Baton"));
		ItemStack iStack = new ItemStack(baton);
		ItemMeta iMeta = iStack.getItemMeta();
		
		iMeta.setDisplayName(ConfigLoader.getDefault("Tag.Tools.BatonName"));
		iStack.setItemMeta(iMeta);
		
		TagManager.getVotedPlayers().forEach(player -> {
			player.getInventory().addItem(iStack);
		});
	}
	
	public static void setArmour() {
		Color armourColour = Color.BLUE;
		ItemStack[] iStack = new ItemStack[] {
				new ItemStack(Material.LEATHER_BOOTS),
				new ItemStack(Material.LEATHER_LEGGINGS),
				new ItemStack(Material.LEATHER_CHESTPLATE),
				new ItemStack(Material.LEATHER_HELMET)
		};
		
		Arrays.asList(iStack).forEach(stack -> {
			LeatherArmorMeta lMeta = (LeatherArmorMeta) stack.getItemMeta();
			
			lMeta.setColor(armourColour);
			stack.setItemMeta(lMeta);
		});
		
		TagManager.getVotedPlayers().forEach(player -> {
			player.getInventory().setArmorContents(iStack);
		});
	}

	
	@SuppressWarnings("deprecation")
	public static void setUpgrade(Player player) {
		ItemStack bow = new ItemStack(Material.BOW),
				arrow = new ItemStack(Material.BOW, Integer.parseInt(ConfigLoader.getDefault("Tag.Tools.ArrowCount"))),
				healPotion = new ItemStack(Material.LINGERING_POTION),
				swiftPotion = new ItemStack(Material.LINGERING_POTION),
				jumpPotion = new ItemStack(Material.LINGERING_POTION),
				blindPotion = new ItemStack(Material.LINGERING_POTION);
		
		// Add meta data to the potions
		PotionMeta healMeta = (PotionMeta) healPotion.getItemMeta();
		healMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL,
				false, false));
		healPotion.setItemMeta(healMeta);
		
		PotionMeta speedMeta = (PotionMeta) swiftPotion.getItemMeta();
		speedMeta.setBasePotionData(new PotionData(PotionType.SPEED,
				false, false));
		swiftPotion.setItemMeta(speedMeta);
		
		PotionMeta jumpMeta = (PotionMeta) jumpPotion.getItemMeta();
		jumpMeta.setBasePotionData(new PotionData(PotionType.JUMP,
				false, false));
		jumpPotion.setItemMeta(jumpMeta);
		
		PotionMeta blindMeta = (PotionMeta) blindPotion.getItemMeta();
		blindMeta.setMainEffect(PotionEffectType.BLINDNESS);
		blindPotion.setItemMeta(blindMeta);
		
		// Add items to an array
		ItemStack[] items = new ItemStack[]{bow, arrow, healPotion, swiftPotion, jumpPotion, blindPotion};
		ItemStack upgrade = items[randomIndexGenerator(items.length)];
		
		// Check if the Player already has a bow, if they do, then give them something else.
		while(player.getInventory().contains(Material.BOW) && upgrade.getType() == Material.BOW) {
			upgrade = items[randomIndexGenerator(items.length)];
		}
		
		player.getInventory().addItem(upgrade);
	}
	
	public static void setWinnerReward(Player player) {  
		ItemStack playerReward;
		
		// Get the skull of a random player
		// ItemStack playerRewardSkull = new ItemStack(Material.SKULL_ITEM);
		// SkullMeta sMeta = (SkullMeta) playerRewardSkull.getItemMeta();
		// sMeta.setOwner(TagManager.getRandomPlayer(TagManager.getRipPlayers()).getName());
		// playerRewardSkull.setItemMeta(sMeta);
		
		List<Enchantment> swordEnchantments = new ArrayList<>(Arrays.asList(
				Enchantment.SWEEPING_EDGE,
				Enchantment.KNOCKBACK,
				Enchantment.DAMAGE_ARTHROPODS,
				Enchantment.FIRE_ASPECT
				));
		List<Enchantment> bowEnchantments = new ArrayList<>(Arrays.asList(
				Enchantment.ARROW_INFINITE,
				Enchantment.ARROW_KNOCKBACK,
				Enchantment.ARROW_FIRE
				));
		
		ItemStack[] rewardStack = new ItemStack[]{
				new ItemStack(Material.BOW),
				new ItemStack(Material.DIAMOND, randomIndexGenerator(10) + 1),
				new ItemStack(Material.ARROW, randomIndexGenerator(64) + 1),
				new ItemStack(Material.BLUE_SHULKER_BOX),
				new ItemStack(Material.SHIELD),
				new ItemStack(Material.DIAMOND_SWORD),
				new ItemStack(Material.IRON_SWORD),
				new ItemStack(Material.GOLD_SWORD)
		};
		
		playerReward = rewardStack[randomIndexGenerator(rewardStack.length)];
		
		// Set proper enchantments if the item is a sword or bow
		if(playerReward.getType() == Material.BOW) {
			ItemMeta bowMeta = playerReward.getItemMeta();
			bowMeta.addEnchant(
					bowEnchantments.get(randomIndexGenerator(bowEnchantments.size())),
					1, true);
			playerReward.setItemMeta(bowMeta);
		} else if (playerReward.getType().name().toLowerCase().contains("sword")) {
			ItemMeta swordMeta = playerReward.getItemMeta();
			swordMeta.addEnchant(
					swordEnchantments.get(randomIndexGenerator(swordEnchantments.size())),
					1, true);
			playerReward.setItemMeta(swordMeta);
		}
		
		// player.getInventory().addItem(playerRewardSkull);
		int moneyReward = Integer.parseInt(ConfigLoader.getDefault("Tag.Rewards.Money"));
		if(Bukkit.getServer().getPluginManager().getPlugin("Essentials") != null)
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
					"economy give" + player + moneyReward);
		
		player.getInventory().addItem(playerReward);
	}
	
	/**
	 * Returns a random number between 0 and the specified <code>maxSize</code>.
	 * As it is being used for arrays, the number is adjusted to fit zero-indexed arrays. 
	 * (Add 1 to the result to counteract this)  
	 * @param maxSize
	 * @return
	 */
	private static int randomIndexGenerator(int maxSize) {
		return ThreadLocalRandom.current().nextInt(0, maxSize - 1); 
	}
}