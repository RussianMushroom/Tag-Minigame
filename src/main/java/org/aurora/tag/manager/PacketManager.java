package org.aurora.tag.manager;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

/**
 * General packet manager
 * @author RussianMushroom
 *
 */
public class PacketManager {
	
	private static ProtocolManager pManager = ProtocolLibrary.getProtocolManager();
	
	public static void displayHologram(String title, String subtitle, Player player) {
		final PacketContainer pTitleContainer = pManager.createPacket(PacketType.Play.Server.TITLE);
		final PacketContainer pSubtitleContainer = pManager.createPacket(PacketType.Play.Server.TITLE);
		
		pTitleContainer.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
		pSubtitleContainer.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
		
		pTitleContainer.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.GOLD + title));
		pSubtitleContainer.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.YELLOW + subtitle));
		
		try {
			pManager.sendServerPacket(player, pTitleContainer);
			pManager.sendServerPacket(player, pSubtitleContainer);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	// WIP
	public static void displayCooldown(int id, int cooldown, Player player) {
		final PacketContainer pCooldownContainer = pManager.createPacket(PacketType.Play.Server.SET_COOLDOWN);
		
		pCooldownContainer.getIntegers().write(0, id);
		pCooldownContainer.getIntegers().write(0, cooldown);
		
		try {
			pManager.sendServerPacket(player, pCooldownContainer);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
