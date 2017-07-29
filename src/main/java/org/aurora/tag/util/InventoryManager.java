package org.aurora.tag.util;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/**
 * 
 * @author RussianMushroom
 *
 */
public class InventoryManager {
	
	private static Material baton = Material.STICK;
	private static Color armourColour = Color.fromRGB(50, 50, 50);

	public static void clearPlayerInventory() {
		TagManager.getVotedPlayers().forEach(player -> {
			player.getInventory().clear();
		});
	}
	
	public static boolean isEmpty(Player player) {
		return player.getInventory().getContents().length == 0;
	}
	
	public static void setTagBaton() {
		baton = Material.valueOf(ConfigLoader.getDefault("Tag.Tools.Baton"));
		ItemStack iStack = new ItemStack(baton);
		
		iStack.getItemMeta().setDisplayName("Baton");
		
		TagManager.getVotedPlayers().forEach(player -> {
			player.getInventory().setItem(0, iStack);
		});
	}
	
	// FIXME
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
}
