package org.aurora.tag.util;

import java.util.Arrays;

import org.aurora.tag.TagManager;
import org.aurora.tag.config.ConfigLoader;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;


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
	
	public static void setTagBow(Player player) {
		Material bow = Material.BOW;
		Material arrow = Material.ARROW;
		
		if(!Arrays.asList(player.getInventory().getContents()).contains(new ItemStack(bow)))
			player.getInventory().addItem(new ItemStack(bow));
		player.getInventory().addItem(new ItemStack(
				arrow, 
				Integer.parseInt(ConfigLoader.getDefault("Tag.Tools.ArrowCount"))));
	}
}
