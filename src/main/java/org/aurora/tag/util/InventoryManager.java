package org.aurora.tag.util;

import java.util.Arrays;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/**
 * 
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
					if(iStack == null || iStack.getType() == Material.AIR)
						return false;
					else
						return true;
				})
				.count() == 0;
	}
	
	public static void setTagBaton() {
		Material baton = Material.valueOf(ConfigLoader.getDefault("Tag.Tools.Baton"));
		ItemStack iStack = new ItemStack(baton);
		
		iStack.getItemMeta().setLocalizedName("Baton");
		
		TagManager.getVotedPlayers().forEach(player -> {
			player.getInventory().addItem(iStack);
		});
	}
	
	// FIXME
	/*
	private static void setArmor() {
		Color armourColour = Color.fromRGB(
				Integer.parseInt(ConfigLoader.getDefault("Tag.Armour.Colour.R")),
				Integer.parseInt(ConfigLoader.getDefault("Tag.Armour.Colour.G")),
				Integer.parseInt(ConfigLoader.getDefault("Tag.Armour.Colour.B"))
				);
		
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
		
		TagManager.getVotedPlayers().forEach(player -> {
			player.getInventory().setHelmet(helm);
			player.getInventory().setChestplate(chest);
			player.getInventory().setLeggings(leg);
			player.getInventory().setBoots(boot);
		});
	}
	*/
	
	public static void setTagBow() {
		Material bow = Material.BOW;
		Material arrow = Material.ARROW;
		
		TagManager.getVotedPlayers().forEach(player -> {
			player.getInventory().addItem(new ItemStack(bow));
			player.getInventory().addItem(new ItemStack(
					arrow, 
					Integer.parseInt(ConfigLoader.getDefault("Tag.Tools.ArrowCount"))));
		});
		
	}
}
