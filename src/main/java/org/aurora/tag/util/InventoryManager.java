package org.aurora.tag.util;

import java.util.Arrays;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.GameCenter;
import org.aurora.tag.game.TagArena;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import io.netty.util.internal.ThreadLocalRandom;


/**
 * Handles the player's inventory.
 * @author RussianMushroom
 *
 */
public class InventoryManager {
	
	public static void clearPlayerInventory(boolean stopping, String arena) {
		GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
			if(stopping) {
				// clear the game inventory and load player's saved inventory
				player.getInventory().clear();
				player.getInventory().setContents(
						GameCenter.getArena(arena).getPlayerInv().get(player));
			} else {
				// Save player's inventory
				GameCenter.getArena(arena).addPlayerInv(
						player, player.getInventory().getContents());
				player.getInventory().clear();
			}
		});
	}
	
	public static void restoreInv(Player player, TagArena arena) {
		player.getInventory().clear();
		player.getInventory().setContents(
				arena.getPlayerInv().get(player));
	}
	
	public static void setTagBaton(String arena) {
		Material baton = Material.valueOf(ConfigLoader.getDefault("Tag.Tools.Baton"));
		ItemStack iStack = new ItemStack(baton);
		ItemMeta iMeta = iStack.getItemMeta();
		
		iMeta.setDisplayName(ConfigLoader.getDefault("Tag.Tools.BatonName"));
		iStack.setItemMeta(iMeta);
		
		GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
			player.getInventory().addItem(iStack);
		});
	}
	
	public static void setArmour(String arena) {
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
		
		GameCenter.getArena(arena).getVotedPlayers().forEach(player -> {
			player.getInventory().setArmorContents(iStack);
		});
	}

	public static void setUpgrade(Player player) {
		ItemStack bow = new ItemStack(Material.BOW),
				arrow = new ItemStack(Material.ARROW, 
						Integer.parseInt(ConfigLoader.getDefault("Tag.Tools.ArrowCount"))),
				healPotion = new ItemStack(Material.LINGERING_POTION),
				swiftPotion = new ItemStack(Material.LINGERING_POTION),
				jumpPotion = new ItemStack(Material.LINGERING_POTION);
				// blindPotion = new ItemStack(Material.LINGERING_POTION);
		
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
		
		/*
		PotionMeta blindMeta = (PotionMeta) blindPotion.getItemMeta();
		blindMeta.setMainEffect(PotionEffectType.BLINDNESS);
		blindPotion.setItemMeta(blindMeta);
		*/
		
		// Add items to an array
		ItemStack[] items = new ItemStack[] {bow, arrow, healPotion, swiftPotion, jumpPotion /*, blindPotion*/};
		ItemStack upgrade = items[randomIndexGenerator(items.length)];
		
		if(upgrade.getType() == Material.BOW) {
			player.getInventory().addItem(arrow);
			
			if(player.getInventory().contains(Material.BOW)) 
				upgrade = arrow;
		}
		
			player.getInventory().addItem(upgrade);
	}
	
	/**
	 * Returns a random number between 0 and the specified <code>maxSize</code>.
	 * As it is being used for arrays, the number is adjusted to fit zero-indexed arrays. 
	 * (Add 1 to the result to counteract this)  
	 * @param maxSize
	 * @return
	 */
	public static int randomIndexGenerator(int maxSize) {
		return ThreadLocalRandom.current().nextInt(0, maxSize - 1); 
	}
	
}