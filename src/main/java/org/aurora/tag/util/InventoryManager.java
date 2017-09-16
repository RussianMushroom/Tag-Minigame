package org.aurora.tag.util;

import java.util.Arrays;
import java.util.Random;

import org.aurora.tag.config.ConfigLoader;
import org.aurora.tag.game.TagArena;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import io.netty.util.internal.ThreadLocalRandom;


/**
 * Handles the player's inventory.
 * @author RussianMushroom
 *
 */
public class InventoryManager {
	
	public static void clearPlayerInventory(boolean stopping, TagArena arena) {
		
		arena.getVotedPlayers().forEach(player -> {
			player.getInventory().clear();
		});
			/*
			if(stopping) {
				// clear the game inventory and load player's saved inventory
				player.getInventory().clear();
				
				ItemStack[] playerInv = GameCenter.getArena(arena).getPlayerInv().get(player);
				if(playerInv != null)
					player.getInventory().setContents(playerInv);
			} else {
				// Save player's inventory
				GameCenter.getArena(arena).addPlayerInv(
						player, player.getInventory().getContents());
				player.getInventory().clear();
			}
		});
		*/
	}
	
	public static void restoreInv(Player player, TagArena arena) {
		player.getInventory().clear();
		/*
		player.getInventory().clear();
		player.getInventory().setContents(
				arena.getPlayerInv().get(player));
				*/
	}
	
	public static void setTagBaton(TagArena arena) {
		Material baton = Material.valueOf(ConfigLoader.getDefault("Tag.Tools.Baton"));
		ItemStack iStack = new ItemStack(baton);
		ItemMeta iMeta = iStack.getItemMeta();
		
		iMeta.setDisplayName(ConfigLoader.getDefault("Tag.Tools.BatonName"));
		iStack.setItemMeta(iMeta);
		
		arena.getVotedPlayers().forEach(player -> {
			player.getInventory().addItem(iStack);
		});
	}
	
	public static void setArmour(TagArena arena) {
		Color armourColour = Color.BLUE;
		Color customArmourColour = Color.ORANGE;
		ItemStack[] iStack = new ItemStack[] {
				new ItemStack(Material.LEATHER_BOOTS),
				new ItemStack(Material.LEATHER_LEGGINGS),
				new ItemStack(Material.LEATHER_CHESTPLATE),
				new ItemStack(Material.LEATHER_HELMET)
		};
		setItemMeta(iStack, armourColour);
		
		ItemStack[] customItemStack = new ItemStack[] {
				new ItemStack(Material.LEATHER_BOOTS),
				new ItemStack(Material.LEATHER_LEGGINGS),
				new ItemStack(Material.LEATHER_CHESTPLATE),
				new ItemStack(Material.PUMPKIN)
		};
		setItemMeta(customItemStack, customArmourColour);
		
		arena.getVotedPlayers().forEach(player -> {
			Random rand = new Random();
			double chance = Double.parseDouble(
					ConfigLoader.getDefault("Tag.Tools.ChanceToGetPumpkin"));
			if(chance != 0d && rand.nextDouble() < chance)
				player.getInventory().setArmorContents(customItemStack);
			else
				player.getInventory().setArmorContents(iStack);
		});
	}
	
	private static void setItemMeta(ItemStack[] items, Color colour) {
		// in this specific case
		Arrays.asList(items).forEach(stack -> {
			if(stack.getType() != Material.PUMPKIN) {
				LeatherArmorMeta lMeta = (LeatherArmorMeta) stack.getItemMeta();
				
				lMeta.setColor(colour);
				stack.setItemMeta(lMeta);
			}
		});
	}

	public static void setUpgrade(Player player) {
		Random rand = new Random();
		Double configChance = Double.parseDouble(
				ConfigLoader.getDefault("Tag.Tools.ChanceToGetEasterEgg"));
		ItemStack bow = new ItemStack(Material.BOW),
				arrow = new ItemStack(Material.ARROW, 
						Integer.parseInt(ConfigLoader.getDefault("Tag.Tools.ArrowCount"))),
				// healPotion = new ItemStack(Material.LINGERING_POTION),
				swiftPotion = new ItemStack(Material.LINGERING_POTION),
				jumpPotion = new ItemStack(Material.LINGERING_POTION),
				blindPotion = new ItemStack(Material.LINGERING_POTION);
		
		// Add meta data to the potions
		// PotionMeta healMeta = (PotionMeta) healPotion.getItemMeta();
		// healMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL,
		//		false, false));
		// healPotion.setItemMeta(healMeta);
		
		PotionMeta speedMeta = (PotionMeta) swiftPotion.getItemMeta();
		speedMeta.setBasePotionData(new PotionData(PotionType.SPEED,
				false, false));
		if(rand.nextDouble() < configChance)
			speedMeta.setDisplayName(ConfigLoader.getDefault("Tag.Tools.SpeedPotionName"));
		swiftPotion.setItemMeta(speedMeta);
		
		PotionMeta jumpMeta = (PotionMeta) jumpPotion.getItemMeta();
		jumpMeta.setBasePotionData(new PotionData(PotionType.JUMP,
				false, false));
		if(rand.nextDouble() < configChance)
			jumpMeta.setDisplayName(ConfigLoader.getDefault("Tag.Tools.JumpPotionName"));
		jumpPotion.setItemMeta(jumpMeta);
		
		
		PotionMeta blindMeta = (PotionMeta) blindPotion.getItemMeta();
		blindMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 1), true);
		if(rand.nextDouble() < configChance)
			blindMeta.setDisplayName(ConfigLoader.getDefault("Tag.Tools.BlindPotionName"));
		blindPotion.setItemMeta(blindMeta);
		
		
		// Add items to an array
		ItemStack[] items = new ItemStack[] {bow, arrow, blindPotion, swiftPotion, jumpPotion /*, blindPotion*/};
		ItemStack upgrade = items[randomIndexGenerator(items.length)];
		
		if(upgrade.getType() == Material.BOW) {
			if(player.getInventory().contains(Material.BOW)) {
				while(upgrade.getType() == Material.BOW) {
					upgrade = items[randomIndexGenerator(items.length)];
				}
			} else	
				player.getInventory().addItem(arrow);
		} else if(upgrade.getType() == Material.ARROW && !player.getInventory().contains(Material.BOW))
			player.getInventory().addItem(bow);
		
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